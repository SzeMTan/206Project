package tatai.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class extends the parent Level and generates random numbers between 1 and 99 for practice.
 * @author se206
 *
 */
public class Practise extends Level {
	private final int MIN = 1;
	private final int MAX = 99;
	
	private int _number = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);

	//Returns the string that represents the number that was randomly generated.
	@Override
	public String getQuestion() {
		return Integer.toString(_number);
	}

	//Generates a random number and converts it to Maori for recognition.
	@Override
	public void generateQuestion() {
		_number = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
		convertToMaori(_number); //get Maori translation of answer
	}

}
