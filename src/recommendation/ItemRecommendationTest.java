//package recommendation;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.junit.Test;
//
//import fileReader.FileReader;
//import fileReader.JsonFileReader;
//import model.Movie;
//import model.User;
//
//public class ItemRecommendationTest {
//
//	
//	/*
//	 * Time it took on my machine:
//	 * Serial: 26.696s
//	 * 
//	 * 
//	 */
//	@Test
//	public void testUser997206And1000Samples() {
//		FileReader fileReader = new JsonFileReader();
//		ItemRecommendation itemRecommendation = new ItemRecommendation();
//		HashMap<Integer,User> users = fileReader.getUsers();
//		HashMap<Integer,Movie> movies = fileReader.getMovies();
//		ArrayList<Movie> recommendedMovies = itemRecommendation.generateRecommendationForUser(997206, users, movies,1000);
//		assertEquals(recommendedMovies.get(0).getId(),1304,0);
//		assertEquals(recommendedMovies.get(0).getRating(),4.998936f,0.0000f);
//		assertEquals(recommendedMovies.get(10).getId(),953,0);
//		assertEquals(recommendedMovies.get(10).getRating(),4.5751977f,0.0000f);
//		assertEquals(recommendedMovies.get(6).getId(),1193,0);
//		assertEquals(recommendedMovies.get(6).getRating(),4.6286297f,0.0000f);
//		assertEquals(recommendedMovies.get(24).getId(),1148,0);
//		assertEquals(recommendedMovies.get(24).getRating(),4.4793544f,0.0000f);
//		assertEquals(recommendedMovies.get(35).getId(),2739,0);
//		assertEquals(recommendedMovies.get(35).getRating(),4.4145417f,0.0000f);
//		assertEquals(recommendedMovies.get(46).getId(),1094,0);
//		assertEquals(recommendedMovies.get(46).getRating(),4.3713636f,0.0000f);
//	}
//	/*
//	 * Time it took on my machine:
//	 * Serial: 24.079s
//	 * 
//	 * 
//	 */
//	@Test
//	public void testUser771795And500Samples() {
//		FileReader fileReader = new JsonFileReader();
//		ItemRecommendation itemRecommendation = new ItemRecommendation();
//		HashMap<Integer,User> users = fileReader.getUsers();
//		HashMap<Integer,Movie> movies = fileReader.getMovies();
//		ArrayList<Movie> recommendedMovies = itemRecommendation.generateRecommendationForUser(771795, users, movies,500);
//		assertEquals(recommendedMovies.get(0).getId(),480,0);
//		assertEquals(recommendedMovies.get(0).getRating(),4.835126f,0.0000f);
//		assertEquals(recommendedMovies.get(9).getId(),1784,0);
//		assertEquals(recommendedMovies.get(9).getRating(),4.568927f,0.0000f);
//		assertEquals(recommendedMovies.get(17).getId(),44555,0);
//		assertEquals(recommendedMovies.get(17).getRating(),4.5012555f,0.0000f);
//		assertEquals(recommendedMovies.get(28).getId(),5618,0);
//		assertEquals(recommendedMovies.get(28).getRating(),4.4392977f,0.0000f);
//		assertEquals(recommendedMovies.get(37).getId(),2692,0);
//		assertEquals(recommendedMovies.get(37).getRating(),4.3849807f,0.0000f);
//	}
//	/*
//	 * Time it took on my machine:
//	 * Serial: 23.920s
//	 * 
//	 * 
//	 */
//	@Test
//	public void testUser847293And200Samples() {
//		FileReader fileReader = new JsonFileReader();
//		ItemRecommendation itemRecommendation = new ItemRecommendation();
//		HashMap<Integer,User> users = fileReader.getUsers();
//		HashMap<Integer,Movie> movies = fileReader.getMovies();
//		ArrayList<Movie> recommendedMovies = itemRecommendation.generateRecommendationForUser(847293, users, movies,200);
//		assertEquals(recommendedMovies.get(0).getId(),904,0);
//		assertEquals(recommendedMovies.get(0).getRating(),4.2815156f,0.0000f);
//		assertEquals(recommendedMovies.get(21).getId(),1617,0);
//		assertEquals(recommendedMovies.get(21).getRating(),3.8915155f,0.0000f);
//		assertEquals(recommendedMovies.get(37).getId(),543,0);
//		assertEquals(recommendedMovies.get(37).getRating(),3.677034f,0.0000f);
//	}
//	/*
//	 * Time it took on my machine:
//	 * Serial: 91.085s
//	 * 
//	 * 
//	 */
//	@Test
//	public void testUser393887And5000Samples() {
//		FileReader fileReader = new JsonFileReader();
//		ItemRecommendation itemRecommendation = new ItemRecommendation();
//		HashMap<Integer,User> users = fileReader.getUsers();
//		HashMap<Integer,Movie> movies = fileReader.getMovies();
//		ArrayList<Movie> recommendedMovies = itemRecommendation.generateRecommendationForUser(393887, users, movies,5000);
//		assertEquals(recommendedMovies.get(0).getId(),114494,0);
//		assertEquals(recommendedMovies.get(0).getRating(),4.1772466f,0.0000f);
//		assertEquals(recommendedMovies.get(9).getId(),210861,0);
//		assertEquals(recommendedMovies.get(9).getRating(),3.7814474f,0.0000f);
//		assertEquals(recommendedMovies.get(22).getId(),160289,0);
//		assertEquals(recommendedMovies.get(22).getRating(),3.7178159f,0.0000f);
//		assertEquals(recommendedMovies.get(29).getId(),2624,0);
//		assertEquals(recommendedMovies.get(29).getRating(),3.6879025f,0.0000f);
//		assertEquals(recommendedMovies.get(40).getId(),367,0);
//		assertEquals(recommendedMovies.get(40).getRating(),3.6449683f,0.0000f);
//		assertEquals(recommendedMovies.get(48).getId(),6920,0);
//		assertEquals(recommendedMovies.get(48).getRating(),3.6000228f,0.0000f);
//	}
//
//}