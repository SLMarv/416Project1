public class Address {
    private final String ip;
    private final int port;

    public Address(String ip, int port){
        this.ip = ip;
        this.port = port;
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
        Address address1 = (Address) o;
        if (port != address1.port) return false;
        return ip.equals(address1.ip);
    }
}