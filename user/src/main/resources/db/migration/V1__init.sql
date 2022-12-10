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
    user_uuid          uuid         not null,
    operator_is_active boolean      not null
);

create table if not exists specialization
(
    specialization_id   bigserial primary key unique
                                     not null,
    specialization_name varchar(128) not null

);

create table if not exists operator_specialization
(
    operator_id       bigint references operator (operator_id) on update cascade on delete cascade not null,
    specialization_id bigint references specialization (specialization_id) on update cascade       not null
);

create table if not exists queue
(
    queue_id    uuid primary key                                                             not null,
    operator_id bigint references operator (operator_id) on update cascade on delete cascade not null,
    date_create date                                                                         not null,
    date_start  date,
    date_finish date
);

create table if not exists queue_archive
(
    queue_id    uuid primary key                                                                 not null,
    operator_id bigint references operator (operator_id) on update no action on delete no action not null,
    date_create date                                                                             not null,
    date_start  date,
    date_finish date
);

create table if not exists code_mapper
(
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
create or replace procedure generate_sequences()
    language plpgsql
as
$$
declare
    l_specializations     varchar[];
    l_specialization_name varchar;
begin
    l_specializations := ARRAY(
            SELECT specialization_name
            FROM specialization
        );
    raise debug '%', l_specializations;

    foreach l_specialization_name in array l_specializations
        loop
            EXECUTE 'CREATE SEQUENCE "' || l_specialization_name || '"';
        end loop;
end
$$;