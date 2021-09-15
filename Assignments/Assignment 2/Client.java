import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private static class SockThread extends Thread {
        protected static final int MAX_HEADER_LINE = 116, MAX_TRIES = 2;
        protected Socket socket;
        protected OutputStream send;
        protected InputStream recv;

        public SockThread() throws IOException {
           socket = new Socket(address, port);
           send = socket.getOutputStream();
           recv = socket.getInputStream();
        }

        protected String readLine() throws IOException {
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

        protected ArrayList<String> getHeader() throws IOException {
            ArrayList<String> ret = new ArrayList<>();
            String next = readLine();
            while (!next.equals("")) {
                ret.add(next);
                next = readLine();
            }
            return ret;
        }

        protected int parseResponse(ArrayList<String> headerList, String check) {
            if (headerList.size() != 1)
                return 103;

            String[] header = headerList.get(0).split(" ");
            switch (header[0]) {
                case "REGISTERED":
                    if (header.length != 3)
                        return 103;
                    if (!header[2].equals(check))
                        return 103;
                    switch (header[1]) {
                        case "TOSEND":
                            return 200;
                        case "TORECV":
                            return 201;
                        default:
                            return 103;
                    }
                case "SENT":
                    if (header.length != 2)
                        return 103;
                    return header[1].equals(check) ? 202 : 103;
                case "ERROR":
                    if (header.length < 2)
                        return 103;
                    switch (header[1]) {
                        case "100":
                            return 100;
                        case "101":
                            return 101;
                        case "102":
                            return 102;
                        default:
                            return 103;
                    }
                default:
                    return 103;
            }
        }

        protected int sendData(List<String> headerList) throws IOException {
            return sendData(headerList, "", username);
        }

        protected int sendData(List<String> headerList, String message, String check) throws IOException {
            send.write((String.join("\n", headerList) + "\n\n" + message).getBytes());
            send.flush();
            return parseResponse(getHeader(), check);
        }

        protected int registerClient(String code) throws IOException {
            for (int i = 0; i < MAX_TRIES; ++i) {
                try {
                    return sendData(Arrays.asList(String.join(" ", "REGISTER", code, username)));
                } catch(IOException e) {
                    continue;
                }
            }
            throw new IOException();
        }

        @Override
        public void run() {
            throw new UnsupportedOperationException();
        }
    }

    private static class SendThread extends SockThread {
        public SendThread() throws IOException {
            // empty constructor to handle throwing of IOException
        }

        private void inAndSend(Scanner scanner) throws IOException {
            String target = scanner.next();
            if (target.charAt(0) != '@' || target.length() == 1) {
                System.out.println("No target user specified, please retype the message");
                return;
            }
            target = target.substring(1);

            String message = scanner.nextLine().substring(1);
            ArrayList<String> headerList = new ArrayList<>();
            headerList.add(String.format("SEND %s", target));
            headerList.add(String.format("Content-length: %d", message.getBytes().length));

            switch (sendData(headerList, message, target)) {
                case 202:
                    System.out.println("Sent!");
                    return;
                case 102:
                    System.out.println("Could not send message");
                    return;
                default:
                    throw new IOException();
            }
        }

        @Override
        public void run() {
            try {
                int code = registerClient("TOSEND");
                if (code != 200) {
                    System.err.println(String.format("Error registering for TOSEND, error code %d", code));
                    throw new IOException();
                }
                Scanner scanner = new Scanner(System.in);
                while (!isInterrupted())
                    inAndSend(scanner);
                throw new IOException();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch(IOException e1) {
                    return;
                }
            }
        }
    }

    private static class RecvThread extends SockThread {
        public RecvThread() throws IOException {
            // empty constructor to handle throwing of IOException
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

        private void sendResponse(int errno) throws IOException {
            sendResponse(errno, "");
        }

        private void sendResponse(int errno, String msg) throws IOException {
            switch (errno) {
                case 103:
                    sendResponse("ERROR 103 Header incomplete");
                    throw new IOException();
                case 203:
                    sendResponse(String.format("RECEIVED %s", msg));
                    break;
                default:
                    break;
            }
        }
        private void recvAndOut() throws IOException {
            ArrayList<String> headerList = getHeader();
            if (headerList.size() != 2)
                sendResponse(103);

            String[] line = headerList.get(0).split(" ");
            if (line.length != 2 || !line[0].equals("FORWARD"))
                sendResponse(103);
            String sender = line[1];

            line = headerList.get(1).split(" ");
            if (line.length != 2 || !line[0].equals("Content-length:"))
                sendResponse(103);

            try {
                int len = Integer.parseInt(line[1]);
                byte[] message = new byte[len];
                int curr = 0;
                while (curr < len) {
                    int read = recv.read(message, curr, len - curr);
                    if (read != -1)
                        curr += read;
                }
                System.out.println(String.format("%s: %s", sender, new String(message)));
                sendResponse(203, sender);
            } catch(NumberFormatException e) {
                sendResponse(103);
                throw new IOException();
            }
        }

        @Override
        public void run() {
            try {
                int code = registerClient("TORECV");
                if (code != 201) {
                    System.err.println(String.format("Error registering for TORECV, error code %d", code));
                    throw new IOException();
                }
                while (!isInterrupted())
                    recvAndOut();
                throw new IOException();
            } catch (IOException e) {
                try {
                    socket.close();
                } catch(IOException e1) {
                    return;
                }
            }
        }
    }

    private static final int MAX_USER_LENGTH = 100;
    private static String username = null, address = "localhost";
    private static int port = 2428; // stands for "chat"

    public static void main(String[] args) {
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
            RecvThread recvThread = new RecvThread();
            sendThread.start();
            recvThread.start();
            while (sendThread.isAlive() && recvThread.isAlive());
            sendThread.interrupt();
            recvThread.interrupt();
        } catch (UnknownHostException e) {
            System.err.println("Invalid host given");
        } catch (IOException e) {
            System.err.println("Could not connect to server");
        }
    }
}
