
package infraestructura

import geometria.Punto

case class Interseccion(val longitud: Double, val latitud: Double, val nombre: String = "")
  extends Punto(longitud, latitud)
