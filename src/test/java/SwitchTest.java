import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    public void testTableGeneration(){
        Map<String, Address> expectedMap = new HashMap<>(); //Needs to be the same type of map as switch class
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
    public void testMessageOutput(){
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
        private final List<Map.Entry<Message,Address>> outputMessages = new ArrayList<>();
        private final ConfigParser parser = new ConfigParser("src\\test\\resources\\testConfig.xml");

        private TSwitch(String deviceID, Queue<Message> testMessages) {
            super(deviceID, testPath);
            this.testMessages = testMessages;
        }

        @Override
        public void sendMessage(Message message, Address outgoingPort){
            outputMessages.add(new AbstractMap.SimpleEntry<>(
                    new Message(parser.parseDeviceAddress(getDeviceID()),
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