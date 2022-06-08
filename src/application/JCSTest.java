package application;

import java.util.ArrayList;
import java.util.HashMap;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IIIIII_Result;

import model.Movie;
import model.User;
import recommendation.ItemRecommendation;

public class JCSTest {
	
	@JCStressTest
	@Outcome(id="7, 1, 10, 1, 4, 1", expect=Expect.ACCEPTABLE, desc="10 movies 8 users.")
	@State
	public static class StressTest{
		@Actor
		public void test(IIIIII_Result r) {
			HashMap<Integer,Movie> movies = new HashMap<>();
			movies.put(1, new Movie(1,-1f));//
			movies.put(2, new Movie(2,-1f));//
			movies.put(3, new Movie(3,-1f));//
			movies.put(4, new Movie(4,-1f));//
			movies.put(5, new Movie(5,-1f));//
			movies.put(6, new Movie(6,-1f));//
			movies.put(7, new Movie(7,-1f));//
			movies.put(8, new Movie(8,-1f));//
			movies.put(9, new Movie(9,-1f));//
			movies.put(10, new Movie(10,-1f));//
			
			HashMap<Integer,User> users = new HashMap<>();
			users.put(1, new User(1));
			users.get(1).addMovie(new Movie(1, 4.0f));
			users.get(1).addMovie(new Movie(8, 1.0f));
			users.get(1).addMovie(new Movie(3, 5.0f));
			users.get(1).addMovie(new Movie(5, 2.0f));
			users.get(1).addMovie(new Movie(6, 3.0f));
			users.get(1).addMovie(new Movie(9, 5.0f));
			users.put(2, new User(2));
			users.get(2).addMovie(new Movie(1, 4.0f));
			users.get(2).addMovie(new Movie(2, 5.0f));
			users.get(2).addMovie(new Movie(8, 2.0f));
			users.get(2).addMovie(new Movie(3, 4.0f));
			users.get(2).addMovie(new Movie(4, 1.0f));
			users.get(2).addMovie(new Movie(6, 4.0f));
			users.get(2).addMovie(new Movie(9, 5.0f));
			users.put(3, new User(3));
			users.get(3).addMovie(new Movie(2, 4.0f));
			users.get(3).addMovie(new Movie(8, 5.0f));
			users.get(3).addMovie(new Movie(5, 5.0f));
			users.get(3).addMovie(new Movie(6, 1.0f));
			users.get(3).addMovie(new Movie(7, 1.0f));
			users.get(3).addMovie(new Movie(9, 3.0f));
			users.get(3).addMovie(new Movie(10, 3.0f));
			users.put(4, new User(4));
			users.get(4).addMovie(new Movie(1, 3.0f));
			users.get(4).addMovie(new Movie(2, 4.0f));
			users.get(4).addMovie(new Movie(3, 2.0f));
			users.get(4).addMovie(new Movie(4, 2.0f));
			users.get(4).addMovie(new Movie(7, 3.0f));
			users.get(4).addMovie(new Movie(10, 2.0f));
			users.put(5, new User(5));
			users.get(5).addMovie(new Movie(1, 4.0f));
			users.get(5).addMovie(new Movie(3, 3.0f));
			users.get(5).addMovie(new Movie(4, 3.0f));
			users.get(5).addMovie(new Movie(5, 5.0f));
			users.get(5).addMovie(new Movie(6, 4.0f));
			users.get(5).addMovie(new Movie(7, 4.0f));
			users.get(5).addMovie(new Movie(9, 4.0f));
			users.get(5).addMovie(new Movie(10, 1.0f));
			users.put(6, new User(6));
			users.get(6).addMovie(new Movie(1, 5.0f));
			users.get(6).addMovie(new Movie(2, 3.0f));
			users.get(6).addMovie(new Movie(4, 1.0f));
			users.get(6).addMovie(new Movie(5, 3.0f));
			users.get(6).addMovie(new Movie(7, 1.0f));
			users.get(6).addMovie(new Movie(8, 4.0f));
			users.get(6).addMovie(new Movie(10, 5.0f));
			users.put(7, new User(7));
			users.get(7).addMovie(new Movie(2, 1.0f));
			users.get(7).addMovie(new Movie(3, 3.0f));
			users.get(7).addMovie(new Movie(4, 5.0f));
			users.get(7).addMovie(new Movie(5, 4.0f));
			users.get(7).addMovie(new Movie(5, 4.0f));
			users.get(7).addMovie(new Movie(7, 4.0f));
			users.get(7).addMovie(new Movie(8, 2.0f));
			users.get(7).addMovie(new Movie(9, 4.0f));
			users.get(7).addMovie(new Movie(10, 3.0f));
			users.put(8, new User(8));
			users.get(8).addMovie(new Movie(1, 2.0f));
			users.get(8).addMovie(new Movie(3, 4.0f));
			users.get(8).addMovie(new Movie(4, 4.0f));
			users.get(8).addMovie(new Movie(5, 2.0f));
			users.get(8).addMovie(new Movie(6, 5.0f));
			users.get(8).addMovie(new Movie(7, 1.0f));
			users.get(8).addMovie(new Movie(8, 5.0f));
			users.get(8).addMovie(new Movie(9, 3.0f));
			users.get(8).addMovie(new Movie(10, 4.0f));
			
			ItemRecommendation itemRecommendation = new ItemRecommendation();
			ArrayList<Movie> recommendedMovies = itemRecommendation.generateRecommendationForUser(1, users, movies,1000);
			r.r1 = recommendedMovies.get(0).getId();
			if(recommendedMovies.get(0).getRating() == 4.1636257f) {
				r.r2 = 1;
			}
			r.r3 = recommendedMovies.get(1).getId();
			if(recommendedMovies.get(1).getRating() == 3.766708f) {
				r.r4 = 1;
			}
			r.r5 = recommendedMovies.get(2).getId();
			if(recommendedMovies.get(2).getRating() == 2.8669868f) {
				r.r6 = 1;
			}
			
		}
	}
	
	
	
}
