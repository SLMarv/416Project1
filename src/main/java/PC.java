public class PC extends Device{
    protected PC(String deviceID, String configPath) {
        super(deviceID, configPath);
    }

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
}
