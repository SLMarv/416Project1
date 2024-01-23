public abstract class Device {

    //TODO
    public void sendMessage(Message message){

    }

    public Message receiveMessage(){
        return null;
    }

    protected static class Message{
        protected final Address senderAddress;
        protected String senderID;
        protected final String destinationID;

        public Message(Address senderAddress, String senderID, String destinationID) {
            this.senderAddress = senderAddress;
            this.senderID = senderID;
            this.destinationID = destinationID;
        }
    }
}

