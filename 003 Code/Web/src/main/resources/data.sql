insert into zanbanzero.user(username, password, roles) values ('user', '$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_USER');
insert into zanbanzero.user(username, password, roles) values ('user1', '$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_USER');

insert into zanbanzero.manager(password, roles, username) values ('$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_MANAGER', 'manager');

insert into zanbanzero.menu(cost, info, name) values (2000, '두유, 복숭아', '치킨너겟');
insert into zanbanzero.menu(cost, info, name) values (45000, '감자', '배고픈 무지');
insert into zanbanzero.menu(cost, info, name) values (12345, '삼겹살 알레르기', '육회');

insert into zanbanzero.store(manager_id, lat, lon) values (1, 320, 200);
insert into zanbanzero.store_state(store_manager_id, congestion) values(1, 50);

insert into zanbanzero.leftover_show(store_manager_id, leftover, updated) values(1, 52.4, "22");