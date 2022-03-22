package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import DB.DB;

public class BaseFrame extends JFrame {
	static Connection con = DB.con;
	static Statement stmt = DB.stmt;
	static int u_no;
	static String u_name;
	static HashMap<Integer, String>[] map = new HashMap[31];
	static HashMap<Integer, String> stations = new HashMap<>();
	static int cost[][] = new int[276][276];
	boolean isLogined = false;

	void init() {
		try {
			var rs = stmt.executeQuery("SELECT * FROM metro.station");
			while (rs.next()) {
				stations.put(rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 1; i < 31; i++) {
			map[i] = new HashMap<>();
			try {
				var rs = stmt.executeQuery(
						"SELECT r.station, s.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and r.metro = "
								+ i);
				while (rs.next()) {
					map[i].put(rs.getInt(1), rs.getString(2));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			var rs = stmt.executeQuery("SELECT * FROM metro.path");
			while (rs.next()) {
				cost[rs.getInt(2)][rs.getInt(3)] = rs.getInt(4);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static {
		try {
			stmt.execute("use metro");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

	public BaseFrame(String title, int w, int h) {
		super("서울메트로 - " + title);
		setSize(w, h);
		init();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	static JButton btn(String cap, ActionListener a) {
		JButton btn = new JButton(cap);
		btn.setBorderPainted(false);
		btn.setBackground(Color.BLUE.darker());
		btn.setForeground(Color.WHITE);
		btn.addActionListener(a);
		return btn;
	}

	static <T extends JComponent> T size(T t, int w, int h) {
		t.setPreferredSize(new Dimension(w, h));
		return t;
	}

	static JLabel lbl(String t, int a) {
		JLabel lbl = new JLabel(t, a);
		return lbl;
	}

	static JLabel lbl(String t, int a, int s) {
		JLabel lbl = new JLabel(t, a);
		lbl.setFont(new Font("", Font.BOLD, s));
		return lbl;
	}

	static int toInt(Object path) {
		if (path.toString().equals(""))
			return 0;
		return Integer.parseInt(path.toString());
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
