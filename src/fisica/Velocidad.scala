package fisica

import scala.math.{cos, sin, toRadians}
import geometria.Angulo

case class Velocidad
(
  private var _magnitud: Double,
  private var _direccion: Angulo
)
{
  def magnitud_=(magnitud: Double): Unit = _magnitud = magnitud
  def magnitud: Double = _magnitud

  def direccion_=(angulo: Angulo): Unit = _direccion = angulo
  def direccion: Angulo = _direccion

  def velocidadDireccionX(): Double = cos(toRadians(direccion.grados)) * magnitud
  def velocidadDireccionY(): Double = sin(toRadians(direccion.grados)) * magnitud
}

object Velocidad {
  def aKilometrosPorHora(metrosPorSegundo: Double): Double = metrosPorSegundo * (18/5)
  def aMetrosPorSegundo(kilometrosPorHora: Double): Double = kilometrosPorHora * (5/18)
}