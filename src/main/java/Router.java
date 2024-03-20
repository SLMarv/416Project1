import java.io.IOException;
import java.net.SocketException;

public class Router extends Device{
        public static final String ROUTER_ID_PREFIX = "R";

        private final VectorTable vectorTable = new VectorTable();

        public static void main(String[] args) throws IOException {
                Router router = new Router(args[0],args[1]);
                router.start();
        }

        protected Router(String deviceID, String configPath) throws SocketException {
                super(deviceID, configPath);
                for(Connection port : virtualPortList){
                        vectorTable.updateTable(new TableEntry(port.getSubnet(), port.getDeviceID(), deviceID, 0));
                }
        }

        @Override
        void start() throws IOException {
                broadcastTable(getDeviceID());
                while (running){
                      Message message = receiveMessage();
                      if (message.getOriginalSenderID().startsWith(ROUTER_ID_PREFIX)) updateTableFrom(message);
                      else if (isDirectlyConnectedToSubnet(message.getMessageContent().substring(0,2)))
                              sendMessageToLocalNetwork(message);
                      else routeMessage(message);
                }
        }


        private boolean isDirectlyConnectedToSubnet(String subnet){
                return vectorTable.get(subnet).getCost() == 0;
        }

        private void routeMessage(Message message) {
                String destinationSubnet = message.getMessageContent().substring(0,2);
                Connection outgoingPort = configParser.parseDeviceAddress(vectorTable.get(destinationSubnet).getOutgoingPort());
                Message outgoingMessage = new Message(outgoingPort, message.getOriginalSenderID(), "", message.getMessageContent());
                sendMessage(outgoingMessage, outgoingPort);
        }


        private void sendMessageToLocalNetwork(Message message) {
                String[] splitMessageContent = message.getMessageContent().split(ROUTING_REGEX);
                String destinationID = splitMessageContent[0];
                String messageContent = message.getOriginalSenderID() + ROUTING_REGEX + splitMessageContent[1];
                Connection outgoingPort = configParser.parseDeviceAddress(vectorTable.get(destinationID.substring(0,2)).getOutgoingPort());
                Message outgoingMessage = new Message(outgoingPort, getDeviceID(), destinationID, messageContent);
                sendMessage(outgoingMessage, outgoingPort);
        }

        private void updateTableFrom(Message message) {
                String content = message.getMessageContent();
                boolean tableUpdated = false;
                for (String entryString : content.split("\n")){
                        if (entryString.equals("")) continue;
                        if (vectorTable.updateTable(entryString, message.getOriginalSenderID())){
                                tableUpdated = true;
                        }
                }
                if (tableUpdated) broadcastTable(message.getOriginalSenderID());
        }

        private void broadcastTable(String updateOriginID) {
                String tableString = vectorTable.toString();
                System.out.println("Updated table from " +updateOriginID + "'s table\n" +
                        "Network / Outgoing port / Next Hop / Cost\n"+
                        tableString);
                for(Connection port : virtualPortList){
                        if (!port.getDeviceID().startsWith(ROUTER_ID_PREFIX)) continue;
                        Message message = new Message(
                                null,
                                getDeviceID(),
                                port.getDeviceID(),
                                tableString
                        );
                        sendMessage(message, port);
                }
        }
}
