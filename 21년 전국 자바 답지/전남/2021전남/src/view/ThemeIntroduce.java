package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import db.DBManager;
import util.Util;

public class ThemeIntroduce extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(), c = new JPanel(), s = new JPanel(new BorderLayout());
	JPanel s_w = new JPanel(new GridLayout()), s_e = new JPanel(new BorderLayout());
	JPanel s_e_n = new JPanel(new BorderLayout()), s_e_c = new JPanel(), s_e_s = new JPanel(new BorderLayout());
	JPanel s_e_n_w = new JPanel(new FlowLayout(0)), s_e_n_e = new JPanel(new FlowLayout(2));
	JPanel s_e_s_n = new JPanel(new FlowLayout(0)), s_e_s_c = new JPanel(new FlowLayout(0)),
			s_e_s_s = new JPanel(new FlowLayout(0));
	JLabel lbl[] = { Util.lbl("", 0, 20), Util.lbl("Theme Introduction", 0, 10) };
	JLabel imglbl = new JLabel();
	JLabel name = new JLabel(tname), explan = new JLabel();
	JLabel idx[] = { new JLabel("제한시간 : ", 2), new JLabel("난이도 : ", 2), new JLabel("추천 인원 : ", 2), new JLabel(),
			new JLabel(), new JLabel() };
	JButton btn = new JButton("예약하기");

	public ThemeIntroduce() {
		super("테마소개", 600, 500);

		data();
		ui();
		event();
		setVisible(true);
	}

	void event() {
		btn.addActionListener(a -> {
			new Reserve().addWindowListener(new Before(ThemeIntroduce.this));
		});
	}

	void data() {
		name.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		String star = "";
		try {
			ResultSet rs = DBManager.rs(
					"SELECT t_time, t_difficulty, t_personnel FROM theme where t_name = '" + tname + "'");
			if (rs.next()) {
				idx[3].setText(rs.getInt(1) + "분");
				for (int i = 0; i < rs.getInt(2); i++) {
					star += "★";
				}
				idx[4].setText(star);
				idx[5].setText(rs.getInt(3) + "");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");
		mainp.add(s, "South");
		s.add(s_w, "West");
		s.add(s_e, "East");
		s_w.add(imglbl);
		s_e.add(s_e_c, "Center");
		s_e.add(s_e_n, "North");
		s_e.add(s_e_s, "South");
		s_e_n.add(s_e_n_w, "West");
		s_e_n.add(s_e_n_e, "East");
		s_e_s.add(s_e_s_n, "North");
		s_e_s.add(s_e_s_c, "Center");
		s_e_s.add(s_e_s_s, "South");

		imglbl.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("Datafiles/테마/" + tno + ".jpg")
				.getScaledInstance(320, 350, Image.SCALE_SMOOTH)));
		s_e_n_w.add(name);
		s_e_n_e.add(btn);
		s_e_s_n.add(idx[0]);
		s_e_s_n.add(idx[3]);
		s_e_s_c.add(idx[1]);
		s_e_s_c.add(idx[4]);
		s_e_s_s.add(idx[2]);
		s_e_s_s.add(idx[5]);
	}

	public static void main(String[] args) {
		tno = 1;
		tname = "404호 살인사건";
		new ThemeIntroduce();
	}
}
