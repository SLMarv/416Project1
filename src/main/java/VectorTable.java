import java.util.HashMap;
import java.util.Map;

public class VectorTable {
    private final Map<String, TableEntry> table = new HashMap<>();

    /**
     * @param fromString
     * @param source device source, null if self.
     * @return true if the new entry was added to the table
     */
    public boolean updateTable(String fromString, String source){
        return updateTable(TableEntry.parseFromString(fromString, source));
    }

    /**
     * @param entry
     * @return true if the new entry was added to the table
     */
    public boolean updateTable(TableEntry entry){
        if (!table.containsKey(entry.getDestination())){
            table.put(entry.getDestination(), entry);
            return true;
        }
        TableEntry oldEntry = table.get(entry.getDestination());
        if(oldEntry.getCost() > entry.getCost()){
            table.put(entry.getDestination(), entry);
            return true;
        }
        return false;
    }

    public Map<String, TableEntry> getTable() {
        return table;
    }
}
