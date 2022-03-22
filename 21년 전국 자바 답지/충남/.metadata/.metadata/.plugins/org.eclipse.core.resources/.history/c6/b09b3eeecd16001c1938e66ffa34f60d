package db;

import java.sql.Connection;
import java.sql.DriverManager;
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
			stmt = con.createStatement();
		} catch (SQLException e) {

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
		execute("load data local infile './지급자료/" + t + ".txt' into table " + t
				+ " fields terminated by '\t'	lines terminated by '\r\n' ignore 1 lines");
	}

	public DB() {
		execute("drop database if exists metro");
		execute("create database metro default character set utf8");
		execute("drop user if exists user@localhost");
		execute("create user user@localhost identified by '1234'");
		execute("grant select, insert, update, delete on metro.* to user@localhost");
		execute("set global local_infile = 1");
		execute("use metro");

		createT("Station", "serial int primary key not null auto_increment, name varchar(40)");
		createT("Metro",
				"serial int primary key not null auto_increment, name varchar(60), start Time, end Time, `interval` Time");
		createT("Route",
				"serial int primary key not null auto_increment, station int, metro int, foreign key(station) references station(serial), foreign key(metro) references Metro(serial)");
		createT("Path",
				"serial int primary key not null auto_increment, departure int, arrive int,cost int, foreign key(departure) references station(serial), foreign key(arrive) references Station(serial)");
		createT("User",
				"serial int primary key not null auto_increment, id varchar(20), pw varchar(20), name varchar(30), birth Date, phone varchar(14)");
		createT("Purchase",
				"serial varchar(6) primary key not null, user int, departure int, arrive int, price int, time Time, date Date, distance int, foreign key(user) references user(serial), foreign key(departure) references Station(serial), foreign key(arrive) references Station(serial)");
	}

	public static void main(String[] args) {
		new DB();
	}
}
