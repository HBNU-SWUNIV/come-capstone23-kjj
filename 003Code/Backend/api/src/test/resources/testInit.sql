delete from menu_food;
delete from menu_info;
delete from menu;

alter table menu_food alter column id restart with 1;
alter table menu_info alter column id restart with 1;
alter table menu alter column id restart with 1;

insert into menu_food(food, name) values('test food', 'test food name');
insert into menu_info(details, info) values('test menu details', 'test menu info');
insert into menu(cost, image, menu_food_id, menu_info_id, name, sold, use_planner)
values(3000, 'test menu image', 1, 1, 'test Menu name', true, false);