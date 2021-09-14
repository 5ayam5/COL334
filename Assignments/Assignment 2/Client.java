import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;

public class Client {
    private static class SockThread extends Thread {
        protected Socket socket;
        protected OutputStream send;
        protected InputStream recv;

        public SockThread() throws IOException {
           socket = new Socket(address, port);
           send = socket.getOutputStream();
           recv = socket.getInputStream();
        }

        protected void sendData(List<String> headerList, String message) throws IOException {
            send.write((String.join("\n", headerList) + "\n\n" + message).getBytes());
            send.flush();
            // @TODO receive response
        }

        protected void registerClient(String code) throws IOException {
            while (true) {
                try {
                    sendData(Arrays.asList(String.join(" ", "REGISTER", code, username)), "");
                    break;
                } catch(IOException e) {
                    continue;
                }
            }
        }

        @Override
        public void run() {
            throw new UnsupportedOperationException();
        }
    }

    private static class SendThread extends SockThread {
        public SendThread() throws IOException {
            // empty constructor to handle throwing of exception
        }

        @Override
        public void run() {
            try {
                registerClient("TOSEND");
            } catch (IOException e) {
                return;
            }
        }   
    }

    private static class RecvThread extends SockThread {
        public RecvThread() throws IOException {
            // empty constructor to handle throwing of exception
        }

        @Override
        public void run() {
            try {
                registerClient("TORECV");
            } catch (IOException e) {
                return;
            }
        }
    }

    private static final int MAX_USER_LENGTH = 100;
    private static String username = null, address = "localhost";
    private static int port = 2428; // stands for "chat"

    public static void main(String args[]) {
        if (args.length == 0) {
            System.err.println("Missing argument: <username> needed");
            return;
        } else if (args[0].length() > MAX_USER_LENGTH) {
            System.err.println(String.format("Username cannot exceed %d characters", MAX_USER_LENGTH));
        }
        if (args.length > 0)
            username = args[0];
        if (args.length > 1)
            address = args[1];
        if (args.length > 2)
            try {
                port = Integer.parseInt(args[2]);
                if (port < 0 || port > 65535)
                    throw new NumberFormatException();
            } catch(NumberFormatException e) {
                port = 2428;
                System.err.println("Invalid argument for port, using default port number " + port);
            }

        try {
            SendThread sendThread = new SendThread();
            sendThread.start();
            RecvThread recvThread = new RecvThread();
            recvThread.start();
        } catch (UnknownHostException e) {
            System.err.println("Invalid host given");
        } catch (IOException e) {
            System.err.println("Could not connect to server");
        }
    }
}
