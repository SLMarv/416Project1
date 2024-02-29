public class TableEntry {
    private final String destination;
    private String nextHop;
    private int cost;


    public TableEntry(String destinationSubnet, String nextHop, int cost) {
        this.destination = destinationSubnet;
        this.nextHop = nextHop;
        this.cost = cost;
    }

    public static TableEntry parseFromString(String s, String sourceOfTableEntry){
        String[] arguments = s.split(" / ");
        try {
            String destination = arguments[0];
            String nextHop = sourceOfTableEntry==null? arguments[1] : sourceOfTableEntry;
            int cost = sourceOfTableEntry==null? Integer.parseInt(arguments[2]) : Integer.parseInt(arguments[2]) + 1;
            return new TableEntry(destination, nextHop, cost);
        } catch (Exception e){
            throw new RuntimeException("Failure to parse into a TableEntry: " + s +"\n");
        }
    }

    @Override
    public String toString() {
        return String.format("%s / %s / %d", destination, nextHop, cost);
    }

    public int getCost() {
        return cost;
    }

    public String getDestination() {
        return destination;
    }

    public String getNextHop() {
        return nextHop;
    }
}
