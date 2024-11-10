CREATE DATABASE IF NOT EXISTS `math`;
USE `math`;

CREATE TABLE `friendship` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `friendId` int NOT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_FRIEND_USER_idx` (`userId`),
  KEY `FK_FRIEND_USER_FRIEND_idx` (`friendId`),
  CONSTRAINT `FK_FRIEND_USER` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_FRIEND_USER_FRIEND` FOREIGN KEY (`friendId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `gamehistory` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `player` (
  `id` int NOT NULL AUTO_INCREMENT,
  `gameId` int NOT NULL,
  `rank` int DEFAULT NULL,
  `score` int DEFAULT NULL,
  `userId` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_PLAYER_RANK_idx` (`userId`),
  KEY `FK_PLAYER_RANK_idx1` (`gameId`),
  CONSTRAINT `FK_PLAYER_GAME` FOREIGN KEY (`gameId`) REFERENCES `gamehistory` (`id`),
  CONSTRAINT `FK_PLAYER_USER` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `rank` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `score` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_RANK_USER_idx` (`userId`),
  CONSTRAINT `FK_RANK_USER` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  `score` int DEFAULT '0',
  `gamesPlayed` int DEFAULT '0',
  `rank` int DEFAULT '0',
  `winRate` float DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
