package cs3524.solutions.mud;

import java.rmi.*; 
import java.util.*;

public class MUDServiceImpl implements MUDService 
{
	private MUD mudInstance;
    public Map<String, MUD> MUDs = new HashMap<String, MUD>();
    public Integer mudLimiter = 4;
    public Integer mudCounter = 0;

	public MUDServiceImpl()    throws RemoteException
    { 
        //Boilerplate as there is nothing to construct
	}

	public void createMUD(String mudName) throws RemoteException 
    {
		try 
        {
			//Try and make a instance of MUD In MUD.java
            if(mudCounter == mudLimiter)
            {
                System.out.println("Too many muds created, you can't create anymore.");
            }
            
            else
            {
                MUDs.put(mudName, new MUD("mymud.edg","mymud.msg","mymud.thg"));
                System.out.println("MUD " + mudName + " created");
                mudCounter = mudCounter + 1;
            }
		}
		
        catch (Exception ex) 
        {
			System.err.println("Error creating MUD. Error details: " + ex.getMessage()); 
		}

	}

    public String welcome() throws RemoteException
    {
        String output = "";
        output = ("----- MUDs -----\n");
        
        for(Map.Entry<String, MUD> entry : MUDs.entrySet()) 
        {
            String key = entry.getKey();
            output += (key + "\n");
        }
        
        output += ("---------------\n");
        output += ("Select a MUD to connect to: ");
        
        return output;
    }

    public String pickMUD(String inputMud) throws RemoteException 
    {
        
        String output = "";

        if(inputMud != "")
        {
            mudInstance = MUDs.get(inputMud);
            output = ( "Welcome to " + inputMud + " MUD Server!\n" );
            output += ( "Please enter a username: " );
        }        
        
        return output;    
    }


	public String getStartLocation() throws RemoteException 
    {
        //Used to get the position of an instantiated user
        return mudInstance.startLocation();
    }
    

    
    public String location(String location) throws RemoteException
    {
        //Asks for the location of a user in a MUD world
        return mudInstance.getVertex(location).toString();
    }
    
    
    public String move(String current, String direction) throws RemoteException
    {
        //Allows users to traverse the MUD enviroment given the start location and the desired location
        //User will move if the direction is valid given the location 
        Vertex currentVertex = mudInstance.getVertex(current);
        
        if(currentVertex._routes.containsKey(direction))
        {
            Edge newLocation = currentVertex._routes.get(direction);
            Vertex newVert = (newLocation._dest);
            return newVert._name;
        } 
        
        else 
        {
            return current;
        }
	}
	
   
    public void refreshLocation(String username, String location) throws RemoteException 
    {
        //Remove the user from a location
        mudInstance.users.remove(username);
         //Add them to the new location
        mudInstance.users.put(username, location);
    
        //System.out.println(mudInstance.users);    
	}  

    
    public String whosThere(String location) throws RemoteException
    {
        
        //Creare an array of all players playing
        ArrayList<String> Players = new ArrayList<String>();
        //Instantiate a variable for username 
        String username;
        
        StringBuilder usernameList = new StringBuilder(); 
        
        Iterator i = mudInstance.users.keySet().iterator();
        //Keep adding users who are at a given location, until there is no more users at location    
        while (i.hasNext()) 
        {
	        username = i.next().toString();
            
            if(mudInstance.users.get(username).equalsIgnoreCase(location))
            {
                Players.add(username);
                usernameList.append(username);
                usernameList.append(", ");
            }
                        
	    }
 
        usernameList.setLength(usernameList.length() - 2);

        //return the string of all players at that location
        return "You can see: " + usernameList.toString();
        
    }

    
    public String locationInfo( String location )
    {
    //Return the correct message, given your location, from Mud.thg 
	return mudInstance.getVertex( location ).toString();
    }
    
    
    public boolean pickUp(String item, String location) throws RemoteException 
    {
        Vertex currentVertex = mudInstance.getVertex(location);
        //Get all items at current location       
        List<String> things = currentVertex._things;
        //If there is something at that location        
        if(things.contains(item))
        {
            //Remove it
            mudInstance.delThing(location, item);
            
            if(location.equals("D"))
            {
                //Change the message at location D to indicate that the tresure is no longer here
            	mudInstance.changeMessage(location, "Looks like there used to be treasure here");
            }

            else if(location.equals("A"))
            {
                //Change the message at location A to indicate that the ring is no longer here
                mudInstance.changeMessage(location, "Looks like there used to be a ring here");
            }
            
            return true;
         }
        
        return false;
    }
}