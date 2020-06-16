package quizsrc;

import java.util.*;

/* Holds info for a Question */
public class Question {
	
	public enum AnswerChoice {ChoiceA, ChoiceB, ChoiceC, ChoiceD, ChoiceNull};

	private String query; // Question text
	
	private ArrayList<ChoicePair> choices; // List of choices
	
	private AnswerChoice answer; // Correct Answer Choice
	
	Question(String q, ArrayList<ChoicePair> cs, AnswerChoice a) {
		query = q;
		choices = cs;
		answer = a;
	}
	
	public String getQuery() {
		return query;
	}
	
	public ArrayList<ChoicePair> getChoices() {
		return choices;
	}
	
	public ChoicePair getChoice(AnswerChoice choice) {
		for (ChoicePair pair : choices) {
			if (pair.getChoice() == choice) {
				return pair;
			}
		}
		
		return new ChoicePair();
	}
	
	public AnswerChoice getAnswer() {
		return answer;
	}
	
	public void shuffleChoices() {
		Collections.shuffle(choices);
	}
}
