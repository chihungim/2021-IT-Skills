package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	public static Connection con;
	public static Statement stmt;
	static {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost?serverTimezone=UTC&allowPublicKeyRetrieval=true&allowLoadLocalInfile=true",
					"root", "1234");
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		}
	}

	void createT(String t, String c) {
		execute("create table " + t + "(" + c + ")");
		execute("load data local infile './�����ڷ�/" + t + ".txt' into table " + t
				+ " lines terminated by '\r\n' ignore 1 lines");
	}

	public DB() {
		execute("drop database if exists Eats");
		execute("create database eats default character set utf8");
		execute("drop user if exists user@localhost");
		execute("create user user@localhost identified by '1234'");
		execute("grant select, delete, update, insert on eats.* to user@localhost");
		execute("set global local_infile = 1");
		execute("use eats");

		createT("Category", "NO int primary key not null auto_increment, NAME varchar(20)");
		createT("Map", "NO int primary key not null auto_increment, X int, Y int, TYPE int");
		createT("User",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, EMAIL VARCHAR(50), PHONE VARCHAR(13), PW VARCHAR(20), NAME VARCHAR(40), MAP INT, FOREIGN KEY(MAP) REFERENCES MAP(NO)");
		createT("Seller",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, EMAIL VARCHAR(50), PHONE VARCHAR(13), PW VARCHAR(20), NAME VARCHAR(40), ABOUT TEXT, CATEGORY INT, DELIVERYFEE INT, MAP INT, FOREIGN KEY(CATEGORY) REFERENCES CATEGORY(NO), FOREIGN KEY(MAP) REFERENCES MAP(NO)");
		createT("Rider",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, EMAIL VARCHAR(50), PHONE VARCHAR(13), PW VARCHAR(20), NAME VARCHAR(40), MAP INT, FOREIGN KEY(MAP) REFERENCES MAP(NO)");
		createT("Payment",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, ISSUER VARCHAR(40), CARD VARCHAR(16), CVV VARCHAR(4), PW VARCHAR(6), USER INT, FOREIGN KEY(USER) REFERENCES USER(NO)");
		createT("Type",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, NAME VARCHAR(40), SELLER INT, FOREIGN KEY(SELLER) REFERENCES SELLER(NO)");

		createT("Menu",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, NAME VARCHAR(100), DESCRIPTION TEXT, PRICE INT, COOKTIME TIME, SELLER INT, TYPE INT, FOREIGN KEY(SELLER) REFERENCES SELLER(NO), FOREIGN KEY(TYPE) REFERENCES TYPE(NO)");
		createT("Options",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, TITLE VARCHAR(50), NAME VARCHAR(50), PRICE INT, MENU INT, FOREIGN KEY(MENU) REFERENCES MENU(NO)");
		createT("Favorite",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, SELLER INT, USER INT, FOREIGN KEY(SELLER) REFERENCES SELLER(NO), FOREIGN KEY(USER) REFERENCES USER(NO)");
		createT("Receipt",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, PRICE INT, RECEIPT_TIME DATETIME, SELLER INT, USER INT, PAYMENT INT, STATUS INT, FOREIGN KEY(SELLER) REFERENCES SELLER(NO), FOREIGN KEY(USER) REFERENCES USER(NO), FOREIGN KEY(PAYMENT) REFERENCES PAYMENT(NO)");
		createT("Receipt_Detail",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, MENU INT, COUNT INT, PRICE INT, RECEIPT INT, FOREIGN KEY(MENU) REFERENCES MENU(NO), FOREIGN KEY(RECEIPT) REFERENCES RECEIPT(NO)");
		createT("Receipt_Options",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, OPTIONS INT, PRICE INT, RECEIPT_DETAIL INT, FOREIGN KEY(OPTIONS) REFERENCES OPTIONS(NO), FOREIGN KEY(RECEIPT_DETAIL) REFERENCES RECEIPT_DETAIL(NO)");
		createT("Delivery",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, RIDER INT, RECEIPT INT, START_TIME DATETIME, FOREIGN KEY(RIDER) REFERENCES RIDER(NO), FOREIGN KEY(RECEIPT) REFERENCES RECEIPT(NO)");
		createT("Review",
				"NO INT PRIMARY KEY NOT NULL AUTO_INCREMENT, TITLE VARCHAR(30), CONTENT TEXT, RATE INT, USER INT, SELLER INT, RECEIPT INT, FOREIGN KEY(USER) REFERENCES USER(NO), FOREIGN KEY(SELLER) REFERENCES SELLER(NO), FOREIGN KEY(RECEIPT) REFERENCES RECEIPT(NO)");
	}

	public static void main(String[] args) {
		new DB();
	}

}
