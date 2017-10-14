package tatai.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton stats object
 * @author lucy
 *
 */
public class Stats {

	//	private static Stats _easyInstance = new Stats();
	//	private static Stats _mediumInstance = new Stats();
	//	private static Stats _hardInstance = new Stats();
	//	private static Stats _customInstance = new Stats();

	private static Stats _statsInstance = new Stats();
	//	private List<Integer> _resultsEasy = new ArrayList<Integer>(); //list of results from easy
	//	private List<Integer> _resultsHard = new ArrayList<Integer>(); // list of results from hard

	private Integer[] _resultsEasy = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private Integer[] _resultsMedium = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private Integer[] _resultsHard = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private Integer[] _resultsCustom = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	private Stats() {}

	//	public static Stats getEasyInstance() {
	//		return _easyInstance;
	//	}
	//	
	//	public static Stats getMediumInstance() {
	//		return _mediumInstance;
	//	}
	//	
	//	public static Stats getHardInstance() {
	//		return _hardInstance;
	//	}
	//	
	//	public static Stats getCustomInstance() {
	//		return _customInstance;
	//	}

	public static Stats getInstance(){
		return _statsInstance;
	}
	/**
	 * Adds result to list of results
	 * @param result
	 */
	public void addResult(int result, LevelSelection level) {
		Integer[] resultsArray = getResultArray(level);
		if (result < 0 || result > 10) {
			System.err.println("attempted to add invalid result"); //cannot have negative score of score > 10
		} else {
				//add score from numberdisplay
				for(int i=0; i <resultsArray.length; i++){
					if(resultsArray[9] > -1){
						for (int j = 0; j < resultsArray.length-1; j++){
							resultsArray[i] = resultsArray[i+1];
							resultsArray[9] = -1;
						}
					}
					if (resultsArray[i] == -1){
						resultsArray[i] = result;
						break;
					}
				}
		}
	}

	/**
	 * Returns the array associated with the level for setting information and calculating values
	 * @return
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
	 * @return
	 */
	public double getAverage(LevelSelection level) {
		Integer[] resultsArray = getResultArray(level);
		double sum = 0;
		int i;
			if (resultsArray[0] == -1) { //there are no scores for easy
				return -1.0;
			} else { //calculate average for easy
				for (i = 0; i < resultsArray.length; i++) {
					if (resultsArray[i] == -1){
						break;
					}
					sum = sum + resultsArray[i];
				}
			}
			return sum/(double)i;
	}

	public double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}

		BigDecimal decimal = new BigDecimal(value);
		decimal = decimal.setScale(places, RoundingMode.HALF_UP);
		return decimal.doubleValue();
	}

	/**
	 * Calculates and returns lowest result of level as int
	 * @return
	 */
	public int getMin(LevelSelection level) {
		Integer[] resultsArray = getResultArray(level);
			if (resultsArray[0] == -1) {//no scores for easy
				return -1;
			}
			else {//calculate min for easy
				int min = resultsArray[0];
				for (int i = 1; i < resultsArray.length; i++) {
					if (resultsArray[i] == -1){
						break;
					}
					if (min == 0) {
						return min;
					} else if (resultsArray[i] < min ) {
						min = resultsArray[i];
					}
				}
				return min;
			}
	}

	/**
	 * calculates and returns max score of level result as int
	 * @return
	 */
	public int getMax(LevelSelection level) {
		Integer[] resultsArray = getResultArray(level);
			if (resultsArray[0] == -1) {//no scores for easy
				return -1;
			}
			else {//calculate max for easy
				int max = resultsArray[0];
				for (int i = 1; i < resultsArray.length; i++) {
					if (max == 10) {
						return max;
					} else if (resultsArray[i] > max) {
						max = resultsArray[i];
					}
				}
				return max;
			}
	}
}
