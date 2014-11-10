package com.iupui.info.algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sequence {
	/**
	 * Reader used to read input from the provided file.
	 */
	private BufferedReader reader;
	/**
	 * Keeps the string representation of the sequence read from the file.
	 */
	private String stringSequence;

	/**
	 * Creates a reader object and then reads the data from the source.
	 * 
	 * @param source
	 * @throws IOException
	 */
	public Sequence(String source) throws IOException {
		reader = new BufferedReader(new FileReader(source));
		readFastaSequence();
	}

	/**
	 * Stringbuilder is used, to avoid continuous recreation of a string. The
	 * input is stored in the stringSequence private field.
	 * 
	 * @throws IOException
	 */
	private void readFastaSequence() throws IOException {
		StringBuilder builder = new StringBuilder();
		String currentLine;
		reader.readLine(); // skip the header as it is not used in this exercise
		while ((currentLine = reader.readLine()) != null) {
			builder.append(currentLine);
		}
		stringSequence = builder.toString();
	}

	/**
	 * Returns a spring representation of the sequence.
	 * 
	 * @return
	 */
	public String getString() {
		return stringSequence;
	}

}
