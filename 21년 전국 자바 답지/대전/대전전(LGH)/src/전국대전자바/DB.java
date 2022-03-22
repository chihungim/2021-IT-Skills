package 전국대전자바;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DB {
	static Connection con;
	static Statement stmt;

	static {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true&allowPublicKeyRetrieval=true",
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
			JOptionPane.showMessageDialog(null, "셋팅 실패", "경고", JOptionPane.ERROR_MESSAGE);
		}
	}

	void createT(String t, String c) {
		execute("create table " + t + "(" + c + ")");
		execute("load data local infile './Datafiles/" + t + ".txt' into table " + t
				+ " lines terminated by '\r\n' ignore 1 lines");
	}

	public DB() {
		execute("drop database if exists 2021전국");
		execute("create database 2021전국 default character set utf8");
		execute("drop user if exists user@localhost");
		execute("create user user@localhost identified by '1234'");
		execute("grant insert,select,update,delete on 2021전국.* to user@localhost");
		execute("set global local_infile=1");
		execute("use 2021전국");

		createT("user",
				"u_no int primary key not null auto_increment,u_name varchar(10),u_id varchar(20),u_pw varchar(20),u_img blob");
		createT("perform",
				"p_no int primary key not null auto_increment,pf_no varchar(10),p_name varchar(20),p_place varchar(20),p_price int,p_actor varchar(20),p_date date");
		createT("ticket",
				"t_no int primary key not null auto_increment,u_no int,p_no int,t_seat varchar(50),t_discount varchar(50),foreign key(u_no) references user(u_no), foreign key(p_no) references perform(p_no)");

		JOptionPane.showMessageDialog(null, "셋팅 성공", "정보", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void main(String[] args) {
		new DB();
	}

}
