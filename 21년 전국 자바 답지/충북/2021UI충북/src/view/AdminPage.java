package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalTime;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class AdminPage extends BasePage {

	DefaultTableModel m_ar = model("번호,이름".split(",")), m_al = model("번호,앨범명,장르,곡 수,발매일".split(",")),
			m_s = model("번호,제목,길이,타이틀곡".split(","));

	JTable t_ar = table(m_ar), t_al = table(m_al), t_s = table(m_s);
	JLabel imglbl, title;

	JTextField songmin, songsec, sField, titleField;
	JRadioButton btn[] = new JRadioButton[2];
	ButtonGroup bg = new ButtonGroup();

	public AdminPage() {
		ui();
		addEvents();
		search();
		//////////////////////////////////
	}

	void ui() {
		var n = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		var c = new JPanel(new GridLayout(1, 0, 5, 5));

		var c1 = new JPanel(new BorderLayout(5, 5));
		var c3 = new JPanel(new BorderLayout(5, 5));
		var c1_n = new JPanel(new BorderLayout(5, 5));
		var c3_n = new JPanel(new BorderLayout(5, 5));
		var c3_c = new JPanel(new BorderLayout(5, 5));
		var c3_c_s = new JPanel(new GridLayout(0, 1));
		// lv1
		add(n, "North");
		add(c);

		// lv2

		for (var bcap : "통계,로그아웃".split(",")) {
			n.add(btn(bcap, a -> {

			}));
		}

		c.add(c1);
		c.add(new JScrollPane(t_al));
		c.add(c3);

		// lv3
		c1.add(c1_n, "North");

		c3.add(c3_n, "North");
		c3.add(c3_c);
		c3_c.add(c3_c_s, "South");

		c1_n.add(sField = new JTextField());
		c1_n.add(btn("검색하기", a -> search()), "East");

		c1.add(new JScrollPane(t_ar));

		c3_n.add(size(imglbl = new JLabel(), 150, 150), "West");
		c3_n.add(title = lbl("", 6, "LEFT"));
		c3_c.add(new JScrollPane(t_s));

		var bcap = "제목,길이,대표 곡".split(",");
		for (int i = 0; i < 3; i++) {
			var tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			tmp.add(size(lbl(bcap[i], JLabel.LEFT, 0, 10), 40, 10));
			switch (i) {
			case 0:
				tmp.add(titleField = new JTextField(30));
				break;
			case 1:
				tmp.add(songmin = new JTextField(5));
				tmp.add(lbl("분", 0, 0, 10));
				tmp.add(songsec = new JTextField(5));
				tmp.add(lbl("초", 0, 0, 10));
				break;
			default:
				for (int j = 0; j < 2; j++) {
					tmp.add(btn[j] = new JRadioButton("예,아니요".split(",")[j]));
					btn[j].setOpaque(false);
					bg.add(btn[j]);
					btn[j].setForeground(Color.WHITE);
				}
				break;
			}
			c3_c_s.add(tmp);
			tmp.setOpaque(false);
		}

		c3.add(btn("수정", a -> {
			if (t_s.getSelectedRow() == -1) {
				eMsg("수정할 음악을 선택해야 합니다.");
				return;
			}

			if (songsec.getText().isEmpty() || songmin.getText().isEmpty() || titleField.getText().isEmpty()) {
				eMsg("모든 항목을 입력해야합니다.");
				return;
			}

			try {
				var rs = stmt
						.executeQuery("select * from song where album = " + t_al.getValueAt(t_al.getSelectedRow(), 0)
								+ " and serial <> " + t_s.getValueAt(t_s.getSelectedRow(), 0) + " and name like '%"
								+ titleField.getText().replaceAll("'", "\\\\'") + "%'");
				if (rs.next()) {
					eMsg("같은 애범에 동일한 제목의 음악이 존재합니다.");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String time = "";

			try {
				time = LocalTime.of(0, toInt(songmin.getText()), toInt(songsec.getText())).toString();
			} catch (Exception e) {
				eMsg("시간을 올바르게 입력해야 합니다.");
				return;
			}

			if (btn[0].isSelected()) {
				execute("update song set titlesong = 0 where album = " + t_al.getValueAt(t_al.getSelectedRow(), 0));
			}

			var idx = btn[0].isSelected() ? 1 : 0;

			execute("update song set titlesong = " + idx + ", name = '" + titleField.getText().replaceAll("'", "\\\\'")
					+ "', length = '" + time + "' where serial = " + t_s.getValueAt(t_s.getSelectedRow(), 0));

			addrow("select serial, name, right(length,5), if(titlesong, '예', '아니요') from song where album = "
					+ t_al.getValueAt(t_al.getSelectedRow(), 0), m_s);

			iMsg("선택한 음악을 수정했습니다.");

			title.setText("");
			bg.clearSelection();
			songmin.setText("");
			songsec.setText("");

		}), "South");

		//

		imglbl.setBorder(new LineBorder(Color.WHITE));
		c.setOpaque(false);
		n.setOpaque(false);
		c1.setOpaque(false);
		c1_n.setOpaque(false);
		c3.setOpaque(false);
		c3_c.setOpaque(false);
		c3_c_s.setOpaque(false);
		c3_n.setOpaque(false);
		setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	void addEvents() {
		t_ar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (t_ar.getSelectedRow() == -1)
					return;

				clear();
				var ar = t_ar.getValueAt(t_ar.getSelectedRow(), 0);
				addrow("select al.serial, al.name, c.name, count(s.serial), date_format(al.release, '%Y년 %m월 %d일') from album al, category c, song s where s.album = al.serial and c.serial = al.category and al.artist = "
						+ ar + " group by al.serial", m_al);
			}
		});

		t_al.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (t_al.getSelectedRow() == -1)
					return;

				var al = t_al.getValueAt(t_al.getSelectedRow(), 0);
				var name = t_al.getValueAt(t_al.getSelectedRow(), 1).toString();

				imglbl.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/album/" + al + ".jpg")
						.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));

				title.setText(name);

				addrow("select serial, name, right(length,5), if(titlesong, '예', '아니요') from song where album = " + al,
						m_s);

				super.mousePressed(e);
			}
		});

		t_s.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (t_s.getSelectedRow() == -1)
					return;

				var title = t_s.getValueAt(t_s.getSelectedRow(), 1).toString();
				var length = t_s.getValueAt(t_s.getSelectedRow(), 2).toString();
				var idx = t_s.getValueAt(t_s.getSelectedRow(), 3).toString().equals("예") ? 0 : 1;

				titleField.setText(title);

				songmin.setText(length.split(":")[0]);
				songsec.setText(length.split(":")[1]);

				btn[idx].setSelected(true);

				super.mousePressed(e);
			}
		});

	}

	void search() {
		clear();
		addrow("select serial, name from artist where name like '%" + sField.getText() + "%'", m_ar);
	}

	void clear() {
		m_al.setRowCount(0);
		m_s.setRowCount(0);
		imglbl.setIcon(null);
		title.setText("");
		titleField.setText("");
		songmin.setText("");
		songsec.setText("");
	}
	//
//	DefaultTableModel ar_m = model("번호,이름".split(",")), al_m = model("번호,앨범명,장르,곡 수,발매일".split(",")),
//			s_m = model("번호,제목,길이,타이틀곡".split(","));
//	JTable ar_t = table(ar_m), al_t = table(al_m), s_t = table(s_m);
//	JLabel imglbl, titlelbl;
//	JTextField sField, titleField, minField, secField;
//	JRadioButton button[] = new JRadioButton[2];
//	ButtonGroup bg = new ButtonGroup();
//
//	public AdminPage() {
//		ui();
//		addEvents();
//		search();
//	}
//
//	void ui() {
//		var n = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		var c = new JPanel(new GridLayout(1, 0, 5, 5));
//		var c1 = new JPanel(new BorderLayout(5, 5));
//		var c3 = new JPanel(new BorderLayout(5, 5));
//		var c1_n = new JPanel(new BorderLayout(5, 5));
//		var c3_n = new JPanel(new BorderLayout(5, 5));
//		var c3_c = new JPanel(new BorderLayout(5, 5));
//		var c3_c_s = new JPanel(new GridLayout(0, 1));
//
//		// lv1
//
//		add(n, "North");
//		add(c);
//
//		// lv2
//
//		for (var bcap : "통계,로그아웃".split(",")) {
//			n.add(btn(bcap, a -> {
//
//			}));
//		}
//
//		c.add(c1);
//		c.add(new JScrollPane(al_t));
//		c.add(c3);
//
//		// lv3
//
//		c1.add(c1_n, "North");
//		c1_n.add(sField = new JTextField());
//		c1_n.add(btn("검색하기", a -> search()), "East");
//
//		c1.add(new JScrollPane(ar_t));
//
//		c3.add(c3_n, "North");
//		c3.add(c3_c);
//
//		c3_c.add(c3_c_s, "South");
//
//		c3_n.add(size(imglbl = new JLabel(), 150, 150), "West");
//		c3_n.add(titlelbl = lbl("", JLabel.LEFT, Font.BOLD, 20));
//
//		var bcap = "제목,길이,대표 곡".split(",");
//
//		for (int i = 0; i < bcap.length; i++) {
//			var tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
//			tmp.add(size(lbl(bcap[i], JLabel.LEFT, 0, 10), 40, 10));
//			switch (i) {
//			case 0:
//				tmp.add(titleField = new JTextField(15));
//				break;
//			case 1:
//				tmp.add(minField = new JTextField(5));
//				tmp.add(lbl("분", JLabel.CENTER, 0, 10));
//				tmp.add(secField = new JTextField(5));
//				tmp.add(lbl("초", JLabel.CENTER, 0, 10));
//				break;
//			default:
//				for (int j = 0; j < 2; j++) {
//					button[j] = new JRadioButton("예,아니요".split(",")[j]);
//					bg.add(button[j]);
//					button[j].setOpaque(false);
//					button[j].setForeground(Color.WHITE);
//					tmp.add(button[j]);
//				}
//				break;
//			}
//
//			c3_c_s.add(tmp);
//			tmp.setOpaque(false);
//		}
//
//		c3_c.add(new JScrollPane(s_t));
//
//		c3.add(btn("수정", a -> {
//
//			if (s_t.getSelectedRow() == -1) {
//				eMsg("수정할 음악을 선택해야 합니다.");
//				return;
//			}
//
//			if (secField.getText().isEmpty() || minField.getText().isEmpty() || titleField.getText().isEmpty()) {
//				eMsg("모든 항목을 입력해야합니다.");
//				return;
//			}
//
//			try {
//				var rs = stmt
//						.executeQuery("select * from song where album = " + al_t.getValueAt(al_t.getSelectedRow(), 0)
//								+ " and serial <> " + s_t.getValueAt(s_t.getSelectedRow(), 0) + " and name like '%"
//								+ titleField.getText().replaceAll("'", "\\\\'") + "%'");
//				if (rs.next()) {
//					eMsg("같은 애범에 동일한 제목의 음악이 존재합니다.");
//					return;
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			String time = "";
//
//			try {
//				time = LocalTime.of(0, toInt(minField.getText()), toInt(secField.getText())).toString();
//			} catch (Exception e) {
//				eMsg("시간을 올바르게 입력해야 합니다.");
//				return;
//			}
//
//			if (button[0].isSelected()) {
//				execute("update song set titlesong = 0 where album = " + al_t.getValueAt(al_t.getSelectedRow(), 0));
//			}
//
//			var idx = button[0].isSelected() ? 1 : 0;
//
//			execute("update song set titlesong = " + idx + ", name = '" + titleField.getText().replaceAll("'", "\\\\'")
//					+ "', length = '" + time + "' where serial = " + s_t.getValueAt(s_t.getSelectedRow(), 0));
//
//			addrow("select serial, name, right(length,5), if(titlesong, '예', '아니요') from song where album = "
//					+ al_t.getValueAt(al_t.getSelectedRow(), 0), s_m);
//
//			iMsg("선택한 음악을 수정했습니다.");
//		}), "South");
//
//		// 속성
//		imglbl.setBorder(new LineBorder(Color.white));
//
//		n.setOpaque(false);
//		c.setOpaque(false);
//		c1.setOpaque(false);
//		c3.setOpaque(false);
//		c1_n.setOpaque(false);
//		c3_c.setOpaque(false);
//		c3_c_s.setOpaque(false);
//		c3_n.setOpaque(false);
//
//		s_t.getColumnModel().getColumn(1).setMaxWidth(200);
//		s_t.getColumnModel().getColumn(1).setMinWidth(200);
//
//		ar_t.getColumnModel().getColumn(1).setMaxWidth(300);
//		ar_t.getColumnModel().getColumn(1).setMinWidth(300);
//
//		setBorder(new EmptyBorder(5, 5, 5, 5));
//	}
//
//	void search() {
//		addrow("select serial, name from artist where name like '%" + sField.getText() + "%'", ar_m);
//		clear();
//	}
//
//	void addEvents() {
//		ar_t.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				if (ar_t.getSelectedRow() == -1)
//					return;
//
//				clear();
//				var ar = ar_t.getValueAt(ar_t.getSelectedRow(), 0);
//				addrow("select al.serial, al.name, c.name, count(s.serial), date_format(al.release, '%Y년 %m월 %d일') from album al, category c, song s where s.album = al.serial and c.serial = al.category and al.artist = "
//						+ ar + " group by al.serial", al_m);
//			}
//		});
//
//		al_t.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				if (al_t.getSelectedRow() == -1)
//					return;
//
//				var al = al_t.getValueAt(al_t.getSelectedRow(), 0);
//				var name = al_t.getValueAt(al_t.getSelectedRow(), 1).toString();
//
//				imglbl.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/album/" + al + ".jpg")
//						.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
//
//				titlelbl.setText(name);
//
//				addrow("select serial, name, right(length,5), if(titlesong, '예', '아니요') from song where album = " + al,
//						s_m);
//
//				super.mousePressed(e);
//			}
//		});
//
//		s_t.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				if (s_t.getSelectedRow() == -1)
//					return;
//
//				var title = s_t.getValueAt(s_t.getSelectedRow(), 1).toString();
//				var length = s_t.getValueAt(s_t.getSelectedRow(), 2).toString();
//				var idx = s_t.getValueAt(s_t.getSelectedRow(), 3).toString().equals("예") ? 0 : 1;
//
//				titleField.setText(title);
//
//				minField.setText(length.split(":")[0]);
//				secField.setText(length.split(":")[1]);
//
//				button[idx].setSelected(true);
//
//				super.mousePressed(e);
//			}
//		});
//	}
//
//	void clear() {
//		al_m.setRowCount(0);
//		s_m.setRowCount(0);
//		imglbl.setIcon(null);
//		titlelbl.setText("");
//		titleField.setText("");
//		secField.setText("");
//		minField.setText("");
//		bg.clearSelection();
//	}

	public static void main(String[] args) {
		MainFrame.bf.getContentPane().removeAll();
		MainFrame.bf.getContentPane().setLayout(new BorderLayout());
		MainFrame.bf.getContentPane().add(new AdminPage());
		MainFrame.bf.getContentPane().revalidate();
	}
}
