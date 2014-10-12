/**
 * @author Konrad Zdanowicz (zdanowicz.konrad@gmail.com)
 * 
 */
package kzdanowi.studies.markov.states;

/**
 * 
 */
public class ProhibitedState extends State {

	@Override
	public String toString() {
		return "F";
	}

	@Override
	public double getReward() {
		return 0;
	}



}
