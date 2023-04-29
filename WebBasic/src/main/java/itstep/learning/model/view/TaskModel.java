package itstep.learning.model.view;

import itstep.learning.data.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class TaskModel {
    private String name;
    private User Author;
    private UUID idTeam;
    private Date deadline;
    private int priority;
    public static final SimpleDateFormat formFormat =
            new SimpleDateFormat( "yyyy-mm-dd" );

    public TaskModel() {

    }

    public UUID getIdTeam() {
        return idTeam;
    }

    public void setIdTeam( String idTeam ) throws IllegalArgumentException {
        this.idTeam = UUID.fromString( idTeam );
    }
    public void setIdTeam( UUID idTeam ) {
        this.idTeam = idTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline( String deadline ) throws ParseException {
        this.deadline = formFormat.parse( deadline );
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority( String priority ) throws NumberFormatException {
        this.priority = Integer.parseInt( priority );
    }
    public void setPriority( int priority ) {
        this.priority = priority;
    }

    public User getAuthor() {
        return Author;
    }

    public void setAuthor(User author) {
        Author = author;
    }

    @Override
    public String toString() {
        return "ClientTask{" +
                "name='" + name + '\'' +
                ", deadline=" + formFormat.format( deadline ) +
                ", priority=" + priority +
                '}';
    }
}
