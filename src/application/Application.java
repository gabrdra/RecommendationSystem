package application;


import fileReader.FileReader;
import fileReader.SimpleTestFileReader;

public class Application {

	public static void main(String[] args) {
		FileReader fileReader = new SimpleTestFileReader();
		fileReader.retrieveData();
	}

}
