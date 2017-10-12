package tatai.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The class Number creates a random number question for the user.
 * @author lucy
 *
 */

public class Number {

	// set range of values _number can take
	private int _minNumber = 1;
	private int _maxNumber = 9;

	//maximum and minimum values _number can take
	private final int MAX = 99; 
	private final int MIN = 1;

	//randomly generated number
	private int _numberOne;
	private int _numberTwo;
	private int  _number = 1000;
	//_number in Maori
	private String _maoriNumber;

	//Level chosen
	private LevelSelection _levelSelected;

	//equation for string label
	private String _equation;

	public Number(LevelSelection level){
		_levelSelected = level;
		generateEquation();
	}
	
	/**
	 * constructor to be used when you know what number you want
	 * @param number
	 */
	public Number(int number){
		_number = number;
		convertToMaori();
	}

	/**
	 * Constructor for Number which allows user to set the range of numbers which can be 
	 * generated. 
	 * @param minNumber
	 * @param maxNumber
	 * @throws NumberOutOfBoundsException (thrown if given numbers are out of range)
	 */
	public Number(int minNumber, int maxNumber, LevelSelection level) throws NumberOutOfBoundsException{
		if (minNumber < MIN || maxNumber > MAX){
			throw new NumberOutOfBoundsException("cannot generate numbers in that range");
		}
		_levelSelected = level;
		_minNumber = minNumber;
		_maxNumber = maxNumber;
		generateEquation();
	}
	
	public Number(int minNumber, int maxNumber) throws NumberOutOfBoundsException{
		if (minNumber < MIN || maxNumber > MAX){
			throw new NumberOutOfBoundsException("cannot generate numbers in that range");
		}
		_minNumber = minNumber;
		_maxNumber = maxNumber;
		generateNumber();
	}
	
	/**
	 * Generates new random number for _number to take
	 */
	
	public void generateNumber(){
		_maoriNumber = "";
		_number = ThreadLocalRandom.current().nextInt(_minNumber, _maxNumber);//comment out this line when testing
		//_number = 99; //Line used for testing
		convertToMaori();
	}

	/**
	 * Generates new random equation for _equation to take
	 */
	public void generateEquation(){
		_maoriNumber = "";
		int _level;
		//int _operand;
		//To select the operand to use for the level, easy and medium uses plus and minus, hard uses plus, minus, multiply and divide
		if (_levelSelected.equals(LevelSelection.EASY) || _levelSelected.equals(LevelSelection.MEDIUM)){
			_level = 3;
		}
		else{
			_level = 5;
		}
			//Generate an operand to use depending on the level selected
			int _operand = ThreadLocalRandom.current().nextInt(1, _level);
			//Generates numbers from 1-99 to addition and subtraction problems
			if(_operand < 3) {
				_number = ThreadLocalRandom.current().nextInt(_minNumber, _maxNumber + 1);//comment out this line when testing
				//_number = 99; //Line used for testing
				_numberTwo = ThreadLocalRandom.current().nextInt(_minNumber, _number + 1);
			}
			//Generates numbers from 1-10 for multiplication and division purposes
			else if (_operand == 3){
				_numberOne = ThreadLocalRandom.current().nextInt(_minNumber, 9);//comment out this line when testing
				//_number = 99; //Line used for testing
				_numberTwo = ThreadLocalRandom.current().nextInt(_minNumber, 9);
			}
			else {
				_number = ThreadLocalRandom.current().nextInt(_minNumber, 9);
				_numberTwo = ThreadLocalRandom.current().nextInt(_minNumber, 9);
			}

			
			System.out.println("apples "+_operand);
			//operand 1 is for addition problems
			if(_operand == 1) {
				
				_numberOne = _number - _numberTwo;
				_equation = _numberOne + " + " + _numberTwo;
			}
			//operand 2 is for subtraction problems
			else if (_operand == 2) {
				//Checks that first number is larger than the second number in the equation
//				if (_numberTwo > _numberOne) {
//					_number = _numberTwo - _numberOne;
//					_equation = _numberTwo + " - " + _numberOne;
//				}
//				else {
//					_number = _numberOne - _numberTwo;
//					_equation = _numberOne + " - " + _numberTwo;
//				}
				
				_numberOne = _number + _numberTwo;
				_equation = _numberOne + " - " +_numberTwo;
			}
			//operand 3 is for multiplication problems
			else if(_operand == 3) {
				_number = _numberOne * _numberTwo;
				_equation = _numberOne + " x " + _numberTwo;
			}
			//operand 4 is for division problems
			else {
				_numberOne = _number * _numberTwo; 
				_equation = _numberOne + " \u00F7 " + _numberTwo;
				System.out.println(_equation);
			}
		
		convertToMaori();
		//System.out.println(_maoriNumber);
	}

	/**
	 * Generates corresponding Maori pronounciation of _number.
	 */
	public void convertToMaori(){
		int temp = _number;
		int digit = temp % 10;
		int placeHolder = 0;

		if (digit > 0){
			_maoriNumber = MaoriNumbers.getValue(digit).toString();
			if (temp > 10) {
				_maoriNumber = "maa" + "\n" + _maoriNumber; //note /n because the recording records seperate words on seperate lines
			}
		} else if (digit == 0){
			while (digit == 0) {
				temp = temp/10;
				digit = temp % 10;
				placeHolder++;
			}
			_maoriNumber = MaoriNumbers.getValue((int)Math.pow(10, placeHolder)).toString();
			if (digit > 1){
				_maoriNumber = MaoriNumbers.getValue(digit).toString() + "\n" + _maoriNumber;
			}
		}

		while (temp > 9) {
			placeHolder++;
			temp = temp/10;
			digit = temp % 10;
			_maoriNumber = MaoriNumbers.getValue((int)Math.pow(10, placeHolder)).toString() + "\n" + _maoriNumber;
			if (digit > 1){
				_maoriNumber = MaoriNumbers.getValue(digit).toString() + "\n" + _maoriNumber;
			}
		}
	}

	/**
	 * Returns the number
	 */
	public Object getQuiz() {
		return _number;
	}

	/**
	 * Returns true if string is the same as the Maori pronounciation of _number. Otherwise false is returned.
	 */
	public boolean compare(String string) {
		if (_maoriNumber.equals(string)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the equation for the label
	 */
	public String getEquation(){
		return _equation;
	}

}
