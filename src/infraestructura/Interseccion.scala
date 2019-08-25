
package infraestructura

import geometria.Punto

class Interseccion(val longitud: Double, val latitud: Double, val nombre: Option[String])
  extends Punto(longitud, latitud)
