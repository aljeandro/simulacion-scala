
package fisica

import geometria.Punto

abstract class Movil {

  var _posicion: Punto
  var _velocidad: Velocidad

  def posicion: Punto

  def posicion_=(posicion: Punto): Unit

  def velocidad: Velocidad

  def velocidad_=(velocidad: Velocidad): Unit

  def aumentarPosicion(dt: Double): Unit

  def anguloMovimiento(): Double = velocidad.angulo.grados

}
