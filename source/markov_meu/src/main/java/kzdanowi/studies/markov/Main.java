/**
 * @author Konrad Zdanowicz (zdanowicz.konrad@gmail.com)
 * 
 */
package kzdanowi.studies.markov;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import kzdanowi.studies.markov.states.State;

public class Main {

	private static final String OUT_SUFFIX = "Out";
	public static final double a = 0.8; // follow direction
	public static final double b = 0.1; // go in the adjacent direction
	public static final double r = -1.0; // default reward for empty field
	public static final double d = 0.9; // discount factor
	public static final int N = 4; // board width
	public static final int M = 4; // board height
	private static double e = 0.01; // error value

	public static void main(String[] args) {
		// List<State> states = Lists.newArrayList(
		// new StartState(r), new EmptyState(r), new ProhibitedState(), new
		// TerminalState(100),
		// new EmptyState(r), new EmptyState(r), new SpecialState(-20.), new
		// EmptyState(r),
		// new EmptyState(r), new EmptyState(r), new EmptyState(r), new
		// EmptyState(r),
		// new EmptyState(r), new EmptyState(r), new EmptyState(r), new
		// EmptyState(r)
		// );
		String filename = null;
		if (args.length > 0) {
			filename = args[0];
		}
		else filename="config";
		System.out.println("Reading data from "+filename+".properties...");
		DataReader reader = new DataReader();
		List<State> input = reader.read(filename);
		System.out.println("(i) The board was parsed correctly.");
		System.out.println("Now executing value iteration algorithm...");
		ValueIteration valueIteration = new ValueIteration(reader.a, reader.b, reader.r, reader.d, reader.N, reader.M, reader.e);
		valueIteration.loadField(input);
		valueIteration.run();
		System.out.println("Saving results to "+filename+OUT_SUFFIX+".png");
		writeData(valueIteration.getConvergePlotData(), filename+OUT_SUFFIX);
		Executor executor = new Executor();
		executor.exec("gnuplot "+filename+OUT_SUFFIX+".gpl");

	}

	private static void writeData(List<double[]> convergePlotData, String name) {

		createDat(convergePlotData, name);
		createGpl(N, M, name);
	}

	private static void createGpl(int n2, int m2, String name) {
		FileOutputStream fop = null;
		File file;

		try {

			file = new File(name + ".gpl");
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes

			fop.write(("set title \"data convergence plot for " + name + ".dat\" \n set xlabel \"Number of iterations\"\nset ylabel \"Utility value\"").getBytes());
			fop.write(("\nset terminal png\n set output '" + name + ".png'\n plot").getBytes());
			int k = 1;
			for (int i = 0; i < m2; i++) {
				for (int j = 0; j < n2; j++) {
					k++;
					String rec = "\"" + name + ".dat\" using " + k + " title 'U(" + (i + 1) + "," + (j + 1) + ")' with lines";
					fop.write(rec.getBytes());
					if (i + 1 == m2 && j + 1 == n2) {
					}

					else
						fop.write(", ".getBytes());

				}
			}

			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void createDat(List<double[]> convergePlotData, String name) {
		FileOutputStream fop = null;
		File file;

		try {

			file = new File(name + ".dat");
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes

			int iter = 0;
			for (double[] serie : convergePlotData) {
				String line = ++iter + "\t";

				for (double d : serie) {
					line += d + "\t";
				}
				line += "\n";
				fop.write(line.getBytes());
			}

			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
