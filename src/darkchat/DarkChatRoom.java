package darkchat;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;

public class DarkChatRoom {
    private int code;
    private HashMap<String, PrintWriter> users;

    /**
     * Creates a new DarkChatRoom
     * @param code associated with the chat room
     */
    public DarkChatRoom(int code) {
        this.code = code;
        users = new HashMap<>();
    }


    /**
     * Adds a user to the chat room
     * @param username of the user to add to the chat room
     * @param writer to send output stream to
     */
    public void addUser(String username, PrintWriter writer) {
        users.put(username, writer);
    }


    /**
     * Removes a user from the chat room
     * @param username of the user to remove form the chat room
     */
    public void removeUser(String username){
        users.remove(username);
    }


    /**
     * Gets a set of all the user names of all users in
     * the chat room
     * @return Set of all the user names of all users in
     * the chat room
     */
    public Set<String> getUserNames(){
        return users.keySet();
    }


    /**
     * Gets the PrintWriter associated with the users who has
     * the specified user name
     * @param username to retrieve PrintWriter of
     * @return the PrintWriter associated with the user
     */
    public PrintWriter getPrintWriter(String username){
        return users.get(username);
    }


    /**
     * Checks is any user with the specified user name exists
     * in the chat room
     * @param username of the user to check
     * @return true if a user with the specified user name exists
     * in the chat room and false otherwise
     */
    public boolean containsUser(String username){
        return users.containsKey(username);
    }


    /**
     * Checks if the chat room is empty
     * @return true if the chat room has 0 users in it and
     * false otherwise
     */
    public boolean isEmpty(){
        return users.size() == 0;
    }


    /**
     * Gets the code associated with the chat room
     * @return the code associated with the chat room
     */
    public String getCode(){
        return Integer.toString(code);
    }
}
