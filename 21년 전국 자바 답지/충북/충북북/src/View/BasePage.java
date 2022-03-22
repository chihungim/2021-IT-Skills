package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

public class BasePage extends JPanel {
	public static MainFrame mf = new MainFrame();
	public static Connection con;
	public static Statement stmt;
	public static int u_serial, u_region, idx = -1, a_serial;
	public static String al_serial, s_serial, ar_serial;
	public static Color myColor = new Color(50, 100, 255);
	public static HomePage homePage;
	static DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	static LinkedList<Integer> que = new LinkedList<Integer>();
	static Stack<Integer> cache = new Stack<>();
	static boolean isPlaying = false;
	static QueuePage queuePage;
	static Timebar bar;
	static HashMap<Integer, Integer> getAlbumRef = new HashMap<>();
	static Timer progress;
	static int time, max;
	static {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true&allowPublicKeyRetrieval=true",
					"root", "1234");
			stmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void next() {
		var value = que.poll();
		if (value == null) {
			notPlaying();
			return;
		}
		cache.push(value);
		queuePage.setCurPlaying(value);
		isPlaying = true;
		homePage.s.setPlaying();
		queuePage.setQue();
		progress.start();
	}

	static void notPlaying() {
		queuePage.n_c.removeAll();
		queuePage.n_c.add(queuePage.notPlaying);
		queuePage.revalidate();
		homePage.s.songName.setText("재생중이 아님");
		homePage.s.img.setIcon(null);
		isPlaying = false;
		homePage.s.setPlaying();
		bar.setValue(0);
		progress.stop();
	}

	static void addtoQue(int val) {
		que.add(val);
		queuePage.setQue();
	}

	static void prev() {
		if (cache.isEmpty()) {
			notPlaying();
			return;
		}
		queuePage.setQue();
		var value = cache.pop();
		que.addFirst(value);
		queuePage.setCurPlaying(value);
		isPlaying = true;
		homePage.s.setPlaying();
		progress.start();
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
		mf.setVisible(true);
	}

	void addtoPlayList(int s_serial) {
		ArrayList<String> list = new ArrayList<String>();
		JComboBox<?> box;
		try {
			var rs = BasePage.stmt
					.executeQuery("select pl.name from playlist pl, user u where u.serial = pl.user and u.serial="
							+ BasePage.u_serial);
			while (rs.next()) {
				list.add(rs.getString(1));
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

	public static JLabel lbl(String c, int a, int style, int size, MouseListener m) {
		JLabel l = new JLabel(c, a);
		l.setForeground(Color.white);
		l.setFont(new Font("맑은 고딕", style, size));
		l.addMouseListener(m);
		return l;
	}

	public static JLabel lbl(String c, int a, int size) {
		JLabel l = new JLabel(c, a);
		l.setForeground(Color.white);
		l.setFont(new Font("맑은 고딕", 0, size));
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
				next();

			this.value = value;
		}
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
