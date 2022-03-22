package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import db.DBManager;
import view.BaseFrame;

public class Util {
	public static void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "경고", JOptionPane.ERROR_MESSAGE);
	}
	
	public static JButton btn(String cap, ActionListener a) {
		JButton b = new JButton(cap);
		b.addActionListener(a);
		return b;
	}
	
	public static <T extends JComponent> T sz(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}
	
	public static void setEmpty(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
	}
	
	public static JLabel lbl(String cap, int alig) {
		JLabel l = new JLabel(cap, alig);
		return l;
	}
	
	public static JLabel lbl(String cap, int alig, int size) {
		JLabel l = new JLabel(cap, alig);
		l.setFont(new Font("", Font.BOLD, size));
		return l;
	}
	
	public static void addRow(DefaultTableModel m, String sql, int cnt) {
		m.setRowCount(cnt);
		try {
			var rs = DBManager.rs(sql);
			while(rs.next()) {
				Object row[] = new Object[m.getColumnCount()];
				for (int i = 0; i < row.length; i++) {
					row[i] = rs.getString(i+1);
				}
				m.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isNum(Object p) {
		try {
			Integer.parseInt(p.toString());
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	public static int toInt(Object o) {
		return Integer.parseInt(o.toString());
	}
	
	public static ImageIcon img(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	public static void setLine(JComponent c) {
		c.setBorder(new LineBorder(Color.black));
	}
}
