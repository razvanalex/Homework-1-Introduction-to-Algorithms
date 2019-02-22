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

public class Ursi {
	private static final String INPUT = "ursi.in";
	private static final String OUTPUT = "ursi.out";
	private static final int MOD = 1000000007;

	private String smiles = null;

	private void readData() {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(INPUT));
			smiles = bfr.readLine();
			bfr.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			
		} catch (IOException e) {
			System.out.println("Error closing the file!");
		}
	}

	private void writeData(Integer result) {
		try {
			BufferedWriter bfw = new BufferedWriter(new FileWriter(OUTPUT));
			bfw.write(result + "\n");
			bfw.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			
		} catch (IOException e) {
			System.out.println("Error closing the file!");
		}
	}

	private int countOcc(String s, char c) {
		int counter = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				counter++;
			} 
		}
		return counter;
	}

	private Integer getResult() {
		int size = smiles.length();
		int count = countOcc(smiles, '^');
		int[][] dp = new int[size + 1][count + 1];

		// Base case
		dp[0][0] = 1;

		// Apply recurrence
		for (int i = 1; i <= size; i++) {
			for (int d = 0; d <= count; d++) {
				if (smiles.charAt(i - 1) == '_') {
					dp[i][d] = (int)((1L * d * dp[i - 1][d]) % MOD);
				} else if (smiles.charAt(i - 1) == '^') {
					if (d + 1 <= count) {
						dp[i][d] += (int)((1L * (d + 1) * dp[i - 1][d + 1]) % MOD);
						dp[i][d] %= MOD;
					}
					if (d - 1 >= 0) {
						dp[i][d] += dp[i - 1][d - 1];
						dp[i][d] %= MOD;
					}
				}
			}
		}
		
		// Return solution
		return dp[size][0];
	}

	public void solve() {
		readData();
		writeData(getResult());
	}

	public static void main(String[] args) {
		new Ursi().solve();
	}
}