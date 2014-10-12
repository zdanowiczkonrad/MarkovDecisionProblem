/**
 * @author Konrad Zdanowicz (zdanowicz.konrad@gmail.com)
 * 
 */
package kzdanowi.studies.markov;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import kzdanowi.studies.markov.states.State;

/**
 * 
 */
public class ValueIteration {
	public double a = 0.8; // probability of agent going according to action
							// direction
	public double b = 0.1; // probability of agent going aside from the action
							// direction
	public double r = -1.0; // default reward for empty field
	public double d = 0.99; // discount factor
	public int N = 4; // board width
	public int M = 4; // board height
	private double e = 0.1; // error value
	private List<State> states;
	private List<double[]> convergePlotData;

	/**
	 * Default constructor
	 */
	public ValueIteration() {
	}

	/**
	 * Constructor that allows to specify custom parameters
	 * 
	 * @param a
	 *            probability of agent going according to the action direction
	 * @param b
	 *            probability of agent going aside from the action direction
	 * @param r
	 *            default reward for empty field
	 * @param d
	 *            discount factor
	 * @param N
	 *            board width
	 * @param M
	 *            board height
	 * @param e
	 *            error value
	 */

	public ValueIteration(double a, double b, double r, double d, int N, int M, double e) {
		this.a = a;
		this.b = b;
		this.r = r;
		this.d = d;
		this.N = N;
		this.M = M;
		this.e = e;
	}

	public void loadField(List<State> states) {
		this.states = states;

		for (int i = M - 1; i >= 0; i--) {
			for (int j = 0; j < N; j++) {
				int index = i * N + j;
				List<Action> actions = new ArrayList<Action>();
				if (states.get(index).toString().equals("F") || states.get(index).toString().equals("G")) {
					actions.add(Action.NONE);

				} else {
					if (hasTop(states, index))
						actions.add(Action.TOP);
					if (hasBottom(states, index))
						actions.add(Action.BOTTOM);
					if (hasLeft(states, index))
						actions.add(Action.LEFT);
					if (hasRight(states, index))
						actions.add(Action.RIGHT);

				}
				states.get(index).setAllowedActions(actions);
			}

		}
		setConvergePlotData(new ArrayList<double[]>());
	}

	public void run() {
		print(states);
		double[] utilities, oldUtilities;
		utilities = getUtilities(states);
		do {
			oldUtilities = utilities;
			epoch(states);
			utilities = getUtilities(states);
			getConvergePlotData().add(utilities);
		} while (mse(utilities, oldUtilities) >= e * (1 - d) / d);
		System.out.println(String.format("Algorithm ended after %d steps.", getConvergePlotData().size()));
		printWithUtils(states);
		printWithArrows(states);
		
	}


	private void epoch(List<State> states) {
		for (int i = 0; i < states.size(); i++) {
			double newUtility = 0.;

			if (states.get(i).toString().equals("G")) {
				newUtility = states.get(i).getReward();
			} else {
				newUtility = states.get(i).getReward() + d * max(states.get(i), states.get(i).getAllowedActions(), states, i);
			}
			states.get(i).setUtility(newUtility);
		}

	}

	private double max(State state, List<Action> actions, List<State> states, int i) {

		if (actions.size() == 1 && actions.get(0) == Action.NONE) {

			return 0;
		}
		double max = Double.MIN_VALUE;
		for (Action action : actions) {
			double sum = findSum(state, action, states, i);
			if (sum >= max) {
				max = sum;
				state.setAction(action);
			}
		}
		return max;
	}

	private double findSum(State state, Action action, List<State> states, int i) {
		double sum = 0;
		if (action == Action.TOP) {
			sum += a * getTopElementU(state, states, i);
			sum += b * getLeftElementU(state, states, i);
			sum += b * getRightElementU(state, states, i);
		} else if (action == Action.BOTTOM) {
			sum += a * getBottomElementU(state, states, i);
			sum += b * getLeftElementU(state, states, i);
			sum += b * getRightElementU(state, states, i);
		} else if (action == Action.LEFT) {
			sum += a * getLeftElementU(state, states, i);
			sum += b * getTopElementU(state, states, i);
			sum += b * getBottomElementU(state, states, i);
		} else if (action == Action.RIGHT) {
			sum += a * getRightElementU(state, states, i);
			sum += b * getTopElementU(state, states, i);
			sum += b * getBottomElementU(state, states, i);
		}
		return sum;
	}

	private double getTopElementU(State state, List<State> states, int i) {
		if (hasTop(states, i))
			return states.get(i + N).getUtility();
		return 0;
	}

	private double getBottomElementU(State state, List<State> states, int i) {
		if (hasBottom(states, i))
			return states.get(i - N).getUtility();
		return 0;
	}

	private double getLeftElementU(State state, List<State> states, int i) {
		if (hasLeft(states, i))
			return states.get(i - 1).getUtility();
		return 0;
	}

	private double getRightElementU(State state, List<State> states, int i) {
		if (hasRight(states, i))
			return states.get(i + 1).getUtility();
		return 0;
	}

	private double mse(double[] utilities, double[] oldUtilities) {
		double result = 0;
		for (int i = 0; i < oldUtilities.length; i++) {
			result += Math.pow(utilities[i] - oldUtilities[i], 2.);
		}
		return result;
	}

	private double[] getUtilities(List<State> states) {
		double utils[] = new double[states.size()];
		for (int i = 0; i < states.size(); i++) {
			utils[i] = states.get(i).getUtility();
		}
		return utils;
	}

	private boolean hasBottom(List<State> states, int i) {
		return i - N >= 0 && !states.get(i - N).toString().equals("F");
	}

	private boolean hasTop(List<State> states, int i) {
		return i + N < N * M && !states.get(i + N).toString().equals("F");
	}

	private boolean hasLeft(List<State> states, int i) {
		return i % N > 0 && !states.get(i - 1).toString().equals("F");
	}

	private boolean hasRight(List<State> states, int i) {
		return i % N < M && i + 1 < N * M && !states.get(i + 1).toString().equals("F");
	}

	public void print(List<State> states) {
		for (int i = M - 1; i >= 0; i--) {
			for (int j = 0; j < N; j++) {
				int index = i * N + j;
				System.out.print(states.get(index) + "\t");
			}
			System.out.println();
		}

		System.out.println("-------------------------");
	}

	public void printWithUtils(List<State> states) {
		for (int i = M - 1; i >= 0; i--) {
			for (int j = 0; j < N; j++) {
				int index = i * N + j;
				System.out.print(String.format("%s %.4f", states.get(index), states.get(index).getUtility()) + " " + getArrowStringsFrom(states.get(index)) + "\t");
			}
			System.out.println();
		}
		System.out.println("-------------------------");
	}

	public void printWithArrows(List<State> states) {
		for (int i = M - 1; i >= 0; i--) {
			for (int j = 0; j < N; j++) {
				int index = i * N + j;
				System.out.print(getArrowStringsFrom(states.get(index)) + "\t");
			}
			System.out.println();
		}

		System.out.println("-------------------------");
	}

	public String getArrowStringsFrom(State state) {
		if (state.toString().equals("G"))
			return String.valueOf(state.getReward());
		if (state.toString().equals("F"))
			return "#";
		if (state.getAction() == Action.BOTTOM)
			return "v";
		if (state.getAction() == Action.TOP)
			return "^";
		if (state.getAction() == Action.LEFT)
			return "<-";
		if (state.getAction() == Action.RIGHT)
			return "->";
		else {
			return "-";
		}
	}

	public List<double[]> getConvergePlotData() {
		return convergePlotData;
	}

	public void setConvergePlotData(List<double[]> convergePlotData) {
		this.convergePlotData = convergePlotData;
	}
}
