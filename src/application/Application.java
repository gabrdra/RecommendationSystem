package application;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import fileReader.FileReader;
import fileReader.JsonFileReader;
import model.Movie;
import model.User;
import recommendation.ItemRecommendation;

public class Application {

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(RecommendationSystemBenchmark.class.getSimpleName())
				.warmupIterations(3)
				.shouldDoGC(true)
				.measurementIterations(8).forks(1)
				.addProfiler(GCProfiler.class)
				.addProfiler(StackProfiler.class)
				.jvmArgs("-server", "-Xms2048m","-Xmx2048m").build();
		new Runner(opt).run();
		
//		HashMap<Integer,Movie> movies = new HashMap<>();
//		movies.put(1, new Movie(1,-1f));//
//		movies.put(2, new Movie(2,-1f));//
//		movies.put(3, new Movie(3,-1f));//
//		movies.put(4, new Movie(4,-1f));//
//		movies.put(5, new Movie(5,-1f));//
//		movies.put(6, new Movie(6,-1f));//
//		movies.put(7, new Movie(7,-1f));//
//		movies.put(8, new Movie(8,-1f));//
//		movies.put(9, new Movie(9,-1f));//
//		movies.put(10, new Movie(10,-1f));//
//		
//		HashMap<Integer,User> users = new HashMap<>();
//		users.put(1, new User(1));
//		users.get(1).addMovie(new Movie(1, 4.0f));
//		users.get(1).addMovie(new Movie(8, 1.0f));
//		users.get(1).addMovie(new Movie(3, 5.0f));
//		users.get(1).addMovie(new Movie(5, 2.0f));
//		users.get(1).addMovie(new Movie(6, 3.0f));
//		users.get(1).addMovie(new Movie(9, 5.0f));
//		users.put(2, new User(2));
//		users.get(2).addMovie(new Movie(1, 4.0f));
//		users.get(2).addMovie(new Movie(2, 5.0f));
//		users.get(2).addMovie(new Movie(8, 2.0f));
//		users.get(2).addMovie(new Movie(3, 4.0f));
//		users.get(2).addMovie(new Movie(4, 1.0f));
//		users.get(2).addMovie(new Movie(6, 4.0f));
//		users.get(2).addMovie(new Movie(9, 5.0f));
//		users.put(3, new User(3));
//		users.get(3).addMovie(new Movie(2, 4.0f));
//		users.get(3).addMovie(new Movie(8, 5.0f));
//		users.get(3).addMovie(new Movie(5, 5.0f));
//		users.get(3).addMovie(new Movie(6, 1.0f));
//		users.get(3).addMovie(new Movie(7, 1.0f));
//		users.get(3).addMovie(new Movie(9, 3.0f));
//		users.get(3).addMovie(new Movie(10, 3.0f));
//		users.put(4, new User(4));
//		users.get(4).addMovie(new Movie(1, 3.0f));
//		users.get(4).addMovie(new Movie(2, 4.0f));
//		users.get(4).addMovie(new Movie(3, 2.0f));
//		users.get(4).addMovie(new Movie(4, 2.0f));
//		users.get(4).addMovie(new Movie(7, 3.0f));
//		users.get(4).addMovie(new Movie(10, 2.0f));
//		users.put(5, new User(5));
//		users.get(5).addMovie(new Movie(1, 4.0f));
//		users.get(5).addMovie(new Movie(3, 3.0f));
//		users.get(5).addMovie(new Movie(4, 3.0f));
//		users.get(5).addMovie(new Movie(5, 5.0f));
//		users.get(5).addMovie(new Movie(6, 4.0f));
//		users.get(5).addMovie(new Movie(7, 4.0f));
//		users.get(5).addMovie(new Movie(9, 4.0f));
//		users.get(5).addMovie(new Movie(10, 1.0f));
//		users.put(6, new User(6));
//		users.get(6).addMovie(new Movie(1, 5.0f));
//		users.get(6).addMovie(new Movie(2, 3.0f));
//		users.get(6).addMovie(new Movie(4, 1.0f));
//		users.get(6).addMovie(new Movie(5, 3.0f));
//		users.get(6).addMovie(new Movie(7, 1.0f));
//		users.get(6).addMovie(new Movie(8, 4.0f));
//		users.get(6).addMovie(new Movie(10, 5.0f));
//		users.put(7, new User(7));
//		users.get(7).addMovie(new Movie(2, 1.0f));
//		users.get(7).addMovie(new Movie(3, 3.0f));
//		users.get(7).addMovie(new Movie(4, 5.0f));
//		users.get(7).addMovie(new Movie(5, 4.0f));
//		users.get(7).addMovie(new Movie(5, 4.0f));
//		users.get(7).addMovie(new Movie(7, 4.0f));
//		users.get(7).addMovie(new Movie(8, 2.0f));
//		users.get(7).addMovie(new Movie(9, 4.0f));
//		users.get(7).addMovie(new Movie(10, 3.0f));
//		users.put(8, new User(8));
//		users.get(8).addMovie(new Movie(1, 2.0f));
//		users.get(8).addMovie(new Movie(3, 4.0f));
//		users.get(8).addMovie(new Movie(4, 4.0f));
//		users.get(8).addMovie(new Movie(5, 2.0f));
//		users.get(8).addMovie(new Movie(6, 5.0f));
//		users.get(8).addMovie(new Movie(7, 1.0f));
//		users.get(8).addMovie(new Movie(8, 5.0f));
//		users.get(8).addMovie(new Movie(9, 3.0f));
//		users.get(8).addMovie(new Movie(10, 4.0f));
//		
//		Instant start = Instant.now();
//		//FileReader fileReader = new JsonFileReader();
//		ItemRecommendation itemRecommendation = new ItemRecommendation();
//		//HashMap<Integer,User> users = fileReader.getUsers();
//		//HashMap<Integer,Movie> movies = fileReader.getMovies();
//		Instant retrieveDataTime = Instant.now();
//		//System.out.println(users.get(997206).getMovies().toString());
//		//System.out.println(movies.get(10).toString());
//		System.out.println("Time it took to retrieve data: "+Duration.between(start, retrieveDataTime));
//		ArrayList<Movie> recommendedMovies = itemRecommendation.generateRecommendationForUser(1, users, movies,1000);
//		Instant generateRecommendationTime = Instant.now();
//		System.out.println("Time it took to generate recommendations: "+Duration.between(retrieveDataTime, generateRecommendationTime));
//		System.out.println(recommendedMovies.size());
//		for (Movie movie : recommendedMovies) {
//			System.out.println(" MovieId: "+movie.getId()+ " predicted movie rating: "+movie.getRating());
//		}
		
		
		
		
		
		
		//Instant start = Instant.now();
//		FileReader fileReader = new JsonFileReader();
//		ItemRecommendation itemRecommendation = new ItemRecommendation();
//		HashMap<Integer,User> users = fileReader.getUsers();
//		HashMap<Integer,Movie> movies = fileReader.getMovies();
//		//Instant retrieveDataTime = Instant.now();
//		//System.out.println(users.get(997206).getMovies().toString());
//		//System.out.println(movies.get(10).toString());
//		//System.out.println("Time it took to retrieve data: "+Duration.between(start, retrieveDataTime));
//		ArrayList<Movie> recommendedMovies = itemRecommendation.generateRecommendationForUser(393887, users, movies,2500);
//		//Instant generateRecommendationTime = Instant.now();
//		//System.out.println("Time it took to generate recommendations: "+Duration.between(retrieveDataTime, generateRecommendationTime));
//		for(int i = 0; i<50;i++) {
//			Movie movie = recommendedMovies.get(i);
//			System.out.println("Index: "+i+" MovieId: "+movie.getId()+ " predicted movie rating: "+movie.getRating());
//		}
		
	}

}
