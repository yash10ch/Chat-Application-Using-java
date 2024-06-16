import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;


public class Client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //Declare Components for GUI using Swing
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Robot",Font.PLAIN,20);
    

    //Constructor
    public Client(){
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("connection done..");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

             createGUI();
             handleEvent();
             startReading();
            // startWriting();

            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

     private void handleEvent() {
       
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //Enter button code is 10
                if(e.getKeyCode()==10){
                    // System.out.println("you have pressed enter button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
            
        });
    }

    private void createGUI(){
        //GUI code..
        //this = window now
        this.setTitle("Client Messager[End]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);           //window center
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       //cross button close program
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        
        heading.setIcon(new ImageIcon("msg1.png"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);

        //messageInput
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //frame ka layout set karenge
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this .add(messageInput,BorderLayout.SOUTH);





        this.setVisible(true);          //window visible
     }

    
    //Start Reading [Method1]
    public void startReading(){
        //thread-read karke deta rahega

        Runnable r1=()->{

            System.out.println("reader started..");

            try {
                while(true){
               
                    String msg= br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
    
                    //System.out.println("Server : "+msg);
                    messageArea.append("Server : "+msg+"\n");
              
               
            }
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("connection closed");
            }

           
            
        };
        new Thread(r1).start();

     }
     
     //Start Writing send [Method2]
     public void startWriting(){
         //thread - data user lega and the send karga client tak
         Runnable r2=()->{
            System.out.println("Writer Started..");
            try {
                while( !socket.isClosed()){
           
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
      
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
      
                 
                }
               
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Connection is closed");
            }
           
         };
         new Thread(r2).start();
     }

    
    public static void main(String[] args) {
        System.out.println("this is client...");
        new Client();
    }

}
