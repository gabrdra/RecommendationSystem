package recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import model.Movie;
import model.User;
import scala.Int;
import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class ItemRecommendation {


	public ArrayList<Movie> generateRecommendationForUser(int userId, int numberOfUsersComparatedTo) {
		SparkSession sparkSession = SparkSession.builder().appName("Recommendation system").master("local[*]").getOrCreate();
		StructType schema = DataTypes.createStructType(new StructField[]{
			DataTypes.createStructField(
				"item_id",
				DataTypes.IntegerType,
				false
			), DataTypes.createStructField(
				"user_id",
				DataTypes.IntegerType,
				false
			), DataTypes.createStructField(
				"rating",
				DataTypes.FloatType,
				false
			)
		});
		JavaRDD<Row> rows = sparkSession.read().format("json")
				.option("multiline", "true")
				.schema(schema)
				.load("E:\\UFRN\\P7\\Concorrente\\ratingsfixed.json")
				.toJavaRDD();
		List<Movie> myUserMovies = new ArrayList<Movie>();
		HashMap<Integer,List<Movie>> othersMovies = new HashMap<Integer,List<Movie>>();
		JavaPairRDD<Integer, Integer> users = rows.mapToPair(row->{
			if(row.getInt(1) == userId) {
				myUserMovies.add(new Movie(row.getInt(0), row.getFloat(2)));
				return null;
			}
			Tuple2<Integer, Integer> returntuple2 = new Tuple2<>(row.getInt(1),row.getInt(0));
			if(!othersMovies.containsKey(othersMovies)) {
				List<Movie> movies = new ArrayList<>();
				movies.add(new Movie(row.getInt(0), row.getFloat(2)));
				othersMovies.put(returntuple2._1, movies);
			}
			return returntuple2;
		});
		JavaPairRDD<Integer, List<Tuple2<Integer, Float>>> usersWithMovies = users.groupByKey().mapToPair(tuple->{
			List<Tuple2<Integer, Float>> movies = new ArrayList<>();
			othersMovies.get(tuple._1).forEach(movie->{
				movies.add(new Tuple2<>(movie.getId(), movie.getRating()));
			});
			return new Tuple2<>(tuple._1, movies);
		});
//		users.foreach(row->{if(row==null){System.out.println("null");}else{{System.out.println(row._1 + row._2.toString());}}});
		//Calculate the cosine similarity
		JavaPairRDD<Float, Integer> cosineSimilarities = usersWithMovies.mapToPair(other->{
//			 Tuple2 <Float,Integer> returnValue = new Tuple2<Float,Integer>(cosineSimilarity(myUserMovies, other._2),other._1);
			Float result = 0f;
			int numberOfMoviesRatedInCommom = 0;
			float userSumOfDeltas = 0f; //This is the squared deviation between a given movie and the remaining movies rated by the user
			float otherSumOfDeltas = 0f;
			float cosineSimilarity = 0f;
			float userAverageRating = 0f;
			for(Movie movie : myUserMovies) {
				userAverageRating += movie.getRating();
			}
			userAverageRating /= myUserMovies.size();
			float otherAverageRating = 0f;
			int iterations = 0;
			for(Tuple2<Integer, Float> movie : other._2) {
				otherAverageRating += movie._2;
				iterations++;
			}
			otherAverageRating /= iterations;
			otherAverageRating /= iterations;
			for(Movie userMovie:myUserMovies) {
				for(Tuple2<Integer, Float> otherMovie:other._2) {
					if(userMovie.getId()==otherMovie._1) {
						numberOfMoviesRatedInCommom++;
						float userMovieDelta = userMovie.getRating()-userAverageRating;
						float otherMovieDelta = otherMovie._2-otherAverageRating;
						cosineSimilarity += userMovieDelta*otherMovieDelta;
						userSumOfDeltas += userMovieDelta*userMovieDelta;
						otherSumOfDeltas += otherMovieDelta*otherMovieDelta;
					}
				}
			}
			if(numberOfMoviesRatedInCommom>1&&cosineSimilarity!=0f&&userSumOfDeltas!=0f&&otherSumOfDeltas!=0f) {
				cosineSimilarity /= Math.sqrt(userSumOfDeltas*otherSumOfDeltas);
				result = cosineSimilarity;
			}
			Tuple2 <Float,Integer> returnValue = new Tuple2<Float,Integer>(result ,other._1);
			return returnValue;
		});
		//Get the firsts with the highest compatibility
		//cosineSimilarities.foreach(other ->{System.out.println(other);});
		System.out.println(cosineSimilarities);
		cosineSimilarities.foreach(row->{if(row==null){System.out.println("null");}else{System.out.println("notnull");}});
		//cosineSimilarities.foreach(row->{if(row==null){System.out.println("null");}else{{System.out.println(row._1 + row._2.toString());}}});
		//Make a JavaPairRDD<Integer, Float> of movies aggregating by the item_id
		//JavaPairRDD<Integer, Float> movies = rows.mapToPair(row->{new Tuple});
		return null;
	}
	
}