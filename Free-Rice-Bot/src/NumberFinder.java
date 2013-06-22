import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.FlowLayout;


public class NumberFinder {
	Rectangle bounds;
	BufferedImage box;
	private int firstAnswer, secondAnswer, thirdAnswer, fourthAnswer;
	private int questionAnswer;
	private static int lastQuestionAnswer;
	
	public void setVariables(){
		int w = 50;
		
//		JFrame frame = new JFrame();
//		frame.getContentPane().setLayout(new FlowLayout());
//		frame.getContentPane().add(new JLabel(new ImageIcon(box)));
//		frame.pack();
//		frame.setVisible(true);
		
		ArrayList<Integer> firstAnswerList;
		ArrayList<Integer> secondAnswerList;
		ArrayList<Integer> thirdAnswerList;
		ArrayList<Integer> fourthAnswerList;
		ArrayList<Integer> questionList;
		
		int h = 152; //Height of the first top text
		firstAnswerList = this.setList(w, h);
		
		h = 191;     //Height of the second top text
		secondAnswerList = this.setList(w, h);	
		
		h = 230;     //Height of the third top text
		thirdAnswerList = this.setList(w, h);	
		
		h = 269;     //Height of the fourth top text
		fourthAnswerList = this.setList(w, h);	
		
		h = 114;     //Height of the question text
		w = 74;
		questionList = this.setList(w, h);
		
		this.firstAnswer = convertListToNumber(firstAnswerList, true);
		//System.out.println("1");
		this.secondAnswer = convertListToNumber(secondAnswerList, true);
		//System.out.println("2");
		this.thirdAnswer = convertListToNumber(thirdAnswerList, true);
		//System.out.println("3");
		this.fourthAnswer = convertListToNumber(fourthAnswerList, true);
		//System.out.println("4");
		this.questionAnswer = convertListToNumber(questionList, false);
	}
	
	private ArrayList<Integer> setList(int w, int h){
		ArrayList<Integer> localList = new ArrayList<Integer>();
		int rgbCode;
		for(int i = 176; i < w + 176; i++){
			rgbCode = FreeRiceBot.getEasyRGB(box.getRGB(i, h));
			if(rgbCode == 151){          // Blue color
				localList.add(2);
			}
			else if(rgbCode == 152){     // Light red color
				localList.add(5);
			}
			else if(rgbCode == 423){     // Light Blue
				localList.add(6);
			}
			else if(rgbCode == 265){     // Light Blueish
				localList.add(7);
			}
			else if(rgbCode == 178){     // Red color
				localList.add(3);
			}
			else if(rgbCode == 198){     // Purple color
				localList.add(4);
			}
			else if(!(rgbCode == 557)){  // Not = green color
				localList.add(1);
			}
			else{
				localList.add(0);
			}
		}
		return localList;
	}
	
	//Converts an answer(or question) to a number
	private int convertListToNumber(ArrayList<Integer> list, boolean wantAnswer){
		boolean change = false;
		String convertedList = "";
		int currentNum;
		int i = 0;
		while(true){
			i += 1;
			if(list.get(i) != 0){
				break;
			}
			if(i > 40){
				throw new Error("Missed it by thaaaat much");
			}
		}
		int countSpace = 0;
		boolean onlyOnce = true;
		for(int l = i; l < list.size(); l++){
			currentNum = list.get(l);
			if(currentNum == 0 && !wantAnswer){
				change = true;
				countSpace += 1;
			}
			else if(currentNum != 0 && change && countSpace >= 20 && onlyOnce){
				convertedList += "X";
				convertedList += currentNum;
				change = false;
				onlyOnce = false;
			}
			else if(currentNum == 0){
				change = true;
			}
			else if(currentNum != 0 && change){
				convertedList += "N";
				convertedList += currentNum;
				change = false;
			}
			else if(currentNum != 0 && !change){
				convertedList += currentNum;
			}
			else{
				throw new Error("This should  be impossible, but just in case.");
			}
		}
		String[] nums = convertedList.split("N");
		int finalNum;
		if(wantAnswer){
			finalNum = answerCode(nums);
		}
		else{
			finalNum = Integer.parseInt(questionCode(nums));
		}
		return finalNum;
	}
		
	private int answerCode(String[] code){
		String chosenOne = "";
		String finalNum = "";
		for(int q = 0; q < code.length; q++){
			chosenOne = code[q];
			if(chosenOne.equals("131121")){     //Zero
				finalNum += "0";
			}
			else if(chosenOne.equals("141")){   //One
				finalNum += "1";
			}
			else if(chosenOne.equals("1511121")){   //Two
				finalNum += "2";
			}
			else if(chosenOne.equals("1311116")){   //Three
				finalNum += "3";
			}
			else if(chosenOne.equals("151")){   //Four
				finalNum += "4";
			}
			else if(chosenOne.equals("131111111")){   //Five
				finalNum += "5";
			}
			else if(chosenOne.equals("111121")){   //Six
				finalNum += "6";
			}
			else if(chosenOne.equals("1111111111")){   //Seven
				finalNum += "7";
			}
			else if(chosenOne.equals("151116")){   //Eight
				finalNum += "8";
			}
			else if(chosenOne.equals("151111")){   //Nine
				finalNum += "9";
			}
			else if(chosenOne.equals("1311111111111111111")){
				finalNum += "57";
			}
			else if(chosenOne.equals("11111111111111111111")){
				finalNum += "77";
			}
			else{
				System.out.println(code[q]);
				throw new Error("Not a legal number");
			}
		}
		return Integer.parseInt(finalNum);
	}
		
	private String questionCode(String[] code){
		String chosenOne = "";
		String finalNum = "";
		String[] times;
		//TODO I should neaten this up at some point...
		for(int q = 0; q < code.length; q++){
			chosenOne = code[q];
			if(chosenOne.contains("X")){
				times = chosenOne.split("X");
				if(!finalNum.equals("") && code.length > q+1){
					String bothTogether = questionCode(times);
					String firstSecondNum = bothTogether.substring(0, 1);
					String secondFirstNum = bothTogether.substring(1, 2);
					String firstNum = finalNum + firstSecondNum;
					String[] finalQuestionNum = new String[]{code[q+1]};
					String finalNumber = questionCode(finalQuestionNum);
					String secondNumber = secondFirstNum + finalNumber;
					return Integer.toString(Integer.parseInt(firstNum) * Integer.parseInt(secondNumber));
				}
				else if(!finalNum.equals("") && code.length == q+1){
					String bothTogether = questionCode(times);
					String firstSecondNum = bothTogether.substring(0, 1);
					String secondNum = bothTogether.substring(1, 2);
					String firstNum = finalNum + firstSecondNum;
					return Integer.toString(Integer.parseInt(firstNum) * Integer.parseInt(secondNum));
				}
				else{
					String bothTogether = questionCode(times);
					String firstNum = bothTogether.substring(0, 1);
					String secondNum = bothTogether.substring(1, 2);
					return Integer.toString(Integer.parseInt(firstNum) * Integer.parseInt(secondNum));
				}
			}
			else if(chosenOne.equals("111117")){     //Zero
				finalNum += "0";
			}
			else if(chosenOne.equals("13121")){   //One
				finalNum += "1";
			}
			else if(chosenOne.equals("1511117")){   //Two
				finalNum += "2";
			}
			else if(chosenOne.equals("111111111")){   //Three
				finalNum += "3";
			}
			else if(chosenOne.equals("15117")){   //Four
				finalNum += "4";
			}
			else if(chosenOne.equals("1511111111")){   //Five
				finalNum += "5";
			}
			else if(chosenOne.equals("1511111")){   //Six
				finalNum += "6";
			}
			else if(chosenOne.equals("11111111111")){   //Seven
				finalNum += "7";
			}
			else if(chosenOne.equals("11111121")){   //Eight
				finalNum += "8";
			}
			else if(chosenOne.equals("111111")){   //Nine
				finalNum += "9";
			}
			else{
				throw new Error("Not a legal number " + code[q]);
			}
		}
		return finalNum;
	}
	
	public void clickAnswer(){
		int firstYPoint = bounds.y + 158;
		int secondYPoint = bounds.y + 197;
		int thirdYPoint = bounds.y + 236;
		int fourthYPoint = bounds.y + 275;
		if(questionAnswer == lastQuestionAnswer){
			FreeRiceBot.count -= 1;
		}
		else if(firstAnswer == questionAnswer){
			clickAnswer(firstYPoint);
		}
		else if(secondAnswer == questionAnswer){
			clickAnswer(secondYPoint);
		}
		else if(thirdAnswer == questionAnswer){
			clickAnswer(thirdYPoint);
		}
		else if(fourthAnswer == questionAnswer){
			clickAnswer(fourthYPoint);
		}
		else{
			throw new Error("No Solution");
		}
	}	
	
	private void clickAnswer(int y){
		lastQuestionAnswer = questionAnswer;
		int onlyXPoint = bounds.x + 186;
		Robot robo;
		try {
			robo = new Robot();
			robo.mouseMove(onlyXPoint, y);
			robo.mousePress(InputEvent.BUTTON1_MASK);
			robo.mouseRelease(InputEvent.BUTTON1_MASK);
			robo.delay(50);
			robo.mouseMove(0, 0);
			robo.delay(FreeRiceBot.delayAmount);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isLoading(){
		for(int h = 114; h < 122; h++){
			for(int w = 240; w < 270; w++){
				int color = FreeRiceBot.getEasyRGB(box.getRGB(w, h));
				if(color == 222 || color == 346){
					return true;
				}
			}
		}
		return false;
	}
	
	public NumberFinder(Rectangle bounds){
		try {
			this.bounds = bounds;
			this.box = new Robot().createScreenCapture(bounds);
		} catch (AWTException e) {
			throw new Error("NumberFinder Constructor - Error");
		}
	}
}
