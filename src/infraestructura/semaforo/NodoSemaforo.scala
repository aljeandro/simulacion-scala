
package infraestructura.semaforo

import infraestructura.Interseccion

class NodoSemaforo(val interseccion: Interseccion, val semaforos: Array[Semaforo]) {

  private var _indexSemaforoActivo: Int = 0

  def indexSemaforoActivo: Int = _indexSemaforoActivo
  def indexSemaforoActivo_=(indexSemaforoActivo: Int): Unit = _indexSemaforoActivo = indexSemaforoActivo

  semaforos.head.estado = "Verde"
  semaforos.tail.foreach(_.estado = "Rojo")

  def verificarEstadoInterseccion(): Unit = {
    val semaforoActivo: Semaforo = semaforos(indexSemaforoActivo)

    if (semaforoActivo.estado == "Verde") {
      if (semaforoActivo.tiempoEnEstado >= semaforoActivo.tiempoVerde) {
        semaforoActivo.estado = "Amarillo"
        semaforoActivo.tiempoEnEstado = 0.0
      }
    }
    else if (semaforoActivo.estado == "Amarillo") {
      if (semaforoActivo.tiempoEnEstado >= Semaforo.tiempoAmarillo) {
        semaforoActivo.estado = "Rojo"
        semaforoActivo.tiempoEnEstado = 0.0

        indexSemaforoActivo = indexSemaforoActivo + 1

        if (indexSemaforoActivo == semaforos.length) indexSemaforoActivo = 0

        semaforos(indexSemaforoActivo).estado = "Verde"
      }
    }
  }
}
