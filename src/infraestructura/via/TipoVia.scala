<<<<<<< HEAD
=======

package infraestructura.via
>>>>>>> 9cfe92a0a65995af48931ab862d1c679a60194bc

package infraestructura.via

class TipoVia(private val nombre: String)

// El identificador debe ser el nombre y el constructor debe ser privado

object TipoVia{

  def apply(nombre: String): TipoVia ={
    new TipoVia(nombre)
  }

}
