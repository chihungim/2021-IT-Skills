package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import db.DBManager;
import util.Util;

public class ThemeList extends BaseFrame {
	
	JPanel n, c;
	JScrollPane scr;
	
	public ThemeList() {
		super("테마목록", 450, 600);
	
		add(n = new JPanel(new BorderLayout()), "North");
		add(scr = new JScrollPane(c = new JPanel(new GridLayout(0, 1))));
		
		n.add(Util.lbl(cname, 0, 35));
		n.add(Util.lbl("T H E M E  L I S T", 0, 15), "South");
		
		try {
//			SELECT t.t_no, t.t_name FROM cafe c, theme t, reservation r where c.c_no = r.c_no and t.t_no = r.t_no and c.c_name = '에이스 광주1호점' group by t.t_name
			System.out.println("SELECT t.t_no, t.t_name FROM cafe c, theme t, reservation r where c.c_no = r.c_no and t.t_no = r.t_no and c.c_name = '"
					+ cname + "' group by t.t_name");
			var rs = DBManager.rs("SELECT t.t_no, t.t_name FROM cafe c, theme t, reservation r where c.c_no = r.c_no and t.t_no = r.t_no and c.c_name = '"
					+ cname + "' group by t.t_name");
			while(rs.next()) {
				Item item = new Item(rs.getString(2), rs.getInt(1));
				c.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Search();
	}
	
	class Item extends JPanel {
		
		JPanel s;
		
		public Item(String name, int theme) {
			System.out.println(name +"/"+theme);
			setName(name);
			setLayout(new BorderLayout());
			add(new JLabel(Util.img("./Datafiles/테마/"+theme+".jpg", 320, 400)));
			add(s = new JPanel(new FlowLayout(FlowLayout.CENTER)), "South");
			s.add(Util.lbl(name, 0));
			s.add(Util.btn("테마소개", a->{
				tno = theme;
				tname = getName();
				new ThemeIntroduce().addWindowListener(new Before(ThemeList.this));
			}));
		}
	}
}
