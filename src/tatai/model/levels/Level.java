package tatai.model.levels;

public abstract class Level {

	private String _maoriNumber;
	
	/**
	 * Generates corresponding Maori pronounciation of _number.
	 */
	public void convertToMaori(int number){
		int temp = number;
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
	 * Returns the converted maori of the specified number
	 */
	public String getMaori() {
		return _maoriNumber;
	}
	
	/**
	 * returns generated question
	 * @return
	 */
	public abstract String getQuestion();
	
	/**
	 * generates question for the level
	 */
	public abstract void generateQuestion();
	
}
