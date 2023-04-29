package itstep.learning.model.server;

import itstep.learning.data.entity.Task;

import java.util.UUID;

public class TaskResp {
    private UUID id;
    private String name;

    public TaskResp( Task entity ) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public TaskResp(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
