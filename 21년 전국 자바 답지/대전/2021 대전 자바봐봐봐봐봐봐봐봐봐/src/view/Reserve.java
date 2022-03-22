package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class Reserve extends BaseFrame {

	DefaultTableModel m = model("pno,날짜,여유좌석,d".split(","));
	JTable t = new JTable(m);
	JLabel lbl;
	String date;
	DecimalFormat dec = new DecimalFormat("#,##0");
	String pno;

	public Reserve(String pno) {
		super("예매", 600, 300);
		this.pno = pno;
		data();
		ui();
		setVisible(true);
	}

	void ui() {
		add(lbl(pinfo[2], JLabel.LEFT, 20), "North");
		var c = new JPanel(new BorderLayout(20, 20));
		var e = new JPanel(new BorderLayout());
		var s = new JPanel(new FlowLayout(2));

		add(c);
		add(sz(e, 200, 1), "East");
		add(s, "South");

		c.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(5, 5, 5, 5)));
		c.add(new JLabel(img("./Datafiles/공연사진/" + pinfo[1] + ".jpg", 120, 150)), "West");
		c.add(lbl = new JLabel("<html><left>장소 : " + pinfo[3] + "<br><br>출연 : " + pinfo[5] + "<br><br>가격 : "
				+ dec.format(toInt(pinfo[4])) + "<br><br>날짜 : " + date));

		e.add(setBorder(new JScrollPane(t), new LineBorder(Color.BLACK)));

		s.add(sz(btn("예매하기", a -> {
			new Stage().addWindowListener(new Before(this));
		}), 150, 30));

		t.getColumnModel().getColumn(0).setMinWidth(0);
		t.getColumnModel().getColumn(0).setMaxWidth(0);
		t.getColumnModel().getColumn(3).setMinWidth(0);
		t.getColumnModel().getColumn(3).setMaxWidth(0);

		setBorder(e, new EmptyBorder(0, 20, 0, 0));
		setBorder((JComponent) getContentPane(), new EmptyBorder(10, 10, 10, 10));
	}

	void data() {
		try {
			System.out.println("select * from perform where p_no=" + pno);
			var rs = stmt.executeQuery("select * from perform where p_no=" + pno);
			if (rs.next()) {
				for (int i = 0; i < pinfo.length; i++) {
					pinfo[i] = rs.getString(i + 1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		date = pinfo[6];
		addRow(m,
				"select p.p_no, date_format(p.p_date, '%m. %d.'), 60 - count(*), p.p_date from perform p, ticket t where p.p_no=t.p_no and p.p_name like '%"
						+ pinfo[2] + "' group by p.p_no");
	}

}
