insert into games(id, create_tstmp, start_tstmp, end_tstmp, status, config_id) values ('c10e4868-5d75-423b-b5fb-d2f6b9657d15', now(), now(), now(), 'WAITING', (select id from config where name = 'Крабо'));
insert into players(game_id, player_name, field) values ('c10e4868-5d75-423b-b5fb-d2f6b9657d15', 'isador', '{00000X;00000X,00000X}');

insert into games(id, create_tstmp, start_tstmp, end_tstmp, status, config_id) values ('c10e4868-5d75-423b-b5fb-d2f6b9657d16', now() - INTERVAL '10 min', now(), null, 'PLAYING', (select id from config where name = 'Крабо'));
insert into players(game_id, player_name, field) values ('c10e4868-5d75-423b-b5fb-d2f6b9657d16', 'biba', '{00000X;00000X,00000X}');
insert into players(game_id, player_name, field) values ('c10e4868-5d75-423b-b5fb-d2f6b9657d16', 'boba', '{00000X;00000X,00000X}');

insert into games(id, create_tstmp, start_tstmp, end_tstmp, status, config_id) values ('c10e4868-5d75-423b-b5fb-d2f6b9657d17', now() - INTERVAL '10 min', now() - INTERVAL '5 min', now(), 'FINISHED', (select id from config where name = 'Дефаулт'));
insert into players(game_id, player_name, field, winner) values ('c10e4868-5d75-423b-b5fb-d2f6b9657d17', 'biba2', '{00000X;00000X,00000X}', true);
insert into players(game_id, player_name, field, winner) values ('c10e4868-5d75-423b-b5fb-d2f6b9657d17', 'boba2', '{00000X;00000X,00000X}', null);
