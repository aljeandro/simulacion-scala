
package conexion

import fisica.Velocidad
import geometria.{Angulo, Punto}
import grafo.GrafoVia
import org.neo4j.driver.v1._

import scala.collection.JavaConversions._
import infraestructura.Interseccion
import infraestructura.via.{Sentido, TipoVia, Via}
import simulacion.Simulacion
import vehiculo._

import scala.collection.mutable.{ArrayBuffer, Queue}

object Conexion {

  val url = "bolt://localhost/7687"
  val user = "neo4j" //Usuario por defecto
  val pass = "1234" //Pass de la bd activa


  def getSession: (Driver, Session) = {
    val driver = GraphDatabase.driver(url, AuthTokens.basic(user, pass))
    val session = driver.session
    (driver, session)
  }

  def getIntersecciones(): Array[Interseccion] = {
    val (driver, session) = getSession

    val scriptNumIntersecciones = "MATCH (:Interseccion) " +
      "WITH COUNT(*) AS numIntersecciones " +
      "RETURN numIntersecciones"
    val resultNumIntersecciones = session.run(scriptNumIntersecciones)

    val numIntersecciones: Int = resultNumIntersecciones.next().values().get(0).asInt()

    val intersecciones: Array[Interseccion] = new Array[Interseccion](numIntersecciones)

    val scriptIntersecciones = "MATCH (intersecciones:Interseccion) RETURN intersecciones"
    val resultIntersecciones = session.run(scriptIntersecciones)

    for (index <- 0 until numIntersecciones if resultIntersecciones.hasNext) {
      val nodoInterseccion = resultIntersecciones.next().values().get(0)

      intersecciones(index) =
        new Interseccion(
          nodoInterseccion.get("longitud").asDouble(),
          nodoInterseccion.get("latitud").asDouble(),
          Some(nodoInterseccion.get("nombre").asString())
        )
    }
    session.close()
    driver.close()
    intersecciones
  }

  def getVias(): Array[Via] = {
    val (driver, session) = getSession

    val scriptNumVias = "MATCH (:Via) " +
      "WITH COUNT(*) AS numVias " +
      "RETURN numVias"
    val resultNumVias = session.run(scriptNumVias)

    val numVias: Int = resultNumVias.next().values().get(0).asInt()

    val vias: Array[Via] = new Array[Via](numVias)

    val scriptVias =
      "MATCH (origen:Interseccion)-[:ORIGEN_DE]->(via:Via)<-[:FIN_DE]-(fin:Interseccion) " +
        "RETURN origen, fin, via"
    val resultVias = session.run(scriptVias)

    for (index <- 0 until numVias if resultVias.hasNext) {
      val values = resultVias.next().values()
      val nodoOrigen = values.get(0)
      val nodoFin = values.get(1)
      val nodoVia = values.get(2)

      vias(index) =
        new Via(
          Simulacion.intersecciones.filter(
            interseccion => interseccion.nombre.get == nodoOrigen.get("nombre").asString()).head,
          Simulacion.intersecciones.filter(
            interseccion => interseccion.nombre.get == nodoFin.get("nombre").asString()).head,
          nodoVia.get("velocidadMax").asDouble(),
          TipoVia(nodoVia.get("tipoVia").asString()),
          if (nodoVia.get("sentido").asString() == "dobleVia") Sentido.dobleVia else Sentido.unaVia,
          nodoVia.get("numeroVia").asString(),
          Some(nodoVia.get("nombre").asString())
        )
    }
    session.close()
    driver.close()
    vias
  }

  def guardarViajesVehiculos(vehiculosViajes: Array[VehiculoViaje]): Unit = {
    val (driver, session) = getSession

    for (vehiculoViaje <- vehiculosViajes) {
      val scriptCreacionViaje: String =
        "CREATE (viaje:Viaje {" +
          s"identificadorViaActual:'${vehiculoViaje.viaActual.nombreIdentificador()}', " +
          s"vehiculoEnDestino:'${vehiculoViaje.vehiculoEnDestino}'}),"

      val scriptCreacionVehiculo: String =
        "(vehiculo:Vehiculo {" +
          s"posicionX:${vehiculoViaje.vehiculo.posicion.x}, " +
          s"posicionY:${vehiculoViaje.vehiculo.posicion.y}, " +
          s"tipo:'${vehiculoViaje.vehiculo.getClass}', " +
          s"placa:'${vehiculoViaje.vehiculo.placa}', " +
          s"magnitudVelocidad:${vehiculoViaje.vehiculo.velocidad.magnitud}, " +
          s"anguloVelocidad:${vehiculoViaje.vehiculo.velocidad.direccion.grados}}),"

      val scriptCreacionOrigen: String =
        "(origen:Origen {" +
          s"posicionX:${vehiculoViaje.origen.x}, " +
          s"posicionY:${vehiculoViaje.origen.y}, " +
          s"nombre:'${vehiculoViaje.origen.nombre.getOrElse("Sin nombre")}'}),"

      val scriptCreacionDestino: String =
        "(destino:Destino {" +
          s"posicionX:${vehiculoViaje.destino.x}, " +
          s"posicionY:${vehiculoViaje.destino.y}, " +
          s"nombre:'${vehiculoViaje.destino.nombre.getOrElse("Sin nombre")}'}),"

      val identificadoresViasPorRecorrer: String =
        vehiculoViaje.colaViasCamino.map(via => via.nombreIdentificador()).mkString("', '")

      val scriptCreacionCamino: String =
        s"(camino:Camino {identificadoresViasPorRecorrer:['$identificadoresViasPorRecorrer']}),"

      val scriptCreacionRelaciones: String =
        "(viaje)-[:TIENE_UN]->(vehiculo)," +
          "(viaje)-[:INICIA_EN]->(origen)," +
          "(viaje)-[:TERMINA_EN]->(destino)," +
          "(viaje)-[:RECORRE_UN]->(camino)"

      val scriptCompletoVehiculoViaje: String = scriptCreacionViaje + scriptCreacionVehiculo + scriptCreacionOrigen +
        scriptCreacionDestino + scriptCreacionCamino + scriptCreacionRelaciones

      session.run(scriptCompletoVehiculoViaje)
    }
    session.close()
    driver.close()
  }

  def getVehiculosViajes(): Array[VehiculoViaje] = {
    val (driver, session) = getSession

    val scriptNumViajes = "MATCH (:Viaje) " +
      "WITH COUNT(*) AS numViajes " +
      "RETURN numViajes"
    val resultNumViajes = session.run(scriptNumViajes)

    val numViajes: Int = resultNumViajes.next().values().get(0).asInt()

    val vehiculosViajes: Array[VehiculoViaje] = new Array[VehiculoViaje](numViajes)

    val scriptGetViajesVehiculos: String =
      "MATCH (viaje)-[r1:TIENE_UN]->(vehiculo), " +
        "(viaje)-[r2:INICIA_EN]->(origen), " +
        "(viaje)-[r3:TERMINA_EN]->(destino), " +
        "(viaje)-[r4:RECORRE_UN]->(camino) " +
        "RETURN viaje, vehiculo, origen, destino, camino"
    val resultViajesVehiculos = session.run(scriptGetViajesVehiculos)

    for (index <- 0 until numViajes if resultViajesVehiculos.hasNext) {
      val values = resultViajesVehiculos.next().values()
      val nodoViaje = values.get(0)
      val nodoVehiculo = values.get(1)
      val nodoOrigen = values.get(2)
      val nodoDestino = values.get(3)
      val nodoCamino = values.get(4)

      var vehiculo: Vehiculo = new Bus

      nodoVehiculo.get("tipo").asString() match {
        case "class vehiculo.Bus" => vehiculo = new Bus
        case "class vehiculo.Camion" => vehiculo = new Camion
        case "class vehiculo.Carro" => vehiculo = new Carro
        case "class vehiculo.Moto" => vehiculo = new Moto
        case "class vehiculo.MotoTaxi" => vehiculo = new MotoTaxi
      }

      vehiculo.placa = nodoVehiculo.get("placa").asString()

      vehiculo.velocidad = new Velocidad(
        nodoVehiculo.get("magnitudVelocidad").asDouble(),
        Angulo(nodoVehiculo.get("anguloVelocidad").asDouble())
      )

      vehiculo.posicion = Punto(
        nodoVehiculo.get("posicionX").asDouble(),
        nodoVehiculo.get("posicionY").asDouble()
      )

      val origen: Interseccion = new Interseccion(
        nodoOrigen.get("posicionX").asDouble(),
        nodoOrigen.get("posicionY").asDouble(),
        Some(nodoOrigen.get("nombre").asString())
      )

      val destino: Interseccion = new Interseccion(
        nodoDestino.get("posicionX").asDouble(),
        nodoDestino.get("posicionY").asDouble(),
        Some(nodoDestino.get("nombre").asString())
      )

      val vehiculoViaje: VehiculoViaje =
        new VehiculoViaje(vehiculo, origen, destino, GrafoVia.getCamino(origen, destino))

      var viasPorRecorrer: List[String] =
        nodoCamino.get("identificadoresViasPorRecorrer").asList(Values.ofToString).toList

      viasPorRecorrer = viasPorRecorrer.map(v => v.substring(1, v.length - 1))

      vehiculoViaje.listaViasCamino = vehiculoViaje.getListaViasCamino

      var listaCaminoPorRecorrer: ArrayBuffer[Via] = ArrayBuffer[Via]()

      for(identificadorVia <- viasPorRecorrer if identificadorVia != ""){
        listaCaminoPorRecorrer +=
          vehiculoViaje.listaViasCamino.filter(via => via.nombreIdentificador() == identificadorVia).head
      }

      vehiculoViaje.colaViasCamino = Queue(listaCaminoPorRecorrer: _*)

      vehiculoViaje.vehiculoEnDestino =
        if(nodoViaje.get("vehiculoEnDestino").asString() == "true") true else false

      vehiculoViaje.viaActual = Simulacion.viasDirigidas.filter(via =>
        via.nombreIdentificador() == nodoViaje.get("identificadorViaActual").asString()
      ).head

      vehiculosViajes(index) = vehiculoViaje
    }
    session.close()
    driver.close()
    vehiculosViajes
  }

  def eliminarVehiculosViajes(): Unit = {
    val (driver, session) = getSession

    val scriptEliminarViajesVehiculos: String =
      "MATCH (viaje)-[r1:TIENE_UN]->(vehiculo), " +
        "(viaje)-[r2:INICIA_EN]->(origen), " +
        "(viaje)-[r3:TERMINA_EN]->(destino), " +
        "(viaje)-[r4:RECORRE_UN]->(camino) " +
        "DELETE r1, r2, r3, r4 " +
        "DELETE viaje, vehiculo, origen, destino, camino"

    session.run(scriptEliminarViajesVehiculos)

    session.close()
    driver.close()
  }

  def guardarTiempos(tiempoSimulado: Double, tiempoReal: Double): Unit = {
    val (driver, session) = getSession

    val scriptGuardarTiempos: String = s"CREATE (:Tiempos {simulado:$tiempoSimulado, real:$tiempoReal})"
    session.run(scriptGuardarTiempos)

    session.close()
    driver.close()
  }

  def getTiempos(): (Double, Double) = {
    val (driver, session) = getSession

    val scriptGetTiempos: String = "MATCH (tiempos:Tiempos) RETURN tiempos"
    val resultTiempos = session.run(scriptGetTiempos)

    val nodoTiempos = resultTiempos.next().values().get(0)
    val tiempoSimulado: Double = nodoTiempos.get("simulado").asDouble()
    val tiempoReal: Double = nodoTiempos.get("real").asDouble()

    session.close()
    driver.close()
    (tiempoSimulado, tiempoReal)
  }

  def eliminarTiempos(): Unit = {
    val (driver, session) = getSession

    val scriptGuardarTiempos: String = "MATCH (tiempos:Tiempos) DELETE tiempos"
    session.run(scriptGuardarTiempos)

    session.close()
    driver.close()
  }
}
