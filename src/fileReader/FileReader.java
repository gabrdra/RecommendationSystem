package fileReader;

import java.util.HashMap;

import model.Movie;
import model.User;

public interface FileReader {
	
	public HashMap<Integer,User> getUsers();
	public HashMap<Integer,Movie> getMovies();
}
