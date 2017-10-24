package tatai.model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class CustomLevel extends Level {

	private ArrayList<Integer> _answers;
	private ArrayList<String> _equations;
	private int _numEquations;
	private String _question; 

	public CustomLevel(int index) {
		CustomLists _customLists = CustomLists.getInstance();//contains all custom lists
		
		if (index < 0 || index >= _customLists.getLists().size()) { //means index doesn't correspond to any list
			throw new IllegalArgumentException();
		} else {
			//get equations and answers of that list
			_equations = _customLists.getEquations(index);
			_answers = _customLists.getAnswer(index);
			
			//get number of equations in that list
			_numEquations = _customLists.getEquations(index).size();
		}
	}

	@Override
	public String getQuestion() {
		return _question;
	}

	@Override
	public void generateQuestion() {
		int question = ThreadLocalRandom.current().nextInt(0, _numEquations);
		_question = _equations.get(question);
		int answer = _answers.get(question);
		convertToMaori(answer);
	}

}
