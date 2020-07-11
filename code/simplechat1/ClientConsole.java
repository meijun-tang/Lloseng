// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import javax.imageio.ImageIO;
import client.*;
import common.*;
import java.util.List;
import java.util.Base64;
import java.lang.Runnable;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.awt.Color;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;

import javax.swing.TransferHandler;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
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
public class ClientConsole extends JFrame implements ChatIF, ActionListener
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  private static DataFlavor URI_LIST_FLAVOR = null;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  String id;
  private JFrame MainFrame;
  private JTextArea DisplayTextArea;
  private JTextArea InputTextArea;
  private JPanel DisplayPanel;
  private JScrollPane DisplayScrollPane;
  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String id, String host, int port) 
  {
    this.id = id;
    MainWindow();
    try 
    {
      client= new ChatClient(id, host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }

  public void ShowAbout()
  {

  }
  
  public void ShowLicense()
  {

  }

  public void actionPerformed(ActionEvent e) 
  { 
    String s = e.getActionCommand(); 
    if (s.equals("About")) 
    {
        String intro = "SimpleChat V0.1\nThis a extension of SimpleChat from assignment 1\n" +
            "* It supports simple text chat;\n" +
            "* It supports all the functioins of assignment 1 like #getport, #setport, #login, etc;\n" +
            "* It has a clean UI;\n" + 
            "* It could exchange diagrams.\n";
        JOptionPane.showMessageDialog(MainFrame, intro, "About", JOptionPane.PLAIN_MESSAGE); 
    } 
    else if ( s.equals("License") ) 
    {
		String license = "";
		try 
		{
			license = new String ( Files.readAllBytes( Paths.get("./LICENSE") ) );
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
        JOptionPane.showMessageDialog(MainFrame, license, "LICENSE", JOptionPane.PLAIN_MESSAGE); 
        
    } 
    else if ( s.equals("Exit") ) 
    {
        System.exit(0);
    }
  } 
  
  public JMenuBar CreateMenuBar()
  {
      JMenuBar mb = new JMenuBar(); 

      JMenu x = new JMenu("SimpleChat");
      JMenuItem m1, m2, m3;

      m1 = new JMenuItem("About"); 
      m1.addActionListener(this);

      m2 = new JMenuItem("License"); 
      m2.addActionListener(this);
      m3 = new JMenuItem("Exit"); 
      m3.addActionListener(this);

      x.add(m1); 
      x.add(m2); 
      x.add(m3);
      mb.add(x);
      
      return mb;
  }

  //Instance methods
  public void MainWindow() 
  {
        JFrame MainFrame=new JFrame("SimpleChat " + this.id);
        JPanel jp=new JPanel();
        String intro = "\n\n\nThis a extension of SimpleChat from assignment 1\n" +
            "* It supports simple text chat;\n" +
            "* It supports all the functioins of assignment 1 like #getport, #setport, #login, etc;\n" +
            "* It has a clean UI;\n" + 
            "* It could exchange diagrams.\n";

        DisplayTextArea=new JTextArea(intro,20,50);
        DisplayTextArea.setLineWrap(true);
        DisplayTextArea.setWrapStyleWord(true);
        DisplayTextArea.setAlignmentX(JComponent.LEFT_ALIGNMENT);

        //JScrollPane jsp=new JScrollPane(DisplayTextArea);
        DisplayPanel=new JPanel();
        //BoxLayout bl = new BoxLayout(DisplayPanel, BoxLayout.Y_AXIS);
        BoxLayout bl = new BoxLayout(DisplayPanel, BoxLayout.PAGE_AXIS);
        DisplayPanel.setLayout(bl);
        DisplayPanel.setBackground(new Color(255, 255, 255));
        DisplayPanel.setForeground(new Color(255, 255, 255));
        DisplayPanel.add(DisplayTextArea);
        Dimension size=DisplayTextArea.getPreferredSize();
        DisplayScrollPane =new JScrollPane(DisplayPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        DisplayScrollPane.setAutoscrolls(true);

        DisplayScrollPane.setBounds(10,10, size.width, size.height);

        jp.add(DisplayScrollPane);  

        InputTextArea=new JTextArea("",10,50);
        InputTextArea.setLineWrap(true);
        InputTextArea.setWrapStyleWord(true);
        JScrollPane jsp2=new JScrollPane(InputTextArea);
        Dimension size2=InputTextArea.getPreferredSize();
        jsp2.setBounds(10,10,size2.width,size2.height);
        jp.add(jsp2);
        InputTextArea.addKeyListener(new KeyListener() {
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    	String value = InputTextArea.getText();
                InputTextArea.setText("");
		    	accept(value.strip());
		    }
		}
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
	    });
		InputTextArea.setTransferHandler( createTransferHandler() );

        MainFrame.add(jp);
        MainFrame.setJMenuBar(CreateMenuBar());
        MainFrame.setSize(size.width+20, size2.height+size.height+80); 
        MainFrame.setVisible(true);
  }

  //private static TransferHandler createTransferHandler(){
  private TransferHandler createTransferHandler()
  {
    return new TransferHandler(){
      @Override
      public boolean importData( JComponent comp, Transferable aTransferable ) {
        try {
            if ( aTransferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor ) ) {
              System.out.println("File list flavor");
              List<File> file_list = ( List<File> ) aTransferable.getTransferData( DataFlavor.javaFileListFlavor );
              System.out.println( "file_list = " + file_list );
			  //InputTextArea.append("File:"+file_list.get(0));
			  accept("#sendfile "+file_list.get(0));
            }
            if ( URI_LIST_FLAVOR != null && aTransferable.isDataFlavorSupported( URI_LIST_FLAVOR ) ){
            System.out.println("URI list flavor");
            String uri_list = ( String ) aTransferable.getTransferData( URI_LIST_FLAVOR );
            System.out.println( "uri_list = " + uri_list );
          }
        } catch ( UnsupportedFlavorException e ) {
          throw new RuntimeException( e );
        } catch ( IOException e ) {
          throw new RuntimeException( e );
        }
        return true;
      }

      @Override
      public boolean canImport( JComponent comp, DataFlavor[] transferFlavors ) {
        return true;
      }
    };
 }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept(String message) 
  {
    try
    {
      //BufferedReader fromConsole = 
      //  new BufferedReader(new InputStreamReader(System.in));
      //String message; 
      String command;

      //while (true) 
        //message = fromConsole.readLine();
        command = message.trim();
        /**
         * May be a command
         */
         if(command.compareTo("#quit") == 0) {
             if (client.isConnected()) {
                client.closeConnection();
             }
             //break;
         } else if (command.compareTo("#logoff") == 0) {
             client.closeConnection();
         } else if (command.startsWith("#sendfile")) {
             if (!client.isConnected()) {
                 System.out.println("ERROR: please login first.");
             } else {
                try {
                    String filepath = command.substring(10);
                    File file = new File(filepath);
                    FileInputStream fis = new FileInputStream(file);
                    int length = (int)file.length();
                    byte [] buffer = new byte [length];
                    fis.read(buffer);
                    fis.close();
                    String base64encodedString = Base64.getEncoder().encodeToString(buffer);
                    System.out.println("sending file : " + file); 
                    String ext = filepath.substring(filepath.lastIndexOf("/") + 1);
                    client.handleMessageFromClientUI("#base64file:" + ext + ":" + base64encodedString);
                }
                catch(Throwable t)
                {   
                  System.out.println("ERROR: host argument error");
                }
             }
         } else if (command.startsWith("#sethost")) {
             if (client.isConnected()) {
                 System.out.println("ERROR: please logged off first.");
             } else {
                try {
                    String host = command.substring(9);
                    client.setHost(host.trim());
                    System.out.println("Host set to: " + host);
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
         } else if (command.startsWith("#login")) {
             if (client.isConnected()) {
                 System.out.println("ERROR: please logged off first.");
             } else {
                try {
                    client.openConnection();
                    client.handleMessageFromClientUI(message);
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
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }
  private void ScrollToBottom()
  {
      SwingUtilities.invokeLater(
          new Runnable()
          {
              public void run()
              {
                  DisplayScrollPane.getVerticalScrollBar().setValue(DisplayScrollPane.getVerticalScrollBar().getMaximum());
                  DisplayScrollPane.getHorizontalScrollBar().setValue(0);
              }
          });
  }

  public void displayString(String message){
      JTextArea jta=new JTextArea(message,1,50);
      jta.setLineWrap(true);
      jta.setWrapStyleWord(true);
      jta.setAlignmentX(JComponent.LEFT_ALIGNMENT);
      DisplayPanel.add(jta);
      Dimension CSize = DisplayPanel.getSize();
      Dimension JTASize = jta.getSize();
      DisplayPanel.setSize(CSize.width, CSize.height+50);
	  ScrollToBottom();
  }
  
  public void displayImage(String imagepath){
    try {
        Image image = ImageIO.read(new File(imagepath));
        ImageIcon imageicon=new ImageIcon(image);
        JLabel label=new JLabel(imageicon);
        label.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        label.setBorder(new LineBorder(Color.BLACK));
        DisplayPanel.add(label);
        Dimension CSize = DisplayPanel.getSize();
        Dimension LSize = label.getSize();
        DisplayPanel.setSize(CSize.width, LSize.height + CSize.height+50);
	    ScrollToBottom();
    } catch (Exception ex) {
        ex.printStackTrace();
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
      if(DisplayPanel != null) {
          if(message.contains("#base64file:")){
              String [] contents = message.split(":");
              System.out.println(contents[0]);
              System.out.println(contents[1]);
              byte [] content = Base64.getDecoder().decode(contents[2]);
              OutputStream out = null;
              try{
                    out = new FileOutputStream("./Files/" + contents[1]);
                    out.write(content);
                    out.close();
              }catch( Exception e  ) {
                e.printStackTrace();
              }
              String ext = contents[1].substring(contents[1].lastIndexOf(".")+1);
              System.out.println("ext=" + ext);
              String formarts = "png,jpeg,jpg,bmp";
              if( formarts.contains(ext) ) {
                  displayString(contents[0].substring(0, contents[0].indexOf(">")+1));
                  displayImage("./Files/" + contents[1]);
              }else{
                    displayString("File saved in ./Files/" + contents[1]);
              }
          }else{
              displayString(message);
      }
    } else System.out.println(message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = ""; String id = "";
    int port = 0;  //The port number

    try
    {
        id = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      System.out.println("ERROR - No login ID specified.  Connection aborted.");
      return;
    }


    try
    {
      host = args[1];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }

    try 
    {   
      port = Integer.parseInt(args[2]); //Get port from command line
    }   
    catch(Throwable t)
    {   
      port = DEFAULT_PORT; //Set port to 5555
    }
 
    ClientConsole chat= new ClientConsole(id, host, port);
    //chat.accept();  //Wait for console data
    //chat.MainWindow();
  }
}
//End of ConsoleChat class
