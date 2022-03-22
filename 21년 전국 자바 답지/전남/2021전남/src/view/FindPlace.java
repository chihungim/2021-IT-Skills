package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import db.DBManager;
import util.Util;

public class FindPlace extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(), c = new JPanel(new GridLayout(0, 4, 10, 10));
	JComboBox com[] = { new JComboBox(), new JComboBox() };
	JScrollPane jsc = new JScrollPane(c);

	public FindPlace() {
		super("지역별 지점", 870, 600);

		ui();
		data();
		event();
		setVisible(true);
	}

	void event() {
		for (int i = 0; i < com.length; i++) {
			com[0].addActionListener(a -> {
				c.removeAll();
				com[1].setSelectedItem("지점선택");
				LocationSearch(com[0].getSelectedItem().toString());
				repaint();
				revalidate();
			});

			com[1].addActionListener(a -> {
				c.removeAll();
				search(com[0].getSelectedItem().toString(), com[1].getSelectedItem().toString());
				repaint();
				revalidate();
			});
		}
	}

	void data() {
		com[0].addItem("지역선택");
		com[1].addItem("지점선택");
		try {
			ResultSet rs = DBManager.rs("SELECT left(c_address, 2) from cafe group by left(c_address, 2)");
			while (rs.next()) {
				com[0].addItem(rs.getString(1));
				com[0].setPreferredSize(new Dimension(130, 25));
			}
			ResultSet rs2 = DBManager.rs("select c_name from cafe");
			while (rs2.next()) {
				com[1].addItem(rs2.getString(1).split(" ")[1]);
				com[1].setPreferredSize(new Dimension(130, 25));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		EntireSearch();

	}

	void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(jsc, "Center");

		n.add(com[0]);
		n.add(com[1]);

	}

	void search(String c_name, String c_address) {
		try {
			ResultSet rs = DBManager.rs("select c_name from cafe where c_address like '%" + c_name
					+ "%' and c_name like '%" + c_address + "%'");
			while (rs.next()) {
				Index idx = new Index(rs.getString(1));
				idx.setPreferredSize(new Dimension(200, 210));
				c.add(idx);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void LocationSearch(String c_name) {
		try {
			ResultSet rs = DBManager.rs("select c_name from cafe where c_address like '%" + c_name + "%'");
			while (rs.next()) {
				Index idx = new Index(rs.getString(1));
				idx.setPreferredSize(new Dimension(200, 210));
				c.add(idx);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void EntireSearch() {
		try {
			ResultSet rs = DBManager.rs("select c_name from cafe");
			while (rs.next()) {
				Index idx = new Index(rs.getString(1));
				idx.setPreferredSize(new Dimension(200, 210));
				c.add(idx);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new FindPlace();
	}

	class Index extends JPanel {
		JPanel mainp = new JPanel(new BorderLayout());
		JPanel n = new JPanel(), c = new JPanel(), s = new JPanel();
		JLabel imglbl = new JLabel(), name = new JLabel();
		JButton btn[] = { new JButton("지점소개"), new JButton("예약하기") };
		int width, height;
		String c_name;

		public Index(String c_name) {
			setLayout(new BorderLayout());
			this.c_name = c_name;

			setUI();
		}

		void setUI() {
			add(mainp);
			mainp.add(n, "North");
			mainp.add(c, "Center");
			mainp.add(s, "South");

			n.add(imglbl);
			c.add(name);
			s.add(btn[0]);
			s.add(btn[1]);

			imglbl.setIcon(
					new ImageIcon(Toolkit.getDefaultToolkit().getImage("Datafiles/지점/" + c_name.split(" ")[0] + ".jpg")
							.getScaledInstance(180, 150, Image.SCALE_SMOOTH)));
			name.setText(c_name);
			for (int i = 0; i < btn.length; i++) {
				btn[i].setPreferredSize(new Dimension(90, 20));
				btn[i].setName(c_name);
				int idx = i;
				btn[i].addActionListener(a -> {
					if (a.getActionCommand().equals("지점소개")) {
						FindPlace.cname = btn[idx].getName();
						cafename = c_name.split(" ")[0];
						new Introduce().addWindowListener(new Before(FindPlace.this));
					} else {
						new Reserve().addWindowListener(new Before(FindPlace.this));
					}
				});
			}
		}
	}
}
