
package infraestructura.via

import geometria.Recta
import infraestructura.Interseccion

class Via(
           val origen: Interseccion,
           val fin: Interseccion,
           val velocidadMax: Double,
           val tipoVia: TipoVia,
           val sentido: Sentido,
           val numeroVia: String,
           val nombre: String
         ) extends Recta {
  type T = Interseccion

  def identificadorOrigenFin(): String = s"$nombre desde ${origen.nombre} hasta ${fin.nombre}"

  def identificadorFinOrigen(): String = s"$nombre desde ${fin.nombre} hasta ${origen.nombre}"
}
