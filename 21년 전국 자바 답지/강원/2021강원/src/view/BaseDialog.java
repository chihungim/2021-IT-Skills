package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;

public class BaseDialog extends JDialog {

	static Connection con = BaseFrame.con;
	static Statement stmt = BaseFrame.stmt;

	public BaseDialog(String title, int w, int h) {
		setTitle(title);
		setModal(true);
		setSize(w, h);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	JLabel lbl(String text, int alig) {
		return new JLabel(text, alig);
	}

	JLabel lbl(String text, int alig, int size) {
		var l = new JLabel(text, alig);
		l.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, size));
		return l;
	}

	void execute(String sql) {
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

	<T extends JComponent> T sz(T c, int w, int h) {
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

	class TextHolder extends JTextField {
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
