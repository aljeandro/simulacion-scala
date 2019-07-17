package vehiculo

import fisica.{Movil, MovimientoUniforme, Velocidad}
import geometria.Punto

abstract class Vehiculo(
                         private var _velocidad: Velocidad,
                         private var _posicion: Punto
                       ) extends Movil with MovimientoUniforme{

  def velocidad: Velocidad = _velocidad

  def velocidad_=(velocidad: Velocidad): Unit = _velocidad = velocidad

  def posicion: Punto = _posicion

  def posicion_=(posicion: Punto): Unit = _posicion = posicion

  def aumentarPosicion(dt: Double): Unit = {

    posicion = movimientoUniforme(dt, posicion, velocidad)

  }
}

object Vehiculo{

}
