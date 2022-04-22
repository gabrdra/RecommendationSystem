package fileReader;

import java.util.HashMap;

import model.User;

public interface FileReader {

	public HashMap<Integer, User> retrieveData();
	
}
