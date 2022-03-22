package ±¤±¤ÁÖ;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	static Connection con;
	static Statement stmt;
	static {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost?serverTimezone=UTC&allowPublicKeyRetrieval=true&allowLoadLocalInfile=true",
					"root", "1234");
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(sql);
		}
	}

	void loadData(String table) {
		execute("load data local infile './Áö±ÞÀÚ·á/" + table.toLowerCase() + ".txt' into table " + table
				+ " ignore 1 lines");
	}
	
	public DB() {
		execute("set global local_infile = 1");
		String sql[] =

				("SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;\r\n"
						+ "SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;\r\n"
						+ "SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n" + "-- Schema Eats\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP SCHEMA IF EXISTS `Eats` ;\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n" + "-- Schema Eats\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "CREATE SCHEMA IF NOT EXISTS `Eats` DEFAULT CHARACTER SET utf8 ;\r\n" + "USE `Eats` ;\r\n"
						+ "\r\n" + "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Category`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Category` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Category` (\r\n"
						+ "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `NAME` VARCHAR(20) NULL,\r\n"
						+ "  PRIMARY KEY (`NO`))\r\n" + "ENGINE = InnoDB;\r\n" + "\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n" + "-- Table `Eats`.`Map`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Map` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Map` (\r\n" + "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n"
						+ "  `X` INT NULL,\r\n" + "  `Y` INT NULL,\r\n" + "  `TYPE` INT NULL,\r\n"
						+ "  PRIMARY KEY (`NO`))\r\n" + "ENGINE = InnoDB;\r\n" + "\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n" + "-- Table `Eats`.`User`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`User` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`User` (\r\n" + "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n"
						+ "  `EMAIL` VARCHAR(50) NULL,\r\n" + "  `PHONE` VARCHAR(13) NULL,\r\n"
						+ "  `PW` VARCHAR(20) NULL,\r\n" + "  `NAME` VARCHAR(40) NULL,\r\n" + "  `MAP` INT NULL,\r\n"
						+ "  PRIMARY KEY (`NO`),\r\n" + "  INDEX `fk_User_Map_idx` (`MAP` ASC),\r\n"
						+ "  CONSTRAINT `fk_User_Map`\r\n" + "    FOREIGN KEY (`MAP`)\r\n"
						+ "    REFERENCES `Eats`.`Map` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n" + "\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Seller`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Seller` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Seller` (\r\n" + "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n"
						+ "  `EMAIL` VARCHAR(50) NULL,\r\n" + "  `PHONE` VARCHAR(13) NULL,\r\n"
						+ "  `PW` VARCHAR(20) NULL,\r\n" + "  `NAME` VARCHAR(40) NULL,\r\n" + "  `ABOUT` TEXT NULL,\r\n"
						+ "  `CATEGORY` INT NULL,\r\n" + "  `DELIVERYFEE` INT NULL,\r\n" + "  `MAP` INT NULL,\r\n"
						+ "  PRIMARY KEY (`NO`),\r\n" + "  INDEX `fk_Seller_Category1_idx` (`CATEGORY` ASC),\r\n"
						+ "  INDEX `fk_Seller_Map1_idx` (`MAP` ASC),\r\n" + "  CONSTRAINT `fk_Seller_Category1`\r\n"
						+ "    FOREIGN KEY (`CATEGORY`)\r\n" + "    REFERENCES `Eats`.`Category` (`NO`)\r\n"
						+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n"
						+ "  CONSTRAINT `fk_Seller_Map1`\r\n" + "    FOREIGN KEY (`MAP`)\r\n"
						+ "    REFERENCES `Eats`.`Map` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n" + "\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n" + "-- Table `Eats`.`Rider`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Rider` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Rider` (\r\n" + "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n"
						+ "  `EMAIL` VARCHAR(50) NULL,\r\n" + "  `PHONE` VARCHAR(13) NULL,\r\n"
						+ "  `PW` VARCHAR(20) NULL,\r\n" + "  `NAME` VARCHAR(40) NULL,\r\n" + "  `MAP` INT NULL,\r\n"
						+ "  PRIMARY KEY (`NO`),\r\n" + "  INDEX `fk_Rider_Map1_idx` (`MAP` ASC),\r\n"
						+ "  CONSTRAINT `fk_Rider_Map1`\r\n" + "    FOREIGN KEY (`MAP`)\r\n"
						+ "    REFERENCES `Eats`.`Map` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n" + "\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Payment`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Payment` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Payment` (\r\n"
						+ "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `ISSUER` VARCHAR(40) NULL,\r\n"
						+ "  `CARD` VARCHAR(16) NULL,\r\n" + "  `CVV` VARCHAR(4) NULL,\r\n"
						+ "  `PW` VARCHAR(6) NULL,\r\n" + "  `USER` INT NULL,\r\n" + "  PRIMARY KEY (`NO`),\r\n"
						+ "  INDEX `fk_Payment_User1_idx` (`USER` ASC),\r\n" + "  CONSTRAINT `fk_Payment_User1`\r\n"
						+ "    FOREIGN KEY (`USER`)\r\n" + "    REFERENCES `Eats`.`User` (`NO`)\r\n"
						+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n"
						+ "\r\n" + "\r\n" + "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Type`\r\n" + "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Type` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Type` (\r\n" + "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n"
						+ "  `NAME` VARCHAR(40) NULL,\r\n" + "  `SELLER` INT NULL,\r\n" + "  PRIMARY KEY (`NO`),\r\n"
						+ "  INDEX `fk_Type_Seller1_idx` (`SELLER` ASC),\r\n" + "  CONSTRAINT `fk_Type_Seller1`\r\n"
						+ "    FOREIGN KEY (`SELLER`)\r\n" + "    REFERENCES `Eats`.`Seller` (`NO`)\r\n"
						+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n"
						+ "\r\n" + "\r\n" + "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Menu`\r\n" + "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Menu` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Menu` (\r\n" + "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n"
						+ "  `NAME` VARCHAR(100) NULL,\r\n" + "  `DESCRIPTION` TEXT NULL,\r\n"
						+ "  `PRICE` INT NULL,\r\n" + "  `COOKTIME` TIME NULL,\r\n" + "  `SELLER` INT NULL,\r\n"
						+ "  `TYPE` INT NULL,\r\n" + "  PRIMARY KEY (`NO`),\r\n"
						+ "  INDEX `fk_Menu_Seller1_idx` (`SELLER` ASC),\r\n"
						+ "  INDEX `fk_Menu_Type1_idx` (`TYPE` ASC),\r\n" + "  CONSTRAINT `fk_Menu_Seller1`\r\n"
						+ "    FOREIGN KEY (`SELLER`)\r\n" + "    REFERENCES `Eats`.`Seller` (`NO`)\r\n"
						+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n"
						+ "  CONSTRAINT `fk_Menu_Type1`\r\n" + "    FOREIGN KEY (`TYPE`)\r\n"
						+ "    REFERENCES `Eats`.`Type` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n" + "\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Options`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Options` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Options` (\r\n"
						+ "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `TITLE` VARCHAR(50) NULL,\r\n"
						+ "  `NAME` VARCHAR(50) NULL,\r\n" + "  `PRICE` INT NULL,\r\n" + "  `MENU` INT NULL,\r\n"
						+ "  PRIMARY KEY (`NO`),\r\n" + "  INDEX `fk_Options_Menu1_idx` (`MENU` ASC),\r\n"
						+ "  CONSTRAINT `fk_Options_Menu1`\r\n" + "    FOREIGN KEY (`MENU`)\r\n"
						+ "    REFERENCES `Eats`.`Menu` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n" + "\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Favorite`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Favorite` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Favorite` (\r\n"
						+ "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `SELLER` INT NULL,\r\n"
						+ "  `USER` INT NULL,\r\n" + "  PRIMARY KEY (`NO`),\r\n"
						+ "  INDEX `fk_Favorite_Seller1_idx` (`SELLER` ASC),\r\n"
						+ "  INDEX `fk_Favorite_User1_idx` (`USER` ASC),\r\n" + "  CONSTRAINT `fk_Favorite_Seller1`\r\n"
						+ "    FOREIGN KEY (`SELLER`)\r\n" + "    REFERENCES `Eats`.`Seller` (`NO`)\r\n"
						+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n"
						+ "  CONSTRAINT `fk_Favorite_User1`\r\n" + "    FOREIGN KEY (`USER`)\r\n"
						+ "    REFERENCES `Eats`.`User` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n" + "\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Receipt`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Receipt` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Receipt` (\r\n"
						+ "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `PRICE` INT NULL,\r\n"
						+ "  `RECEIPT_TIME` DATETIME NULL,\r\n" + "  `SELLER` INT NULL,\r\n" + "  `USER` INT NULL,\r\n"
						+ "  `PAYMENT` INT NULL,\r\n" + "  `STATUS` INT NULL,\r\n" + "  PRIMARY KEY (`NO`),\r\n"
						+ "  INDEX `fk_Receipt_Seller1_idx` (`SELLER` ASC),\r\n"
						+ "  INDEX `fk_Receipt_User1_idx` (`USER` ASC),\r\n"
						+ "  INDEX `fk_Receipt_Payment1_idx` (`PAYMENT` ASC),\r\n"
						+ "  CONSTRAINT `fk_Receipt_Seller1`\r\n" + "    FOREIGN KEY (`SELLER`)\r\n"
						+ "    REFERENCES `Eats`.`Seller` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `fk_Receipt_User1`\r\n"
						+ "    FOREIGN KEY (`USER`)\r\n" + "    REFERENCES `Eats`.`User` (`NO`)\r\n"
						+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n"
						+ "  CONSTRAINT `fk_Receipt_Payment1`\r\n" + "    FOREIGN KEY (`PAYMENT`)\r\n"
						+ "    REFERENCES `Eats`.`Payment` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n" + "\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Receipt_Detail`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Receipt_Detail` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Receipt_Detail` (\r\n"
						+ "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `MENU` INT NULL,\r\n"
						+ "  `COUNT` INT NULL,\r\n" + "  `PRICE` INT NULL,\r\n" + "  `RECEIPT` INT NULL,\r\n"
						+ "  PRIMARY KEY (`NO`),\r\n" + "  INDEX `fk_Receipt_Detail_Menu1_idx` (`MENU` ASC),\r\n"
						+ "  INDEX `fk_Receipt_Detail_Receipt1_idx` (`RECEIPT` ASC),\r\n"
						+ "  CONSTRAINT `fk_Receipt_Detail_Menu1`\r\n" + "    FOREIGN KEY (`MENU`)\r\n"
						+ "    REFERENCES `Eats`.`Menu` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `fk_Receipt_Detail_Receipt1`\r\n"
						+ "    FOREIGN KEY (`RECEIPT`)\r\n" + "    REFERENCES `Eats`.`Receipt` (`NO`)\r\n"
						+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n"
						+ "\r\n" + "\r\n" + "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Receipt_Options`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Receipt_Options` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Receipt_Options` (\r\n"
						+ "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `OPTIONS` INT NULL,\r\n"
						+ "  `PRICE` INT NULL,\r\n" + "  `RECEIPT_DETAIL` INT NULL,\r\n" + "  PRIMARY KEY (`NO`),\r\n"
						+ "  INDEX `fk_Receipt_Options_Receipt_Detail1_idx` (`RECEIPT_DETAIL` ASC),\r\n"
						+ "  INDEX `fk_Receipt_Options_Options1_idx` (`OPTIONS` ASC),\r\n"
						+ "  CONSTRAINT `fk_Receipt_Options_Receipt_Detail1`\r\n"
						+ "    FOREIGN KEY (`RECEIPT_DETAIL`)\r\n" + "    REFERENCES `Eats`.`Receipt_Detail` (`NO`)\r\n"
						+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n"
						+ "  CONSTRAINT `fk_Receipt_Options_Options1`\r\n" + "    FOREIGN KEY (`OPTIONS`)\r\n"
						+ "    REFERENCES `Eats`.`Options` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n" + "\r\n" + "\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Delivery`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Delivery` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Delivery` (\r\n"
						+ "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n" + "  `RIDER` INT NULL,\r\n"
						+ "  `RECEIPT` INT NULL,\r\n" + "  `START_TIME` DATETIME NULL,\r\n"
						+ "  PRIMARY KEY (`NO`),\r\n" + "  INDEX `fk_Delivery_Rider1_idx` (`RIDER` ASC),\r\n"
						+ "  INDEX `fk_Delivery_Receipt1_idx` (`RECEIPT` ASC),\r\n"
						+ "  CONSTRAINT `fk_Delivery_Rider1`\r\n" + "    FOREIGN KEY (`RIDER`)\r\n"
						+ "    REFERENCES `Eats`.`Rider` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `fk_Delivery_Receipt1`\r\n"
						+ "    FOREIGN KEY (`RECEIPT`)\r\n" + "    REFERENCES `Eats`.`Receipt` (`NO`)\r\n"
						+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n"
						+ "\r\n" + "\r\n" + "-- -----------------------------------------------------\r\n"
						+ "-- Table `Eats`.`Review`\r\n"
						+ "-- -----------------------------------------------------\r\n"
						+ "DROP TABLE IF EXISTS `Eats`.`Review` ;\r\n" + "\r\n"
						+ "CREATE TABLE IF NOT EXISTS `Eats`.`Review` (\r\n" + "  `NO` INT NOT NULL AUTO_INCREMENT,\r\n"
						+ "  `TITLE` VARCHAR(30) NULL,\r\n" + "  `CONTENT` TEXT NULL,\r\n" + "  `RATE` INT NULL,\r\n"
						+ "  `USER` INT NULL,\r\n" + "  `SELLER` INT NULL,\r\n" + "  `RECEIPT` INT NULL,\r\n"
						+ "  PRIMARY KEY (`NO`),\r\n" + "  INDEX `fk_Review_User1_idx` (`USER` ASC),\r\n"
						+ "  INDEX `fk_Review_Seller1_idx` (`SELLER` ASC),\r\n"
						+ "  INDEX `fk_Review_Receipt1_idx` (`RECEIPT` ASC),\r\n" + "  CONSTRAINT `fk_Review_User1`\r\n"
						+ "    FOREIGN KEY (`USER`)\r\n" + "    REFERENCES `Eats`.`User` (`NO`)\r\n"
						+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n"
						+ "  CONSTRAINT `fk_Review_Seller1`\r\n" + "    FOREIGN KEY (`SELLER`)\r\n"
						+ "    REFERENCES `Eats`.`Seller` (`NO`)\r\n" + "    ON DELETE NO ACTION\r\n"
						+ "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `fk_Review_Receipt1`\r\n"
						+ "    FOREIGN KEY (`RECEIPT`)\r\n" + "    REFERENCES `Eats`.`Receipt` (`NO`)\r\n"
						+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;\r\n")
								.split(";");
		for (String s : sql)
			execute(s);

		String t[] = "Category,Delivery,Favorite,Map,Menu,Options,Payment,Receipt_detail,Receipt_options,Receipt,Review,Rider,Seller,Type,User"
				.split(",");
		 
		for (String ta : t) {
			loadData(ta);
		}
	}

	public static void main(String[] args) {
		new DB();
	}
}
