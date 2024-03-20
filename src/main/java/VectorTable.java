import java.util.HashMap;
import java.util.Map;

public class VectorTable extends HashMap<String, TableEntry> {

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
        if (!containsKey(entry.getDestination())){
            put(entry.getDestination(), entry);
            return true;
        }
        TableEntry oldEntry = get(entry.getDestination());
        if(oldEntry.getCost() > entry.getCost()){
            put(entry.getDestination(), entry);
            return true;
        }
        return false;
    }
}
