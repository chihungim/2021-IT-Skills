package 전국대전자바;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class Search extends BaseFrame {
	JMenuBar bar = new JMenuBar();
	JMenu menu = new JMenu("분류");
	JMenuItem item[] = { new JMenuItem("전체"), new JMenuItem("뮤지컬"), new JMenuItem("오페라"), new JMenuItem("콘서트") };
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new FlowLayout(2)), c = new JPanel(new BorderLayout());
	JPanel c_w = new JPanel(new BorderLayout()), c_e = new JPanel(new BorderLayout());
	JPanel c_w_n = new JPanel(new FlowLayout(2)), c_w_w = new JPanel(), c_w_c = new JPanel(), c_w_e = new JPanel(),
			c_w_s = new JPanel(new BorderLayout()), c_e_n = new JPanel(new FlowLayout(2)),
			c_e_c = new JPanel(new BorderLayout());
	JPanel c_w_c_c = new JPanel(new GridLayout(2, 2, 5, 5)), c_w_s_n = new JPanel(), c_w_s_s = new JPanel();
	JLabel pername = new JLabel("공연명 :"), div = new JLabel("분류 : "), dividx = new JLabel("전체"), left = new JLabel("◀"),
			right = new JLabel("▶"), first = new JLabel("《 처음으로"), last = new JLabel("마지막으로 》"),
			reserve = new JLabel("현재 예매 가능 공연");
	JLabel imglbl2[] = { new JLabel(), new JLabel(), new JLabel(), new JLabel() };
	int pNum = 0;
	JLabel circle[] = { new JLabel("●"), new JLabel("●"), new JLabel("●"), new JLabel("●"), new JLabel("●"),
			new JLabel("●"), new JLabel("●"), new JLabel("●") };
	JTextField txt = new JTextField(15);
	JButton btn = new JButton("검색");
	DefaultTableModel m = model("공연번호,공연날짜,공연명,공연가격".split(","));
	JTable t = table(m);
	JScrollPane scr = new JScrollPane(t);
	String name = "";
	int idx = 0, cnt = 0;
	ArrayList<Object[]> sql = new ArrayList<Object[]>();
	ArrayList<Object[]> sql1 = new ArrayList<Object[]>();
	ArrayList<Object[]> sql2 = new ArrayList<Object[]>();
	ArrayList<Object[]> sql3 = new ArrayList<Object[]>();

	public Search() {
		super("검색", 800, 650);

		data();
		ui();
		property();
		event();
		search(txt.getText());
		entireImage();
		setVisible(true);
	}

	private void data() {
		try {
			ResultSet rs = stmt
					.executeQuery("select * from perform where p_date >= curdate() group by p_name order by p_date");
			while (rs.next()) {
				sql.add(new Object[] { rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6),
						rs.getString(7) });
			}
			ResultSet rs1 = stmt.executeQuery(
					"select * from perform where p_date >= curdate() and left(pf_no, 1) = 'M' group by p_name order by p_date");
			while (rs1.next()) {
				sql1.add(new Object[] { rs1.getString(2), rs1.getString(3), rs1.getString(4), rs1.getInt(5),
						rs1.getString(6), rs1.getString(7) });
			}
			ResultSet rs2 = stmt.executeQuery(
					"select * from perform where p_date >= curdate() and left(pf_no, 1) = 'O' group by p_name order by p_date");
			while (rs2.next()) {
				sql2.add(new Object[] { rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getInt(5),
						rs2.getString(6), rs2.getString(7) });
			}
			ResultSet rs3 = stmt.executeQuery(
					"select * from perform where p_date >= curdate() and left(pf_no, 1) = 'C' group by p_name order by p_date");
			while (rs3.next()) {
				sql3.add(new Object[] { rs3.getString(2), rs3.getString(3), rs3.getString(4), rs3.getInt(5),
						rs3.getString(6), rs3.getString(7) });
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void property() {
		c_w_n.setBorder(new EmptyBorder(0, 0, 10, 30));
		c_w_w.setBorder(new EmptyBorder(80, 0, 0, 0));
		c_w_e.setBorder(new EmptyBorder(80, 0, 0, 0));
		c_w_s.setBorder(new EmptyBorder(0, 0, 80, 0));
		c_e_c.setBorder(new EmptyBorder(0, 0, 5, 5));
		c_w.setBorder(new EmptyBorder(100, 3, 50, 0));
		left.setFont(new Font("", Font.BOLD, 30));
		right.setFont(new Font("", Font.BOLD, 30));
		div.setFont(new Font("굴림", Font.BOLD, 15));
		dividx.setFont(new Font("굴림", Font.BOLD, 15));
		first.setFont(new Font("굴림", Font.PLAIN, 15));
		last.setFont(new Font("굴림", Font.PLAIN, 15));
		t.getColumnModel().getColumn(0).setMinWidth(0);
		t.getColumnModel().getColumn(0).setMaxWidth(0);

		for (int i = 0; i < circle.length; i++) {
			circle[i].setForeground(Color.black);
		}
		circle[0].setForeground(Color.red);
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private void event() {
		item[0].addActionListener(a -> {
			addrow(m,
					"select pf_no, p_date, p_name, format(p_price, '#,##0') from perform where p_date >= curdate() order by p_date asc, p_price desc");
			entireImage();
			dividx.setText("전체");
		});
		item[1].addActionListener(a -> {
			name = "M";
			rowData(name);
			setImage2();
			dividx.setText("뮤지컬");
		});
		item[2].addActionListener(a -> {
			name = "O";
			rowData(name);
			setImage3();
			dividx.setText("오페라");
		});
		item[3].addActionListener(a -> {
			name = "C";
			rowData(name);
			setImage4();
			dividx.setText("콘서트");
		});

		right.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if ((pNum + 1) * 4 + 1 <= sql.size()) {
					pNum++;
					if (dividx.getText().equals("전체"))
						entireImage();
					else if (dividx.getText().equals("뮤지컬"))
						setImage2();
					else if (dividx.getText().equals("오페라"))
						setImage3();
					else if (dividx.getText().equals("콘서트")) {
						setImage4();
					}
				}
				if (cnt < 7) {
					cnt++;
					circle[cnt].setForeground(Color.red);
					for (int i = 0; i < cnt; i++) {
						circle[i].setForeground(Color.black);
					}
				}

			}
		});

		left.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (pNum > 0) {
					pNum--;
					if (dividx.getText().equals("전체"))
						entireImage();
					else if (dividx.getText().equals("뮤지컬"))
						setImage2();
					else if (dividx.getText().equals("오페라"))
						setImage3();
					else if (dividx.getText().equals("콘서트")) {
						setImage4();
					}
				}
				if (0 < cnt) {
					cnt--;
					circle[cnt].setForeground(Color.red);

					for (int i = 0; i < cnt; i++) {
						circle[i].setForeground(Color.black);
					}

					for (int i = cnt + 1; i < 8; i++) {
						circle[i].setForeground(Color.black);
					}
				}
			}
		});

		first.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				circle[0].setForeground(Color.red);

				for (int i = 1; i < 8; i++) {
					circle[i].setForeground(Color.black);
				}
				pNum = 0;

			}
		});

		last.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				circle[7].setForeground(Color.red);
				for (int i = 0; i < 7; i++) {
					circle[i].setForeground(Color.black);
				}
			}
		});

		for (int i = 0; i < imglbl2.length; i++) {
			int idx = i;
			imglbl2[i].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					try {
						if (dividx.getText().equals("전체"))
							new Reserve(sql.get(pNum * 4 + idx)).addWindowListener(new before(Search.this));
						else if (dividx.getText().equals("뮤지컬"))
							new Reserve(sql1.get(pNum * 4 + idx)).addWindowListener(new before(Search.this));
						else if (dividx.getText().equals("오페라"))
							new Reserve(sql2.get(pNum * 4 + idx)).addWindowListener(new before(Search.this));
						else if (dividx.getText().equals("콘서트"))
							new Reserve(sql3.get(pNum * 4 + idx)).addWindowListener(new before(Search.this));
					} catch (Exception e1) {
						eMsg("공연정보가 없습니다.");
						return;
					}
				}
			});
		}
		t.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				p_name = (String) ((JTable) e.getSource()).getValueAt(t.getSelectedRow(), 2);
				p_date = (String) t.getValueAt(t.getSelectedRow(), 1);
				new Reserve().addWindowListener(new before(Search.this));
			}
		});

		btn.addActionListener(a -> {
			search(txt.getText());
		});
	}

	void rowData(String name) {
		String sql = "select pf_no, p_date, p_name, format(p_price, '#,##0') from perform where pf_no like '%" + name
				+ "%' and p_date >= curdate() order by p_date asc, p_price desc";
		addrow(m, sql);
	}

	void entireImage() {
		for (int i = 0; i < 4; i++) {
			imglbl2[i].setIcon(null);
		}
		for (int i = pNum * 4; i <= pNum * 4 + 3; i++) {
			try {
				imglbl2[i % 4].setIcon(new ImageIcon(
						Toolkit.getDefaultToolkit().getImage("./Datafiles/공연사진/" + (String) sql.get(i)[0] + ".jpg")
								.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
			} catch (Exception e) {

			}
		}
	}

	void setImage2() {
		for (int i = 0; i < 4; i++) {
			imglbl2[i].setIcon(null);
		}
		try {
			for (int i = pNum * 4; i <= pNum * 4 + 3; i++) {
				imglbl2[i % 4].setIcon(new ImageIcon(
						Toolkit.getDefaultToolkit().getImage("./Datafiles/공연사진/" + (String) sql1.get(i)[0] + ".jpg")
								.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
			}
		} catch (Exception e) {
		}
	}

	void setImage3() {
		for (int i = 0; i < 4; i++) {
			imglbl2[i].setIcon(null);
		}
		try {
			for (int i = pNum * 4; i <= pNum * 4 + 3; i++) {
				imglbl2[i % 4].setIcon(new ImageIcon(
						Toolkit.getDefaultToolkit().getImage("./Datafiles/공연사진/" + (String) sql2.get(i)[0] + ".jpg")
								.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
			}
		} catch (Exception e) {
		}
	}

	void setImage4() {
		for (int i = 0; i < 4; i++) {
			imglbl2[i].setIcon(null);
		}
		try {
			for (int i = pNum * 4; i <= pNum * 4 + 3; i++) {
				imglbl2[i % 4].setIcon(new ImageIcon(
						Toolkit.getDefaultToolkit().getImage("./Datafiles/공연사진/" + (String) sql3.get(i)[0] + ".jpg")
								.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
			}
		} catch (Exception e) {
		}
	}

	void search(String p_name) {
		try {
			ResultSet rs = stmt.executeQuery(
					"select pf_no, p_name, p_date from perform where p_date >= curdate() and p_name like '%" + p_name
							+ "%' group by p_name order by p_date");
			while (rs.next()) {
				for (int i = pNum * 4; i <= pNum * 4 + 3; i++) {
					imglbl2[i].setIcon(new ImageIcon(
							Toolkit.getDefaultToolkit().getImage("./Datafiles/공연사진/" + rs.getString(1) + ".jpg")
									.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		p_name = p_name.equals("") ? "true" : "p_name like '%" + p_name + "%'";
		addrow(m, "select p_no, p_date, p_name, format(p_price, '#,##0') from perform where " + p_name
				+ " and pf_no like '" + name + "%'");
		if (t.getRowCount() == 0) {
			iMsg("검색 결과가 없습니다.");
			return;
		}
	}

	private void ui() {
		bar.add(menu);
		for (int i = 0; i < item.length; i++) {
			menu.add(item[i]);
		}
		setJMenuBar(bar);

		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");
		c.add(c_e, "East");
		c.add(c_w, "West");
		c_w.add(c_w_n, "North");
		c_w.add(c_w_w, "West");
		c_w.add(c_w_c, "Center");
		c_w.add(c_w_e, "East");
		c_w.add(c_w_s, "South");
		c_e.add(c_e_n, "North");
		c_e.add(c_e_c, "Center");
		c_w_c.add(c_w_c_c);
		c_w_s.add(c_w_s_s, "South");
		c_w_s.add(c_w_s_n, "North");

		n.add(pername);
		n.add(txt);
		n.add(btn);
		c_w_n.add(div);
		c_w_n.add(dividx);
		c_w_w.add(left);
		for (int i = 0; i < 4; i++) {
			imglbl2[i].setPreferredSize(new Dimension(100, 100));
			imglbl2[i].setBorder(new LineBorder(Color.black));
			c_w_c_c.add(imglbl2[i]);
			repaint();
			revalidate();
		}
		c_w_e.add(right);
		for (int i = 0; i < circle.length; i++) {
			c_w_s_n.add(circle[i]);
		}
		c_w_s_s.add(first);
		c_w_s_s.add(last);
		c_e_n.add(reserve);
		c_e_c.add(scr);

	}

	public static void main(String[] args) {
		isLogined = true;
		new Search();
	}
}
