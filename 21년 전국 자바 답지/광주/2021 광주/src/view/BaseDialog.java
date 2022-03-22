package view;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BaseDialog extends JDialog {
	static Connection con = db.DB.con;
	static Statement stmt = db.DB.stmt;

	static int uno, sno, rno;
	static String uname, rname, sname;
	static ArrayList<ArrayList<Object>> orderList = new ArrayList<>();
	static HashMap<String, Integer> optionMap = new HashMap<>();
	static HashMap<String, Integer> menuMap = new HashMap<>();

	static DecimalFormat df = new DecimalFormat("#,##0");
	static {
		try {
			stmt.execute("use eats");
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

	public BaseDialog(String title, int w, int h) {
		super((Dialog) null);
		setTitle(title);
		setSize(w, h);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./�����ڷ�/logo.png"));
	}

	static JButton btn(String t, ActionListener a) {
		var bt = new JButton(t);
		bt.setBackground(Color.GREEN.darker().darker());
		bt.setForeground(Color.WHITE);
		bt.addActionListener(a);
		return bt;
	}

	static JLabel lbl(String title, int alig) {
		return new JLabel(title, alig);
	}

	static JLabel lbl(String title, int alig, int size) {
		JLabel l = new JLabel(title, alig);
		l.setFont(new Font("���� ����", Font.BOLD, size));
		return l;
	}

	static DefaultTableModel model(String col[]) {
		var m = new DefaultTableModel(null, col) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return m;
	}

	int toInt(Object obj) {
		if (obj.toString().equals(""))
			return 0;
		return Integer.parseInt(obj.toString());
	}

	JComponent setBorder(JComponent jc, Border b) {
		jc.setBorder(b);
		return jc;
	}

	public static JComponent setAlig(JComponent jc, float alig) {
		jc.setAlignmentX(alig);
		return jc;
	}

	static ImageIcon getIcon(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

	static ImageIcon getIcon(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path));
	}

	static JTable table(DefaultTableModel m, int alig) {
		JTable t = new JTable(m);
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(alig);
		t.getTableHeader().setResizingAllowed(false);
		t.getTableHeader().setReorderingAllowed(false);

		for (int i = 0; i < t.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		return t;
	}

	JComponent size(JComponent jc, int w, int h) {
		jc.setPreferredSize(new Dimension(w, h));
		return jc;
	}

	class PlaceHolderField extends JTextField {

		String placeHolder;

		public PlaceHolderField(int col) {
			super(col);
		}

		public PlaceHolderField(String placeHolder, int col) {
			super(col);
			this.placeHolder = placeHolder;
		}

		public PlaceHolderField(String placeHolder) {
			this.placeHolder = placeHolder;
		}

		void setPlaceHolder(String placeHolder) {
			this.placeHolder = placeHolder;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			final Graphics2D g2 = (Graphics2D) g;
			if (placeHolder == null || placeHolder.length() == 0 || PlaceHolderField.this.getText().length() > 0)
				return;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.LIGHT_GRAY);
			g2.setFont(new Font("���� ����", Font.BOLD, (int) (getHeight() * 0.5)));
			g2.drawString(placeHolder, getInsets().left, g2.getFontMetrics().getMaxAscent() + getInsets().bottom);
		}
	}

	JButton themeButton() {
		JButton btn = new JButton("�׸�") {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				this.setBackground(Color.LIGHT_GRAY.darker());
				this.setForeground(Color.WHITE);
			}
		};

		btn.setOpaque(true);
		btn.addActionListener(e -> {
			tMode = !tMode;
			themeMode();

			repaint();
			revalidate();
		});

		return btn;
	}

	class Before extends WindowAdapter {

		BaseDialog b;

		public Before(BaseDialog b) {
			this.b = b;
			b.setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			b.setVisible(true);
			super.windowClosed(e);
		}
	}

	class JPanel extends javax.swing.JPanel {
		public JPanel() {
			super(new FlowLayout(FlowLayout.CENTER, 5, 5));
			setOpaque(false);
		}

		public JPanel(LayoutManager lm) {
			super(lm);
			setOpaque(false);
		}
	}

	static void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "�޽���", JOptionPane.INFORMATION_MESSAGE);
	}

	static void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "�޽���", JOptionPane.ERROR_MESSAGE);
	}

	static String getOne(String sql) throws SQLException {
		var rs = stmt.executeQuery(sql);
		if (rs.next()) {
			return rs.getString(1);
		} else {
			return "";
		}
	}

}
