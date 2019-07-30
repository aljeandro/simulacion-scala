
package simulacion

import java.awt.Color

import fisica.Velocidad
import geometria.Angulo
import grafico.Grafico
import grafo.GrafoVia
import vehiculo._
import infraestructura.via._
import infraestructura.Interseccion

import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import scala.math.{abs, round}

object Simulacion extends Runnable{

  var intersecciones: Array[Interseccion] = _
  var vias: Array[Via] = _

  var t: Int = _
  var dt: Int = _
  var tiempoDormir: Int = _
  var minVehiculos: Int = _
  var maxVehiculos: Int = _
  var minVelocidad: Int = _
  var maxVelocidad: Int = _
  var proporcionCarro: Double = _
  var proporcionMoto: Double = _
  var proporcionBus: Double = _
  var proporcionCamion: Double = _
  var proporcionMotoTaxi: Double = _
  var continuarSimulacion: Boolean = _

  var cantVehiculos: Int = _
  var vehiculos: Array[Vehiculo] = _
  var vehiculosViajes: Array[VehiculoViaje] = _

  def iniciarSimulacion(): Unit = {
    cargarInfraestructura()
    cargarParametros()         // TODO: Estos parámetros deben ser cargados desde el Json
    crearVehiculos()
    construirGrafo()
    crearViajesVehiculos()
    run()
  }

  def cargarInfraestructura(): Unit = {
    val niquia = new Interseccion(300, 12000, "Niquia", new Color(3, 214, 0))
    val lauraAuto = new Interseccion(2400, 11400, "M. Laura Auto", new Color(161, 92, 255))
    val lauraReg = new Interseccion(2400, 12600, "M. Laura Reg", new Color(132, 77, 206))
    val ptoCero = new Interseccion(5400, 12000, "Pto 0", new Color(0, 0, 0))
    val mino = new Interseccion(6300, 15000, "Minorista", new Color(167, 238, 129))
    val villa = new Interseccion(6300, 19500, "Villanueva", new Color(190, 226, 171))
    val ig65 = new Interseccion(5400, 10500, "65 Igu", new Color(197, 252, 167))
    val robledo = new Interseccion(5400, 1500, "Exito Rob", new Color(156, 207, 236))
    val colReg = new Interseccion(8250, 12000, "Col Reg", new Color(225, 236, 55))
    val colFerr = new Interseccion(8250, 15000, "Col Ferr", new Color(165, 173, 55))
    val col65 = new Interseccion(8250, 10500, "Col 65", new Color(200, 140, 140))
    val col80 = new Interseccion(8250, 1500, "Col 80", new Color(103, 162, 90))
    val juanOr = new Interseccion(10500, 19500, "Sn Juan Ori", new Color(59, 83, 150))
    val maca = new Interseccion(10500, 12000, "Macarena", new Color(108, 61, 61))
    val expo = new Interseccion(12000, 13500, "Exposiciones", new Color(209, 19, 19))
    val reg30 = new Interseccion(13500, 15000, "Reg 30", new Color(16, 151, 53))
    val monte = new Interseccion(16500, 15000, "Monterrey", new Color(6, 209, 61))
    val agua = new Interseccion(19500, 15000, "Aguacatala", new Color(78, 9, 156))
    val viva = new Interseccion(21000, 15000, "Viva Env", new Color(156, 80, 8))
    val mayor = new Interseccion(23400, 15000, "Mayorca", new Color(146, 82, 108))
    val ferrCol = new Interseccion(8250, 15000, "Ferr Col", new Color(32, 117, 96))
    val ferrJuan = new Interseccion(10500, 15000, "Alpujarra", new Color(32, 117, 49))
    val sanDiego = new Interseccion(12000, 19500, "San Diego", new Color(180, 56, 200))
    val premium = new Interseccion(13500, 19500, "Premium", new Color(150, 56, 43))
    val pp = new Interseccion(16500, 19500, "Parque Pob", new Color(116, 69, 24))
    val santafe = new Interseccion(19500, 18750, "Santa Fe", new Color(45, 197, 195))
    val pqEnv = new Interseccion(21000, 18000, "Envigado", new Color(12, 229, 45))
    val juan65 = new Interseccion(10500, 10500, "Juan 65", new Color(0, 199, 181))
    val juan80 = new Interseccion(10500, 1500, "Juan 80", new Color(0, 41, 195))
    val _33_65 = new Interseccion(12000, 10500, "33 con 65", new Color(73, 97, 187))
    val bule = new Interseccion(12000, 7500, "Bulerias", new Color(25, 81, 32))
    val gema = new Interseccion(12000, 1500, "St Gema", new Color(19, 22, 51))
    val _30_65 = new Interseccion(13500, 10500, "30 con 65", new Color(54, 62, 126))
    val _30_70 = new Interseccion(13500, 4500, "30 con 70", new Color(159, 116, 116))
    val _30_80 = new Interseccion(13500, 1500, "30 con 80", new Color(100, 62, 50))
    val bol65 = new Interseccion(11100, 10500, "Boliv con 65", new Color(230, 71, 20))
    val gu10 = new Interseccion(16500, 12000, "Guay con 10", new Color(240, 58, 0))
    val terminal = new Interseccion(16500, 10500, "Term Sur", new Color(117, 83, 73))
    val gu30 = new Interseccion(13500, 12000, "Guay 30", new Color(73, 112, 117))
    val gu80 = new Interseccion(19500, 12000, "Guay 80", new Color(105, 117, 73))
    val _65_80 = new Interseccion(19500, 10500, "65 con 30", new Color(35, 86, 85))
    val gu_37S = new Interseccion(21000, 12000, "Guay con 37S", new Color(60, 6, 86))

    intersecciones = Array(niquia, lauraAuto, lauraReg, ptoCero, mino, villa, ig65, robledo, colReg, colFerr, col65, col80, juanOr, maca,
      expo, reg30, monte, agua, viva, mayor, ferrCol, ferrJuan, sanDiego, premium, pp, santafe, pqEnv, juan65, juan80,
      _33_65, bule, gema, _30_65, _30_70, _30_80, bol65, gu10, terminal, gu30, gu80, _65_80, gu_37S)

    vias = Array(
      new Via(niquia, lauraAuto, 80, TipoVia("Carrera"), Sentido.dobleVia, "64C", "Auto Norte"),
      new Via(niquia, lauraReg, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
      new Via(lauraAuto, lauraReg, 60, TipoVia("Calle"), Sentido.dobleVia, "94", "Pte Madre Laura"),
      new Via(lauraAuto, ptoCero, 80, TipoVia("Carrera"), Sentido.dobleVia, "64C", "Auto Norte"),
      new Via(lauraReg, ptoCero, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
      new Via(ptoCero, mino, 60, TipoVia("Calle"), Sentido.dobleVia, "58", "Oriental"),
      new Via(mino, villa, 60, TipoVia("Calle"), Sentido.dobleVia, "58", "Oriental"),
      new Via(ptoCero, ig65, 60, TipoVia("Calle"), Sentido.dobleVia, "55", "Iguaná"),
      new Via(ig65, robledo, 60, TipoVia("Calle"), Sentido.dobleVia, "55", "Iguaná"),
      new Via(ptoCero, colReg, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
      new Via(colReg, maca, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
      new Via(maca, expo, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
      new Via(expo, reg30, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
      new Via(reg30, monte, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
      new Via(monte, agua, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
      new Via(agua, viva, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
      new Via(viva, mayor, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", "Regional"),
      new Via(mino, ferrCol, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", "Ferrocarril"),
      new Via(ferrCol, ferrJuan, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", "Ferrocarril"),
      new Via(ferrJuan, expo, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", "Ferrocarril"),
      new Via(villa, juanOr, 60, TipoVia("Carrera"), Sentido.dobleVia, "46", "Oriental"),
      new Via(juanOr, sanDiego, 60, TipoVia("Carrera"), Sentido.dobleVia, "46", "Oriental"),
      new Via(sanDiego, premium, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
      new Via(premium, pp, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
      new Via(pp, santafe, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
      new Via(santafe, pqEnv, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
      new Via(pqEnv, mayor, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", "Av Pob"),
      new Via(ferrCol, colReg, 60, TipoVia("Calle"), Sentido.dobleVia, "450", "Colombia"),
      new Via(colReg, col65, 60, TipoVia("Calle"), Sentido.dobleVia, "450", "Colombia"),
      new Via(col65, col80, 60, TipoVia("Calle"), Sentido.dobleVia, "450", "Colombia"),
      new Via(juanOr, ferrJuan, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
      new Via(ferrJuan, maca, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
      new Via(maca, juan65, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
      new Via(juan65, juan80, 60, TipoVia("Calle"), Sentido.dobleVia, "44", "Sn Juan"),
      new Via(sanDiego, expo, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
      new Via(expo, _33_65, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
      new Via(_33_65, bule, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
      new Via(bule, gema, 60, TipoVia("Calle"), Sentido.dobleVia, "33", "33"),
      new Via(premium, reg30, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
      new Via(reg30, _30_65, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
      new Via(_30_65, _30_70, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
      new Via(_30_70, _30_80, 60, TipoVia("Calle"), Sentido.dobleVia, "30", "30"),
      new Via(maca, bol65, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", "Boliv"),
      new Via(bol65, bule, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", "Boliv"),
      new Via(bule, _30_70, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", "Boliv"),
      new Via(juan80, bule, 60, TipoVia("Transversal"), Sentido.dobleVia, "39B", "Nutibara"),
      new Via(pp, monte, 60, TipoVia("Calle"), Sentido.dobleVia, "10", "10"),
      new Via(monte, gu10, 60, TipoVia("Calle"), Sentido.dobleVia, "10", "10"),
      new Via(gu10, terminal, 60, TipoVia("Calle"), Sentido.dobleVia, "10", "10"),
      new Via(expo, gu30, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
      new Via(gu30, gu10, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
      new Via(gu10, gu80, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
      new Via(gu80, gu_37S, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", "Av Guay"),
      new Via(lauraAuto, ig65, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", "65"),
      new Via(ig65, col65, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", "65"),
      new Via(juan65, col65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
      new Via(bol65, juan65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
      new Via(_33_65, bol65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
      new Via(_30_65, _33_65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", "65"),
      new Via(_30_65, terminal, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", "65"),
      new Via(terminal, _65_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "65"),
      new Via(robledo, col80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
      new Via(col80, juan80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
      new Via(juan80, gema, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
      new Via(gema, _30_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
      new Via(_30_80, _65_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
      new Via(_65_80, gu80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
      new Via(gu80, agua, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", "80"),
      new Via(agua, santafe, 60, TipoVia("Calle"), Sentido.dobleVia, "12S", "80"),
      new Via(viva, pqEnv, 60, TipoVia("Calle"), Sentido.dobleVia, "37S", "37S"),
      new Via(viva, gu_37S, 60, TipoVia("Calle"), Sentido.dobleVia, "63", "37S")
    )
  }

  def cargarParametros(): Unit = {
    // TODO: Cargar estos datos del Json
    t = 0
    dt = 1
    tiempoDormir = 1
    minVehiculos = 100
    maxVehiculos = 200
    minVelocidad = 40
    maxVelocidad = 100
    proporcionCarro = 0.4
    proporcionMoto = 0.3
    proporcionBus = 0.15
    proporcionCamion = 0.1
    proporcionMotoTaxi = 0.05

    continuarSimulacion = true
  }

  def crearVehiculos(): Unit = {
    cantVehiculos = Random.nextInt(maxVehiculos - minVehiculos) + minVehiculos
    val cantCarro: Int = round(cantVehiculos * proporcionCarro).toInt
    val cantMoto: Int = round(cantVehiculos * proporcionMoto).toInt
    val cantBus: Int = round(cantVehiculos * proporcionBus).toInt
    val cantCamion: Int = round(cantVehiculos * proporcionCamion).toInt
    val cantMotoTaxi: Int = round(cantVehiculos * proporcionMotoTaxi).toInt

    var contCarro: Int = 0
    var contMoto: Int = 0
    var contBus: Int = 0
    var contCamion: Int = 0
    var contMotoTaxi: Int = 0

    var index: Int = 0
    var puedeIngresarVehiculo: Boolean = false

    vehiculos = new Array[Vehiculo](cantVehiculos)
    vehiculosViajes = new Array[VehiculoViaje](cantVehiculos)
    VehiculoViaje.vehiculosEnSuDestino = new ArrayBuffer[Vehiculo](cantVehiculos)

    while (index < cantVehiculos) {
      val vehiculo = Vehiculo.generarVehiculo

      if (vehiculo.getClass == classOf[Carro] && contCarro < cantCarro) {
        contCarro += 1
        puedeIngresarVehiculo = true
      }
      else if (vehiculo.getClass == classOf[Moto] && contMoto < cantMoto) {
        contMoto += 1
        puedeIngresarVehiculo = true
      }
      else if (vehiculo.getClass == classOf[Bus] && contBus < cantBus) {
        contBus += 1
        puedeIngresarVehiculo = true
      }
      else if (vehiculo.getClass == classOf[Camion] && contCamion < cantCamion) {
        contCamion += 1
        puedeIngresarVehiculo = true
      }
      else if (vehiculo.getClass == classOf[MotoTaxi] && contMotoTaxi < cantMotoTaxi) {
        contMotoTaxi += 1
        puedeIngresarVehiculo = true
      }
      else if(abs(cantVehiculos - index) <= 2) puedeIngresarVehiculo = true

      if(puedeIngresarVehiculo) {
        vehiculos(index) = vehiculo
        puedeIngresarVehiculo = false
        index += 1
      }
    }
  }

  def construirGrafo(): Unit = GrafoVia.construir(vias)

  def crearViajesVehiculos(): Unit = {
    val cantIntersecciones = intersecciones.size

    for(index <- 0 to cantVehiculos - 1){
      val indexOrigenAleat: Int = Random.nextInt(cantIntersecciones)
      var indexDestinoAleat: Int = Random.nextInt(cantIntersecciones)

      while(indexOrigenAleat == indexDestinoAleat) indexDestinoAleat = Random.nextInt(cantIntersecciones)

      val origen: Interseccion = intersecciones(indexOrigenAleat)
      val destino: Interseccion = intersecciones(indexDestinoAleat)

      val vehiculoActual = vehiculos(index)
      vehiculoActual.posicion = origen
      vehiculoActual.velocidad = new Velocidad(Random.nextInt(maxVelocidad - minVelocidad) + minVelocidad,
        new Angulo(0))

      vehiculosViajes(index) = new VehiculoViaje(vehiculoActual, origen, destino,
        GrafoVia.grafo.get(origen) shortestPathTo GrafoVia.grafo.get(destino))
    }
  }

  def run(): Unit = {
    while (continuarSimulacion) {
      vehiculosViajes.foreach(_.mover(dt))
      t += dt
      Grafico.graficarVehiculos(vehiculosViajes)
      Thread.sleep(tiempoDormir)

      if(VehiculoViaje.vehiculosEnSuDestino.length == cantVehiculos) continuarSimulacion = false
    }
  }
}
