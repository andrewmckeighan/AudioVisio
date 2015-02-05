package Experiments;

import java.net.*;
import java.io.*;


public class ExperimentalProtocol {
	//program states
	private  static final int WAITING = 0;
	private  static final int SENDSTATUSONE = 1;
	private  static final int SENDSTATUSTWO = 2;
	private  static final int SENDSTATUSTHREE = 3;
	//limit to the amount of iterations of the program sending/recieving
	private static final int NUMITERATIONS = 5;
	//initial state and number of iterations
	private int state = WAITING;
	private int currentIteration = 0;
	//server responses
	private String[] messages = {"one", "two", "three", "four", "five"};
	private String[] responeses = {"1","2","3","4","5"};

	
	//takes an input of string, uses state to choose correct reply, outputs correct reply
	//this will send a number, from 1 to 5 first in text, then in numerals.
	public String processInput(String inputString){
		String outputString = null;
		
		if(state == WAITING){
			outputString = "Sending Number in text";
			state = SENDSTATUSONE;
		}
		else if(state == SENDSTATUSONE){
			if(inputString.toLowerCase().equals("okay")){
				outputString = messages[currentIteration];
				state = SENDSTATUSTWO;
			}
			else{
				outputString = "reply with 'okay' to continue...";
				state = SENDSTATUSONE;
			}
		}
		else if(state == SENDSTATUSTHREE){
			if(inputString.toLowerCase().equals("y")){
				outputString = "Sending Number in text";
				if(currentIteration == NUMITERATIONS-1){
					currentIteration = 0;
				}
				else{
					currentIteration++;
					state = SENDSTATUSONE;
				}
			}
			else{
				outputString = "End...";
				state = WAITING;
			}
		}
		return outputString;
	}
}