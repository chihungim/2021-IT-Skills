package additional;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import view.BasePage;
import view.BasePage;

public class Pattern extends JDialog {
	
	PatternPanel p = new PatternPanel();
	JPanel c, s;
	JButton btn;
	
	public Pattern(JTextField txt) {
		super(BasePage.mf, "패턴", true);
		this.setSize(400, 430);
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.white);
		
		this.add(c = new JPanel(new GridLayout(0, 1)));
		c.add(p);
		this.add(s = new JPanel(), "South");
		s.add(btn = new JButton("확인"));
		btn.addActionListener(a->{
			txt.setText(String.join("", p.list));
			this.dispose();
		});
		
		
		
		c.setOpaque(false);
		p.setOpaque(false);
		s.setOpaque(false);
	}
	
}

class PatternPanel extends JPanel {
	
	JLabel lbl[] = new JLabel[9];
	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<Point> drawPoints = new ArrayList<Point>();
	ArrayList<String> list = new ArrayList<String>();
	JPanel s = new JPanel(), c = new JPanel(new GridLayout(0, 3));
	int w, h, size = 20;
	boolean chk = false;
	
	public PatternPanel() {
		this.setBackground(Color.white);
		this.setLayout(new GridLayout(0, 3));
		for (int i = 0; i <= 9; i++) {
			points.add(new Point(63 + (i%3) * 130, 58 + (i/3) * 120));
		}
		
		for (int i = 0; i < 9; i++) {
			JLabel l;
			this.add(l = new JLabel() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					g.fillOval(this.getWidth()/2-(size/2), this.getHeight()/2-(size/2), size, size);
				}
			});
			l.setBorder(new LineBorder(Color.LIGHT_GRAY));
		}
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				if (list.size()>1) {
					chk = true;
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (chk) return;
				for (int i = 0; i < points.size(); i++) {
					if (points.get(i).x+size>=e.getX() && points.get(i).x-size <=e.getX() && points.get(i).y+size>=e.getY() && points.get(i).y-size <=e.getY()) {
						if (!drawPoints.contains(new Point(points.get(i).x, points.get(i).y))) {
							drawPoints.add(new Point(points.get(i).x, points.get(i).y));
							list.add(i+1+"");
						}
					}
				}
				repaint();
				revalidate();
				
			}
		});
		
		
		this.setVisible(true);
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(7));
		g2.setColor(Color.blue.darker());
		
		int xx[] = new int[drawPoints.size()], yy[] = new int[drawPoints.size()];
		for (int i = 0; i < drawPoints.size(); i++) {
			xx[i] = drawPoints.get(i).x;
			yy[i] = drawPoints.get(i).y;
		}
		g2.drawPolyline(xx, yy, drawPoints.size());
		
	}
	
}