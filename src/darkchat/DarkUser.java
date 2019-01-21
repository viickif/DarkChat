package darkchat;

import java.io.PrintWriter;

public class DarkUser {
    private final String userName;
    private final PrintWriter out;

    public DarkUser(String userName, PrintWriter out){
        this.userName=userName;
        this.out=out;
    }

    public String getUserName(){
        return userName;
    }

    public PrintWriter getPrintWriter(){
        return out;
    }
}
