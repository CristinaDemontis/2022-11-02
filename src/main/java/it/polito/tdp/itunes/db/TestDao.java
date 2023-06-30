package it.polito.tdp.itunes.db;

import java.util.*;

import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Track;

public class TestDao {

	public static void main(String[] args) {
		
		Genre g = new Genre(1, "Rock"); 
		
		ItunesDAO dao = new ItunesDAO(); 
		List<Track> tracks = dao.getAllTracksMinMax(120, 240, g); 
		System.out.println(tracks.size());
		
		

	}

}
