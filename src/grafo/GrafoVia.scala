
package grafo

import infraestructura.Interseccion
import infraestructura.via.Via
import scalax.collection.edge.WLDiEdge
import scalax.collection.mutable.Graph

object GrafoVia {

  val grafo = Graph[Interseccion, WLDiEdge]()

  def construir(vias: Array[Via]): Unit = {

    val viasDobleSentido: Array[Via] = vias.filter(_.sentido.nombre == "dobleVia")

    (vias.map(_.origen) ++ vias.map(_.fin)).distinct.foreach(grafo.add(_))

    vias.foreach(via =>
      grafo.add(WLDiEdge(via.origen, via.fin)(via.longitud, via.identificadorOrigenFin())))

    viasDobleSentido.foreach(via =>
      grafo.add(WLDiEdge(via.fin, via.origen)(via.longitud, via.identificadorFinOrigen())))
  }
}
