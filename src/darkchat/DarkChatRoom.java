package darkchat;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;

public class DarkChatRoom {
    private int code;
    private HashMap<String, PrintWriter> users;

    public DarkChatRoom(int code) {
        this.code = code;
        users = new HashMap<>();
    }

    public void addUser(String username, PrintWriter writer) {
        users.put(username, writer);
    }

    public void removeUser(String username){
        users.remove(username);
    }

    public Set<String> getUserNames(){
        return users.keySet();
    }

    public PrintWriter getPrintWriter(String username){
        return users.get(username);
    }

    public boolean containsUser(String username){
        return users.containsKey(username);
    }

    public boolean isEmpty(){
        return users.size() == 0;
    }

    public String getCode(){
        return Integer.toString(code);
    }
}
