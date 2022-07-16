//package application;
//import java.util.HashMap;
//import org.openjdk.jmh.annotations.Benchmark;
//import org.openjdk.jmh.annotations.Fork;
//import org.openjdk.jmh.annotations.Scope;
//import org.openjdk.jmh.annotations.Setup;
//import org.openjdk.jmh.annotations.State;
//import fileReader.FileReader;
//import fileReader.JsonFileReader;
//import model.Movie;
//import model.User;
//import recommendation.ItemRecommendation;
//@State(Scope.Benchmark)
//@Fork(1)
//public class RecommendationSystemBenchmark {
//	private static HashMap<Integer,User> users;
//	private static HashMap<Integer,Movie> movies;
//	@Setup
//	public static final void setup() {
//		FileReader fileReader = new JsonFileReader();
//		users = fileReader.getUsers();
//		movies = fileReader.getMovies();
//	}
//	@Benchmark
//	@Fork(value = 1)
//	public void recommendationsForUserWith5000Samples() {
//		ItemRecommendation ir = new ItemRecommendation();
//		ir.generateRecommendationForUser(393887, new HashMap<Integer,User>(users), new HashMap<Integer,Movie>(movies), 5000);
//	}
//	@Benchmark
//	@Fork(value = 1)
//	public void recommendationsForUserWith2500Samples() {
//		ItemRecommendation ir = new ItemRecommendation();
//		ir.generateRecommendationForUser(393887, new HashMap<Integer,User>(users), new HashMap<Integer,Movie>(movies), 2500);
//	}
//	@Benchmark
//	@Fork(value = 1)
//	public void recommendationsForUserWith250Samples() {
//		ItemRecommendation ir = new ItemRecommendation();
//		ir.generateRecommendationForUser(393887, new HashMap<Integer,User>(users), new HashMap<Integer,Movie>(movies), 250);
//	}
//}
