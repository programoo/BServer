import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class SocketHashMap extends Thread {
	Socket client_socket = null;
	private ArrayList<Socket> client_socket_list;
	private HashMap<Socket,String> create_socket_list;
	private HashMap<Socket,String> create_socket_list_miror;
	
	public SocketHashMap(Socket client_socket,String data)
	{
	    this.client_socket = client_socket;
	    this.client_socket_list = client_socket_list;
	    this.create_socket_list = create_socket_list;
	}
	
		
}