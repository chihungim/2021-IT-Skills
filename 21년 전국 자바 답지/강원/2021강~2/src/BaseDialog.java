import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BaseDialog extends JFrame {
	static Connection con;
	static Statement stmt;

	static boolean tMode = true;
	static boolean chk1, chk2;

	HashMap<String, String> map = new HashMap<String, String>();

	static Color whiteColor = Color.WHITE;
	static Color blackColor = Color.BLACK;
	static Color btnColor = new Color(0, 123, 255);

	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

	static Emsg eMsg;
	static Imsg iMsg;

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

	public BaseDialog(String title, int w, int h) {
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

		((JPanel) getContentPane()).setBackground(whiteColor);
	}

	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	JPanel panel() {
		JPanel p = new JPanel(null) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				this.setBackground(whiteColor);
			}
		};

		p.setOpaque(true);
		return p;
	}

	JLabel lbl(String str, int alig) {
		JLabel lbl = new JLabel(str, alig) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				this.setBackground(whiteColor);
				this.setForeground(blackColor);
			}
		};
		lbl.setOpaque(true);
		return lbl;
	}

	JLabel lbl(String str, int alig, int size) {
		JLabel lbl = new JLabel(str, alig) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				this.setBackground(whiteColor);
				this.setForeground(blackColor);
			}
		};
		lbl.setFont(new Font("맑은 고딕", Font.BOLD, size));
		lbl.setOpaque(true);
		return lbl;
	}

	JButton btn(String text, ActionListener a) {
		JButton jb = new JButton(text);
		jb.setBackground(btnColor);
		jb.setForeground(whiteColor);
		jb.setOpaque(true);
		jb.addActionListener(a);
		return jb;
	}

	JButton themeButton() {
		JButton btn = new JButton("테마") {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				this.setBackground(blackColor);
				this.setForeground(whiteColor);
			}
		};

		btn.setOpaque(true);
		btn.addActionListener(e -> {
			tMode = !tMode;

			if (tMode) {
				whiteColor = Color.WHITE;
				blackColor = Color.BLACK;
			} else {
				whiteColor = Color.DARK_GRAY;
				blackColor = Color.WHITE;
			}

			((JPanel) getContentPane()).setBackground(whiteColor);
			repaint();
			revalidate();
		});

		return btn;
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

	DefaultTableModel model(String[] col) {
		DefaultTableModel m = new DefaultTableModel(null, col);
		return m;
	}

	JTable table(DefaultTableModel m) {
		JTable t = new JTable(m);
		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.getTableHeader().setReorderingAllowed(false);
		t.getTableHeader().setResizingAllowed(false);
		for (int i = 0; i < t.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		return t;
	}

	void addRow(DefaultTableModel m, String sql) {
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

	String getOne(String sql) {
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

	class Imsg extends JDialog {
		public Imsg(String msg) {
			setTitle("안내");
			setLayout(new BorderLayout());
			setSize(350, 150);
			setDefaultCloseOperation(2);
			setLocationRelativeTo(null);
			setBackground(whiteColor);
			Icon icon = UIManager.getIcon("OptionPane.informationIcon");

			JLabel lbl;
			add(lbl = new JLabel(msg, icon, 0));
			lbl.setOpaque(true);
			lbl.setBackground(whiteColor);
			lbl.setForeground(blackColor);
			JPanel p = new JPanel();

			add(p, "South");
			p.setBackground(whiteColor);

			p.add(btn("확인", e -> {
				dispose();
			}));

			this.setIconImage(null);
			setModal(true);
			setVisible(true);
		}
	}

	class Emsg extends JDialog {
		public Emsg(String msg) {
			setTitle("오류");
			setLayout(new BorderLayout());
			setSize(350, 150);
			setDefaultCloseOperation(2);
			setLocationRelativeTo(null);
			setBackground(whiteColor);
			Icon icon = UIManager.getIcon("OptionPane.errorIcon");

			JLabel lbl;
			add(lbl = new JLabel(msg, icon, 0));
			lbl.setOpaque(true);
			lbl.setBackground(whiteColor);
			lbl.setForeground(blackColor);
			JPanel p = new JPanel();

			add(p, "South");
			p.setBackground(whiteColor);

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

	JComponent setBounds(JComponent c, int x, int y, int w, int h) {
		c.setBounds(x, y, w, h);
		add(c);
		return c;
	}

	JComponent empty(JComponent c, int top, int left, int bottom, int right) {
		c.setBorder(new EmptyBorder(top, left, bottom, right));
		return c;
	}

	JTextField txt(JTextField txt, final String str) {
		txt.setText(str);
		txt.setBackground(whiteColor);
		txt.setForeground(Color.LIGHT_GRAY);
		txt.setOpaque(false);
		txt.setBorder(new MatteBorder(0, 0, 1, 0, blackColor));

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
					txt.setForeground(blackColor);
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
		txt.setBackground(whiteColor);
		txt.setForeground(Color.LIGHT_GRAY);
		txt.setOpaque(false);
		txt.setEchoChar((char) 0);
		txt.setBorder(new MatteBorder(0, 0, 1, 0, blackColor));

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
					txt.setForeground(blackColor);
				} else if (txt.getText().isBlank()) {
					chk1 = false;
				}
			}
		});

		txt.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (txt.getText().equals(str) || txt.getText().length() == 0) {
					txt.setEchoChar((char) 0);
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

}
