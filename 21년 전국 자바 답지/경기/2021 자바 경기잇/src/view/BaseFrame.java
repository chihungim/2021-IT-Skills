package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class BaseFrame extends JFrame {

	static String uno, uname;
	static int uold, uheight, udisable;
	static String age[] = { "", "성인", "청소년", "어린이", "노인" };

	static Statement stmt = db.DB.stmt;
	static Connection con = db.DB.con;

	static {
		try {
			stmt.execute("use adventure");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BaseFrame(String title, int w, int h) {
		super(title);

		this.setSize(w, h);
		this.setDefaultCloseOperation(2);
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.white);
	}

	static void setLogin(int no) {
		try {
			var rs = stmt.executeQuery("select * from user where u_no=" + no);
			if (rs.next()) {
				uno = rs.getString(1);
				uname = rs.getString(2);
				uheight = rs.getInt(5);
				uold = getAge(LocalDate.parse(rs.getString(6)));
				udisable = rs.getInt(8);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JLabel imglbl(String path, int w, int h) {
		return new JLabel(
				new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH)));
	}

	public JLabel imglbl(String path) {
		return new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(path)));
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

	public void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "오류", JOptionPane.ERROR_MESSAGE);
	}

	void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "정보", JOptionPane.INFORMATION_MESSAGE);
	}

	<T extends JComponent> T sz(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	JComponent setLine(JComponent c, Color col) {
		c.setBorder(new LineBorder(col));
		return c;
	}

	void setEmpty(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
	}

	ImageIcon img(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

	ImageIcon img(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path));
	}

	JButton btn(String text, ActionListener a) {
		JButton b = new JButton(text);
		b.addActionListener(a);
		return b;
	}

	JLabel lbl(String text, int alig) {
		JLabel l = new JLabel(text, alig);
		return l;
	}

	JLabel lbl(String text, int alig, int size) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("맑은 고딕", Font.BOLD, size));
		return l;
	}

	DefaultTableModel model(String[] str) {
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
		return t;
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static int rei(Object obj) {

		return obj.toString().isEmpty() ? 0 : Integer.parseInt(obj.toString());
	}

	static int val(Object obj) {
		int i = rei(obj.toString().replaceAll("[^0-9]", ""));
		return i == 0 ? 0 : i;
	}

	static int getAge(LocalDate date) {

		int year = date.getYear();
		int age = 0;
		for (int i = year + 1; i <= LocalDate.now().getYear(); i++) {
			if (LocalDate.of(i, date.getMonthValue(), date.getDayOfMonth()).isBefore(LocalDate.now())
					|| LocalDate.of(i, date.getMonthValue(), date.getDayOfMonth()).isEqual(LocalDate.now())) {
				age += 1;
			}
		}
		return age;
	}

	boolean isNumeric(Object obj) {
		try {
			Integer.parseInt(obj.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	class IconLabel extends JLabel {

		String title;

		public IconLabel(String title, boolean enabled) {
			super("", img("./datafiles/아이콘.png", 25, 30), 0);
			this.title = title;
			this.setEnabled(enabled);

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (!enabled) {
						eMsg("회원님이 이용할 수 없는 놀이기구입니다.");
						return;
					}
					new MoreInform(title).addWindowListener(new Before(BaseFrame.this));
				}
			});
		}

		public IconLabel(String title, String floor) {
			super("", img("./datafiles/아이콘.png", 25, 30), 0);
			this.title = title;

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

					new Ride_apply_edit(Ride_apply_edit.EDIT, title, floor, (Ride) BaseFrame.this);
				}
			});

		}
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
