import java.io.IOException;
import java.net.SocketException;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PC extends Device{

    private final String subnetRouterID;

    public static void main(String[] args) throws IOException {
        PC pc = new PC(args[0],args[1]);
        pc.start();
    }

    protected PC(String deviceID, String configPath) throws SocketException {
        super(deviceID, configPath);
        subnetRouterID = configParser.parseRouterFor(deviceID.substring(0,2));
    }

    public void start() throws IOException {
        //make executor
        ExecutorService eS = Executors.newFixedThreadPool(4);
        //add reading runnable
        eS.submit(new ReadingRunnable(this));
        while(running) {
            Message message = receiveMessage();
            //print message sender and content
            if (Objects.equals(message.getDestinationID(), getDeviceID())) {
                if(message.getOriginalSenderID().startsWith(Router.ROUTER_ID_PREFIX)){
                    String messageContent = message.getMessageContent().split(ROUTING_REGEX)[1];
                    String originalSender = message.getMessageContent().split(ROUTING_REGEX)[0];
                    message = new Message(message.getVirtualPort(), originalSender, message.getDestinationID(), messageContent);
                }
                System.out.println("Received message from " + message.getOriginalSenderID() + ":\n" + message.getMessageContent());
            }
        }
    }

    private static class ReadingRunnable implements Runnable {
        PC pc;

        public ReadingRunnable(PC pc) {
            this.pc = pc;
        }

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (pc.running) {

                System.out.print("Enter destination: ");
                String destination = scanner.nextLine();

                if (destination.equals("!E")) {
                    pc.running = false;
                    return;
                }

                System.out.println("Enter message content: ");
                String content = scanner.nextLine();

                if (destination.contains(".") && !destination.startsWith(pc.getDeviceID().substring(0,2))){
                    content = destination + ROUTING_REGEX + content;
                    destination = pc.subnetRouterID;
                }

                Message message = new Message(pc.virtualPortList.get(0), pc.getDeviceID(), destination, content);
                pc.sendMessage(message, pc.virtualPortList.get(0));

                //make a scanner reading terminal input and prompt user for destination and message content info
                //if input == exitCode then make device.running = false then break
                //Otherwise create a Message and call sendMessage(message)
            }
            scanner.close();
        }
    }
}


