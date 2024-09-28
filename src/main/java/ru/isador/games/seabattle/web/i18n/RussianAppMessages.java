package ru.isador.games.seabattle.web.i18n;

import io.quarkus.qute.i18n.Localized;
import io.quarkus.qute.i18n.Message;

@Localized("ru")
public interface RussianAppMessages extends AppMessages {

    @Message("Морской бой")
    String game_title();

    @Message("Дороу! Пиши логин/пароль и проходи")
    String greeting();

    @Message("Поехали!")
    String greeting_submit();

    @Message("Оу. Такой юнга уже играет. Выбери какой-нибудь другой ник")
    String greeting_player_exists_error();

    @Message("Добро пожаловать в лобби, {playerName}.")
    String lobby_player_name(String playerName);

    @Message("Логин сюда")
    String greeting_name_label();
}
