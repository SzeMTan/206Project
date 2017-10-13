package tatai.model;

/**
 * Recording create a recording, plays the recording and will interpret what is said in the recording.
 * Interpretation will only work for Maori numbers between 1 to 99.
 */
import java.io.File;

public class Recording {

	//Bash commands
	private final String FILE = "foo.wav"; //name of recording file
	private final String RECORD = "arecord -r 22050 -c 1 -i -t wav -f s16_LE " + FILE; //record 
	private final String RECOGNITION = "HVite -H ./HTK/MaoriNumbers/HMMs/hmm15/macros -H "
			+ "./HTK/MaoriNumbers/HMMs/hmm15/hmmdefs -C ./HTK/MaoriNumbers/user/configLR  "
			+ "-w ./HTK/MaoriNumbers/user/wordNetworkNum -o SWT -l '*' -i ./HTK/MaoriNumbers/"
			+ "recout.mlf -p 0.0 -s 5.0  ./HTK/MaoriNumbers/user/dictionaryD ./HTK/MaoriNumbers"
			+ "/user/tiedList " + FILE; //interpret. May have to adjust file paths depending on location of files.
	private final String WORD = "awk '/sil/{flag = flag + 1}; flag % 2 == 1 && ! /sil/' ./HTK/"
			+ "MaoriNumbers/recout.mlf"; //get interpretation of recording
	private final String PLAY = "aplay " + FILE; //play recording

	/**
	 * record() checks if there is an existing recording and deletes it. Then a new recording is made and interpreted.
	 * This makes sure that there is only one recording in existance at a time.
	 */
	public void beginRecord() {
		File recording = new File(FILE);
		if (recording.exists()) {
			recording.delete();
		}
		Bash record = new Bash(RECORD);
		record.execute();
	}
	
	/**
	 * kills recording process
	 */
	public void finishRecord() {
		String killRecord = RECORD.substring(1);
		killRecord = "kill $(ps aux | grep '[" + RECORD.charAt(0) + "]" + killRecord+ "' | awk '{print $2}')";
		Bash kill = new Bash(killRecord);
		kill.execute();
		Bash recognition = new Bash(RECOGNITION);
		recognition.execute();
	}

	/**
	 * play() will play the existing recording
	 */
	public void play() {
		Bash play = new Bash(PLAY);
		play.execute();
	}
	
	/**
	 * Will kill any aplay process
	 */
	public void killPlay() {
		String killPlay = PLAY.substring(1);
		killPlay = "kill $(ps aux | grep '[" + PLAY.charAt(0) + "]" + killPlay + "' | awk '{print $2}')";
		Bash kill = new Bash(killPlay);
		kill.execute();
	}

	/**
	 * getWord() returns the interpretation of the current recording.
	 * @return: String of the interpretation.
	 */
	public String getWord() {
		Bash word = new Bash(WORD);
		String maoriWord = "";
		maoriWord = word.execute();
		return maoriWord;
	}

}
