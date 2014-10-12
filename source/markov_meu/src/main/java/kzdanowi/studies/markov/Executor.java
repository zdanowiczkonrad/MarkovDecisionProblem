/**
 * @author Konrad Zdanowicz (zdanowicz.konrad@gmail.com)
 * 
 */
package kzdanowi.studies.markov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 */
public class Executor {
	public void exec(String command) {
		try {
			doExec(command);
		} catch (IOException e) {
			showHint(command);
		} catch (InterruptedException e) {
			showHint(command);
		}
	}

	private void showHint(String command) {
		System.err.println("Something went wrong... run gnuplot manually: ");
		System.out.println("> "+command);
	}

	private void doExec(String command) throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(command);
		p.waitFor();

		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
	}
}
