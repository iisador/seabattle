package ru.isador.games.seabattle.web.i18n;

import io.quarkus.qute.i18n.Message;
import io.quarkus.qute.i18n.MessageBundle;

@MessageBundle
public interface AppMessages {

    @Message("Сиашип баттле")
    String game_title();

    @Message("Хеллоу стрэнджер. Вот из йоур нэйм?")
    String greeting();

    @Message("Субмит")
    String greeting_submit();

    @Message("Сорри пэл, зис узернейм алреди плэинг. Чуз анотхер")
    String greeting_player_exists_error();

    @Message("Соу {playerName}, зис из лобби")
    String lobby_player_name(String playerName);

    @Message("Тайп йоур нэйм хере, литтл бой")
    String greeting_name_label();
}
