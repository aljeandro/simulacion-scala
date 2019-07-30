
package geometria

import scala.math.{hypot, toDegrees, atan}

trait Recta{
  type T <: Punto

  val origen: T
  val fin: T

  def longitud: Double = hypot(fin.x - origen.x, fin.y - origen.y)

  def angulo: Double = toDegrees(atan((fin.y - origen.y) / (fin.x - origen.x)))
}
