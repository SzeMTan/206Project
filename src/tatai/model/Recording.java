package tatai.model;

/**
 * Recording create a recording, plays the recording and will interpret what is said in the recording.
 * Interpretation will only work for Maori numbers between 1 to 99.
 */
import java.io.File;

public class Recording {

	//Bash commands
	private final String FILE = "foo.wav"; //name of recording file
	private final String RECORDEASY = "arecord -d 2 -r 22050 -c 1 -i -t wav -f s16_LE " + FILE; //record easy level
	private final String RECORDHARD = "arecord -d 5 -r 22050 -c 1 -i -t wav -f s16_LE " + FILE; //record hard level
	private final String RECOGNITION = "HVite -H /home/se206/Documents/HTK/MaoriNumbers/HMMs/hmm15/macros -H "
			+ "/home/se206/Documents/HTK/MaoriNumbers/HMMs/hmm15/hmmdefs -C /home/se206/Documents/HTK/MaoriNumbers/user/configLR  "
			+ "-w /home/se206/Documents/HTK/MaoriNumbers/user/wordNetworkNum -o SWT -l '*' -i /home/se206/Documents/HTK/MaoriNumbers/"
			+ "recout.mlf -p 0.0 -s 5.0  /home/se206/Documents/HTK/MaoriNumbers/user/dictionaryD /home/se206/Documents/HTK/MaoriNumbers"
			+ "/user/tiedList " + FILE; //interpret. May have to adjust file paths depending on location of files.
	private final String WORD = "awk '/sil/{flag = flag + 1}; flag % 2 == 1 && ! /sil/' /home/se206/Documents/HTK/"
			+ "MaoriNumbers/recout.mlf"; //get interpretation of recording
	private final String PLAY = "aplay " + FILE; //play recording

	/**
	 * record() checks if there is an existing recording and deletes it. Then a new recording is made and interpreted.
	 * This makes sure that there is only one recording in existance at a time.
	 */
	public void recordEasy() {
		File recording = new File(FILE);
		if (recording.exists()) {
			recording.delete();
		}
		Bash record = new Bash(RECORDEASY);
		record.execute();
		Bash recognition = new Bash(RECOGNITION);
		recognition.execute();
	}
	
	public void recordHard() {
		File recording = new File(FILE);
		if (recording.exists()) {
			recording.delete();
		}
		Bash record = new Bash(RECORDHARD);
		record.execute();
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
	 * Kills any arecord process
	 */
	public void killRecordEasy() {
		String killRecord = RECORDEASY.substring(1);
		killRecord = "kill $(ps aux | grep '[" + RECORDEASY.charAt(0) + "]" + killRecord+ "' | awk '{print $2}')";
		Bash kill = new Bash(killRecord);
		kill.execute();
	}
	
	public void killRecordHard() {
		String killRecord = RECORDHARD.substring(1);
		killRecord = "kill $(ps aux | grep '[" + RECORDHARD.charAt(0) + "]" + killRecord+ "' | awk '{print $2}')";
		Bash kill = new Bash(killRecord);
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
