package tatai.model;

public interface Quiz {

	/**
	 * generates new value for quiz
	 */
	public void generateNew();
	/**
	 * gets Maori string value of quiz
	 */
	public Object getQuiz();
	/**
	 * compares string value of quiz to other string. Will return true
	 * if they are equal. Otherwise will return false.
	 * @param string is string to be compared
	 */
	public boolean compare(String string);
}
