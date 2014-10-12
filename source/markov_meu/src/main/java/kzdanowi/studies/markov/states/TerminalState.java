/**
 * @author Konrad Zdanowicz (zdanowicz.konrad@gmail.com)
 * 
 */
package kzdanowi.studies.markov.states;

/**
 * 
 */
public class TerminalState extends State {

	private double d;

	public TerminalState(double d) {
		this.d = d;
	}
	 
	@Override
	public String toString() {
		return "G";
	}

	@Override
	public double getReward() {
		return d;
	}

}
