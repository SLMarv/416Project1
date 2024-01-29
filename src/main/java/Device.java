import java.util.List;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Device {
    private final Address deviceAddress;
    private final String deviceID;
    protected final List<Address> virtualPortList;

    protected static ReentrantLock lock = new ReentrantLock();

    protected static boolean isDeviceSending = false;
    protected static Condition isNotSending = lock.newCondition();
    protected static Condition isDoneReceiving = lock.newCondition();

    protected static int devicesSendingNum = 0;
    protected static int devicesReceivingNum = 0;

    protected Device(String deviceID, String configPath) {
        ConfigParser configParser = new ConfigParser(configPath);
        this.deviceID = deviceID;
        this.deviceAddress = configParser.parseDeviceAddress(deviceID);
        this.virtualPortList = configParser.parseVirtualPorts(deviceID);
        //TODO set up socket here
    }


    //TODO
    public void sendMessage(Message message, Address outgoingPort){
        /* ExecutorService es = Executors.newFixedThreadPool(4);
        Runnable uploader = new CaseS();
        es.submit(uploader); */
        // TODO: finish CaseS
    }

    public static class CaseS implements Runnable {

        // add variables here

        public CaseS(){

            // and here

        }
        public void run() {

            lock.lock();

            try {

                while(isDeviceSending && devicesReceivingNum == 0) {
                    isDoneReceiving.await();
                }
                isDeviceSending = true;
                devicesSendingNum++;

                // send UDP packet here

                devicesSendingNum--;
                if(devicesSendingNum == 0){
                    isNotSending.signal();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                isDeviceSending = false;
                lock.unlock();
            }

        }
    }

    public Message receiveMessage(){
        /* ExecutorService es = Executors.newFixedThreadPool(4);
        Runnable uploader = new CaseR();
        es.submit(uploader); */
        // TODO: finish CaseR
        return null;
    }

    private static class CaseR implements Runnable {

        // add variables here

        public CaseR(){

            // and here

        }
        public void run() {

            lock.lock();

            try {

                while(isDeviceSending && devicesReceivingNum == 0) {
                    isDoneReceiving.await();
                }
                isDeviceSending = true;
                devicesSendingNum++;

                // receive UDP packet here

                devicesSendingNum--;
                if(devicesSendingNum == 0){
                    isNotSending.signal();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                isDeviceSending = false;
                lock.unlock();
            }

        }

    }

    public String getDeviceID() {
        return deviceID;
    }

    protected static class Message{
        private final Address address;
        private final String originalSenderID;
        private final String destinationID;
        private final String messageContent;


        /**
         * @param address The port that the device received the message from
         * @param originalSenderID The ID of the PC that first sent the message
         * @param destinationID The intended final destination of the message.
         */
        public Message(Address address, String originalSenderID, String destinationID, String messageContent) {
            this.address = address;
            this.originalSenderID = originalSenderID;
            this.destinationID = destinationID;
            this.messageContent = messageContent;
        }

        public String getDestinationID() {
            return destinationID;
        }

        public Address getVirtualPort() {
            return address;
        }

        public String getOriginalSenderID() {
            return originalSenderID;
        }

        public String getMessageContent() {
            return messageContent;
        }
    }
}

