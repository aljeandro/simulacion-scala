
package vehiculo

import fisica._
import geometria.Punto

import scala.util.Random

abstract class Vehiculo() extends Movil with MovimientoUniforme{

  protected var _velocidad: Velocidad = _
  protected var _posicion: Punto = _
  val placa: String

  def velocidad: Velocidad = _velocidad
  def velocidad_=(velocidad: Velocidad): Unit = _velocidad = velocidad

  def posicion: Punto = _posicion
  def posicion_=(posicion: Punto): Unit = _posicion = posicion

  def aumentarPosicion(dt: Double): Unit = posicion = movimientoUniforme(dt, posicion, velocidad)
}

object Vehiculo{

  def generarVehiculo: Vehiculo = {
    val numAleatorio: Int = Random.nextInt(5)

    if (numAleatorio == 0) new Carro()
    else if (numAleatorio == 1) new Moto()
    else if (numAleatorio == 2) new Bus()
    else if (numAleatorio == 3) new Camion()
    else new MotoTaxi()
  }
}
