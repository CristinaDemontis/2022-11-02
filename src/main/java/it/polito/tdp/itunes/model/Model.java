package it.polito.tdp.itunes.model;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private Graph<Track, DefaultEdge> grafo; 
	private List<Track> vertici ;
	private ItunesDAO dao ;
	private Map<Integer, Track > mappaVertici; 
	
	public Model() {
		this.dao = new ItunesDAO();
	} 
	
	public void creaGrafo(double min, double max, Genre genere) {
		
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		this.vertici = this.dao.getAllTracksMinMax(min, max, genere); 
		this.mappaVertici = new HashMap<>(); 
		
		for(Track t: vertici) {
			mappaVertici.put(t.getTrackId(), t); 
		}
		
		Graphs.addAllVertices(this.grafo,this.vertici); 
		
		List<Arco> archi = this.dao.getArchi(mappaVertici);
		for(Arco a: archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getT1(), a.getT2());
		}
		
	
		System.out.println(this.grafo.vertexSet().size()); 
		System.out.println(this.grafo.edgeSet().size()); 

		
		
	}
	
	public List<Genre> getGenre(){
		return this.dao.getAllGenres(); 
	}
	
	public List<AnalisiGrafo> analizzaGrafo(double min, double max, Genre genere){
		ConnectivityInspector<Track, DefaultEdge>  ci = new ConnectivityInspector<>(this.grafo); 
		List<Set<Track>> sets = ci.connectedSets(); 
		Map<Integer, Set<Integer>> mappaTracksPlaylist = this.dao.getTracks_Playlist(genere, min, max); 
		
		List<AnalisiGrafo> result = new ArrayList<>(); 
		
		for(Set<Track> set: sets) {
			Set<Integer> unione = new HashSet<>();
			int num = 0; 
			for(Track t: set) {
				Set<Integer> playlist = mappaTracksPlaylist.get(t.getTrackId()); 
				unione.addAll(playlist); 
				num++; 
			}
			AnalisiGrafo a = new AnalisiGrafo(num, unione.size());
			result.add(a); 
			System.out.println(a.toString()); 
		}		
		
		return result; 
	}
	
	public int getnVertici() {
		return this.grafo.vertexSet().size() ;
	}
	
	public int getnArchi() {
		return this.grafo.edgeSet().size() ;
	}
	
	
	

	
	
}
