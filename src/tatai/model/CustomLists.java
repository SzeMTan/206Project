package tatai.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class CustomLists {

	private ArrayList<String> _nameOfList = new ArrayList<String>();
	private ArrayList<String> _comments = new ArrayList<String>();
	private ArrayList<ArrayList<String>> _equations = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<Integer>> _answers = new ArrayList<ArrayList<Integer>>();

	private static CustomLists _instance = null;

	private CustomLists() {}

	public static CustomLists getInstance() {
		Gson gsonLoad = new Gson();
		File jsonFile = new File("customLists.json");
		if (_instance == null && jsonFile.exists()) {
			try {
				_instance = gsonLoad.fromJson(new FileReader(jsonFile),CustomLists.class);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				e.printStackTrace();
			}
		} 
		if (_instance == null){
			_instance = new CustomLists();
		}
		return _instance;
	}

	public void deleteList(int index) {
		_nameOfList.remove(index);
		_equations.remove(index);
		_answers.remove(index);
		_comments.remove(index);
	}

	public void updateList(int index, String name, String comments, ArrayList<String> equations, ArrayList<Integer> answers) {
		if (index != -1) {
			_nameOfList.set(index, name);
			_comments.set(index, comments);
			_equations.set(index, equations);
			_answers.set(index, answers);
		} else {
			_nameOfList.add(name);
			_comments.add(comments);
			_equations.add(equations);
			_answers.add(answers);
		}
	}

	public ArrayList<String> getLists() {
		return _nameOfList;
	}

	public String getComment(int index) {
		return _comments.get(index);
	}

	public ArrayList<String> getEquations(int index){
		return _equations.get(index);
	}

	public ArrayList<Integer> getAnswer(int index){
		return _answers.get(index);
	}
}
