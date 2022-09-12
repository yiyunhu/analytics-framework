package edu.cmu.cs514.hw6.framework;

import java.sql.Timestamp;

/**
 * Used to process data, compare and sort the timestamp
 */
public class processedData implements Comparable<processedData>{
	private String text;
	private Timestamp timestamp;
	private float score;

	public processedData(String text, Timestamp timestamp) {
		this.text = text;
		this.timestamp = timestamp;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}


	public double getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}


	@Override
	public int compareTo(processedData o) {
		return this.getTimestamp().compareTo(o.getTimestamp());
	}
}