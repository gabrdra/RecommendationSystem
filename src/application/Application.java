package application;

import java.util.HashMap;

import fileReader.FileReader;
import fileReader.JsonFileReader;
import model.User;
import recommendation.ItemRecommendation;

public class Application {

	public static void main(String[] args) {
		FileReader fileReader = new JsonFileReader();
		ItemRecommendation itemRecommendation = new ItemRecommendation();
		HashMap<Integer,User> users = fileReader.retrieveData();
		itemRecommendation.generateRecommendationForUser(997206, users, 10);
		//System.out.println(users.get(997206).getMovies().toString());
	}

}
