package simulacion

import grafo.GrafoVia
import vehiculo.Vehiculo
import infraestructura.via.Via

import scala.collection.mutable.ArrayBuffer

object Simulacion extends Runnable{

  val grafo = GrafoVia
  var t = 0
  var dt = 1
  var tiempoDormir = 0.5
  val minVehiculos = 100
  val maxVehiculos = 200
  val minVelocidad = 40
  val maxVelocidad = 100
  var vehiculos = ArrayBuffer[Vehiculo]()
  val vias = ArrayBuffer[Via]()

  def run(){}
}

