
package geometria

import scala.math.{hypot, toDegrees, atan}

trait Recta {
  type T <: Punto

  val origen: T
  val fin: T

  def longitud: Double = hypot(fin.x - origen.x, fin.y - origen.y)

  def angulo: Double = {
    if (fin.x == origen.x) {
      if (fin.y > origen.y) 90 else 270
    }
    else if (fin.y == origen.y) {
      if (fin.x > origen.x) 0 else 180
    }
    else {
      val angTriangulo = toDegrees(atan((fin.y - origen.y) / (fin.x - origen.x)))

      if (fin.y > origen.y) {
        if (fin.x > origen.x) angTriangulo
        else 180 + angTriangulo
      }
      else {
        if (fin.x < origen.x) 180 + angTriangulo
        else 360 + angTriangulo
      }
    }
  }
}
