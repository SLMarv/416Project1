import java.io.IOException;
import java.net.SocketException;
import java.util.Objects;

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
                broadcastTable(getMACAddress());
                while (running){
                      Message message = receiveMessage();
                      if (message.getOriginalSenderID().startsWith(ROUTER_ID_PREFIX)
                              && !message.getMessageContent().contains(ROUTING_REGEX)) updateTableFrom(message);
                      else if (Objects.equals(message.getDestinationID(), getMACAddress())){
                              if (isDirectlyConnectedToSubnet(message.getMessageContent().split(ROUTING_REGEX)[1].split("\\.")[0]))
                                      sendMessageToLocalNetwork(message);
                              else routeMessage(message);
                      } else System.out.println("Ignoring message from " + message.getOriginalSenderID());
                }
        }


        private boolean isDirectlyConnectedToSubnet(String subnet){
                return vectorTable.get(subnet).getCost() == 0;
        }

        private void routeMessage(Message message) {
                String destinationSubnet = message.getMessageContent().split(ROUTING_REGEX)[1].split("\\.")[0];
                String destinationMAC = vectorTable.get(destinationSubnet).getOutgoingPort();
                Connection outgoingPort = configParser.parseDeviceAddress(destinationMAC);
                Message outgoingMessage = new Message(outgoingPort, getMACAddress(), destinationMAC, message.getMessageContent());
                System.out.println("Routing to: " + destinationMAC);
                sendMessage(outgoingMessage, outgoingPort);
        }


        private void sendMessageToLocalNetwork(Message message) {
                String[] destinationIPSplit = message.getMessageContent().split(ROUTING_REGEX)[1].split("\\.");
                Connection outgoingPort = configParser.parseDeviceAddress(vectorTable.get(destinationIPSplit[0]).getOutgoingPort());
                Message outgoingMessage = new Message(outgoingPort, getMACAddress(), destinationIPSplit[1], message.getMessageContent());
                System.out.println("Sending to local System: " + destinationIPSplit[0] + destinationIPSplit[1]);
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
                                getMACAddress(),
                                port.getDeviceID(),
                                tableString
                        );
                        sendMessage(message, port);
                }
        }
}
