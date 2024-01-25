import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Switch extends Device{
    private final Map<String, Address> deviceIDToPortMap = new HashMap<>();
    private boolean running = true;

    public static void main(String[] args) {
        Switch sw = new Switch(args[0],args[1]);
        sw.start();
    }

    public Switch(String deviceID, String configPath){
        super(deviceID, configPath);

    }

    public void start(){
        //noinspection InfiniteLoopStatement
        while (running){
            Message incomingMessage = receiveMessage();
            if (!deviceIDToPortMap.containsKey(incomingMessage.getOriginalSenderID())){
                deviceIDToPortMap.put(incomingMessage.getOriginalSenderID(), incomingMessage.getVirtualPort());
            }
            if (deviceIDToPortMap.containsKey(incomingMessage.getDestinationID())){
                Address outgoingPort = deviceIDToPortMap.get(incomingMessage.getDestinationID());
                sendMessage(incomingMessage, outgoingPort);
            } else {
                flood(incomingMessage);
            }
        }
    }

    private void flood(Message incomingMessage) {
        List<Address> outgoingPorts = new ArrayList<>(virtualPortList);
        outgoingPorts.remove(incomingMessage.getVirtualPort());
        for(Address outgoingPort:outgoingPorts) sendMessage(incomingMessage, outgoingPort);
    }

    public Map<String, Address> getTable() {
        return deviceIDToPortMap;
    }

    public void stop(){
        running = false;
    }
}
