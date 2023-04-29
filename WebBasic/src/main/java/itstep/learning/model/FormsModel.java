package itstep.learning.model;

import javafx.scene.paint.Color;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormsModel {
    private int number;
    private String text;
    private Date date;
    private String message;
    private Color color;

    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FormsModel{" +
                " number: " + number +
                " text: '" + text + '\'' +
                " date: " + date +
                " color: " + color +
                " message: '" + message + '\'' +
                " method: " + method + ' ' +
                '}';
    }

    public static FormsModel parse(HttpServletRequest req) {
        FormsModel model = new FormsModel();

        String string = req.getParameter("text");

        String str_num = req.getParameter("number");
        int number = str_num == null || str_num == "" ? 0 : Integer.parseInt(str_num);

        String str_date = req.getParameter("date");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        if (str_date == null || str_date == "") {
            date = new Date();
        } else {
            try {
                date = dateFormat.parse(str_date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        String par_color = req.getParameter("color");
        Color color = Color.web(par_color == null ? "#FFFFFF" : par_color);

        model.setText(string);
        model.setNumber(number);
        model.setDate(date);
        model.setColor(color);

        return model;
    }
}
