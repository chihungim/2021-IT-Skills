package db;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	
	static Connection con;
	static Statement stmt;
	
	static {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true&allowPublicKeyRetrieval=true", "root", "1234");
			stmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void createTable(String t, String c) {
		execute("create table "+t+"("+c+")");
		execute("load data local infile './datafiles/"+t+".txt' into table "+t+" lines terminated by '\r\n' ignore 1 lines");
	}
	
	public DB() {
		execute("drop database if exists adventure");
		execute("create database adventure default character set utf8");
		execute("drop user if exists user@localhost");
		execute("create user user@localhost identified by '1234'");
		execute("grant select,insert,update,delete on adventure.* to user@localhost");
		execute("use adventure");
		execute("set global local_infile=1");
		
		createTable("user", "u_no int primary key not null auto_increment, u_name varchar(10), u_id varchar(15), u_pw varchar(15), u_height int, u_date date, u_age int, u_disable int");
		createTable("ride", "r_no int primary key not null auto_increment, r_name varchar(15), r_floor varchar(2), r_max int, r_height varchar(15), r_old varchar(15), r_money int, r_disable int, r_explation varchar(150), r_img longblob");
		createTable("ticket", "t_no int primary key not null auto_increment, u_no int, t_date varchar(10), r_no int, t_magicpass int, foreign key(u_no) references user(u_no), foreign key(r_no) references ride(r_no)");
		
		try (var pst = con.prepareStatement("update ride set r_img = ? where r_name =?")) {
			var rs = stmt.executeQuery("select r_name from ride");
			while (rs.next()) {
				pst.setObject(1, new FileInputStream(new File("./datafiles/이미지/"+rs.getString(1)+".jpg")));
				pst.setObject(2, rs.getString(1));
				pst.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new DB();
	}
	
}
