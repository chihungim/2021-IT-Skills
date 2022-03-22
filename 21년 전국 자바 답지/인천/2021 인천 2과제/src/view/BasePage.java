package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class BasePage extends JPanel {
	JPanel c, w, e, n, s;
	public static MainFrame mf = new MainFrame();
	static int uno, sno, s_addr, u_addr;

	static Connection con = db.DB.con;
	static Statement stmt = db.DB.stmt;
	static {
		try {
			stmt.execute("use Delivery");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String uname, sname, pname;
	static LocalDateTime startTime;

	public static void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getOne(String sql) {
		try {
			var rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			return null;
		}
		return "";
	}

	public static ResultSet rs(String sql) throws SQLException {
		var rs = stmt.executeQuery(sql);
		return rs;
	}

	public static JComponent setBorder(JComponent jc, Border b) {
		jc.setBorder(b);
		return jc;
	}

	public static JButton btn(String cap, ActionListener a) {
		var btn = new JButton(cap);
		btn.addActionListener(a);
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setBackground(Color.white);
		btn.setBorder(new LineBorder(Color.BLACK));
		return btn;
	}

	public static JLabel lbl(String cap, int al) {
		return new JLabel(cap, al);
	}

	public static JLabel lbl(String cap, int al, int s) {
		var lbl = new JLabel(cap, al);
		lbl.setFont(new Font("", Font.BOLD, s));
		return lbl;
	}

	public static void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "정보", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "오류", JOptionPane.ERROR_MESSAGE);
	}

	public static <T extends JComponent> T sz(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	public static ImageIcon getImage(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

	public static ImageIcon getImage(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path));
	}

	public static Integer toInt(Object obj) {
		if (obj.equals(""))
			return 0;
		return Integer.parseInt(obj.toString());
	}

	public BasePage() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
	}

	public static void logout() {
		iMsg("로그아웃 하셨습니다.");
		BasePage.uno = 0;
		BasePage.uname = "";
		BasePage.sno = 0;
		BasePage.sname = "";
		mf.swapPage(new MainPage());
		mf.btn.setText("Login");
		mf.menuInit(true);
	}

	static class JPanel extends javax.swing.JPanel {
		public JPanel() {
			super(new FlowLayout(FlowLayout.LEFT, 5, 5));
			setOpaque(false);
		}

		public JPanel(LayoutManager l) {
			super(l);
			setOpaque(false);
		}
	}
}
