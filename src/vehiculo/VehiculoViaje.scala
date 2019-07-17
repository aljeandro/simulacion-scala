package vehiculo

import infraestructura.Interseccion
import grafo.GrafoVia

class VehiculoViaje(
                     val vehiculo: Vehiculo,
                     val origen: Interseccion,
                     val destino: Interseccion,
                     val camino: GrafoVia.grafo.Path
                   ) {

}
