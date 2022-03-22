import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RegisterMap_exportTxtFile extends JFrame {

	BufferedImage img;
	JLabel flag;
	JLabel map;
	JPanel jp;
	
	boolean move_flag;
	JLabel points;
	ArrayList<Point> pList = new ArrayList<Point>();

	int roadCnt, roadCnt2 = 0, clickCnt = 0;
	String node1 = null, node2 = null;
	BufferedWriter writer1, writer2;
	BufferedReader reader;
	JRadioButton jrb[];
	int bwSize = 50, bhSize = 50, hOff=-15, vOff=-8;

	public RegisterMap_exportTxtFile() {
		super("저기요");
		this.setSize(1200, 900);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);
		this.getContentPane().setBackground(Color.YELLOW);

		jp = new JPanel();
		this.add(jp);

		jp.setLayout(null);
		jp.setBackground(Color.WHITE);
		jp.setSize(1200, 900);

		// 텍스트 파일 열기: 읽기 모드
		jrb = new JRadioButton[50];
		try {
			reader = Files.newBufferedReader(Paths.get("./LoadLocation.txt"));

			String s;
			roadCnt = 0;
			
			s = reader.readLine();
			while (s != null && s != "") {
				String info[] = s.split("\t");

				jrb[roadCnt] = new JRadioButton(info[0]);
				jrb[roadCnt].setVerticalAlignment(JRadioButton.TOP);
				jrb[roadCnt].setHorizontalTextPosition(JRadioButton.CENTER);
				jrb[roadCnt].setVerticalTextPosition(JRadioButton.BOTTOM);
				jrb[roadCnt].setName(info[0]);
				jrb[roadCnt].setOpaque(false);
				jrb[roadCnt].setBounds(Integer.parseInt(info[1])+hOff, Integer.parseInt(info[2])+vOff, bwSize, bhSize);
				jrb[roadCnt].setBackground(Color.CYAN);
				
				pList.add(new Point(Integer.parseInt(info[1]), Integer.parseInt(info[2])));
				jrb[roadCnt].addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if(node1==null && ((JRadioButton)e.getSource()).isSelected()) 
							node1 = ((JRadioButton)e.getSource()).getName();
						else if(node2==null && ((JRadioButton)e.getSource()).isSelected())
							node2 = ((JRadioButton)e.getSource()).getName();
						
						if (node1 != null && node2 != null) {
							//텍스트 파일에 쓰기
							try {
								writer2.write(node1 + "\t" + node2 + "\n");
								writer2.flush();
							} catch (IOException e1) {
							}
							
							node1 = node2 = null;
							clearRadioButton();							
						}
					}
				});
				
				jp.add(jrb[roadCnt]);
				
				roadCnt++;
				s = reader.readLine();
			}
			reader.close();
		} catch (IOException e1) {
		}
		
		JLabel l = new JLabel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D)g;
				g2.setStroke(new BasicStroke(3));
				g2.setColor(Color.red);
				
				int xx[] = new int[pList.size()], yy[] = new int[pList.size()];
				for (int i = 0; i < pList.size(); i++) {
					xx[i] = pList.get(i).x;
					yy[i] = pList.get(i).y;
				}
//				g2.drawPolyline(xx, yy, pList.size());
			}
		};
		
		l.setBounds(0, 0, 1000, 700);
		jp.add(l);
		map = new JLabel(
				new ImageIcon(new ImageIcon("./map.png").getImage().getScaledInstance(1000, 700, Image.SCALE_DEFAULT)));
		map.setBounds(0, 0, 1000, 700);
		jp.add(map);
		

		// 텍스트 파일 열기: 쓰기 파일
		try {
			writer1 = Files.newBufferedWriter(Paths.get("./LoadLocation.txt"), StandardOpenOption.APPEND);
		} catch (IOException e1) {
		}

		try {
			writer2 = Files.newBufferedWriter(Paths.get("./LoadConnect.txt"), StandardOpenOption.APPEND);
		} catch (IOException e1) {
		}

		// 이벤트 처리
		map.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 텍스트 파일에 기록
				try {
					roadCnt2++;
					writer1.write(String.format("%03d", roadCnt2) + "\t" + e.getX() + "\t" + e.getY() + "\n");
					writer1.flush();
				} catch (IOException e1) {
				}
			}
		});

		//
		this.setVisible(true);
	}

	void clearRadioButton() {
		for(int i=0; i<jrb.length; i++) {
			if(jrb[i] == null) break;
			jrb[i].setSelected(false);
		}
	}
	
	public static void main(String[] args) {
		new RegisterMap_exportTxtFile();
	}
}
