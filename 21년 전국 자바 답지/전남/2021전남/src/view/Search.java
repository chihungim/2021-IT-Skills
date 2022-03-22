package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import db.DBManager;
import util.Util;

public class Search extends BaseFrame {

	JPanel n, n_s, c, c_w, c_c;
	DefaultTableModel m = new DefaultTableModel(null, "지역".split(",")) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};;
	JTable t;
	JScrollPane scr1;
	JScrollPane scr2;
	JTextField txt;
	String[] bcap = "평점 높은 순,리뷰 많은 순,1인 가능,난이도 5".split(",");

	public Search() {
		super("검색", 750, 450);
		setLayout(new BorderLayout(5, 5));
		add(n = new JPanel(new BorderLayout()), "North");
		add(c = new JPanel(new BorderLayout(5, 5)));
		c.add(c_w = new JPanel(new BorderLayout()), "West");
		c.add(scr1 = new JScrollPane(c_c = new JPanel(new GridLayout(0, 3, 5, 5))));
		n.add(Util.lbl("방탈출 카페 검색", JLabel.LEFT, 30));
		n.add(n_s = new JPanel(new FlowLayout(FlowLayout.LEFT)), "South");
		c_w.add(Util.sz(scr2 = new JScrollPane(t = new JTable(m)), 80, 400), "West");

		for (int i = 0; i < bcap.length; i++) {
			n_s.add(Util.btn(bcap[i], a -> {

			}));
		}

		n_s.add(Util.sz(new JLabel(" "), 150, 20));
		n_s.add(txt = new JTextField(10));
		n_s.add(Util.btn("검색", a -> {

			if (a.getActionCommand().equals("평점 높은 순")) {
				try {
					ResultSet rs = DBManager.rs(
							"SELECT c.c_name FROM evaluation e, cafe c where e.c_no = c.c_no group by c.c_no order by e.e_score desc");
					while (rs.next()) {
						search(rs.getString(1), 0);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (a.getActionCommand().equals("리뷰 많은 순")) {
				try {
					ResultSet rs = DBManager.rs(
							"SELECT c_name FROM notice n, cafe c, reservation r where c.c_no = r.c_no and n.u_no = r.u_no and r.r_date <= n.n_date group by c.c_no");
					while (rs.next()) {
						search(rs.getString(1), 0);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (a.getActionCommand().equals("1인 가능")) {
				try {
					ResultSet rs = DBManager.rs(
							"SELECT c_name FROM notice n, cafe c, reservation r where c.c_no = r.c_no and n.u_no = r.u_no and r.r_people = 1 group by c.c_no");
					while (rs.next()) {
						search(rs.getString(1), 0);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (a.getActionCommand().equals("난이도 5")) {
				try {
					ResultSet rs = DBManager.rs(
							"SELECT c_name FROM theme t, cafe c, reservation r where c.c_no = r.c_no and t.t_no = r.t_no and t.t_difficulty = 5 group by c.c_no");
					while (rs.next()) {
						search(rs.getString(1), 0);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}));

		m.addRow(new Object[] { "전체", });
		Util.addRow(m, "select left(c_address, 2) from cafe group by left(c_address, 2)", 1);

		Util.setEmpty(((JPanel) getContentPane()), 5, 5, 5, 5);

		search(txt.getText(), 0);

		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				search(t.getValueAt(t.getSelectedRow(), 0) + "", 1);
			}
		});

		setVisible(true);
	}

	void search(String text, int t) {
		c_c.removeAll();
		text = text.equals("전체") ? "" : text;
		String sql = "select c_name from cafe where ";
		sql += t == 0 ? "c_name like '%" + text + "%'" : "left(c_address, 2) like '%" + text + "%'";
		try {
			var rs = DBManager.rs(sql);
			while (rs.next()) {
				Item item = new Item(rs.getString(1));
				c_c.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		repaint();
		revalidate();

	}

	class Item extends JPanel {
		public Item(String name) {
			setLayout(new BorderLayout());
			add(new JLabel(Util.img("./Datafiles/지점/" + name.split(" ")[0] + ".jpg", 180, 180)));
			add(Util.lbl(name, JLabel.CENTER), "South");
			Util.setLine(this);
			setName(name);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						cname = ((JPanel) e.getSource()).getName();
						cafename = name.split(" ")[0];
						new Introduce().addWindowListener(new Before(Search.this));
					}
				}
			});
		}
	}

	public static void main(String[] args) {
		new Search();
	}
}
