package tatai.model;

import java.util.HashMap;
import java.util.Map;

/**
 * MaoriNumbers contains all the numbers and their corresponding Maori pronounciation
 * required to pronounce 1 to 99 in Maori. In order to count to greater than 99, the 
 * required words in Maori will need to be added with their integer value. Note that 
 * the enum does not include "maa".
 * @author lucy
 *
 */
public enum MaoriNumbers {
	
	tahi(1), rua(2), toru(3), whaa(4), rima(5), ono(6), whitu(7), waru(8), iwa(9), tekau(10);
	
	private int _num;
	
	private static Map<Integer, MaoriNumbers> _map = new HashMap<Integer, MaoriNumbers>();
	
	static {
		for (MaoriNumbers num: MaoriNumbers.values()){
			_map.put(num._num, num);
		}
	}
	
	private MaoriNumbers(int num){
		this._num = num;
	}
	
	public static MaoriNumbers getValue(int num){
		return _map.get(num);
	}
}
