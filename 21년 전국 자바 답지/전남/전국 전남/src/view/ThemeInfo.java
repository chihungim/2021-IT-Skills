package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ThemeInfo extends BaseFrame {

	String star = "";

	public ThemeInfo() {
		super("테마 소개", 600, 500);
		setUI();
		setVisible(true);
	}

	void setUI() {
		var n = new JPanel(new BorderLayout());
		var c = new JPanel(new GridLayout(1, 0));
		var c_c = new JPanel(new BorderLayout());
		var c_c_n = new JPanel(new BorderLayout());
		var c_c_n_e = new JPanel(new FlowLayout());
		add(n, "North");
		add(c);

		c.add(imglbl("./Datafiles/테마/" + tno + ".jpg", 290, 400));
		c.add(c_c);

		c_c.add(c_c_n, "North");
		c_c_n.add(c_c_n_e, "East");

		n.add(lbl("<html><font color = \"BLACK\">테마소개", 0, 30));
		n.add(lbl("<html><font color = \"BLACK\">Theme Introduction", 0, 15), "South");

		c_c_n.add(lbl(tname, JLabel.LEFT, 20));
		c_c_n_e.add(btn("예약하기", a -> {
			new Reserve(location, cname, tname + "").addWindowListener(new Before(this));
		}));
		c_c.add(lbl("<html>" + texplain, JLabel.LEFT, 16));

		for (int i = 0; i < toInt(tdfficulty); i++) {
			star = star + "★";
		}

		c_c.add(lbl("<html>제한시간:" + ttime + "분<br>난이도:" + tdfficulty + "<br>추천인원:" + tpersonnel, JLabel.LEFT, 15),
				"South");

		c_c_n.setOpaque(false);
		c_c_n_e.setOpaque(false);
		c_c.setOpaque(false);
		c.setOpaque(false);
		((JPanel) getContentPane()).setOpaque(true);
		((JPanel) getContentPane()).setBackground(Color.BLACK);
	}

	@Override
	public JLabel lbl(String cap, int alig, int size) {
		var a = super.lbl(cap, alig, size);
		a.setForeground(Color.WHITE);
		return a;
	}

}
