
package fisica

import scala.math._

import geometria.Angulo

class Velocidad(private var _magnitud: Double, private var _angulo: Angulo) {

  def magnitud_=(magnitud: Double): Unit = _magnitud = magnitud

  def magnitud: Double = _magnitud

  def angulo_=(angulo: Angulo): Unit = _angulo = angulo

  def angulo: Angulo = _angulo

  def velocidadDireccionX(): Double = cos(toRadians(angulo.grados)) * magnitud

  def velocidadDireccionY(): Double = sin(toRadians(angulo.grados)) * magnitud

}

object Velocidad{

}
