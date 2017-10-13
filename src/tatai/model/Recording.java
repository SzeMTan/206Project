package tatai.model;

/**
 * Recording create a recording, plays the recording and will interpret what is said in the recording.
 * Interpretation will only work for Maori numbers between 1 to 99.
 */
import java.io.File;

public class Recording {

	//Bash commands
	private final String FILE = "foo.wav"; //name of recording file
	private final String TEMPFILE = "fooTemp.wav"; //store temp audio here
	private final String RECORD = "arecord -d 0.25 -r 22050 -c 1 -i -t wav -f s16_LE " + FILE; //record 
	private final String TEMPRECORD = "arecord -d 0.25 -r 22050 -c 1 -i -t wav -f s16_LE " + TEMPFILE;//record temp audio
	private final String COMBINEAUDIO = "yes | ffmpeg -i " + FILE + " -i " + TEMPFILE + " -filter_complex '[0:0][1:0]concat=n=2"
			+ ":v=0:a=1[out]' -map '[out]' " + FILE; //combine audio and temp audio;
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
	public void firstRecord() {
		File recording = new File(FILE);
		if (recording.exists()) {
			recording.delete();
		}
		Bash record = new Bash(RECORD);
		record.execute();
	}
	
	public void tempRecord() {
		File tempRecording = new File(TEMPFILE);
		if (tempRecording.exists()) {
			tempRecording.delete();
		}
		Bash tempRecord = new Bash(TEMPRECORD);
		tempRecord.execute();
		Bash combine = new Bash(COMBINEAUDIO);
		combine.execute();
	}
	
	public void recognize() {
		Bash recognize = new Bash(RECOGNITION);
		recognize.execute();
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
