package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import db.DBManager;

public class BaseDialog extends JDialog {
	
	JPanel n, c, w, s, e;
	
	public BaseDialog(String tit, int w, int h) {
		this.setTitle(tit);
		this.setModal(true);
		this.setSize(w, h);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);
		this.getContentPane().setBackground(BaseFrame.col);
		
	}
	
	static DefaultTableModel model(String[] str) {
		DefaultTableModel m = new DefaultTableModel(null, str) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				for (int i = 0; i < getColumnCount(); i++) {
					for (int j = 0; j < getRowCount(); j++) {
						if (isNum(getValueAt(j, i).toString())) {
							return Integer.class;
						}
					}
				}
				return String.class;
			}
		};
		return m;
	}
	
	JTable table(DefaultTableModel m) {
		JTable t = new JTable(m);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(m);
		t.setAutoCreateRowSorter(true);
		return t;
	}
	
	static boolean isNum(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	void addRow(DefaultTableModel m, String sql) {
		m.setRowCount(0);
		try {
			var rs= DBManager.rs(sql);
			while (rs.next()) {
				Object row[] = new Object[m.getColumnCount()];
				row[0] = rs.getInt(1);
				for (int i = 1; i < row.length; i++) {
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
	
	static int rei(Object obj) {
		if (obj.toString().equals("")) return 0;
		return Integer.parseInt(obj.toString());
	}
	
	static JLabel lbl(String text, int alig) {
		JLabel l = new JLabel(text, alig) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(BaseFrame.col);
				setForeground(BaseFrame.fcol);
			}
		};
		return l;
	}
	
	static JLabel lbl(String text, int alig, int size) {
		JLabel l = new JLabel(text, alig) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(BaseFrame.col);
				setForeground(BaseFrame.fcol);
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
				setBackground(BaseFrame.col);
				setForeground(BaseFrame.fcol);
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
				setBackground(BaseFrame.col);
				setForeground(BaseFrame.fcol);
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
				setBackground(BaseFrame.col);
				setForeground(BaseFrame.fcol);
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
		b.setBackground(BaseFrame.blue);
		b.setForeground(Color.white);
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
			setBackground(BaseFrame.col);
			setForeground(BaseFrame.fcol);
			
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
			setBackground(BaseFrame.col);
			setForeground(BaseFrame.fcol);
			
			if (hol == null || hol.length() == 0 || this.getText().length() > 0) return;
			
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.LIGHT_GRAY);
			g2.setFont(new Font("", Font.PLAIN, 13));
			g2.drawString(hol, getInsets().left, this.getHeight()/2+10);
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
			
			setEmpty((JPanel)getContentPane(), 5, 0, 0, 0);
			this.getContentPane().setBackground(BaseFrame.col);
			this.setVisible(true);
			
		}
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			setBackground(BaseFrame.col);
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
			this.getContentPane().setBackground(BaseFrame.col);
			this.setVisible(true);
			
		}
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			setBackground(BaseFrame.col);
		}
	}
	
}
