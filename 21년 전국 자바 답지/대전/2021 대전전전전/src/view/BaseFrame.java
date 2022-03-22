package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import db.DBManager;

public class BaseFrame extends JFrame {
	
	JPanel n, c, w, s, e;
	static String uno="", uname, pdate;
	static String pinfo[] = new String[7];
	static Main main = new Main();
	
	public BaseFrame(String tit, int w, int h) {
		super(tit);
		this.setSize(w, h);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);
	}
	
	int rei(Object obj) {
		if (obj.toString().equals("")) return 0;
		return Integer.parseInt(obj.toString());
	}
	
	void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "°æ°í", JOptionPane.ERROR_MESSAGE);
	}
	
	void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Á¤º¸", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	void setLine(JComponent c, Color col) {
		c.setBorder(new LineBorder(col));
	}
	
	void setEmpty(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
	}
	
	<T extends JComponent> T sz(T c, int w, int h){
		c.setPreferredSize(new Dimension(w, h));
		return c;
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
	
	void addRow(DefaultTableModel m, String sql) {
		m.setRowCount(0);
		try {
			var rs = DBManager.rs(sql);
			while (rs.next()) {
				Object row[] = new Object[m.getColumnCount()];
				for (int i = 0; i < row.length; i++) {
					row[i] = rs.getString(i+1);
				}
				m.addRow(row);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	JLabel lbl(String text, int alig) {
		JLabel l = new JLabel(text, alig);
		return l;
	}
	
	JLabel lbl(String text, int alig, int size) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, size));
		return l;
	}
	
	JLabel lbl(String text, int alig, int size, MouseAdapter a) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, size));
		l.addMouseListener(a);
		return l;
	}
	
	JLabel lbl(String text, int alig, int size, String font, MouseAdapter a) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font(font, Font.BOLD, size));
		l.addMouseListener(a);
		return l;
	}
	
	JLabel lblP(String text, int alig, int size) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, size));
		return l;
	}
	
	JButton btn(String text, ActionListener a) {
		JButton b = new JButton(text);
		b.addActionListener(a);
		return b;
	}
	
	ImageIcon img(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	class Before extends WindowAdapter {
		BaseFrame b;
		public Before(BaseFrame b) {
			this.b=b;
			b.setVisible(false);
		}
		
		@Override
		public void windowClosed(WindowEvent e) {
			b.setVisible(true);
		}
	}
	
	ImageIcon getDBImg(String sql) {
		return null;
	}
	
}
