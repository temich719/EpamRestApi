create schema giftCertificates;

-- alter table user add column creationEntityDate DATETIME after userName;
-- alter table user add column modificationEntityDate DATETIME after creationEntityDate;
-- alter table user add column createdEntityBy varchar(50) after modificationEntityDate;
-- alter table user add column modifiedEntityBy varchar(50) after createdEntityBy;

create table gift_certificate
(
    id               int auto_increment primary key,
    name             varchar(50)  not null,
    description      varchar(100) not null,
    price            varchar(20)  not null,
    duration         varchar(30)  not null,
    create_date      varchar(100) not null,
    last_update_date varchar(100) not null,
    creationEntityDate DATETIME,
    modificationEntityDate DATETIME,
    createdEntityBy varchar(50),
    modifiedEntityBy varchar(50)
);

create table tag
(
    id   int auto_increment primary key,
    name varchar(50) not null unique,
    creationEntityDate DATETIME,
    modificationEntityDate DATETIME,
    createdEntityBy varchar(50),
    modifiedEntityBy varchar(50)
);

create table certificates_and_tags
(
    gift_certificate_id int not null,
    tag_id              int not null,
    foreign key (gift_certificate_id) references gift_certificate (id),
    foreign key (tag_id) references tag (id)
);