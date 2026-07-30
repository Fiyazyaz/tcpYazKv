package model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {

    private final Map<String, KeyValueStoreExpiry> map = new ConcurrentHashMap<>();

    public String put(String key, KeyValueStoreExpiry keyValueStoreExpiry) {

        KeyValueStoreExpiry keyValueStoreExpiry1 = new KeyValueStoreExpiry();
        keyValueStoreExpiry1.setValue(keyValueStoreExpiry.getValue());
        keyValueStoreExpiry1.setExpiry(System.currentTimeMillis() + keyValueStoreExpiry.getExpiry() * 1000);

        map.put(key,keyValueStoreExpiry1 );
        return "OK";
    }

    public KeyValueStoreExpiry get(String key) {
        KeyValueStoreExpiry keyValueStoreExpiry = map.get(key);

        if(keyValueStoreExpiry == null) {
            return new KeyValueStoreExpiry();
        }
        if ( keyValueStoreExpiry.getExpiry() <= System.currentTimeMillis()) {
            map.remove(key);
            return new KeyValueStoreExpiry();
        }

        return keyValueStoreExpiry;
    }

    public String delete(String key) {

        KeyValueStoreExpiry keyValueStoreExpiry = map.get(key);

        if (keyValueStoreExpiry == null) {
            return "NOT FOUND";
        }

        if ( keyValueStoreExpiry.getExpiry() <= System.currentTimeMillis()) {
            map.remove(key);
            return "Time out, Already Delete";
        }

        if (map.remove(key) != null) {
            return "OK";
        }

        return "NOT FOUND";
    }

    public void restore(String key, KeyValueStoreExpiry value) {
        map.put(key, value);
    }
}
