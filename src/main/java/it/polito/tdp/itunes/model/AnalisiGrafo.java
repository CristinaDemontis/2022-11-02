package it.polito.tdp.itunes.model;

public class AnalisiGrafo {
	private int numVertici; 
	private int numPlaylist;
	
	public AnalisiGrafo(int numVertici, int numPlaylist) {
		super();
		this.numVertici = numVertici;
		this.numPlaylist = numPlaylist;
	}

	public int getNumVertici() {
		return numVertici;
	}

	public int getNumPlaylist() {
		return numPlaylist;
	}

	@Override
	public String toString() {
		return "Componente con "+numVertici+ ", inseriti in "+ numPlaylist+" playlist."; 
	} 
	
	
	

}
