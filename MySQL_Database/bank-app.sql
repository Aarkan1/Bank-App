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

-- Data exporting was unselected.
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

-- Data exporting was unselected.
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

-- Data exporting was unselected.
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Dumping structure for event bank_app.future_transactions_payment
DELIMITER //
CREATE DEFINER=`root`@`localhost` EVENT `future_transactions_payment` ON SCHEDULE EVERY 1 DAY STARTS '2019-03-26 00:00:10' ON COMPLETION PRESERVE ENABLE DO BEGIN

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
DECLARE message VARCHAR(1000);

SET p_to = (SELECT account_nr FROM accounts WHERE user_person_nr = p_to_user AND `type` = 'salary-account' LIMIT 1);
SET p_from = (SELECT account_nr FROM accounts WHERE user_person_nr = 6901021155 AND `type` = 'savings' LIMIT 1);

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
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
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

-- Data exporting was unselected.
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

-- Data exporting was unselected.
-- Dumping structure for view bank_app.addedaccounts
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `addedaccounts`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `addedaccounts` AS select `x`.`id` AS `id`,`a`.`account_nr` AS `account_nr`,`x`.`name` AS `name`,`x`.`user_person_nr` AS `user_person_nr` from (`accounts` `a` join `userxaccounts` `x`) where (`a`.`account_nr` = `x`.`account_nr`);

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
