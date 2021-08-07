package client;

import com.beust.jcommander.Parameter;

import java.util.HashMap;
import java.util.Map;

public class Args {
    @Parameter(names = {"-t"})
    private String type;

    @Parameter(names = {"-k"})
    private String key;

    @Parameter(names = {"-v"})
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();

        if (type != null && !type.isEmpty()) map.put("type", type);
        if (key != null && !key.isEmpty()) map.put("key", key);
        if (value != null && !value.isEmpty()) map.put("value", value);

        return map;
    }
}
