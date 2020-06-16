package quizsrc;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;

import java.util.*;

import quizsrc.Question.AnswerChoice;

public class Gui {
	
	/* Enum to represent Quiz Graphics States */
	private enum GuiState { StartState, QuestionState, WaitState, ResultState };
	
	private enum ButtonColor { GreenColor, RedColor, WhiteColor };
	
	/* Gui Values */
	private static final int HEIGHT = 400;
	private static final int WIDTH = 750;
	
	private static final int SEC = 1000;
	
	private static final int NUM_QUESTIONS = 10;
	
	// Button Command Values
	private static final String START_CMD = "start";
	private static final String A_CMD = "a";
	private static final String B_CMD = "b";
	private static final String C_CMD = "c";
	private static final String D_CMD = "d";
	private static final String RESTART_CMD = "restart";
	private static final String DONE_CMD = "done";

	private JFrame mainFrame; // Main Frame
	
	private JPanel ctrlPanel; // Control Panel
	
	private JLabel gameLabel; // Game Title
	private JLabel msgLabel; // Feedback Messages
	private JLabel quesLabel; // Question Query 
	
	private JButton startButton; // Start Quiz 
	
	private JButton aButton; // Choice A
	private JButton bButton; // Choice B 
	private JButton cButton; // Choice C 
	private JButton dButton; // Choice D 
	
	private JButton restartButton; // Another Quiz
	private JButton doneButton; // Done with Quiz
	
	private GuiState state; // Holds current state of Gui
	
	private QuestionBank qbank; // All Questions
	private ArrayList<Question> questions; // Ten Random Questions
	
	private Question question; // Holds Current Question
	
	private int currq; // Holds current Question Number
	
	private AnswerChoice selected; // Holds User Answer 
	
	private Timer timer;
	
	private int correct;
	
	public Gui(QuestionBank qb) {
		
		qbank = qb;
		
		questions = new ArrayList<Question>();
		
		question = null;
		
		currq = 0;
		
		correct = 0;

		selected = AnswerChoice.ChoiceNull;
		
		state = GuiState.StartState;
		
		initMainFrame();
		initButtons();
		initTimer();

		gameLabel = new JLabel("\"The Office\" Trivia Quiz", JLabel.CENTER);
		msgLabel = new JLabel("", JLabel.CENTER);
		quesLabel = new JLabel("", JLabel.CENTER);

		gameLabel.setSize(350, 100);
		msgLabel.setSize(350, 100);
		quesLabel.setSize(350, 100);
		
		drawScreen();
	}
	
	private void initMainFrame() {
		mainFrame = new JFrame("Office Trivia");
		mainFrame.setSize(WIDTH, HEIGHT);
		mainFrame.setLayout(new GridLayout(5, 1));
		
		mainFrame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		
		ctrlPanel = new JPanel();
		ctrlPanel.setLayout(new FlowLayout());
	}
	
	private void initButtons() {
		startButton = new JButton("Start");
		
		aButton = new JButton("");
		bButton = new JButton("");
		cButton = new JButton("");
		dButton = new JButton("");
		
		restartButton = new JButton("Retry");
		doneButton = new JButton("Exit");
		
		startButton.setActionCommand(START_CMD);
		
		aButton.setActionCommand(A_CMD);
		bButton.setActionCommand(B_CMD);
		cButton.setActionCommand(C_CMD);
		dButton.setActionCommand(D_CMD);
		
		restartButton.setActionCommand(RESTART_CMD);
		doneButton.setActionCommand(DONE_CMD);
		
		startButton.addActionListener(new ButtonClickListener());
		
		aButton.addActionListener(new ButtonClickListener());
		bButton.addActionListener(new ButtonClickListener());
		cButton.addActionListener(new ButtonClickListener());
		dButton.addActionListener(new ButtonClickListener());
		
		restartButton.addActionListener(new ButtonClickListener());
		doneButton.addActionListener(new ButtonClickListener());
		
		aButton.setFocusPainted(false);
		bButton.setFocusPainted(false);
		cButton.setFocusPainted(false);
		dButton.setFocusPainted(false);
	}
	
	private void initTimer() {
		timer = new Timer(2*SEC, new TimerListener());
		
		timer.setRepeats(false);
		timer.setInitialDelay(2*SEC);
	}
	
	public void drawScreen() {
		mainFrame.setVisible(false);
		
		switch (state) {
		case StartState:
			
			mainFrame.add(gameLabel);
			
			ctrlPanel.add(startButton, BorderLayout.CENTER);
			
			mainFrame.add(ctrlPanel);
			
			break;
		case QuestionState:
			question = questions.get(currq);
			
			question.shuffleChoices();
			
			ArrayList<ChoicePair> choices = question.getChoices();
			
			quesLabel.setText(question.getQuery());
			
			aButton.setText(choices.get(0).getDesc());
			bButton.setText(choices.get(1).getDesc());
			cButton.setText(choices.get(2).getDesc());
			dButton.setText(choices.get(3).getDesc());
			
			msgLabel.setText("Question " + Integer.toString(currq+1) + " / 10");
			
			mainFrame.add(msgLabel);
			
			mainFrame.add(quesLabel);
			
			ctrlPanel.add(aButton);
			ctrlPanel.add(bButton);
			ctrlPanel.add(cButton);
			ctrlPanel.add(dButton);
			
			mainFrame.add(ctrlPanel);
			break;
		case WaitState:
			
			AnswerChoice ac = AnswerChoice.ChoiceNull;
			
			for (int i = 0; i < question.getChoices().size(); i++) {
				ChoicePair cp = question.getChoices().get(i);
				
				if (cp.getChoice() == AnswerChoice.ChoiceNull) {
					continue;
				}
				
				switch (i) {
				case 0:
					ac = AnswerChoice.ChoiceA;
					break;
				case 1:
					ac = AnswerChoice.ChoiceB;
					break;
				case 2:
					ac = AnswerChoice.ChoiceC;
					break;
				case 3:
					ac = AnswerChoice.ChoiceD;
					break;
				}
				
				if (cp.getChoice() == selected) {
					if (selected == question.getAnswer()) {
						colorButton(ac, ButtonColor.GreenColor);
						correct++;
						continue;
					}
					else {
						colorButton(ac, ButtonColor.RedColor);
						continue;
					}
				}
				else if (cp.getChoice() == question.getAnswer()) {
					colorButton(ac, ButtonColor.GreenColor);
					continue;
				}
				else {
					colorButton(ac, ButtonColor.WhiteColor);
				}
			}
			break;
		case ResultState:
			mainFrame.remove(quesLabel);
			
			mainFrame.remove(ctrlPanel);
			
			msgLabel.setText("Result: " + Integer.toString(correct) + " / 10");

			mainFrame.add(msgLabel);
			
			ctrlPanel.remove(aButton);
			ctrlPanel.remove(bButton);
			ctrlPanel.remove(cButton);
			ctrlPanel.remove(dButton);
			
			ctrlPanel.add(restartButton);
			ctrlPanel.add(doneButton);
			
			mainFrame.add(ctrlPanel);
			break;
		default:
			break; // Do Nothing
		}

		mainFrame.setVisible(true);
	}
	
	private void colorButton(AnswerChoice ac, ButtonColor color) {

		mainFrame.remove(ctrlPanel);
		
		switch (ac) {
		case ChoiceA:
			ctrlPanel.remove(aButton);
			
			switch (color) {
			case GreenColor:
				aButton.setBackground(Color.GREEN);
				break;
			case RedColor:
				aButton.setBackground(Color.RED);
				break;
			case WhiteColor:
				aButton.setBackground(Color.WHITE);
				break;
			}
			
			ctrlPanel.add(aButton);
			break;
		case ChoiceB:
			ctrlPanel.remove(bButton);
			
			switch (color) {
			case GreenColor:
				bButton.setBackground(Color.GREEN);
				break;
			case RedColor:
				bButton.setBackground(Color.RED);
				break;
			case WhiteColor:
				bButton.setBackground(Color.WHITE);
				break;
			}
			
			ctrlPanel.add(bButton);
			break;
		case ChoiceC:
			ctrlPanel.remove(cButton);
			
			switch (color) {
			case GreenColor:
				cButton.setBackground(Color.GREEN);
				break;
			case RedColor:
				cButton.setBackground(Color.RED);
				break;
			case WhiteColor:
				cButton.setBackground(Color.WHITE);
				break;
			}
			
			ctrlPanel.add(cButton);
			break;
		case ChoiceD:
			ctrlPanel.remove(dButton);
			
			switch (color) {
			case GreenColor:
				dButton.setBackground(Color.GREEN);
				break;
			case RedColor:
				dButton.setBackground(Color.RED);
				break;
			case WhiteColor:
				dButton.setBackground(Color.WHITE);
				break;
			}
			
			ctrlPanel.add(dButton);
			break;
		default:
			break;
		}
		
		mainFrame.add(ctrlPanel);
	}
	
	private void handleStart() {
		state = GuiState.QuestionState;
		
		mainFrame.remove(ctrlPanel);
		
		ctrlPanel.remove(startButton);
		
		mainFrame.add(ctrlPanel);
		
		questions = qbank.generateQuestionList();
		
		drawScreen();
	}
	
	private void handleChoice(String cmd) {
		if (state == GuiState.WaitState) {
			return;
		}
		
		state = GuiState.WaitState;
		
		switch (cmd) {
		case A_CMD:
			selected = question.getChoices().get(0).getChoice();
			break;
		case B_CMD:
			selected = question.getChoices().get(1).getChoice();
			break;
		case C_CMD:
			selected = question.getChoices().get(2).getChoice();
			break;
		case D_CMD:
			selected = question.getChoices().get(3).getChoice();
			break;
		default:
			break;
		}
		
		timer.start();
		
		drawScreen();
	}
	
	private void handleRestart() {
		state = GuiState.QuestionState;
		
		currq = 0;
		
		correct = 0;
		
		questions = qbank.generateQuestionList();
		
		mainFrame.remove(ctrlPanel);
		
		mainFrame.remove(msgLabel);
		
		ctrlPanel.remove(restartButton);
		ctrlPanel.remove(doneButton);
		
		mainFrame.add(ctrlPanel);
		
		drawScreen();
	}
	
	private class ButtonClickListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent ae) {
			String cmd = ae.getActionCommand();
			
			if (cmd == START_CMD) {
				handleStart();
			}
			else if (cmd == A_CMD || cmd == B_CMD || cmd == C_CMD || cmd == D_CMD) {
				handleChoice(cmd);
			}
			else if (cmd == RESTART_CMD) {
				handleRestart();
			}
			else if (cmd == DONE_CMD) {
				System.exit(0);
			}
		}
	}

	private class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			currq++;
			
			if (currq == NUM_QUESTIONS) {
				state = GuiState.ResultState;
			}
			else {
				state = GuiState.QuestionState;
			}
			
			for (AnswerChoice ac : AnswerChoice.values()) {
				if (ac == AnswerChoice.ChoiceNull) continue;
				
				colorButton(ac, ButtonColor.WhiteColor);
			}
			
			drawScreen();
		}
	}
}
