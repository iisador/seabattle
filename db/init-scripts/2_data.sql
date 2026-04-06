-- Initial data
insert into parameters_list(name, descr, type) values('FIELD_SIZE', 'Размер', 'int');
insert into parameters_list(name, descr, type) values('BORDERS_ALLOWED', 'Границы', 'bool');
insert into parameters_list(name, descr, type) values('CORNERS_ALLOWED', 'Углы', 'bool');
insert into parameters_list(name, descr, type) values('SHIPS', 'Корабли', 'string');
insert into parameters_list(name, descr, type) values('GAME_DURATION_MINUTES', 'Длительность игры', 'int');

insert into config(id, name, predefined, ord) values ('60d86085-2200-47d7-a2a7-eba88cbdd237', 'Океан', true, 2);
insert into game_config_params(config_id, name, value) values('60d86085-2200-47d7-a2a7-eba88cbdd237', 'FIELD_SIZE', '10');
insert into game_config_params(config_id, name, value) values('60d86085-2200-47d7-a2a7-eba88cbdd237', 'BORDERS_ALLOWED', 'true');
insert into game_config_params(config_id, name, value) values('60d86085-2200-47d7-a2a7-eba88cbdd237', 'CORNERS_ALLOWED', 'false');
insert into game_config_params(config_id, name, value) values('60d86085-2200-47d7-a2a7-eba88cbdd237', 'SHIPS', '1x4;2x3;3x2;4x1');
insert into game_config_params(config_id, name, value) values('60d86085-2200-47d7-a2a7-eba88cbdd237', 'GAME_DURATION_MINUTES', '5');

insert into config(id, name, predefined, ord) values ('d3bcaefb-a319-4264-894a-725e98449c97', 'Море', true, 1);
insert into game_config_params(config_id, name, value) values('d3bcaefb-a319-4264-894a-725e98449c97', 'FIELD_SIZE', '5');
insert into game_config_params(config_id, name, value) values('d3bcaefb-a319-4264-894a-725e98449c97', 'BORDERS_ALLOWED', 'true');
insert into game_config_params(config_id, name, value) values('d3bcaefb-a319-4264-894a-725e98449c97', 'CORNERS_ALLOWED', 'false');
insert into game_config_params(config_id, name, value) values('d3bcaefb-a319-4264-894a-725e98449c97', 'SHIPS', '1x2;2x1;3x1');
insert into game_config_params(config_id, name, value) values('d3bcaefb-a319-4264-894a-725e98449c97', 'GAME_DURATION_MINUTES', '3');

insert into config(id, name, predefined, ord) values (gen_random_uuid(), 'Малипуська', false, 3);
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Малипуська'), 'FIELD_SIZE', '3');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Малипуська'), 'BORDERS_ALLOWED', 'true');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Малипуська'), 'CORNERS_ALLOWED', 'false');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Малипуська'), 'SHIPS', '1x2');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Малипуська'), 'GAME_DURATION_MINUTES', '1');

insert into config(id, name, predefined, ord) values (gen_random_uuid(), 'ЁКАРНЫЙ БАБАЙ', false, 4);
insert into game_config_params(config_id, name, value) values((select id from config where name = 'ЁКАРНЫЙ БАБАЙ'), 'FIELD_SIZE', '20');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'ЁКАРНЫЙ БАБАЙ'), 'BORDERS_ALLOWED', 'true');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'ЁКАРНЫЙ БАБАЙ'), 'CORNERS_ALLOWED', 'false');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'ЁКАРНЫЙ БАБАЙ'), 'SHIPS', '1x1');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'ЁКАРНЫЙ БАБАЙ'), 'GAME_DURATION_MINUTES', '10');

insert into users(name, password, role) values ('nastytasya','$2a$10$M/PNZaH3f3TUPMawRQl2PuDq5t8bMNICyg9NdbqH/k8gWC08Z5OmG','admin');
