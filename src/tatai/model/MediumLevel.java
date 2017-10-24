package tatai.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class extends the parent Level and generates questions which have an answer that is between 1 and 99 with addition and subtraction
 * @author se206
 *
 */
public class MediumLevel extends Level {

	private int _answer;
	private int _numberTwo;
	private int _numberOne;
	
	private String _equation;
	
	private final int MIN = 1;
	private final int MAX = 99;

	//Returns the string that represents the question that was randomly generated.
	@Override
	public String getQuestion() {
		return _equation;
	}

	//Randomly generates questions that have the operand of either addition or subtraction
	// and converts the answer to Maori for recognition.
	@Override
	public void generateQuestion() {
		int operand = ThreadLocalRandom.current().nextInt(1, 3); //choose operand
		_answer = ThreadLocalRandom.current().nextInt(MIN, MAX + 1); //make sure answer is from 1 to 99
		if(operand == 1) { // means it's addition
			_numberTwo = ThreadLocalRandom.current().nextInt(MIN, _answer + 1);
			_numberOne = _answer - _numberTwo;
			_equation = _numberOne + " + " + _numberTwo;
		} else { // means it's subtraction
			_numberTwo = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
			_numberOne = _answer + _numberTwo;
			_equation = _numberOne + " - " +_numberTwo;
		}
		convertToMaori(_answer); //get Maori translation of answer
	}

}
