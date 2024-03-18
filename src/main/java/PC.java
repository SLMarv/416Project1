import java.io.IOException;
import java.net.SocketException;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PC extends Device{
    public static void main(String[] args) throws IOException {
        PC pc = new PC(args[0],args[1]);
        pc.start();
    }

    protected PC(String deviceID, String configPath) throws SocketException {
        super(deviceID, configPath);
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
                System.out.println("Received message from " + message.getOriginalSenderID() + ":\n" + message.getMessageContent());
            }
        }
    }

    public static class ReadingRunnable implements Runnable {
        Device device;

        public ReadingRunnable(Device device) {
            this.device = device;
        }

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (device.running) {

                System.out.print("Enter destination: ");
                String destination = scanner.nextLine();

                if (destination.equals("!E")) {
                    device.running = false;
                    return;
                }

                System.out.println("Enter message content: ");
                String content = scanner.nextLine();


                Message message = new Message(device.virtualPortList.get(0), device.getDeviceID(), destination, content);
                device.sendMessage(message, device.virtualPortList.get(0));

                //make a scanner reading terminal input and prompt user for destination and message content info
                //if input == exitCode then make device.running = false then break
                //Otherwise create a Message and call sendMessage(message)
            }
            scanner.close();
        }
    }
}


