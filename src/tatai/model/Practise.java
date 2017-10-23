package tatai.model;

import java.util.concurrent.ThreadLocalRandom;

public class Practise extends Level {
	private final int MIN = 1;
	private final int MAX = 99;
	
	private int _number = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);

	@Override
	public String getQuestion() {
		return Integer.toString(_number);
	}

	@Override
	public void generateQuestion() {
		_number = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
		convertToMaori(_number); //get Maori translation of answer
	}

}
