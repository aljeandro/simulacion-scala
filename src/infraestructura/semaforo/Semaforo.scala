package infraestructura.semaforo

import infraestructura.Interseccion
import infraestructura.via.Via

class Semaforo(val viaUbicacion: Via, val interseccionUbicacion: Interseccion, val tiempoVerde: Long) {

  private var _estado: String = _

  def estado: String = _estado
  def estado_=(estado: String): Unit = _estado = estado
}

object Semaforo{

  private var _tiempoAmarillo: Long = _

  def tiempoAmarillo: Long = _tiempoAmarillo
  def tiempoAmarillo_=(tiempoAmarillo: Long): Unit = _tiempoAmarillo = tiempoAmarillo
}
