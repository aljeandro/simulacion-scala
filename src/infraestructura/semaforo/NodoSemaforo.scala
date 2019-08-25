package infraestructura.semaforo

import infraestructura.Interseccion

class NodoSemaforo(val interseccion: Interseccion, val semaforos: Array[Semaforo]) extends Runnable{

  def run(): Unit = {

  }

}
