package cs3524.solutions.mud;

import java.rmi.*; 
import java.util.*;
import java.io.*;;
import java.net.InetAddress;

public class MUDClient 
{

	//Creates an instance of MUDService called service
	static MUDService service;

	//Variable declarations
	static BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
    private static String username;
    private static String location; 
    private static String nameOfMUD;

	public static void main(String args[]) throws Exception 
	{
		// An if statement that check if the user has provided a valid host and port 
		if(args.length < 2) 
		{
			System.err.println("Missing arguments. PLease specify both <host> <port>");
			return;
		}

		//The first argument will be made equall to hostname
		String hostName = args[0];

		//The seccond argument will be made equall to port
		int port = Integer.parseInt(args[1]);

		//Specify the security policy and set security manager
		System.setProperty( "java.security.policy", "muddy.policy" ) ;
        System.setSecurityManager( new RMISecurityManager() ) ;


		try
		{
			//Create registration URL from hostname, port
			String regUrl = "rmi://" + hostName + ":" + port + "/MudService";
			service = (MUDService)Naming.lookup(regUrl);
			
			//Once connected call function startUp()
			startUp();
		}
		catch (java.io.IOException e) 
		{
			System.err.println("Input error");
			System.err.println(e.getMessage());
		}
	}

	static void startUp() throws Exception 
	{

		//Print out the String returned by intoroduction function in MUDServiceImpl.java	
		System.out.println(service.welcome());
		nameOfMUD = in.readLine();
		try
		{
			System.out.println(service.pickMUD(nameOfMUD));
			
			//Ask user to set username
			username = in.readLine();
			
			//Use the String returned by getStartLocation from MUDServiceImpl to set the value for location 
			location = service.getStartLocation();
			gamePlay();
		}
		catch(Exception e)
		{
			System.out.println("Server is down, try again soon");
		}

	}

	static void gamePlay() throws Exception 
	{
		
		//A variable that represents whether or not the user is still playing, used to maintain a loop checking for input
		boolean stillPlaying = true;

		//Get user start location and them assign it to location
		String location = service.getStartLocation();

		//Register User location with 
		service.refreshLocation(username, location);
		System.out.println(service.locationInfo(location));

		while(stillPlaying)
		{
			System.out.println("\nWhat would you like to do?");
			
			//Chcks user imput and sets equal to command
			String command = in.readLine();

			if (command.equals("exit")) 
			{
				//If command equals exit print message then leave game
				System.out.println("You'll be back.....");
				System.exit(0);
			}

			else if (command.equalsIgnoreCase("move")) 
			{
				//If command equals move then ask the user where they want to move
				//Accepts 'north' 'south' 'east' 'west'
				System.out
				.println("Which way would you like to move.....");
				String direction = in.readLine();
				
				//Use service method move direction and pass it the current location of the player and the direction they wans to go
				//Both are Strings
				String newLocation = service.move(location, direction.toLowerCase());
				location = newLocation;
				
				//Print out to the user what the surrounding location is like
				System.out.println(service.locationInfo(location));

				service.refreshLocation(username, location);
			}

			else if (command.equalsIgnoreCase("Yell"))
			{
				//If command is yell ask user what they would like to yell
				System.out.println("What would you like to yell?");
				
				//Read in user input
				String yell = in.readLine();
				if (yell.equalsIgnoreCase("Who's there?"))
				{
					//If user asks who's there then print who is at the location, including their own username
					System.out.println(service.whosThere(location));
				}

				else
				{
					//If they don't type a valid input then alert the user
					System.out.println("You can only yell 'Who's there?'");
				}
			}

			else if (command.equalsIgnoreCase("take"))
			{
				//If command is take then ask the user what they would like to take
				System.out.println("What would you like to take?");
				
				//Read in user input
				String item = in.readLine();
				if(service.pickUp(item,location))
				{
					//If pickUp returns true then tell the user they own the item they tried to take
					System.out.println("You now own the " + item+"\n");
				}
				
				else
				{
					//If the item is not at location then tell the user they can't take the item
					System.out.println("You could not take " + item+"\n");
				}
			}

			else if (command.equalsIgnoreCase("create mud"))
			{
				System.out.println("What would you like to call it?");
				String mudName = in.readLine();
				service.createMUD(mudName);
			}


			//CHANGES SUCCESSFULLY BUT THIS IS EXPERIMENTAL AT BEST AND DOES NOT SOLVE MANY PROBLEMS FACED WITH MOVEING MUD
			else if (command.equalsIgnoreCase("Change MUD"))
			{
				startUp();
			}
		}
	}
}

//TO RUN: RUN FROM mud(1)    java cs3524.solutions.mud.MUDClient jack-U05FA 50010
