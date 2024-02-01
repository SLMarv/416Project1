public class PC extends Device{
    protected PC(String deviceID, String configPath) {
        super(deviceID, configPath);
    }

    public void start(){
        //make executor
        //add reading runnable
        while(running) {
            Message message = receiveMessage();
            //print message sender and content
        }
    }

    public static class ReadingRunnable implements Runnable{
        Device device;

        public ReadingRunnable(Device device){
            this.device = device;
        }

        @Override
        public void run() {
            while(device.running){
                //make a scanner reading terminal input and prompt user for destination and message content info
                //if input == exitCode then make device.running = false then break
                //Otherwise create a Message and call sendMessage(message)
            }
        }
    }

}
