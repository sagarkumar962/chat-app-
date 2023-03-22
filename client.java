import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Font;

//import java.io.*;

public class client extends  JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;


    // gui
    private JLabel heading=new JLabel("client area");
    private JTextArea messageArea= new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto", Font.PLAIN,20);


     public client() {
         try {
             System.out.println("sending requesto server ");
             socket=new Socket("127.0.0.1",7777);
             System.out.println("connection done");
             br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

             out = new PrintWriter(socket.getOutputStream());


             createGUI();
             handleEvents();
             startReading();
//           startWriting();
         } catch (Exception e) {

         }
     }
         private void handleEvents() {
             messageInput.addKeyListener(new KeyListener() {
                 @Override
                 public void keyTyped(KeyEvent e) {

                 }

                 @Override
                 public void keyPressed(KeyEvent e) {

                 }

                 @Override
                 public void keyReleased(KeyEvent e) {
                     System.out.println("key released"+e.getKeyCode());
                     if(e.getKeyCode()==10){
                         String contentToSend=messageInput.getText();
                         messageArea.append(("Me:"+contentToSend+"\n"));
                         out.println(contentToSend);
                         out.flush();
                         messageInput.setText("");
                         messageInput.requestFocus();
                         //System.out.println("you have pressed enter");
                     }

                 }
             });
         }





         //reading

     private void createGUI(){
         this.setTitle("Client Messager[END]");
         this.setSize(600,600);
         this.setLocationRelativeTo(null);
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         heading.setFont(font);
         messageArea.setFont(font);
         messageInput.setFont(font);
         heading.setIcon(new ImageIcon("icon.png"));
         heading.setHorizontalTextPosition(SwingConstants.CENTER);
         heading.setVerticalTextPosition(SwingConstants.BOTTOM);

         heading.setHorizontalAlignment(SwingConstants.CENTER);
         heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
         messageInput.setHorizontalAlignment(SwingConstants.CENTER);
         this.setLayout(new BorderLayout());

         this.add(heading,BorderLayout.NORTH);
         JScrollPane jScrollPane=new JScrollPane(messageArea);
         this.add(messageArea,BorderLayout.CENTER);
         this.add(messageInput,BorderLayout.SOUTH);

         this.setVisible(true);

     }

     public void startReading() {
        Runnable r1 = () -> {
            System.out.println("reader started...");

            while (true) {
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("server terminated the chat");
                        JOptionPane.showMessageDialog(this,"server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                  //  System.out.println("sever:" + msg);
                    messageArea.append("Server:"+msg+"\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        new Thread(r1).start();


    }
//writing
    public void startWriting(){
        Runnable r2=()->{
            System.out.println("writer started");
            while(true)
            {
                try{
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content =br1.readLine();
                    out.println(content);
                    out.flush();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();


    }

    public static void main(String[] args) {
        new client();
    }
}
