INSERT INTO zanbanzero.user(username, password, roles) VALUES ('user', '$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_USER');
INSERT INTO zanbanzero.user(username, password, roles) VALUES ('user1', '$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_USER');

INSERT INTO zanbanzero.user_policy(user_id, monday, tuesday, wednesday, thursday, friday, default_menu) VALUES (1, 1, 0, 1, 0, 1, 1);
INSERT INTO zanbanzero.user_policy(user_id, monday, tuesday, wednesday, thursday, friday, default_menu) VALUES (2, 1, 1, 1, 1, 1, 2);

INSERT INTO zanbanzero.manager(password, roles, username) values ('$2a$10$rb7fhQHdtxNf7RgTJVlERePH99.BqB6/i5LE61leYMh0vGOQoGbIC', 'ROLE_MANAGER', 'manager');

INSERT INTO zanbanzero.menu(cost, name, sold) VALUES (5000, '백반정식', true);
INSERT INTO zanbanzero.menu(cost, name, sold) VALUES (2000, '치킨너겟', true);
INSERT INTO zanbanzero.menu(cost, name, sold) VALUES (45000, '배고픈 무지', true);
INSERT INTO zanbanzero.menu(cost, name, sold) VALUES (12345, '육회', true);

INSERT INTO zanbanzero.menu_info(menu_id, details, info) VALUES (1, "매일 바뀌는 백반정식", "돼지고기 알레르기");
INSERT INTO zanbanzero.menu_info(menu_id, details, info) VALUES (2, "하림이 만든 치킨너겟", "닭고기 알레르기");
INSERT INTO zanbanzero.menu_info(menu_id, details, info) VALUES (3, "무지개", "카카오 빈");
INSERT INTO zanbanzero.menu_info(menu_id, details, info) VALUES (4, "신선육회", "소고기");

INSERT INTO zanbanzero.leftover_show(leftover, updated) VALUES(52.4, '2023-03-02 10:00:00.000000000');

INSERT INTO zanbanzero.orders(user_id, order_date, updated, recognize) VALUES (null, '2023-04-08', '2023-04-09 10:00:00.000000000', 0);
INSERT INTO zanbanzero.orders(user_id, order_date, updated, recognize) VALUES (null, '2023-04-08', '2023-04-09 10:00:00.000000000', 0);
INSERT INTO zanbanzero.orders(user_id, order_date, updated, recognize) VALUES (null, '2023-04-09', '2023-04-09 10:00:00.000000000', 0);
INSERT INTO zanbanzero.orders(user_id, order_date, updated, recognize) VALUES (null, '2023-04-09', '2023-04-09 10:00:00.000000000', 0);

INSERT INTO zanbanzero.store_state(date, congestion, today) VALUES ('2023-03-03', null, 234);
INSERT INTO zanbanzero.store_state(date, congestion, today) VALUES ('2023-03-02', null, 123);
INSERT INTO zanbanzero.store_state(date, congestion, today) VALUES ('2023-03-04', null, 146);

INSERT INTO zanbanzero.planner(date, menus, off) VALUES ('2023-03-02', '닭발 콩나무물무침 계란말이', false);
INSERT INTO zanbanzero.planner(date, menus, off) VALUES ('2023-03-03', '닭발 잭과콩나무', false);
INSERT INTO zanbanzero.planner(date, menus, off) VALUES ('2023-04-02', '피자', false);
INSERT INTO zanbanzero.planner(date, menus, off) VALUES ('2023-04-03', '치킨너겟 케찹', false);
INSERT INTO zanbanzero.planner(date, menus, off) VALUES ('2023-04-04', '닭가슴살 훈제', false);
INSERT INTO zanbanzero.planner(date, menus, off) VALUES ('2023-04-05', '건강 밀키트', false);
INSERT INTO zanbanzero.planner(date, menus, off) VALUES ('2023-04-06', '콩나물죽 김치볶음', false);
INSERT INTO zanbanzero.planner(date, menus, off) VALUES ('2023-04-07', '지옥의 삼겹살', false);
INSERT INTO zanbanzero.planner(date, menus, off) VALUES ('2023-04-22', '강낭콩볶음 훈제로스트치킨', false);