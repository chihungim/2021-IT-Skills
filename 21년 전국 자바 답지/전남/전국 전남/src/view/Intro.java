package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class Intro extends BaseFrame {
	public Intro() {
		super("지점소개", 600, 400);
		setUI();
		setVisible(true);
	}

	void setUI() {
		var n = new JPanel(new BorderLayout());
		var c = new JPanel(new BorderLayout());
		var c_n = new JPanel(new BorderLayout());
		var c_n_e = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		var c_c = new JPanel(new BorderLayout());
		var c_s = new JPanel(new BorderLayout());

		add(n, "North");
		add(c);
		c.add(c_n, "North");
		c.add(c_c);
		c.add(c_s, "South");
		c_n.add(c_n_e, "East");

		n.add(lbl("지점소개", 0, 35));
		n.add(lbl("B R A N C H  O F F I C E", 0, 15), "South");

		var title = lbl(cname, JLabel.LEFT, 15);
		title.setForeground(Color.orange);
		c_n.add(title);
		c_n_e.add(btn("테마 보러가기", a -> {
			new Theme().addWindowListener(new Before(this));
		}));

		try {
			var rs = stmt.executeQuery("select * from cafe where c_no = '" + cno + "'");
			if (rs.next()) {
				c_c.add(imglbl("./Datafiles/지점/" + cname.split(" ")[0] + ".jpg", 500, 300));
				var lbl1 = lbl(rs.getString(5), JLabel.LEFT);
				var lbl2 = lbl(rs.getString(4).replaceAll("-", "."), JLabel.CENTER);
				cprice = rs.getString("c_price");
				cdivision = rs.getString(3);
				c_s.add(lbl1, "West");
				c_s.add(lbl2);
				lbl1.setForeground(Color.LIGHT_GRAY);
				lbl2.setForeground(Color.LIGHT_GRAY);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c_n.setBorder(new MatteBorder(0, 2, 0, 0, Color.ORANGE));
		c.setBackground(Color.BLACK);
		c_n.setOpaque(false);
		c_n_e.setOpaque(false);
		c_c.setOpaque(false);
		c_s.setOpaque(false);
		c.setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	@Override
	public JLabel lbl(String cap, int alig, int size) {
		var a = super.lbl(cap, alig);
		a.setFont(new Font("HY헤드라인M", Font.BOLD, size));
		return a;
	}

	public static void main(String[] args) {
		cno = "A-001";
		cname = "에이스 강남점";
		new Intro();

	}
}
