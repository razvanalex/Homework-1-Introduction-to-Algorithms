import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Pair<F, S> {
	private F first;
	private S second;

	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public F getFirst() {
		return this.first;
	}

	public S getSecond() {
		return this.second;
	}

	@Override
	public String toString() {
		return "<" + first + ", " + second + ">";
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + first.hashCode();
		result = 31 * result + second.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pair<?, ?>) {
			return this.getFirst() == ((Pair<?, ?>) obj).getFirst()
					&& this.getSecond() == ((Pair<?, ?>) obj).getSecond();
		}
		return false;
	}

}

public class Numaratoare {
	private static final String INPUT = "numaratoare.in";
	private static final String OUTPUT = "numaratoare.out";

	private int S, N, I;

	private void readData() {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(INPUT));

			S = Integer.parseInt(bfr.readLine());
			N = Integer.parseInt(bfr.readLine());
			I = Integer.parseInt(bfr.readLine());

			bfr.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found!");

		} catch (IOException e) {
			System.out.println("Error closing the file!");
		}

	}

	private void writeData(String result) {
		try {
			BufferedWriter bfw = new BufferedWriter(new FileWriter(OUTPUT));
			bfw.write(result);
			bfw.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found!");

		} catch (IOException e) {
			System.out.println("Error closing the file!");
		}
	}

	private String printSol(ArrayList<Integer> sol) {
		if (sol == null || sol.size() == 0) {
			return "-";
		}

		int len = sol.size();
		StringBuffer sb = new StringBuffer(len * sol.get(len - 1));
		sb.append(S + "=");

		for (int i = 0; i < len - 1; i++) {
			sb.append(sol.get(i) + "+");
		}

		return sb.append(sol.get(len - 1)).toString();
	}

	private boolean bkt(int crtS, int crtN, ArrayList<Integer> dp, 
			ArrayList<ArrayList<Integer>> solution) {

		// Stop condition
		if (crtN == 0) {
			if (crtS == 0) {
				solution.add(new ArrayList<>(dp));
				if (solution.size() == I + 1) {
					return true;
				}
			}
			return false;
		}

		// Try each possible solution
		for (int ci = crtS - crtN + 1; ci > 0; ci--) {
			dp.set(N - crtN, ci);
			int newIndex = crtN - 1;
			int newSum = crtS - ci;

			// We don't want permutations
			if (N - crtN - 1 >= 0 && ci > dp.get(N - crtN - 1)) {
				continue;
			}

			// Keep finding
			if (bkt(newSum, newIndex, dp, solution)) {
				return true;
			}
		}

		return false;
	}

	private String getResult() {
		ArrayList<ArrayList<Integer>> solution = new ArrayList<>();

		// Create the dynamics
		ArrayList<Integer> dp = new ArrayList<>(N);
		for (int i = 0; i < N; i++) {
			dp.add(0);
		}

		// Generate solutions
		bkt(S, N, dp, solution);

		// Get the right solution
		ArrayList<Integer> sol = null;
		if (0 <= I && I < solution.size()) {
			sol = solution.get(I);
		}

		// Return final solution
		return printSol(sol);
	}

	public void solve() {
		readData();
		writeData(getResult());
	}

	public static void main(String[] args) {
		new Numaratoare().solve();
	}
}