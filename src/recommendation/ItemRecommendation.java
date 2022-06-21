package recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
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
		//Collections.sort(usersList,Collections.reverseOrder());
		float userAverageRating = user.getMoviesAverageRating();
		Object recommendedMoviesLock = new Object();
		ArrayList<Movie> moviesArrayList = new ArrayList<Movie>();
		movies.forEach((movieid, movie)->{
			moviesArrayList.add(movie);
		});
		PredictRatingScore prs = new PredictRatingScore(moviesArrayList, 0, moviesArrayList.size(), otherAmount, userAverageRating, recommendedMovies, usersList, recommendedMoviesLock);
		ForkJoinPool pool = new ForkJoinPool();
		pool.invoke(prs);
		pool.awaitQuiescence(120, TimeUnit.SECONDS);
		synchronized (recommendedMoviesLock) {
			Collections.sort(recommendedMovies, Collections.reverseOrder());
		}

		return recommendedMovies;
	}
	private class PredictRatingScore extends RecursiveAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2894314549636050188L;
		private static final int SEQUENCIAL_THRESHOLD = 1000;
		private ArrayList<Movie> movies;
		private int startIndex;
		private int moviesLength;
		private ArrayList<Movie> recommendedMovies;
		private ArrayList<User> usersList;
		private int otherAmount;
		private float userAverageRating;
		private Object recommendedMoviesLock;
		public PredictRatingScore(ArrayList<Movie> movies, int startIndex, int moviesLength, 
				int otherAmount, float userAverageRating,
				ArrayList<Movie> recommendedMovies, ArrayList<User> usersList,Object recommendedMoviesLock) {
			this.movies = movies;
			this.startIndex = startIndex;
			this.moviesLength = moviesLength;
			this.otherAmount = otherAmount;
			this.userAverageRating = userAverageRating;
			this.recommendedMovies = recommendedMovies;
			this.usersList = usersList;	
			this.recommendedMoviesLock = recommendedMoviesLock;
		}
		private void predictScore() {
			for (int i = startIndex; i < startIndex+moviesLength; i++) {
				Movie movie = movies.get(i);
				int counter = 0;
				float normalizedSumOfSimilaritty = 0f;
				float total = 0f;
				for(int i1 = 0; i1<otherAmount;i1++) {
					User other = usersList.get(i1);
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
						recommendedMovies.add(new Movie(movie.getId(),predictedRating));
					}
				}
			}
		}
		@Override
		protected void compute() {
			if(moviesLength < SEQUENCIAL_THRESHOLD) {
				predictScore();
				return;
			}
			int split = moviesLength/2;
			invokeAll(new PredictRatingScore(movies, startIndex, split, otherAmount, userAverageRating, recommendedMovies, usersList, recommendedMoviesLock),
					new PredictRatingScore(movies, startIndex+split, moviesLength-split, otherAmount, userAverageRating, recommendedMovies, usersList, recommendedMoviesLock));
		}
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
		ArrayList<Movie> userMovies = user.getMovies();
		ArrayList<Movie> otherMovies = other.getMovies();
		float userAverageRating = user.getMoviesAverageRating();
		float otherAverageRating = other.getMoviesAverageRating();
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
