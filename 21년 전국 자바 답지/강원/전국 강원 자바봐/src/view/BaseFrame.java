package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BaseFrame extends JFrame {
	static Connection con;
	static Statement stmt;

	static Color blackColor = Color.BLACK;
	static Color whiteColor = Color.WHITE;
	static boolean chk1;
	static boolean chk2;
	static int u_no;
	static String u_name, u_id;

	static eMsg emsg;
	static iMsg imsg;

	static int sx = 0;
	static int sy = 0;
	static int ex = 0;
	static int ey = 0;
	static int tmpex = 0;
	static int tmpey = 0;
	static ArrayList<String> posList = new ArrayList<String>();
	public static Vector<Point> sv = new Vector<Point>();
	public static Vector<Point> ev = new Vector<Point>();

	static DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

	static HashMap<String, String> hashMap = new HashMap<String, String>();

	static {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost?allowLoadLocalInfile=true&serverTimezone=UTC&allowPublicKeyRetrieval=true",
					"root", "1234");
			stmt = con.createStatement();
			stmt.execute("use busticketbooking");
			dtcr.setHorizontalAlignment(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static String getOne(String sql) {
		try {
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			return "";
		}
		return "";
	}

	public BaseFrame(String title, int w, int h) {
		setTitle(title);
		setSize(w, h);
		setDefaultCloseOperation(2);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		hashMap.put("부산", "busan");
		hashMap.put("강원도", "gangwondo");
		hashMap.put("전라남도", "Jeollanam-do");
		hashMap.put("광주", "gyeongju");
		hashMap.put("서울", "seoul");
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("ㅇㅇ");
				if (e.getKeyChar() == 'Z' || e.getKeyChar() == 'z') {
					sv.remove(sv.size() - 1);
					ev.remove(ev.size() - 1);

					posList.remove(posList.size() - 1);
					System.out.println("좌표목록:" + String.join(",", posList));

					sx = sy = tmpex = tmpey = 0;
					repaint();
				}
			}
		});
		((JPanel) getContentPane()).setBackground(whiteColor);
	}

	static DefaultTableModel model(String[] col) {
		DefaultTableModel m = new DefaultTableModel(null, col);
		return m;
	}
	
	static DefaultTableModel nmodel(String[] col) {
		DefaultTableModel m = new DefaultTableModel(null, col) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return m;
	}

	static JLabel imageLabel(String path, int w, int h) {
		return new JLabel(
				new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH)));
	}

	static JLabel imageLabel(String path) {
		return new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(path)));
	}

	static JTable table(DefaultTableModel m) {
		JTable jt = new JTable(m);
		for (int i = 0; i < m.getColumnCount(); i++) {
			jt.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		return jt;
	}

	static void addRow(DefaultTableModel dtm, String sql) {
		dtm.setRowCount(0);

		try {
			var rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Object row[] = new Object[dtm.getColumnCount()];
				for (int i = 0; i < row.length; i++) {
					row[i] = rs.getString(i + 1);
				}
				dtm.addRow(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	JComponent size(JComponent jc, int w, int h) {
		jc.setPreferredSize(new Dimension(w, h));
		return jc;
	}

	JComponent setBounds(JComponent c, int x, int y, int w, int h) {
		c.setBounds(x, y, w, h);
		add(c);
		return c;
	}

	static void setBounds(JComponent p, JComponent c, int x, int y, int w, int h) {
		c.setBounds(x, y, w, h);
		p.add(c);
	}

	static void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static JButton btn(String text, ActionListener a) {
		JButton btn = new JButton(text);
		btn.addActionListener(a);
		btn.setBackground(new Color(0, 123, 255));
		btn.setForeground(whiteColor);
		return btn;
	}

	static JLabel lbl(String text, int alig) {
		JLabel lbl = new JLabel(text, alig);
		lbl.setBackground(whiteColor);
		lbl.setForeground(blackColor);
		lbl.setOpaque(false);
		return lbl;
	}

	static JLabel lbl(String text, int alig, int size) {
		JLabel lbl = new JLabel(text, alig);
		lbl.setFont(new Font("맑은 고딕", Font.BOLD, size));
		lbl.setBackground(whiteColor);
		lbl.setForeground(blackColor);
		lbl.setOpaque(false);
		return lbl;
	}

	JButton themeButton() {
		JButton btn = new JButton("테마");
		btn.setOpaque(true);
		btn.setBackground((whiteColor.equals(Color.WHITE)) ? Color.DARK_GRAY : Color.WHITE);
		btn.setForeground((whiteColor.equals(Color.WHITE)) ? Color.WHITE : Color.BLACK);
		btn.addActionListener(e -> {
			whiteColor = (whiteColor.equals(Color.WHITE)) ? Color.DARK_GRAY : Color.WHITE;
			blackColor = (whiteColor.equals(Color.WHITE)) ? Color.WHITE : Color.BLACK;
			BaseDialog.whiteColor = (whiteColor.equals(Color.WHITE)) ? Color.WHITE : Color.DARK_GRAY;
			BaseDialog.blackColor = (whiteColor.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE;
			((JPanel) getContentPane()).setBackground(whiteColor);
			repaint();
			revalidate();
		});

		return btn;
	}

	static int toInt(Object path) {
		return Integer.parseInt(path.toString());
	}

	static JTextField HintText(final String txt, JTextField t) {
		t.setText(txt);
		t.setFont(new Font("맑은 고딕", Font.BOLD, 11));
		t.setForeground(Color.LIGHT_GRAY);
		t.setBackground(Color.WHITE);
		t.setBorder(new MatteBorder(0, 0, 1, 0, blackColor));
		t.setOpaque(false);

		t.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (t.getText().equals(txt)) {
					return;
				} else {
					t.setText(t.getText());
				}

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (t.getText().equals(txt) || t.getText().length() == 0) {
					t.setText(txt);
					t.setForeground(Color.LIGHT_GRAY);
				}
			}
		});

		t.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '\b' && t.getText().equals(txt)) {
					e.consume();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (t.getText().equals(txt) || t.getText().length() == 0)
					chk1 = true;

				if (chk1) {
					t.setText("");
					chk1 = false;
					t.setForeground(blackColor);
				} else if (t.getText().isEmpty())
					chk1 = false;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (t.getText().isEmpty()) {
					t.setText(txt);
					t.setForeground(Color.LIGHT_GRAY);
				}
			}
		});
		return t;
	}

	JPasswordField HintText(final String txt) {
		JPasswordField t = new JPasswordField();
		t.setText(txt);
		t.setFont(new Font("맑은 고딕", Font.BOLD, 11));
		t.setForeground(Color.LIGHT_GRAY);
		t.setBackground(Color.WHITE);
		t.setEchoChar((char) 0);
		t.setBorder(new MatteBorder(0, 0, 1, 0, blackColor));
		t.setOpaque(true);

		t.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (t.getText().equals(txt)) {
					return;
				} else {
					t.setText(t.getText());
				}

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (t.getText().equals(txt) || t.getText().length() == 0) {
					t.setText(txt);
					t.setForeground(Color.LIGHT_GRAY);
				}
			}
		});

		t.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '\b' && t.getText().equals(txt)) {
					e.consume();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (t.getText().equals(txt) || t.getText().length() == 0)
					chk2 = true;

				if (chk2) {
					t.setText("");
					chk2 = false;
					t.setEchoChar('*');
					t.setForeground(Color.BLACK);
				} else if (t.getText().isEmpty())
					chk2 = false;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (t.getText().isEmpty()) {
					t.setText(txt);
					t.setForeground(Color.LIGHT_GRAY);
					t.setEchoChar((char) 0);
				}
			}
		});
		return t;
	}

	static class iMsg extends JDialog {
		String msg;

		public iMsg(String msg) {
			setTitle("안내");
			setSize(350, 150);
			setDefaultCloseOperation(2);
			setLocationRelativeTo(null);

			this.msg = msg;

			UI();
			setVisible(true);
		}

		void UI() {
			setLayout(new BorderLayout());
			setBackground(whiteColor);

			Icon icon = UIManager.getIcon("OptionPane.informationIcon");
			JLabel lbl;
			var s = new JPanel();

			add(s, "South");
			add(lbl = new JLabel(msg, icon, 0));

			s.setBackground(whiteColor);
			lbl.setOpaque(true);
			lbl.setBackground(whiteColor);
			lbl.setForeground(blackColor);

			s.add(btn("확인", e -> {
				dispose();
			}));

			this.setIconImage(null);
			setModal(true);
		}
	}

	static class eMsg extends JDialog {
		String msg;

		public eMsg(String msg) {
			setTitle("오류");
			setSize(350, 150);
			setDefaultCloseOperation(2);
			setLocationRelativeTo(null);

			this.msg = msg;

			UI();
			setVisible(true);
		}

		void UI() {
			setLayout(new BorderLayout());
			setBackground(whiteColor);

			Icon icon = UIManager.getIcon("OptionPane.errorIcon");
			JLabel lbl;
			var s = new JPanel();

			add(s, "South");
			add(lbl = new JLabel(msg, icon, 0));

			lbl.setOpaque(true);
			s.setBackground(whiteColor);
			lbl.setBackground(whiteColor);
			lbl.setForeground(blackColor);

			s.add(btn("확인", e -> {
				dispose();
			}));

			this.setIconImage(null);
			setModal(true);
		}
	}

	public static class MyPanel extends JPanel {

		public MyPanel() {
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					sx = e.getX();
					sy = e.getY();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					ex = e.getX();
					ey = e.getY();

					// 현재 박스 좌표 기록, 전체 박스 좌표 출력
					posList.add("{" + sx + "," + sy + "," + (ex - sx) + "," + (ey - sy) + "}");
					System.out.println("좌표목록:" + String.join(",", posList));

					sv.add(new Point(sx, sy));
					ev.add(new Point(ex, ey));
					repaint();
				}
			});

			this.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					tmpex = e.getX();
					tmpey = e.getY();
					repaint();
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.BLACK);

			Point cs, ce;
			// 기록된 박스들 모두 표시
			for (int i = 0; i < sv.size(); i++) {
				cs = sv.get(i);
				ce = ev.get(i);
				g.drawRect(cs.x, cs.y, ce.x - cs.x, ce.y - cs.y);
			}

			// 드래그 자취 표시
			if (sx > 0 || sy > 0 || tmpex > 0 || tmpey > 0)
				g.drawRect(sx, sy, tmpex - sx, tmpey - sy);
		}
	}

	class before extends WindowAdapter {
		BaseFrame b;

		public before(BaseFrame b) {
			this.b = b;
			b.setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			b.setVisible(true);
		}
	}
}
