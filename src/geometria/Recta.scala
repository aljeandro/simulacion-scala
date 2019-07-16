package geometria

trait Recta{

  type T <: Punto

  val origen: T
  val fin: T

  def longitud = Math.hypot(origen.x - fin.x, origen.y - fin.y)
}
