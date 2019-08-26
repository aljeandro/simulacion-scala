
package conexion

import org.neo4j.driver.v1._

import infraestructura.Interseccion
import infraestructura.via.{Sentido, TipoVia, Via}
import simulacion.Simulacion

object Conexion {

  val url = "bolt://localhost/7687"
  val user = "neo4j" //Usuario por defecto
  val pass = "1234" //Pass de la bd activa

  def getSession: (Driver, Session) = {
    val driver = GraphDatabase.driver(url, AuthTokens.basic(user, pass))
    val session = driver.session
    (driver, session)
  }

  def getIntersecciones: Array[Interseccion] = {
    val (driver, session) = getSession

    val scriptNumIntersecciones = "MATCH (intersecciones:Interseccion) " +
      "WITH COUNT(intersecciones) AS numIntersecciones " +
      "RETURN numIntersecciones"
    val resultNumIntersecciones = session.run(scriptNumIntersecciones)

    val numIntersecciones = resultNumIntersecciones.next().values().get(0).asInt()

    var intersecciones: Array[Interseccion] = new Array[Interseccion](numIntersecciones)

    val scriptIntersecciones = "MATCH (intersecciones:Interseccion) RETURN intersecciones"
    val resultIntersecciones = session.run(scriptIntersecciones)

    var index: Int = 0
    while (resultIntersecciones.hasNext) {
      val nodoInterseccion = resultIntersecciones.next().values().get(0)

      intersecciones(index) =
        new Interseccion(
          nodoInterseccion.get("longitud").asDouble(),
          nodoInterseccion.get("latitud").asDouble(),
          Some(nodoInterseccion.get("nombre").asString())
        )
      index += 1
    }
    session.close()
    driver.close()
    intersecciones
  }

  def getVias: Array[Via] = {
    val (driver, session) = getSession

    val scriptNumVias = "MATCH (vias:Via) " +
      "WITH COUNT(vias) AS numVias " +
      "RETURN numVias"
    val resultNumVias = session.run(scriptNumVias)

    val numVias = resultNumVias.next().values().get(0).asInt()

    var vias: Array[Via] = new Array[Via](numVias)

    val scriptVias = "MATCH (vias:Via) RETURN vias"
    val resultVias = session.run(scriptVias)

    var index: Int = 0
    while (resultVias.hasNext) {
      val nodoInterseccion = resultVias.next().values().get(0)

      vias(index) =
        new Via(
          Simulacion.intersecciones.filter(
            interseccion => interseccion.nombre.get == nodoInterseccion.get("origen").asString()).head,
          Simulacion.intersecciones.filter(
            interseccion => interseccion.nombre.get == nodoInterseccion.get("fin").asString()).head,
          nodoInterseccion.get("velocidadMax").asDouble(),
          TipoVia(nodoInterseccion.get("tipoVia").asString()),
          if (nodoInterseccion.get("sentido").asString() == "dobleVia") Sentido.dobleVia else Sentido.unaVia,
          nodoInterseccion.get("numeroVia").asString(),
          Some(nodoInterseccion.get("nombre").asString())
        )
      index += 1
    }
    session.close()
    driver.close()
    vias
  }
}
