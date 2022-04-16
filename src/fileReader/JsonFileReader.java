package fileReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import model.User;

public class JsonFileReader implements FileReader {

	public static final String path = "E:\\UFRN\\P7\\Concorrente\\ratings.json";
	@Override
	public ArrayList<User> retrieveData() {
		ArrayList<User> data = new ArrayList<User>();
		
		try {
			Stream inputStream = Files.lines(Paths.get(path));
			inputStream.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}

}
