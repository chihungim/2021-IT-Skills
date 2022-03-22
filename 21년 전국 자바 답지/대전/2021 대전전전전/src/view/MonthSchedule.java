package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import db.DBManager;

public class MonthSchedule extends BaseFrame {

	JLabel m, prev, next;
	LocalDate date = LocalDate.now();
	Type types[] = { new Type("M", "뮤지컬"), new Type("O", "오페라"), new Type("C", "콘서트") };
	DayBox box[] = new DayBox[42];
	String type="";
	ArrayList<Type> music= new ArrayList<Type>(),
					opera= new ArrayList<Type>(),
					concert= new ArrayList<Type>();
	
	public MonthSchedule() {
		super("월별 일정", 900, 850);
		
		this.add(n = new JPanel(new GridLayout(0, 1)), "North");
		this.add(c = new JPanel(new GridLayout(0, 7)));
		
		var n1 = new JPanel(new FlowLayout(0));
		var n2 = new JPanel(new FlowLayout(2));
		
		n.add(n1);
		n.add(n2);
		
		n1.add(prev = lbl("◀", 0, 25, new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				date = date.plusMonths(-1);
				m.setText(date.getMonthValue()+"월");
				setCal();
			}
		}));
		n1.add(m = lbl(date.getMonthValue()+"월", 0, 25));
		n1.add(next = lbl("▶", 0, 25, new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				date = date.plusMonths(1);
				m.setText(date.getMonthValue()+"월");
				setCal();
			}
		}));
		
		for (int i = 0; i < types.length; i++) {
			n2.add(types[i]);
			
			types[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getSource().equals(types[0])) {
						type = "M";
						setCal();
					} else if (e.getSource().equals(types[1])) {
						type = "O";
						setCal();
					} else if (e.getSource().equals(types[2])) {
						type = "C";
						setCal();
					}
				}
			});
		}
		n2.add(btn("전체", a->{
			type = "";
			setCal();
		}));
		
		for (var s: "일,월,화,수,목,금,토".split(",")) {
			var jl = lbl(s, 0);
			c.add(jl);
			if (s.equals("일")) jl.setForeground(Color.red); 
			if (s.equals("토")) jl.setForeground(Color.blue); 
		}
		
		for (int i = 0; i < box.length; i++) {
			c.add(box[i] = new DayBox());
			setLine(box[i], Color.black);
			
			if (i%7==0) box[i].day.setForeground(Color.red);
			if (i%7==6) box[i].day.setForeground(Color.blue);
		}
		
		setCal();
		setEmpty((JPanel)getContentPane(), 10, 10, 10, 10);
		this.setVisible(true);
		
	}
	
	void setCal() {
		music.clear();
		opera.clear();
		concert.clear();
	
		date = date.of(date.getYear(), date.getMonthValue(), 1);
		
		int s = date.getDayOfWeek().getValue() % 7, e = date.lengthOfMonth();
		for (int i = 0; i < 42; i++) {
			var d = date.plusDays(i - s);
			box[i].c.removeAll();
			
			if (d.getMonthValue() != date.getMonthValue()) {
				box[i].c.setVisible(false);
				box[i].day.setVisible(false);
			} else {
				box[i].c.setVisible(true);
				box[i].day.setVisible(true);
				
				box[i].day.setText(d.getDayOfMonth()+"");
				
				try {
					var rs = DBManager.rs("SELECT pf_no, p_name FROM perform where pf_no like '%"+type+"%' and p_date = '"+d+"'");
					while (rs.next()) {
						String t = rs.getString(1).substring(0, 1);
						var typeP = new Type(t, rs.getString(2));
						box[i].c.add(sz(typeP, 100, 20));
						
						if (t.equals("M")) {
							music.add(typeP);
						} else if (t.equals("O")) {
							opera.add(typeP);
						} else if (t.equals("C")) {
							concert.add(typeP);
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				repaint();
				revalidate();
			}
		}
		
		
	}
	
	class DayBox extends JPanel {
		
		JLabel day;
		JPanel c;
		
		public DayBox() {
			super(new BorderLayout());
			
			this.add(day = new JLabel("", 4), "North");
			this.add(c = new JPanel(new FlowLayout(0)));
		}
	}
	
	class Type extends JPanel {
		
		JLabel lbl;
		public Type(String t, String c) {
			
			this.setLayout(new BorderLayout());
			this.add(lbl = sz(new JLabel(t, 0), 20, 20), "West");
			this.add(new JLabel(c));
			
			setLine(lbl, Color.black);
			lbl.setForeground(Color.white);
			lbl.setOpaque(true);
			
			switch (t) {
			case "M":
				lbl.setBackground(Color.magenta.brighter());
				break;
			case "O":
				lbl.setBackground(Color.blue);
				break;
			case "C":
				lbl.setBackground(Color.orange);
				break;
			}
			
		}
	}
	
	public static void main(String[] args) {
		new MonthSchedule();
	}
	
}
