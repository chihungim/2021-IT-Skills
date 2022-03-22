package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import db.DBManager;
import util.Util;

public class AddNotice extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new BorderLayout()), c = new JPanel(new BorderLayout()), s = new JPanel(new FlowLayout(2));
	JPanel n_n = new JPanel(new BorderLayout()), n_c = new JPanel(new BorderLayout()), c_w = new JPanel(),
			c_c = new JPanel(new FlowLayout(0));
	JPanel n_n_w = new JPanel(new GridLayout(0, 1, 10, 10)), n_n_c = new JPanel(new GridLayout(0, 1, 10, 10)),
			n_c_w = new JPanel(), n_c_c = new JPanel();
	JLabel lbl[] = { Util.lbl("아이디", 2), Util.lbl("등록일", 2), Util.lbl("제목", 2), Util.lbl("내용", 2), Util.lbl("공개여부", 2) };
	JTextField txt[] = { new JTextField(20), new JTextField(15), new JTextField(15) };
	JTextArea area = new JTextArea();
	JRadioButton radio[] = { new JRadioButton("공개"), new JRadioButton("비공개") };
	JButton btn[] = { new JButton("등록"), new JButton("취소") };
	String isOpened = "";

	public AddNotice() {
		super("게시물 등록", 350, 420);

		ui();
		data();
		event();
		setVisible(true);
	}

	void event() {
		for (int i = 0; i < radio.length; i++) {
			radio[0].addActionListener(a -> {
				isOpened = "O";
			});
			radio[1].addActionListener(a -> {
				isOpened = "X";
			});
		}

		for (int i = 0; i < btn.length; i++) {
			btn[i].addActionListener(a -> {
				if (a.getActionCommand().equals("등록")) {
					if (txt[0].getText().equals("") || txt[1].getText().equals("") || txt[2].getText().equals("")
							|| area.getText().equals("")) {
						Util.eMsg("빈칸이 있습니다.");
						return;
					}
					Util.iMsg("게시물이 등록되었습니다.");
					int no = 0;
					try {
						ResultSet rs = DBManager.rs("select u_no from user where u_id = '" + uid + "'");
						if (rs.next()) {
							no = rs.getInt(1);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DBManager.execute("insert notice values(0, '" + txt[1].getText() + "','" + no + "','" + txt[2].getText()
							+ "','" + area + "','" + 0 + "','" + isOpened + "')");
					new Notice().addWindowListener(new Before(AddNotice.this));
				}
			});
		}
	}

	void data() {
		area.setPreferredSize(new Dimension(220, 200));
		area.setBorder(new LineBorder(Color.BLACK));
		txt[0].setText(uid);
		txt[0].setEnabled(false);
		txt[1].setText(ndate);
		txt[1].setEnabled(false);
	}

	void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");
		mainp.add(s, "South");
		n.add(n_n, "North");
		n.add(n_c, "Center");
		c.add(c_w, "West");
		c.add(c_c, "Center");
		n_n.add(n_n_w, "West");
		n_n.add(n_n_c, "East");
		n_c.add(n_c_w, "West");
		n_c.add(n_c_c, "East");

		for (int i = 0; i < 3; i++) {
			n_n_w.add(lbl[i]);
			n_n_c.add(txt[i]);
			txt[i].setBorder(new LineBorder(Color.black));
		}
		n_c_w.add(lbl[3]);
		n_c_c.add(area);
		c_w.add(lbl[4]);
		c_c.add(radio[0]);
		c_c.add(radio[1]);
		s.add(btn[0]);
		s.add(btn[1]);

		n_n_c.setBorder(new EmptyBorder(0, 0, 0, 15));
		n_n_w.setBorder(new EmptyBorder(0, 15, 0, 0));
		n_c_c.setBorder(new EmptyBorder(0, 0, 0, 15));
		n_c_w.setBorder(new EmptyBorder(60, 10, 0, 0));
		c_w.setBorder(new EmptyBorder(0, 10, 0, 15));
	}

	public static void main(String[] args) {
		new AddNotice();
	}
}
