package itstep.learning.data.entity;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private int status;
    private UUID idAuthor;
    private UUID idTeam;
    private Date createDate;
    private Date deadline;
    private int priority;
    private static final SimpleDateFormat formFormat =
            new SimpleDateFormat( "yyyy-mm-dd" );

    public Task() {

    }
    public Task(ResultSet resultSet ) throws RuntimeException {
        try {
            setId(UUID.fromString( resultSet.getString("id") ) );
            setName( resultSet.getString("name") );
            setStatus( resultSet.getInt("status") );
            setIdAuthor( UUID.fromString( resultSet.getString("id_user") ) );
            setIdTeam( UUID.fromString( resultSet.getString("id_team") ) );
            setCreateDate( resultSet.getDate("created_dt") );
            setDeadline( resultSet.getDate("deadline") );
            setPriority( resultSet.getInt("priority") );
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    // region Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UUID getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(UUID idAuthor) {
        this.idAuthor = idAuthor;
    }

    public UUID getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(UUID idTeam) {
        this.idTeam = idTeam;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate( String createDate ) throws ParseException {
        this.createDate = formFormat.parse( createDate );
    }

    public void setCreateDate( Date createDate ) {
        this.createDate = createDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    // endregion
}
/*
CREATE TABLE `tasks` (
    `id` char(36) NOT NULL COMMENT 'UUID',
    `name` varchar(64) NOT NULL,
    `status` int DEFAULT 0,
    `id_user` char(36) NOT NULL COMMENT 'Author',
    `id_team` char(36) NOT NULL,
    `created_dt` datetime DEFAULT CURRENT_TIMESTAMP,
    `deadline` datetime,
    `priority` TINYINT default 0,
    PRIMARY KEY (`id`))
    ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
