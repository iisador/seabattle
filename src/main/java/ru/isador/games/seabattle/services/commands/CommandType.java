package ru.isador.games.seabattle.services.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CommandType {
    JOINED_GAME,
    FIRE,
    CHAT_MESSAGE,
    VIEWER_JOINED;

    public static CommandType getType(String str) {
        Matcher m = Pattern.compile("\"type\":\\s*\"(\\w+)\"").matcher(str);
        if (m.find()) {
            return Enum.valueOf(CommandType.class, m.group(1));
        }
        return null;
    }
}
