package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class BaseFrame extends JFrame {

	static Connection con = db.DB.con;
	static Statement stmt = db.DB.stmt;
	static boolean tMode = true;
	static int uno;
	static int colWidth = 0;
	static String uname, pw;
	static HashMap<String, String> ll2map = new HashMap<String, String>();
	static {
		try {
			stmt.execute("use busticketbooking");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static {
		try {
			var rs = stmt.executeQuery("select * from ll2");
			while (rs.next()) {
				ll2map.put(rs.getString(2), rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static HashMap<String, String> map = new HashMap<String, String>();
	static {
		map.put("ºÎ»ê", "busan");
		map.put("°­¿øµµ", "gangwondo");
		map.put("Àü¶ó³²µµ", "Jeollanam-do");
		map.put("±¤ÁÖ", "gyeongju");
		map.put("¼­¿ï", "seoul");
	}

	public BaseFrame(String title, int w, int h) {
		super(title);

		setSize(w, h);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		themeMode();
	}

	static void setLogin(String no) {
		try {
			var rs = stmt.executeQuery("select * from user where no = '" + no + "'");
			if (rs.next()) {
				uno = rs.getInt(1);
				uname = rs.getString("name");
				pw = rs.getString("pwd");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static int toInt(Object p) {
		return Integer.parseInt(p.toString());
	}

	static class Popup extends JMenuItem {

		HashMap<String, ArrayList<String>> map = new HashMap<>();
		boolean isClicked = false;

		public Popup(JTextField txt) {
			data();
			setPreferredSize(new Dimension(200, 300));
			setLayout(new GridLayout(1, 0));
			final var left = new JPanel(new GridLayout(0, 1));
			final var right = new JPanel(new GridLayout(0, 1));
			add(new JScrollPane(left));
			add(new JScrollPane(right));
			for (var loc : map.keySet()) {
				left.add(btn(loc, a -> {
					right.removeAll();
					right.setLayout(new GridLayout(0, 1));
					map.get(a.getActionCommand()).forEach(c -> {
						right.add(btn(c, e -> {
							txt.setText(a.getActionCommand() + " " + e.getActionCommand());
							isClicked = true;
						}));
					});
					right.setVisible(true);
					revalidate();
					repaint();
				}));
			}
			right.setVisible(false);
		}

		void data() {
			try {
				var rs = stmt.executeQuery(
						"select l.name, l2.name from location l, location2 l2 where l2.location_no = l.no");
				while (rs.next()) {
					if (map.containsKey(rs.getString(1))) {
						map.get(rs.getString(1)).add(rs.getString(2));
					} else {
						var list = new ArrayList<String>();
						map.put(rs.getString(1), list);
						list.add(rs.getString(2));
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

	static JTable table(DefaultTableModel m, int alig) {
		JTable t = new JTable(m);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.getTableHeader().setReorderingAllowed(false);
		t.getTableHeader().setResizingAllowed(false);
		t.setAutoCreateRowSorter(true);
		var dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(alig);
		for (int i = 0; i < t.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		JTableHeader header = t.getTableHeader();

		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTableHeader h = (JTableHeader) e.getSource();
				int c = h.columnAtPoint(e.getPoint());
				colWidth = h.getColumnModel().getColumn(c).getWidth();
			}
		});

		return t;
	}

	static void addRow(String sql, DefaultTableModel m) {
		try {
			var rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Object[] row = new Object[m.getColumnCount()];
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

	static JLabel lbl(String text, int alig) {
		return new JLabel(text, alig);
	}

	static JLabel lbl(String text, int alig, int size) {
		var l = new JLabel(text, alig);
		l.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, size));
		return l;
	}

	static void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static JButton btn(String t, ActionListener a) {
		var b = new JButton(t);
		b.addActionListener(a);
		b.setForeground(Color.WHITE);
		b.setBackground(new Color(0, 123, 255));
		return b;
	}

	static <T extends JComponent> T sz(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	ImageIcon getIcon(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

	ImageIcon getIcon(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path));
	}

	JComponent getPos(JComponent jc) {
		jc.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println(e.getX() + "," + e.getY());
				super.mousePressed(e);
			}
		});

		return jc;
	}

	static void eMsg(String msg) {
		JLabel lbl = new JLabel(msg, UIManager.getIcon("OptionPane.errorIcon"), JLabel.CENTER);
		JOptionPane.showMessageDialog(null, lbl, "¿À·ù", JOptionPane.DEFAULT_OPTION);
	}

	static void iMsg(String msg) {
		JLabel lbl = new JLabel(msg, UIManager.getIcon("OptionPane.informationIcon"), JLabel.CENTER);
		JOptionPane.showMessageDialog(null, lbl, "¾È³»", JOptionPane.DEFAULT_OPTION);
	}

	static class AscIcon implements Icon {
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.drawString("¡è", colWidth - 20, 15);
		}

		public int getIconWidth() {
			return 0;
		}

		public int getIconHeight() {
			return 0;
		}
	}

	static class DescIcon implements Icon {
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.drawString("¡é", colWidth - 20, 15);
		}

		public int getIconWidth() {
			return 0;
		}

		public int getIconHeight() {
			return 0;
		}
	}

	void themeMode() {
		UIManager.getLookAndFeelDefaults().put("Table.ascendingSortIcon", new AscIcon());
		UIManager.getLookAndFeelDefaults().put("Table.descendingSortIcon", new DescIcon());
		UIManager.put("Button.background", new Color(0, 123, 255));
		UIManager.put("Button.foreground", Color.WHITE);
		UIManager.getLookAndFeelDefaults().put("OptionPane.okButtonText", "È®ÀÎ");
		UIManager.getLookAndFeelDefaults().put("OptionPane.cancelButtonText", "Ãë¼Ò");
		if (tMode) {
			this.getContentPane().setBackground(Color.WHITE);
			UIManager.getLookAndFeelDefaults().put("OptionPane.background", new ColorUIResource(Color.WHITE));
			UIManager.getLookAndFeelDefaults().put("Panel.background", new ColorUIResource(Color.WHITE));
			UIManager.getLookAndFeelDefaults().put("TitledBorder.titleColor", new ColorUIResource(Color.BLACK));
			UIManager.getLookAndFeelDefaults().put("Label.foreground", new ColorUIResource(Color.BLACK));
			UIManager.getLookAndFeelDefaults().put("Label.background", new ColorUIResource(Color.WHITE));
			UIManager.getLookAndFeelDefaults().put("TextField.background", new ColorUIResource(Color.WHITE));
			UIManager.getLookAndFeelDefaults().put("TextField.foreground", new ColorUIResource(Color.BLACK));
			UIManager.getLookAndFeelDefaults().put("PasswordField.background", new ColorUIResource(Color.WHITE));
		} else {
			this.getContentPane().setBackground(Color.DARK_GRAY);
			UIManager.getLookAndFeelDefaults().put("OptionPane.background", new ColorUIResource(Color.DARK_GRAY));
			UIManager.getLookAndFeelDefaults().put("Panel.background", new ColorUIResource(Color.DARK_GRAY));
			UIManager.getLookAndFeelDefaults().put("TitledBorder.titleColor", new ColorUIResource(Color.WHITE));
			UIManager.getLookAndFeelDefaults().put("Label.foreground", new ColorUIResource(Color.WHITE));
			UIManager.getLookAndFeelDefaults().put("Label.background", new ColorUIResource(Color.DARK_GRAY));
			UIManager.getLookAndFeelDefaults().put("TextField.background", new ColorUIResource(Color.DARK_GRAY));
			UIManager.getLookAndFeelDefaults().put("TextField.foreground", new ColorUIResource(Color.white));
			UIManager.getLookAndFeelDefaults().put("PasswordField.background", new ColorUIResource(Color.darkGray));
		}
		SwingUtilities.updateComponentTreeUI(this);
	}

	JButton themeButton() {
		JButton btn = new JButton("Å×¸¶") {
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

	static String getOne(String sql) {
		try {
			var rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(1);
			} else
				return "";
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	class Before extends WindowAdapter {
		BaseFrame b;

		public Before(BaseFrame b) {
			this.b = b;
		}

		@Override
		public void windowClosed(WindowEvent e) {
			b.setVisible(true);
		}
	}

	static class TextHolder extends JTextField {
		String holder;

		public TextHolder(String holder, int col) {
			super(col);
			this.holder = holder;
			setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			final Graphics2D g2 = (Graphics2D) g;
			if (holder == null || holder.length() == 0 || super.getText().length() > 0)
				return;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.LIGHT_GRAY);
			g2.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, (int) (getHeight() * 0.5)));
			g2.drawString(holder, getInsets().left, g2.getFontMetrics().getMaxAscent() + getInsets().bottom);
		}
	}

	class TextHolderPW extends JPasswordField {
		String holder;

		public TextHolderPW(String holder, char ch, int col) {
			super(col);
			super.setEchoChar(ch);
			this.holder = holder;
			setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			final Graphics2D g2 = (Graphics2D) g;
			if (holder == null || holder.length() == 0 || super.getPassword().length > 0)
				return;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.LIGHT_GRAY);
			g2.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, (int) (getHeight() * 0.5)));
			g2.drawString(holder, getInsets().left, g2.getFontMetrics().getMaxAscent() + getInsets().bottom);
		}
	}

}
