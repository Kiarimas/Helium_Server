import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Server 
 * the Sever for Helium
 * 
 * @author Lucas_Catron
 *
 */
public class Server implements Runnable {

	private boolean open;
	private int port;
	private ServerSocket listener;
	private String serverName;
	private String password = "";

	/**
	 * Server 
	 * 
	 * Constructor; sets the port number
	 * @param portNum number to set to
	 */
	Server(int portNum) {
		setPort(portNum);
	}
	
	/**
	 * Server 
	 * 
	 * Constructor; sets the port number, and name
	 * @param portNum number to set to
	 */
	Server(int portNum, String name) {
		setPort(portNum);
		setName(name);
	}
	
	/**
	 * Server 
	 * 
	 * Constructor; sets the port number, and name, and password
	 * @param portNum number to set to
	 */
	Server(int portNum, String name, String pass) {
		setPort(portNum);
		setName(name);
		setPassword(pass);
	}

	/**
	 * openServer
	 * opens new server
	 * @return
	 */
	public boolean openServer() {
		(new Thread(new Server(port))).start();
		return true;
	}

	/**
	 * closeServer()
	 * 
	 * @return boolean, successful close = true
	 */
	public boolean closeServer() {
		open = false;
		return true;

	}


	@Override
	public void run() {
		try {
			listener = new ServerSocket(port);
			open = true;
			try {
				while (open) {
					System.out.println("Waiting for a client...");
					Socket socket = listener.accept();
					System.out.println("Accepted a socket...");
					System.out.println();
					try {
						Person person = new Person(socket);
						initalOut(person.getSocket());
						while(initalIn(person.getSocket(), person));

					} finally {
//						socket.close();
					}

				}

			} finally {
				listener.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * initialOut
	 * place holder for future first contact with client
	 * @param socket
	 * @throws IOException
	 */
	private void initalOut(Socket socket) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println("10");
		out.flush();
		//out.close();
	}

	/**
	 * initalIn
	 * This method MUST run until input receive or time out. Takes response from client. Place holder
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private boolean initalIn(Socket socket, Person p) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		boolean t = true;
		boolean wait = true;
		while (t) {
			if (in.ready()) {
				String answer = in.readLine();
				System.out.println(answer);
				p.setHandle(answer);
				answer = in.readLine();
				System.out.println(answer);
				System.out.println(checkPassword(answer));     ////BROKEN
				wait = false;
			} else {
				t = false;
			}
		}
//		in.close();
		return wait;
	}
	//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ALL BELOW ARE SET, GET, CHECK METHODS/////////////////////////////////
	/**
	 * setPort
	 * @param num
	 */
	public void setPort(int num) {
		port = num;
	}

	/**
	 * getPort
	 * @return the port number
	 */
	public int getPort() {
		return port;
	}
	
	public void setName(String nim){
		serverName = nim;
	}
	
	public String getName(){
		return serverName;
	}
	
	private boolean setPassword(String x) {
		password = x;
		return true;
	}
	
	private boolean checkPassword(String x) {
		if (x == password) {
			return true;
		}
		else { return false;}
	}

}
