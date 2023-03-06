insert into zanbanzero.user(username, password, roles) values ('user', '$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_USER');
insert into zanbanzero.user(username, password, roles) values ('user1', '$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_USER');

insert into zanbanzero.manager(password, roles, username) values ('$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_MANAGER', 'manager');

insert into zanbanzero.menu(cost, name, sold) values (2000, '치킨너겟', true);
insert into zanbanzero.menu(cost, name, sold) values (45000, '배고픈 무지', true);
insert into zanbanzero.menu(cost, name, sold) values (12345, '육회', true);

insert into zanbanzero.menu_info(menu_id, details, info) values (1, "하림이 만든 치킨너겟", "닭고기 알레르기");
insert into zanbanzero.menu_info(menu_id, details, info) values (2, "무지개", "카카오 빈");
insert into zanbanzero.menu_info(menu_id, details, info) values (3, "신선육회", "소고기");

insert into zanbanzero.store(manager_id, lat, lon) values (1, 320, 200);
insert into zanbanzero.store_state(store_manager_id, congestion) values(1, 50);

insert into zanbanzero.leftover_show(store_manager_id, leftover, updated) values(1, 52.4, "22");