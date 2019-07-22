
package infraestructura.via

class TipoVia(private val nombre: String)

// El identificador debe ser el nombre y el constructor debe ser privado

object TipoVia{

  def apply(nombre: String): TipoVia ={
    new TipoVia(nombre)
  }

}
