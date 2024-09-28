package com.optum.DTO;

public class response {
    answer firstQuestionReply;
    answer secondQuestionReply;

    public response() {
    }

    public response(answer firstQuestionReply, answer secondQuestionReply) {
        this.firstQuestionReply = firstQuestionReply;
        this.secondQuestionReply = secondQuestionReply;
    }
    public answer getFirstQuestionReply() {
        return firstQuestionReply;
    }

    public void setFirstQuestionReply(answer firstQuestionReply) {
        this.firstQuestionReply = firstQuestionReply;
    }

    public answer getSecondQuestionReply() {
        return secondQuestionReply;
    }

    public void setSecondQuestionReply(answer secondQuestionReply) {
        this.secondQuestionReply = secondQuestionReply;
    }


}
