package fisica

import geometria.Angulo

class Velocidad(private var _magnitud: Double, private var _angulo: Angulo) {

  def magnitud_=(magnitud: Double): Unit = _magnitud = magnitud

  def magnitud: Double = _magnitud

  def angulo_=(angulo: Angulo): Unit = _angulo = angulo

  def angulo: Angulo = _angulo

  def velocidadDireccionX(): Double = Math.cos(Math.toRadians(angulo.grados)) * magnitud

  def velocidadDireccionY(): Double = Math.sin(Math.toRadians(angulo.grados)) * magnitud

}

object Velocidad{

}
