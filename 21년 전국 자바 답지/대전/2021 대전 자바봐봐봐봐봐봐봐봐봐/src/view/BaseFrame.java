package view;

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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BaseFrame extends JFrame {
	static Connection con = db.DB.con;
	static Statement stmt = db.DB.stmt;
	static String uno = "", uname, pdate;
	static String pinfo[] = new String[7];
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	static {
		try {
			stmt.execute("use 2021����");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BaseFrame(String title, int w, int h) {
		super(title);
		setSize(w, h);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./Datafiles/������.jpg"));
	}

	public static String getOne(String sql) {
		try {
			var rs = stmt.executeQuery(sql);
			if (rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	JButton btn(String cap, ActionListener a) {
		JButton b = new JButton(cap);
		b.addActionListener(a);
		return b;
	}

	JLabel lbl(String text, int a) {
		return new JLabel(text, a);
	}

	JLabel[] lbls(String[] text, int a, int size) {
		var l = new JLabel[text.length];

		for (int i = 0; i < text.length; i++) {
			l[i] = new JLabel(text[i], a);
			l[i].setFont(new Font("���� ����", Font.PLAIN, size));
		}

		return l;
	}

	void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "���", JOptionPane.ERROR_MESSAGE);
	}

	void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "����", JOptionPane.INFORMATION_MESSAGE);
	}

	JComponent setBorder(JComponent jc, Border b) {
		jc.setBorder(b);
		return jc;
	}

	static <T extends JComponent> T sz(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	JLabel lbl(String text, int a, int size) {
		JLabel l = new JLabel(text, a);
		l.setFont(new Font("���� ����", Font.BOLD, size));
		return l;
	}

	static DefaultTableModel model(String[] str) {
		DefaultTableModel m = new DefaultTableModel(null, str) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return m;
	}

	JTable table(DefaultTableModel m) {
		JTable t = new JTable(m);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.getTableHeader().setReorderingAllowed(false);
		t.getTableHeader().setResizingAllowed(false);
		for (int i = 0; i < t.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		return t;
	}

	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void addRow(DefaultTableModel m, String sql) {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	int toInt(Object path) {
		return Integer.parseInt(path.toString());
	}

	ImageIcon img(String p, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(p).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

	ImageIcon img(String p) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(p));
	}

	class Before extends WindowAdapter {
		BaseFrame b;

		public Before(BaseFrame b) {
			this.b = b;
			b.setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			b.setVisible(true);
		}
	}
}
