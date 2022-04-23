package recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import model.Movie;
import model.User;

public class ItemRecommendation {

	
	public ArrayList<Movie> generateRecommendationForUser(int userId, HashMap<Integer, User> users, int recommendationAmount){
		ArrayList<Movie> recommendedMovies = new ArrayList<Movie>();
		User user = users.get(userId);
		ArrayList<User> usersList = calculateUsersSimilarity(user, users);
		Collections.sort(usersList,Collections.reverseOrder());
		
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
		ArrayList<Movie> userMovies = user.getMovies();
		ArrayList<Movie> otherMovies = other.getMovies();
		float averageUserRating = user.getMoviesAverageRating();
		float averageOtherRating = other.getMoviesAverageRating();
		for(Movie userMovie:userMovies) {
			for(Movie otherMovie:otherMovies) {
				if(userMovie.equals(otherMovie)) {
					numberOfMoviesRatedInCommom++;
					float userMovieDelta = userMovie.getRating()-averageUserRating;
					float otherMovieDelta = otherMovie.getRating()-averageOtherRating;
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
