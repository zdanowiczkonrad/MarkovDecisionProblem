/**
 * @author Konrad Zdanowicz (zdanowicz.konrad@gmail.com)
 * 
 */
package kzdanowi.studies.markov;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kzdanowi.studies.markov.states.EmptyState;
import kzdanowi.studies.markov.states.ProhibitedState;
import kzdanowi.studies.markov.states.SpecialState;
import kzdanowi.studies.markov.states.StartState;
import kzdanowi.studies.markov.states.State;
import kzdanowi.studies.markov.states.TerminalState;

public class DataReader {
	State[] states = new State[100];
	Properties properties = new Properties();
	Pattern pair = Pattern.compile("\\(([\\d]+),([\\d]+)\\)");
	Pattern triple = Pattern.compile("\\(([\\d]+),([\\d]+),([\\d\\.\\-]+)\\)");
	double a;
	double b;
	int N=4; // default value
	int M;
	double r;
	double d;
	double e;
	List<State> statesList = new ArrayList<State>();

	public List<State> read(String path) {

		doRead(path);
		doRead(path);
		for (int i = 0; i < M * N; i++) {
			statesList.add(states[i] != null ? states[i] : new EmptyState(r));
		}
		return statesList;
	}

	private void doRead(String path) {
		try {
			properties.load(new FileInputStream(path+".properties"));
			for (String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);

				if (key.equals("states.start")) {
					singleStart(value);

				} else if (key.equals("states.terminal")) {

					singleTerminal(value);
				} else if (key.equals("states.prohibited")) {

					singleProhibited(value);
				} else if (key.equals("states.special")) {

					singleSpecial(value);
				} else if (key.equals("N")) {
					N = Integer.parseInt(value);
				} else if (key.equals("M")) {
					M = Integer.parseInt(value);					
				} else if (key.equals("r")) {
					r = Double.parseDouble(value);
				} else if (key.equals("a")) {
					a = Double.parseDouble(value);
				} else if (key.equals("b")) {
					b = Double.parseDouble(value);
				} else if (key.equals("d")) {
					d = Double.parseDouble(value);
				} else if (key.equals("e")) {
					e = Double.parseDouble(value);
				}
			}

		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}

	private void singleSpecial(String value) {
		Matcher m = triple.matcher(value);

		while (m.find()) {
			int x = Integer.parseInt(m.group(1));			
			int y = Integer.parseInt(m.group(2));
			double val = Double.parseDouble(m.group(3));
			addSpecialPoint(x, y, val);
		}
	}

	private void addSpecialPoint(int x, int y, double val) {
		states[getPos(x, y)] = new SpecialState(val);
	}

	private void singleProhibited(String value) {
		Matcher m = pair.matcher(value);

		while (m.find()) {
			int x = Integer.parseInt(m.group(1));
			int y = Integer.parseInt(m.group(2));
			addProhibitedPoint(x, y);
		}
	}

	private void addProhibitedPoint(int x, int y) {
		states[getPos(x, y)] = new ProhibitedState();
	}

	private void singleStart(String value) {
		Matcher m = pair.matcher(value);

		while (m.find()) {
			int x = Integer.parseInt(m.group(1));
			int y = Integer.parseInt(m.group(2));
			addStartPoint(x, y);
		}
	}

	private void singleTerminal(String value) {
		Matcher m = triple.matcher(value);

		while (m.find()) {
			int x = Integer.parseInt(m.group(1));
			int y = Integer.parseInt(m.group(2));
			double val = Double.parseDouble(m.group(3));
			addTerminalPoint(x, y, val);
			
		}
	}

	private void addTerminalPoint(int x, int y, double val) {
		states[getPos(x, y)] = new TerminalState(val);
	}

	private void addStartPoint(int x, int y) {
		states[getPos(x, y)] = new StartState(r);
	}

	private int getPos(int x, int y) {
		int pos = (x-1)+(y-1)*N;
		return pos;
		
	}
}
