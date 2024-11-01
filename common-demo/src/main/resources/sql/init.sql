-- Drop Schema and all of its tables if it exists
DROP SCHEMA IF EXISTS `common-utils`;

-- Create new Schema
CREATE SCHEMA `common-utils` COLLATE utf8mb4_0900_ai_ci;

-- Use the new Schema
USE `common-utils`;

-- Drop tables if they exist
DROP TABLE IF EXISTS t_refresh_token;
DROP TABLE IF EXISTS t_user_roles;
DROP TABLE IF EXISTS t_user;
DROP TABLE IF EXISTS t_role;

-- Create tables
CREATE TABLE t_refresh_token
(
    id          BIGINT AUTO_INCREMENT COMMENT 'The unique identifier for the RefreshToken entity'
        PRIMARY KEY,
    user_id     BIGINT       NOT NULL COMMENT 'Represents the associated User entity for this RefreshToken',
    token       VARCHAR(255) NOT NULL COMMENT 'Represents the refresh token associated with a user',
    expire_date TIMESTAMP    NOT NULL COMMENT 'Represents the expiration date and time of the RefreshToken',
    CONSTRAINT token
        UNIQUE (token)
)
    COMMENT 'Table for the RefreshToken entity';

CREATE TABLE t_role
(
    id   INT AUTO_INCREMENT COMMENT 'The unique identifier for a Role entity'
        PRIMARY KEY,
    name TINYINT NULL COMMENT 'The ordinal value of the role; uses the ordinal values of RoleEnum'
)
    COMMENT 'Table for the Role entity';

CREATE TABLE t_user
(
    id       BIGINT AUTO_INCREMENT COMMENT 'The unique identifier for the User entity'
        PRIMARY KEY,
    username VARCHAR(255) NOT NULL COMMENT 'The unique username for the User entity',
    email    VARCHAR(255) NOT NULL COMMENT 'The email address of the User',
    password VARCHAR(255) NOT NULL COMMENT 'Stores the password for the User entity',
    CONSTRAINT email
        UNIQUE (email),
    CONSTRAINT username
        UNIQUE (username)
)
    COMMENT 'Table for the User entity';

CREATE TABLE t_user_roles
(
    user_id BIGINT NOT NULL COMMENT 'Foreign key to User entity',
    role_id INT    NOT NULL COMMENT 'Foreign key to Role entity',
    PRIMARY KEY (user_id, role_id)
)
    COMMENT 'Join table to link users with their roles';

-- Batch insert data
INSERT INTO t_role (id, name) VALUES
                                  (1, 0),
                                  (2, 1),
                                  (3, 2);