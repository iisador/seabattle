-- Initial data
insert into parameters_list(name, descr, type) values('FIELD_SIZE', 'Размер', 'int');
insert into parameters_list(name, descr, type) values('BORDERS_ALLOWED', 'Границы', 'bool');
insert into parameters_list(name, descr, type) values('CORNERS_ALLOWED', 'Углы', 'bool');
insert into parameters_list(name, descr, type) values('SHIPS', 'Корабли', 'string');
insert into parameters_list(name, descr, type) values('GAME_DURATION_MINUTES', 'Длительность игры', 'int');

insert into config(id, name, predefined, ord) values (gen_random_uuid(), 'Senior', true, 2);
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Senior'), 'FIELD_SIZE', '10');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Senior'), 'BORDERS_ALLOWED', 'true');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Senior'), 'CORNERS_ALLOWED', 'false');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Senior'), 'SHIPS', '1x4;2x3;3x2;4x1');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Senior'), 'GAME_DURATION_MINUTES', '10');

insert into config(id, name, predefined, ord) values (gen_random_uuid(), 'Middle', true, 1);
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Middle'), 'FIELD_SIZE', '5');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Middle'), 'BORDERS_ALLOWED', 'true');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Middle'), 'CORNERS_ALLOWED', 'false');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Middle'), 'SHIPS', '1x2;2x1;3x1');
insert into game_config_params(config_id, name, value) values((select id from config where name = 'Middle'), 'GAME_DURATION_MINUTES', '5');

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
