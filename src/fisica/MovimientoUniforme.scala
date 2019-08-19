package fisica

import geometria.Punto

trait MovimientoUniforme {

  def movimientoUniforme(dt: Double, posicion: Punto, velocidad: Velocidad): Punto = {
    val nuevaX: Double = posicion.x + velocidad.velocidadDireccionX() * dt
    val nuevaY: Double = posicion.y + velocidad.velocidadDireccionY() * dt

    Punto(nuevaX, nuevaY)
  }
}