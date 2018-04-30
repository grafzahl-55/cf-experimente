create sequence hibernate_sequence start with 1 increment by 1;

create table application_instance (
    id bigint not null,
    created_on timestamp,
    description varchar(1024),
    modified_on timestamp,
    version_number bigint,
    application_name varchar(128) not null,
    platform varchar(128) not null,
    service_type varchar(64) not null,
    stage varchar(32) not null,
    primary key (id)
);


create table context_definition (
    id bigint not null,
    created_on timestamp,
    description varchar(1024),
    modified_on timestamp,
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
    created_on timestamp,
    description varchar(1024),
    modified_on timestamp,
    version_number bigint,
    content_type varchar(128),
    role varchar(128),
    source blob,
    primary key (id)
);


create table context_definition_entity_tags (
    context_definition_entity_id bigint not null,
    tags varchar(255)
);



create table context_deployment_entity (
    id bigint not null,
    created_on timestamp,
    description varchar(1024),
    modified_on timestamp,
    version_number bigint,
    application_instance_id bigint not null,
    context_definition_id bigint not null,
    primary key (id)
);

alter table application_instance add constraint appuniqueplatform unique (platform, application_name);

create index i_context_def_type on context_definition (service_type);

alter table context_definition add constraint contextuniquename unique (context_name);


alter table context_definition_sources add constraint ctxuniquerole unique (sources_id);

alter table context_definition_sources add constraint fkctxdefsource foreign key (sources_id) references context_source;


alter table context_definition_sources
    add constraint fkctxsourcedef foreign key (context_definition_entity_id) references context_definition;


alter table context_definition_entity_tags
    add constraint fktegscontext foreign key (context_definition_entity_id) references context_definition;

alter table context_deployment_entity
    add constraint fkappinstdeployment foreign key (application_instance_id) references application_instance;

alter table context_deployment_entity
    add constraint fkctxdefdeployment foreign key (context_definition_id) references context_definition;

