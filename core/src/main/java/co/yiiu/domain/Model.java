package co.yiiu.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomoya at 2020/1/7
 */
public class Model implements Serializable {
    private static final long serialVersionUID = -3568166121593327818L;

    private Map<String, Object> attribute = new HashMap<>();

    public void addAttribute(String key, Object value) {
        attribute.put(key, value);
    }

    public Map<String, Object> getAttribute() {
        return attribute;
    }
}
