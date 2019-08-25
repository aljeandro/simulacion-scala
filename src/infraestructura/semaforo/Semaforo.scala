package infraestructura.semaforo

import infraestructura.Interseccion
import infraestructura.via.Via

class Semaforo(val viaUbicacion: Via, val interseccionUbicacion: Interseccion, val tiempoVerde: Double) {

  private var _estado: String = _

  def estado: String = _estado
  def estado_=(estado: String): Unit = _estado = estado


}
