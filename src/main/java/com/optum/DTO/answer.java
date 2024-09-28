package com.optum.DTO;

public class answer implements java.io.Serializable {
    private String answerYorN;
    private String expertcomment;

    private String documentsignature;

    private String documentdate;

    private String dischargeflag;

    private String dischargecomment;

    public answer() {
    }

    public answer(String answerYorN, String expertcomment, String documentsignature, String documentdate, String dischargeflag, String dischargecomment) {
        this.answerYorN = answerYorN;
        this.expertcomment = expertcomment;
        this.documentsignature = documentsignature;
        this.documentdate = documentdate;
        this.dischargeflag = dischargeflag;
        this.dischargecomment = dischargecomment;
    }

    public String getAnswerYorN() {
        return answerYorN;
    }

    public void setAnswerYorN(String answerYorN) {
        this.answerYorN = answerYorN;
    }

    public String getExpertcomment() {
        return expertcomment;
    }

    public void setExpertcomment(String expertcomment) {
        this.expertcomment = expertcomment;
    }

    public String getDocumentsignature() {
        return documentsignature;
    }

    public void setDocumentsignature(String documentsignature) {
        this.documentsignature = documentsignature;
    }

    public String getDocumentdate() {
        return documentdate;
    }

    public void setDocumentdate(String documentdate) {
        this.documentdate = documentdate;
    }

    public String getDischargeflag() {
        return dischargeflag;
    }

    public void setDischargeflag(String dischargeflag) {
        this.dischargeflag = dischargeflag;
    }

    public String getDischargecomment() {
        return dischargecomment;
    }

    public void setDischargecomment(String dischargecomment) {
        this.dischargecomment = dischargecomment;
    }
}
