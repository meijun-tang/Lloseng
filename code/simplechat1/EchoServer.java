// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  private boolean serverClosed = true;
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    String message = msg.toString();
    if (message.startsWith("#login")) {
        try
        {
            if (client.getInfo("id") != null) {
                client.sendToClient("ERROR: you are already logined.");
            } else {
                String id = message.substring(6);
                client.setInfo("id", id.trim());
                client.sendToClient("Login success.");
            }
        }
        catch (Exception ex) 
        {
            try
            {
                client.sendToClient("Login error.");
            }
            catch (Exception e) {
                System.out.println("SendToClient error");
            }
        }
    } else {
        System.out.println("Message received: " + message + " from " + client);
        if (client.getInfo("id") != null) {
            this.sendToAllClients(client.getInfo("id") + ": " + message);
        } else {
            try 
            {
                client.sendToClient("Please login first.");
                client.close();
            }
            catch (Exception e) {
                System.out.println("SendToClient error");
            }
        }
    }
  }

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClientUI
    (Object msg)
  {
    System.out.println("Message received: " + msg + " from Console");
    this.sendToAllClients("SERVER MSG>" + msg);
  }
    
  public boolean isClosed() {
	return serverClosed;
  }  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
	serverClosed = false;
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  /** 
   * Hook method called when the server is clased.
   * The default implementation does nothing. This method may be
   * overriden by subclasses. When the server is closed while still
   * listening, serverStopped() will also be called.
   */
  protected void serverClosed() {
	serverClosed = true;;
    System.out.println
      ("Server has closed all connections.");
  }

  /** 
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) {
    System.out.println
      ("A new client connected.");
  }

  /** 
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized.
   *
   * @param client the client that raised the exception.
   * @param Throwable the exception thrown.
   */
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
    System.out.println
      ("Client " + client.getInfo("id") + " disconnected.");
  }

  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
