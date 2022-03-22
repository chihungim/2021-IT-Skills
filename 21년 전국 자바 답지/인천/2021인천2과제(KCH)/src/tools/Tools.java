package tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Tools {

	public static Connection con;
	public static Statement stmt;

	static {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true&allowPublicKeyRetrieval=true",
					"user", "1234");
			stmt = con.createStatement();
			stmt.execute("use delivery");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String getOneRef(String sql) {
		try {
			var rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static ResultSet rs(String sql) {
		try {
			var rs = stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "����", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "����", JOptionPane.ERROR_MESSAGE);
	}

	public static <T extends JComponent> T size(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	public static void setLine(JComponent c) {
		c.setBorder(new LineBorder(Color.black));
	}

	public static void setLine(JComponent c, Color col) {
		c.setBorder(new LineBorder(col));
	}

	public static void setEmpty(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
	}

	public static JButton btn(String text, ActionListener a) {
		JButton b = new JButton(text);
		b.addActionListener(a);
		b.setFont(new Font("", Font.PLAIN, 15));
		setLine(b);
		b.setMargin(new Insets(0, 0, 0, 0));
		b.setBackground(Color.white);
		return b;
	}

	public static JButton btn(String text, ActionListener a, Icon icon) {
		JButton b = new JButton(text, icon);
		b.addActionListener(a);
		b.setBackground(Color.white);
		setLine(b, Color.LIGHT_GRAY);
		b.setHorizontalTextPosition(JButton.CENTER);
		b.setVerticalTextPosition(JButton.BOTTOM);
		return b;
	}

	public static JLabel lbl(String text, int alig) {
		JLabel l = new JLabel(text, alig);
		return l;
	}

	public static JLabel lbl(String text, int alig, int size) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("", Font.PLAIN, size));
		return l;
	}

	public static JLabel lbl(String text, int alig, int size, Color col) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("", Font.BOLD, size));
		l.setForeground(col);
		return l;
	}

	public static JLabel lbl(String text, int alig, Color col) {
		JLabel l = new JLabel(text, alig);
		l.setForeground(col);
		return l;
	}

	public static boolean isNum(Object obj) {
		try {
			Integer.parseInt(obj.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static int toInt(Object obj) {
		return Integer.parseInt(obj.toString());
	}

	public static int val(Object obj, int base) {
		return obj.toString().isEmpty() ? base : Integer.parseInt(obj.toString());
	}

	public static ImageIcon img(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

}
