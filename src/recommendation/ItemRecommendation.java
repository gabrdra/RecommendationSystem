package recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import model.Movie;
import model.User;

public class ItemRecommendation {

	
	public ArrayList<Movie> generateRecommendationForUser(int userId, HashMap<Integer, User> users, HashMap<Integer, Movie> movies,int numberOfUsersComparatedTo){
		ArrayList<Movie> recommendedMovies = new ArrayList<Movie>();
		User user = users.get(userId);
		ArrayList<User> usersList = calculateUsersSimilarity(user, users);
		int otherAmount = Math.min(usersList.size(), numberOfUsersComparatedTo);
		Collections.sort(usersList,Collections.reverseOrder());
		float userAverageRating = user.getMoviesAverageRating();
		Object recommendedMoviesLock = new Object();
		
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());  
		
		movies.forEach((movieId, movie)->{	
			executor.execute(new Runnable() {

				@Override
				public void run() {
					int counter = 0;
					float normalizedSumOfSimilaritty = 0f;
					float total = 0f;
					for(int i = 0; i<otherAmount;i++) {
						User other = usersList.get(i);
						ArrayList<Movie> otherMovies = other.getMovies();
						int movieIndex = otherMovies.indexOf(movie);
						if(movieIndex>-1f) {
							counter++;
							normalizedSumOfSimilaritty += Math.abs(other.getSimilarity());
							total += (otherMovies.get(movieIndex).getRating()-other.getMoviesAverageRating())*other.getSimilarity();
						}
					}
					if(counter>5){// only adds a movie if it has being rated more than 5 times by other users
						float predictedRating = userAverageRating+(total/normalizedSumOfSimilaritty);
						synchronized (recommendedMoviesLock) {
							recommendedMovies.add(new Movie(movieId,predictedRating));
						}
					}
				}
				
			});
		});
		executor.shutdown();
		try {
			executor.awaitTermination(60, TimeUnit.SECONDS);
		}catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(recommendedMovies, Collections.reverseOrder());

		return recommendedMovies;
	}
	
	private ArrayList<User> calculateUsersSimilarity(User user, HashMap<Integer, User> users) {
		ArrayList<User> usersList = new ArrayList<User>();
		users.forEach((otherId, other)->{
			if(!other.equals(user)) {
				User otherWithSimilarityCalculated = cosineSimilarity(user,other);
				if(otherWithSimilarityCalculated.getSimilarity()!=-2f) {
					
					usersList.add(otherWithSimilarityCalculated);
				}
			}
		});
		return usersList;
	}
	
	private User cosineSimilarity(User user, User other) {
		int numberOfMoviesRatedInCommom = 0;
		float userSumOfDeltas = 0f; //This is the squared deviation between a given movie and the remaining movies rated by the user
		float otherSumOfDeltas = 0f; 
		float cosineSimilarity = 0f;
		ArrayList<Movie> userMovies = null;
		CompletableFuture<ArrayList<Movie>> userMoviesFuture = CompletableFuture.supplyAsync(()->user.getMovies());
		ArrayList<Movie> otherMovies = null;
		CompletableFuture<ArrayList<Movie>> otherMoviesFuture = CompletableFuture.supplyAsync(()->other.getMovies());
		float userAverageRating = 0;
		CompletableFuture<Float> userAverageRatingFuture = CompletableFuture.supplyAsync(()->user.getMoviesAverageRating());
		float otherAverageRating = 0;
		CompletableFuture<Float> otherAverageRatingFuture = CompletableFuture.supplyAsync(()->other.getMoviesAverageRating());
		CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(userMoviesFuture, otherMoviesFuture,userAverageRatingFuture,otherAverageRatingFuture);
			try {
				combinedFuture.get();
				userMovies = userMoviesFuture.get();
				otherMovies = otherMoviesFuture.get();
				userAverageRating = userAverageRatingFuture.get();
				otherAverageRating = otherAverageRatingFuture.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		for(Movie userMovie:userMovies) {
			for(Movie otherMovie:otherMovies) {
				if(userMovie.equals(otherMovie)) {
					numberOfMoviesRatedInCommom++;
					float userMovieDelta = userMovie.getRating()-userAverageRating;
					float otherMovieDelta = otherMovie.getRating()-otherAverageRating;
					cosineSimilarity += userMovieDelta*otherMovieDelta;
					userSumOfDeltas += userMovieDelta*userMovieDelta;
					otherSumOfDeltas += otherMovieDelta*otherMovieDelta;
				}
			}
		}
		if(numberOfMoviesRatedInCommom>1&&cosineSimilarity!=0f&&userSumOfDeltas!=0f&&otherSumOfDeltas!=0f) {
			cosineSimilarity /= Math.sqrt(userSumOfDeltas*otherSumOfDeltas);
			other.setSimilarity(cosineSimilarity);
		}
		return other;
		
	}
}
