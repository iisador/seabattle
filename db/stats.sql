with dates as (select '2026-04-28 00:00'::timestamptz as ds,
                      '2026-06-01 23:59'::timestamptz as de),
     game_creators AS (SELECT DISTINCT ON (p.game_id) p.game_id,
                                                      p.player_name
                       FROM players p
                       where du between (select ds from dates) and (select de from dates)
                       ORDER BY p.game_id, p.du ASC)
select (select count(*)
        from games
        where create_tstmp between (select ds from dates) and (select de from dates)) as "Всего игр создано",
       (select count(*)
        from games
        where create_tstmp between (select ds from dates) and (select de from dates)
          and start_tstmp is not null
          and end_tstmp is not null)                                                  as "Завершенных игр",
       (select count(*)
        from games
        where create_tstmp between (select ds from dates) and (select de from dates)
          and start_tstmp is null
          and end_tstmp is null)                                                      as "Несыгранных игр (просто создали и вышли)",
       (SELECT player_name || ' (' || COUNT(*) || ' шт)'
        FROM game_creators
        GROUP BY player_name
        ORDER BY COUNT(*) DESC
        LIMIT 1)                                                                      as "Больше всего игр создал игрок",
       (SELECT COUNT(*)
        FROM game_log
        WHERE command::jsonb ->> 'type' = 'FIRE'
          and tstmp between (select ds from dates) and (select de from dates))        as "Всего выстрелов совершено",
       (SELECT DATE(tstmp) || ' (' || COUNT(*) || ' штук)'
        FROM game_log
        WHERE command::jsonb ->> 'type' = 'FIRE'
          and tstmp between (select ds from dates) and (select de from dates)
        GROUP BY DATE(tstmp)
        ORDER BY count(*) DESC
        LIMIT 1)                                                                      as "Самый опасный день (больше всего выстрелов было в...)",
       (SELECT command::jsonb ->> 'playerName' || ' (' || COUNT(*) || ' штук)'
        FROM game_log
        WHERE command::jsonb ->> 'type' = 'FIRE'
          and tstmp between (select ds from dates) and (select de from dates)
        GROUP BY command::jsonb ->> 'playerName'
        ORDER BY count(*) DESC
        LIMIT 1)                                                                      as "Больше всего выстрелов совершил игрок",
       (SELECT player_name || ' (' || COUNT(*) || ' штук)'
        FROM players
        WHERE winner = true
          and du between (select ds from dates) and (select de from dates)
        GROUP BY player_name
        ORDER BY COUNT(*) DESC
        LIMIT 1)                                                                      as "Больше всего побед одержал игрок";

--- Топ по созданию игр
with dates as (select '2026-04-28 00:00'::timestamptz as ds,
                      '2026-06-01 23:59'::timestamptz as de),
     game_creators AS (SELECT DISTINCT ON (p.game_id) p.game_id,
                                                      p.player_name
                       FROM players p
                       where du between (select ds from dates) and (select de from dates)
                       ORDER BY p.game_id, p.du ASC),
     games_count AS (SELECT player_name, COUNT(*) as c
                     FROM game_creators
                     GROUP BY player_name)
select u.name, gc.c
from users u
         left join games_count gc on u.name = gc.player_name
ORDER BY c DESC NULLS LAST, u.name;
