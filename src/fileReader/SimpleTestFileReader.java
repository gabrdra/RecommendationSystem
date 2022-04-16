package fileReader;

import java.util.Arrays;

public class SimpleTestFileReader implements FileReader{

	@Override
	public float[][] retrieveData() {
		//float[][] array = new float[4][4];
		//for(float[]line:array)
		//	Arrays.fill(line, -1f);
		float[][] array = {
				{4f,3f,-1f,-1f},
				{2f,-1f,1f,-1f},
				{-1f,-1f,2f,4f},
				{2f,1f,4f,-1f}};
		return array;
	}

}
