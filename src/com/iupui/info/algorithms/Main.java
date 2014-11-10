package com.iupui.info.algorithms;

import java.io.IOException;

public class Main {

	/**
	 * IO is done in separate classes. The FASTA sequence is parsed in the
	 * Sequence class and the blosum matrix is read from the file in the
	 * BlosumMatrix class. When all the input is ready, appropriate objects are
	 * passed to the algorithm object. The algorithm is implemented in the
	 * SmithWaterman class.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String sequence1 = (new Sequence("C:\\Users\\Ashish\\Downloads\\rejavahelp\\seq1.fasta")).getString();
			String sequence2 = (new Sequence("C:\\Users\\Ashish\\Downloads\\rejavahelp\\seq2.fasta")).getString();
			BlosumMatrix matrix = new BlosumMatrix("C:\\Users\\Ashish\\Downloads\\rejavahelp\\blosum.txt");
			SmithWaterman algorithm = new SmithWaterman(matrix, sequence1, sequence2);
			algorithm.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
