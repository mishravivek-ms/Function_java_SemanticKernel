package com.optum.util;
import java.io.Serializable;

public class InputStreamWrapper implements Serializable {
    private byte[] data;

    public InputStreamWrapper() {
    }
    public InputStreamWrapper(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}