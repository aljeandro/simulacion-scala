
package infraestructura.semaforo

import infraestructura.Interseccion
import simulacion.Simulacion

class NodoSemaforo(val interseccion: Interseccion, val semaforos: Array[Semaforo]) extends Runnable{

  private var _hilo: Thread = _

  def hilo: Thread = _hilo
  def hilo_=(hilo: Thread): Unit = _hilo = hilo

  def run(): Unit = {

    semaforos.tail.foreach(semaforo => semaforo.estado = "Rojo")

    while(Simulacion.continuarSimulacion){

      for (semaforoNodo <- semaforos){

        semaforoNodo.estado = "Verde"
        Thread.sleep(semaforoNodo.tiempoVerde)

        semaforoNodo.estado = "Amarillo"
        Thread.sleep(Semaforo.tiempoAmarillo)

        semaforoNodo.estado = "Rojo"
      }
    }

  }

}
