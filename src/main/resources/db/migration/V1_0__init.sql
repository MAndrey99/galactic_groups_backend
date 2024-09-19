create table organization (
    id serial primary key,
    org_name varchar(255) not null
);

create table service_user (
    id bigserial not null primary key,
    full_name varchar(64) not null,
    mail varchar(255) not null
        constraint uk_user_mail unique,
    organization_id integer not null
        constraint fk_user_organization
            references organization(id) on delete cascade,
    password_hash varchar(255) not null,
    role_id smallint not null
);

create index service_user_mail_idx on service_user (mail);
create index student_organization_id_idx on service_user (organization_id);

create table student (
    id bigserial primary key,
    address varchar(255),
    full_name varchar(255) not null,
    group_name varchar(255) not null,
    phone_number varchar(255),
    organization_id integer not null
        constraint fk_student_organization
            references organization(id) on delete cascade
);
