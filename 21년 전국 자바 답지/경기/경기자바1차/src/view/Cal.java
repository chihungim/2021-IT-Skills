package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JPanel;

import db.DBManager;

public class Cal extends BaseFrame {

	String week[] = "일,월,화,수,목,금,토".split(",");
	LocalDate date = LocalDate.of(2021, 10, 1);
	DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy년 MM월");
	JLabel lblAll, lblDate;
	JPanel day[] = new JPanel[42];
	JLabel lbl[] = new JLabel[42];
	DecimalFormat dec = new DecimalFormat("#,##0");

	public Cal() {
		super("월별 분석", 650, 700);

		this.add(n = new JPanel(new FlowLayout(1, 60, 0)), "North");
		this.add(c = new JPanel(new GridLayout(0, 7)));

		n.add(btn("◀", a -> {
			setMonth(-1);
		}));
		n.add(lblDate = lbl(f.format(date), 0, 25));
		n.add(btn("▶", a -> {
			setMonth(1);
		}));

		n.add(lblAll = lbl("", 2, 20));

		for (int i = 0; i < week.length; i++) {
			var lbl = new JLabel(week[i], 0);
			c.add(lbl);
			if (i == 0) {
				lbl.setForeground(Color.red);
			} else if (i == 6)
				lbl.setForeground(Color.blue);
		}

		for (int i = 0; i < day.length; i++) {
			c.add(day[i] = new JPanel(new BorderLayout()));
			day[i].add(lbl[i] = new JLabel(), "North");
			lbl[i].setHorizontalAlignment(JLabel.RIGHT);
			day[i].setVisible(false);
			setLine(day[i], Color.black);
			day[i].setOpaque(false);
		}

		n.setOpaque(false);
		c.setOpaque(false);

		drawCal();

		setEmpty((JPanel) getContentPane(), 5, 5, 5, 5);
		this.setVisible(true);
	}

	void setMonth(int m) {
		if (date.plusMonths(m).isAfter(LocalDate.now())) {
			iMsg("미래는 매출실적을 확인할 수 없습니다.");
			return;
		}
		date = date.plusMonths(m);
		drawCal();
	}

	void drawCal() {
		date = date.of(date.getYear(), date.getMonthValue(), 1);
		int sday = date.getDayOfWeek().getValue() % 7, eday = date.lengthOfMonth();
		lblDate.setText(f.format(date));
		int person = 0, price = 0;

		for (int i = 0; i < day.length; i++) {
			day[i].setVisible(false);
		}

		for (int i = sday; i < sday + eday; i++) {
			day[i].setVisible(true);
			lbl[i].setText(i - sday + 1 + "");

			String msg = "<html><font color = \"green\">";
			try {
				var rs = DBManager
						.rs("SELECT count(*), sum(r_money) FROM ticket t, ride r where r.r_no=t.r_no and t.t_date='"
								+ LocalDate.of(date.getYear(), date.getMonthValue(), i - sday + 1).toString()
								+ "' group by t.t_date");
				if (rs.next()) {
					msg += rs.getString(1) + "명";
					msg += "<br><font color = \"red\">" + dec.format(rs.getInt(2)) + "원";
					person += rs.getInt(1);
					price += rs.getInt(2);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			day[i].add(new JLabel(msg));
		}
		lblAll.setText(
				"<html><font color = \"green\">" + person + "명<br><font color = \"red\">" + dec.format(price) + "원");
	}

	public static void main(String[] args) {
		new Cal();
	}

}
