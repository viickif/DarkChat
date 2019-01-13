package DarkChat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class DarkServer {
    private ServerSocket serverSocket;
    private HashMap<Integer, DarkChatRoom> rooms;
    private static final String CODE_PATTERN="^[0-9]+$";
    private static final String NAME_PATTERN = "^[a-zA-Z0-9]+$";
    private static final int MAX_ROOMS=30;

    public DarkServer(int port) throws IOException{
        serverSocket = new ServerSocket(port);
        rooms=new HashMap<>();
    }

    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            // create a new thread to handle that client
            Thread handler = new Thread(new Runnable() {
                public void run() {
                    try {
                        try {
                            handle(socket);
                        } finally {
                            socket.close();
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            });
            handler.start();
        }
    }

    private void handle(Socket socket) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()), true);

        DarkChatRoom chatRoom = initializeRoom(in,out);
        String username=initializeUser(chatRoom, in, out);

        message(chatRoom, username, in, out);

    }

    private void message(DarkChatRoom chatRoom,String myName, BufferedReader in, PrintWriter out){
        out.println("You have joined chat room "+ chatRoom.getCode()+".");
        out.println("================ D A R K   C H A T =====================================================");

        Set<String> usernames=chatRoom.getUserNames();

        try {
            for (String message = in.readLine(); message != null; message = in
                    .readLine()) {
                if(message.startsWith("psst/")){
                    String[] whisperStr=message.split("/");
                    String[] whisperUsers=whisperStr[1].split(" ");

                    for(String user : whisperUsers){
                        if(!user.isEmpty() && chatRoom.containsUser(user)){
                            chatRoom.getPrintWriter(user).println(myName + " **psst** : " + whisperStr[2].trim());
                        } else{
                            out.println("**"+user + " is not a valid user**");
                        }
                    }
                }
            else {
                    for (String user : usernames) {
                        if (!user.equals(myName)) {
                            chatRoom.getPrintWriter(user).println(myName + ": " + message);
                        }
                    }
                }
            }
            //user disconnects
        } catch(IOException ioe){
            chatRoom.removeUser(myName);
            if(chatRoom.isEmpty()){
                rooms.remove(Integer.valueOf(chatRoom.getCode()));
            }
            else {
                for (String user : usernames) {
                    System.out.println("sending to: " + user + " " + chatRoom.getPrintWriter(user));
                    chatRoom.getPrintWriter(user).println(myName + " has left the chat.");
                }
            }
        }
    }

    private String initializeUser(DarkChatRoom chatRoom, BufferedReader in, PrintWriter out){
        out.println("Enter your username.");

        try {
            for (String name = in.readLine(); name != null; name = in
                    .readLine()) {
                if (name.matches(NAME_PATTERN)) {
                    //username is already taken
                    if(chatRoom.containsUser(name)){
                        out.println(name+" is already taken. Try something else.");
                        //username is valid
                    } else {
                        chatRoom.addUser(name,out);
                        out.println("Joining as "+name+" ...");
                        return name;
                    }
                    //something is wrong with username
                } else{
                    out.println("Invalid username format.");
                    out.println("Username must only contain alphabetic letters and numbers");
                }
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
        return null;
    }

    private DarkChatRoom initializeRoom(BufferedReader in, PrintWriter out){
        int code=-1;
        boolean goodCode;

        out.println("Welcome to Dark Chat");
        out.println("Enter \"+\" to start a new chat room");
        out.println("Enter a chat room code to join an existing chat room");

        try {
            for (String line = in.readLine(); line != null; line = in
                    .readLine()) {

                //create new chat room
                if (line.equals("+")) {
                    code = getCode();
                    if (code!=-1) {
                        rooms.put(code, new DarkChatRoom(code));
                        out.println("A new chat room was successfully created.");
                        out.println("Chat room code: "+ Integer.toString(code));
                        break;
                    } else {
                        out.println("All rooms are currently full. Try again later.");
                    }
                    //join existing chat room
                } else if (line.matches(CODE_PATTERN)) {
                    code=Integer.valueOf(line);
                    goodCode=rooms.containsKey(code);
                    if(goodCode){
                        out.println("Selecting chat room "+code);
                        break;
                    } else{
                        out.println(line+" is an invalid chat room code. Try again.");
                    }
                    //invalid command
                } else {
                    out.println("Invalid command. Try again.");
                }
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
        return rooms.get(code);
    }

    private int getCode(){
        for(int i=0;i<MAX_ROOMS;i++){
            if (!rooms.containsKey(i)){
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        DarkServer server;
        try {
            server = new DarkServer(9000);
            System.out.println("DarkChat server is running...");
            server.serve();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}



