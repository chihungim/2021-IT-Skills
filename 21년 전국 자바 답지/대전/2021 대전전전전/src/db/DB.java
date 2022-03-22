package db;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

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
		execute("load data local infile './Datafiles/"+t+".txt' into table "+t+" lines terminated by '\r\n' ignore 1 lines");
	}
	
	public DB() {
		execute("drop database if exists 2021����");
		execute("create database 2021���� default character set utf8");
		execute("drop user if exists user@localhost");
		execute("create user user@localhost identified by '1234'");
		execute("grant select,insert,update,delete on 2021����.* to user@localhost");
		execute("set global local_infile= 1");
		execute("use 2021����");
		
		createTable("user", "u_no int primary key not null auto_increment, u_name varchar(10), u_id varchar(20), u_pw varchar(20), u_img blob");
		createTable("perform", "p_no int primary key not null auto_increment, pf_no varchar(10), p_name varchar(20), p_place varchar(20), p_price int, p_actor varchar(20), p_date date");
		createTable("ticket", "t_no int primary key not null auto_increment, u_no int, p_no int, t_seat varchar(50), t_discount varchar(50), foreign key(u_no) references user(u_no) on update cascade on delete cascade, foreign key(p_no) references perform(p_no) on delete cascade on update cascade");
		
		try (var pst = con.prepareStatement("update user set u_img=? where u_no=?")){
			for (int i = 1; i < 11; i++) {
				pst.setObject(1, new FileInputStream(new File("./Datafiles/ȸ������/"+i+".jpg")));
				pst.setObject(2, i);
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
