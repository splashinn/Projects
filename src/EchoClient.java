// EchoClient.java
// (C) 2014 splashinn
// Connect the server on a known port and IP.
// Asynchronously send a message to the server.
// Calculate and display the round trip time for each message and the average round trip time for all messages sent.

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient {

    private String hostname;
    private int port;
    private Socket clientSocket;
    private BufferedReader inFromUser, inFromServer;
    private DataOutputStream outToServer;
    private double averageTime = 0;
    private int count = 0;

    public EchoClient(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
        try {
            this.clientSocket = new Socket(this.hostname, this.port);
        } catch (UnknownHostException e) {
            System.out.println("Connection Error: unknown host");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Connection Error: connection refused");
            System.exit(1);
        }
        try{
            this.inFromUser = new BufferedReader( new InputStreamReader(System.in));
            this.outToServer = new DataOutputStream(this.clientSocket.getOutputStream());
            this.inFromServer = new BufferedReader(
                    new InputStreamReader(this.clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error on Initializing echoclient");
            System.exit(1);
        }

    }

    public void start(){
        System.out.println("Connecting to " + hostname + " with port No " + port);
        String msgSend;
        try {
            while ((msgSend = inFromUser.readLine()) != null){
                // sendMessage asynchronous
                sendMessage(msgSend, new Callback(){
                    // callback function and calculate the average time
                    public void callback(long timeUsed, String msgReceived){
                        averageTime = (count * averageTime + (timeUsed)) / (count + 1);
                        ++count;
                        System.out.println(msgReceived +
                            " rtt=" +  (double)Math.round(timeUsed * 100)/100    + " ms" +
                            " artt=" + (double)Math.round(averageTime * 100)/100 + " ms");

                    }
                });
            }
        } catch (IOException e) {
            System.out.println("Error on reading message from user");
        }
    }

    private void sendMessage(String message, Callback cb){
        Thread sendMessageThread = new Thread(new SendMessageRequest(message, cb));
        sendMessageThread.start();
    }

    interface Callback {
        public void callback(long time, String msg);
    }

    class SendMessageRequest implements Runnable{

        private String message;
        private Callback cb;
        SendMessageRequest(String message, Callback cb){
            this.message = message;
            this.cb = cb;
        }
        @Override
        public void run() {
            String msgReceived;
            long timeStart, timeEnd, timeUsed;
            try {
                timeStart = System.nanoTime();
                outToServer.writeBytes(this.message + '\n');
                msgReceived = inFromServer.readLine();
                timeEnd = System.nanoTime();
                // Calculate the time and get the output
                timeUsed = (timeEnd - timeStart) / 1000000;
                cb.callback(timeUsed, msgReceived);
            } catch (IOException e) {
                System.out.println("Error on sending message to server");
            }

        }

    }

    public static void showUsage(){
        System.out.println("Usage: java EchoClient [hostname] [portNo]");
    }
    /**
     * Entry of the program
     */
            public static void main(String[] args) {
        String hostname = "";
        int port = 0;
        if (args.length < 2){
            showUsage();
            System.exit(0);
        }
        else{
            hostname = args[0];
            port = Integer.parseInt(args[1]);
        }

        EchoClient client = new EchoClient(hostname, port);
        client.start();
    }
}
