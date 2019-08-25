
package simulacion

import fisica.Velocidad
import geometria.Angulo
import grafico.Grafico
import grafo.GrafoVia
import vehiculo._
import infraestructura.via._
import infraestructura.Interseccion
import infraestructura.semaforo.{NodoSemaforo, Semaforo}
import resultadosSimulacion.{Json, ResultadosSimulacion}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import vehiculo.Vehiculo

object Simulacion extends Runnable {

  //---------- Infraestructura ----------
  private var _intersecciones: Array[Interseccion] = _
  private var _vias: Array[Via] = _
  private var _viasDirigidas: Array[Via] = _
  private var _semaforos: Array[Semaforo] = _
  private var _nodoSemaforos: Array[NodoSemaforo] = _

  def intersecciones: Array[Interseccion] = _intersecciones
  def intersecciones_=(intersecciones: Array[Interseccion]): Unit = _intersecciones = intersecciones

  def vias: Array[Via] = _vias
  def vias_=(vias: Array[Via]): Unit = _vias = vias

  def viasDirigidas: Array[Via] = _viasDirigidas
  def viasDirigidas_=(viasDirigidas: Array[Via]): Unit = _viasDirigidas = viasDirigidas

  def semaforos: Array[Semaforo] = _semaforos
  def semaforos_=(semaforos: Array[Semaforo]): Unit = _semaforos = semaforos

  def nodoSemaforos: Array[NodoSemaforo] = _nodoSemaforos
  def nodoSemaforos_=(nodoSemaforos: Array[NodoSemaforo]): Unit = _nodoSemaforos = nodoSemaforos

  //---------- Parámetros de la simulación ----------
  private var _dt: Double = _
  private var _tRefresh: Long = _
  private var _minVehiculos: Int = _
  private var _maxVehiculos: Int = _
  private var _minVelocidad: Int = _
  private var _maxVelocidad: Int = _
  private var _minTiempoVerde: Int = _
  private var _maxTiempoVerde: Int = _
  private var _tiempoAmarillo: Int = _
  private var _tiempoSimulado: Double = _
  private var _tiempoReal: Double = _
  private var _continuarSimulacion: Boolean = _
  private var _cantVehiculos: Int = _
  private var hilo: Thread = _

  def dt: Double = _dt
  def dt_=(dt: Double): Unit = _dt = dt

  def tRefresh: Long = _tRefresh
  def tRefresh_=(tRefresh: Long): Unit = _tRefresh = tRefresh

  def minVehiculos: Int = _minVehiculos
  def minVehiculos_=(minVehiculos: Int): Unit = _minVehiculos = minVehiculos

  def maxVehiculos: Int = _maxVehiculos
  def maxVehiculos_=(maxVehiculos: Int): Unit = _maxVehiculos = maxVehiculos

  def minVelocidad: Int = _minVelocidad
  def minVelocidad_=(minVelocidad: Int): Unit = _minVelocidad = minVelocidad

  def minTiempoVerde: Int = _minTiempoVerde
  def minTiempoVerde_=(minTiempoVerde: Int): Unit = _minTiempoVerde = minTiempoVerde

  def maxTiempoVerde: Int = _maxTiempoVerde
  def maxTiempoVerde_=(maxTiempoVerde: Int): Unit = _maxTiempoVerde = maxTiempoVerde

  def tiempoAmarillo: Int = _tiempoAmarillo
  def tiempoAmarillo_=(tiempoAmarillo: Int): Unit = _tiempoAmarillo = tiempoAmarillo

  def maxVelocidad: Int = _maxVelocidad
  def maxVelocidad_=(maxVelocidad: Int): Unit = _maxVelocidad = maxVelocidad

  def tiempoSimulado: Double = _tiempoSimulado
  def tiempoSimulado_=(tiempoSimulado: Double): Unit = _tiempoSimulado = tiempoSimulado

  def tiempoReal: Double = _tiempoReal
  def tiempoReal_=(tiempoReal: Double): Unit = _tiempoReal = tiempoReal

  def continuarSimulacion: Boolean = _continuarSimulacion
  def continuarSimulacion_=(continuarSimulacion: Boolean): Unit = _continuarSimulacion = continuarSimulacion

  def cantVehiculos: Int = _cantVehiculos
  def cantVehiculos_=(cantVehiculos: Int): Unit = _cantVehiculos = cantVehiculos

  //---------- Listas de los vehiculos ----------
  private var _vehiculos: Array[Vehiculo] = _
  private var _vehiculosViajes: Array[VehiculoViaje] = _

  def vehiculos: Array[Vehiculo] = _vehiculos
  def vehiculos_=(vehiculos: Array[Vehiculo]): Unit = _vehiculos = vehiculos

  def vehiculosViajes: Array[VehiculoViaje] = _vehiculosViajes
  def vehiculosViajes_=(vehiculosViajes: Array[VehiculoViaje]): Unit = _vehiculosViajes = vehiculosViajes


  def iniciarSimulacion(): Unit = {
    cargarInfraestructura()
    construirGrafo()
    cargarParametros()
    semaforos = crearSemaforos(vias)
    nodoSemaforos = crearNodoSemaforos(semaforos, intersecciones)
    Grafico.dibujarMapa(vias)
  }

  def cargarInfraestructura(): Unit = {
    val niquia = new Interseccion(300, 12000, Some("Niquia"))
    val lauraAuto = new Interseccion(2400, 11400, Some("M. Laura Auto"))
    val lauraReg = new Interseccion(2400, 12600, Some("M. Laura Reg"))
    val ptoCero = new Interseccion(5400, 12000, Some("Pto 0"))
    val mino = new Interseccion(6300, 15000, Some("Minorista"))
    val villa = new Interseccion(6300, 19500, Some("Villanueva"))
    val ig65 = new Interseccion(5400, 10500, Some("65 Igu"))
    val robledo = new Interseccion(5400, 1500, Some("Exito Rob"))
    val colReg = new Interseccion(8250, 12000, Some("Col Reg"))
    val colFerr = new Interseccion(8250, 15000, Some("Col Ferr"))
    val col65 = new Interseccion(8250, 10500, Some("Col 65"))
    val col80 = new Interseccion(8250, 1500, Some("Col 80"))
    val juanOr = new Interseccion(10500, 19500, Some("Sn Juan Ori"))
    val maca = new Interseccion(10500, 12000, Some("Macarena"))
    val expo = new Interseccion(12000, 13500, Some("Exposiciones"))
    val reg30 = new Interseccion(13500, 15000, Some("Reg 30"))
    val monte = new Interseccion(16500, 15000, Some("Monterrey"))
    val agua = new Interseccion(19500, 15000, Some("Aguacatala"))
    val viva = new Interseccion(21000, 15000, Some("Viva Env"))
    val mayor = new Interseccion(23400, 15000, Some("Mayorca"))
    val ferrCol = new Interseccion(8250, 15000, Some("Ferr Col"))
    val ferrJuan = new Interseccion(10500, 15000, Some("Alpujarra"))
    val sanDiego = new Interseccion(12000, 19500, Some("San Diego"))
    val premium = new Interseccion(13500, 19500, Some("Premium"))
    val pp = new Interseccion(16500, 19500, Some("Parque Pob"))
    val santafe = new Interseccion(19500, 18750, Some("Santa Fe"))
    val pqEnv = new Interseccion(21000, 18000, Some("Envigado"))
    val juan65 = new Interseccion(10500, 10500, Some("Juan 65"))
    val juan80 = new Interseccion(10500, 1500, Some("Juan 80"))
    val _33_65 = new Interseccion(12000, 10500, Some("33 con 65"))
    val bule = new Interseccion(12000, 7500, Some("Bulerias"))
    val gema = new Interseccion(12000, 1500, Some("St Gema"))
    val _30_65 = new Interseccion(13500, 10500, Some("30 con 65"))
    val _30_70 = new Interseccion(13500, 4500, Some("30 con 70"))
    val _30_80 = new Interseccion(13500, 1500, Some("30 con 80"))
    val bol65 = new Interseccion(11100, 10500, Some("Boliv con 65"))
    val gu10 = new Interseccion(16500, 12000, Some("Guay con 10"))
    val terminal = new Interseccion(16500, 10500, Some("Term Sur"))
    val gu30 = new Interseccion(13500, 12000, Some("Guay 30"))
    val gu80 = new Interseccion(19500, 12000, Some("Guay 80"))
    val _65_80 = new Interseccion(19500, 10500, Some("65 con 30"))
    val gu_37S = new Interseccion(21000, 12000, Some("Guay con 37S"))

    intersecciones = Array(niquia, lauraAuto, lauraReg, ptoCero, mino, villa, ig65, robledo, colReg, colFerr, col65,
      col80, juanOr, maca, expo, reg30, monte, agua, viva, mayor, ferrCol, ferrJuan, sanDiego, premium, pp, santafe,
      pqEnv, juan65, juan80, _33_65, bule, gema, _30_65, _30_70, _30_80, bol65, gu10, terminal, gu30, gu80, _65_80,
      gu_37S)

    vias = Array(
      new Via(niquia, lauraAuto, 80, TipoVia("Carrera"), Sentido.dobleVia, "64C", Some("Auto Norte")),
      new Via(niquia, lauraReg, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
      new Via(lauraAuto, lauraReg, 60, TipoVia("Calle"), Sentido.dobleVia, "94", Some("Pte Madre Laura")),
      new Via(lauraAuto, ptoCero, 80, TipoVia("Carrera"), Sentido.dobleVia, "64C", Some("Auto Norte")),
      new Via(lauraReg, ptoCero, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
      new Via(ptoCero, mino, 60, TipoVia("Calle"), Sentido.dobleVia, "58", Some("Oriental")),
      new Via(mino, villa, 60, TipoVia("Calle"), Sentido.dobleVia, "58", Some("Oriental")),
      new Via(ptoCero, ig65, 60, TipoVia("Calle"), Sentido.dobleVia, "55", Some("Iguaná")),
      new Via(ig65, robledo, 60, TipoVia("Calle"), Sentido.dobleVia, "55", Some("Iguaná")),
      new Via(ptoCero, colReg, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
      new Via(colReg, maca, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
      new Via(maca, expo, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
      new Via(expo, reg30, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
      new Via(reg30, monte, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
      new Via(monte, agua, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
      new Via(agua, viva, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
      new Via(viva, mayor, 80, TipoVia("Carrera"), Sentido.dobleVia, "62", Some("Regional")),
      new Via(mino, ferrCol, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", Some("Ferrocarril")),
      new Via(ferrCol, ferrJuan, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", Some("Ferrocarril")),
      new Via(ferrJuan, expo, 60, TipoVia("Carrera"), Sentido.dobleVia, "55", Some("Ferrocarril")),
      new Via(villa, juanOr, 60, TipoVia("Carrera"), Sentido.dobleVia, "46", Some("Oriental")),
      new Via(juanOr, sanDiego, 60, TipoVia("Carrera"), Sentido.dobleVia, "46", Some("Oriental")),
      new Via(sanDiego, premium, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", Some("Av Pob")),
      new Via(premium, pp, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", Some("Av Pob")),
      new Via(pp, santafe, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", Some("Av Pob")),
      new Via(santafe, pqEnv, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", Some("Av Pob")),
      new Via(pqEnv, mayor, 60, TipoVia("Carrera"), Sentido.dobleVia, "43A", Some("Av Pob")),
      new Via(ferrCol, colReg, 60, TipoVia("Calle"), Sentido.dobleVia, "450", Some("Colombia")),
      new Via(colReg, col65, 60, TipoVia("Calle"), Sentido.dobleVia, "450", Some("Colombia")),
      new Via(col65, col80, 60, TipoVia("Calle"), Sentido.dobleVia, "450", Some("Colombia")),
      new Via(juanOr, ferrJuan, 60, TipoVia("Calle"), Sentido.dobleVia, "44", Some("Sn Juan")),
      new Via(ferrJuan, maca, 60, TipoVia("Calle"), Sentido.dobleVia, "44", Some("Sn Juan")),
      new Via(maca, juan65, 60, TipoVia("Calle"), Sentido.dobleVia, "44", Some("Sn Juan")),
      new Via(juan65, juan80, 60, TipoVia("Calle"), Sentido.dobleVia, "44", Some("Sn Juan")),
      new Via(sanDiego, expo, 60, TipoVia("Calle"), Sentido.dobleVia, "33", Some("33")),
      new Via(expo, _33_65, 60, TipoVia("Calle"), Sentido.dobleVia, "33", Some("33")),
      new Via(_33_65, bule, 60, TipoVia("Calle"), Sentido.dobleVia, "33", Some("33")),
      new Via(bule, gema, 60, TipoVia("Calle"), Sentido.dobleVia, "33", Some("33")),
      new Via(premium, reg30, 60, TipoVia("Calle"), Sentido.dobleVia, "30", Some("30")),
      new Via(reg30, _30_65, 60, TipoVia("Calle"), Sentido.dobleVia, "30", Some("30")),
      new Via(_30_65, _30_70, 60, TipoVia("Calle"), Sentido.dobleVia, "30", Some("30")),
      new Via(_30_70, _30_80, 60, TipoVia("Calle"), Sentido.dobleVia, "30", Some("30")),
      new Via(maca, bol65, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", Some("Boliv")),
      new Via(bol65, bule, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", Some("Boliv")),
      new Via(bule, _30_70, 60, TipoVia("Diagonal"), Sentido.dobleVia, "74B", Some("Boliv")),
      new Via(juan80, bule, 60, TipoVia("Transversal"), Sentido.dobleVia, "39B", Some("Nutibara")),
      new Via(pp, monte, 60, TipoVia("Calle"), Sentido.dobleVia, "10", Some("10")),
      new Via(monte, gu10, 60, TipoVia("Calle"), Sentido.dobleVia, "10", Some("10")),
      new Via(gu10, terminal, 60, TipoVia("Calle"), Sentido.dobleVia, "10", Some("10")),
      new Via(expo, gu30, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", Some("Av Guay")),
      new Via(gu30, gu10, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", Some("Av Guay")),
      new Via(gu10, gu80, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", Some("Av Guay")),
      new Via(gu80, gu_37S, 60, TipoVia("Carrera"), Sentido.dobleVia, "52", Some("Av Guay")),
      new Via(lauraAuto, ig65, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", Some("65")),
      new Via(ig65, col65, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", Some("65")),
      new Via(juan65, col65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", Some("65")),
      new Via(bol65, juan65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", Some("65")),
      new Via(_33_65, bol65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", Some("65")),
      new Via(_30_65, _33_65, 60, TipoVia("Carrera"), Sentido.unaVia, "65", Some("65")),
      new Via(_30_65, terminal, 60, TipoVia("Carrera"), Sentido.dobleVia, "65", Some("65")),
      new Via(terminal, _65_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("65")),
      new Via(robledo, col80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
      new Via(col80, juan80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
      new Via(juan80, gema, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
      new Via(gema, _30_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
      new Via(_30_80, _65_80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
      new Via(_65_80, gu80, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
      new Via(gu80, agua, 60, TipoVia("Carrera"), Sentido.dobleVia, "80", Some("80")),
      new Via(agua, santafe, 60, TipoVia("Calle"), Sentido.dobleVia, "12S", Some("80")),
      new Via(viva, pqEnv, 60, TipoVia("Calle"), Sentido.dobleVia, "37S", Some("37S")),
      new Via(viva, gu_37S, 60, TipoVia("Calle"), Sentido.dobleVia, "63", Some("37S"))
    )
  }

  def construirGrafo(): Unit = GrafoVia.construir(vias, intersecciones)

  def cargarParametros(): Unit = {
    dt = Json.tiempoDt
    tRefresh = Json.tRefresh
    minVehiculos = Json.vehiculosMinimo
    maxVehiculos = Json.vehiculosMaximo
    minVelocidad = Json.velocidadMinima
    maxVelocidad = Json.velocidadMaxima
    Vehiculo.proporcionCarro = Json.proporcionCarros
    Vehiculo.proporcionMoto = Json.proporcionMotos
    Vehiculo.proporcionBus = Json.proporcionBuses
    Vehiculo.proporcionCamion = Json.proporcionCamiones
    minTiempoVerde = 20 // TODO: Leer desde Json
    maxTiempoVerde = 40 // TODO: Leer desde Json
    Semaforo.tiempoAmarillo = 3 // TODO: Leer desde Json
    tiempoSimulado = 0
    tiempoReal = 0
  }

  def crearSemaforos(vias: Array[Via]): Array[Semaforo] = {

    val semaforosLocal: ArrayBuffer[Semaforo] = ArrayBuffer()

    def crearSemaforoVia(via: Via): Unit = {

      via.sentido match {

        case Sentido("dobleVia") =>

          val semaforo1: Semaforo = new Semaforo(
            via,
            via.origen,
            Random.nextInt(maxTiempoVerde - minTiempoVerde) + minTiempoVerde)

          val semaforo2: Semaforo = new Semaforo(
            via,
            via.fin,
            Random.nextInt(maxTiempoVerde - minTiempoVerde) + minTiempoVerde)

          semaforosLocal += semaforo1
          semaforosLocal += semaforo2

        case Sentido("unaVia") =>

          val semaforo: Semaforo = new Semaforo(
            via,
            via.fin,
            Random.nextInt(maxTiempoVerde - minTiempoVerde) + minTiempoVerde)

          semaforosLocal += semaforo
      }
    }
    vias.foreach(via => crearSemaforoVia(via))
    semaforosLocal.toArray
  }

  def crearNodoSemaforos(semaforos: Array[Semaforo], intersecciones: Array[Interseccion]): Array[NodoSemaforo] = {

    val nodoSemaforosLocal: ArrayBuffer[NodoSemaforo] = ArrayBuffer()

    def crearNodoSemaforo(interseccion: Interseccion, semaforosInterseccion: Array[Semaforo]): Unit = {

      nodoSemaforosLocal += new NodoSemaforo(interseccion, semaforosInterseccion)
    }

    intersecciones.foreach(interseccion => {
      val semaforosInterseccion = semaforos.filter(_.interseccionUbicacion == interseccion)
      crearNodoSemaforo(interseccion, semaforosInterseccion)
    })

    nodoSemaforosLocal.toArray
  }

  def crearHilosNodoSemaforos(nodoSemaforos: Array[NodoSemaforo]): Unit = {
    nodoSemaforos.foreach(nodoSemaforo => nodoSemaforo.hilo = new Thread(nodoSemaforo))
  }

  def iniciarHilosNodoSemaforos(nodoSemaforos: Array[NodoSemaforo]): Unit = {
    nodoSemaforos.foreach(nodoSemaforo => nodoSemaforo.hilo.start())
  }

  def crearVehiculos(): Unit = {
    definirCantidadVehiculosEnListas()

    var index: Int = 0
    while (index < cantVehiculos) {
      val vehiculo = Vehiculo.generarVehiculo
      vehiculos(index) = vehiculo
      index += 1
    }
  }

  def definirCantidadVehiculosEnListas(): Unit = {
    cantVehiculos = Random.nextInt(maxVehiculos - minVehiculos) + minVehiculos
    vehiculos = new Array[Vehiculo](cantVehiculos)
    vehiculosViajes = new Array[VehiculoViaje](cantVehiculos)
    VehiculoViaje.vehiculosEnSuDestino = new ArrayBuffer[Vehiculo](cantVehiculos)
  }

  def crearViajesVehiculos(): Unit = {
    for (index <- 0 until cantVehiculos) {
      val (origen, destino) = crearOrigenYDestinoVehiculo()

      val vehiculoActual = vehiculos(index)
      vehiculoActual.posicion = origen
      vehiculoActual.velocidad = new Velocidad(Random.nextInt(maxVelocidad - minVelocidad) + minVelocidad, Angulo(0))

      vehiculosViajes(index) = new VehiculoViaje(vehiculoActual, origen, destino, GrafoVia.getCamino(origen, destino))
    }
  }

  def crearOrigenYDestinoVehiculo(): (Interseccion, Interseccion) = {
    val cantIntersecciones = intersecciones.length

    val indexOrigenAleat: Int = Random.nextInt(cantIntersecciones)
    var indexDestinoAleat: Int = Random.nextInt(cantIntersecciones)

    while (indexOrigenAleat == indexDestinoAleat) indexDestinoAleat = Random.nextInt(cantIntersecciones)

    (intersecciones(indexOrigenAleat), intersecciones(indexDestinoAleat))
  }

  def iniciarAnimacion(): Unit = {
    continuarSimulacion = true
    cargarParametros()
    crearVehiculos()
    crearViajesVehiculos()
    Grafico.dibujarVehiculos(vehiculosViajes)
    crearHilosNodoSemaforos(nodoSemaforos)
    iniciarHilosNodoSemaforos(nodoSemaforos)
    hilo = new Thread(Simulacion)
    hilo.start()
  }

  def pausarAnimacion(): Unit = continuarSimulacion = false

  def run(): Unit = {

    while (continuarSimulacion) {

      vehiculosViajes.foreach(_.mover(dt))
      tiempoSimulado += dt
      tiempoReal += tRefresh
      Grafico.graficarVehiculos(vehiculosViajes)
      Thread.sleep(tRefresh)

      if (VehiculoViaje.vehiculosEnSuDestino.length == cantVehiculos) {
        continuarSimulacion = false
        generarResultadoSimulacion()

      }
    }
  }

  def generarResultadoSimulacion(): Unit = {
    Json.escribirResultados(new ResultadosSimulacion(
      vias,
      intersecciones,
      vehiculosViajes,
      tiempoSimulado,
      tiempoReal))
  }
}
