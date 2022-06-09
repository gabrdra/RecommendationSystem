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
		ArrayList<Movie> moviePredictions = ir.generateRecommendationForUser(393887,users,movies, 2500);
		if(moviePredictions.get(0).getId() == 8928 && moviePredictions.get(0).getRating() == 4.185441f
				&& moviePredictions.get(15).getId() == 590 && moviePredictions.get(15).getRating() == 3.729213f
				&& moviePredictions.get(45).getId() == 96432 && moviePredictions.get(45).getRating() == 3.6171794f) {
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
