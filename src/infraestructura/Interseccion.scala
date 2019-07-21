
package infraestructura

import java.awt.Color

import geometria.Punto

case class Interseccion(val longitud: Double, val latitud: Double, val nombre: String = "", val color: Color)
  extends Punto(longitud, latitud)
