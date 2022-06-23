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
	private HashMap<Integer,User> usersData;
	private HashMap<Integer,Movie> moviesData;
	
	public JsonFileReader() {retrieveData();};
	
	private void retrieveData() {
		usersData = new HashMap<Integer,User>();
		moviesData = new HashMap<Integer,Movie>();
		Object usersLock = new Object();
        try(Stream<String> inputStream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)){
        	Consumer<String> consumer = line->{
        		String[] jsonParts = line.split(", ");
        		int userId = Integer.parseInt(jsonParts[1].substring(11));
        		int movieId = Integer.parseInt(jsonParts[0].substring(12));
        		float rating = Float.parseFloat(jsonParts[2].substring(10,13));
        		synchronized (usersLock) {
        			User user = usersData.get(userId);
            		if(user != null) {
            			Movie movie = new Movie(movieId, rating);
            			user.addMovie(movie);
            		}
            		else {
            			user = new User(userId);
            			Movie movie = new Movie(movieId, rating);
            			user.addMovie(movie);
            			usersData.put(userId, user);
            		}
            		if(!moviesData.containsKey(movieId)) {
            			Movie movie = new Movie(movieId,-1f);
            			moviesData.put(movieId, movie);
            		}
				}
        		
        		//System.out.println(jsonParts[0]+" split "+jsonParts[0].substring(12));
        		//System.out.println(jsonParts[1]+" split "+jsonParts[1].substring(11));
        		//System.out.println(jsonParts[2]+" split "+jsonParts[2].substring(10,13));
        		};
        	inputStream.parallel().forEach(consumer);
        	inputStream.close();
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
	}

	@Override
	public HashMap<Integer, User> getUsers() {
		// TODO Auto-generated method stub
		return usersData;
	}

	@Override
	public HashMap<Integer, Movie> getMovies() {
		// TODO Auto-generated method stub
		return moviesData;
	}

}
