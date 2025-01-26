package chatApplication;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.io.*;
class Server extends JFrame{
    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    // Declare Componenet
    private JLabel heading=new JLabel("Server Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    // Constructor
    Server(){
        try{
            server=new ServerSocket(7777);
            System.out.println("Server is ready to accept the Connection");
            System.out.println("Waiting....");
            socket=server.accept();

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out= new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            // startWriting();
        }
        catch(Exception e){
            // e.printStackTrace();
            System.out.println("Connection is Closed");
        }
    }

  private void handleEvents(){
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
                // System.out.println("Key released "+e.getKeyCode());
                if(e.getKeyCode()==10){
                    // System.out.println("You have pressed eneter button");
                    String contentToSend=messageInput.getText();
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
        this.setTitle("Server Side Messages[END]");
        this.setSize(500,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Coding for Component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // Set the layout of the frame
        this.setLayout(new BorderLayout());
        
        // adding the components to the frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea); //To add Scroll bar in the text area
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }


    public void startReading(){
      // 1st thread will read the data

    Runnable r1=()->{
        System.out.println("Reader Started...");
        try{
            while(!socket.isClosed()){
                String msg=br.readLine();
                if(msg.equals("Exit")){
                    // System.out.println("Client Terminated The Chat");
                    JOptionPane.showMessageDialog(this,"Client Terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                // System.out.println("Client :"+msg);
                messageArea.append("Client :"+msg+"\n");
            }
            // System.out.println("Connection is Closed");
        }
        catch(Exception e){
            // e.printStackTrace();
            System.out.println("Connection is Closed");
        }
    };
       new Thread(r1).start();
    }
    public void startWriting(){
    //    2nd thread will take the data from the user and send it to the client
    Runnable r2=()->{
        System.out.println("Writer Started...");
        try{
            while(!socket.isClosed()){
                BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                String content =br1.readLine();

                out.println(content);
                out.flush();

                if(content.equals("Exit")){
                    socket.close();
                    break;
                }
            }
            // System.out.println("Connection is Closed");
            
        }
        catch(Exception e){
            // e.printStackTrace();
            System.out.println("Connection is Closed");
        }
    };
      new Thread(r2).start();

    }
    public static void main(String args[]){
        System.out.println("This is Server ....going to Start Server");
        new Server();
    }
}
