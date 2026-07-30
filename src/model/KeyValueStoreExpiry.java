package model;

public class KeyValueStoreExpiry  {
    String value;
    Long expiry;

    public KeyValueStoreExpiry() {
    }

    public KeyValueStoreExpiry(String value, Long expiry) {
        this.value = value;
        this.expiry = expiry;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getExpiry() {
        return expiry;
    }

    public void setExpiry(Long expiry) {
        this.expiry = expiry;
    }

    @Override
    public String toString() {
        return "KeyValueStoreExpiry{" +
                "value='" + value + '\'' +
                ", expiry=" + expiry +
                '}';
    }
}
