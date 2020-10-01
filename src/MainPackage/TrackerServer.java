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
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrackerServer {

	
	static ServerSocket TrackerServer;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//************* Files and  folder *******************************
		File FolderT = new File("TrackerFolder");
		FolderT.mkdir();// create the directory of the folder 
		File FileT = new File("TrackerFolder/Tracker.txt");
		File FileTT = new File("TrackerFolder/TrackerTemp.txt");
		File File1 = new File("TrackerFolder/File1.txt");
		File File2 = new File("TrackerFolder/File2.txt");
		File File3 = new File("TrackerFolder/File3.txt");
		try {
			FileTT.createNewFile();// create fileTT for Hashed filenames + user that have file
			FileT.createNewFile();//create fileT For Hashed filenames
			File1.createNewFile();//create file1
			File2.createNewFile();//create file2
			File3.createNewFile();//create file3	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//****************** Start Thread *****************************
		try {
			TrackerServer = new ServerSocket(4952);
			System.out.println("Tracker Server is ready to share files.");
			while(true)
			{
				Socket Peer = TrackerServer.accept();
				
				Thread P2P = new PeerClass(Peer);
				P2P.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}		
}

//************* PeerClass ( *Thread* ) *********************

class PeerClass extends Thread{
	
	private static Socket Peer = new Socket();
	
	static int currentpeer=1; // help in create folder to peer and its file for the new peer
	static int currentpeersame=1; // help in create folder to peer and its file for the same peer
	static String sreach;
	static String CurP; // currentpeer
	static String CurPsame;//cuuenterpeersame
	static int samepeer=1;
	static String sp; 
	static String newpeerfolder;
	static int newpeerfoldercounter=1;
	
	// Constructor 
	public PeerClass(Socket Peer) {
		PeerClass.Peer = Peer;
	}
	
	@SuppressWarnings("unused")
	@Override
	public void run() {
		
		newPeer();
		System.out.println("End of program.");	
	}
	
	
//************************ Functions ********************************


//* Function for new peer
 static void newPeer() {
	 
	 try {
		 
		 
		    DataInputStream InRCV = new DataInputStream(Peer.getInputStream()); // to receive request message from peer.
			
		    DataOutputStream OutSND = new DataOutputStream(Peer.getOutputStream()); // to send response message to peer.
			
			
		    OutSND.writeUTF("Connected Successfully!");
		    
			//************** for login process ************************
			// for only new peer
			OutSND.writeUTF("Enter UserName: "); // send to peer
			String usernamemsg = InRCV.readUTF(); // rcv from peer 
			//OutSND.writeUTF("UserName OK");
			//***********
			OutSND.writeUTF("Enter your password: ");
			String passwordmsg = InRCV.readUTF();
			//OutSND.writeUTF("Password Ok");
			//*********************************************************
			
			
			while(true)
			{

				
				
				OutSND.writeUTF("Enter the key of the file you want to download: ");
				
				String Fkey = InRCV.readUTF(); // to rcv the key 
				
				//*** search for the key in the file 
			 	Path p = Paths.get("TrackerFolder/Tracker.txt").toAbsolutePath();
				List<String> titles = Files.lines(p).collect(Collectors.toList());
				String sreach = Fkey;
				
				boolean infile = titles.stream().anyMatch(predicate->predicate.equalsIgnoreCase(sreach));
				CurP = Integer.toString(currentpeer); // counter to set the number of the peer (new peer)
				if(infile)//file found
				{
					
					
					OutSND.writeUTF("200 OK - File Sent Successfully!"); // send this to this peer on the port and its IP
					File FoldPeer = new File("Peer"+CurP);//to create the folder
					FoldPeer.mkdir();
					
					// the following if and else to check on input and send the file to the peer running
					if(sreach.equals("12"))
					{
					newpeerfolder = Integer.toString(newpeerfoldercounter);//newpeerfoldercounter is a counter to set the number of the current peer to create the file in the right folder for the current peer.
					File file = new File("Peer"+newpeerfolder+"/File"+CurP+".txt");
					//File file = new File("Peer1/File1.txt");	
					file.createNewFile();
					}
					
					else if(sreach.equals("13"))
					{
						CurP = Integer.toString((newpeerfoldercounter+1));
						newpeerfolder = Integer.toString(currentpeer);
						File file = new File("Peer"+newpeerfolder+"/File"+CurP+".txt");
						file.createNewFile();
					}
					else if(sreach.equals("100"))
					{
						CurP = Integer.toString((newpeerfoldercounter+2));
						newpeerfolder = Integer.toString(currentpeer);
						File file = new File("Peer"+newpeerfolder+"/File"+CurP+".txt");
						file.createNewFile();
					}
					///********** to add in the file ********************
					CurP = Integer.toString(currentpeer); // to put 1 again in CurP to write user 1 for ex in the file Tracker
					FileWriter FW = new FileWriter("TrackerFolder/TrackerTemp.txt",true);
					FW.write(sreach+" user "+CurP);
					FW.write("\r\n");
					FW.close();
					
					//**** recent copy of tracker to peer
					File recent = new File("Peer"+CurP+"/TrackerTemp.txt");
					recent.createNewFile();
					FileWriter fwt = new FileWriter("Peer"+CurP+"/TrackerTemp.txt",true);
					fwt.write(sreach+" user "+CurP);
					fwt.write("\r\n");
					fwt.close();
					currentpeer++; //to switch to peer 2
					//***********************************************************
					OutSND.writeUTF("Enter cont to download another file or enter 'close' to close the connection: ");
					
					String Answer = InRCV.readUTF();//rcv the another req
					
					if(Answer.equals("close"))
					{
						try {
						OutSND.writeUTF("Connection Closed!");
						
						Peer.close();
						break;
						}catch (Exception e) {
							// TODO: handle exception
							 e.printStackTrace();
						}
						
						
					}
					else if(Answer.equals("cont"))
					{
						
						samePeer();
						break;
					}
					else
					{
						OutSND.writeUTF("Invalid Input. please run again!");
						break;
					}
				
				}
				else
				{
					OutSND.writeUTF("400 ERROR - File Does not Found! ");// this line only print yes in server console
					break;
				}
				InRCV.close();
				OutSND.close();
			}
			
			
	 }catch (Exception e) {
		// TODO: handle exception
		 e.printStackTrace();
	}
	 
	 
 }

 //Function for the same Peer
 static void samePeer() {
	 try {
		 
		 	sp=Integer.toString(samepeer); // counter for the same peer
		 	CurPsame = Integer.toString(currentpeersame);	 // 
		    DataInputStream InRCV = new DataInputStream(Peer.getInputStream()); // to receive request message from peer.
			
		    DataOutputStream OutSND = new DataOutputStream(Peer.getOutputStream()); // to send response message to peer.
			
		    			
		    
			OutSND.writeUTF("Enter the key of the file you want to download: ");
			
			String Fkey = InRCV.readUTF(); // to rcv the key 
		    
		    
		    while(true)
		    {
		    	
		    	Path p = Paths.get("TrackerFolder/Tracker.txt").toAbsolutePath();
				List<String> titles = Files.lines(p).collect(Collectors.toList());
				String sreach = Fkey;
				
				boolean infile = titles.stream().anyMatch(predicate->predicate.equalsIgnoreCase(sreach));
				currentpeer=1;
				CurP = Integer.toString(currentpeer);//file number based on hash number
				if(infile)//file found
				{
					
					
					OutSND.writeUTF("200 OK - File Sent Successfully!"); // send this to this peer on the port and its IP
					if(sreach.equals("12"))
					{
						
						File filee = new File("Peer"+sp+"/File"+CurP+".txt");
						filee.createNewFile();
					}
					else if(sreach.equals("13"))
					{
						CurP = Integer.toString(currentpeer+1);
						File filee = new File("Peer"+sp+"/File"+CurP+".txt");
						filee.createNewFile();
					}
					else if(sreach.equals("100"))
					{
						CurP = Integer.toString(currentpeer+2);
						File filee = new File("Peer"+sp+"/File"+CurP+".txt");
						filee.createNewFile();
					}
					
					
					CurP = Integer.toString(currentpeer); // to write user 1 for ex
					FileWriter fww = new FileWriter("TrackerFolder/TrackerTemp.txt",true);
					//fww.write("\r\n");
					fww.write(sreach+" user "+CurPsame);
					fww.write("\r\n");
					fww.close();
					currentpeer++;
					currentpeersame++;
					//**** recent copy of tracker to peer
					File recent = new File("Peer"+sp+"/TrackerTemp.txt");
					recent.createNewFile();
					FileWriter fwt = new FileWriter("Peer"+sp+"/TrackerTemp.txt",true);
					fwt.write(sreach+" user "+CurPsame);
					fwt.write("\r\n");
					fwt.close();
					//********************************************
					OutSND.writeUTF("Enter cont to download another file or enter 'close' to close the connection: ");
					
					String Answer = InRCV.readUTF();//rcv the another req
					
					if(Answer.equals("close"))
					{
						try {
							OutSND.writeUTF("Connection Closed!");
							Peer.close();
							break;
							}catch (Exception e) {
								// TODO: handle exception
								 e.printStackTrace();
							}
					}
					else if(Answer.equals("cont"))
					{
						
						samePeer();
					}
					else
					{
						OutSND.writeUTF("Invalid Input. please run again!");
						break;
					}
				}
				else
				{
					OutSND.writeUTF("400 ERROR - File Does not Found! ");// this line only print yes in server console
					break;
				}
				InRCV.close();
				OutSND.close();
		    }//end while
		 
	 }catch (Exception e) {
		// TODO: handle exception
		 e.printStackTrace();
	}
 }

}//* end of class PeerClass extends Thread *******************
