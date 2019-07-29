
package infraestructura.via

class TipoVia(private val _nombre: String){

  def nombre: String = _nombre
}

object TipoVia{

  def apply(nombre: String): TipoVia ={
    new TipoVia(nombre)
  }
}
