package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class BaseFrame extends JFrame {

	static Connection con = db.DB.con;
	static Statement stmt = db.DB.stmt;
	static final int INF = 1000000;
	JPanel n, w, c, s, e;
	static final String IMAGE = "./지급자료/images/";
	static ArrayList<String> mNames = new ArrayList<>();
	static ArrayList<String> stNames = new ArrayList<>();
	static Object mstInfos[][] = new Object[31][4]; // row lines column, list정류장 ,s , e, i
	static int adjDim[][] = new int[276][276];
	static int mTimes[][][] = new int[31][100][300];
	static int lineDim[][] = new int[276][276];
	static int no, age, birth;
	static String name;
	static {
		try {
			stmt.execute("use Metro");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BaseFrame(String title, int w, int h) {
		super("서울매트로 - " + title);
		setSize(w, h);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		UIManager.put("Button.foreground", Color.WHITE);
		UIManager.put("Button.background", new Color(60, 60, 240));
	}

	static String getOne(String sql) {
		try {
			var rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(1);
			} else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	static void datainit() throws SQLException {
		ResultSet rs = null;
		rs = stmt.executeQuery("select * from metro");
		mNames.add("");
		while (rs.next()) {
			mNames.add(rs.getString(2));
		}

		rs = stmt.executeQuery("select * from station");
		stNames.add("");
		while (rs.next()) {
			stNames.add(rs.getString(2));
		}

		for (int i = 1; i < 31; i++) {
			mstInfos[i][0] = new ArrayList<String>();
			((ArrayList) mstInfos[i][0]).add("");

			rs = stmt.executeQuery(
					"select s.name from route r, station s where r.metro = " + i + " and r.station = s.serial");
			while (rs.next()) {
				((ArrayList) mstInfos[i][0]).add(rs.getString(1));
			}
			rs = stmt.executeQuery("select * from metro where serial = " + i);
			rs.next();
			mstInfos[i][1] = rs.getString(3);
			mstInfos[i][2] = rs.getString(4);
			mstInfos[i][3] = rs.getString(5);
		}

		for (int i = 1; i <= 30; i++) {
			ArrayList al = (ArrayList) mstInfos[i][0];
			for (int j = 1; j <= al.size() - 2; j++) {
				lineDim[stNames.indexOf(al.get(j))][stNames.indexOf(al.get(j + 1))] = i;
			}
		}

		for (int i = 1; i < 276; i++) {
			for (int j = i + 1; j < 276; j++) {
				adjDim[i][j] = adjDim[j][i] = INF;
			}
		}
		rs = stmt.executeQuery("select * from path");
		while (rs.next()) {
			adjDim[rs.getInt(2)][rs.getInt(3)] = rs.getInt(4) * 5;
		}

		for (int i = 1; i <= 30; i++) {
			for (int j = 1; j < 100; j++) {
				ArrayList m = (ArrayList) mstInfos[i][0];
				if (j >= m.size())
					break;

				int st = LocalTime.parse((CharSequence) mstInfos[i][1]).toSecondOfDay();
				int et = LocalTime.parse((CharSequence) mstInfos[i][2]).toSecondOfDay();
				int gap = LocalTime.parse((CharSequence) mstInfos[i][3]).toSecondOfDay();

				mTimes[i][j][0] = j == 1 ? st
						: mTimes[i][j - 1][0] + adjDim[stNames.indexOf(m.get(j - 1))][stNames.indexOf(m.get(j))];
				for (int k = 1; k < 300; k++) {
					if (j > 1 && mTimes[i][j - 1][k] == 0)
						break;
					if (mTimes[i][j][k - 1] + gap > et)
						break;
					mTimes[i][j][k] = mTimes[i][j][k - 1] + gap;
				}
			}
		}
	}

	static int toInt(Object path) {
		return Integer.parseInt(path.toString());
	}

	static JButton btn(String cap, ActionListener a) {
		JButton btn = new JButton(cap);
		btn.addActionListener(a);
		return btn;
	}

	static JLabel lbl(String cap, int alig) {
		return new JLabel(cap, alig);
	}

	static JLabel lbl(String cap, int alig, int size) {
		var l = new JLabel(cap, alig);
		l.setFont(new Font("맑은 고딕", Font.BOLD, size));
		return l;
	}

	static JLabel lbl(String cap, int alig, int size, Color col) {
		var l = new JLabel(cap, alig);
		l.setForeground(col);
		l.setFont(new Font("맑은 고딕", Font.BOLD, size));
		return l;
	}

	static ImageIcon getIcon(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

	static ImageIcon getIcon(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path));
	}

	JComponent sz(JComponent jc, int w, int h) {
		jc.setPreferredSize(new Dimension(w, h));
		return jc;
	}

	static void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "경고", JOptionPane.ERROR_MESSAGE);
	}

	static void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "정보", JOptionPane.INFORMATION_MESSAGE);
	}

	class before extends WindowAdapter {
		BaseFrame b;

		public before(BaseFrame b) {
			this.b = b;
			b.setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			b.setVisible(true);
			super.windowClosed(e);
		}
	}

}
