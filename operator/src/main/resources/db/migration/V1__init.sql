create table if not exists operator (
    operator_id bigserial primary key 
                          unique 
                          not null,
    operator_email varchar(128) not null,
    operator_password varchar(128) not null,
    operator_name varchar(64) not null,
    user_uuid varchar(64),
    operator_is_active boolean not null
);

create table if not exists specialization (
    specialization_id bigserial primary key 
                                unique 
                                not null,
    specialization_name varchar(128) not null
    
);

create table if not exists operator_specialization (
    operator_id bigint references operator(operator_id) on update cascade 
                                                        on delete cascade 
                                                        not null,
    specialization_id bigint references specialization(specialization_id) on update cascade 
                                                                          not null
);

create table if not exists queue (
    queue_id bigserial primary key
                       unique 
                       not null,
    operator_id bigint references operator (operator_id) on update cascade 
                                                         on delete cascade 
                                                         not null,
    date_create date not null,
    date_start date,
    date_finish date
);

create table if not exists queue_archive (
    queue_id bigserial primary key 
                       unique 
                       not null,
    operator_id bigint references operator (operator_id) on update no action
                                                         on delete no action 
                                                         not null,
    date_create date not null,
    date_start date,
    date_finish date
);

