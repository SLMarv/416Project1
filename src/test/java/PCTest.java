import org.junit.jupiter.api.Test;

import java.net.SocketException;
import java.util.*;

public class PCTest {
    private static final String testPath = "src\\test\\resources\\testConfig.xml";

    @Test
    public void testSending(){

    }



    private static class TestPC extends PC {
        private final Queue<Message> testMessages;
        private final boolean testingSending;
        private final List<Map.Entry<Message, Connection>> outputMessages = new ArrayList<>();
        private final ConfigParser parser = new ConfigParser(testPath);

        private TestPC(String deviceID, Queue<Message> testMessages, boolean testingSending) throws SocketException {
            super(deviceID, testPath);
            this.testMessages = testMessages;
            this.testingSending = testingSending;
        }

        @Override
        public void sendMessage(Message message, Connection outgoingPort) {
            outputMessages.add(new AbstractMap.SimpleEntry<>(
                    new Message(parser.parseDeviceAddress(getMACAddress()),
                            message.getOriginalSenderID(),
                            message.getDestinationID(),
                            message.getMessageContent()),
                    outgoingPort));
        }

        @Override
        public Message receiveMessage() {
            Message message = testMessages.remove();
            if (testMessages.isEmpty() && !testingSending) running = false;
            return message;
        }
    }
}
