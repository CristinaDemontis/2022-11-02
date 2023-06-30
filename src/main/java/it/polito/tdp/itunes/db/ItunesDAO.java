package it.polito.tdp.itunes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Arco;
import it.polito.tdp.itunes.model.Artist;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.MediaType;
import it.polito.tdp.itunes.model.Playlist;
import it.polito.tdp.itunes.model.Track;

public class ItunesDAO {
	
	public List<Album> getAllAlbums(){
		final String sql = "SELECT * FROM Album";
		List<Album> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Album(res.getInt("AlbumId"), res.getString("Title")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Artist> getAllArtists(){
		final String sql = "SELECT * FROM Artist";
		List<Artist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Artist(res.getInt("ArtistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Playlist> getAllPlaylists(){
		final String sql = "SELECT * FROM Playlist";
		List<Playlist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Playlist(res.getInt("PlaylistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Track> getAllTracks(){
		final String sql = "SELECT * FROM Track";
		List<Track> result = new ArrayList<Track>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getInt("Milliseconds"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice")));
			
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Genre> getAllGenres(){
		final String sql = "SELECT * FROM Genre";
		List<Genre> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Genre(res.getInt("GenreId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		
		Collections.sort(result);
		return result;
	}
	
	public List<MediaType> getAllMediaTypes(){
		final String sql = "SELECT * FROM MediaType";
		List<MediaType> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new MediaType(res.getInt("MediaTypeId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Track> getAllTracksMinMax(double min, double max, Genre genere){
		final String sql = "SELECT t.TrackId, t.Name, t.Composer, (t.Milliseconds/1000) AS sec, t.Bytes, t.UnitPrice "
				+ "FROM track t "
				+ "WHERE t.GenreId = ? AND (t.Milliseconds/1000) >= ? AND (t.Milliseconds/1000) <= ? ";
		List<Track> result = new ArrayList<Track>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, genere.getGenreId()); 
			st.setDouble(2, min);
			st.setDouble(3, max);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getDouble("sec"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice")));
			
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Arco> getArchi(Map<Integer, Track> mappa ){
		
		String sql = "SELECT t1.TrackId AS t1, t2.TrackId AS t2 "
				+ "FROM 	(SELECT TrackId, COUNT(*) AS num "
				+ "		FROM playlisttrack "
				+ "		GROUP BY TrackId) t1, "
				+ "		(SELECT TrackId, COUNT(*) AS num "
				+ "		FROM playlisttrack  "
				+ "		GROUP BY TrackId) t2 "
				+ "WHERE t1.TrackId > t2.TrackId AND t1.num = t2.num "; 
		List<Arco> result = new ArrayList<>(); 
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				Track t1 = mappa.get(res.getInt("t1"));
				Track t2 = mappa.get(res.getInt("t2"));
				if(t1 != null && t2 != null) {
					result.add(new Arco(t1,t2));
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
		
		
	}
	
	public Map<Integer, Set<Integer>> getTracks_Playlist(Genre genere, Double min, double max){
		
		String sql = "SELECT t.TrackId, p.PlaylistId "
				+ "FROM track t , playlisttrack p "
				+ "WHERE t.GenreId = ? AND (t.Milliseconds/1000) >= ? AND (t.Milliseconds/1000) <= ? AND t.TrackId = p.TrackId "
				+ "ORDER BY t.TrackId ";
		
		Map<Integer, Set<Integer>> result  = new HashMap<>(); 
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, genere.getGenreId()); 
			st.setDouble(2, min);
			st.setDouble(3, max);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Set<Integer> s= result.get(res.getInt("TrackId")); 
				if(s == null) {
					Set<Integer> list = new HashSet<>(); 
					list.add(res.getInt("PlaylistId")); 
					result.put(res.getInt("TrackId"), list); 
				}else {
					result.get(res.getInt("TrackId")).add(res.getInt("PlaylistId")); 
				}
			
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
		
		
		
	}
	
	
}
