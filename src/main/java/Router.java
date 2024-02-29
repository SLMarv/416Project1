import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Router extends Device{
        private final VectorTable vectorTable = new VectorTable();
        private final Map<String, Connection> nextHopMap = new HashMap<>();

        protected Router(String deviceID, String configPath) throws SocketException {
                super(deviceID, configPath);
                for(Connection port : virtualPortList){
                        nextHopMap.put(port.getDeviceID(), port);
                        vectorTable.updateTable(new TableEntry(port.getSubnet(), port.getDeviceID(), 1));
                }
        }

        @Override
        void start() throws IOException {

        }

        public VectorTable getVectorTable() {
                return vectorTable;
        }
}
