import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

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

public class Frati {
	private static final String INPUT = "frati.in";
	private static final String OUTPUT = "frati.out";

	private int N;
	private TreeMap<Integer, ArrayList<Integer[]>> concurs;

	private <T1, T2> void sortTreeMap(TreeMap<T1, ArrayList<T2>> treeMap, 
			Comparator<? super T2> comparator) {

		if (treeMap == null || treeMap.isEmpty()) {
			return;
		}

		for (Map.Entry<T1, ArrayList<T2>> e : treeMap.entrySet()) {
			T1 key = e.getKey();
			ArrayList<T2> list = e.getValue();

			Collections.sort(list, comparator);
			treeMap.put(key, list);
		}
	}

	private <T1, T2> void addToTreeMap(T1 k, T2 e, TreeMap<T1, ArrayList<T2>> t) {
		if (t == null) {
			return;
		}

		ArrayList<T2> list = t.get(k);
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(e);
		t.put(k, list);
	}

	private <T1, T2> T2 getNext(TreeMap<T1, ArrayList<T2>> t, int crtBro) {
		if (t.isEmpty()) {
			return null;
		}

		Map.Entry<T1, ArrayList<T2>> e = t.pollLastEntry();
		ArrayList<T2> l = e.getValue();
		T2 res = null;

		if (crtBro == 0) {
			res = l.remove(0);
		} else {
			res = l.remove(l.size() - 1);
		}

		if (!l.isEmpty()) {
			t.put(e.getKey(), l);
		}

		return res;
	}

	private void readData() {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(INPUT));

			N = Integer.parseInt(bfr.readLine());
			concurs = new TreeMap<>();

			for (int i = 0; i < N; i++) {
				String[] tokens = bfr.readLine().split(" ");
				int first = Integer.parseInt(tokens[0]);
				int second = Integer.parseInt(tokens[1]);
				int key = first + second;
				addToTreeMap(key, new Integer[] { first, second }, concurs);
			}

			bfr.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found!");

		} catch (IOException e) {
			System.out.println("Error closing the file!");
		}
	}

	private void writeData(Pair<Integer, Integer> result) {
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

	private Pair<Integer, Integer> getResult() {
		int[] frate = new int[] { 0, 0 };
		Integer[] maxJon, maxSam;

		// Sort pairs
		sortTreeMap(concurs, new Comparator<Integer[]>() {
			@Override
			public int compare(Integer[] o1, Integer[] o2) {
				int sumOfDiffs = (o2[0] - o1[0]) + (o2[1] - o1[1]);
				int jonDiff = o2[0] - o1[0];
				int samDiff = o2[1] - o1[1];
				return sumOfDiffs != 0 ? sumOfDiffs : (jonDiff != 0 ? jonDiff : samDiff);
			}
		});

		// First choice
		maxJon = getNext(concurs, 0);
		maxSam = getNext(concurs, 1);

		for (int numChosen = 0; numChosen < N; numChosen++) {
			int crtBro = numChosen % 2;

			// For last choice, there is only 1 choice
			if (numChosen == N - 1) {
				if (maxJon == null) {
					maxJon = maxSam;
				} else {
					maxSam = maxJon;
				}
			}

			int diff = (maxSam[1] - maxJon[0]) - (maxJon[1] - maxSam[0]);

			// Make the choice of the best solution
			if (diff < 0) {
				frate[crtBro] += maxJon[crtBro];
				maxJon = getNext(concurs, 0);
			} else if (diff > 0) {
				frate[crtBro] += maxSam[crtBro];
				maxSam = getNext(concurs, 1);
			} else {
				int max = Math.max(maxJon[crtBro], maxSam[crtBro]);
				frate[crtBro] += max;
				if (max == maxJon[crtBro]) {
					maxJon = getNext(concurs, 0);
				} else {
					maxSam = getNext(concurs, 1);
				}
			}
		}

		// Return the solution
		return new Pair<>(frate[0], frate[1]);
	}

	public void solve() {
		readData();
		writeData(getResult());
	}

	public static void main(String[] args) {
		new Frati().solve();
	}
}