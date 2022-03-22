package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Notice extends BaseFrame {

	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new BorderLayout()), c = new JPanel(new BorderLayout());
	JPanel n_w = new JPanel(new FlowLayout(0)), n_e = new JPanel(new FlowLayout(2)), c_w = new JPanel(new GridLayout()),
			c_e = new JPanel();
	JLabel lbl[] = { new JLabel("페이지 정보 : "), new JLabel("1"), new JLabel(" / "), new JLabel("5"), new JLabel("검색 :") };
	JButton btn[] = { new JButton("◀"), new JButton("▶"), new JButton("게시물 작성") };
	JComboBox com = new JComboBox("제목,아이디,내용".split(","));
	JTextField txt = new JTextField(15);
	DefaultTableModel m = new DefaultTableModel(null, "번호,제목,아이디,등록일,조회".split(",")) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable t = new JTable(m);
	JScrollPane jsc = new JScrollPane(t);
	int cnt = 0;

	public Notice() {
		super("게시판", 800, 400);

		ui();
		event();
		setVisible(true);
	}

	void event() {
		for (int i = 0; i < btn.length; i++) {
			btn[i].addActionListener(a -> {
				t.removeAll();
				if (a.getActionCommand().equals("◀")) {
					cnt--;
					lbl[1].setText(cnt + "");
				} else if (a.getActionCommand().equals("▶")) {
					cnt++;
					lbl[1].setText(cnt + "");
				} else {
					uid = (String) t.getValueAt(t.getSelectedRow(), 2);
					ndate = (String) t.getValueAt(t.getSelectedRow(), 3);
					new AddNotice().addWindowListener(new Before(Notice.this));
				}
			});
		}

		t.addMouseListener(new MouseAdapter() {
			boolean bool = false;

			public void mousePressed(MouseEvent e) {
				c_e.removeAll();
				if (e.getClickCount() == 2) {
					try {
						if (bool == false) {
							ResultSet rs = stmt.executeQuery("select n_content from notice where n_title = '"
									+ t.getValueAt(t.getSelectedRow(), 1) + "'");
							while (rs.next()) {
								Accumulate ac = new Accumulate(t.getValueAt(t.getSelectedRow(), 1), rs.getString(1));
								ac.setPreferredSize(new Dimension(250, 300));
								c_e.add(ac);
								repaint();
								revalidate();
							}
						} else {
							c_e.removeAll();
							repaint();
							revalidate();
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (t.getValueAt(t.getSelectedRow(), 2).equals(uid)) {
						t.setSelectionForeground(Color.blue);
					} else {
						t.setSelectionForeground(Color.black);
						execute(("update notice set n_viewcount = n_viewcount+1 where n_title = '"
								+ t.getValueAt(t.getSelectedRow(), 1) + "'"));
						repaint();
						revalidate();
					}
				}

			}
		});

		txt.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					m.setNumRows(0);
					if (com.getSelectedIndex() == 0) {
						rowData(txt.getText());
					} else if (com.getSelectedIndex() == 1) {
						rowData2(txt.getText());
					} else if (com.getSelectedIndex() == 2) {
						rowData3(txt.getText());
					}
				}
			}
		});

	}

	void rowData(String title) {
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			int i = 1;
			ResultSet rs = stmt.executeQuery(
					"SELECT n_title, u_id, n_date, n_viewcount FROM notice n, user u where n.u_no = u.u_no and n_open = 'O' and n.n_title like '%"
							+ title + "%' order by n_date asc");
			while (rs.next()) {
				title = rs.getString(1);
				addRow(m, i, title, rs.getString(2), rs.getString(3), rs.getString(4));
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void rowData2(String id) {
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);

		try {
			int i = 1;
			ResultSet rs = stmt.executeQuery(
					"SELECT n_title, u_id, n_date, n_viewcount FROM notice n, user u where n.u_no = u.u_no and n_open = 'O' and u.u_id like '%"
							+ id + "%' order by n_date asc");
			while (rs.next()) {
				id = rs.getString(2);
				addRow(m, i, rs.getString(1), id, rs.getString(3), rs.getString(4));
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void rowData3(String index) {
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			int i = 1;
			ResultSet rs = stmt.executeQuery(
					"SELECT n_title, u_id, n_date, n_viewcount FROM notice n, user u where n.u_no = u.u_no and n_open = 'O' and n.n_content like '%"
							+ index + "%' order by n_date asc");
			while (rs.next()) {
				addRow(m, i, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void entireRow() {
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			int i = 1;
			ResultSet rs = stmt.executeQuery(
					"SELECT n_title, u_id, n_date, n_viewcount FROM notice n, user u where n.u_no = u.u_no and n_open = 'O' order by n_date asc");
			while (rs.next()) {
				addRow(m, i, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void addRow(DefaultTableModel m, int cnt, String title, String id, String date, String view) {
		Object row[] = new Object[5];
		row[0] = cnt;
		row[1] = title;
		row[2] = id;
		row[3] = date;
		row[4] = view;

		m.addRow(row);

	}

	void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");
		n.add(n_w, "West");
		n.add(n_e, "East");
		c.add(c_e, "East");
		c.add(c_w, "Center");

		for (int i = 0; i < 4; i++) {
			n_w.add(lbl[i]);
		}
		n_w.add(btn[0]);
		n_w.add(btn[1]);
		n_e.add(lbl[4]);
		n_e.add(com);
		n_e.add(txt);
		n_e.add(btn[2]);
		c_w.add(jsc);
		c.setBorder(new LineBorder(Color.BLACK));

		entireRow();
	}

	public static void main(String[] args) {
		uid = "room6";
		new Notice();
	}

	class Accumulate extends JPanel {
		JPanel mainp = new JPanel(new BorderLayout());
		JPanel n = new JPanel(new FlowLayout(0)), c = new JPanel(new GridLayout()), s = new JPanel(new FlowLayout(2));
		JLabel lbl[] = { lbl("제목 : ", 2, 12), new JLabel(), lbl("작성일", 4, 10), new JLabel(LocalDate.now() + "") };
		JTextArea txt = new JTextArea();
		Object 제목;
		String 설명;

		public Accumulate(Object 제목, String 설명) {
			setLayout(new BorderLayout());
			this.제목 = 제목;
			this.설명 = 설명;

			setUI();
		}

		void setUI() {
			add(mainp);
			mainp.add(n, "North");
			mainp.add(c, "Center");
			mainp.add(s, "South");

			n.add(lbl[0]);
			n.add(lbl[1]);
			c.add(txt);
			s.add(lbl[2]);
			s.add(lbl[3]);

			lbl[1].setText(제목 + "");
			txt.setText(설명);
			txt.setLineWrap(true);
			txt.setBorder(new LineBorder(Color.black));
			txt.setEditable(false);
		}
	}
}
