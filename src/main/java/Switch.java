import java.util.HashMap;
import java.util.Map;

public class Switch {
    private final int numberOfPorts;
    private final Map<String, Integer> table = new HashMap<>();

    public static void main(String[] args) {
        Switch sw = new Switch(args[0],args[1] );
    }

    public Switch(String deviceID, String configPath){
        Network network = new Network(deviceID, configPath);
        numberOfPorts = network.getNumberOfPorts();
    }
}
