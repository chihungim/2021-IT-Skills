package ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import base.BasePage;

public class SongItem extends JPanel {
	float op = 1f;
	JPanel main = new JPanel(new BorderLayout()) {
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, op);
			g2.setComposite(ac);
			super.paint(g);
		}
	};
	JLabel img;
	JLabel name;

	public SongItem() {
		setLayout(new BorderLayout());
		setBackground(Color.white);
		main.setBackground(Color.black);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				op = 0.5f;
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				op = 1f;
				repaint();
			}
		});
		
		add(main);
	}
	
	public SongItem(String path, String t, String a, String imga, int lbla) {	
		this();
		
		setBorder(new LineBorder(Color.black));
		
		img =  new JLabel(new ImageIcon(
				Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
		name = BasePage.lbl(t, lbla, Font.BOLD, 10);

		main.add(img, imga);
		main.add(name, a);
	}
	
	public SongItem(String path, String t, String ali, String imga, int jla, MouseListener a) {
		this(path, t, ali, imga, jla);
		addMouseListener(a);
	}
	
	public SongItem(String path, String t, String ali, String imga, int jla, int w, int h, MouseListener a) {
		this(path, t, ali, imga, jla, a);
		img =  new JLabel(new ImageIcon(
				Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(w, h, Image.SCALE_SMOOTH)));
	}
}
