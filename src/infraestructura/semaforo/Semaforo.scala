
package infraestructura.semaforo

import infraestructura.Interseccion
import infraestructura.via.Via

class Semaforo(val viaUbicacion: Via, val interseccionUbicacion: Interseccion, val tiempoVerde: Long) {

  private var _estado: String = _   // Verde, Amarillo, Rojo
  private var _tiempoEnEstado: Double = 0.0 // Tiempo en estado Verde o Amarillo

  def estado: String = _estado
  def estado_=(estado: String): Unit = _estado = estado

  def tiempoEnEstado: Double = _tiempoEnEstado
  def tiempoEnEstado_=(tiempoEnEstado: Double): Unit = _tiempoEnEstado = tiempoEnEstado

  def actualizarEstado(dt: Double): Unit = {
    tiempoEnEstado = tiempoEnEstado + dt
  }
}

object Semaforo{

  private var _tiempoAmarillo: Long = _

  def tiempoAmarillo: Long = _tiempoAmarillo
  def tiempoAmarillo_=(tiempoAmarillo: Long): Unit = _tiempoAmarillo = tiempoAmarillo
}
