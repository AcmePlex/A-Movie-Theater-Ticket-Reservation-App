/*
 Navicat Premium Data Transfer

 Source Server         : mysql1
 Source Server Type    : MySQL
 Source Server Version : 80040
 Source Host           : localhost:3306
 Source Schema         : acmeplex

 Target Server Type    : MySQL
 Target Server Version : 80040
 File Encoding         : 65001

 Date: 14/11/2024 20:08:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for credit_card
-- ----------------------------
DROP TABLE IF EXISTS `credit_card`;
CREATE TABLE `credit_card`  (
  `id` int NOT NULL,
  `number` int NOT NULL,
  `expire_month` int NOT NULL,
  `expire_year` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of credit_card
-- ----------------------------

-- ----------------------------
-- Table structure for credit_record
-- ----------------------------
DROP TABLE IF EXISTS `credit_record`;
CREATE TABLE `credit_record`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `credit_points` int NOT NULL,
  `expire_date` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of credit_record
-- ----------------------------

-- ----------------------------
-- Table structure for genre
-- ----------------------------
DROP TABLE IF EXISTS `genre`;
CREATE TABLE `genre`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of genre
-- ----------------------------
INSERT INTO `genre` VALUES (1, 'Animation');
INSERT INTO `genre` VALUES (2, 'Comedy');
INSERT INTO `genre` VALUES (3, 'Action');
INSERT INTO `genre` VALUES (4, 'Science Fiction');
INSERT INTO `genre` VALUES (5, 'Romance');
INSERT INTO `genre` VALUES (6, 'Thriller');
INSERT INTO `genre` VALUES (7, 'Horror');
INSERT INTO `genre` VALUES (8, 'Crime');
INSERT INTO `genre` VALUES (9, 'Fantasy');
INSERT INTO `genre` VALUES (10, 'Drama');
INSERT INTO `genre` VALUES (11, 'Children');
INSERT INTO `genre` VALUES (12, 'Historical');
INSERT INTO `genre` VALUES (13, 'Adventure');

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `genre_id` int NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `release_date` date NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `length` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`genre_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of movie
-- ----------------------------
INSERT INTO `movie` VALUES (1, 1, 'Toy Story', '1995-11-19', 'A funny animation movie.', 'toy-story.jpg', 81);
INSERT INTO `movie` VALUES (2, 1, 'The Wild Robot', '2024-09-27', 'Animation movie.', '35301_320_470.webp', 102);
INSERT INTO `movie` VALUES (3, 13, 'The Best Christmas Pageant Ever', '2024-11-08', 'Based on the beloved book, The Best Christmas Pageant Ever centers on the Herdmans -- absolutely the worst kids in the history of the world. But this Christmas, they\'re taking over their local church Pageant -- and they just might unwittingly teach a shocked community the true meaning of Christmas.', 'TheBestChristmasnPageantEverOfficialPoster.jpg', 99);
INSERT INTO `movie` VALUES (4, 6, 'Heretic', '2024-11-08', 'Two young missionaries are forced to prove their faith when they knock on the wrong door and are greeted by a diabolical Mr. Reed (Hugh Grant), becoming ensnared in his deadly game of cat-and-mouse.', 'HereticPoster.jpg', 110);
INSERT INTO `movie` VALUES (5, 10, 'Here', '2024-11-01', 'From the reunited director, writer, and stars of Forrest Gump, Here is an original film about multiple families and a special place they inhabit. The story travels through generations, capturing the human experience in its purest form. Directed by Robert Zemeckis, screenplay by Eric Roth & Zemeckis and told much in the style of the acclaimed graphic novel by Richard McGuire on which it is based, Tom Hanks and Robin Wright star in a tale of love, loss, laughter and life, all of which happen right Here.', 'HereOfficialPoster.jpg', 104);
INSERT INTO `movie` VALUES (6, 3, 'Venom: The Last Dance', '2024-10-25', 'Tom Hardy returns as Venom, one of Marvel’s greatest and most complex characters, for the final film in the trilogy. Eddie and Venom are on the run. Hunted by both of their worlds and with the net closing in, the duo are forced into a devastating decision that will bring the curtains down on Venom and Eddie\'s last dance.', 'VNM3_CAN_350x519_TSR_01.jpg', 109);

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `amount` decimal(10, 2) NOT NULL,
  `credit_card_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment
-- ----------------------------

-- ----------------------------
-- Table structure for registered_user
-- ----------------------------
DROP TABLE IF EXISTS `registered_user`;
CREATE TABLE `registered_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `expire_date` datetime NOT NULL,
  `credit_card_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `credit_card_id`(`credit_card_id` ASC) USING BTREE,
  CONSTRAINT `registered_user_ibfk_1` FOREIGN KEY (`credit_card_id`) REFERENCES `credit_card` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registered_user
-- ----------------------------

-- ----------------------------
-- Table structure for seat
-- ----------------------------
DROP TABLE IF EXISTS `seat`;
CREATE TABLE `seat`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `theatre_id` int NOT NULL,
  `row` int NOT NULL,
  `column` int NOT NULL,
  `theatre_row` int NOT NULL,
  `theatre_column` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `theatre_id`(`theatre_id` ASC) USING BTREE,
  CONSTRAINT `seat_ibfk_1` FOREIGN KEY (`theatre_id`) REFERENCES `theatre` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seat
-- ----------------------------
INSERT INTO `seat` VALUES (1, 1, 1, 1, 1, 1);
INSERT INTO `seat` VALUES (2, 1, 1, 2, 1, 2);
INSERT INTO `seat` VALUES (3, 1, 1, 3, 1, 4);
INSERT INTO `seat` VALUES (4, 1, 1, 4, 1, 5);
INSERT INTO `seat` VALUES (5, 1, 2, 1, 3, 1);
INSERT INTO `seat` VALUES (6, 1, 2, 2, 3, 2);
INSERT INTO `seat` VALUES (7, 1, 2, 3, 3, 4);
INSERT INTO `seat` VALUES (8, 1, 2, 4, 3, 5);

-- ----------------------------
-- Table structure for showtime
-- ----------------------------
DROP TABLE IF EXISTS `showtime`;
CREATE TABLE `showtime`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `movie_id` int NOT NULL,
  `theatre_id` int NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `tickets` int NOT NULL,
  `tickets_sold` int NOT NULL,
  `public_announcement_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `movie_id`(`movie_id` ASC) USING BTREE,
  INDEX `theatre_id`(`theatre_id` ASC) USING BTREE,
  CONSTRAINT `showtime_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `showtime_ibfk_2` FOREIGN KEY (`theatre_id`) REFERENCES `theatre` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of showtime
-- ----------------------------
INSERT INTO `showtime` VALUES (1, 1, 1, '2024-11-13 15:49:30', '2024-11-13 17:49:33', 8, 0, '2024-11-14 16:16:05');

-- ----------------------------
-- Table structure for showtime_seat
-- ----------------------------
DROP TABLE IF EXISTS `showtime_seat`;
CREATE TABLE `showtime_seat`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `showtime_id` int NOT NULL,
  `seat_id` int NOT NULL,
  `available` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `show_id`(`showtime_id` ASC) USING BTREE,
  INDEX `seat_id`(`seat_id` ASC) USING BTREE,
  CONSTRAINT `showtime_seat_ibfk_1` FOREIGN KEY (`showtime_id`) REFERENCES `showtime` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `showtime_seat_ibfk_2` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of showtime_seat
-- ----------------------------
INSERT INTO `showtime_seat` VALUES (1, 1, 1, b'1');
INSERT INTO `showtime_seat` VALUES (2, 1, 2, b'1');
INSERT INTO `showtime_seat` VALUES (3, 1, 3, b'1');
INSERT INTO `showtime_seat` VALUES (4, 1, 4, b'1');
INSERT INTO `showtime_seat` VALUES (5, 1, 5, b'1');
INSERT INTO `showtime_seat` VALUES (6, 1, 6, b'1');
INSERT INTO `showtime_seat` VALUES (7, 1, 7, b'1');
INSERT INTO `showtime_seat` VALUES (8, 1, 8, b'0');

-- ----------------------------
-- Table structure for theatre
-- ----------------------------
DROP TABLE IF EXISTS `theatre`;
CREATE TABLE `theatre`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `rows` int NOT NULL,
  `columns` int NOT NULL,
  `seats` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of theatre
-- ----------------------------
INSERT INTO `theatre` VALUES (1, 'AcmePlex movie theatre', 'Calgary', 3, 5, 8);
INSERT INTO `theatre` VALUES (2, 'AcmePlex royal theatre', 'Montreal', 5, 6, 26);

-- ----------------------------
-- Table structure for ticket
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket`  (
  `no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `registered_user_id` int NULL DEFAULT NULL,
  `theatre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `show_start_time` datetime NOT NULL,
  `show_end_time` datetime NOT NULL,
  `seat_row` int NOT NULL,
  `seat_column` int NOT NULL,
  `issue_date` datetime NOT NULL,
  `expire_date` datetime NOT NULL,
  PRIMARY KEY (`no`) USING BTREE,
  INDEX `registered_user_id`(`registered_user_id` ASC) USING BTREE,
  CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`registered_user_id`) REFERENCES `registered_user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ticket
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
