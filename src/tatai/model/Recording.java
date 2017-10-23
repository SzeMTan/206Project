package tatai.model;

import java.io.BufferedReader;
/**
 * Recording create a recording, plays the recording and will interpret what is said in the recording.
 * Interpretation will only work for Maori numbers between 1 to 99.
 */
import java.io.File;
import java.io.InputStreamReader;

public class Recording {

	//Bash commands
	private final String FILE = "foo.wav"; //name of recording file
	private final String RECORD = "ffmpeg -f alsa -i default -acodec pcm_s16le -ar 22050 -ac 1 " + FILE; //record 
	//Recognises the user input and translates it into words
	private final String RECOGNITION = "HVite -H HMMs/hmm15/macros -H HMMs/hmm15/hmmdefs -C user/configLR  -w user/wordNetworkNum -o SWT -l '*' -i recout.mlf -p 0.0 -s 5.0  user/dictionaryD user/tiedList " + FILE;
	private final String WORD = "awk '/sil/{flag = flag + 1}; flag % 2 == 1 && ! /sil/' recout.mlf"; //get interpretation of recording
	private final String PLAY = "aplay " + FILE; //play recording
	//command to sent interrupt signal to record
	private final String KILLRECORD = "kill -SIGINT $(ps aux | grep '[f]fmpeg -f alsa -i default -acodec pcm_s16le -ar 22050 -ac 1 foo.wav' | awk '{print $2}')";

	/**
	 * record() checks if there is an existing recording and deletes it. Then a new recording is made and interpreted.
	 * This makes sure that there is only one recording in existance at a time.
	 */
	public int record() {
		//delete old recording file
		File recording = new File(FILE);
		if (recording.exists()) {
			recording.delete();
		}
		//record new recording
		ProcessBuilder builder = new ProcessBuilder("bash","-c",RECORD);
		int exitStatus = -1; //This exit status is used for checking whether the recording has been successfully created (recording of over 2 seconds)
		//If it hasn't been successfully recorded then a popup will notify the user.
		try {
			Process process = builder.start();
			
			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			exitStatus = process.waitFor();
			
			if (exitStatus != 0) { //command has not executed successfully
				String line;
				while ((line = stdout.readLine()) != null) {
					System.err.println(line);
				}
			} 
		} catch (Exception e) {}
		//return whether or not recording completed successfully
		return exitStatus;
	}
	
	/**
	 * Kills the recording thread that the user is recording on
	 */
	public void killRecord() {
		Bash kill = new Bash(KILLRECORD);
		kill.execute();
	}
	
	/**
	 * Runs the recognition bash command for recording recognition
	 */
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
