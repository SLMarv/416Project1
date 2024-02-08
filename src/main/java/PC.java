import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PC extends Device{
    protected PC(String deviceID, String configPath) {
        super(deviceID, configPath);
    }

    public void start(){
        //make executor
        ExecutorService eS = Executors.newFixedThreadPool(4);
        //add reading runnable
        eS.submit(new ReadingRunnable(this));
        while(running) {
            Message message = receiveMessage();
            //print message sender and content
            System.out.println("Received message from " +message.getOriginalSenderID()+ ":" +message.getMessageContent() );
        }
    }

    public static class ReadingRunnable implements Runnable {
        Device device;

        public ReadingRunnable(Device device) {
            this.device = device;
        }

        @Override
        public void run() {

            while (device.running) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter destination: ");
                String destination = scanner.nextLine();

                System.out.println("Enter message content: ");
                String content = scanner.nextLine();

                System.out.println("Enter source port");
                int port= Integer.parseInt(scanner.nextLine());
                Address address = new Address(destination, port);
                if (content.equals("exitCode")) {
                    device.running = false;
                    break;
                } else {


                    Message message = new Message(address, device.getDeviceID(), destination, content);
                    device.sendMessage(message, address);
                }
                scanner.close();
                //make a scanner reading terminal input and prompt user for destination and message content info
                //if input == exitCode then make device.running = false then break
                //Otherwise create a Message and call sendMessage(message)
            }
        }
    }
}


