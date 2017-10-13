package tatai.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Bash takes a bash command, executes it and returns the results.
 * @author lucy
 *
 */
public class Bash {

	private String _bashCommand;

	public Bash(String bashCommand){
		_bashCommand = bashCommand;
	}

	/**
	 * Execute the bash command. If the command fails to execute then an error will be thrown.
	 * @return String containing the results of executing the command.
	 */
	public String execute(){ //will throw exception if cancelled
		String result = "";

		ProcessBuilder builder = new ProcessBuilder("bash","-c",_bashCommand);

		try {
			Process process = builder.start();
			int exitStatus = process.waitFor();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			BufferedReader stdin = new BufferedReader(new InputStreamReader(process.getInputStream()));

			if (exitStatus != 0) { //command has not executed successfully
				String line;
				while ((line = stdout.readLine()) != null) {
					result = result + "\n" + line;
					System.err.println(line);
				}
			} else {
				String line;
				if ((line = stdin.readLine()) != null) {
					result = line;
				}
				while((line = stdin.readLine())!=null) {
					result = result + "\n" + line;
				}
			}

		} catch (Exception e) {}		

		return result;
	}
}