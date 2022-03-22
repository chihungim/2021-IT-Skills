import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class BaseFrame extends JFrame {
	static Connection con;
	static Statement stmt;

	static boolean tMode = true;
	static boolean chk1, chk2;

	static int u_no;
	static String u_name, u_id, orderBy;

	static HashMap<String, String> map = new HashMap<String, String>();

	static Color btnfgColor = Color.WHITE;
	static Color btnbgColor = new Color(0, 123, 255);
	static int colWidth = 0;
	
//	static Color whiteColor = Color.WHITE;
//	static Color blackColor = Color.BLACK;
//	static Color btnColor = new Color(0, 123, 255);

	static DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

	static Emsg eMsg;
	static Imsg iMsg;

	static Vector vector = new Vector();

	static {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true&allowPublicKeyRetrieval=true",
					"root", "1234");
			stmt = con.createStatement();
			stmt.execute("use busticketbooking");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BaseFrame(String title, int w, int h) {
		super(title);
		setSize(w, h);
		setDefaultCloseOperation(2);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		map.put("부산", "busan");
		map.put("강원도", "gangwondo");
		map.put("전라남도", "Jeollanam-do");
		map.put("광주", "gyeongju");
		map.put("서울", "seoul");

		themeMode();
		//테이블 오름차순, 내림차순 아이콘 변경
		UIManager.getLookAndFeelDefaults().put("Table.ascendingSortIcon", new AscIcon());
		UIManager.getLookAndFeelDefaults().put("Table.descendingSortIcon", new DescIcon());
//		((JPanel) getContentPane()).setBackground(whiteColor);
	}

	static void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static JPanel panel() {
		JPanel p = new JPanel(null) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
//				this.setBackground(whiteColor);
			}
		};

		p.setOpaque(true);
		return p;
	}

	static JPanel bPanel(int idx) {
		JPanel p = new JPanel(new BorderLayout(idx, idx)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
//				this.setBackground(whiteColor);
			}
		};

		p.setOpaque(true);
		return p;
	}

	static JPanel gPanel(int row, int cols) {
		JPanel p = new JPanel(new GridLayout(row, cols)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
//				this.setBackground(whiteColor);
			}
		};

		p.setOpaque(true);
		return p;
	}

	static JPanel fPanel(int idx) {
		JPanel p = new JPanel(new FlowLayout(idx)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
//				this.setBackground(whiteColor);
			}
		};

		p.setOpaque(true);
		return p;
	}

	JLabel lbl(String str, int alig) {
		JLabel lbl = new JLabel(str, alig);
		lbl.setOpaque(true);
		return lbl;
	}

	static JLabel lbl(String str, int alig, int size) {
		JLabel lbl = new JLabel(str, alig);
		lbl.setFont(new Font("맑은 고딕", Font.BOLD, size));
		lbl.setOpaque(true);
		return lbl;
	}

	static JButton btn(String text, ActionListener a) {
		JButton jb = new JButton(text);
		jb.setBackground(btnbgColor);
		jb.setForeground(btnfgColor);
		jb.setOpaque(true);
		jb.addActionListener(a);
		return jb;
	}

	JButton themeButton() {
		JButton btn = new JButton("테마") {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				this.setBackground(btnbgColor);
				this.setForeground(btnfgColor);
			}
		};

		btn.setOpaque(true);
		btn.addActionListener(e -> {
			tMode = !tMode;
			themeMode();

//			((JPanel) getContentPane()).setBackground(whiteColor);
			repaint();
			revalidate();
		});

		return btn;
	}

	void themeMode() {
		if (tMode) {
			this.getContentPane().setBackground(Color.WHITE);
			UIManager.getLookAndFeelDefaults().put("Panel.background", new ColorUIResource(Color.WHITE));
			UIManager.getLookAndFeelDefaults().put("TitledBorder.titleColor", new ColorUIResource(Color.BLACK));
			UIManager.getLookAndFeelDefaults().put("Label.foreground", new ColorUIResource(Color.BLACK));
			UIManager.getLookAndFeelDefaults().put("Label.background", new ColorUIResource(Color.WHITE));
			UIManager.getLookAndFeelDefaults().put("TextField.background", new ColorUIResource(Color.WHITE));
//			whiteColor = Color.WHITE;
//			blackColor = Color.BLACK;
		} else {
			this.getContentPane().setBackground(Color.DARK_GRAY);
			UIManager.getLookAndFeelDefaults().put("Panel.background", new ColorUIResource(Color.DARK_GRAY));
			UIManager.getLookAndFeelDefaults().put("TitledBorder.titleColor", new ColorUIResource(Color.WHITE));
			UIManager.getLookAndFeelDefaults().put("Label.foreground", new ColorUIResource(Color.WHITE));
			UIManager.getLookAndFeelDefaults().put("Label.background", new ColorUIResource(Color.DARK_GRAY));
			UIManager.getLookAndFeelDefaults().put("TextField.background", new ColorUIResource(Color.DARK_GRAY));
//			whiteColor = Color.DARK_GRAY;
//			blackColor = Color.WHITE;
		}
		SwingUtilities.updateComponentTreeUI(this);
	}
	
	static int toInt(Object path) {
		return Integer.parseInt(path.toString());
	}

	static ImageIcon img(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage("지급파일/images/" + path).getScaledInstance(w, h,
				Image.SCALE_SMOOTH));
	}

	static ImageIcon imgP(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}

	static <T extends JComponent> T size(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	static DefaultTableModel model(String[] col) {
		DefaultTableModel m = new DefaultTableModel(null, col) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return true;
			}
		};
		return m;
	}

	static JTable table(DefaultTableModel m) {        
		JTable t = new JTable(m);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.getTableHeader().setReorderingAllowed(false);
		t.getTableHeader().setResizingAllowed(false);
//		t.setRowSorter(new TableRowSorter(m));
		t.setAutoCreateRowSorter(true);
		for (int i = 0; i < t.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(dtcr);
			t.getTableHeader().addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
				}
			});
		}

		//정렬하는 열 값 추출
		JTableHeader header=t.getTableHeader();

		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTableHeader h=(JTableHeader)e.getSource();
				int c=h.columnAtPoint(e.getPoint());
				colWidth = h.getColumnModel().getColumn(c).getWidth();
			}
		});
		
		return t;
	}
	
	static void addRow(DefaultTableModel m, String sql) {
		m.setRowCount(0);

		try {
			var rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Object row[] = new Object[m.getColumnCount()];
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

	static String getOne(String sql) {
		try {
			var rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			return "";
		}
		return "";
	}

	class Before extends WindowAdapter {
		BaseFrame b;

		public Before(BaseFrame b) {
			this.b = b;
			b.setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			b.setVisible(true);
		}
	}

	static class Imsg extends JDialog {
		public Imsg(String msg) {
			setTitle("안내");
			setLayout(new BorderLayout());
			setSize(350, 150);
			setDefaultCloseOperation(2);
			setLocationRelativeTo(null);
//			setBackground(whiteColor);
			Icon icon = UIManager.getIcon("OptionPane.informationIcon");

			JLabel lbl;
			add(lbl = new JLabel(msg, icon, 0));
			lbl.setOpaque(true);
//			lbl.setBackground(whiteColor);
//			lbl.setForeground(blackColor);
			JPanel p = new JPanel();

			add(p, "South");
//			p.setBackground(whiteColor);

			p.add(btn("확인", e -> {
				dispose();
			}));

			this.setIconImage(null);
			setModal(true);
			setVisible(true);
		}
	}

	static class Emsg extends JDialog {
		public Emsg(String msg) {
			setTitle("오류");
			setLayout(new BorderLayout());
			setSize(350, 150);
			setDefaultCloseOperation(2);
			setLocationRelativeTo(null);
//			setBackground(whiteColor);
			Icon icon = UIManager.getIcon("OptionPane.errorIcon");

			JLabel lbl;
			add(lbl = new JLabel(msg, icon, 0));
			lbl.setOpaque(true);
//			lbl.setBackground(whiteColor);
//			lbl.setForeground(blackColor);
			JPanel p = new JPanel();

			add(p, "South");
//			p.setBackground(whiteColor);

			p.add(btn("확인", e -> {
				dispose();
			}));

			this.setIconImage(null);
			setModal(true);
			setVisible(true);
		}
	}

	void drawLine(JComponent c) {
		c.setBorder(new LineBorder(Color.BLACK));
	}

	static void setBounds(JComponent p, JComponent c, int x, int y, int w, int h) {
		c.setBounds(x, y, w, h);
		p.add(c);
	}

	JComponent setBounds(JComponent c, int x, int y, int w, int h) {
		c.setBounds(x, y, w, h);
		add(c);
		return c;
	}

	JComponent empty(JComponent c, int top, int left, int bottom, int right) {
		c.setBorder(new EmptyBorder(top, left, bottom, right));
		return c;
	}

	static JTextField txt(JTextField txt, final String str) {
		txt.setText(str);
//		txt.setBackground(whiteColor);
		txt.setForeground(Color.LIGHT_GRAY);
		txt.setOpaque(false);
//		txt.setBorder(new MatteBorder(0, 0, 1, 0, blackColor));
		txt.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

		txt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '\b' && txt.getText().equals(str)) {
					e.consume();
				}

				if (txt.getText().contentEquals(str)) {
					txt.setText("");
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (txt.getText().equals(str) || txt.getText().length() == 0) {
					chk1 = true;
				}

				if (chk1) {
					txt.setText("");
					chk1 = false;
//					txt.setForeground(blackColor);
				} else if (txt.getText().isBlank()) {
					chk1 = false;
				}
			}
		});

		txt.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (txt.getText().equals(str) || txt.getText().length() == 0) {
					txt.setText(str);
					txt.setForeground(Color.LIGHT_GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (txt.getText().equals(str)) {

				} else {
					txt.setText(txt.getText());
				}
			}
		});
		return txt;
	}

	JPasswordField txt(JPasswordField txt, final String str) {
		txt.setText(str);
//		txt.setBackground(whiteColor);
		txt.setForeground(Color.LIGHT_GRAY);
		txt.setOpaque(false);
		txt.setEchoChar((char) 0);
//		txt.setBorder(new MatteBorder(0, 0, 1, 0, blackColor));
		txt.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

		txt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '\b' && txt.getText().equals(str)) {
					e.consume();
				}

				if (txt.getText().contentEquals(str)) {
					txt.setText("");
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (txt.getText().equals(str) || txt.getText().length() == 0) {
					chk1 = true;
				}

				if (chk1) {
					txt.setText("");
					chk1 = false;
//					txt.setForeground(blackColor);
				} else if (txt.getText().isBlank()) {
					chk1 = false;
				}
			}
		});

		txt.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (txt.getText().equals(str) || txt.getText().length() == 0) {
					txt.setText(str);
//					txt.setForeground(Color.LIGHT_GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (txt.getText().equals(str)) {

				} else {
					txt.setText(txt.getText());
				}
			}
		});
		return txt;
	}
	
	static class AscIcon implements Icon {
	    public void paintIcon(Component c, Graphics g, int x, int y) {
	    	Graphics2D g2d = (Graphics2D)g.create();
	    	g2d.drawString("↑", colWidth-20, 15);
	    }
	    public int getIconWidth() { return 0; }
	    public int getIconHeight() { return 0; }
	}
	
	static class DescIcon implements Icon {
	    public void paintIcon(Component c, Graphics g, int x, int y) {
	    	Graphics2D g2d = (Graphics2D)g.create();
	    	g2d.drawString("↓", colWidth-20, 15);
	    }
	    public int getIconWidth() { return 0; }
	    public int getIconHeight() { return 0; }
	}
}
