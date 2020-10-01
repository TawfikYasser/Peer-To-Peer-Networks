/*
 * 
 * 
 * 
 * 
 * 
 * @Network Project
 * @P2P
 * 
 * 
 * 
 * 
 */
package MainPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Peer {
	public static  String sF1;
	public static  String sF2;
	public static  String sF3;
	public static int counterPeer=0; // to know the number of peer to create folder!!!
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		
		// Hash Function to convert filename(s) to its key
		HashMap<String,Integer> HM = new HashMap<>();
		HM.put("File1",12);
		HM.put("File2",13);
		HM.put("File3",100);
		int if1 = HM.get("File1");
		int if2 = HM.get("File2");
		int if3 = HM.get("File3");
		sF1 = Integer.toString(if1);
	    sF2 = Integer.toString(if2);
		sF3 = Integer.toString(if3);
		//******************************************************
		
		
		
		
		//******** to insert this keys to Tracker file *****************
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("TrackerFolder/Tracker.txt"));
		
	    writer.write(sF1);
	    writer.newLine();
	    writer.write(sF2);
	    writer.newLine();
	    writer.write(sF3);
	    writer.newLine();
	    writer.close();
	 	
	    //********************************************************************
	    AsPeer();
	}
//******************************************************** end of main ***
	static void AsPeer() throws IOException
		{
		
			try {
				InetAddress IP = InetAddress.getByName("localhost");
				Socket PeerSocket = new Socket(IP,4952);
				String PeerIP = IP.getHostAddress();
				DataInputStream InputToRCV = new DataInputStream(PeerSocket.getInputStream());
				DataOutputStream OutputToSND = new DataOutputStream(PeerSocket.getOutputStream());
				Scanner two = new Scanner(System.in);
				
				//************for welcome msg from server ********************
				String ServerWelMsg = InputToRCV.readUTF(); // RCV (OutSND.writeUTF("Connected Successfully!");)
				System.out.println("Server: "+ServerWelMsg); // print it
				//******************************************


				//********* for login process **************
				//username
				String usernamemsg = InputToRCV.readUTF(); // rcv from server
				System.out.println(usernamemsg); //print it
				//******************************************
				String req = two.nextLine();
				OutputToSND.writeUTF(req); // send the reply to server 
				
				
				//*********************************************
				//password
				String passwordmsg = InputToRCV.readUTF();
				System.out.println(passwordmsg);
				//**********************************************
				String req2 = two.nextLine();
				OutputToSND.writeUTF(req2);
				//**********************************************
				
				
				
				// store user names and passwords to save peers connect, in file Login
				 FileWriter fileWriter = new FileWriter("Login.txt", true);
				 fileWriter.write(req);
				 fileWriter.write(" - ");
				 fileWriter.write(req2);
				 fileWriter.write(" - ");
				 fileWriter.write(PeerIP);
				 fileWriter.write("\r\n");	 
				 fileWriter.close();
				 
				 
				//************************************************
				
				// while here 
				
				 while(true)
				 {
					 Scanner in = new Scanner(System.in);
					 String Fdown = InputToRCV.readUTF(); // to rcv OutSND.writeUTF("Enter the key of the file you want to download: ");
					 System.out.println(Fdown); // print it
					 
					 String fkey =Integer.toString(two.nextInt());// to let peer to enter the key 
					 OutputToSND.writeUTF(fkey); // send the key
					 
					 
					 //***************************************************************
					 counterPeer++;// first peer request file then we will be in peer2 
					 //***************************************************************
					 
					 
					 
					 String ResMsg = InputToRCV.readUTF(); // rcv the response from the server  200 Ok - or - 400 Not Found
					 System.out.println(ResMsg); // print it
					 

					 //****************************************************************
					
					 // to rcv here after download the file 
					 String AGain = InputToRCV.readUTF();
					 System.out.println("server say: "+AGain);
					 
					 
					 String Ag = in.nextLine();
					 OutputToSND.writeUTF(Ag); // close or another
					 
					
					 		
				 }
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}
