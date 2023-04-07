INSERT INTO zanbanzero.user(username, password, roles) VALUES ('user', '$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_USER');
INSERT INTO zanbanzero.user(username, password, roles) VALUES ('user1', '$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_USER');

INSERT INTO zanbanzero.manager(password, roles, username) values ('$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_MANAGER', 'manager');

INSERT INTO zanbanzero.menu(cost, name, sold) VALUES (2000, '치킨너겟', true);
INSERT INTO zanbanzero.menu(cost, name, sold) VALUES (45000, '배고픈 무지', true);
INSERT INTO zanbanzero.menu(cost, name, sold) VALUES (12345, '육회', true);

INSERT INTO zanbanzero.menu_info(menu_id, details, info) VALUES (1, "하림이 만든 치킨너겟", "닭고기 알레르기");
INSERT INTO zanbanzero.menu_info(menu_id, details, info) VALUES (2, "무지개", "카카오 빈");
INSERT INTO zanbanzero.menu_info(menu_id, details, info) VALUES (3, "신선육회", "소고기");

INSERT INTO zanbanzero.store(manager_id, name, lat, lon) VALUES (1, "한밭대",320, 200);
INSERT INTO zanbanzero.store_state(store_manager_id, congestion) VALUES(1, 50);

INSERT INTO zanbanzero.leftover_show(store_manager_id, leftover, updated) VALUES(1, 52.4, "22");

INSERT INTO zanbanzero.planner(date, menus, kcal) VALUES ('2023-03-02 10:00:00.000000000', '닭발 콩나무물무침 계란말이', 362);
INSERT INTO zanbanzero.planner(date, menus, kcal) VALUES ('2023-04-02 10:00:00.000000000', '피자', 1362);
INSERT INTO zanbanzero.planner(date, menus, kcal) VALUES ('2023-04-03 10:00:00.000000000', '치킨너겟 케찹', 2362);
INSERT INTO zanbanzero.planner(date, menus, kcal) VALUES ('2023-04-04 10:00:00.000000000', '닭가슴살 훈제', 32);
INSERT INTO zanbanzero.planner(date, menus, kcal) VALUES ('2023-04-05 10:00:00.000000000', '건강 밀키트', 33);
INSERT INTO zanbanzero.planner(date, menus, kcal) VALUES ('2023-04-06 10:00:00.000000000', '콩나물죽 김치볶음', 3162);
INSERT INTO zanbanzero.planner(date, menus, kcal) VALUES ('2023-04-07 10:00:00.000000000', '지옥의 삼겹살', 23362);
INSERT INTO zanbanzero.planner(date, menus, kcal) VALUES ('2023-04-22 10:00:00.000000000', '강낭콩볶음 훈제로스트치킨', 1362);