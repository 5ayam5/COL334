import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class Server {
    private static class ClientThread extends Thread {
        private static final int MAX_HEADER_LINE = 116, MAX_TRIES = 2;
        private Socket socket;
        protected String username;
        protected OutputStream send;
        private InputStream recv;

        public ClientThread(Socket socket) throws IOException {
            this.socket = socket;
            send = socket.getOutputStream();
            recv = socket.getInputStream();
        }

        private static boolean registeredUser(String username) {
            return username.equals("ALL") || (recvSockets.containsKey(username) && sendUsers.contains(username));
        }

        private static void removeUser(String username) {
            sendUsers.remove(username);
            recvSockets.remove(username);
        }

        private void sendResponse(String header) throws IOException {
            for (int i = 0; i < MAX_TRIES; ++i) {
                try {
                    send.write((header + "\n\n").getBytes());
                    send.flush();
                    return;
                } catch(IOException e) {
                    continue;
                }
            }
            throw new IOException();
        }

        private void sendResponse(int errno) throws IOException, InterruptedException {
            sendResponse(errno, "");
        }

        private void sendResponse(int errno, String msg) throws IOException, InterruptedException {
            switch (errno) {
                case 100:
                    sendResponse("ERROR 100 Malformed username");
                    break;
                case 101:
                    sendResponse("ERROR 101 No user registered");
                    break;
                case 102:
                    sendResponse("ERROR 102 Unable to send");
                    break;
                case 103:
                    sendResponse("ERROR 103 Header incomplete");
                    throw new IOException();
                case 200:
                    sendResponse("REGISTERED TOSEND " + msg);
                    System.err.println(username + " connected for send");
                    break;
                case 201:
                    sendResponse("REGISTERED TORECV " + msg);
                    System.err.println(username + " connected for recv");
                    throw new InterruptedException();
                case 202:
                    sendResponse("SENT " + msg);
                    break;
                default:
                    break;
            }
        }

        private String readLine() throws IOException {
            StringBuilder ret = new StringBuilder();
            char next = (char)recv.read();
            if (next == 65535)
                throw new IOException();
            while (next != '\n' && ret.length() < MAX_HEADER_LINE) {
                ret.append(next);
                next = (char)recv.read();
            }
            if (next != '\n')
                throw new IOException();
            return ret.toString();
        }

        protected List<String> getHeader() throws IOException {
            List<String> ret = new ArrayList<>();
            String next = readLine();
            while (!next.equals("")) {
                ret.add(next);
                next = readLine();
            }
            return ret;
        }

        private void connectClient() throws IOException, InterruptedException {
            List<String> headerList = getHeader();
            if (headerList.size() != 1)
                sendResponse(103);
            String[] header = headerList.get(0).split(" ");
            if (header.length < 3 || !header[0].equals("REGISTER"))
                sendResponse(103);

            username = header[2];
            if (header[1].equals("TOSEND")) {
                if (sendUsers.contains(username) || header.length != 3) {
                    sendResponse(100);
                    return;
                }
                sendUsers.add(username);
                sendResponse(200, username);
            } else if (header[1].equals("TORECV")) {
                if (recvSockets.containsKey(username) || header.length != 3) {
                    sendResponse(100);
                    return;
                }
                recvSockets.put(username, socket);
                sendResponse(201, username);
            } else
                sendResponse(103);
        }

        private boolean forward(List<String> targets, List<String> headerList, String message) {
            HashMap<String, SendThread> threads = new HashMap<>();
            for (String target : targets)
                try {
                    SendThread thread = new SendThread(recvSockets.get(target), headerList, message);
                    thread.start();
                    threads.put(target, thread);
                } catch (IOException e) {
                    removeUser(target);
                }
            boolean success = true;
            for (Map.Entry<String, SendThread> thread : threads.entrySet())
                try {
                    thread.getValue().join();
                } catch (InterruptedException e) {
                    removeUser(thread.getKey());
                    success = false;
                }
            return success;
        }
        
        private List<String> generateTargets(String target) {
            List<String> targets = new ArrayList<>();
            if (target.equals("ALL")) {
                for (String user : sendUsers)
                    if (registeredUser(user))
                        targets.add(user);
            } else
                targets.add(target);
            return targets;
        }

        private String readMessage(int len) throws IOException {
            byte[] message = new byte[len];
            int curr = 0;
            while (curr < len) {
                int read = recv.read(message, curr, len - curr);
                if (read != -1)
                    curr += read;
            }
            return new String(message);
        }

        private void recvAndFwdMsg() throws IOException, InterruptedException {
            List<String> headerList = getHeader();
            if (!registeredUser(username)) {
                sendResponse(101);
                return;
            }
            if (headerList.size() != 2)
                sendResponse(103);

            String[] line = headerList.get(0).split(" ");
            if (line.length != 2 || !line[0].equals("SEND"))
                sendResponse(103);

            String target = line[1];
            line = headerList.get(1).split(" ");
            if (line.length != 2 || !line[0].equals("Content-length:"))
                sendResponse(103);
            if (!registeredUser(target)) {
                try {
                    readMessage(Integer.parseInt(line[1]));
                    sendResponse(102);
                    return;
                } catch (NumberFormatException e) {
                    sendResponse(103);
                    throw new IOException();
                }
            }
            List<String> targets = generateTargets(target);

            try {
                headerList.set(0, String.format("FORWARD %s", username));
                if (forward(targets, headerList, readMessage(Integer.parseInt(line[1]))))
                    sendResponse(202, target);
                else
                    sendResponse(102);
            } catch(NumberFormatException e) {
                sendResponse(103);
                throw new IOException();
            }
        }

        @Override
        public void run() {
            try {
                connectClient();
                while (true)
                    recvAndFwdMsg();
            } catch (IOException e) {
                removeUser(username);
                try {
                    socket.close();
                } catch(IOException e1) {
                    return;
                }
            } catch (InterruptedException ie) {
                return;
            }
        }
    }

    private static class SendThread extends ClientThread {
        private List<String> headerList;
        private String message;

        public SendThread(Socket socket, List<String> headerList, String message) throws IOException {
            super(socket);
            this.headerList = headerList;
            this.message = message;
        }

        private int parseResponse(List<String> headerList, String check) {
            if (headerList.size() != 1)
                return 103;

            String[] header = headerList.get(0).split(" ");
            if (header.length != 2)
                return 103;
            switch (header[0]) {
                case "RECEIVED":
                    return header[1].equals(check) ? 203 : 103;
                default:
                    return 103;
            }
        }

        private int sendData(List<String> headerList, String message) throws IOException {
            send.write((String.join("\n", headerList) + "\n\n" + message).getBytes());
            send.flush();
            headerList = getHeader();
            return parseResponse(headerList, username);
        }

        @Override
        public void run() {
            try {
                switch (sendData(headerList, message)) {
                    case 203:
                        return;
                    default:
                        throw new IOException();
                }
            } catch(IOException e) {
                this.interrupt();
            }
        }
    }

    private static int port = 2428; // stands for "chat"
    private static ServerSocket serverSocket = null;
    private static volatile HashMap<String, Socket> recvSockets = new HashMap<>(); // map for TORECV sockets
    private static volatile HashSet<String> sendUsers = new HashSet<>(); // set of users added via TOSEND

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

    public static void main(String[] args) {
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
