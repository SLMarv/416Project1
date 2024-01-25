import java.util.List;

public abstract class Device {
    private final Address deviceAddress;
    private final String deviceID;
    protected final List<Address> virtualPortList;

    protected Device(String deviceID, String configPath) {
        ConfigParser configParser = new ConfigParser(configPath);
        this.deviceID = deviceID;
        this.deviceAddress = configParser.parseDeviceAddress(deviceID);
        this.virtualPortList = configParser.parseVirtualPorts(deviceID);
        //TODO set up socket here
    }


    //TODO
    public void sendMessage(Message message, Address outgoingPort){

    }

    public Message receiveMessage(){
        return null;
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
         * @param address The port that the device received the message from
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

