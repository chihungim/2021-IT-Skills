package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class MyFrame extends JFrame {
	static ArrayList<JPanel> panels = new ArrayList<JPanel>();
	
	//초기 작업
	public MyFrame() {
		super("Music");
		setSize(1300, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(2);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./지급자료/images/logo.png"));
		getContentPane().setBackground(Color.DARK_GRAY);
	}

	//패널 생성
	void addPanel(Border bd, Color bg, String imgpath, int ...loc) {
		JPanel _obj = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if(imgpath!="") {
					ImageIcon img = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imgpath)
						.getScaledInstance(loc[2], loc[3], Image.SCALE_SMOOTH));
					g.drawImage(img.getImage(), 0, 0, null);
				}
			}
		};
		
		_obj.setBackground(bg);
		_obj.setBorder(bd);

		if(this.getContentPane().getLayout() == null)
			_obj.setBounds(loc[0], loc[1], loc[2], loc[3]);
		else
			_obj.setPreferredSize(new Dimension(loc[2], loc[3]));
			
		panels.add(_obj);
		this.add(panels.get(panels.size()-1));
	}
	
	//스크롤팬 생성
	void addScrollPane(JPanel _jp, int ...loc) {
		JScrollPane _obj = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		if(this.getContentPane().getLayout() == null)
			_obj.setBounds(loc[0], loc[1], loc[2], loc[3]);
		else
			_obj.setPreferredSize(new Dimension(loc[2], loc[3]));
		
		_jp.setPreferredSize(new Dimension(loc[2], loc[3]));
		_obj.setViewportView(_jp);
		
		this.add(_obj);
		
		//여백 제거
		_obj.setBorder(new EmptyBorder(0, 0, 0, 0));
	}
}
