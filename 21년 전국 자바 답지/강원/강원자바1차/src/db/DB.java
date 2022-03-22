package db;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

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
		execute("load data local infile './지급파일/"+t+".csv' into table "+t+" fields terminated by ',' lines terminated by '\r\n' ignore 1 lines");
	}
	
	public DB() {
		execute("drop database if exists busticketbooking");
		execute("create database busticketbooking default character set utf8");
		execute("drop user if exists user@localhost");
		execute("create user user@localhost identified by '1234'");
		execute("grant select,insert,update,delete on busticketbooking.* to user@localhost");
		execute("set global local_infile=1");
		execute("use busticketbooking");
		
		createTable("user", "no int primary key not null auto_increment, id varchar(50) not null, pwd varchar(50) not null, name varchar(50) not null, email varchar(50) not null, point int not null");
		createTable("location", "no int primary key not null auto_increment, name varchar(50) not null");
		createTable("location2", "no int primary key not null auto_increment, name varchar(50) not null, location_no int not null, foreign key(location_no) references location(no)");
		createTable("schedule", "no int primary key not null auto_increment, departure_location2_no int not null, arrival_location2_no int not null, date datetime not null, elapsed_time time not null");
		createTable("reservation", "no int primary key not null auto_increment, user_no int not null, schedule_no int not null, foreign key(user_no) references user(no), foreign key(schedule_no) references schedule(no)");
		createTable("recommend", "no int primary key not null auto_increment, location_no int not null, foreign key(location_no) references location(no)");
		createTable("recommend_info", "recommend_no int not null, title varchar(50) not null, descrption varchar(1000), img longblob not null, primary key(recommend_no, title), foreign key(recommend_no) references recommend(no)");
		
		execute("create view loc as select l2.no, concat(l.name, ' ', l2.name) as name from location2 l2, location l where l2.location_no=l.no");
		
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("부산", "busan");
		hash.put("광주", "gyeongju");
		hash.put("강원도", "gangwondo");
		hash.put("전라남도", "Jeollanam-do");
		hash.put("서울", "seoul");
		try {
			PreparedStatement ps = con.prepareStatement("update recommend_info set img = ? where recommend_no = ? and title = ?");
			ResultSet rs = stmt.executeQuery("select ri.recommend_no, l.name, ri.title from recommend_info ri inner join recommend r on ri.recommend_no = r.no inner join location l on r.location_no = l.no order by recommend_no asc");
			while(rs.next()) {
				ps.setObject(1, new FileInputStream(new File("./지급파일/images/recommend/"+hash.get(rs.getString(2))+"/"+rs.getString(3)+".jpg")));
				ps.setObject(2, rs.getString(1));
				ps.setObject(3, rs.getString(3));
				ps.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		new DB();
	}
}
