package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import fileReader.FileReader;
import fileReader.JsonFileReader;
import model.Movie;
import model.User;
import recommendation.ItemRecommendation;

public class JMeterTest  extends AbstractJavaSamplerClient implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2915179084626452758L;

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult result = new SampleResult();
		result.sampleStart();
		result.setSampleLabel("Test Sample");
		
		FileReader fileReader = new JsonFileReader();
		HashMap<Integer, User> users = fileReader.getUsers();
		HashMap<Integer, Movie> movies = fileReader.getMovies();
		ItemRecommendation ir = new ItemRecommendation();
		ArrayList<Movie> moviePredictions = ir.generateRecommendationForUser(997206, users, movies,1000);
		if(moviePredictions.get(0).getId() == 1304 && moviePredictions.get(0).getRating() == 4.998936f
				&& moviePredictions.get(24).getId() == 1148 && moviePredictions.get(24).getRating() == 4.4793544f
				&& moviePredictions.get(46).getId() == 1094 && moviePredictions.get(46).getRating() == 4.3713636f) {
			result.sampleEnd();
			result.setResponseCode("200");
			result.setResponseMessage("OK");
			result.setSuccessful(true);
		}else {
			result.sampleEnd();
			result.setResponseCode("500");
			result.setResponseMessage("Results were different than expected.");
			result.setSuccessful(false);
		}
		return result;
	}

}
