
package fisica

import geometria.Punto

import scala.math.round

trait MovimientoUniforme {

  def movimientoUniforme(dt: Double, posicion: Punto, velocidad: Velocidad): Punto = {
    val nuevaX: Int = round(posicion.x + velocidad.velocidadDireccionX() * dt).toInt
    val nuevaY: Int = round(posicion.y + velocidad.velocidadDireccionY() * dt).toInt

    new Punto(nuevaX, nuevaY)
  }
}
