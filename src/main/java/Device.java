import java.io.IOException;
import java.net.*;
import java.util.List;

public abstract class Device {

    private final String deviceID;
    protected static List<Address> virtualPortList = null;
    private final DatagramSocket socket;
    protected boolean running = true;

    protected Device(String deviceID, String configPath) throws SocketException {
        ConfigParser configParser = new ConfigParser(configPath);
        this.deviceID = deviceID;
        Address deviceAddress = configParser.parseDeviceAddress(deviceID);
        virtualPortList = configParser.parseVirtualPorts(deviceID);
        socket = new DatagramSocket(deviceAddress.getPort());
        //TODO set up socket here
    }

    abstract void start() throws IOException;

    //TODO
    public void sendMessage(Message message, Address outgoingPort){
        String messageContent = message.getMessageContent();
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
        int messageSize = 100;
        DatagramPacket request = new DatagramPacket(new byte[messageSize], messageSize);
        socket.receive(request);
        byte[] buffer = request.getData();
        Address address = new Address(request.getAddress().getHostAddress(), request.getPort());
        String[] data = new String(buffer).split("%%");
        String destinationID = data[0];
        String originID = data[1];
        StringBuilder message = new StringBuilder();
        for (int index = 2; index < data.length; index++){
            message.append(data[index]);
        }
        return new Message(address, originID, destinationID, message.toString());
    }

    public String getDeviceID() {
        return deviceID;
    }

    protected static class Message{
        private final Address address;
        private final String originalSenderID;
        private final String destinationID;
        private final String messageContent;


        /**
         * @param address The port that the device received the message from. If sending from this device,
         *                this parameter isn't used
         * @param originalSenderID The ID of the PC that first sent the message
         * @param destinationID The intended final destination of the message.
         */
        public Message(Address address, String originalSenderID, String destinationID, String messageContent) {
            this.address = address;
            this.originalSenderID = originalSenderID;
            this.destinationID = destinationID;
            this.messageContent = messageContent;
        }

        public String getDestinationID() {
            return destinationID;
        }

        public Address getVirtualPort() {
            return address;
        }

        public String getOriginalSenderID() {
            return originalSenderID;
        }

        public String getMessageContent() {
            return messageContent;
        }
    }
}

