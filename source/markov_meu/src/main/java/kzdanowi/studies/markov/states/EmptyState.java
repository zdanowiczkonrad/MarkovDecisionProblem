/**
 * @author Konrad Zdanowicz (zdanowicz.konrad@gmail.com)
 * 
 */
package kzdanowi.studies.markov.states;

/**
 * 
 */
public class EmptyState extends State {

	private double d;

	public EmptyState(double d) {
		this.d = d;
		
	}
	@Override
	public String toString() {
		return "_";
	}

	@Override
	public double getReward() {
		return d;
	}


	
}
