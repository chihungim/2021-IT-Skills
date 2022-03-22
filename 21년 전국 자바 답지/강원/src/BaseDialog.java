import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.Icon;
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
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BaseDialog extends JFrame {
	static Connection con;
	static Statement stmt;

	static Color blackColor = Color.BLACK;
	static Color whiteColor = Color.WHITE;

	static boolean chk1;
	static boolean chk2;
	static int u_no;
	static String u_name, u_id;

	eMsg emsg;
	iMsg imsg;

	static DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();

	HashMap<String, String> hashMap = new HashMap<String, String>();

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

	String getOne(String sql) {
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

	public BaseDialog(String title, int w, int h) {
		setTitle(title);
		setSize(w, h);
		setDefaultCloseOperation(2);
		setLocationRelativeTo(null); 
		setLayout(new BorderLayout());

		hashMap.put("�λ�", "busan");
		hashMap.put("������", "gangwondo");
		hashMap.put("���󳲵�", "Jeollanam-do");
		hashMap.put("����", "gyeongju");
		hashMap.put("����", "seoul");

		((JPanel) getContentPane()).setBackground(whiteColor);
	}

	JComponent setBounds(JComponent c, int x, int y, int w, int h) {
		c.setBounds(x, y, w, h);
		add(c);
		return c;
	}

	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	JButton btn(String text, ActionListener a) {
		JButton btn = new JButton(text);
		btn.addActionListener(a);
		btn.setBackground(new Color(0, 123, 255));
		btn.setForeground(whiteColor);
		return btn;
	}

	JLabel lbl(String text, int alig) {
		JLabel lbl = new JLabel(text, alig);
		lbl.setBackground(whiteColor);
		lbl.setForeground(blackColor);
		lbl.setOpaque(false);
		return lbl;
	}

	JLabel lbl(String text, int alig, int size) {
		JLabel lbl = new JLabel(text, alig);
		lbl.setFont(new Font("���� ���", Font.BOLD, size));
		lbl.setBackground(whiteColor);
		lbl.setForeground(blackColor);
		lbl.setOpaque(false);
		return lbl;
	}

	JButton themeButton() {
		JButton btn = new JButton("�׸�");
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

	static <T extends JComponent> T size(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	DefaultTableModel model(String[] col) {
		DefaultTableModel m = new DefaultTableModel(null, col) {
//			public boolean isCellEditable(int row, int column) {
//				return false;
//			};
		};
		return m;
	}

	JTable table(DefaultTableModel m) {
		JTable jt = new JTable(m);
		for (int i = 0; i < m.getColumnCount(); i++) {
			jt.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		return jt;
	}

	void addRow(DefaultTableModel dtm, String sql) {
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

	static int toInt(Object path) {
		return Integer.parseInt(path.toString());
	}

	JTextField HintText(JTextField t, final String txt) {
		t.setText(txt);
		t.setFont(new Font("���� ���", Font.BOLD, 11));
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

	JPasswordField HintText(JPasswordField t, final String txt) {
		t.setText(txt);
		t.setFont(new Font("���� ���", Font.BOLD, 11));
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

	class iMsg extends JDialog {
		String msg;

		public iMsg(String msg) {
			setTitle("�ȳ�");
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

			s.add(btn("Ȯ��", e -> {
				dispose();
			}));

			this.setIconImage(null);
			setModal(true);
		}
	}

	class eMsg extends JDialog {
		String msg;

		public eMsg(String msg) {
			setTitle("����");
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

			s.add(btn("Ȯ��", e -> {
				dispose();
			}));

			this.setIconImage(null);
			setModal(true);
		}
	}
}
