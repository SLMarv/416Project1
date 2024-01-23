import java.util.HashMap;
import java.util.Map;

public class Switch extends Device{
    private Map<String, Address> table;

    public static void main(String[] args) {
        Switch sw = new Switch(args[0],args[1] );
    }

    public Switch(String deviceID, String configPath){

    }

    public void start(){
        //noinspection InfiniteLoopStatement
        while (true){
            Message incomingMessage = receiveMessage();

        }
    }
}
