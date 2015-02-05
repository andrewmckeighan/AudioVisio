package Experiments;

import java.net.*;
import java.io.*;

public class ExperimentalServer {
	//from http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockServer.java
	//commenting the code to figure out how the server works
	public static void main(String[] args) throws IOException{
		//Does the game run this program when "host" is pushed, or how does that work?
		
		//performs check for one argument: port number. Reports errors, exits if incorrect input
		if(args.length  != 1){
			System.err.println("Usage: java ExperimentServer *port number*");
			//make adjustments for amount of safe digits
			System.exit(1);
		}
		//grabbing port number from command line args
		int portNumber = Integer.parseInt(args[0]);
		
		try(
			//creates a ServerSocket, which is a Socket listening for a request via a port
			//Will exit if port is not available
			//need to find the correct library
			ServerSocket xServerSocket = new ServerSocket(portNumber);
			//the ServerSocket accepts request from port, creates a Socket with the port and address of the ServerSocket
			//the ServerSocket will continue to listen for requests, can make other sockets
			Socket xClientSocket = xServerSocket.accept();
			//Creates a way to write to an output stream from the socket
			PrintWriter xOut = new PrintWriter(xClientSocket.getOutputStream(), true);
			//xOut has autoflushing...what is this?
			//Creates a way to read from an input stream from the socket, via a stream reader
			BufferedReader xIn = new BufferedReader(new InputStreamReader(xClientSocket.getInputStream()));
			){
			//if server connection can be made, protocol with client starts
			String input, output;
			//gets the protocol
			ExperimentalProtocol xP = new ExperimentalProtocol();
			//makes a string of output from the protocol processes
			output = xP.processInput(null);
			xOut.println(output);
			
			while((input = xIn.readLine())!= null){
				output = xP.processInput(input);
				xOut.println(output);
				//termination condition
				if(output.equals("Bye"))
						break;
			}
			
		}
		//if the port doesn't catch a request or can't be used, prints message, plus the ioexception message
		catch(IOException e){
			System.out.println("Error with port number " + portNumber + "or port is listening");
			System.out.println(e.getMessage());
		}
		
	}

}