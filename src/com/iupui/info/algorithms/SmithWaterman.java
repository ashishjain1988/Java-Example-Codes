package com.iupui.info.algorithms;

public class SmithWaterman {

	/**
	 * Stores the values of all the alignments. 2D array to store values for 2
	 * sequences.
	 */
	private int[][] cube;
	/**
	 * Used for backtracking. Keeps track from where the transition was made.
	 */
	private int[][] backtrackcube;
	/**
	 * The given sequences.
	 */
	private String sequence1;
	private String sequence2;
	/**
	 * Provided Blosum matrix.
	 */
	private BlosumMatrix matrix;
	/**
	 * Penalty constant.
	 */
	private final int GAPPenalty = -4;

	/**
	 * Initialisation code. The sequences and the matrix are stored as local
	 * variables.
	 * 
	 * @param matrix
	 * @param sequence1
	 * @param sequence2
	 */
	public SmithWaterman(BlosumMatrix matrix, String sequence1,
			String sequence2) {
		this.matrix = matrix;
		this.sequence1 = sequence1;
		this.sequence2 = sequence2;
		cube = new int[sequence1.length() + 1][sequence2.length() + 1];
		backtrackcube = new int[sequence1.length() + 1][sequence2.length() + 1];
	}

	/**
	 * Returns a sum of blosomValues for 2 given identifiers.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private int getBlosum(char a, char b) {
		return (matrix.getValue(a, b));
	}

	/**
	 * Returns an array, where the first element is the maximum score(value) and
	 * the second element is the index of this value. The index is used for
	 * backtracking.
	 * 
	 * @param array
	 * @return
	 */
	private int[] getMax(int[] array) {
		int max = array[0];
		int maxIndex = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}
		}
		int[] result = { max, maxIndex };
		return result;
	}

	/**
	 * This is cube creation code. There are 3 possible scores to choose from at
	 * each step of the algorithm.
	 */
	private void createCube() {
	//Changed this array to 3 as we are doing Global Alignment and don't want to put 0 in place of negative values.
		int[] matchValues = new int[3];
		for (int i = 1; i < cube.length; i++) {
			for (int j = 1; j < cube[0].length; j++) {
					// Full Match
					matchValues[0] = cube[i - 1][j - 1]
							+ getBlosum(sequence1.charAt(i - 1),
									sequence2.charAt(j - 1));
					// Insertion / Deletion (3*Penalty)/3
					matchValues[1] = cube[i - 1][j] + GAPPenalty;
					matchValues[2] = cube[i][j - 1] + GAPPenalty;
					// Get maximal score
					int[] maxValues = getMax(matchValues);
					// Save the maximum score in the array
					cube[i][j] = maxValues[0];
					// Save the backtracking data.
					backtrackcube[i][j] = maxValues[1];
			}
		}
	}
	
	/**
	 * The method finds the indices of the element with the highest value.
	 * 
	 * @return
	 */

	private int[] getMaxValueIndices() {
		int[] indices = { 0, 0 };
		int max = cube[0][0];
		for (int i = 0; i < cube.length; i++) {
			for (int j = 0; j < cube[0].length; j++) {
					if (cube[i][j] >= max) {
						max = cube[i][j];
						indices[0] = i;
						indices[1] = j;
					}
			}
		}
		return indices;
	}
		
	/**
	 * This method is used for backtracking. The switch helps to determine from
	 * where the transition was made. Backtrack cube keeps track of all the
	 * transitions.
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	private int[] getIndices(int i, int j) {
		int[] indices = { i, j};
		int max = backtrackcube[i][j];
		switch (max) {
		case 1:
			indices[0] = i - 1;
			indices[1] = j - 1;
			break;
		case 2:
			indices[0] = i - 1;
			break;
		case 3:
			indices[1] = j - 1;
			break;
		}
		return indices;
	}

	/**
	 * The aligned sequences are created by careful backtracking from the
	 * highest value. When the index has been decreased that means that a match
	 * has been made and a letter is added to the appropriate sequence.
	 * Otherwise, a deletion/insertion is made and a hyphen is placed in the
	 * string. At the end the strings are reversed.
	 */
	private void printSequences() {
		StringBuilder seq1 = new StringBuilder();
		StringBuilder seq2 = new StringBuilder();
		//Change to get the outermost element of the matrix
		int m = (cube.length - 1);
		int n = (cube[1].length -1);
		int a[] = {m,n};
		int[] maxValueIndices = a;
		/*int[] maxValueIndices = getMaxValueIndices();*/
		int i = maxValueIndices[0];
		int j = maxValueIndices[1];
		int alignmentSum = cube[i][j];
		//System.out.println(i + " " + j + " ");
		while (cube[i][j] != 0 && (i != 0 || j != 0)) {
			int[] indices = getIndices(i, j);
			if (indices[0] < i) {
				seq1.append(sequence1.charAt(i - 1));
			} else {
				seq1.append("-");
			}
			if (indices[1] < j) {
				seq2.append(sequence2.charAt(j - 1));
			} else {
				seq2.append("-");
			}
			i = indices[0];
			j = indices[1];
				i--;
				j--;
				m--;
				n--;
		}
		seq1.reverse();
		seq2.reverse();
		System.out.println(seq1);
		System.out.println(seq2);
		System.out.println(alignmentSum);
	}

	/**
	 * Code used to run the whole algorithm.
	 */
	public void run() {
		createCube();
		printSequences();
		System.out.println(cube);
		System.out.println(backtrackcube);
	}}
