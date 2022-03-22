import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DB {
	static Connection con;
	static Statement stmt;

	String cascade = "on delete cascade on update cascade";

	static {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true&allowPublicKeyRetrieval=true",
					"root", "1234");
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
		execute("create table " + t + "(" + c + ")");
		execute("load data local infile './��������/" + t + ".csv' into table " + t
				+ " fields terminated by ',' lines terminated by '\r\n' ignore 1 lines ");
	}

	public DB() {
		execute("drop database if exists busticketbooking");
		execute("create database busticketbooking default character set utf8");
		execute("drop user if exists user@localhost");
		execute("create user user@localhost identified by '1234'");
		execute("use busticketbooking");
		execute("grant update, insert, select, delete on busticketbooking.* to user@localhost");
		execute("set global local_infile = 1");

		createTable("user",
				"no int primary key not null auto_increment, id varchar(50) not null, pwd varchar(50) not null, name varchar(50) not null, email varchar(50) not null, point int not null");
		createTable("location", "no int primary key not null auto_increment, name varchar(50) not null");
		createTable("location2",
				"no int primary key not null auto_increment, name varchar(50) not null, location_no int not null, foreign key(location_no) references location(no)"
						+ cascade);
		createTable("schedule",
				"no int primary key not null auto_increment, departure_location2_no int not null, arrival_location2_no int not null, date datetime not null, elapsed_time time not null, foreign key(departure_location2_no) references location2(no)"
						+ cascade + ", foreign key(arrival_location2_no) references location2(no)" + cascade);
		createTable("reservation",
				"no int primary key not null auto_increment, user_no int not null, schedule_no int not null, foreign key(user_no) references user(no)"
						+ cascade + ", foreign key(schedule_no) references schedule(no)" + cascade);
		createTable("recommend",
				"no int primary key not null auto_increment, location_no int not null, foreign key(location_no) references location(no)"
						+ cascade);
		createTable("recommend_info",
				"recommend_no int not null auto_increment, title varchar(50) not null, description varchar(200), img longblob not null, primary key(recommend_no, title), foreign key(recommend_no) references recommend(no)"
						+ cascade);

		try {
			PreparedStatement ps = con
					.prepareStatement("update recommend_info set img=? where title=? and recommend_no=?");
			var rs = stmt.executeQuery(
					"select ri.recommend_no, l.name, ri.title, description, ri.img from recommend_info ri, recommend r, location l where ri.recommend_no = r.no and r.location_no = l.no order by recommend_no asc");

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("�λ�", "busan");
			map.put("������", "gangwondo");
			map.put("���󳲵�", "Jeollanam-do");
			map.put("����", "gyeongju");
			map.put("����", "seoul");

			while (rs.next()) {
				ps.setObject(1, new FileInputStream(new File(
						"��������/images/recommend/" + map.get(rs.getString(2)) + "/" + rs.getString(3) + ".jpg")));
				ps.setObject(2, rs.getString(3));
				ps.setObject(3, rs.getString(1));
				ps.execute();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		execute("create view a as select l2.no, concat(l.name, ' ', l2.name) as name from location2 l2, location l where l2.location_no = l.no");
	}

	public static void main(String[] args) {
		new DB();
	}
}
