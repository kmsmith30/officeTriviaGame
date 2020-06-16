package quizsrc;

import java.io.*;
import java.util.*;

import quizsrc.Question.AnswerChoice;

public class QuestionBank {
	
	private static final String QUESTION_FILE = "office_questions.txt";
	
	private static final int NUM_TOKENS = 7;
	
	private static final int NUM_QUESTIONS = 10;
	
	private static final int INIT_NUM = -1;
	
	private static final AnswerChoice CHOICE_A = AnswerChoice.ChoiceA;
	private static final AnswerChoice CHOICE_B = AnswerChoice.ChoiceB;
	private static final AnswerChoice CHOICE_C = AnswerChoice.ChoiceC;
	private static final AnswerChoice CHOICE_D = AnswerChoice.ChoiceD;
	
	private static final AnswerChoice CHOICE_NULL = AnswerChoice.ChoiceNull;

	private HashMap<Integer, Question> questions; // Holds all questions from file
	
	private Scanner questionScanner; // Scans question file
	
	public QuestionBank() {
		
		try {
			questionScanner = new Scanner(new File(QUESTION_FILE));
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		questions = new HashMap<Integer, Question>();
		
		readQuestionFile();
	}
	
	private void readQuestionFile() {
		while (questionScanner.hasNextLine()) {
			String line = questionScanner.nextLine();
			
			if (line.contains("\"Number\",") || line.length() < 1) {
				continue;
			}
			
			String[] tokens = readLine(line);
			
			int qnum = Integer.parseInt(tokens[0]);
			
			String q = tokens[1];
			
			ArrayList<ChoicePair> choices = new ArrayList<ChoicePair>();
			
			choices.add(new ChoicePair(tokens[2], CHOICE_A));
			choices.add(new ChoicePair(tokens[3], CHOICE_B));
			choices.add(new ChoicePair(tokens[4], CHOICE_C));
			choices.add(new ChoicePair(tokens[5], CHOICE_D));
			
			AnswerChoice answer = getChoiceFromString(tokens[6]);
			
			questions.put(qnum, new Question(q, choices, answer));
		}
		
		questionScanner.close();
	}
	
	private AnswerChoice getChoiceFromString(String whatami) {
		
		switch (whatami) {
		case "A":
			return CHOICE_A;
		case "B":
			return CHOICE_B;
		case "C":
			return CHOICE_C;
		case "D":
			return CHOICE_D;
		default:
			return CHOICE_NULL;
		}
	}
	
	private String[] readLine(String readme) {
		
		String[] tokens = new String[NUM_TOKENS];
		
		String token = "";
		int curr = 0;
		
		boolean inQuote = false;
		
		boolean ignoreQuote = false;
		boolean doneBuff = false;
		
		for (int i = 0; i < readme.length(); i++) {
			char c = readme.charAt(i);
			
			if (c == ',' && !inQuote) {
				tokens[curr] = token;
				
				token = "";
				
				curr++;
				
				doneBuff = true;
				
				continue;
			}
			else if (c == '"') {
				if (!ignoreQuote) {
					inQuote = !inQuote;
				}
				else {
					token += '"';
					
					ignoreQuote = false;
				}
				continue;
			}
			else if (c == '\\') {
				ignoreQuote = true;
				
				continue;
			}
			else {
				if (!doneBuff) {
					token += c;
				}
				else {
					doneBuff = false;
				}
			}
		}
		
		tokens[curr] = token;
		
		return tokens;
	}
	
	public int getSize() {
		return questions.size();
	}
	
	public Question getQuestion(int i) {
		return questions.get(i);
	}
	
	public ArrayList<Question> generateQuestionList() {
		ArrayList<Question> qlist = new ArrayList<Question>();
		
		int[] qnums = new int[NUM_QUESTIONS];
		
		for (int i = 0; i < NUM_QUESTIONS; i++) {
			qnums[i] = INIT_NUM;
		}
		
		for (int i = 0; i < NUM_QUESTIONS; i++) {
			
			int rando = new Random().nextInt(this.getSize() - 1) + 1;
			
			while (isChosen(rando, qnums)) {
				rando = new Random().nextInt(this.getSize() - 1) + 1;
			}
			
			qnums[i] = rando;
		}
		
		for (int i = 0; i < NUM_QUESTIONS; i++) {
			if (qnums[i] == INIT_NUM) {
				System.out.println("Error: something went wrong during getting random qs");
				break;
			}			
			
			qlist.add(this.getQuestion(qnums[i]));
		}
		
		return qlist;
	}
	
	private boolean isChosen(int x, int[] chosen) {
		for (int i = 0; i < NUM_QUESTIONS; i++) {
			if (x == chosen[i]) {
				return true;
			}
		}
		
		return false;
	}
}
