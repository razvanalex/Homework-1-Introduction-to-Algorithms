import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
}

public class Planificare {
	private static final String INPUT = "planificare.in";
	private static final String OUTPUT = "planificare.out";

	private int P, D, B;
	private int[] durate;

	private void readData() {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(INPUT));

			String[] tokens = bfr.readLine().split(" ");
			P = Integer.parseInt(tokens[0]);
			D = Integer.parseInt(tokens[1]);
			B = Integer.parseInt(tokens[2]);

			durate = new int[P];
			for (int i = 0; i < P; i++) {
				durate[i] = Integer.parseInt(bfr.readLine());
			}

			bfr.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found!");

		} catch (IOException e) {
			System.out.println("Error closing the file!");
		}

	}

	private void writeData(Pair<Long, Long> result) {
		try {
			BufferedWriter bfw = new BufferedWriter(new FileWriter(OUTPUT));
			bfw.write(result.getFirst() + " " + result.getSecond());
			bfw.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found!");

		} catch (IOException e) {
			System.out.println("Error closing the file!");
		}
	}

	private Pair<Long, Long> getResult() {
		long totalWaste = 0, numContests = 0;
		int[] backPath = new int[P];
		long[] dp = new long[P];
		long[][] pa = new long[P][P];
		int j = 0, i = 0;

		// Compute pa(i, j) with i = 0,P and j = i,P
		for (i = 0; i < P; i++) {
			for (j = i; j < P; j++) {
				if (j > 1 && pa[i][j - 1] == Long.MAX_VALUE) {
					pa[i][j] = Long.MAX_VALUE;
					continue;
				}
				pa[i][j] = D - durate[i];
				for (int k = i + 1; k <= j; k++) {
					pa[i][j] -= B + durate[k];
				}
				if (pa[i][j] < 0) {
					pa[i][j] = Long.MAX_VALUE;
				} else {
					pa[i][j] = (long) Math.pow(pa[i][j], 3);
				}
			}
		}

		// Apply recurrence
		for (i = P - 1; i >= 0; i--) {
			dp[P - 1 - i] = pa[i][P - 1];
			backPath[i] = P;

			for (j = i + 1; j < P; j++) {
				if (pa[i][j - 1] == Long.MAX_VALUE) {
					continue;
				}
				
				long val = dp[P - 1 - j] + pa[i][j - 1];
				if (val < dp[P - 1 - i]) {
					dp[P - 1 - i] = val;
					backPath[i] = j;
				}
			}
		}

		totalWaste = dp[P - 1];
		i = 0;

		// Reconstitute the choice
		do {
			j = backPath[i];

			// Ignore the waste of time after the last contest
			if (j == P) {
				totalWaste -= pa[i][j - 1];
			}

			// Count contests
			numContests++;
			
			// Go to next group
			i = j;
		} while (j < P);

		// Return final solution
		return new Pair<>(totalWaste, numContests);
	}

	public void solve() {
		readData();
		writeData(getResult());
	}

	public static void main(String[] args) {
		new Planificare().solve();
	}
}