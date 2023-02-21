package itstep.learning.model;

import java.util.Date;

public class AboutModel {
    private String message;
    private Date moment;

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
