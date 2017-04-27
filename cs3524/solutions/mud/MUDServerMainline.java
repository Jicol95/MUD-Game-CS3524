package cs3524.solutions.mud;

import java.io.*;
import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class MUDServerMainline 
{
	static BufferedReader in = new BufferedReader( new InputStreamReader (System.in));

	public static void main (String args[]) 
	{
		//Prevents user from starting up the server without having entered a host and port number
		if (args.length < 2) 
		{
			System.err.println("Missing arguments. PLease specify both <host> <port>");
			return;
		}

		//Variable that contains the port number
		int registryPort = Integer.parseInt(args[0]);
		
		//Variable that contains the hpst number
		int serverPort = Integer.parseInt(args[1]);
		
		//Give the user some feedback so that they know the system is still operational
		System.out.println("Starting server on port " + Integer.toString(registryPort) + "...");

		try 
		{
			//Make hostName equall to localhost
			String hostName = (InetAddress.getLocalHost()).getCanonicalHostName();
        	
			//Specify security policy
        	System.setProperty( "java.security.policy", "muddy.policy" ) ;
        	System.setSecurityManager( new RMISecurityManager() ) ;

			//Create an instance of MUDServiceImpl() 
			MUDServiceImpl mudService = new MUDServiceImpl();
			
			//Create a stub for mudService
			MUDService mudstub = (MUDService)UnicastRemoteObject.exportObject(mudService, serverPort);
			
			//Buile the url
			String regUrl = "rmi://" + hostName + ":" + registryPort + "/MudService";

			try 
			{
				//Bind the mudstub to that url
				Naming.rebind(regUrl, mudstub);
			}
			catch (Exception e) 
			{
				//If the bind failed print the exception message
				System.out.println(e.getMessage());
			}
			
			//Notify the user that the server is now running & where it is running
			System.out.println("Server running at: " + regUrl);
			
			//More text for the user to indicate system progress
			System.out.println("Creating default MUD...");
			
			//Call createMud() which creates an instance of MUD
			mudService.createMUD("default");
		}
		catch(Exception b)
		{
			//If there was a problem with instance creation then alert the user
			System.err.println(b.getMessage());
		}
	}
}

//TO RUN: RUN FROM mud(1)		java cs3524.solutions.mud.MUDServerMainlin50010 50011
