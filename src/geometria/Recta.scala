
package geometria

trait Recta{
  type T <: Punto

  val origen: T
  val fin: T

  def longitud: Double = Math.hypot(origen.x - fin.x, origen.y - fin.y)

  def angulo: Double = Math.atan((origen.y - fin.y) / (origen.x - fin.x))  //
}
