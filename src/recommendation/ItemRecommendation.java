package recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import model.Movie;
import model.User;
import org.apache.spark.api.java.JavaSparkContext;
import org.codehaus.janino.Java;
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


	public void generateRecommendationForUser(int userId, int numberOfUsersComparatedTo) {
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
		Dataset<Row> rows = sparkSession.read().format("json")
				.option("multiline", "true")
				.schema(schema)
				.load("E:\\UFRN\\P7\\Concorrente\\ratingsfixed.json");
		rows.createOrReplaceTempView("ratings");
		Dataset<Row> users = sparkSession.sql("SELECT * FROM ratings");
		JavaSparkContext sc = new JavaSparkContext(sparkSession.sparkContext());
//		Dataset<Row> myUser = sparkSession.sql("SELECT * FROM ratings WHERE user_id = " + userId);
//		myUser.show();
//		//Select all other users that are not myuser
//
//		Dataset<Row> otherUsers = sparkSession.sql("SELECT * FROM ratings WHERE user_id != " + userId);
//		//Creates a Dataset of the cosine similarities between myuser and other users
//		Dataset<Row> cosineSimilarities = sparkSession.sql("SELECT user_id, cosine_similarity(ratings.rating, otherUsers.rating) AS similarity " +
//				"FROM ratings AS myUser, ratings AS otherUsers WHERE myUser.user_id = " + userId + " AND otherUsers.user_id != " + userId);
		JavaPairRDD<Integer, Tuple2<Integer, Float>> ratings = users.javaRDD().mapToPair(row -> {
			return new Tuple2<>(row.getInt(1), new Tuple2<>(row.getInt(0), row.getFloat(2)));
		});
		JavaPairRDD<Integer, Iterable<Tuple2<Integer, Float>>> groupedRatings = ratings.groupByKey().filter(tuple -> tuple._1 != userId).filter(tuple-> tuple!=null);
		List<Tuple2<Integer, Iterable<Tuple2<Integer, Float>>>> myUser = ratings.groupByKey().filter(tuple -> tuple._1 == userId).filter(tuple-> tuple!=null).collect();
		List<Tuple2<Integer, Float>> myUserMoviesList = new ArrayList<>();
		for (Tuple2<Integer, Float> item : myUser.get(0)._2) {
			myUserMoviesList.add(item);
		}
//		System.out.println("ratingsSize: "+ratings.count());
//		System.out.println("groupedRatings: "+groupedRatings.count());
//		System.out.println("myUserMoviesListSize: "+myUserMoviesList.size());

		float userAverageRating = 0f;
		for(Tuple2<Integer, Float> tuple : myUserMoviesList) {
			userAverageRating += tuple._2;
		}
		userAverageRating /= myUserMoviesList.size();
		float finalUserAverageRating = userAverageRating;
		List<Tuple2<Float, Iterable<Tuple2<Integer,Float>>>> othersWithSimilarity = new ArrayList<>();
		groupedRatings.take(numberOfUsersComparatedTo).forEach((other)->{
			float result = -2f;
			int numberOfMoviesRatedInCommom = 0;
			float userSumOfDeltas = 0f; //This is the squared deviation between a given movie and the remaining movies rated by the user
			float otherSumOfDeltas = 0f;
			float cosineSimilarity = 0f;
			float otherAverageRating = 0f;
			int iterations = 0;
			for(Tuple2<Integer, Float> tuple : other._2) {
				otherAverageRating += tuple._2;
				iterations++;
			}
			otherAverageRating /= iterations;
			for(Tuple2<Integer, Float> userMovie:myUserMoviesList) {
				for(Tuple2<Integer, Float> otherMovie:other._2) {
					if(userMovie.equals(otherMovie)) {
						numberOfMoviesRatedInCommom++;
						float userMovieDelta = userMovie._2-finalUserAverageRating;
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
			if(result!=-2f) {

				othersWithSimilarity.add(new Tuple2<>(result, other._2));
			}
		});

//		System.out.println("List: "+othersWithSimilarity.size());
		JavaRDD<Tuple2<Float, Iterable<Tuple2<Integer,Float>>>> similarityRDD =
				sc.parallelize(othersWithSimilarity);
//		System.out.println("Similarity: "+similarityRDD.count());
		JavaPairRDD<Float, Iterable<Tuple2<Integer,Float>>> similarityWithMoviesSorted =
				JavaPairRDD.fromJavaRDD(similarityRDD).sortByKey(false);
//		System.out.println("Movies: "+similarityWithMoviesSorted.count());
//		similarityWithMoviesSorted.take(numberOfUsersComparatedTo).forEach(tuple->{
//			System.out.println(" Cosine similarity: " + tuple._1);
//		});
//		JavaPairRDD<Integer,Float> moviesUngrouped = rows.mapToPair(row -> {
//			return new Tuple2<Integer,Float>(row.getInt(0), row.getFloat(2));
//		});
		Dataset<Row> movies = sparkSession.sql("SELECT DISTINCT item_id FROM ratings");
		JavaPairRDD<Integer,Float> moviesGrouped = movies.javaRDD().mapToPair(row -> {
			return new Tuple2<>(row.getInt(0), 0f);
		});
//		moviesGrouped.foreach(tuple->{
//			System.out.println(" movie: " + tuple._1+" rating: "+tuple._2);
//		});
		List<Tuple2<Float, Iterable<Tuple2<Integer, Float>>>> usersList = similarityWithMoviesSorted.collect();
		//System.out.println("Before");
		JavaPairRDD<Float,Integer> moviesPredictions = moviesGrouped.mapToPair(movie->{
			//System.out.println(movie._1+" "+movie._2);
			int counter = 0;
			float normalizedSumOfSimilaritty = 0f;
			float total = 0f;
			for(int i = 0; i<usersList.size();i++) {
				Tuple2<Float, Iterable<Tuple2<Integer, Float>>> other = usersList.get(i);
				float otherAverageRating = 0f;
				int iterations = 0;
				for(Tuple2<Integer, Float> tuple : other._2) {
					otherAverageRating += tuple._2;
					iterations++;
				}
				otherAverageRating /= iterations;
				Iterable<Tuple2<Integer, Float>> otherMovies = other._2;
				for(Tuple2<Integer, Float>otherMovie: otherMovies) {
					//System.out.println(otherMovie._1+" OM "+otherMovie._2);
					if(otherMovie._1.equals(movie._1)) {
						//System.out.println("Inside if");
						counter++;
						//System.out.println(counter);
						normalizedSumOfSimilaritty += Math.abs(other._1);
						total += (otherMovie._2-otherAverageRating)*other._1;
						break;
					}
				}
			}
			//System.out.println("Outside if: "+counter);
			if(counter>5){// only adds a movie if it has being rated by other users
				float predictedRating = finalUserAverageRating+(total/normalizedSumOfSimilaritty);
				return new Tuple2<Float,Integer>(predictedRating,movie._1);
				//recommendedMovies.add(new Movie(movieId,predictedRating));
			}
			return null;
		});
		//System.out.println("After, Movies Predictions size: "+moviesPredictions.count());
		moviesPredictions.filter(tuple-> tuple!=null).sortByKey(false).take(50).forEach(movie->{
			System.out.println("MovieId:"+movie._2+" Predicted Rating:"+movie._1);
		});
	}

}