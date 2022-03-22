package 충남;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	static Statement stmt;
	static Connection con;
	
	public DB() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost?allowLoadLocalInfile=true&serverTimezone=UTC&allowPublicKeyRetreival=true","root","1234");
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void createDB() {
		execute("drop database if exists metro");
		execute("create database metro");
		execute("drop user if exists user@localhost");
		execute("create user user@localhost identified by '1234'");
		execute("use metro");
		execute("grant update, delete, insert, select on metro.* to user@localhost");
		execute("set global local_infile = 1");
	}
	
	void createTable(String n, String c) {
		execute("create table "+n+"("+c+")");
		execute("load data local infile './지급자료/"+n+".txt' into table "+n+" lines terminated by '\r\n' ignore 1 lines");
	}

	public static void main(String[] args) {
		DB db = new DB();
		db.createDB();
		db.createTable("Station", "serial int primary key not null auto_increment, name varchar(40)");
		db.createTable("Metro", "serial int primary key not null auto_increment, name varchar(60), start time, end time, `interval` time");
		db.createTable("route", "serial int primary key not null auto_increment, station int, metro int, foreign key(station) references station(serial) on delete cascade on update cascade, foreign key(metro) references metro(serial) on delete cascade on update cascade");
		db.createTable("Path", "serial int primary key not null auto_increment, departure int, arrive int, cost int, foreign key(arrive) references station(serial) on delete cascade on update cascade, foreign key(departure) references station(serial) on delete cascade on update cascade");
		db.createTable("User", "serial int primary key not null auto_increment, id varchar(20), pw varchar(50), name varchar(30), birth date, phone varchar(14)");
		db.createTable("Purchase", "serial int primary key not null auto_increment, user int , departure int, arrive int, price int, time time, date date, dsitance int, foreign key(user) references user(serial) on delete cascade on update cascade, foreign key(departure) references station(serial) on delete cascade on update cascade, foreign key(arrive) references station(serial) on delete cascade on update cascade");
	}

}
