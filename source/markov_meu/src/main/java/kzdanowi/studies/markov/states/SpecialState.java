/**
 * @author Konrad Zdanowicz (zdanowicz.konrad@gmail.com)
 * 
 */
package kzdanowi.studies.markov.states;

/**
 * 
 */
public class SpecialState extends State {
	double d;

	public SpecialState(double d) {
		this.d = d;
	}

	@Override
	public String toString() {
		return "B";
	}

	@Override
	public double getReward() {
		return d;
	}

	
}
