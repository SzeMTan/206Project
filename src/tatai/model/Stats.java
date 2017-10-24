package tatai.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * Singleton stats object
 * @author lucy
 *
 */
public class Stats {

	private static Stats _statsInstance = null;
	
	//stores previous 10 results and used for data display on bar graph. Initialised to -1 before any user input
	private Integer[] _resultsEasy = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private Integer[] _resultsMedium = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private Integer[] _resultsHard = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private Integer[] _resultsCustom = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	//store max score for each level
	private int _maxEasy = -1;
	private int _maxMedium = -1;
	private int _maxHard = -1;
	private int _maxCustom = -1;


	//used to calculate average
	//store total combined score for each level
	private int _totalEasy = 0;
	private int _totalMedium = 0;
	private int _totalHard = 0;
	private int _totalCustom = 0;

	//store how many times user has played each level
	private int _playsEasy = 0;
	private int _playsMedium = 0;
	private int _playsHard = 0;
	private int _playsCustom = 0;


	private Stats() {}

	//loads the stored JSON stats object if there exists one otherwise creates a new singleton Stats object with default values
	public static Stats getInstance(){
		Gson gsonLoad = new Gson();
		File jsonFile = new File("stats.json");
		if (_statsInstance == null  && jsonFile.exists()){ //Checks if the file exists
			try {
				_statsInstance = gsonLoad.fromJson(new FileReader(jsonFile),Stats.class); //loads JSON object
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(_statsInstance == null){
			_statsInstance = new Stats();
		}
		return _statsInstance;
	}
	
	/**
	 * Adds result to list of results
	 * @param result from NumberDisplayController
	 * @param level specified
	 */
	public void addResult(int result, LevelSelection level) {
		Integer[] resultsArray = getResultArray(level);
		if (result < 0 || result > 10) {
			System.err.println("attempted to add invalid result"); //cannot have negative score of score > 10
		} else {
			//add score from numberdisplay
			for(int i=0; i <resultsArray.length; i++){
				if (resultsArray[i] == -1){
					resultsArray[i] = result;
					break;
				} else if (i == resultsArray.length - 1) { //means array is full and results have to be shifted to left so new result can be appended
					for (int j = 0; j < resultsArray.length-2; j++){
						resultsArray[j] = resultsArray[j+1];
						resultsArray[resultsArray.length - 1] = result;
					}
				}
			}
			//update max, total and plays value for the level
			if (level.equals(LevelSelection.EASY)) {
				if (result > _maxEasy) {
					_maxEasy = result;
				}
				_totalEasy = _totalEasy + result;
				_playsEasy++;
			} else if (level.equals(LevelSelection.MEDIUM)) {
				if (result > _maxMedium) {
					_maxMedium = result;
				}
				_totalMedium = _totalMedium + result;
				_playsMedium++;
			} else if (level.equals(LevelSelection.HARD)) {
				if (result > _maxHard) {
					_maxHard = result;
				}
				_totalHard = _totalHard + result;
				_playsHard++;
			} else {
				if (result > _maxCustom) {
					_maxCustom = result;
				}
				_totalCustom = _totalCustom + result;
				_playsCustom++;
			}
		}
	}

	/**
	 * Returns the array associated with the level for setting information and calculating values
	 * @param level
	 * @return: corresponding array depending on level.
	 */

	public Integer[] getResultArray(LevelSelection level){
		if(level.equals(LevelSelection.EASY)){
			return _resultsEasy;
		}
		else if (level.equals(LevelSelection.MEDIUM)){
			return _resultsMedium;
		}
		else if (level.equals(LevelSelection.HARD)){
			return _resultsHard;
		}
		else{
			return _resultsCustom;
		}
	}

	/**
	 * Calculates and returns average of easy or hard level as an int.
	 * @return: either the calculated average or -1 if there isn't any data to calculate.
	 */
	public double getAverage(LevelSelection level) {
		if (level.equals(LevelSelection.EASY)) {
			if (_playsEasy != 0) {
				return round((double)_totalEasy/_playsEasy,2);
			} else {
				return -1;
			}
		} else if (level.equals(LevelSelection.MEDIUM)) {
			if(_playsMedium != 0) {
				return round((double)_totalMedium/_playsMedium,2);
			} else {
				return -1;
			}
		} else if (level.equals(LevelSelection.HARD)) {
			if (_playsHard != 0){
				return round((double)_totalHard/_playsHard,2);
			} else {
				return -1;
			}
		} else {
			if (_playsCustom != 0) {
				return round((double)_totalCustom/_playsCustom,2);
			} else {
				return -1;
			}
		}
	}

	/**
	 * Takes a double value and rounds it to the indicated number of places
	 * @param value: the double value to be rounded
	 * @param places: the number of places to round to
	 * @return :a double value rounded to the indicated number of places
	 */
	public double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal decimal = new BigDecimal(value);
		decimal = decimal.setScale(places, RoundingMode.HALF_UP);
		return decimal.doubleValue();
	}

	/**
	 * calculates and returns max score of level result as int
	 * @return max value corresponding to the input level
	 */
	public int getMax(LevelSelection level) {
		if (level.equals(LevelSelection.EASY)) {
			return _maxEasy;
		} else if (level.equals(LevelSelection.MEDIUM)) {
			return _maxMedium;
		} else if (level.equals(LevelSelection.HARD)) {
			return _maxHard;
		} else {
			return _maxCustom;
		}
	}
}
