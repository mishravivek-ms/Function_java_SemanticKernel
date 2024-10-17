package com.aiaudit.util;
import java.io.Serializable;

public class inputStreamWrapper implements Serializable {
    private byte[] data;

    public inputStreamWrapper() {
    }
    public inputStreamWrapper(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}