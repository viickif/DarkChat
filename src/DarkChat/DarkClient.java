package DarkChat;

import java.io.*;
import java.net.Socket;

public class DarkClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    //Rep Invariant: socket, in, out !=null

    /**
     * Make a DB221StudentClient and connect it to a server running on
     * hostname at the specified port.
     *
     * @param hostname, name of host
     * @param port      to connect to
     * @throws IOException if can't connect
     */
    public DarkClient(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

    }

    /**
     * Send a request to the server. Requires this is "open".
     *
     * @param myMessage to execute in database
     * @throws IOException if network or server failure
     */
    public void sendRequest(String myMessage) throws IOException {
        out.print(myMessage + "\n");
        out.flush(); // important! make sure x actually gets sent
    }


    /**
     * Get a reply from the next request that was submitted.
     * Requires this is "open".
     *
     * @return reply from server
     * @throws IOException if network or server failure
     */
    public String getReply() throws IOException {
        if (in.ready()) {
            return in.readLine();
        } else {
            return null;
        }
    }

    /**
     * Closes the client's connection to the server.
     * This client is now "closed". Requires this is "open".
     *
     * @throws IOException if close fails
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) {
        DarkClient client = null;

        try {
            client = new DarkClient("localhost", 9000);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        do {
            try {
                //if the user has a message or command to send, listen
                if (br.ready()) {
                    String command = br.readLine();
                    client.sendRequest(command);
                }

                // otherwise, listen for messages from others
                String reply = client.getReply();
                if (reply != null) {
                    System.out.println(reply);
                }

            } catch (IOException ioe) {
                System.err.println("Something went wrong. Try again later.");
            }
        } while (true);

    }
}


