package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import db.DBManager;

public class Inform extends BaseFrame {

	JTabbedPane tab = new JTabbedPane();
	String cap[] = "조회수,급여,제목,근무기간,직종,근무요일,고용형태,근무시간,마감일,복리후생".split(",");
	String info[] = new String[cap.length];
	String benefit[] = "국민연금,고용보험,산재보험,건강보험,상해보험,퇴직연금,사학연금,정기보너스,우수사원포상,장기근속자포상,인센티브제,휴가,유연근무제,시간선택제,자율근무제,차량유류비지급,통근버스 운행,야간교통비지급,기숙사운영,사택제공,아침식사 제공,점심식사 제공,저녁식사 제공,간식제공,건강검진,진료비지원,금연수당"
			.split(",");
	String path;

	public Inform(String cno) {
		super("상세정보", 700, 500);
		try {
			var rs = DBManager.rs("SELECT r.hits, concat(r.standard, ' ', r.salary, '원'), r.title, r.period, d.name, r.week, e.name, r.time, r.deadline, r.benefit FROM recruitment r, details d, employment e where r.d_no=d.d_no and r.e_no=e.e_no and c_no="+cno);
			if (rs.next()) {
				for (int i = 0; i < info.length; i++) {
					info[i] = rs.getString(i+1);
				}
				String eno[] = info[9].split(",");
				info[9] = "";
				for (int i = 0; i < eno.length; i++) {
					eno[i] = benefit[rei(eno[i])-1];
				}
				info[9] = String.join(",", eno);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.add(n = new JPanel(new BorderLayout()), "North");
		n.add(lbl(DBManager.getOne("select title from recruitment where c_no="+cno), JLabel.CENTER, 25));
		n.add(btn("지원하기", a -> {

		}), "East");
		

		this.add(c = new JPanel(new BorderLayout()));
		c.add(tab);

		var c_c = new JPanel(new GridLayout(0, 2));
		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(new JLabel(cap[i]), 100, 25));
			
			if (i==1) {
				tmp.add(sz(new JLabel(info[i]), 80, 25));
				tmp.add(btn("급여게산기", a->{
					new Calc();
				}));
			} else {
				tmp.add(sz(new JLabel(info[i]), 200, 25));
			}
			c_c.add(tmp);
		}
		tab.add("모집,근무조건", c_c);
		
		var c_info = new JPanel(new BorderLayout());
		var c_info_c = new JPanel(new GridLayout(0, 1));
		var c_info_e = new JPanel();
		c_info.add(c_info_c);
		c_info.add(c_info_e, "East");
		sz(c_info, 1, 800);
		tab.add("기업정보", new JScrollPane(c_info));
		
		try {
			var rs = DBManager.rs("select c_no, name, entrepreneur, address, detail from company where c_no="+cno);
			if (rs.next()) {
				path = isPath(rs.getString(1));
				c_info_c.add(new JLabel("채용기업정보", 2));
				String cap[] = "기업이름,대표자,회사주소,사업내용".split(",");
				for (int i = 0; i < 4; i++) {
					var tmp = new JPanel(new FlowLayout(0));
					tmp.add(sz(new JLabel(cap[i]), 100, 25));
					tmp.add(sz(new JLabel(rs.getString(i+2)), 300, 25));
					c_info_c.add(tmp);
				}
				JLabel img;
				c_info_e.add(img=new JLabel(img(path, 180, 150)));
				setLine(img);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		var c_info_s = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//				g2.setFont(new Font("굴림", 20, Font.PLAIN));
//				System.out.println(DBManager.getOne("select d_no from details where name ='"+info[4]+"'"));
				
				
			
				
				
			}
		};
		sz(c_info_s, 1, 500);
		c_info.add(c_info_s, "South");
		
		setEmpty(c_info, 15, 15, 15, 15);
		setEmpty(c_c, 10, 10, 10, 10);
		setEmpty(c, 10, 0, 0, 0);
		setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Inform("1");
	}


}
