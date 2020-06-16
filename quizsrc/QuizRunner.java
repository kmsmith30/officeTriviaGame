package quizsrc;

public class QuizRunner {
	
	private static QuestionBank questionBank;
	
	private static Gui gui;
	
	public static void main(String[] args) {
		questionBank = new QuestionBank();
		
		gui = new Gui(questionBank);
		
		try {
			gui.wait(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
