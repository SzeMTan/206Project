package tatai.model;

import java.util.concurrent.ThreadLocalRandom;

public class EasyLevel extends Level {
	
	private int _answer;
	private int _numberTwo;
	private int _numberOne;
	
	private String _equation;
	
	private final int MIN = 1;
	private final int MAX = 9;

	@Override
	public String getQuestion() {
		return _equation;
	}

	@Override
	public void generateQuestion() {
		int operand = ThreadLocalRandom.current().nextInt(1, 3); //choose operand
		_answer = ThreadLocalRandom.current().nextInt(MIN, MAX + 1); //make sure answer is from 1 to 9
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
