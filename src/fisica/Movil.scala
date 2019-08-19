package fisica

import geometria.Punto

abstract class Movil {

  protected var _posicion: Punto
  protected var _velocidad: Velocidad

  def posicion: Punto
  def posicion_=(posicion: Punto): Unit

  def velocidad: Velocidad
  def velocidad_=(velocidad: Velocidad): Unit

  def aumentarPosicion(dt: Double): Unit

  def direccion(): Double = velocidad.direccion.grados
}