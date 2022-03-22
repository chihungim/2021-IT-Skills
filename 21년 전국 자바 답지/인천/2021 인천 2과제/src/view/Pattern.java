package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Pattern extends JDialog {

	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<String> pw = new ArrayList<>();
	boolean dragFlag, oneChane = true;
	JLabel dot[] = new JLabel[9];

	public Pattern(JTextField txt) {
		super(BasePage.mf, true);
		setTitle("����");
		setSize(310, 380);
		setLocationRelativeTo(null);
		var s = new JPanel(new FlowLayout(FlowLayout.CENTER));

		((JPanel) getContentPane()).setBackground(Color.WHITE);
		var c = new JPanel(new GridLayout(0, 3)) {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setStroke(new BasicStroke(7));
				g2.setColor(Color.blue.darker());

				int xx[] = new int[points.size()], yy[] = new int[points.size()];
				for (int i = 0; i < points.size(); i++) {
					xx[i] = points.get(i).x + 8;
					yy[i] = points.get(i).y + 8;
				}
				g2.drawPolyline(xx, yy, points.size());
			}
		};

		add(c);
		for (int i = 0; i < dot.length; i++) {
			var tmp = new JPanel(new GridBagLayout());
			tmp.add(dot[i] = BasePage.lbl("��", JLabel.CENTER, 15));

			tmp.setBorder(new LineBorder(Color.LIGHT_GRAY));
			tmp.setName(i + 1 + "");
			tmp.setOpaque(false);

			dot[i].setName((i + 1) + "");
			dot[i].setOpaque(false);

			dot[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					dragFlag = true;
					mouseEntered(e);
					super.mousePressed(e);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					dragFlag = false;
					oneChane = false;
					super.mouseReleased(e);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					if (dragFlag && oneChane) {
						var me = (JLabel) e.getSource();
						var rpos = me.getRootPane().getLocationOnScreen();
						var mpos = me.getLocationOnScreen();
						var dif = new Point(mpos.x - rpos.x, mpos.y - rpos.y);
						if (!points.contains(dif)) {
							points.add(dif);
							pw.add(me.getName());
						}
						repaint();
					}
				}
			});
			c.add(tmp);
		}
		add(s, "South");
		JButton btn;
		s.add(btn = new JButton("Ȯ��"), "South");

		btn.addActionListener(a -> {
			txt.setText(String.join("", pw));
			setVisible(false);
		});
	}

	public static void main(String[] args) {
		new Pattern(null).setVisible(true);
	}
}
