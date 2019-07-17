package fisica

import geometria.Punto

abstract class Movil {

  var _velocidad: Velocidad

  var _posicion: Punto

  def aumentarPosicion(dt: Double): Unit

  def anguloMovimiento(): Double = velocidad.angulo.grados
}
