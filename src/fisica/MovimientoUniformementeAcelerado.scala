package fisica

import geometria.Punto

trait MovimientoUniformementeAcelerado {

  def movimientoUniformementeAcelerado(dt: Double,
                                       posicion: Punto,
                                       velocidad: Velocidad,
                                       aceleracion: Double): Punto = {

    val nuevaX: Double = (aceleracion * Math.cos(Math.toRadians(velocidad.direccion.grados)) * Math.pow(dt, 2.0) / 2.0) + velocidad.velocidadDireccionX() * dt + posicion.x
    val nuevaY: Double = (aceleracion * Math.sin(Math.toRadians(velocidad.direccion.grados)) * Math.pow(dt, 2.0) / 2.0) + velocidad.velocidadDireccionY() * dt + posicion.y

    velocidad.magnitud = velocidadUniformementeAcelerada(dt, velocidad, aceleracion)

    Punto(nuevaX, nuevaY)
  }

  def velocidadUniformementeAcelerada(dt: Double,
                                      velocidad: Velocidad,
                                      aceleracion: Double): Double = {
    velocidad.magnitud + aceleracion * dt
  }
}
