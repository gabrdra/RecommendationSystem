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

import org.apache.cxf.jaxrs.model.UserApplication;
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
		//Map the data to a JavaPairRDD that contains the user_id, movie_id and the rating
		List<Tuple2<Integer, Float>> myUserMoviesList = new ArrayList<>();
		JavaPairRDD<Integer, Tuple2<Integer, Float>> ratings = rows.mapToPair(row -> {
			if(row.getInt(1) == userId) {
				myUserMoviesList.add(new Tuple2<>(row.getInt(0), row.getFloat(2)));
			}
			return new Tuple2<>(row.getInt(1), new Tuple2<>(row.getInt(0), row.getFloat(2)));
		});
		float userAverageRatingLocal = 0;
		for(Tuple2<Integer, Float> tuple : myUserMoviesList) {
			userAverageRatingLocal += tuple._2;
		}
		userAverageRatingLocal /= myUserMoviesList.size();
		final float userAverageRating = userAverageRatingLocal;
		//final List<Tuple2<Integer, Float>> myUserMovies = myUserMoviesTemp;
		//Calculate the cosine similarity between the user and the other users using the myUserMovies list
		JavaPairRDD<Float, Tuple2<Integer, Iterable<Tuple2<Integer, Float>>>> cosineSimilarities = ratings.groupByKey()
				.filter(tuple -> tuple._1 != userId)
				.mapToPair(tuple -> {
					float result = 0;
					int numberOfMoviesRatedInCommom = 0;
					float userSumOfDeltas = 0f; //This is the squared deviation between a given movie and the remaining movies rated by the user
					float otherSumOfDeltas = 0f;
					float cosineSimilarity = 0f;
					float otherAverageRating = 0f;
					int iterations = 0;
					for(Tuple2<Integer, Float> movie : tuple._2) {
						otherAverageRating += movie._2;
						iterations++;
					}
					otherAverageRating /= iterations;
					for(Tuple2<Integer, Float> otherMovie : tuple._2) {
						for(Tuple2<Integer, Float> userMovie : myUserMoviesList) {
							if(userMovie._1==otherMovie._1) {
								numberOfMoviesRatedInCommom++;
								float userMovieDelta = userMovie._2-userAverageRating;
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
//					System.out.println("NM: "+numberOfMoviesRatedInCommom+ "CS: "+cosineSimilarity
//							+"US: "+userSumOfDeltas+" OS: "+otherSumOfDeltas);
					return new Tuple2<Float,Tuple2<Integer, Iterable<Tuple2<Integer, Float>>>>(result, tuple);
				});
		//Sort the cosine similarities in descending order
		JavaPairRDD<Float, Tuple2<Integer, Iterable<Tuple2<Integer, Float>>>> sortedCosineSimilarities = cosineSimilarities.sortByKey(false);
		sortedCosineSimilarities.foreach(tuple->{
			System.out.println("User: " + tuple._2._1 + " Cosine similarity: " + tuple._1);
		});
		//Get the top users with the highest cosine similarity and put it in a list
//		List<Tuple2<Float, Tuple2<Integer, Iterable<Tuple2<Integer, Float>>>>> topUsers = sortedCosineSimilarities.take(numberOfUsersComparatedTo);
//		//Print top users list
//		topUsers.forEach(tuple -> {
//			System.out.println("User: " + tuple._2._1 + " Cosine similarity: " + tuple._1);
//		});


		return null;
	}

}