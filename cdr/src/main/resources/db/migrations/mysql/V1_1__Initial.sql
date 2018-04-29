create table application_instance(
    id bigint not null,
    created_on datetime,
    description varchar(1024),
    modified_on datetime,
    version_number bigint,
    application_name varchar(128) not null,
    platform varchar(128) not null,
    service_type varchar(64) not null,
    stage varchar(32) not null,
    primary key (id)
);

create table context_definition (
    id bigint not null,
    created_on datetime,
    description varchar(1024),
    modified_on datetime,
    version_number bigint,
    context_name varchar(255) not null,
    service_type varchar(64) not null,
    primary key (id)
);

create table context_definition_sources (
    context_definition_entity_id bigint not null,
    sources_id bigint not null
);

create table context_source (
    id bigint not null,
    created_on datetime,
    description varchar(1024),
    modified_on datetime,
    version_number bigint,
    content_type varchar(128),
    role varchar(128),
    source longblob,
    primary key (id)
);

create table context_definition_entity_tags (
    context_definition_entity_id bigint not null,
    tags varchar(255)
);

create table context_deployment_entity (
    id bigint not null,
    created_on datetime,
    description varchar(1024),
    modified_on datetime,
    version_number bigint,
    application_instance_id bigint not null,
    context_definition_id bigint not null,
    primary key (id)
);

create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );

alter table application_instance
    add constraint UKeivr35gbregfhy07gy64cffn0 unique (platform, application_name);

create index IDX9migm3w53oafpdmvt89st6wq4 on context_definition (service_type);

alter table context_definition add constraint UKbawi966wv6ggpoydb7q1yjtrh unique (context_name);

alter table context_definition_sources add constraint UK_jsxht2vd738ync2nriukddaut unique (sources_id);

alter table context_definition_sources add constraint FKmjhi8qvcof0onwvcg65owb891
    foreign key (sources_id) references context_source (id);


alter table context_definition_sources
    add constraint FK6omrk80uhvlyxjm31yuuumbl4 foreign key (context_definition_entity_id) references context_definition (id);

alter table context_definition_entity_tags
    add constraint FK29al5nskl27s41lhuqeoa714s foreign key (context_definition_entity_id) references context_definition (id);

alter table context_deployment_entity
    add constraint FK6t3fk6uxi25dixum4uaje9w4a foreign key (application_instance_id) references application_instance (id);
alter table context_deployment_entity
    add constraint FK6vdsqr7i76netvcx73gu9vl04 foreign key (context_definition_id) references context_definition (id);
