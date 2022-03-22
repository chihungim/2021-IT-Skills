package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import db.DBManager;

public class BaseFrame extends JFrame {
	
	JPanel n, c, w, s, e;
	static String uno="", uname, upw;
	static DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	static Color blue = new Color(0, 123, 255);
	HashMap<String, String> hash = new HashMap<String, String>();
	static Color col = Color.white, fcol = Color.black;
	static Login l = new Login();
	TitledBorder titled[] = { new TitledBorder(new LineBorder(Color.black), ""),new TitledBorder(new LineBorder(Color.black), ""),new TitledBorder(new LineBorder(Color.black), ""),new TitledBorder(new LineBorder(Color.black), ""),new TitledBorder(new LineBorder(Color.black), "") };
	
	public BaseFrame(String tit, int w, int h) {
		super(tit);
		this.setSize(w, h);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);
		this.getContentPane().setBackground(col);
		
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		hash.put("부산", "busan");
		hash.put("광주", "gyeongju");
		hash.put("강원도", "gangwondo");
		hash.put("전라남도", "Jeollanam-do");
		hash.put("서울", "seoul");
	}
	
	static DefaultTableModel model(String[] str) {
		DefaultTableModel m = new DefaultTableModel(null, str) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return m;
	}
	
	JTable table(DefaultTableModel m) {
		JTable t = new JTable(m);
		t.setAutoCreateRowSorter(true);
		return t;
	}
	
	static void addRow(DefaultTableModel m, String sql) {
		m.setRowCount(0);
		try {
			var rs= DBManager.rs(sql);
			while (rs.next()) {
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
	
	static void setEmpty(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
	}
	
	static void setLine(JComponent c, Color color) {
		c.setBorder(new LineBorder(color));
	}
	
//	static void setTitled(JComponent c, String cap) {
//		TitledBorder title = new TitledBorder(new LineBorder(Color.black), cap) {
//			@Override
//			public void setTitle(String title) {
//				// TODO Auto-generated method stub
//				super.setTitle(title);
//			}
//		};
//		c.setBorder(title);
//	}
	
	static int rei(Object obj) {
		if (obj.toString().equals("")) return 0;
		return Integer.parseInt(obj.toString());
	}
	
	static JLabel lbl(String text, int alig) {
		JLabel l = new JLabel(text, alig) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(col);
				setForeground(fcol);
			}
		};
		return l;
	}
	
	static JLabel lbl(String text, int alig, int size) {
		JLabel l = new JLabel(text, alig) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(col);
				setForeground(fcol);
			}
		};
		l.setFont(new Font("맑은 고딕", Font.BOLD, size));
		return l;
	}

	static JLabel lblP(String text, int alig, int size) {
		JLabel l = new JLabel(text, alig) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(col);
				setForeground(fcol);
			}
		};
		l.setFont(new Font("", Font.PLAIN, size));
		return l;
	}
	
	static JLabel lbl(String text, int alig, MouseAdapter a) {
		JLabel l = new JLabel(text, alig) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(col);
				setForeground(fcol);
			}
		};
		l.addMouseListener(a);
		return l;
	}
	
	static JPanel pnl(LayoutManager lay) {
		JPanel p = new JPanel(lay) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(col);
				setForeground(fcol);
			}
		};
		p.setOpaque(true);
		return p;
	}
	
	static <T extends JComponent> T sz(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}
	
	static JButton btn(String text, ActionListener a) {
		JButton b = new JButton(text);
		b.addActionListener(a);
		b.setBackground(blue);
		b.setForeground(Color.white);
		return b;
	}
	
	static ImageIcon img(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	JComponent addComp(JComponent comp, int x, int y, int w, int h) {
		add(comp);
		comp.setBounds(x, y, w, h);
		return comp;
	}
	
	JComponent addComp(JPanel p, JComponent comp, int x, int y, int w, int h) {
		p.add(comp);
		comp.setBounds(x, y, w, h);
		return comp;
	}
	
	JButton theme() {
		JButton b = new JButton("테마") {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				setBackground(col.equals(Color.white) ? Color.DARK_GRAY : Color.white);
				setForeground(fcol.equals(Color.black) ? Color.white : Color.black);
			}
		};
		
		b.addActionListener(a->{
			col = col.equals(Color.white) ? Color.DARK_GRAY : Color.white;
			fcol = fcol.equals(Color.black) ? Color.white : Color.black;
			
			for (var l : titled) {
				l.setTitleColor(fcol);
			}
			
			BaseFrame.this.getContentPane().setBackground(col);
			BaseFrame.this.repaint();
			BaseFrame.this.revalidate();
			
		});
		
		return b;
	}
	
	static class HolderField extends JTextField {
		String hol;
		
		public HolderField(int col, String hol) {
			super(col);
			this.hol = hol;
			setBorder(new MatteBorder(0,0,1,0,Color.black));
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			setBackground(col);
			setForeground(fcol);
			
			if (hol == null || hol.length() == 0 || this.getText().length() > 0) return;
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.LIGHT_GRAY);
			g2.setFont(new Font("", Font.PLAIN, 13));
			g2.drawString(hol, getInsets().left, this.getHeight()/2+10);
		}
		
		
	}
	
	static class HolderPWField extends JPasswordField {
		String hol;
		
		public HolderPWField(int col, String hol) {
			super(col);
			setEchoChar('*');
			this.hol = hol;
			setBorder(new MatteBorder(0,0,1,0,Color.black));
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			setBackground(col);
			setForeground(fcol);
			
			if (hol == null || hol.length() == 0 || this.getText().length() > 0) return;
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.LIGHT_GRAY);
			g2.setFont(new Font("", Font.PLAIN, 13));
			g2.drawString(hol, getInsets().left, this.getHeight()/2+10);
		}
		
	}
	
	
	static class Before extends WindowAdapter {
		BaseFrame b;
		public Before(BaseFrame b) {
			this.b=b;
			b.setVisible(false);
		}
		@Override
		public void windowClosing(WindowEvent e) {
			b.setVisible(true);
		}
	}
	
	static class iMsg extends JDialog {
		JPanel c, s;
		
		public iMsg(String msg) {
			this.setTitle("안내");
			this.setModal(true);
			this.setSize(350, 130);
			this.setDefaultCloseOperation(2);
			this.setLocationRelativeTo(null);
			this.setIconImage(null);
			
			Icon ic = UIManager.getIcon("OptionPane.informationIcon");
			
			this.add(c = new JPanel());
			this.add(s = new JPanel(), "South");
			
			c.add(new JLabel(ic));
			c.add(lbl(msg, 0, 13));
			
			c.setOpaque(false);
			s.setOpaque(false);
			
			s.add(sz(btn("확인", a->{
				this.dispose();
			}), 60, 25));
			
			setEmpty((JPanel)getContentPane(), 10, 0, 0, 0);
			this.getContentPane().setBackground(col);
			this.setVisible(true);
			
		}
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			setBackground(col);
		}
	}
	
	static class eMsg extends JDialog {
		JPanel c, s;
		
		public eMsg(String msg) {
			this.setTitle("경고");
			this.setModal(true);
			this.setSize(350, 130);
			this.setDefaultCloseOperation(2);
			this.setLocationRelativeTo(null);
			this.setIconImage(null);
			
			Icon ic = UIManager.getIcon("OptionPane.errorIcon");
			
			this.add(c = new JPanel());
			this.add(s = new JPanel(), "South");
			
			c.add(new JLabel(ic));
			c.add(lbl(msg, 0, 13));
			
			c.setOpaque(false);
			s.setOpaque(false);
			
			s.add(sz(btn("확인", a->{
				this.dispose();
			}), 80, 30));
			
			setEmpty((JPanel)getContentPane(), 5, 0, 0, 0);
			this.getContentPane().setBackground(col);
			this.setVisible(true);
			
		}
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			setBackground(col);
		}
	}
	
}
