package infraestructura.via

import geometria.Recta
import infraestructura.Interseccion

class Via extends Recta(
         val origen: Interseccion,
         val fin: Interseccion,
         val velocidadMax: Double,
         val tipoVia: TipoVia,
         val sentido: Sentido,
         val numeroVia: String,
         val nombre: String
         ) {

}
