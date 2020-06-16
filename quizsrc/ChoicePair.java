package quizsrc;

import quizsrc.Question.AnswerChoice;

/* Represents an Answer Choice and its 'choice' position on Question List */
public class ChoicePair {

	private String description; // Text to show on button
	
	private AnswerChoice choice; // A, B, C, or D
	
	public ChoicePair() {
		this.description = "";
		
		this.choice = AnswerChoice.ChoiceNull;
	}
	
	public ChoicePair(String desc, AnswerChoice c) {
		this.description = desc;
		
		this.choice = c;
	}
	
	public String getDesc() {
		return this.description;
	}
	
	public AnswerChoice getChoice() {
		return this.choice;
	}
}
