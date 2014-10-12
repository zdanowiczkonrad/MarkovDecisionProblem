/**
 * @author Konrad Zdanowicz (zdanowicz.konrad@gmail.com)
 * 
 */
package kzdanowi.studies.markov.states;

import java.util.List;

import kzdanowi.studies.markov.Action;

/**
 * 
 */
public abstract class State {
	private double utility=0.0;
	private List<Action> allowedActions;
	private Action action;
	
	public double getUtility() {
		return utility;
	}

	public void setUtility(double utility) {
		this.utility = utility;
	}

	public abstract double getReward();


	public List<Action> getAllowedActions() {
		return allowedActions;
	}

	public void setAllowedActions(List<Action> allowedActions) {
		this.allowedActions = allowedActions;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	
}
