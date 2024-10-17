package com.aiaudit.DTO;

import java.io.InputStream;

public class kernelRequest {
    String MRN_number;
    String document_name;

    InputStream data;

    public InputStream getData() {
        return data;
    }

    public void setData(InputStream data) {
        this.data = data;
    }


    public kernelRequest() {
    }
    public kernelRequest(String MRN_number, String document_name) {
        this.MRN_number = MRN_number;
        this.document_name = document_name;
    }


    public String getMRN_number() {
        return MRN_number;
    }

    public void setMRN_number(String MRN_number) {
        this.MRN_number = MRN_number;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

}
