--liquibase formatted sql

--changeset trainee:init
CREATE TABLE IF NOT EXISTS guest
(
    id                uuid                  NOT NULL    PRIMARY KEY,
    surname           varchar(255)          NOT NULL,
    first_name        varchar(255)          NOT NULL,
    patronymic        varchar(255),
    birth_date        date                  NOT NULL,
    phone             varchar(20)           NOT NULL
);



CREATE TABLE IF NOT EXISTS room
(
    id              uuid                NOT NULL    PRIMARY KEY,
    floor           integer             NOT NULL,
    room_number     integer             NOT NULL,
    capacity        integer             NOT NULL
);



CREATE TABLE IF NOT EXISTS booking
(
    id              uuid                NOT NULL    PRIMARY KEY,
    check_in        timestamptz         NOT NULL,
    check_out       timestamptz         NOT NULL,
    room_id         uuid                NOT NULL    REFERENCES room(id)
);



CREATE TABLE IF NOT EXISTS booking_guest
(
    booking_id      uuid      NOT NULL    REFERENCES booking(id) ON DELETE CASCADE,
    guest_id        uuid      NOT NULL    REFERENCES guest(id) ON DELETE CASCADE,
    PRIMARY KEY (booking_id, guest_id)
);