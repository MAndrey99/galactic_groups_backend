INSERT INTO ORGANIZATION (ID, ORG_NAME)
VALUES (1, 'LETI');

INSERT INTO SERVICE_USER (ID, FULL_NAME, MAIL, ORGANIZATION_ID, ROLE_ID, PASSWORD_HASH)
VALUES (1, 'leti owner', 'owner@leti.ru', 1, 0, '$2a$12$GV3SYScJkoHqMTz8CA8Ia.3hUDCInP9/P5/pAmonuhTqwRTga/MZu');

INSERT INTO SERVICE_USER (ID, FULL_NAME, MAIL, ORGANIZATION_ID, ROLE_ID, PASSWORD_HASH)
VALUES (2, 'leti staff', 'user@leti.ru', 1, 1, '$2a$12$8nEyWniUBZyDT.l3HmYt8.mSnRpLbqL7hVwo1GS6BjBnNCrNuQTH2');

INSERT INTO STUDENT (id, address, full_name, group_name, phone_number, organization_id)
VALUES (1, 'city 1, st.2', 'Maslov Andrey', '7375', '88005553535', 1);

INSERT INTO STUDENT (id, full_name, group_name, organization_id)
VALUES (2, 'Dmitriy Bond', '7375', 1);

INSERT INTO STUDENT (id, full_name, group_name, phone_number, organization_id)
VALUES (3, 'Nikita', '7374', '88005553535', 1);
