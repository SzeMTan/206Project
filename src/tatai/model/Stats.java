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

	private static Stats _instance = new Stats();

	private List<Integer> _resultsEasy = new ArrayList<Integer>(); //list of results from easy
	private List<Integer> _resultsHard = new ArrayList<Integer>(); // list of results from hard

	private Stats() {}

	public static Stats getInstance() {
		return _instance;
	}

	/**
	 * Adds result to list of results
	 * @param result
	 */
	public void addResult(int result, LevelSelection level) {
		if (result < 0 || result > 10) {
			System.err.println("attempted to add invalid result"); //cannot have negative score of score > 10
		} else {
			if (level.equals(LevelSelection.EASY)) {
				//add score from easy
				_resultsEasy.add(result);
			} else {
				//add score from hard
				_resultsHard.add(result);
			}
		}
	}

	/**
	 * Creates and returns an array with frequency of results depending
	 * on the level
	 * @return
	 */
	public int[] getResultArray(LevelSelection level) {
		int[] resultFreq = new int[11];
		//check what level it is
		if (level.equals(LevelSelection.EASY)) {
			//get array for easy level
			for (int i = 0; i < _resultsEasy.size(); i++) {
				resultFreq[_resultsEasy.get(i)]++;
			}
		} else {
			//gets array for hard level
			for (int i = 0; i < _resultsHard.size(); i++) {
				resultFreq[_resultsHard.get(i)]++;
			}
		}
		return resultFreq;
	}

	/**
	 * Calculates and returns average of easy or hard level as an int.
	 * @return
	 */
	public double getAverage(LevelSelection level) {
		int sum = 0;
		if (level.equals(LevelSelection.EASY)) {
			if (_resultsEasy.size() == 0) { //there are no scores for easy
				return -1.0;
			} else { //calculate average for easy
				for (int i = 0; i < _resultsEasy.size(); i++) {
					sum = sum + _resultsEasy.get(i);
				}
			}
			return round((double)sum/_resultsEasy.size(), 2);
		} else {
			if (_resultsHard.size() == 0) { //there are no scores for hard
				return -1.0;
			} else { //calculate average for hard
				for (int i = 0; i < _resultsHard.size(); i++) {
					sum = sum + _resultsHard.get(i);
				}
				
				DecimalFormat df = new DecimalFormat("#.##");
				df.setRoundingMode(RoundingMode.CEILING);
				
				return round((double)sum/_resultsHard.size(), 2);
			}
		}
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
		if (level.equals(LevelSelection.EASY)) {
			if (_resultsEasy.size() == 0) {//no scores for easy
				return -1;
			}
			else {//calculate min for easy
				int min = _resultsEasy.get(0);
				for (int i = 1; i < _resultsEasy.size(); i++) {
					if (min == 0) {
						return min;
					} else if (_resultsEasy.get(i) < min) {
						min = _resultsEasy.get(i);
					}
				}
				return min;
			}
		} else {
			if (_resultsHard.size() == 0) {//no scores for hard
				return -1;
			}
			else {//caulculate min for hard
				int min = _resultsHard.get(0);
				for (int i = 1; i < _resultsHard.size(); i++) {
					if (min == 0) {
						return min;
					} else if (_resultsHard.get(i) < min) {
						min = _resultsHard.get(i);
					}
				}
				return min;
			}
		}
	}

	/**
	 * calculates and returns max score of level result as int
	 * @return
	 */
	public int getMax(LevelSelection level) {
		if (level.equals(LevelSelection.EASY)) {
			if (_resultsEasy.size() == 0) {//no scores for easy
				return -1;
			}
			else {//calculate max for easy
				int max = _resultsEasy.get(0);
				for (int i = 1; i < _resultsEasy.size(); i++) {
					if (max == 10) {
						return max;
					} else if (_resultsEasy.get(i) > max) {
						max = _resultsEasy.get(i);
					}
				}
				return max;
			}
		} else {
			if (_resultsHard.size() == 0) {//no scores for hard
				return -1;
			}
			else {//calculate max for hard
				int max = _resultsHard.get(0);
				for (int i = 1; i < _resultsHard.size(); i++) {
					if (max == 10) {
						return max;
					} else if (_resultsHard.get(i) < max) {
						max = _resultsHard.get(i);
					}
				}
				return max;
			}
		}
	}
}
