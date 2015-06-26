//Atilla Saadat
//Hotline Miami
//June 12, 2015
//this server purely sents data using strings, it just connect as many user as it wants in pair, 
//eg connectedPlayer1 with connectedPlayer2, connectedPlayer3 with ConnectedPlayer4, etc.

import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Database{
	
	static ServerSocket serverSocket;
	static Socket socket;
	static DataOutputStream out;
	static DataInputStream in;
	static ArrayList<Users> allUsers = new ArrayList<Users>();
	//contructor
	public Server(){}
	
	//main
	public static void main(String [] args) throws Exception{
		serverSocket = new ServerSocket(PORT);
		int userId = 0;
		try{
			//print host information
			InetAddress localAddress = InetAddress.getLocalHost();
			System.out.println(localAddress.getHostName());
			System.out.println(localAddress.getHostAddress());
		}
		catch (IOException e){}
		while(true){
			//start connection
			socket = serverSocket.accept();
			
			int initialRun;
			if(allUsers.size()==0){
				initialRun = 1;
			}else{
				initialRun = 1;
			}
			for (int i=0; i<allUsers.size()+initialRun; i++){	
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				if (allUsers.size()<userId+initialRun){
					allUsers.add(new Users(out, in, allUsers, userId));
					Thread thread = new Thread(allUsers.get(userId));
					thread.start();
					break;
				}
			}
			userId++;
		}
	}
	
	//the following was an attempt to disconnect users and free up there positions for future queue positions
	/*public static void removeDisconnectedUser(ArrayList<Users> totalUsers, int passedUserId){
		if(totalUsers.size()!=0){
			System.out.println("it was true " +passedUserId);
			allUsers.set(passedUserId,null);		
		}
		for(Users user: allUsers){
			if(user!=null){
				user.updateUserList(allUsers);
			}
		}
	}*/
}
//user class, responsible for sending messages to other users
class Users implements Runnable{
	DataOutputStream out;
	DataInputStream in;
	boolean userDisconnected = false;
	ArrayList<Users> users = new ArrayList<Users>();
	int userId;
	String userName;
	//gets the data streams for sending data as UTF-8, the list of all users, and current userID for seeing to the partner is
	public Users(DataOutputStream out, DataInputStream in, ArrayList<Users> users, int userId){
		this.out=out;
		this.in=in;
		this.users=users;
		this.userId=userId;
		
	}
		
	public void run(){
		while (true){
				String message = null;			
				try{
					//message+added current userID only accessable on the server
					message = in.readUTF()+","+userId;
					if(message!=null){
						//matches current user with the one above or one below based on userId
						//creating pairs of players
						if(userId%2==0){
							users.get(userId+1).out.writeUTF(message);
						}else{
							users.get(userId-1).out.writeUTF(message);

						}
					}
				}catch(IndexOutOfBoundsException e){

				}catch(NullPointerException e){

				}catch(IOException e){
					out=null;
					in=null;
				}
		}
	}
}

//the following was an attempt to send data as bytes instead of string to reduce strain on the server converting string to bytes
//and back, making it client-side. The classes and onjects didnt seem to work together.
/* class ByteHandler{
	
	public ByteHandler(){}
	
	private int currentLength = -1;
	
	void sendData(String message,DataOutputStream out)throws IOException{ 
		byte[] data = message.getBytes();
		int start = 0;
		int length = data.length;
		out.writeInt(length);
		if(length>0){
			out.write(data, start, length);
		}
		currentLength = length;
	}
	
	
	
	void serverRelay(DataInputStream in,DataOutputStream out){
		try{
		int length = in.readInt();
		byte[] data = new byte[length];
		if(length>0){
			in.readFully(data);
		}
		int start = 0;
		out.writeInt(length);
		if(length>0){
			out.write(data, start, length);
		}
		}catch(IOException e){
			System.out.println("stab is a fag");
		}
		
	}
	
	
	
	String receivedData(DataInputStream in)throws IOException{
		int length = in.readInt();
		byte[] data = new byte[length];
		if(length>0){
			in.readFully(data);
		}
		return new String(data);
	}

}*/