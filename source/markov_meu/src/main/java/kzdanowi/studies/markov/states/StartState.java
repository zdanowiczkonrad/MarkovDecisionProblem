/**
 * @author Konrad Zdanowicz (zdanowicz.konrad@gmail.com)
 * 
 */
package kzdanowi.studies.markov.states;

/**
 * 
 */
public class StartState extends State {

	private double d;

	public StartState(double d) {
		this.d = d;
	}

	@Override
	public String toString() {
		return "S";
	}

	@Override
	public double getReward() {
		return d;
	}

}
