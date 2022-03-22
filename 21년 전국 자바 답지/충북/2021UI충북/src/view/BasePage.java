package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class BasePage extends JPanel {
	public static Connection con = db.DB.con;
	public static Statement stmt = db.DB.stmt;
	public static int u_serial, u_region, idx = -1, a_serial;
	public static String al_serial, s_serial, ar_serial, al_name, a_name;
	public static Color myColor = new Color(50, 100, 255);
	static DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	static LinkedList<Integer> que = new LinkedList<Integer>();
	static Stack<Integer> cache = new Stack<>();
	static boolean isPlaying = false;
//	static QueuePage queuePage;
	static MainFrame mf;
	static Timebar bar;
	static HashMap<Integer, Integer> getAlbumRef = new HashMap<>();
	static Timer progress;
	static int time, max;

	void addSongRow(String sql, DefaultTableModel m) {
		try {
			var rs = stmt.executeQuery(sql);
			Object row[] = new Object[m.getColumnCount()];
			while (rs.next()) {
				row[0] = rs.getInt(1) == 0 ? "" : "★";
				row[2] = rs.getString(2);
				row[3] = rs.getInt(3) == 0 ? "♡" : "♥";
				row[4] = rs.getString(4);
				row[5] = rs.getString(5);
				row[1] = rs.getRow();
				m.addRow(row);
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	DefaultTableModel songModel() {
		DefaultTableModel m = new DefaultTableModel(null, "chk,row,name,like,title,serial".split(",")) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		return m;
	}

	JTable songTable(DefaultTableModel m) {
		JTable t = new JTable(m) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				JComponent v = (JComponent) super.prepareRenderer(renderer, row, column);
				if (row == super.getSelectedRow()) {
					v.setBackground(Color.WHITE.darker());
					v.setForeground(Color.WHITE);
				} else {
					v.setBackground(Color.black);
					v.setForeground(Color.WHITE);
				}

				v.setBorder(BorderFactory.createEmptyBorder());
				v.repaint();
				v.revalidate();
				return v;
			}

		};

		t.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				JTable t = (JTable) e.getSource();
				Point p = e.getPoint();
				int row = t.rowAtPoint(p);
				if (row < 0 || row > t.getRowCount()) {
					t.clearSelection();
					return;
				}
				t.setRowSelectionInterval(row, row);
			}
		});

		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				t.clearSelection();

				super.mouseExited(e);
			}
		});

		t.getTableHeader().setReorderingAllowed(false);
		t.getTableHeader().setResizingAllowed(false);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.setIntercellSpacing(new Dimension(0, 0));
		t.setBackground(Color.black);

		t.setForeground(Color.WHITE);
		t.setShowGrid(false);
		t.setGridColor(Color.BLACK);

		t.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (t.getSelectedRow() == -1)
					return;

				int row = t.getSelectedRow();
				int col = t.getSelectedColumn();

				if (t.getSelectedColumn() == 3) {
					if (t.getValueAt(row, col).equals("♥")) {
						t.setValueAt("♡", row, col);
						execute("delete from favorite where user =" + u_serial + " and song = "
								+ t.getValueAt(row, t.getColumnCount() - 1));
					} else {
						t.setValueAt("♥", row, col);
						execute("insert favorite values(0, " + u_serial + ","
								+ t.getValueAt(row, t.getColumnCount() - 1) + ")");
					}

					return;
				}

			}
		});

		t.setRowHeight(30);

		t.getColumnModel().getColumn(0).setMaxWidth(20);
		t.getColumnModel().getColumn(0).setMinWidth(20);
		t.getColumnModel().getColumn(1).setMaxWidth(30);
		t.getColumnModel().getColumn(2).setMinWidth(50);
		t.getColumnModel().getColumn(3).setMaxWidth(20);

		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(SwingConstants.CENTER);
		t.getColumnModel().getColumn(3).setCellRenderer(center);
		t.getColumnModel().getColumn(4).setCellRenderer(center);

		t.getColumnModel().getColumn(t.getColumnCount() - 2).setMinWidth(50);
		t.getColumnModel().getColumn(t.getColumnCount() - 2).setMaxWidth(50);
		t.getColumnModel().getColumn(t.getColumnCount() - 1).setMinWidth(0);
		t.getColumnModel().getColumn(t.getColumnCount() - 1).setMaxWidth(0);

		return t;
	}

	class Timebar extends JLabel {

		public double max = 1000, value;

		JPanel time;

		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			// getPercentage of value and change it to curval
			double percent = (value / max); // 원비 -> 해당 width의 비율로
			g2.setColor(Color.ORANGE);
			g2.fillRect(0, 0, (int) (getWidth() * percent), getHeight());
			repaint();
			super.paint(g);
		}

		void setMaxValue(double max) {
			this.max = max;
		}

		void setValue(double value) {
			if (value == max)
//				next();

				this.value = value;
		}
	}

	void addrow(String sql, DefaultTableModel m) {
		m.setRowCount(0);
		try {
			var rs = stmt.executeQuery(sql);
			Object row[] = new Object[m.getColumnCount()];
			while (rs.next()) {
				for (int i = 0; i < row.length; i++) {
					row[i] = rs.getString(i + 1);
				}
				m.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void setData() {
		try {
			var rs = stmt.executeQuery("select * from song");
			while (rs.next()) {
				getAlbumRef.put(rs.getInt(1), rs.getInt(4));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BasePage() {
		setLayout(new BorderLayout());
		execute("use music");
		setBackground(Color.black);
		MainFrame.bf.setVisible(true);
	}

	void addtoPlayList(int s_serial) {
		ArrayList<String> list = new ArrayList<String>();
		JComboBox<?> box;
		try {
			var rs1 = BasePage.stmt
					.executeQuery("select pl.name from playlist pl, user u where u.serial = pl.user and u.serial="
							+ BasePage.u_serial);
			while (rs1.next()) {
				list.add(rs1.getString(1));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		box = new JComboBox<>(list.toArray());
		int ynMsg = JOptionPane.showConfirmDialog(null, box, "플레이리스트에 추가", JOptionPane.YES_NO_OPTION);
		if (ynMsg == 0) {
			if (box.getSelectedIndex() == -1) {
				BasePage.eMsg("플레이리스트를 선택해주세요.");
				return;
			}
			BasePage.iMsg("추가되었습니다.");
			BasePage.execute("insert into songlist values(0, (select serial from playlist where name like '"
					+ box.getSelectedItem() + "'), " + s_serial + ")");

		}

	}

	public static int toInt(Object p) {
		return Integer.parseInt(p.toString());
	}

	public static void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static JLabel lbl(String c, int a, int style, int size) {
		JLabel l = new JLabel(c, a);
		l.setForeground(Color.white);
		l.setFont(new Font("맑은 고딕", style, size));
		return l;
	}

	public static JLabel lbl(String c, int a, int style, int size, MouseAdapter b) {
		JLabel l = new JLabel(c, a);
		l.setForeground(Color.white);
		l.setFont(new Font("맑은 고딕", style, size));
		l.addMouseListener(b);
		return l;
	}

	public static JButton btn(String cap, ActionListener a) {
		JButton b = new JButton(cap);
		b.setBackground(Color.white);
		b.addActionListener(a);
		return b;
	}

	public static JTable table(DefaultTableModel m) {
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		JTable t = new JTable(m);
		t.getTableHeader().setReorderingAllowed(false);
		t.getTableHeader().setResizingAllowed(false);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		for (int i = 0; i < t.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		return t;
	}

	public static DefaultTableModel model(String col[]) {
		DefaultTableModel m = new DefaultTableModel(null, col) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return m;
	}

	public static JLabel imglbl(String path, int w, int h) {
		return new JLabel(
				new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH)));
	}

	JLabel lbl(String l, int size, String aligment, String... color) {
		final String colour;
		if (color.length == 0) {
			colour = "WHITE";
		} else
			colour = color[0];
		return new JLabel("<html><font face = \"맑은 고딕\"; color = \"" + colour + "\"; size = \"" + size + "\";><"
				+ aligment + ">" + l) {
			@Override
			public void setText(String text) {
				super.setText("<html><font face = \"맑은 고딕\"; color = \"" + colour + "\"; size = \"" + size + "\";><"
						+ aligment + ">" + text);
			}
		};
	}

	public static void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "", JOptionPane.ERROR_MESSAGE);
	}

	public static <T extends JComponent> T size(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	public static JTextField txt(int size) {
		JTextField t = new JTextField(size);
		t.setBackground(Color.gray);
		return t;
	}
}
