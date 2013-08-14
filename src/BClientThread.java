import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map.Entry;

public class BClientThread extends Thread {
	Socket client_socket = null;
	Socket friend_socket = null;
	BServer bs;
	
	String client_msg;
	boolean run=true;
	
	public BClientThread(Socket client_socket,BServer bs) 
	{
	    this.client_socket = client_socket;
	    this.bs = bs;
	}
	
	public void broadCastMsg(String msg)
	{
		
		for(int i=0;i<bs.getClient_socket_list().size();i++)
		{
		
			try 
			{
				new DataOutputStream(bs.getClient_socket_list().get(i).getOutputStream())
				.writeBytes("["+msg+"]\n");
				
			} 
			catch (IOException e) 
			{
				//delete broken client socket stop blocking thread
				run = false;
				bs.getClient_socket_list().remove(bs.getClient_socket_list().get(i));
				bs.getClient_socket_list().remove(bs.getClient_socket_list().get(i));
				e.printStackTrace();
			}
		}
	}
	
	public void broadCastTable()
	{
		//remove null item
		for (Entry<Socket, String> entry : bs.getCreate_socket_list().entrySet() ) 
		{
		    Socket key = entry.getKey();
		    String value = entry.getValue();
	    	broadCastMsg(value);
		    
		}
	}
	public Socket getKeyFromValue(String value_orig)
	{
		//remove null item
		for (Entry<Socket, String> entry : bs.getCreate_socket_list().entrySet() ) 
		{
		    Socket key = entry.getKey();
		    String value = entry.getValue();
		    if(value.split(",")[1].equalsIgnoreCase(value_orig)){
		    	return key;
		    }
		}
		return null;
	}
	
	public void talkWithMyFriend(String msg){
		try 
		{
			new DataOutputStream(friend_socket.getOutputStream())
			.writeBytes("["+msg+"]\n");
			
		} 
		catch (IOException e) 
		{
			//delete broken client socket stop blocking thread
			bs.getClient_socket_list().remove(friend_socket);
			bs.getClient_socket_list().remove(friend_socket);
			e.printStackTrace();
		}
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	public void run() 
	{
	       
	    while(run)
	    {
	    	//Waiting Socket Client PING
	
	        DataInputStream inFromClient;
			try 
			{
				inFromClient = new DataInputStream(client_socket.getInputStream());
				client_msg = inFromClient.readLine();
				
				//check client command
				if(client_msg == null){
					//in here when client disconnect 
					System.out.println("Client force shutdown");
					bs.getClient_socket_list().remove(client_socket);
					bs.getCreate_socket_list().remove(client_socket);
					broadCastTable();
					break;
				}
				
				String split_msg[] = client_msg.split(",");
				for(int i=0;i<split_msg.length;i++)
				{
					System.out.println("["+i+"]"+split_msg[i]);
				}
				
				
				if(isInteger(split_msg[0]))
				if(Integer.parseInt(split_msg[0]) == 0)
				{
					System.out.println("command: broadcast");
					broadCastMsg(client_msg);
				}
				else if (Integer.parseInt(split_msg[0]) == 1){
					System.out.println("command: create room");
					bs.getCreate_socket_list().put(client_socket, client_msg);
					broadCastTable();
				}
				else if (Integer.parseInt(split_msg[0]) == 2)
				{
					System.out.println("command: join room");
					friend_socket = getKeyFromValue(split_msg[1]);
					//pair friend socket point to this socket
					bs.getClient_thread_list().get(friend_socket).friend_socket = client_socket;
				}
				else if (Integer.parseInt(split_msg[0]) == 3)
				{
					System.out.println("command: start chatting");
					talkWithMyFriend(client_msg);

				}
				
				else
				{
					System.out.println("Invalid command");
				}
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
	    
	    }
	    
	}
}