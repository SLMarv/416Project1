import java.io.IOException;
import java.net.*;
import java.util.List;

public abstract class Device {

    static private final String REGEX = "%%";
    static protected final String ROUTING_REGEX = "!'";
    static public final Connection PRINT_CONNECTION = new Connection("OUT","OUT",0, null);

    private final String macAddress;
    protected  List<Connection> virtualPortList;
    private final DatagramSocket socket;
    protected boolean running = true;
    protected final ConfigParser configParser;

    protected Device(String macAddress, String configPath) throws SocketException {
        configParser = new ConfigParser(configPath);
        this.macAddress = macAddress;
        System.out.println("Running Device " + macAddress);
        Connection deviceConnection = configParser.parseDeviceAddress(macAddress);
        virtualPortList = configParser.parseVirtualPorts(macAddress);
        socket = new DatagramSocket(deviceConnection.getPort());
    }

    abstract void start() throws IOException;

    //TODO
    public void sendMessage(Message message, Connection outgoingPort){
        String messageContent = message.getDestinationID() + REGEX + message.getOriginalSenderID()
                + REGEX+ message.getMessageContent() +REGEX;
        try {
            InetAddress serverIP = InetAddress.getByName(outgoingPort.getIP());
            byte[] buffer =messageContent.getBytes();
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, serverIP, outgoingPort.getPort());
            socket.send(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message receiveMessage() throws IOException {
        int messageSize = 1000;
        DatagramPacket request = new DatagramPacket(new byte[messageSize], messageSize);
        socket.receive(request);
        byte[] buffer = request.getData();
        Connection connection = new Connection(request.getAddress().getHostAddress(), request.getPort());
        String packetContent = new String(buffer);
        String[] data = packetContent.split(REGEX);
        if (data.length == 3) throw new RuntimeException("Received message is missing an ending regex (possible" +
                "that the receiving buffer is not big enough):\n" + packetContent);
        String destinationID = data[0];
        String originID = data[1];
        StringBuilder message = new StringBuilder();
        for (int index = 2; index < data.length-1; index++){
            message.append(data[index]);
        }
        return new Message(connection, originID, destinationID, message.toString());
    }

    public String getMACAddress() {
        return macAddress;
    }

    protected static class Message{
        private final Connection connection;
        private final String originalSenderID;
        private final String destinationID;
        private final String messageContent;


        /**
         * @param connection The port that the device received the message from. If sending from this device,
         *                this parameter isn't used and can be null
         * @param originalSenderID The ID of the PC that first sent the message
         * @param destinationID The intended final destination of the message.
         */
        public Message(Connection connection, String originalSenderID, String destinationID, String messageContent) {
            this.connection = connection;
            this.originalSenderID = originalSenderID;
            this.destinationID = destinationID;
            this.messageContent = messageContent;
        }

        public String getDestinationID() {
            return destinationID;
        }

        public Connection getVirtualPort() {
            return connection;
        }

        public String getOriginalSenderID() {
            return originalSenderID;
        }

        public String getMessageContent() {
            return messageContent;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "connection=" + connection +
                    ", originalSenderID='" + originalSenderID + '\'' +
                    ", destinationID='" + destinationID + '\'' +
                    ", messageContent='" + messageContent + '\'' +
                    '}';
        }
    }
}

