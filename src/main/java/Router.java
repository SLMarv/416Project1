import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Router extends Device{
        private final VectorTable vectorTable = new VectorTable();
        private final Map<String, Connection> nextHopMap = new HashMap<>();

        public static void main(String[] args) throws IOException {
                Router router = new Router(args[0],args[1]);
                router.start();
        }

        protected Router(String deviceID, String configPath) throws SocketException {
                super(deviceID, configPath);
                for(Connection port : virtualPortList){
                        nextHopMap.put(port.getDeviceID(), port);
                        vectorTable.updateTable(new TableEntry(port.getSubnet(), port.getDeviceID(), 1));
                }
        }

        @Override
        void start() throws IOException {
                System.out.print("");
                broadcastTable();
                while (running){
                      Message message = receiveMessage();
                      String content = message.getMessageContent();
                      boolean tableUpdated = false;
                      for (String entryString : content.split("\n")){
                              if (entryString.equals("")) continue;
                              if (vectorTable.updateTable(entryString, message.getOriginalSenderID())){
                                      tableUpdated = true;
                              }
                      }
                      if (tableUpdated) broadcastTable();
                }
        }

        private void broadcastTable() {
                for(Connection port : virtualPortList){
                        if (!"R".equals(port.getDeviceID().substring(0,1))) continue;
                        Message message = new Message(
                                null,
                                getDeviceID(),
                                port.getDeviceID(),
                                convertTableToString(vectorTable)
                        );
                        sendMessage(message, port);
                        System.out.println(message.getMessageContent());
                }
        }

        private String convertTableToString(VectorTable vectorTable) {
                StringBuilder stringBuilder = new StringBuilder();
                for (TableEntry entry : vectorTable.getTable().values()){
                        stringBuilder.append(entry.toString()).append("\n");
                }
                return stringBuilder.toString();
        }
}