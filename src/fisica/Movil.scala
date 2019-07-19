
package fisica

import geometria.Punto

abstract class Movil {
  var _velocidad: Velocidad
  var _posicion: Punto

  def velocidad: Velocidad

  def velocidad_=(velocidad: Velocidad): Unit

  def posicion: Punto

  def posicion_=(posicion: Punto): Unit

  // Falta desarrollar el cuerpo de este método
  def aumentarPosicion(dt: Double): Unit

  // ¿Falta desarrollar el cuerpo de este método?
  def anguloMovimiento(): Double = velocidad.angulo.grados
}
