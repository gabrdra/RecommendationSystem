package fileReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import model.Movie;
import model.User;

public class JsonFileReader implements FileReader {

	public static final String path = "E:\\UFRN\\P7\\Concorrente\\ratings.json";
	@Override
	public HashMap<Integer,User> retrieveData() {
		HashMap<Integer,User> data = new HashMap<Integer,User>();
		
        try(Stream<String> inputStream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)){
        	Consumer<String> consumer = line->{
        		String[] jsonParts = line.split(", ");
        		int userId = Integer.parseInt(jsonParts[1].substring(11));
        		User user = data.get(userId);
        		if(user != null) {
        			int movieId = Integer.parseInt(jsonParts[0].substring(12));
        			float rating = Float.parseFloat(jsonParts[2].substring(10,13));
        			Movie movie = new Movie(movieId, rating);
        			user.addMovie(movie);
        		}
        		else {
        			user = new User(userId);
        			int movieId = Integer.parseInt(jsonParts[0].substring(12));
        			float rating = Float.parseFloat(jsonParts[2].substring(10,13));
        			Movie movie = new Movie(movieId, rating);
        			user.addMovie(movie);
        			data.put(userId, user);
        		}
        		//System.out.println(jsonParts[0]+" split "+jsonParts[0].substring(12));
        		//System.out.println(jsonParts[1]+" split "+jsonParts[1].substring(11));
        		//System.out.println(jsonParts[2]+" split "+jsonParts[2].substring(10,13));
        		};
        	inputStream.forEach(consumer);
//        	Iterator<String> streamIterator = inputStream.iterator();
//        	while(streamIterator.hasNext()) {
//        		String[] jsonParts = streamIterator.next().split(", ");
//        		System.out.println(jsonParts[0]);
//        		System.out.println(jsonParts[1]);
//        		System.out.println(jsonParts[2]);
//        	}
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}
