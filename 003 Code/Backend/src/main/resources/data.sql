INSERT INTO zanbanzero.user(username, password, roles) VALUES ('user', '$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_USER');
INSERT INTO zanbanzero.user(username, password, roles) VALUES ('user1', '$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_USER');

INSERT INTO zanbanzero.manager(password, roles, username) values ('$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_MANAGER', 'manager');

INSERT INTO zanbanzero.menu(cost, name, sold) VALUES (2000, '치킨너겟', true);
INSERT INTO zanbanzero.menu(cost, name, sold) VALUES (45000, '배고픈 무지', true);
INSERT INTO zanbanzero.menu(cost, name, sold) VALUES (12345, '육회', true);

INSERT INTO zanbanzero.menu_info(menu_id, details, info) VALUES (1, "하림이 만든 치킨너겟", "닭고기 알레르기");
INSERT INTO zanbanzero.menu_info(menu_id, details, info) VALUES (2, "무지개", "카카오 빈");
INSERT INTO zanbanzero.menu_info(menu_id, details, info) VALUES (3, "신선육회", "소고기");

INSERT INTO zanbanzero.store(manager_id, lat, lon) VALUES (1, 320, 200);
INSERT INTO zanbanzero.store_state(store_manager_id, congestion) VALUES(1, 50);

INSERT INTO zanbanzero.leftover_show(store_manager_id, leftover, updated) VALUES(1, 52.4, "22");