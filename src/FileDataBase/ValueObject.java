package FileDataBase;
import org.json.simple.JSONObject;

public class ValueObject {
    JSONObject value;
    long TTL;
    ValueObject(JSONObject value, long TTL){
        this.value = value;
        this.TTL = TTL;
    }
}
