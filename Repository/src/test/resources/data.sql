insert into gift_certificate(name, description, price, duration, create_date, last_update_date)
values ('SportMaster', 'for winter clothes', '200$', '5 days', '2022-04-05T16:51Z', '2022-04-05T23:36Z'),
       ('DoDo Pizza', 'pizza', '50$', '20 days', '2022-04-11T10:18Z', '2022-04-11T10:18Z'),
       ('OZ', 'books', '100$', '31 days', '2022-04-05T23:41Z', '2022-04-05T23:41Z');

insert into tag(name)
values ('sport'),
       ('food'),
       ('development'),
       ('books');

insert into certificates_and_tags(gift_certificate_id, tag_id)
values (1, 1),
       (2, 2),
       (3, 3),
       (3, 4);
