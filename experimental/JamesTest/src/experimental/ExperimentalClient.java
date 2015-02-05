package experimental;

import java.io.*;
import java.net*;

public class ExperimentalClient {

		public static void main(String[] args) throws IOException{
			//prevents incorrect format on running the client
			if(args.length!=2){
				System.err.println("input requires java ExperimentalClient <host name> <port number>");
				System.exit(1);
			}
			//grabs host name and port number from command line input
			String hostName = args[0];
			int portNumber = Integer.parseInt(args[1]);
			
			try(
				//request socket, set up read/write
				Socket xSocket = new Socket(hostName, portNumber);
				PrintWriter xOut = new PrintWriter(xSocket.getOutputSteam(), true);
				BufferedReader xIn = new BufferedReader(new InputStreamReader(xSocket.getInputStream()));
				){
				//reading from system
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
				//getting strings from server/user
				String fromServer;
				String fromUser;
				//while the server can read
				while((fromServer = in.readLine())!=null){
					//report from the server
					System.out.println("Server: " + fromServer);.
					//end condition
					if(fromServer.toLowerCase().equals("bye"))
						break;
					
					//read and report from user
					fromUser = stdIn.readLine();
					if(fromUser!=null){
						System.out.println("Client: " + fromUser);
						System.out.println(fromUser);
					}
				}
			}
			//catch exceptions
			catch(UnknownHostException e){
				System.err.println("cannot find host: " + hostName);
				System.exit(1);
			}
			catch(IOException e){
				System.err.println("couldn't get I/O for host: " + hostName);
				System.exit(1);
			}
		}
	
}
//note: the game will have to create its own server, with the host connecting automatically.