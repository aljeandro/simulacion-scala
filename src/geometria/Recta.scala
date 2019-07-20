
package geometria

import scala.math._

trait Recta{
  type T <: Punto

  val origen: T
  val fin: T

  def longitud: Double = hypot(origen.x - fin.x, origen.y - fin.y)

  def angulo: Double = toDegrees(atan((origen.y - fin.y) / (origen.x - fin.x)))
}
