package 충남;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BaseFrame extends JFrame {
	JPanel c, w, e, s, n;
	static int NO = -1, sno,age,birth;
	static String NAME;
	static Statement stmt;
	static Connection con;
	static DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	static ArrayList<String> metroNames = new ArrayList<String>();
	static ArrayList<String> stNames = new ArrayList<String>();
	static Object metroStinfo[][] = new Object[31][4];
	static int adj[][] = new int[276][276];
	static int line[][] = new int[276][276];
	static int metroTime[][][] = new int[31][100][300];
	static LocalTime now = LocalTime.now();
	int INF = 1000000000;
	ResultSet rs;

	static {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost?allowLoadLocalInfile=true&serverTimezone=UTC&allowPublicKeyRetreival=true",
					"user", "1234");
			stmt = con.createStatement();
			stmt.execute("use metro");
			dtcr.setHorizontalAlignment(dtcr.CENTER);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void data() throws SQLException {
		rs = stmt.executeQuery("select * from metro");
		metroNames.add("");
		while (rs.next()) {
			metroNames.add(rs.getString(2));
		}

		rs = stmt.executeQuery("select * from station");
		stNames.add("");
		while (rs.next()) {
			stNames.add(rs.getString(2));
		}

		for (int i = 1; i < 31; i++) {
			metroStinfo[i][0] = new ArrayList<String>();
			((ArrayList) metroStinfo[i][0]).add("");

			rs = stmt.executeQuery(
					"select s.name from route r, station s where r.metro = " + i + " and r.station = s.serial");
			while(rs.next()) {
				((ArrayList)metroStinfo[i][0]).add(rs.getString(1));
			}
			rs = stmt.executeQuery("select * from metro where serial = "+i);
			rs.next();
			metroStinfo[i][1] = rs.getString(3);
			metroStinfo[i][2] = rs.getString(4);
			metroStinfo[i][3] = rs.getString(5);
		}
		
		for (int i = 1; i <= 30; i++) {
			ArrayList al = (ArrayList) metroStinfo[i][0];
			for (int j = 1; j <= al.size() -2; j++) {
				line[stNames.indexOf(al.get(j))][stNames.indexOf(al.get(j+1))] = i;
			}
		}
		
		for (int i = 1; i < 276; i++) {
			for (int j = i + 1; j < 276; j++) {
				adj[i][j] = adj[j][i] = INF;
			}
		}
		rs = stmt.executeQuery("select * from path");
		while(rs.next()) {
			adj[rs.getInt(2)][rs.getInt(3)] = rs.getInt(4) * 5; 
		}
		
		for (int i = 1; i <= 30; i++) {
			for (int j = 1; j < 100; j++) {
				ArrayList m = (ArrayList)metroStinfo[i][0];
				if(j >= m.size()) break;
				
				int st = LocalTime.parse((CharSequence) metroStinfo[i][1]).toSecondOfDay();
				int et = LocalTime.parse((CharSequence) metroStinfo[i][2]).toSecondOfDay();
				int gap = LocalTime.parse((CharSequence) metroStinfo[i][3]).toSecondOfDay();
				
				metroTime[i][j][0] = j == 1?st : metroTime[i][j - 1][0] + adj[stNames.indexOf(m.get(j - 1))][stNames.indexOf(m.get(j))];
				for (int k = 1; k < 300; k++) {
					if(j > 1 && metroTime[i][j-1][k] == 0) break;
					if(metroTime[i][j][k- 1] +gap > et) break;
					metroTime[i][j][k] = metroTime[i][j][k - 1] + gap;
				}
			}
		}
	}

	static void msg(String message) {
		JOptionPane.showMessageDialog(null, message, "메시지", JOptionPane.INFORMATION_MESSAGE);
	}

	static void errmsg(String message) {
		JOptionPane.showMessageDialog(null, message, "메시지", JOptionPane.ERROR_MESSAGE);
	}

	static JLabel label(String str, int alig) {
		JLabel jl = new JLabel(str, alig);
		return jl;
	}

	static JLabel label(String str, int alig, int size) {
		JLabel jl = new JLabel(str, alig);
		jl.setFont(new Font("", Font.BOLD, size));
		return jl;
	}
	
	static JLabel label(String str, int alig, int size, Color col) {
		JLabel jl = new JLabel(str, alig);
		jl.setFont(new Font("", Font.BOLD, size));
		jl.setForeground(col);
		return jl;
	}

	JLabel labelP(String str, int alig, int size) {
		JLabel jl = new JLabel(str, alig);
		jl.setFont(new Font("", Font.PLAIN, size));
		return jl;
	}

	static JButton btn(String str, ActionListener a) {
		JButton jb = new JButton(str);
		jb.addActionListener(a);
		jb.setBackground(new Color(50, 100, 255));
		jb.setForeground(Color.WHITE);
		return jb;
	}
	
	void hold(JTextField tt, String hold) {
		tt.setText(hold);
		tt.setForeground(Color.LIGHT_GRAY);
		
		tt.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(tt.getText().trim().isEmpty()) {
					tt.setText(hold);
					tt.setForeground(Color.LIGHT_GRAY);
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(tt.getText().equals(hold)) {
					tt.setText("");
					tt.setForeground(Color.BLACK);
				}
				
			}
		});
	}
	
	String getone(String sql) {
		try {
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				return rs.getString(1);
			}
			return "";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	JComponent addCom(JComponent c, int x, int y, int w, int h) {
		add(c);
		c.setBounds(x, y, w, h);
		return c;
	}

	JComponent addCom(JPanel p, JComponent c, int x, int y, int w, int h) {
		p.add(c);
		c.setBounds(x, y, w, h);
		return c;
	}

	static <T extends JComponent> T siz(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}
	
	String tformat(LocalTime t, String format) {
		return DateTimeFormatter.ofPattern(format).format(t);
	}
	
	String format(int str, String format) {
		return new DecimalFormat(format).format(str);
	}

	static int toint(Object str) {
		return Integer.parseInt(str.toString());
	}

	ImageIcon img(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/" + path));
	}

	ImageIcon img(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/" + path).getScaledInstance(w, h,
				Image.SCALE_SMOOTH));
	}

	ImageIcon imgP(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

	DefaultTableModel model(String str[]) {
		DefaultTableModel dtm = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};

		return dtm;
	}

	JTable table(DefaultTableModel dtm) {
		JTable table = new JTable(dtm);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		return table;
	}

	JComponent line(JComponent c) {
		c.setBorder(new LineBorder(Color.BLACK));
		return c;
	}

	JComponent emp(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
		return c;
	}
	
	class be extends WindowAdapter {
		BaseFrame b;
		
		public be(BaseFrame bef) {
			b = bef;
			bef.setVisible(false);
		}
		
		@Override
		public void windowClosed(WindowEvent e) {
			b.setVisible(true);
		}
	}

	public BaseFrame(String title, int w, int h) {
		setTitle("서울메트로 - " + title);
		setSize(w, h);
		setIconImage(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(2);
	}

}
