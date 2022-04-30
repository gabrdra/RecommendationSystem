package application;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import fileReader.FileReader;
import fileReader.JsonFileReader;
import model.Movie;
import model.User;
import recommendation.ItemRecommendation;
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(1)
public class RecommendationSystemBenchmark {
	private static HashMap<Integer,User> users;
	private static HashMap<Integer,Movie> movies;
	@Setup
	public static final void setup() {
		FileReader fileReader = new JsonFileReader();
		users = fileReader.getUsers();
		movies = fileReader.getMovies();
	}
	@Benchmark
	@Fork(value = 1)
	public void recommendationsForUser() {
		ItemRecommendation ir = new ItemRecommendation();
		ir.generateRecommendationForUser(393887, new HashMap<Integer,User>(users), new HashMap<Integer,Movie>(movies), 5000);
	}
}
