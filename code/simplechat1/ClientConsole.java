// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client= new ChatClient(host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message; String command;

      while (true) 
      {
        message = fromConsole.readLine();
        command = message.trim();
        /**
         * May be a command
         */
         if(command.compareTo("#quit") == 0) {
             client.closeConnection();
             break;
         } else if (command.compareTo("#logoff") == 0) {
             client.closeConnection();
         } else if (command.startsWith("#sethost")) {
             if (client.isConnected()) {
                 System.out.println("ERROR: please logged off first.");
             } else {
                try {
                    String host = command.substring(9);
                    client.setHost(host.trim());
                    System.out.println("Set host to " + host);
                }
                catch(Throwable t)
                {   
                  System.out.println("ERROR: host argument error");
                }
             }
         } else if (command.startsWith("#setport")) {
             if (client.isConnected()) {
                 System.out.println("ERROR: please logged off first.");
             } else {
                String port = command.substring(10);
                try {
                    client.setPort(Integer.parseInt(port));
                    System.out.println("Set port to " + port);
                }
                catch(Throwable t)
                {   
                  System.out.println("ERROR: port is not integer");
                }
             }
         } else if (command.compareTo("#login") == 0) {
             if (client.isConnected()) {
                 System.out.println("ERROR: please logged off first.");
             } else {
                try {
                    client.openConnection();
                    System.out.println("Login Success");
                }
                catch(IOException ex)
                {   
                  System.out.println("ERROR: login failed");
                  System.out.println(ex);
                }
             }
         } else if (command.compareTo("#gethost") == 0) {
             String host = client.getHost();
             System.out.println(host);
         } else if (command.compareTo("#getport") == 0) {
             int port = client.getPort();
             System.out.println(port);
         } else 
                client.handleMessageFromClientUI(message);
        }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;  //The port number

    try
    {
      host = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }

    try 
    {   
      port = Integer.parseInt(args[1]); //Get port from command line
    }   
    catch(Throwable t)
    {   
      port = DEFAULT_PORT; //Set port to 5555
    }
 
    ClientConsole chat= new ClientConsole(host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
