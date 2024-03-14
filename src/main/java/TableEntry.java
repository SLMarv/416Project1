public class TableEntry {
    private final String destination;
    private String outgoingPort;
    private String nextHop;
    private int cost;


    public TableEntry(String destinationSubnet, String outgoingPort, String nextHop, int cost) {
        this.destination = destinationSubnet;
        this.outgoingPort = outgoingPort;
        this.nextHop = nextHop;
        this.cost = cost;
    }

    public static TableEntry parseFromString(String s, String sourceOfTableEntry){
        String[] arguments = s.split(" / ");
        try {
            String destination = arguments[0];
            String outGoingPort = sourceOfTableEntry==null? arguments[1] : sourceOfTableEntry;
            String nextHop = arguments[3];
            int cost = sourceOfTableEntry==null? Integer.parseInt(arguments[3]) : Integer.parseInt(arguments[3]) + 1;
            return new TableEntry(destination, outGoingPort, nextHop, cost);
        } catch (Exception e){
            throw new RuntimeException("Failure to parse into a TableEntry: " + s +"\n");
        }
    }

    @Override
    public String toString() {
        return String.format("%s / %s / %s / %d", destination, outgoingPort, nextHop, cost);
    }

    public int getCost() {
        return cost;
    }

    public String getDestination() {
        return destination;
    }

    public String getOutgoingPort() {
        return outgoingPort;
    }
}
