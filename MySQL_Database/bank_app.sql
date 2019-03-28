-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.37 - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL Version:             10.1.0.5464
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for bank_app
CREATE DATABASE IF NOT EXISTS `bank_app` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `bank_app`;

-- Dumping structure for table bank_app.accounts
CREATE TABLE IF NOT EXISTS `accounts` (
  `account_nr` varchar(50) NOT NULL,
  `user_person_nr` varchar(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `saldo` double unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`account_nr`),
  KEY `FK_accounts_users` (`user_person_nr`),
  CONSTRAINT `FK_accounts_users` FOREIGN KEY (`user_person_nr`) REFERENCES `users` (`person_nr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table bank_app.accounts: ~8 rows (approximately)
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` (`account_nr`, `user_person_nr`, `name`, `type`, `saldo`) VALUES
	('111222333444', '690102-1155', 'superkonto', 'savings', 939750),
	('1133-4455', '690102-1155', 'Phone-Bill', 'billings', 0),
	('852116070843', '750312-3453', 'Test-konto', 'savings', 13),
	('852132761968', '881102-4492', 'Lönekonto', 'salary-account', 19649),
	('852137761968', '881102-4492', 'Spar-konto', 'savings', 20451),
	('852142482441', '881102-4492', 'TESTA', 'savings', 155),
	('852142483447', '750312-3453', 'Kort-konto', 'card-account', 4021.5),
	('852147483647', '750312-3453', 'Lönkonto', 'salary-account', 38324.5);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;

-- Dumping structure for view bank_app.addedaccounts
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `addedaccounts` (
	`id` INT(11) UNSIGNED NOT NULL,
	`account_nr` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`name` VARCHAR(50) NULL COLLATE 'utf8_general_ci',
	`user_person_nr` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci'
) ENGINE=MyISAM;

-- Dumping structure for table bank_app.autogiro
CREATE TABLE IF NOT EXISTS `autogiro` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_from` varchar(50) NOT NULL,
  `account_to` varchar(50) NOT NULL,
  `amount` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_autogiro_accounts` (`account_from`),
  KEY `FK_autogiro_accounts_2` (`account_to`),
  CONSTRAINT `FK_autogiro_accounts` FOREIGN KEY (`account_from`) REFERENCES `accounts` (`account_nr`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_autogiro_accounts_2` FOREIGN KEY (`account_to`) REFERENCES `accounts` (`account_nr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Dumping data for table bank_app.autogiro: ~2 rows (approximately)
/*!40000 ALTER TABLE `autogiro` DISABLE KEYS */;
INSERT INTO `autogiro` (`id`, `account_from`, `account_to`, `amount`) VALUES
	(4, '852147483647', '852116070843', 5),
	(5, '852147483647', '1133-4455', 20);
/*!40000 ALTER TABLE `autogiro` ENABLE KEYS */;

-- Dumping structure for event bank_app.autogiro_payment
DELIMITER //
CREATE DEFINER=`root`@`localhost` EVENT `autogiro_payment` ON SCHEDULE EVERY 1 MONTH STARTS '2019-03-27 00:00:01' ON COMPLETION PRESERVE ENABLE DO BEGIN
DECLARE n INT DEFAULT 0;
DECLARE i INT DEFAULT 0;

DECLARE from_acc VARCHAR(50);
DECLARE to_acc VARCHAR(50);
DECLARE a_amount DOUBLE;
SELECT COUNT(*) FROM autogiro INTO n;
SET i = 0;
WHILE i < n DO
SET from_acc = (SELECT auto.account_from FROM autogiro auto LIMIT 1 OFFSET i);
SET to_acc = (SELECT auto.account_to FROM autogiro auto LIMIT 1 OFFSET i);
SET a_amount = (SELECT auto.amount FROM autogiro auto LIMIT 1 OFFSET i);
	CALL transfer_money(a_amount, 'AUTOGIRO', from_acc, to_acc);
	SET i = i + 1;
END WHILE;
END//
DELIMITER ;

-- Dumping structure for table bank_app.cards
CREATE TABLE IF NOT EXISTS `cards` (
  `card_nr` bigint(16) NOT NULL,
  `account_nr` varchar(50) NOT NULL,
  `expire_date` date NOT NULL,
  `cvc` int(3) unsigned NOT NULL,
  PRIMARY KEY (`card_nr`),
  KEY `FK_cards_accounts` (`account_nr`),
  CONSTRAINT `FK_cards_accounts` FOREIGN KEY (`account_nr`) REFERENCES `accounts` (`account_nr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table bank_app.cards: ~1 rows (approximately)
/*!40000 ALTER TABLE `cards` DISABLE KEYS */;
INSERT INTO `cards` (`card_nr`, `account_nr`, `expire_date`, `cvc`) VALUES
	(1111222233334444, '852142483447', '2024-03-19', 111);
/*!40000 ALTER TABLE `cards` ENABLE KEYS */;

-- Dumping structure for procedure bank_app.card_pay
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `card_pay`(
	IN `c_card_nr` BIGINT,
	IN `c_to_account` VARCHAR(50),
	IN `c_amount` DOUBLE

)
BEGIN
DECLARE c_from BIGINT;
DECLARE message VARCHAR(1000);
SET c_from = (SELECT a.account_nr FROM accounts a WHERE a.account_nr = (SELECT c.account_nr FROM cards c WHERE c.card_nr = c_card_nr));
SET message = 'CARD PAYMENT';

CALL transfer_money(c_amount, message, c_from, c_to_account);

END//
DELIMITER ;

-- Dumping structure for table bank_app.future_transactions
CREATE TABLE IF NOT EXISTS `future_transactions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `message` varchar(50) DEFAULT NULL,
  `amount` double NOT NULL,
  `account_from` varchar(50) NOT NULL,
  `account_to` varchar(50) NOT NULL,
  `date_to_send` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_future_transactions_accounts` (`account_from`),
  KEY `FK_future_transactions_accounts_2` (`account_to`),
  CONSTRAINT `FK_future_transactions_accounts` FOREIGN KEY (`account_from`) REFERENCES `accounts` (`account_nr`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_future_transactions_accounts_2` FOREIGN KEY (`account_to`) REFERENCES `accounts` (`account_nr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- Dumping data for table bank_app.future_transactions: ~2 rows (approximately)
/*!40000 ALTER TABLE `future_transactions` DISABLE KEYS */;
INSERT INTO `future_transactions` (`id`, `message`, `amount`, `account_from`, `account_to`, `date_to_send`) VALUES
	(5, 'f transsaction', 1, '852137761968', '852142482441', '2019-03-28 23:00:00'),
	(6, 'Framtida betalning', 110, '852147483647', '1133-4455', '2019-04-01 22:00:00');
/*!40000 ALTER TABLE `future_transactions` ENABLE KEYS */;

-- Dumping structure for event bank_app.future_transactions_payment
DELIMITER //
CREATE DEFINER=`root`@`localhost` EVENT `future_transactions_payment` ON SCHEDULE EVERY 1 DAY STARTS '2019-03-28 00:00:10' ON COMPLETION PRESERVE ENABLE DO BEGIN
DECLARE n INT DEFAULT 0;
DECLARE i INT DEFAULT 0;

DECLARE from_acc VARCHAR(50);
DECLARE to_acc VARCHAR(50);
DECLARE c_message VARCHAR(50);
DECLARE a_amount DOUBLE;
DECLARE d_id INT;
DECLARE c_time TIMESTAMP;
SELECT COUNT(*) FROM future_transactions INTO n;
SET i = 0;
WHILE i < n DO
	SET c_time = (SELECT f.date_to_send FROM future_transactions f LIMIT 1 OFFSET i);

	IF c_time <= CURRENT_TIMESTAMP THEN
		SET from_acc = (SELECT f.account_from FROM future_transactions f LIMIT 1 OFFSET i);
		SET to_acc = (SELECT f.account_to FROM future_transactions f LIMIT 1 OFFSET i);
		SET a_amount = (SELECT f.amount FROM future_transactions f LIMIT 1 OFFSET i);
		SET d_id = (SELECT f.id FROM future_transactions f LIMIT 1 OFFSET i);
		SET c_message = (SELECT f.message FROM future_transactions f LIMIT 1 OFFSET i);

		CALL transfer_money(a_amount, c_message, from_acc, to_acc);
		DELETE FROM future_transactions WHERE id = d_id LIMIT 1;
	ELSE
		SET i = i + 1;
	END IF;
END WHILE;
END//
DELIMITER ;

-- Dumping structure for procedure bank_app.get_last_transactions
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_last_transactions`(
	IN `in_userID` VARCHAR(11)


)
BEGIN 
	SELECT * FROM transactions t 
	WHERE t.account_from = (SELECT accounts.account_nr FROM accounts WHERE accounts.account_nr = t.account_from AND accounts.user_person_nr = in_userID LIMIT 1)
	OR t.account_to = (SELECT accounts.account_nr FROM accounts WHERE accounts.account_nr = t.account_to AND accounts.user_person_nr = in_userID LIMIT 1)
	ORDER BY id DESC LIMIT 5;
END//
DELIMITER ;

-- Dumping structure for function bank_app.has_balance
DELIMITER //
CREATE DEFINER=`root`@`localhost` FUNCTION `has_balance`(
	`in_amount` DOUBLE,
	`in_account_nr` VARCHAR(50)

) RETURNS tinyint(1)
    DETERMINISTIC
BEGIN
	DECLARE flag BOOL;
	SET flag = if(in_amount < (SELECT a.saldo FROM accounts a WHERE a.account_nr = in_account_nr), 1, 0);
	RETURN flag;
END//
DELIMITER ;

-- Dumping structure for procedure bank_app.pay_salary
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `pay_salary`(
	IN `p_amount` DOUBLE,
	IN `p_to_user` VARCHAR(50)


)
BEGIN
DECLARE p_to BIGINT;
DECLARE p_from BIGINT;
DECLARE message VARCHAR(50);

SET p_to = (SELECT account_nr FROM accounts WHERE user_person_nr = p_to_user AND `type` = 'salary-account' LIMIT 1);
SET p_from = (SELECT account_nr FROM accounts WHERE user_person_nr = '690102-1155' AND `type` = 'savings' LIMIT 1);

SET message = 'SALARY';

CALL transfer_money(p_amount, message, p_from, p_to);

END//
DELIMITER ;

-- Dumping structure for table bank_app.transactions
CREATE TABLE IF NOT EXISTS `transactions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `message` varchar(50) DEFAULT NULL,
  `amount` double NOT NULL,
  `account_from` varchar(50) NOT NULL,
  `account_to` varchar(50) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8;

-- Dumping data for table bank_app.transactions: ~110 rows (approximately)
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` (`id`, `message`, `amount`, `account_from`, `account_to`, `date`) VALUES
	(1, 'SALARY', 20000, '111222333444', '852147483647', '2019-03-19 14:39:53'),
	(2, 'CARD PAYMENT', 1000, '852142483447', '852147483647', '2019-03-19 15:57:00'),
	(3, 'CARD PAYMENT', 500.5, '852142483447', '852147483647', '2019-03-20 11:45:23'),
	(4, 'CARD PAYMENT', 20.5, '852142483447', '852147483647', '2019-03-20 11:57:46'),
	(5, 'CARD PAYMENT', 20.5, '852142483447', '852147483647', '2019-03-20 13:39:55'),
	(6, 'CARD PAYMENT', 20.5, '852142483447', '852147483647', '2019-03-20 13:43:22'),
	(7, 'CARD PAYMENT', 20.5, '852142483447', '852147483647', '2019-03-20 13:45:28'),
	(8, 'CARD PAYMENT', 20.5, '852142483447', '852147483647', '2019-03-20 13:46:33'),
	(9, 'CARD PAYMENT', 20.5, '852142483447', '852147483647', '2019-03-20 13:52:59'),
	(10, 'CARD PAYMENT', 20.5, '852142483447', '852147483647', '2019-03-20 13:53:42'),
	(11, 'CARD PAYMENT', 20.5, '852142483447', '852147483647', '2019-03-20 14:08:56'),
	(12, 'CARD PAYMENT', 20.5, '852142483447', '852147483647', '2019-03-20 14:11:16'),
	(13, 'CARD PAYMENT', 20.5, '852142483447', '852147483647', '2019-03-20 14:30:02'),
	(14, 'Motorcykel', 2200, '852147483647', '852142483447', '2019-03-20 14:58:50'),
	(15, '852147483647', 10, '852147483647', '852142483447', '2019-03-21 07:12:28'),
	(16, '852147483647', 10, '852147483647', '852142483447', '2019-03-21 07:13:15'),
	(17, '852142483447', 10, '852142483447', '852142483447', '2019-03-21 07:28:40'),
	(18, '852147483647', 10, '852147483647', '852142483447', '2019-03-21 07:28:47'),
	(19, '852142483447', 10, '852142483447', '852142483447', '2019-03-21 07:28:54'),
	(20, '852147483647', 10, '852147483647', '852142483447', '2019-03-21 07:29:01'),
	(21, '852142483447', 10, '852142483447', '852142483447', '2019-03-21 07:29:07'),
	(22, '852142483447', 20, '852142483447', '852142483447', '2019-03-21 07:31:54'),
	(23, '852147483647', 20, '852147483647', '852142483447', '2019-03-21 07:32:01'),
	(24, '852142483447', 20, '852142483447', '852142483447', '2019-03-21 07:32:07'),
	(25, '852147483647', 2000, '852147483647', '852142483447', '2019-03-21 12:42:39'),
	(26, '852142483447', 322, '852142483447', '852147483647', '2019-03-21 12:49:17'),
	(27, '852142483447', 125, '852142483447', '852147483647', '2019-03-21 12:58:08'),
	(28, '852147483647', 22, '852147483647', '852142483447', '2019-03-21 13:03:12'),
	(29, 'SALARY', 20000, '111222333444', '852132761968', '2019-03-21 13:47:50'),
	(30, '852132761968', 123, '852132761968', '852132761968', '2019-03-21 13:48:51'),
	(31, '852132761968', 111, '852132761968', '852137761968', '2019-03-21 13:49:22'),
	(32, '852147483647', 100, '852147483647', '852142483447', '2019-03-22 08:16:36'),
	(33, 'SALARY', 100, '111222333444', '852132761968', '2019-03-22 10:14:09'),
	(34, '852142483447', 123, '852142483447', '852137762968', '2019-03-22 10:21:32'),
	(35, 'test sending', 5, '852137762968', '852147483647', '2019-03-22 11:15:30'),
	(36, 'hej', 100, '852142483447', '852137762968', '2019-03-22 15:04:33'),
	(37, 'Hej på dig', 321, '852147483647', '852121090169', '2019-03-25 11:12:53'),
	(38, 'test till anna', 10, '852142483447', '852137761968', '2019-03-25 11:50:46'),
	(39, 'qdwqdw', 32, '852147483647', '852116070843', '2019-03-25 12:50:19'),
	(40, 'Test skickar', 1, '852116070843', 'Annas-konto', '2019-03-25 13:13:22'),
	(41, 'MONTHLY SAVING', 15, '852132761968', '852137761968', '2019-03-25 14:30:03'),
	(42, 'MONTHLY SAVING', 15, '852132761968', '852137761968', '2019-03-25 14:30:08'),
	(43, 'MONTHLY SAVING', 15, '852132761968', '852137761968', '2019-03-25 14:30:13'),
	(44, 'MONTHLY SAVING', 15, '852132761968', '852137761968', '2019-03-25 14:30:18'),
	(45, 'MONTHLY SAVING', 15, '852132761968', '852137761968', '2019-03-25 14:30:23'),
	(46, 'MONTHLY SAVING', 15, '852132761968', '852137761968', '2019-03-25 14:56:30'),
	(47, 'MONTHLY SAVING', 15, '852132761968', '852137761968', '2019-03-25 14:56:35'),
	(48, 'MONTHLY SAVING', 15, '852132761968', '852137761968', '2019-03-25 14:56:40'),
	(49, 'MONTHLY SAVING', 15, '852132761968', '852137761968', '2019-03-25 14:56:45'),
	(50, 'MONTHLY SAVING', 15, '852132761968', '852137761968', '2019-03-25 14:56:50'),
	(51, 'MONTHLY SAVING', 10, '852132761968', '852137761968', '2019-03-25 15:03:50'),
	(52, 'MONTHLY SAVING', 10, '852132761968', '852137761968', '2019-03-25 15:03:55'),
	(53, 'MONTHLY SAVING', 10, '852132761968', '852137761968', '2019-03-25 15:04:00'),
	(54, 'MONTHLY SAVING', 10, '852132761968', '852137761968', '2019-03-25 15:04:05'),
	(55, 'MONTHLY SAVING', 10, '852132761968', '852137761968', '2019-03-25 15:04:51'),
	(56, 'MONTHLY SAVING', 5, '852147483647', '852142483447', '2019-03-25 19:18:39'),
	(57, 'AUTOGIRO', 1, '852132761968', '852142482441', '2019-03-26 09:48:57'),
	(58, 'AUTOGIRO', 1, '852132761968', '852142482441', '2019-03-26 09:49:02'),
	(59, 'AUTOGIRO', 1, '852132761968', '852142482441', '2019-03-26 09:49:07'),
	(60, 'AUTOGIRO', 1, '852132761968', '852142482441', '2019-03-26 09:49:12'),
	(61, 'AUTOGIRO', 1, '852132761968', '852142482441', '2019-03-26 09:49:17'),
	(62, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:49:22'),
	(63, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:49:22'),
	(64, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:49:27'),
	(65, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:49:27'),
	(66, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:49:32'),
	(67, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:49:32'),
	(68, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:49:37'),
	(69, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:49:37'),
	(70, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:49:41'),
	(71, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:49:41'),
	(72, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:49:47'),
	(73, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:49:47'),
	(74, 'AUTOGIRO', 3, '852147483647', '852142483447', '2019-03-26 09:49:47'),
	(75, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:49:52'),
	(76, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:49:52'),
	(77, 'AUTOGIRO', 3, '852147483647', '852142483447', '2019-03-26 09:49:52'),
	(78, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:49:56'),
	(79, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:49:57'),
	(80, 'AUTOGIRO', 3, '852147483647', '852142483447', '2019-03-26 09:49:57'),
	(81, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:50:02'),
	(82, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:50:02'),
	(83, 'AUTOGIRO', 3, '852147483647', '852142483447', '2019-03-26 09:50:02'),
	(84, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:50:07'),
	(85, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:50:07'),
	(86, 'AUTOGIRO', 3, '852147483647', '852142483447', '2019-03-26 09:50:07'),
	(87, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:50:11'),
	(88, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:50:12'),
	(89, 'AUTOGIRO', 3, '852147483647', '852142483447', '2019-03-26 09:50:12'),
	(90, 'AUTOGIRO', 1, '852132761968', '852116070843', '2019-03-26 09:50:17'),
	(91, 'AUTOGIRO', 2, '852142483447', '852142482441', '2019-03-26 09:50:17'),
	(92, 'AUTOGIRO', 1, '852132761968', '852142482441', '2019-03-26 09:50:22'),
	(93, 'Hej', 10, '852142483447', '852116070843', '2019-03-26 12:39:46'),
	(94, 'Hej anna', 5, '852147483647', '852137761968', '2019-03-26 12:52:42'),
	(95, 'Hej anna', 5, '852142483447', '852142482441', '2019-03-26 12:53:31'),
	(96, 'ok', 1, '852132761968', '852137761968', '2019-03-26 13:09:17'),
	(97, 'nollat', 1, '852121520227', '852116070843', '2019-03-26 14:17:36'),
	(98, 'ny nolla', 1, '852121520227', '852116070843', '2019-03-26 14:18:35'),
	(99, 'sista försöket', 1, '852121520227', '852116070843', '2019-03-26 14:19:39'),
	(100, 'skoja', 1, '852121520227', '852116070843', '2019-03-26 14:21:40'),
	(101, 'test', 123, '852132761968', '852137761968', '2019-03-26 14:33:47'),
	(102, 'f3e', 12, '852116070843', '852137761968', '2019-03-26 15:41:01'),
	(103, 'CARD PAYMENT', 10, '852142483447', '852116070843', '2019-03-28 10:22:41'),
	(104, 'ok', 11, '852116070843', '852142483447', '2019-03-28 11:20:03'),
	(105, 'to much', 40, '852116070843', '852142483447', '2019-03-28 11:22:08'),
	(106, 'SALARY', 50, '111222333444', '852147483647', '2019-03-28 11:24:31'),
	(107, 'SALARY', 20000, '111222333444', '852147483647', '2019-03-28 11:24:49'),
	(108, 'f test', 2, '852116070843', '852132761968', '2019-03-28 13:37:07'),
	(109, 'f test igen', 11, '852137761968', '852116070843', '2019-03-28 13:39:25'),
	(110, 'ABC123', 120, '852147483647', '852142482441', '2019-03-28 14:37:04');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;

-- Dumping structure for procedure bank_app.transfer_money
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `transfer_money`(
	IN `p_amount` DOUBLE,
	IN `p_message` VARCHAR(50),
	IN `p_from` VARCHAR(50),
	IN `p_to` VARCHAR(50)






)
BEGIN
	IF (has_balance(p_amount, p_from) = 1) THEN 
	# decrease saldo
		UPDATE accounts SET saldo = (saldo - p_amount) WHERE account_nr = p_from;
	# increase saldo
		UPDATE accounts SET saldo = (saldo + p_amount) WHERE account_nr = p_to;

		INSERT INTO transactions SET message = p_message, amount = p_amount, account_from = p_from, account_to = p_to, `date` = CURRENT_TIMESTAMP;

	END IF;
END//
DELIMITER ;

-- Dumping structure for table bank_app.users
CREATE TABLE IF NOT EXISTS `users` (
  `person_nr` varchar(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `age` int(3) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`person_nr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table bank_app.users: ~3 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`person_nr`, `name`, `age`, `email`, `password`) VALUES
	('690102-1155', 'BOSS', 40, 'boss@boss.nu', 'iambossen'),
	('750312-3453', 'Kalle', 25, 'kalle@abc.se', 'abc12345?'),
	('881102-4492', 'Anna', 20, 'anna@abc.se', 'abc12345?');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- Dumping structure for table bank_app.userXaccounts
CREATE TABLE IF NOT EXISTS `userXaccounts` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_person_nr` varchar(50) NOT NULL,
  `account_nr` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_addedAccounts_users` (`user_person_nr`),
  KEY `FK_addedAccounts_accounts` (`account_nr`),
  CONSTRAINT `FK_addedAccounts_accounts` FOREIGN KEY (`account_nr`) REFERENCES `accounts` (`account_nr`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_addedAccounts_users` FOREIGN KEY (`user_person_nr`) REFERENCES `users` (`person_nr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- Dumping data for table bank_app.userXaccounts: ~3 rows (approximately)
/*!40000 ALTER TABLE `userXaccounts` DISABLE KEYS */;
INSERT INTO `userXaccounts` (`id`, `user_person_nr`, `account_nr`, `name`) VALUES
	(3, '750312-3453', '852132761968', 'Annas-konto'),
	(4, '750312-3453', '852142482441', 'Annas test account'),
	(8, '750312-3453', '1133-4455', 'Mobil-räkning');
/*!40000 ALTER TABLE `userXaccounts` ENABLE KEYS */;

-- Dumping structure for view bank_app.addedaccounts
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `addedaccounts`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `addedaccounts` AS select `x`.`id` AS `id`,`a`.`account_nr` AS `account_nr`,`x`.`name` AS `name`,`x`.`user_person_nr` AS `user_person_nr` from (`accounts` `a` join `userxaccounts` `x`) where (`a`.`account_nr` = `x`.`account_nr`);

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
