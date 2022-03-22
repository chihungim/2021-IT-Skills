package ±¤±¤ÁÖ;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import ±¤±¤ÁÖ.BaseFrame.Before;

public class BaseFrame extends JFrame {
	static Connection con = DB.con;
	static Statement stmt = DB.stmt;
	CardLayout pages;
	JPanel masterP;
	static boolean isPurchaseOver = false;
	static ArrayList<ArrayList<Object>> orderList = new ArrayList<>();
	static DecimalFormat df = new DecimalFormat("#,##0");
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

	static {
		try {
			stmt.execute("use eats");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static int uno = 0, rno = 0, sno = 0, mno = 0; 
	static String uname, rname, sname;
	static Color btnColor = new Color(52, 146, 75);

	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BaseFrame(String t, int w, int h) {
		super(t);
		setSize(w, h);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./Áö±ÞÀÚ·á/logo.png"));
		((JPanel) getContentPane()).setBackground(Color.WHITE);
		setBackground(Color.WHITE);

		setLocationRelativeTo(null);
	}

	JButton btn(String cap, ActionListener a) {
		JButton b = new JButton(cap);
		b.setBackground(btnColor);
		b.setForeground(Color.WHITE);
		b.addActionListener(a);
		return b;
	}

	static JLabel lbl(String l, int a) {
		JLabel lb = new JLabel(l, a);
		lb.setFont(new Font("¸¼Àº°íµñ", 0, 15));
		return lb;
	}

	int getLastRef(String sql) {
		try {
			var rs = stmt.executeQuery(sql + " order by no desc");
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return 0;
			}
		} catch (SQLException e) {
			return 0;
		}
	}

	static int toInt(Object path) {
		return Integer.parseInt(path.toString());
	}

	static JLabel lbl(String l, int a, int s) {
		JLabel lb = new JLabel(l, a);
		lb.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, s));
		return lb;
	}

	static void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "¸Þ½ÃÁö", JOptionPane.ERROR_MESSAGE);
	}

	static void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "¸Þ½ÃÁö", JOptionPane.INFORMATION_MESSAGE);
	}

	Image getImage(String path, int w, int h) {
		return Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH);
	}

	Image getImage(String path) {
		return Toolkit.getDefaultToolkit().getImage(path);
	}

	JTable table(DefaultTableModel m) {
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		JTable t = new JTable(m);
		t.getTableHeader().setReorderingAllowed(false);
		t.getTableHeader().setResizingAllowed(false);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		for (int i = 0; i < t.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		return t;
	}

	DefaultTableModel model(String col[]) {
		DefaultTableModel m = new DefaultTableModel(null, col) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return m;
	}

	static JComponent size(JComponent jc, int w, int h) {
		jc.setPreferredSize(new Dimension(w, h));
		return jc;
	}

	void addrow(String sql, DefaultTableModel m) {
		m.setRowCount(0);
		try {
			var rs = stmt.executeQuery(sql);
			Object row[] = new Object[m.getColumnCount()];
			while (rs.next()) {
				for (int i = 0; i < row.length; i++) {
					row[i] = rs.getString(i + 1);
				}
				m.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			if (e.getSource() instanceof Purchase && isPurchaseOver) {
				Login l = new Login();
				l.setVisible(false);
				new Main().addWindowListener(new Before(l));
			} else {
				b.setVisible(true);
			}
		}
	}

	class PlaceHolderTextField extends JTextField {
		String placeHolder;

		public PlaceHolderTextField(int col) {
			super(col);
		}

		void setPlaceHolder(String placeHolder) {
			this.placeHolder = placeHolder;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			final Graphics2D g2 = (Graphics2D) g;
			if (placeHolder == null || placeHolder.length() == 0 || PlaceHolderTextField.this.getText().length() > 0)
				return;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.LIGHT_GRAY);
			g2.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, (int) (getHeight() * 0.5)));
			g2.drawString(placeHolder, getInsets().left, g2.getFontMetrics().getMaxAscent() + getInsets().bottom);
		}

	}
}
