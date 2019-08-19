package geometria

case class Angulo(private var _grados: Double) {
  def grados: Double = _grados
  def grados_=(grados: Double): Unit = _grados = grados
}