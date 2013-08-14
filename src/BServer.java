import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class BServer {
	
	private int port;
	private ServerSocket s_sock;
	private ArrayList<Socket> client_socket_list;
	private HashMap<Socket,String> create_socket_list;
	private HashMap<Socket,BClientThread> client_thread_list;
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ServerSocket getS_sock() {
		return s_sock;
	}

	public void setS_sock(ServerSocket s_sock) {
		this.s_sock = s_sock;
	}

	public ArrayList<Socket> getClient_socket_list() {
		return client_socket_list;
	}

	public void setClient_socket_list(ArrayList<Socket> client_socket_list) {
		this.client_socket_list = client_socket_list;
	}

	public HashMap<Socket, String> getCreate_socket_list() {
		return create_socket_list;
	}

	public void setCreate_socket_list(HashMap<Socket, String> create_socket_list) {
		this.create_socket_list = create_socket_list;
	}

	public HashMap<Socket, BClientThread> getClient_thread_list() {
		return client_thread_list;
	}

	public void setClient_thread_list(
			HashMap<Socket, BClientThread> client_thread_list) {
		this.client_thread_list = client_thread_list;
	}

	public BServer(){
		port = 50000;
		try 
		{	
			s_sock = new ServerSocket(port);
			client_socket_list = new ArrayList<Socket>();
			create_socket_list = new HashMap<Socket,String>();
			client_thread_list	= new HashMap<Socket,BClientThread>();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
		
	}
	
	public void start(){
		try {
			while(true){
				
				System.out.println("Server start with port: "+port);
				Socket client_socket = s_sock.accept();
				client_socket_list.add(client_socket);
				broadCastSharedTable();
				BClientThread bt = new BClientThread(client_socket, this);
				client_thread_list.put(client_socket, bt);
				bt.start();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void broadCastSharedTable(){

		//remove null item
		for(int i=0;i<client_socket_list.size();i++){
		
			try {
				new DataOutputStream(client_socket_list.get(i).getOutputStream())
				.writeBytes("online :"+client_socket_list.size());
				
			} catch (IOException e) {
				//delete broken client socket
				client_socket_list.remove(client_socket_list.get(i));
				e.printStackTrace();
			}
			
			
		}
	}
	
    public static void main(String[] args) throws IOException {
    	new BServer()
    	.start();
        
        
    }//end main

}
