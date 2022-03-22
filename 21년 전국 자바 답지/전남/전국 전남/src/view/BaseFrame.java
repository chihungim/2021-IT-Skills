package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class BaseFrame extends JFrame {
	static int uno, tno;
	static String uname, cno, cname, cprice, cdivision, uid, ndate, tname, tdfficulty, ttime, tpersonnel, texplain,
			caddress, location;
	static boolean isPayed = false;
	public static Connection con = db.DB.con;
	public static Statement stmt = db.DB.stmt;
	public static ArrayList<JFrame> windows = new ArrayList<>();
	public DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

	static {
//		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			stmt.execute("use roomescape");
		} catch (SQLException e) {
			// TODO Auto-gene rated catch block
			e.printStackTrace();
		}
	}

	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BaseFrame(String n, int w, int h) {
		super(n);
		setSize(w, h);
		setDefaultCloseOperation(2);
		setLocationRelativeTo(null);
	}

	public static void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "정보", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "경고", JOptionPane.ERROR_MESSAGE);
	}

	public static JButton btn(String cap, ActionListener a) {
		JButton b = new JButton(cap);
		b.addActionListener(a);
		return b;
	}

	public static <T extends JComponent> T sz(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	public static void setEmpty(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
	}

	public JLabel lbl(String cap, int alig) {
		JLabel l = new JLabel(cap, alig);
		return l;
	}

	public Boolean isExists(String t, String c, String v) {
		try {
			var rs = stmt.executeQuery("select * from " + t + " where " + c + " = '" + v + "'");
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public JLabel lbl(String cap, int alig, int size) {
		JLabel l = new JLabel(cap, alig);
		l.setFont(new Font("", Font.BOLD, size));
		return l;
	}

	public void addRow(DefaultTableModel m, String sql) {
		m.setRowCount(0);
		try {
			var rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Object row[] = new Object[m.getColumnCount()];
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

	JTable table(DefaultTableModel m) {
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		JTable t = new JTable(m) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				var c = super.prepareRenderer(renderer, row, column);
				var v = getValueAt(row, getColumnCount() - 1);
				if (v.equals(false)) {
					c.setEnabled(false);
				} else {
					c.setEnabled(true);
				}

				return c;
			}
		};
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.getTableHeader().setReorderingAllowed(false);
		t.getTableHeader().setResizingAllowed(false);

		for (int i = 0; i < t.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}

		return t;
	}

	DefaultTableModel model(String... col) {
		DefaultTableModel m = new DefaultTableModel(null, col) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return m;
	}

	public static boolean isNum(Object p) {
		try {
			Integer.parseInt(p.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getValue(String sql) {
		try {
			var rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	public static String getValue(String sql, String colName) {
		try {
			var rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(colName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	public static int toInt(Object o) {
		return Integer.parseInt(o.toString());
	}

	public static ImageIcon img(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

	public static ImageIcon img(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path));
	}

	public static JLabel imglbl(String path, int w, int h) {
		return new JLabel(
				new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH)));
	}

	public static JLabel imglbl(String path) {
		return new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(path)));
	}

	public static void setLine(JComponent c) {
		c.setBorder(new LineBorder(Color.black));
	}

	static class Before extends WindowAdapter {
		BaseFrame b;

		public Before(BaseFrame b) {
			this.b = b;
			windows.add(b);
			b.setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			if (isPayed) {
				windows.get(1).setVisible(true);
				isPayed = false;
				return;
			} else {
				b.setVisible(true);
			}
		}
	}
}
