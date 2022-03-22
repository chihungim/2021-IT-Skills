package base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import ui.HomePage;
import ui.MainFrame;

public class BasePage extends JPanel {
	public static MainFrame mf = new MainFrame();
	public static Connection con;
	public static Statement stmt;
	public static int u_serial, u_region;
	public static String al_serial, s_serial, ar_serial;
	public static Color myColor = new Color(50, 100, 255);
	public static HomePage homePage;
	static DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
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

	public BasePage() {
		setLayout(new BorderLayout());
		execute("use music");
		setBackground(Color.black);

		mf.setVisible(true);
	}

	public static int toInt(Object p) {
		return Integer.parseInt(p.toString());
	}



	public static void execute(String sql) {
		try {
			System.out.println(sql);
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static JLabel lbl(String c, int a, int style, int size) {
		JLabel l = new JLabel(c, a);
		l.setForeground(Color.white);
		l.setFont(new Font("¸¼Àº °íµñ", style, size));
		return l;
	}

	public static JLabel lbl(String c, int a, int style, int size, MouseAdapter b) {
		JLabel l = new JLabel(c, a);
		l.setForeground(Color.white);
		l.setFont(new Font("¸¼Àº °íµñ", style, size));
		l.addMouseListener(b);
		return l;
	}

	public static JButton btn(String cap, ActionListener a) {
		JButton b = new JButton(cap);
		b.setBackground(Color.white);
		b.addActionListener(a);
		return b;
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
