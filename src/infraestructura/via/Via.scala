
package infraestructura.via

import geometria.Recta
import infraestructura.Interseccion

class Via
(
  val origen: Interseccion,
  val fin: Interseccion,
  val velocidadMax: Double,
  val tipoVia: TipoVia,
  val sentido: Sentido,
  val numeroVia: String,
  val nombre: Option[String]
) extends Recta
{
  type T = Interseccion

  def nombreIdentificador(): String =
    s"${nombre.getOrElse("Via sin nombre")} desde " +
      s"${origen.nombre.getOrElse("origen sin nombre")} hasta ${fin.nombre.getOrElse("fin sin nombre")}"
}
