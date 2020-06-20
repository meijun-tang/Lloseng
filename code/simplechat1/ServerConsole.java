// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import common.*;

/**
 * This class constructs the UI for a EchoServer client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ServerConsole.
   */
  EchoServer client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {
    try 
    {
      client = new EchoServer(port);
      client.listen();
    } 
    catch(IOException exception) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the server's message handler.
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
             client.doclose();
             break;
         } else if (command.compareTo("#stop") == 0) {
            client.stopListening();
         } else if (command.compareTo("#close") == 0) {
            client.doclose();
         } else if (command.startsWith("#setport")) {
             if (!client.isClosed()) {
                 System.out.println("ERROR: please close first.");
             } else {
                String port = command.substring(9);
                try {
                    client.setPort(Integer.parseInt(port));
                    System.out.println("Port set to: " + port);
                }
                catch(Throwable t)
                {   
                  System.out.println("ERROR: port is not integer");
                }
             }
         } else if (command.compareTo("#start") == 0) {
             if (client.isListening()) {
                 System.out.println("ERROR: please close first.");
             } else {
                try {
                    client.listen();
                    System.out.println("Start success");
                }
                catch(IOException ex)
                {   
                  System.out.println("ERROR: start failed");
                  System.out.println(ex);
                }
             }
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
    int port = 0;  //The port number

    try 
    {   
      port = Integer.parseInt(args[0]); //Get port from command line
    }   
    catch(Throwable t)
    {   
      port = DEFAULT_PORT; //Set port to 5555
    }
 
    ServerConsole sc = new ServerConsole(port);
    sc.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
