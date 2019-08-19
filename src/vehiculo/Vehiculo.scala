package vehiculo

import fisica._
import geometria.{Angulo, Punto}

import scala.util.Random

abstract case class Vehiculo() extends Movil with MovimientoUniforme{

  protected var _velocidad: Velocidad = new Velocidad(0, Angulo(0))
  protected var _posicion: Punto = Punto(0, 0)
  val placa: String

  def velocidad: Velocidad = _velocidad
  def velocidad_=(velocidad: Velocidad): Unit = _velocidad = velocidad

  def posicion: Punto = _posicion
  def posicion_=(posicion: Punto): Unit = _posicion = posicion

  def aumentarPosicion(dt: Double): Unit = posicion = movimientoUniforme(dt, posicion, velocidad)
}

object Vehiculo{

  var proporcionCarro: Double = _
  var proporcionMoto: Double = _
  var proporcionBus: Double = _
  var proporcionCamion: Double = _

  def generarVehiculo: Vehiculo = {
    val numAleatorio: Double = Random.nextDouble() // Valor aleatorio entre 0 (inclusivo) y 1 (exclusivo)

    if (0 < numAleatorio && numAleatorio <= proporcionCarro) new Carro
    else if (proporcionCarro < numAleatorio && numAleatorio <= proporcionCarro + proporcionMoto) new Moto
    else if (proporcionCarro + proporcionMoto < numAleatorio && numAleatorio <= proporcionCarro + proporcionMoto + proporcionBus) new Bus
    else if (proporcionCarro + proporcionMoto + proporcionBus < numAleatorio && numAleatorio <= proporcionCarro + proporcionMoto + proporcionBus + proporcionCamion) new Camion
    else new MotoTaxi
  }
}