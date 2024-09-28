package com.optum.DTO;

public class agreementDTO {
    String signature;
    String signaturedate;

    public agreementDTO() {
    }
    public agreementDTO(String signature, String signaturedate) {
        this.signature = signature;
        this.signaturedate = signaturedate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignaturedate() {
        return signaturedate;
    }

    public void setSignaturedate(String signaturedate) {
        this.signaturedate = signaturedate;
    }

    @Override
    public String toString() {
        return "agreementDTO{" +
                "signature='" + signature + '\'' +
                ", signaturedate='" + signaturedate + '\'' +
                '}';
    }
}
