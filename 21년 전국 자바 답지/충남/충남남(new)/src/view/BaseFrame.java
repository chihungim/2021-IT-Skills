package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import db.DB;

public class BaseFrame extends JFrame {

	JPanel n, c, s, w, e;
	static LocalTime startTime = LocalTime.now();
	static String station[] = new String[276];
	static int uno, sno, age, birthyear;
	static String uname;
	Color lightBlue = new Color(50, 100, 255);
	static Connection con = DB.con;
	static Statement stmt = DB.stmt;
	static HashMap<String, Integer> lineMap = new HashMap<>();
	static HashMap<String, String> lineName = new HashMap<>();
	static HashMap<String, String> line = new HashMap<>();
	static HashMap<String, Integer> metroSerial = new HashMap<>();
	static HashMap<Integer, String>[] map = new HashMap[31];
	static HashMap<Integer, String> stations = new HashMap<>();
	static HashMap<Integer, ArrayList<Integer>> MetroSerialToLines = new HashMap<>();
	static ArrayList<Integer> lineList[] = new ArrayList[6];
	static int cost[][] = new int[276][276];
	static int cost2[][] = new int[276][276];

	public BaseFrame(String tit, int w, int h) {
		super("¼­¿ï¸ÞÆ®·Î - " + tit);
		this.setSize(w, h);
		this.setDefaultCloseOperation(2);
		this.setLocationRelativeTo(null);
		dataInit();
	}

	void execute(String sql) {
		try {
			stmt.execute(sql);
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

	void dataInit() {

		for (int i = 1; i <= 30; i++) {
			ArrayList<Integer> lines = new ArrayList<>();
			try {
				var rs = stmt.executeQuery("SELECT * FROM metro.route where metro = " + i);
				while (rs.next()) {
					lines.add(rs.getInt(2));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MetroSerialToLines.put(i, lines);
		}

		try {
			var rs = stmt.executeQuery("select * from metro");
			while (rs.next()) {
				metroSerial.put(rs.getString(2), rs.getInt(1));
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery("select * from station");
			while (rs.next()) {
				station[rs.getInt(1)] = rs.getString(2);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 1; i < 31; i++) {
			map[i] = new HashMap<>();
			try {
				var rs = stmt.executeQuery(
						"SELECT r.station, s.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and left(r.metro,1) = "
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
			cost = new int[584][3]; // path°¹¼ö: 584°³
			int i = 0;
			while (rs.next()) {
				cost[i][0] = rs.getInt(2);
				cost[i][1] = rs.getInt(3);
				cost[i][2] = rs.getInt(4);
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

		for (int i = 1; i < 6; i++) {
			lineList[i] = new ArrayList<>();
			try {
				var rs = stmt.executeQuery(
						"SELECT distinct r.station FROM metro m , route r where r.metro = m.serial and left(m.name,1)="
								+ i);
				while (rs.next()) {
					lineList[i].add(rs.getInt(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			var rs = stmt.executeQuery("SELECT * FROM metro.path");
			while (rs.next()) {
				cost2[rs.getInt(2)][rs.getInt(3)] = rs.getInt(4);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery(
					"select p.departure, p.arrive, sub1.ho from `path` p, (SELECT distinct r.station st, left(m.name,1) ho FROM metro m , route r where r.metro = m.serial) sub1, (SELECT distinct r.station st, left(m.name,1) ho FROM metro m , route r where r.metro = m.serial) sub2 where (sub1.st<>sub2.st and sub1.ho=sub2.ho) and (p.departure = sub1.st and p.arrive = sub2.st);");
			while (rs.next()) {
				lineMap.put(rs.getInt(1) + "," + rs.getInt(2), rs.getInt(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery(
					"select p.departure, p.arrive, sub1.name from `path` p, (SELECT distinct r.station st, left(m.name,1) ho, m.name FROM metro m , route r where r.metro = m.serial) sub1, (SELECT distinct r.station st, left(m.name,1) ho FROM metro m , route r where r.metro = m.serial) sub2 where (sub1.st<>sub2.st and sub1.ho=sub2.ho) and (p.departure = sub1.st and p.arrive = sub2.st)");
			while (rs.next()) {
				lineName.put(rs.getInt(1) + "," + rs.getInt(2), rs.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	String getOne(String sql) {
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

	static void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "°æ°í", JOptionPane.ERROR_MESSAGE);
	}

	static void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Á¤º¸", JOptionPane.INFORMATION_MESSAGE);
	}

	static <T extends JComponent> T sz(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	static JLabel lbl(String text, int alig) {
		JLabel l = new JLabel(text, alig);
		return l;
	}

	static JLabel lbl(String text, int alig, int size) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, size));
		return l;
	}

	static JLabel lbl(String text, int alig, int size, Color col) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, size));
		l.setForeground(col);
		return l;
	}

	static JLabel lbl(String text, int alig, int size, Color col, int font) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("¸¼Àº °íµñ", font, size));
		l.setForeground(col);
		return l;
	}

	static JButton btn(String text, ActionListener a) {
		JButton b = new JButton(text);
		b.addActionListener(a);
		b.setBackground(new Color(50, 100, 255));
		b.setForeground(Color.white);
		return b;
	}

	void setLine(JComponent c) {
		c.setBorder(new LineBorder(Color.black));
	}

	static void setEmpty(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
	}

	ImageIcon img(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

	static int rei(Object obj) {
		if (obj.toString().equals(""))
			return 0;
		return Integer.parseInt(obj.toString());
	}

	void closeAll() {
		for (Window windows : Window.getWindows()) {
			windows.dispose();
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

	class HolderTextField extends JTextField {
		boolean textWritten = false;

		public HolderTextField(int columns, String text) {
			super(columns);
			setPlaceHolder(text);
		}

		void setPlaceHolder(String text) {
			setText(text);
			this.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					// TODO Auto-generated method stub
					warn();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					// TODO Auto-generated method stub
					warn();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					// TODO Auto-generated method stub
					warn();
				}

				public void warn() {
					if (getText().trim().length() != 0) {
						setForeground(Color.BLACK);
						textWritten = true;
					}
				}
			});

			this.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					if (getText().trim().length() == 0) {
						customText(text);
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
					if (!textWritten) {
						setText("");
					}
				}
			});

		}

		void customText(String text) {
			setText(text);
			setForeground(Color.LIGHT_GRAY);
			textWritten = false;
		}

	}

}
