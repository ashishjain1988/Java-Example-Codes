package com.iupui.info.algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BlosumMatrix {

	/**
	 * Reader used to read input from the provided file.
	 */
	private BufferedReader reader;
	/**
	 * Stores the letter identifiers of the matrix.
	 */
	private String letters;
	/**
	 * Stores all the blosom values.
	 */
	private int[][] blosumValues;

	/**
	 * Reads a blosum matrix from the input file.
	 * 
	 * @param source
	 * @throws IOException
	 */
	public BlosumMatrix(String source) throws IOException {
		reader = new BufferedReader(new FileReader(source));
		String currentLine = reader.readLine();
		letters = currentLine.replaceAll("\\s", ""); // stores the identifiers
		blosumValues = new int[letters.length()][letters.length()];
		int j = 0;
		while ((currentLine = reader.readLine()) != null) {
			String[] values = currentLine.trim().replaceAll(" +", " ")
					.split(" "); // remove all the additional whitespaces
			for (int i = 1; i < values.length; i++) {
				blosumValues[i - 1][j] = Integer.parseInt(values[i]); 
				// parse Strings to Integers
			}
			j++;
		}
	}

	/**
	 * Return a value from the blosom matrix, given letter identifiers.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public int getValue(char a, char b) {
		int index1 = letters.indexOf(a);
		int index2 = letters.indexOf(b);
		return blosumValues[index1][index2];
	}

}
