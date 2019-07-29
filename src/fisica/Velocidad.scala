
package fisica

import scala.math.cos
import scala.math.sin
import scala.math.toRadians

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
  def aKilometrosPorHora(metrosPorSegundo: Double): Double = metrosPorSegundo * (18/5)

  def aMetrosPorSegundo(kilometrosPorHora: Double): Double = kilometrosPorHora * (5/18)
}
