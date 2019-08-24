
package grafo

import infraestructura.Interseccion
import infraestructura.via.{Via, Sentido}
import simulacion.Simulacion

import scalax.collection.edge.WLDiEdge
import scalax.collection.mutable.Graph

object GrafoVia {

  val grafo = Graph[Interseccion, WLDiEdge]()

  def construir(vias: Array[Via]): Unit = {

    val viasDobleSentido: Array[Via] = vias.filter(_.sentido.nombre == "dobleVia")

    Simulacion.viasDirigidas = vias ++ viasDobleSentido.map(via =>
      new Via(via.fin, via.origen, via.velocidadMax, via.tipoVia, Sentido.unaVia, via.numeroVia, via.nombre))

    (vias.map(_.origen) ++ vias.map(_.fin)).distinct.foreach(interseccion => grafo.add(interseccion))

    Simulacion.viasDirigidas.foreach(via =>
      grafo.add(WLDiEdge(via.origen, via.fin)(via.longitud, via.nombreIdentificador())))
  }

  def getCamino(origen: Interseccion, destino: Interseccion): Option[grafo.Path] ={
    GrafoVia.grafo.get(origen) shortestPathTo GrafoVia.grafo.get(destino)
  }
}
