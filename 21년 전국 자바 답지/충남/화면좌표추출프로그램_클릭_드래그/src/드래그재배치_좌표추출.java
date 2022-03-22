import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class 드래그재배치_좌표추출 extends JFrame {
	BufferedImage img;
	JLabel imgLbl[] = new JLabel[10];
	int dragX, dragY, pressX, pressY;
 
	public 드래그재배치_좌표추출() {
		super("Flood Fill");
		this.setSize(1200, 900);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);

		this.setLayout(null);
		
		//이미지 배치
		String names[] = "강원도,경기도,경상남도,경상북도,전라남도,전라북도,제주도,충청남도,충청북도,한반도".split(",");
		try {
			for(int i=0; i<10; i++) {
				img = ImageIO.read(new File(names[i] + ".png"));
				imgLbl[i] = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(), img.getHeight(), Image.SCALE_DEFAULT)));
				imgLbl[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						JLabel jl = (JLabel)e.getSource();
						pressX = e.getX();
						pressY = e.getY();
					}
					
					@Override
					public void mouseReleased(MouseEvent e) {
						String loc = "";
						for(int i=0; i<10; i++) {
							loc = loc + "{" + imgLbl[i].getX() + "," + imgLbl[i].getY() + "}, "; 
						}
						System.out.println(loc);
					}
				});
				
				imgLbl[i].addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseDragged(MouseEvent e) {
						JLabel jl = (JLabel)e.getSource();
						dragX = e.getX();
						dragY = e.getY();
						
						jl.setLocation(jl.getX() + dragX-pressX,  jl.getY()+dragY-pressY);
					}
				});
				
				add(imgLbl[i]);
				imgLbl[i].setBounds(0,0,img.getWidth(),img.getHeight());
			}
		} catch (IOException e) {
		}

		this.setVisible(true);
	}

	public static void main(String[] args) {
		new 드래그재배치_좌표추출();
	}
}
