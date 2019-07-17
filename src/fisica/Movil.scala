package fisica

import geometria.Punto

abstract class Movil {

  var _velocidad: Velocidad

  var _posicion: Punto

  def velocidad: Velocidad

  def velocidad_=(velocidad: Velocidad): Unit

  def posicion: Punto

  def posicion_=(posicion: Punto): Unit

  def aumentarPosicion(dt: Double): Unit

  def anguloMovimiento(): Double = velocidad.angulo.grados
}
