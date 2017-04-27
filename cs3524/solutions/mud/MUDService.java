package cs3524.solutions.mud;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MUDService extends Remote
{
    public void createMUD(String mudName) throws RemoteException;
    
    public String getStartLocation() throws RemoteException;
    
    public String location(String location) throws RemoteException;
    
    public String locationInfo( String loc ) throws RemoteException;    
    
    public String move(String current, String direction) throws RemoteException;
    
    public String pickMUD(String inputMud) throws RemoteException;
    
    public boolean pickUp(String item, String location) throws RemoteException;
    
    public void refreshLocation(String username, String location) throws RemoteException ;
    
    public String welcome() throws RemoteException;
    
    public String whosThere(String location) throws RemoteException;
}