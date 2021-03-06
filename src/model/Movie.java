package model;

public class Movie implements Comparable<Movie>{

	private int id;
	private float rating;
	
	public Movie(int id, float rating) {
		this.id = id;
		this.rating = rating;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}

	@Override
	public boolean equals(Object obj) {
		Movie other = (Movie) obj;
		return this.id == other.id;
	}

	@Override
	public int compareTo(Movie o) {
		double delta = rating - o.rating;
        if (delta > 0 ) return 1;
        if (delta < 0) return -1;
        return 0;
	}
}
