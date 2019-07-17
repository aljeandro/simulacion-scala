package vehiculo

import infraestructura.Interseccion
import grafo.GrafoVia

class VehiculoViaje(
                     val vehiculo: Vehiculo,
                     origen: Interseccion,
                     val destino: Interseccion,
                     val camino: GrafoVia.grafo.Path
                   ) {

}
