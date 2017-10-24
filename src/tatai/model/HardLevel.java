package tatai.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class extends the parent Level and generates questions which have an answer that is between 1 and 99 with all four operands
 * @author se206
 *
 */

public class HardLevel extends Level {
	private int _answer;
	private int _numberTwo;
	private int _numberOne;

	private String _equation;

	private final int MIN = 1;
	private final int MAXADDSUB = 99;
	private final int MAXMULTDIV = 9;
	
	//Returns the string that represents the question that was randomly generated.
	@Override
	public String getQuestion() {
		return _equation;
	}

	//Randomly generates questions that have the operand of either addition, subtraction, multiplication and division
	// with the answer between 1 and 99
	// and converts the answer to Maori for recognition.
	@Override
	public void generateQuestion() {
		int operand = ThreadLocalRandom.current().nextInt(1, 5); //choose operand
		if (operand <= 2 ) {
			_answer = ThreadLocalRandom.current().nextInt(MIN, MAXADDSUB + 1); //make sure answer is from 1 to 99
			if(operand == 1) { // means it's addition
				_numberTwo = ThreadLocalRandom.current().nextInt(MIN, _answer + 1);
				_numberOne = _answer - _numberTwo;
				_equation = _numberOne + " + " + _numberTwo;
			} else { // means it's subtraction
				_numberTwo = ThreadLocalRandom.current().nextInt(MIN, MAXADDSUB + 1);
				_numberOne = _answer + _numberTwo;
				_equation = _numberOne + " - " +_numberTwo;
			}
		} else {
			if (operand == 3) { // means it's multiplication
				_numberOne = ThreadLocalRandom.current().nextInt(MIN, MAXMULTDIV + 1);
				_numberTwo = ThreadLocalRandom.current().nextInt(MIN, MAXMULTDIV + 1);
				_answer = _numberOne * _numberTwo;
				_equation = _numberOne + " x " + _numberTwo;
			} else { // means it's division
				_answer = ThreadLocalRandom.current().nextInt(MIN, MAXMULTDIV + 1);
				_numberTwo = ThreadLocalRandom.current().nextInt(MIN, MAXMULTDIV + 1);
				_numberOne = _answer * _numberTwo;
				_equation = _numberOne + " \u00F7 " + _numberTwo;
			}
		}
		convertToMaori(_answer); //get Maori translation of answer
	}

}
