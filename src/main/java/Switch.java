import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Switch extends Device{
    private final Map<String, Connection> deviceIDToPortMap = new HashMap<>();
    private boolean running = true;

    public static void main(String[] args) throws IOException {
        Switch sw = new Switch(args[0],args[1]);
        sw.start();
    }

    public Switch(String deviceID, String configPath) throws SocketException {
        super(deviceID, configPath);

    }

    public void start() throws IOException {
        //noinspection InfiniteLoopStatement
        while (running){
            Message incomingMessage = receiveMessage();
            if (!deviceIDToPortMap.containsKey(incomingMessage.getOriginalSenderID())){
                deviceIDToPortMap.put(incomingMessage.getOriginalSenderID(), incomingMessage.getVirtualPort());
            }
            if (deviceIDToPortMap.containsKey(incomingMessage.getDestinationID())){
                Connection outgoingPort = deviceIDToPortMap.get(incomingMessage.getDestinationID());
                sendMessage(incomingMessage, outgoingPort);
            } else {
                flood(incomingMessage);
            }
        }
    }

    private void flood(Message incomingMessage) {
        List<Connection> outgoingPorts = new ArrayList<>(virtualPortList);
        outgoingPorts.remove(incomingMessage.getVirtualPort());
        StringBuilder print = new StringBuilder("flooding to\n");
        for(Connection outgoingPort:outgoingPorts) {
            sendMessage(incomingMessage, outgoingPort);
            print.append(outgoingPort.getIP()).append(" : ").append(outgoingPort.getPort()).append("\n");
        }
        System.out.println(print);
    }

    public Map<String, Connection> getTable() {
        return deviceIDToPortMap;
    }

    public void stop(){
        running = false;
    }
}
