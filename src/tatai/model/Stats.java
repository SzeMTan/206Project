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

	private static Stats _easyInstance = new Stats();
	private static Stats _mediumInstance = new Stats();
	private static Stats _hardInstance = new Stats();
	private static Stats _customInstance = new Stats();

//	private List<Integer> _resultsEasy = new ArrayList<Integer>(); //list of results from easy
//	private List<Integer> _resultsHard = new ArrayList<Integer>(); // list of results from hard
	
	private Integer[] _resultsEasy = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private Integer[] _resultsMedium = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private Integer[] _resultsHard = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private Integer[] _resultsCustom = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

	private Stats() {}

	public static Stats getEasyInstance() {
		return _easyInstance;
	}
	
	public static Stats getMediumInstance() {
		return _mediumInstance;
	}
	
	public static Stats getHardInstance() {
		return _hardInstance;
	}
	
	public static Stats getCustomInstance() {
		return _customInstance;
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
				for(int i=0; i <_resultsEasy.length; i++){
					if(_resultsEasy[9] > -1){
						for (int j = 0; j < _resultsEasy.length-1; j++){
							_resultsEasy[i] = _resultsEasy[i+1];
							_resultsEasy[9] = -1;
						}
					}
					if (_resultsEasy[i] == -1){
						_resultsEasy[i] = result;
						break;
					}
				}
			} else {
				//add score from hard
				//_resultsHard.add(result);
			}
		}
	}

	/**
	 * Creates and returns an array with frequency of results depending
	 * on the level
	 * @return
	 */
//	public int[] getResultArray(LevelSelection level) {
//		int[] resultFreq = new int[11];
//		//check what level it is
//		if (level.equals(LevelSelection.EASY)) {
//			//get array for easy level
//			for (int i = 0; i < _resultsEasy.size(); i++) {
//				resultFreq[_resultsEasy.get(i)]++;
//			}
//		} else {
//			//gets array for hard level
//			for (int i = 0; i < _resultsHard.size(); i++) {
//				resultFreq[_resultsHard.get(i)]++;
//			}
//		}
//		return resultFreq;
//	}
	
	public Integer[] getResultArray(LevelSelection level){
		if(level.equals(LevelSelection.EASY)){
			return _resultsEasy;
		}
		return _resultsHard;
	}

	/**
	 * Calculates and returns average of easy or hard level as an int.
	 * @return
	 */
	public double getAverage(LevelSelection level) {
		double sum = 0;
		int i;
		if (level.equals(LevelSelection.EASY)) {
			if (_resultsEasy[0] == -1) { //there are no scores for easy
				return -1.0;
			} else { //calculate average for easy
				for (i = 0; i < _resultsEasy.length; i++) {
					if (_resultsEasy[i] == -1){
						break;
					}
					sum = sum + _resultsEasy[i];
				}
			}
			return sum/(double)i;
			//return round((double)sum/(i), 2);
//		} else {
//			if (_resultsHard.size() == 0) { //there are no scores for hard
//				return -1.0;
//			} else { //calculate average for hard
//				for (int i = 0; i < _resultsHard.size(); i++) {
//					sum = sum + _resultsHard.get(i);
//				}
//				
//				DecimalFormat df = new DecimalFormat("#.##");
//				df.setRoundingMode(RoundingMode.CEILING);
//				
//				return round((double)sum/_resultsHard.size(), 2);
//			}
		}
		return -1;
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
			if (_resultsEasy[0] == -1) {//no scores for easy
				return -1;
			}
			else {//calculate min for easy
				int min = _resultsEasy[0];
				for (int i = 1; i < _resultsEasy.length; i++) {
					if (_resultsEasy[i] == -1){
						break;
					}
					if (min == 0) {
						return min;
					} else if (_resultsEasy[i] < min ) {
						min = _resultsEasy[i];
					}
				}
				return min;
			}
		} else {
//			if (_resultsHard.size() == 0) {//no scores for hard
//				return -1;
//			}
//			else {//caulculate min for hard
//				int min = _resultsHard.get(0);
//				for (int i = 1; i < _resultsHard.size(); i++) {
//					if (min == 0) {
//						return min;
//					} else if (_resultsHard.get(i) < min) {
//						min = _resultsHard.get(i);
//					}
//				}
//				return min;
//			}
		}
		return -1;
	}

	/**
	 * calculates and returns max score of level result as int
	 * @return
	 */
	public int getMax(LevelSelection level) {
		if (level.equals(LevelSelection.EASY)) {
			if (_resultsEasy[0] == -1) {//no scores for easy
				return -1;
			}
			else {//calculate max for easy
				int max = _resultsEasy[0];
				for (int i = 1; i < _resultsEasy.length; i++) {
					if (max == 10) {
						return max;
					} else if (_resultsEasy[i] > max) {
						max = _resultsEasy[i];
					}
				}
				return max;
			}
		} else {
//			if (_resultsHard.size() == 0) {//no scores for hard
//				return -1;
//			}
//			else {//calculate max for hard
//				int max = _resultsHard.get(0);
//				for (int i = 1; i < _resultsHard.size(); i++) {
//					if (max == 10) {
//						return max;
//					} else if (_resultsHard.get(i) < max) {
//						max = _resultsHard.get(i);
//					}
//				}
//				return max;
//			}
		}
		return -1;
	}
}
