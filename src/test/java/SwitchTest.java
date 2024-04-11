import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;

class SwitchTest {
    private static final String testPath = "src\\test\\resources\\testConfig.xml";
    private final ConfigParser configParser = new ConfigParser(testPath);
    private final List<Device.Message> testMessages = List.of(
            new Device.Message(configParser.parseDeviceAddress("A"), "A", "C", "Hello"),
            new Device.Message(configParser.parseDeviceAddress("S2"), "C", "A", "Hi"),
            new Device.Message(configParser.parseDeviceAddress("S2"), "D", "C", "No u"),
            new Device.Message(configParser.parseDeviceAddress("A"), "A", "D", "shut up")
    );

    @Test
    public void testTableGeneration() throws IOException {
        Map<String, Connection> expectedMap = new HashMap<>(); //Needs to be the same type of map as switch class
        Switch testSwitch = new TSwitch("S1", new LinkedList<>(testMessages));
        testSwitch.start();
        for (int index = 0; index < testMessages.size()-1; index++){
            Device.Message message = testMessages.get(index);
            expectedMap.put(message.getOriginalSenderID(), message.getVirtualPort());
        }
        Assertions.assertEquals(expectedMap.keySet(), testSwitch.getTable().keySet());
        Assertions.assertEquals(expectedMap.values(), expectedMap.values());
    }

    @Test
    public void testMessageOutput() throws IOException {
        TSwitch testSwitch = new TSwitch("S1", new LinkedList<>(testMessages));
        testSwitch.start();
        /*
        for (Map.Entry<Device.Message,Address>  message: testSwitch.outputMessages){
             System.out.println(message.getKey().getOriginalSenderID() + ":" + message.getKey().getDestinationID() + ":"
                     + message.getValue().getIP());
         }
        */
        Assertions.assertEquals(5, testSwitch.outputMessages.size());
    }

    private static class TSwitch extends Switch {
        private final Queue<Message> testMessages;
        private final List<Map.Entry<Message, Connection>> outputMessages = new ArrayList<>();
        private final ConfigParser parser = new ConfigParser("src\\test\\resources\\testConfig.xml");

        private TSwitch(String deviceID, Queue<Message> testMessages) throws SocketException {
            super(deviceID, testPath);
            this.testMessages = testMessages;
        }

        @Override
        public void sendMessage(Message message, Connection outgoingPort){
            outputMessages.add(new AbstractMap.SimpleEntry<>(
                    new Message(parser.parseDeviceAddress(getMACAddress()),
                        message.getOriginalSenderID(),
                        message.getDestinationID(),
                        message.getMessageContent()),
                    outgoingPort));
        }

        @Override
        public Message receiveMessage(){
            Message message = testMessages.remove();
            if (testMessages.isEmpty()) stop();
            return message;
        }
    }
}