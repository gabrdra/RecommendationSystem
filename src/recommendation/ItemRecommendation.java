package recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

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
		Semaphore semaphore = new Semaphore(Runtime.getRuntime().availableProcessors());
		movies.forEach((movieId, movie)->{
			try {
				semaphore.acquire();
				PredictRatingScore p = new PredictRatingScore(movieId,otherAmount,userAverageRating,movie,
						recommendedMovies,usersList,recommendedMoviesLock);
				p.start();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				semaphore.release();
			}
			
		});
		//while(PredictRatingScore.activeCount()>1) {System.out.println(PredictRatingScore.activeCount());};
		while(!semaphore.tryAcquire(Runtime.getRuntime().availableProcessors())) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			};
		Collections.sort(recommendedMovies, Collections.reverseOrder());

		return recommendedMovies;
	}
	private class PredictRatingScore extends Thread{
		private int movieId;
		private Movie movie;
		private ArrayList<Movie> recommendedMovies;
		private ArrayList<User> usersList;
		private int otherAmount;
		private float userAverageRating;
		private Object recommendedMoviesLock;
		public PredictRatingScore(int movieId,int otherAmount,float userAverageRating,Movie movie,
				ArrayList<Movie> recommendedMovies, ArrayList<User> usersList,Object recommendedMoviesLock) {
			this.movieId = movieId;
			this.movie = movie;
			this.otherAmount = otherAmount;
			this.userAverageRating = userAverageRating;
			this.movie = movie;
			this.recommendedMovies = recommendedMovies;
			this.usersList = usersList;	
			this.recommendedMoviesLock = recommendedMoviesLock;
		}
		public void run(){
			predictScore();
		}
		private void predictScore() {
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
