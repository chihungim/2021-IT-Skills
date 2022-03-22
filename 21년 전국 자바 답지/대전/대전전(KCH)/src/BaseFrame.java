import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BaseFrame extends JFrame {
	static Connection con;
	static Statement stmt;
	static int cfw, cfh, u_no, p_no, price, count;
	static String u_name, date, p_price, p_name, p_place, p_date, total_price, u_id;
	static int sumPrice;
	static boolean isLogined, remId, isEdit;
	JLabel totalidx = new JLabel();
	DecimalFormat format = new DecimalFormat("#,##0");
	static DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	int p;
	static before bf;
	static boolean bool;
	static {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true&allowPublicKeyRetrieval=true",
					"root", "1234");
			stmt = con.createStatement();
			stmt.execute("use 2021전국");
		} catch (SQLException e) {
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

	public JLabel imgLabel(String path) {
		return new JLabel(new ImageIcon(path));
	}

	public JLabel imgLabel(String path, int w, int h) {
		return new JLabel(
				new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH)));
	}

	public BaseFrame(String title, int w, int h) {
		super(title);
		setSize(w, h);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(new ImageIcon("Datafiles/오렌지.jpg").getImage());
	}

	void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "경고", JOptionPane.ERROR_MESSAGE);
	}

	void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "정보", JOptionPane.INFORMATION_MESSAGE);
	}

	JLabel lbl(String text, int alig) {
		JLabel lbl = new JLabel(text, alig);
		return lbl;
	}

	public JLabel lbl(String text, int alig, int size) {
		JLabel lbl = new JLabel(text, alig);
		lbl.setFont(new Font("", Font.BOLD, size));
		return lbl;
	}

	JButton btn(String text, ActionListener a) {
		JButton btn = new JButton(text);
		btn.addActionListener(a);
		return btn;
	}

	int toInt(Object path) {
		return Integer.parseInt(path.toString());
	}

	DefaultTableModel model(String[] col) {
		DefaultTableModel m = new DefaultTableModel(null, col) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
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

	void addrow(DefaultTableModel m, String sql) {
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

	void addrow(DefaultTableModel m, String p_date, String date, String value) {
		Object row[] = new Object[3];
		row[0] = p_date;
		row[1] = date;
		row[2] = value;

		m.addRow(row);

	}

	static <T extends JComponent> T size(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	static class before extends WindowAdapter {
		static ArrayList<BaseFrame> baseFrames = new ArrayList<>();
		BaseFrame b;

		public before(BaseFrame b) {
			this.b = b;
			baseFrames.add(b);
			b.setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			b.setVisible(true);
		}
	}
}
