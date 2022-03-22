package additional;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Captcha extends JFrame {
	
	JPanel n, c, s;
	String captchar;
	JLabel lbl;
	JTextField txt;
	
	public Captcha() {
		super();
		this.setSize(400, 400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);
		this.setVisible(true);
		
		captchar = GetCaptcha();
		this.add(n = new JPanel(), "North");
		this.add(c = new JPanel(new GridLayout(0, 6)));
		this.add(s = new JPanel(), "South");
		
		for (int i = 0; i < 5; i++) {
			int j = i;
			c.add(lbl = new JLabel() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					
					Graphics2D g2 = (Graphics2D)g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setFont(new Font("HYÇìµå¶óÀÎM", Font.BOLD, 25));
					
					Random r = new Random();
					g2.setColor(Color.white);
					g2.fillRect(0, this.getHeight()/2-60, this.getWidth(), 100);
					g2.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)).darker());
					int flag = r.nextInt(2);
					g2.rotate(flag==0?r.nextInt(10)/10.0:-r.nextInt(10)/10.0, lbl.getWidth()/2, lbl.getHeight()/2);
					g2.drawString(captchar.charAt(j)+"", 15, this.getHeight()/2);
					
				}
			});
		}
		
		
		n.add(btn("Ä¸Ã­ ´Ù½Ã »ý¼º", a->{
			captchar = GetCaptcha();
			repaint();
			revalidate();
		}));
		
		s.add(txt = new JTextField(10));
		txt.setHorizontalAlignment(SwingConstants.CENTER);
		s.add(btn("È®ÀÎ", a->{
			if (!txt.getText().toUpperCase().equals(captchar.toUpperCase())) {
				JOptionPane.showMessageDialog(null, "Ä¸Ã­ Æ²¸²", "°æ°í", JOptionPane.ERROR_MESSAGE);
				captchar = GetCaptcha();
				repaint();
				revalidate();
				return;
			}
			
			JOptionPane.showMessageDialog(null, "Ä¸Ã­ ¸ÂÀ½", "Á¤º¸", JOptionPane.INFORMATION_MESSAGE);
		}));
		
		c.setBorder(new EmptyBorder(0, 60, 0, 0));
	}
	
	String GetCaptcha() {
		ArrayList<Character> arr = new ArrayList<Character>();
		String captcha ="";
		for (int i = '0'; i < 'Z'; i++) {
			if (i>=58 && i<=64) 
				continue;
			arr.add((char)i);
		}

		Random r = new Random();
		
		while (true) {
			if (captcha.length()==5) {
				if (!(captcha.matches(".*[0-9].*") && captcha.matches(".*[a-zA-Z].*"))) {
					captcha = "";
				} else return captcha;
			} 
			captcha += arr.get(r.nextInt(arr.size()));
		}
	}
	
	JButton btn(String text, ActionListener a) {
		JButton b = new JButton(text);
		b.addActionListener(a);
		return b;
	}
	
	public static void main(String[] args) {
		new Captcha();
	}
	
}
