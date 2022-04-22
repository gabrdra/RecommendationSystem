package model;

import java.util.ArrayList;

public class User implements Comparable<User>{
	private int id;
	private ArrayList<Movie> movies;
	private float similarity =-2f;
	public User() {
		this(0);
	}
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
	public float getSimilarity() {
		return similarity;
	}
	public void setSimilarity(float similarity) {
		this.similarity = similarity;
	}
	@Override
    public int compareTo(User other) {
        double delta = similarity - other.similarity;
        if (delta > 0 ) return 1;
        if (delta < 0) return -1;
        return 0;
    }
}
