package model;

import java.util.ArrayList;

public class User {
	private int id;
	private ArrayList<Movie> movies;
	public User(int id) {
		this.id = id;
		this.movies = new ArrayList<Movie>();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<Movie> getMovies() {
		return movies;
	}
	public void setMovies(ArrayList<Movie> movies) {
		this.movies = movies;
	}
	public void addMovie(Movie movie) {
		movies.add(movie);
	}
	
}
