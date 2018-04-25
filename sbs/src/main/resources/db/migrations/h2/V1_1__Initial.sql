create sequence hibernate_sequence start with 1 increment by 1;

create table file_info (
    id bigint not null, 
    content_length bigint not null, 
    content_type varchar(128), 
    name varchar(255), 
    folder_id bigint, 
    primary key (id)
);

create table folder_info (
    id bigint not null, 
    name varchar(255), 
    primary key (id)

);

create table tag_value (
    id binary not null, 
    file_id bigint, 
    tag_name varchar(255), 
    tag_value varchar(4096), 
    primary key (id)
);

alter table folder_info add constraint UKk1w5bk8a1xm51skuke3oijxb6 unique (name);
create index IDXlb3mcaa34u8xgft53dscisvw9 on tag_value (file_id);
create index IDXoq3295s1t6hgcy6n4bijh7aoy on tag_value (tag_name, tag_value);
alter table file_info add constraint FKhq0xlsjbm8xdo0qt9ow7oloos foreign key (folder_id) references folder_info;

