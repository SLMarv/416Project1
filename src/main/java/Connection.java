public class Connection {
    private final String deviceID;
    private final String ip;
    private final int port;
    private final String subnet;

    public Connection(String ip, int port){
        this.ip = ip;
        this.port = port;
        this.subnet = "";
        this.deviceID = "";
    }

    public Connection(String deviceID, String ip, int port, String subnet){
        this.deviceID = deviceID;
        this.ip = ip;
        this.port = port;
        this.subnet = subnet;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection connection1 = (Connection) o;
        if (port != connection1.port) return false;
        return ip.equals(connection1.ip);
    }

    public String getSubnet() {
        return subnet;
    }

    public String getDeviceID() {
        return deviceID;
    }
}