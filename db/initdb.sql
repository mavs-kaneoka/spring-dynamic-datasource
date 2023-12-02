-- ***********************
-- system
-- ***********************
CREATE DATABASE IF NOT EXISTS `system`;
GRANT ALL ON `system`.* TO 'mysql_user'@'%';
USE `system`;

-- connections
CREATE TABLE `connections` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `tenant_id` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `database` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `connections`
(`tenant_id`, `database`, `username`, `password`, `created_at`, `updated_at`)
VALUES('tenant01', 'jdbc:mysql://db/tenant01', 'mysql_user', 'mysql_password', '2023-12-17 12:00:00', NULL);

INSERT INTO `connections`
(`tenant_id`, `database`, `username`, `password`, `created_at`, `updated_at`)
VALUES('tenant02', 'jdbc:mysql://db/tenant02', 'mysql_user', 'mysql_password', '2023-12-17 12:00:00', NULL);

-- ***********************
-- tenant01
-- ***********************
CREATE DATABASE IF NOT EXISTS `tenant01`;
GRANT ALL ON `tenant01`.* TO 'mysql_user'@'%';
USE `tenant01`;

-- users
CREATE TABLE `users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `users`
(`name`, `created_at`, `updated_at`)
VALUES('tenant01ユーザー1', '2023-12-17 12:00:00', NULL);

INSERT INTO `users`
(`name`, `created_at`, `updated_at`)
VALUES('tenant01ユーザー2', '2023-12-17 12:00:00', NULL);

-- ***********************
-- tenant02
-- ***********************
CREATE DATABASE IF NOT EXISTS `tenant02`;
GRANT ALL ON `tenant02`.* TO 'mysql_user'@'%';
USE `tenant02`;

-- users
CREATE TABLE `users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `users`
(`name`, `created_at`, `updated_at`)
VALUES('tenant02ユーザー', '2023-12-17 12:00:00', NULL);