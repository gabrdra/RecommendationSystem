package application;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fileReader.FileReader;
import fileReader.JsonFileReader;
import model.Movie;
import model.User;
import recommendation.ItemRecommendation;

public class Application {

	public static void main(String[] args) {
		Instant start = Instant.now();
		FileReader fileReader = new JsonFileReader();
		ItemRecommendation itemRecommendation = new ItemRecommendation();
		HashMap<Integer,User> users = fileReader.getUsers();
		HashMap<Integer,Movie> movies = fileReader.getMovies();
		Instant retrieveDataTime = Instant.now();
		//System.out.println(users.get(997206).getMovies().toString());
		//System.out.println(movies.get(10).toString());
		System.out.println("Time it took to retrieve data: "+Duration.between(start, retrieveDataTime));
		ArrayList<Movie> recommendedMovies = itemRecommendation.generateRecommendationForUser(997206, users, movies,1000);
		Instant generateRecommendationTime = Instant.now();
		System.out.println("Time it took to generate recommendations: "+Duration.between(retrieveDataTime, generateRecommendationTime));
		for(Movie movie:recommendedMovies) {
			System.out.println("MovieId: "+movie.getId()+ " predicted movie rating: "+movie.getRating());
		}
		
	}

}
