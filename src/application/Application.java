package application;

import java.util.HashMap;

import fileReader.FileReader;
import fileReader.JsonFileReader;
import model.User;

public class Application {

	public static void main(String[] args) {
		FileReader fileReader = new JsonFileReader();
		HashMap<Integer,User> users = fileReader.retrieveData();
		System.out.println(users.get(997206).getMovies().toString());
	}

}
