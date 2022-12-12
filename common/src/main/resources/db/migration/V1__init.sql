/*
 ********* Таблицы *********
 */
create table if not exists operator
(
    operator_id        bigserial primary key unique
                                    not null,
    operator_email     varchar(128) not null,
    operator_password  varchar(128) not null,
    operator_name      varchar(64)  not null,
    user_uuid          uuid
);

create table if not exists specialization
(
    specialization_id   bigserial primary key unique
                                     not null,
    specialization_name varchar(128) not null

);

create table if not exists operator_specialization
(
    operator_id       bigint references operator (operator_id) on update cascade on delete cascade             not null,
    specialization_id bigint references specialization (specialization_id) on update cascade on delete cascade not null
);

create table if not exists queue
(
    queue_id    uuid primary key not null,
    operator_id bigint references operator (operator_id) on update cascade on delete cascade,
    user_code   varchar(64),
    date_create timestamp        not null,
    date_start  timestamp,
    date_finish timestamp
);

create table if not exists queue_archive
(
    queue_id    uuid primary key                                                                 not null,
    operator_id bigint references operator (operator_id) on update no action on delete no action not null,
    user_code   varchar(64)                                                                      not null,
    date_create timestamp                                                                        not null,
    date_start  timestamp                                                                        not null,
    date_finish timestamp                                                                        not null
);

create table if not exists code
(
    code_id       bigserial primary key unique not null,
    sequence_name varchar(128),
    code_letter   character
);

/*
 ********* Процедуры *********
 */

/*
 Процедура генерации последовательностей. Создает последовательности
 для каждой специализации. В качестве имени последовательности
 используется имя специализации (specialization_name).
 */
create or replace procedure generate_sequence(specialization_name varchar)
    language plpgsql
as
$$
begin
    EXECUTE 'CREATE SEQUENCE "' || specialization_name || '"';
end
$$;