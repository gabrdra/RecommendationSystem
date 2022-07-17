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
		Dataset<Row> rows = sparkSession.read().format("json")
				.option("multiline", "true")
				.schema(schema)
				.load("E:\\UFRN\\P7\\Concorrente\\ratingsfixed.json");
		rows.createOrReplaceTempView("ratings");
		Dataset<Row> myUser = sparkSession.sql("SELECT * FROM ratings WHERE user_id = " + userId);
		myUser.show();
		//Select all other users that are not myuser
		String cosineSimilarity ="SELECT item_id, rating, FROM ratings WHERE user_id == " + userId;
		Dataset<Row> otherUsers = sparkSession.sql("SELECT * FROM ratings WHERE user_id != " + userId);
		//Creates a Dataset of the cosine similarities between myuser and other users
		Dataset<Row> cosineSimilarities = sparkSession.sql("SELECT user_id, cosine_similarity(ratings.rating, otherUsers.rating) AS similarity " +
				"FROM ratings AS myUser, ratings AS otherUsers WHERE myUser.user_id = " + userId + " AND otherUsers.user_id != " + userId);



		return null;
	}

}