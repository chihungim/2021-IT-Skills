package ±§¡÷;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Basedialog extends JDialog {
	static Statement stmt;
	static Connection con;
	static DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	static int NO, sNO;
	static String NAME, TYPE;
	Color green = Color.GREEN.darker().darker();
	DecimalFormat format = new DecimalFormat("#,##0");
	static ArrayList<ArrayList<Object>> order = new ArrayList<ArrayList<Object>>();
	static boolean isPur;
	
	static {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost?allowLoadLocalInfile=true&serverTimezone=UTC&allowPublicKeyRetrieval=true","user","1234");
			stmt = con.createStatement();
			stmt.execute("use eats");
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
	
	void errmsg(String str) {
		JOptionPane.showMessageDialog(null, str, "∏ﬁΩ√¡ˆ", JOptionPane.ERROR_MESSAGE);
	}
	
	void msg(String str) {
		JOptionPane.showMessageDialog(null, str, "∏ﬁΩ√¡ˆ", JOptionPane.INFORMATION_MESSAGE);
	}
	
	JLabel label(String str, int alig) {
		JLabel jl = new JLabel(str, alig);
		return jl;
	}
	
	DefaultTableModel model(String str[]) {
		DefaultTableModel dtm = new DefaultTableModel(null, str) {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		return dtm;
	}
	
	JLabel label(String str, int alig, int size) {
		JLabel jl = new JLabel(str, alig);
		jl.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, size));
		return jl;
	}
	
	JLabel labelP(String str, int alig, int size) {
		JLabel jl = new JLabel(str, alig);
		jl.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.PLAIN, size));
		return jl;
	}
	
	JComponent addCom(JComponent c, int x, int y, int w, int h) {
		c.setBounds(x, y, w, h);
		return c;
	}
	
	JComponent addCom(JPanel p, JComponent c, int x, int y, int w, int h) {
		p.add(c);
		c.setBounds(x, y, w, h);
		return c;
	}
	
	static <T extends JComponent>T size(T c, int w, int h){
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}
	
	JButton btn(String str, ActionListener a) {
		JButton jb = new JButton(str);
		jb.addActionListener(a);
		jb.setBackground(Color.GREEN.darker());
		jb.setForeground(Color.WHITE);
		return jb;
	}
	
	int toint(Object str) {
		return Integer.parseInt(str.toString());
	}
	
	ImageIcon img(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage("./¡ˆ±ﬁ¿⁄∑·/"+path));
	}
	
	ImageIcon img(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage("./¡ˆ±ﬁ¿⁄∑·/"+path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	ImageIcon imgP(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	static class PlaceH extends JTextField {
		String place;
		
		public PlaceH(int col) {
			super(col);
		}
		
		void setPlace(String str) {
			place = str;
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			final Graphics2D g2 = (Graphics2D)g;
			if(place == null || place.length() == 0 || PlaceH.this.getText().length() > 0) {
				return;
			}
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.LIGHT_GRAY);
			g2.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 13));
			g2.drawString(place, getInsets().left, (getHeight() / 2)+5);
		}
	}
	
	static class PlaceTA extends JTextArea {
		String place;
		
		public PlaceTA() {
			setLineWrap(true);
		}
		
		void setPlace(String str) {
			place = str;
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			final Graphics2D g2 = (Graphics2D)g;
			if(place == null || place.length() == 0 || PlaceTA.this.getText().length() > 0) {
				return;
			}
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.LIGHT_GRAY);
			g2.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 13));
			g2.drawString(place, getInsets().left, 20);
		}
	}
	
	String getone(String sql) {
		try {
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				return rs.getString(1);
			}else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	JComponent emp(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
		return c;
	}
	
	JComponent line(JComponent c) {
		c.setBorder(new LineBorder(Color.BLACK));
		return c;
	}
	
	
	class be extends WindowAdapter {
		Basedialog b;
		
		public be(Basedialog bef) {
			b = bef;
			bef.setVisible(false);
		}
		
		@Override
		public void windowClosed(WindowEvent e) {
			b.setVisible(true);
		}
	}
	
	public Basedialog(String title, int w, int h) {
		setTitle(title);
		setSize(w, h);
		setModal(false);
		setIconImage(img("logo.png").getImage());
		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(2);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
	}

}
