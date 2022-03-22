import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "���� ����", "���", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	void setUserImg() {

		String sql = "update user set u_img = ? where u_no = ?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			try {
				var rs = stmt.executeQuery("select * from user");
				while (rs.next()) {
					FileInputStream fin = new FileInputStream("./Datafiles/ȸ������/" + rs.getInt(1) + ".jpg");
					ps.setBinaryStream(1, fin);
					ps.setInt(2, rs.getInt(1));
					ps.execute();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void createT(String t, String c) {
		execute("create table " + t + "(" + c + ")");
		execute("load data local infile './Datafiles/" + t + ".txt' into table " + t
				+ " lines terminated by '\r\n' ignore 1 lines");
	}

	public DB() {
		execute("drop database if exists 2021����");
		execute("create database 2021���� default character set utf8");
		execute("drop user if exists user@localhost");
		execute("create user user@localhost identified by '1234'");
		execute("grant insert,select,update,delete on 2021����.* to user@localhost");
		execute("set global local_infile=1");
		execute("use 2021����");

		createT("user",
				"u_no int primary key not null auto_increment,u_name varchar(10),u_id varchar(20),u_pw varchar(20),u_img blob");
		createT("perform",
				"p_no int primary key not null auto_increment,pf_no varchar(10),p_name varchar(20),p_place varchar(20),p_price int,p_actor varchar(20),p_date date");
		createT("ticket",
				"t_no int primary key not null auto_increment,u_no int,p_no int,t_seat varchar(50),t_discount varchar(50),foreign key(u_no) references user(u_no), foreign key(p_no) references perform(p_no)");

		setUserImg();
		JOptionPane.showMessageDialog(null, "���� ����", "����", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void main(String[] args) {
		new DB();
	}

}
