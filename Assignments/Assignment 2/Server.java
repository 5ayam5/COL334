import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public class Server {
    private static class ClientThread extends Thread {
        private static final int MAX_HEADER_LINE = 116;
        private Socket socket;
        private String username;
        private OutputStream send;
        private InputStream recv;

        public ClientThread(Socket socket) throws IOException {
            this.socket = socket;
            send = socket.getOutputStream();
            recv = socket.getInputStream();
        }

        private static boolean registeredUser(String username) {
            return recvSockets.containsKey(username) && sendUsers.contains(username);
        }

        // @TODO implement this as thread (to make it consistent with broadcasting)
        private static void sendData(Socket socket, ArrayList<String> headerList, String message) throws IOException {
            socket.getOutputStream().write((String.join("\n", headerList) + "\n\n" + message).getBytes());
            socket.getOutputStream().flush();
            // @TODO receive response
        }

        private void sendError(int errno) throws IOException {
            switch (errno) {
                case 100:
                    break;
                case 101:
                    break;
                case 102:
                    break;
                case 103:
                    throw new IOException();
                default:
                    break;
            }
        }

        private String readLine() throws IOException {
            StringBuilder ret = new StringBuilder();
            char next = (char)recv.read();
            while (next != '\n' && ret.length() < MAX_HEADER_LINE) {
                ret.append(next);
                next = (char)recv.read();
            }
            if (next != '\n')
                throw new IOException();
            return ret.toString();
        }

        private ArrayList<String> getHeader() throws IOException {
            ArrayList<String> ret = new ArrayList<>();
            String next = readLine();
            while (!next.equals("")) {
                ret.add(next);
                next = readLine();
            }
            return ret;
        }

        private void connectClient(String[] header) throws IOException {
            if (!header[0].equals("REGISTER")) {
                sendError(103);
                return;
            }
            username = header[2];
            if (header[1].equals("TOSEND")) {
                if (sendUsers.contains(username) || header.length != 3) {
                    sendError(100);
                    return;
                }
                sendUsers.add(username);
                System.err.println(username + " connected for send");
            } else if (header[1].equals("TORECV")) {
                if (recvSockets.containsKey(username) || header.length != 3) {
                    sendError(100);
                    return;
                }
                recvSockets.put(username, socket);
                System.err.println(username + " connected for recv");
                throw new IOException();
            } else
                sendError(103);
        }

        private void recvMessage(ArrayList<String> headerList) throws IOException {
            if (!registeredUser(username)) {
                sendError(101);
                return;
            }
        }

        @Override
        public void run() {
            try {
                ArrayList<String> headerList = getHeader();
                if (headerList.size() == 1)
                    connectClient(headerList.get(0).split(" "));
                else if (headerList.size() == 2)
                    recvMessage(headerList);
                else
                    throw new IOException();
            } catch (IOException e) {
                return;
            }
        }
    }

    private static int port = 2428; // stands for "chat"
    private static ServerSocket serverSocket = null;
    private static HashMap<String, Socket> recvSockets = new HashMap<>(); // map for TORECV sockets
    private static HashSet<String> sendUsers = new HashSet<>(); // set of users added via TOSEND

    // listen for new clients to join and create threads for handling them
    public static void connectionLoop() {
        while (true) {
            try {
                Socket client = serverSocket.accept(); // listen and accept
                ClientThread clientThread = new ClientThread(client);
                clientThread.start();
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static void main(String args[]) {
        if (args.length > 0)
            try {
                port = Integer.parseInt(args[0]);
                if (port < 0 || port > 65535)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                port = 2428;
                System.err.println("Invalid argument for port, using default port number " + port);
            }

        try { 
            serverSocket = new ServerSocket(port); // creates and binds the server socket
            System.err.println("Server running on port " + port);
        } catch (IOException e) {
            System.err.println("Port " + port + " unavailable, using a random port number");
            main(new String[]{"0"});
            return;
        }

        connectionLoop();
    }
}
