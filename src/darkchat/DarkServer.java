package darkchat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class DarkServer {
    private ServerSocket serverSocket;
    private HashMap<Integer, DarkChatRoom> rooms;
    private static final String CODE_PATTERN = "^[0-9]+$";
    private static final String NAME_PATTERN = "^[a-zA-Z0-9]+$";
    private static final int MAX_ROOMS = 30;


    /**
     * Creates a new DarkServer. DarkServer is a server that
     * allows clients to connect to it and execute group chat room
     * features
     * @param port where server listens for connections
     * @throws IOException if I/O errors occur while opening socket
     */
    public DarkServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        rooms = new HashMap<>();
    }


    /**
     * Listens for client connection and handles the client
     * when they connect
     * @throws IOException if I/O error occurs while waiting for
     * connection
     */
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


    /**
     * Handles the client by initiating a chat room for the client
     * and executing messaging features
     * @param socket where client is connected
     * @throws IOException if connection encounters an error
     */
    private void handle(Socket socket) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()), true);

        DarkChatRoom chatRoom = initializeRoom(in, out);
        String username = initializeUser(chatRoom, in, out);

        message(chatRoom, username, in, out);

    }

    /**
     * Executes group messaging function
     * @param chatRoom that group messaging is occuring in
     * @param myName user name of the client
     * @param in  input stream of messages from client
     * @param out to send output stream of messages
     */
    private void message(DarkChatRoom chatRoom, String myName, BufferedReader in, PrintWriter out) {
        out.println("You have joined chat room " + chatRoom.getCode() + ".");
        out.println("================ D A R K   C H A T ============================"
                + "=========================");

        Set<String> usernames = chatRoom.getUserNames();

        try {
            for (String message = in.readLine(); message != null; message = in
                    .readLine()) {
                if (message.startsWith("psst/")) {
                    String[] whisperStr = message.split("/");
                    String[] whisperUsers = whisperStr[1].split(" ");

                    for (String user : whisperUsers) {
                        if (!user.isEmpty() && chatRoom.containsUser(user)) {
                            chatRoom.getPrintWriter(user).println(myName
                                    + " **psst** : " + whisperStr[2].trim());
                        } else {
                            out.println("**" + user + " is not a valid user**");
                        }
                    }
                } else {
                    for (String user : usernames) {
                        if (!user.equals(myName)) {
                            chatRoom.getPrintWriter(user).println(myName + ": " + message);
                        }
                    }
                }
            }
            //user disconnects
        } catch (IOException ioe) {
           disconnectUser(chatRoom, myName);
        } finally {
            out.close();
        }
    }

    /**
     * Disconnects the client from the chat room and sends a message notifying
     * the remaining users in the chat room that the client has disconnected.
     * If the chat room has no remaining users in it, the chat room is deleted.
     * @param chatRoom to disconnect the client from
     * @param myName user name of the client that
     */
    private void disconnectUser(DarkChatRoom chatRoom, String myName) {
        chatRoom.removeUser(myName);
        if (chatRoom.isEmpty()) {
            rooms.remove(Integer.valueOf(chatRoom.getCode()));
        } else {
            for (String user : chatRoom.getUserNames()) {
                chatRoom.getPrintWriter(user).println(myName + " has left the chat.");
            }
        }
    }

    /**
     * Allows the client to set up their username within the
     * chat room
     * @param chatRoom to join
     * @param in input stream from client
     * @param out to send output stream of replies from the server
     * @return user name of the client
     */
    private String initializeUser(DarkChatRoom chatRoom, BufferedReader in, PrintWriter out) {
        out.println("Enter your username.");

        try {
            for (String name = in.readLine(); name != null; name = in
                    .readLine()) {
                if (name.matches(NAME_PATTERN)) {
                    //username is already taken
                    if (chatRoom.containsUser(name)) {
                        out.println(name + " is already taken. Try something else.");
                        //username is valid
                    } else {
                        chatRoom.addUser(name, out);
                        out.println("Joining as " + name + " ...");
                        return name;
                    }
                    //something is wrong with username
                } else {
                    out.println("Invalid username format.");
                    out.println("Username must only contain alphabetic letters and numbers");
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    /**
     * Allows the client to join an existing chat room by providing an
     * access code or initialize a new chat room
     * @param in input stream from client
     * @param out to send output stream of replies from server
     * @return the existing or new chat room the client joined
     */
    private DarkChatRoom initializeRoom(BufferedReader in, PrintWriter out) {
        int code = -1;
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
                    if (code != -1) {
                        rooms.put(code, new DarkChatRoom(code));
                        out.println("A new chat room was successfully created.");
                        out.println("Chat room code: " + Integer.toString(code));
                        break;
                    } else {
                        out.println("All rooms are currently full. Try again later.");
                    }
                    //join existing chat room
                } else if (line.matches(CODE_PATTERN)) {
                    code = Integer.valueOf(line);
                    goodCode = rooms.containsKey(code);
                    if (goodCode) {
                        out.println("Selecting chat room " + code);
                        break;
                    } else {
                        out.println(line + " is an invalid chat room code. Try again.");
                    }
                    //invalid command
                } else {
                    out.println("Invalid command. Try again.");
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return rooms.get(code);
    }

    /**
     * Generates an access code from 0-29 for a new chat room
     * @return access code of the new chat room
     */
    private int getCode() {
        for (int i = 0; i < MAX_ROOMS; i++) {
            if (!rooms.containsKey(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Starts a DarkServer at port 9000
     */
    public static void main(String[] args) {
        DarkServer server;
        try {
            server = new DarkServer(9000);
            System.out.println("darkchat server is running...");
            server.serve();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}



