package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import db.DBManager;

public class PurchaseList extends BaseFrame {
 
	String week[] = "일,월,화,수,목,금,토".split(",");
	JButton prev, next;
	LocalDate date = LocalDate.now();
	JLabel lblDate;
	JPanel days[] = new JPanel[42];
	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 MM월");
	JPanel c_c;

	public PurchaseList() {
		super("결제내역", 1000, 700);

		this.add(n = new JPanel(new FlowLayout()), "North");
		this.add(c = new JPanel(new BorderLayout()));
		
		var c_n = new JPanel(new GridLayout(0, 7));
		c.add(c_n, "North");
		for (int i = 0; i < week.length; i++) {
			var jl = new JLabel(week[i], 0);
			c_n.add(jl);
			if (i == 0) {
				jl.setForeground(Color.red);
			} else if (i == 6)
				jl.setForeground(Color.blue);
		}
		c.add(c_c = new JPanel(new GridLayout(0, 7)));

		n.add(btn("◀", a -> {
			setMonth(-1);
		}));
		n.add(lblDate = lblP(format.format(date), 0, 13, Color.black));
		n.add(btn("▶", a -> {
			setMonth(1);
		}));

		setCal();

		this.setVisible(true);
	}

	void setMonth(int months) {
		date = date.plusMonths(months);
		lblDate.setText(format.format(date));
		c_c.removeAll();
		this.setCal();
	}

	void setCal() {
		
		
		date = date.of(date.getYear(), date.getMonthValue(), 1);
		int sday = date.getDayOfWeek().getValue(), eday = date.lengthOfMonth();
		for (int i = 0; i < days.length; i++) {
			LocalDate l = LocalDate.ofEpochDay(date.toEpochDay() - sday + i);
			int d = l.getDayOfMonth();
			Color col = (i % 7 == 0 ? Color.red : (i % 7 == 6 ? Color.blue : Color.black));
			if (l.toEpochDay() < date.toEpochDay() || l.toEpochDay() > date.plusMonths(1).toEpochDay() - 1) {
				col = new Color(col.getRed(), col.getGreen(), col.getBlue(), 127);
			}
			try {
				var rs = DBManager.rs("SELECT s1.name, s2.name FROM purchase pu, station s1, station s2 where user = "+uno+" and date= '"+l.toString()+"' and s1.serial=pu.departure and s2.serial=pu.arrive order by date, pu.serial");
				String station = "<html>";
				while (rs.next()) {
					station += rs.getString(1)+"→"+rs.getString(2)+"<br>";
				}
				c_c.add(days[i] = new ChartPanel(station, col, d));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		repaint();
		revalidate();
	}
	
	class ChartPanel extends JPanel {
		JPanel n, c;
		JScrollPane scr;
		public ChartPanel(String info, Color col, int d) {
			super(new BorderLayout());
			this.add(n = new JPanel(new FlowLayout(2)), "North");
			this.add(scr=new JScrollPane(c = new JPanel(new FlowLayout(0))));
			
			n.add(lblP(String.format("%02d", d), 0, 13, col));
			c.add(lblP(info, 0, 13, Color.black));
			
			n.setBackground(Color.white);
			c.setBackground(Color.white);
			
			setLine(this);
		}
	}

	public static void main(String[] args) {
		uno = "1";
		new PurchaseList();
	}

}
