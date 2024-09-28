package ru.isador.games.seabattle.services.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResponseGameChanged extends Response {

    private final String createTime;
    private final String players;
    private final String configName;
    private final String status;
    private final String id;

    public ResponseGameChanged(LocalDateTime createTime, String players, String configName, String status, String id) {
        super(ResponseType.GAME_CHANGED);
        this.createTime = createTime.format(DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss"));;
        this.players = players;
        this.configName = configName;
        this.status = status;
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getPlayers() {
        return players;
    }

    public String getConfigName() {
        return configName;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }
}
