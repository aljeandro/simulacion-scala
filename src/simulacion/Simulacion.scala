package simulacion

import fisica.Velocidad
import geometria.Angulo
import grafico.Grafico
import grafico.Grafico.dibujarCamaras
import grafo.GrafoVia
import vehiculo._
import vehiculo.Vehiculo
import infraestructura.via._
import infraestructura.{CamaraFotoDeteccion, Interseccion}
import infraestructura.semaforo.{NodoSemaforo, Semaforo}
import resultadosSimulacion.{Comparendo, Json, ResultadosSimulacion}
import conexion.Conexion

import scala.collection.mutable.ArrayBuffer
import scala.util.Random


object Simulacion extends Runnable {

  //---------- Infraestructura ----------
  private var _intersecciones: Array[Interseccion] = _
  private var _vias: Array[Via] = _
  private var _viasDirigidas: Array[Via] = _
  private var _semaforos: Array[Semaforo] = _
  private var _nodoSemaforos: Array[NodoSemaforo] = _
  val camarasFotoDeteccion: ArrayBuffer[CamaraFotoDeteccion] = ArrayBuffer()

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
  private var _minAceleracion: Double = _
  private var _maxAceleracion: Double = _
  private var _minTiempoVerde: Int = _
  private var _maxTiempoVerde: Int = _
  private var _tiempoAmarillo: Int = _
  private var _XSemaforoFrenar: Int = _
  private var _XSemaforoAmarilloContinuar: Int = _
  private var _tiempoSimulado: Double = _
  private var _tiempoReal: Double = _
  private var _continuarSimulacion: Boolean = _
  private var _cantVehiculos: Int = _
  private var _hilo: Thread = _

  def dt: Double = _dt
  def dt_=(dt: Double): Unit = _dt = dt

  def tRefresh: Long = _tRefresh
  def tRefresh_=(tRefresh: Long): Unit = _tRefresh = tRefresh

  def minVehiculos: Int = _minVehiculos
  def minVehiculos_=(minVehiculos: Int): Unit = _minVehiculos = minVehiculos

  def maxVehiculos: Int = _maxVehiculos
  def maxVehiculos_=(maxVehiculos: Int): Unit = _maxVehiculos = maxVehiculos

  def maxVelocidad: Int = _maxVelocidad
  def maxVelocidad_=(maxVelocidad: Int): Unit = _maxVelocidad = maxVelocidad

  def minVelocidad: Int = _minVelocidad
  def minVelocidad_=(minVelocidad: Int): Unit = _minVelocidad = minVelocidad

  def maxAceleracion: Double = _maxAceleracion
  def maxAceleracion_=(maxAceleracion: Double): Unit = _maxAceleracion = maxAceleracion

  def minAceleracion: Double = _minAceleracion
  def minAceleracion_=(minAceleracion: Double): Unit = _minAceleracion = minAceleracion

  def minTiempoVerde: Int = _minTiempoVerde
  def minTiempoVerde_=(minTiempoVerde: Int): Unit = _minTiempoVerde = minTiempoVerde

  def maxTiempoVerde: Int = _maxTiempoVerde
  def maxTiempoVerde_=(maxTiempoVerde: Int): Unit = _maxTiempoVerde = maxTiempoVerde

  def tiempoAmarillo: Int = _tiempoAmarillo
  def tiempoAmarillo_=(tiempoAmarillo: Int): Unit = _tiempoAmarillo = tiempoAmarillo

  def XSemaforoFrenar: Int = _XSemaforoFrenar
  def XSemaforoFrenar_=(XSemaforoFrenar: Int): Unit = _XSemaforoFrenar = XSemaforoFrenar

  def XSemaforoAmarilloContinuar: Int = _XSemaforoAmarilloContinuar
  def XSemaforoAmarilloContinuar_=(XSemaforoAmarilloContinuar: Int): Unit = _XSemaforoAmarilloContinuar = XSemaforoAmarilloContinuar

  def tiempoSimulado: Double = _tiempoSimulado
  def tiempoSimulado_=(tiempoSimulado: Double): Unit = _tiempoSimulado = tiempoSimulado

  def tiempoReal: Double = _tiempoReal
  def tiempoReal_=(tiempoReal: Double): Unit = _tiempoReal = tiempoReal

  def continuarSimulacion: Boolean = _continuarSimulacion
  def continuarSimulacion_=(continuarSimulacion: Boolean): Unit = _continuarSimulacion = continuarSimulacion

  def cantVehiculos: Int = _cantVehiculos
  def cantVehiculos_=(cantVehiculos: Int): Unit = _cantVehiculos = cantVehiculos

  def hilo: Thread = _hilo
  def hilo_=(hilo: Thread): Unit = _hilo = hilo

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
    crearCamarasFotoDeteccion()
    // dibujarCamaras(camarasFotoDeteccion)
    cargarParametros()
    semaforos = crearSemaforos(vias)
    nodoSemaforos = crearNodoSemaforos(semaforos, intersecciones)
    Grafico.dibujarMapa(vias, intersecciones)
  }

  def cargarInfraestructura(): Unit = {
    intersecciones = Conexion.getIntersecciones()
    vias = Conexion.getVias()
  }

  def crearCamarasFotoDeteccion(): Unit = {
    for (x <- 0 until 9) {
      camarasFotoDeteccion += new CamaraFotoDeteccion(viasDirigidas(x), viasDirigidas(x).longitud / 2)
    }
  }

  def construirGrafo(): Unit = GrafoVia.construir(vias, intersecciones)

  def cargarParametros(): Unit = {
    dt = Json.tiempoDt
    tRefresh = Json.tRefresh
    minVehiculos = Json.vehiculosMinimo
    maxVehiculos = Json.vehiculosMaximo
    minVelocidad = Json.velocidadMinima
    maxVelocidad = Json.velocidadMaxima
    minAceleracion = Json.aceleracionMinima
    maxAceleracion = Json.aceleracionMaxima
    Vehiculo.proporcionCarro = Json.proporcionCarros
    Vehiculo.proporcionMoto = Json.proporcionMotos
    Vehiculo.proporcionBus = Json.proporcionBuses
    Vehiculo.proporcionCamion = Json.proporcionCamiones
    minTiempoVerde = Json.minTiempoVerde
    maxTiempoVerde = Json.maxTiempoVerde
    Semaforo.tiempoAmarillo = Json.tiempoAmarillo
    XSemaforoFrenar = Json.XSemaforoFrenar
    XSemaforoAmarilloContinuar = Json.XSemaforoAmarilloContinuar
  }

  def crearSemaforos(vias: Array[Via]): Array[Semaforo] = {

    val semaforosLocal: ArrayBuffer[Semaforo] = ArrayBuffer()

    def crearSemaforoVia(via: Via): Unit = {

          val semaforo: Semaforo = new Semaforo(
            via,
            via.fin,
            Random.nextInt(maxTiempoVerde - minTiempoVerde) + minTiempoVerde)

          semaforosLocal += semaforo
      }

    viasDirigidas.foreach(via => crearSemaforoVia(via))
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

  def nuevaAnimacion(): Unit = {
    continuarSimulacion = true
    tiempoSimulado = 0
    tiempoReal = 0
    crearVehiculos()
    crearViajesVehiculos()
    Grafico.dibujarVehiculos(vehiculosViajes)
    hilo = new Thread(Simulacion)
    hilo.start()
  }

  def continuarAnimacion(): Unit = {
    continuarSimulacion = true

    tiempoAmarillo = Json.tiempoAmarillo

    val (tiempoS, tiempoR) = Conexion.getTiempos()
    tiempoSimulado = tiempoS
    tiempoReal = tiempoR
    Conexion.eliminarTiempos()

    semaforos = Conexion.getSemaforos()
    Conexion.eliminarSemaforos()

    nodoSemaforos = crearNodoSemaforos(semaforos, intersecciones)

    vehiculosViajes = Conexion.getVehiculosViajes()
    Conexion.eliminarVehiculosViajes()

    agregarVehiculosEnDestino()
    Grafico.dibujarVehiculos(vehiculosViajes)

    hilo = new Thread(Simulacion)
    hilo.start()
  }

  def agregarVehiculosEnDestino(): Unit = {
    cantVehiculos = vehiculosViajes.length
    VehiculoViaje.vehiculosEnSuDestino = new ArrayBuffer[Vehiculo](cantVehiculos)
    for(vehiculoViaje <- vehiculosViajes if vehiculoViaje.vehiculoEnDestino) {
      VehiculoViaje.vehiculosEnSuDestino += vehiculoViaje.vehiculo
    }
  }

  def pausarAnimacion(): Unit = {
    continuarSimulacion = false
    Conexion.guardarViajesVehiculos(vehiculosViajes)
    Conexion.guardarTiempos(tiempoSimulado, tiempoReal)
    Conexion.guardarSemaforos(semaforos)
    generarResultadoSimulacion()
    System.exit(0)
  }

  def crearVehiculos(): Unit = {
    definirCantidadVehiculosEnListas()
    for(index <- 0 until cantVehiculos) vehiculos(index) = Vehiculo.generarVehiculo
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
      vehiculoActual.velocidad = new Velocidad(0, Angulo(0))
      vehiculoActual.magnitudVelocidadCrucero = Random.nextInt(maxVelocidad - minVelocidad) + minVelocidad
      vehiculoActual.aceleracionAsignada = minAceleracion + Random.nextDouble() * (maxAceleracion - minAceleracion)
      vehiculoActual.aceleracionActual = vehiculoActual.aceleracionAsignada
      vehiculosViajes(index) = new VehiculoViaje(vehiculoActual, origen, destino, GrafoVia.getCamino(origen, destino))
      vehiculosViajes(index).inicializarParametrosViaje()
    }
  }

  def crearOrigenYDestinoVehiculo(): (Interseccion, Interseccion) = {
    val cantIntersecciones = intersecciones.length

    val indexOrigenAleat: Int = Random.nextInt(cantIntersecciones)
    var indexDestinoAleat: Int = Random.nextInt(cantIntersecciones)

    while (indexOrigenAleat == indexDestinoAleat) indexDestinoAleat = Random.nextInt(cantIntersecciones)

    (intersecciones(indexOrigenAleat), intersecciones(indexDestinoAleat))
  }

  def run(): Unit = {
    while (continuarSimulacion) {
      semaforos.filter(semaforo => semaforo.estado == "Verde" || semaforo.estado == "Amarillo").foreach(_.actualizarEstado(dt))
      nodoSemaforos.foreach(_.verificarEstadoInterseccion())
      vehiculosViajes.foreach(_.mover(dt))
      tiempoSimulado += dt
      tiempoReal += tRefresh
      Grafico.graficarVehiculos(vehiculosViajes)
      Thread.sleep(tRefresh)

      if (VehiculoViaje.vehiculosEnSuDestino.length == cantVehiculos) {
        continuarSimulacion = false
        generarResultadoSimulacion()
        System.exit(0)
      }
    }
  }

  def generarResultadoSimulacion(): Unit = {
    Json.escribirResultados(
      new ResultadosSimulacion(
        vias,
        intersecciones,
        vehiculosViajes,
        tiempoSimulado,
        tiempoReal
      )
    )
  }

  var listaComparendos: ArrayBuffer[Comparendo] = _

}
