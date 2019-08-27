package simulacion

import fisica.Velocidad
import geometria.Angulo
import grafico.Grafico
import grafo.GrafoVia
import vehiculo._
import vehiculo.Vehiculo
import infraestructura.via._
import infraestructura.Interseccion
import infraestructura.semaforo.{NodoSemaforo, Semaforo}
import resultadosSimulacion.{Json, ResultadosSimulacion}
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
  private var _hilo: Thread = _

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

  def maxVelocidad: Int = _maxVelocidad
  def maxVelocidad_=(maxVelocidad: Int): Unit = _maxVelocidad = maxVelocidad

  def minTiempoVerde: Int = _minTiempoVerde
  def minTiempoVerde_=(minTiempoVerde: Int): Unit = _minTiempoVerde = minTiempoVerde

  def maxTiempoVerde: Int = _maxTiempoVerde
  def maxTiempoVerde_=(maxTiempoVerde: Int): Unit = _maxTiempoVerde = maxTiempoVerde

  def tiempoAmarillo: Int = _tiempoAmarillo
  def tiempoAmarillo_=(tiempoAmarillo: Int): Unit = _tiempoAmarillo = tiempoAmarillo

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
    cargarParametros()
    semaforos = crearSemaforos(vias)
    nodoSemaforos = crearNodoSemaforos(semaforos, intersecciones)
    Grafico.dibujarMapa(vias, intersecciones)
  }

  def cargarInfraestructura(): Unit = {
    intersecciones = Conexion.getIntersecciones
    vias = Conexion.getVias
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
    minTiempoVerde = Json.minTiempoVerde
    maxTiempoVerde = Json.maxTiempoVerde
    Semaforo.tiempoAmarillo = Json.tiempoAmarillo
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
    Grafico.dibujarVehiculos(vehiculosViajes)
    hilo = new Thread(Simulacion)
    hilo.start()
  }

  def pausarAnimacion(): Unit = {
    continuarSimulacion = false
    Grafico.eliminarSeriesVehiculos()
  }

  def pararAnimacion(): Unit = {
    continuarSimulacion = false
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

  def run(): Unit = {
    while (continuarSimulacion) {
      vehiculosViajes.foreach(_.mover(dt))
      tiempoSimulado += dt
      tiempoReal += tRefresh
      Grafico.graficarVehiculos(vehiculosViajes)
      Thread.sleep(tRefresh * 1000) // Multiplicando por 1000 se pasa a milisegundos

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
}
