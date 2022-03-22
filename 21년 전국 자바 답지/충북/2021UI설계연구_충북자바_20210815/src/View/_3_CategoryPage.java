package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class _3_CategoryPage extends MyCommonPage{
	MyPanel panel1, panel2, panel3;
	
	String info1[][];
	
	public _3_CategoryPage() {
		//B구역 배치
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {0,2,1015,230});
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {0,235,1015,39});
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {1,276,1016,400});

		data();
		ui();
	}
	
	private void data() {
		//panel3용 데이터
		info1 = new String[][] {{"1.jpg", "Answer: Love Myself", "Love Yourself: Answer"},
							    {"2.jpg", "Intro: Persona", "MAP OF THE SOUL: 7"},
							    {"3.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP"},
							    {"4.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP"},
							    {"5.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP"},
							    {"6.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP"},
							    {"7.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP"},
							    {"8.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP"},
							    {"9.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP"},
							    {"10.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP"},
							    {"11.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP"},
							    {"12.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP"},
							    {"13.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP"}
						       };
	}

	private void ui() {
		//panel1 생성
		panel1=new MyPanel(bpanel.panels.get(0), new BorderLayout());
		panel1.addLabel("팝", Color.white, "./지급자료/images/category/팝.jpg", 0, Font.BOLD, 20, new int[] {0,0,1015,230});
		panel1.alignLabelimgtxt(panel1.lbls.get(0), JLabel.CENTER, JLabel.CENTER);
		
		//panel2 생성
		panel2=new MyPanel(bpanel.panels.get(1), new FlowLayout(FlowLayout.LEFT));
		panel2.addLabel("최근 발매한 앨범", Color.WHITE, "", JLabel.LEFT, Font.BOLD, 15, new int[] {0,0,200,20});

		//panel3 생성
		panel3=new MyPanel(bpanel.panels.get(2), new GridLayout(0, 3, 10, 10));
		for(int i=0; i<13; i++) {
			panel3.addInfoBoxLabel(Color.WHITE, "./지급자료/images/album/" + info1[i][0], "<html>"+info1[i][1]+"<br>"+info1[i][2], Font.PLAIN, 12, new int[] {120,120});
			panel3.alignLabelimgtxt(panel3.lbls.get(i), JLabel.RIGHT, JLabel.CENTER);
		}
		
		//여백 조정
		panel3.me.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		//스크롤할 수 있도록 B구역 높이를 조정한다.
		panel3.me.setBounds(1, 276, 1016, (120+5)*5 + 30);
		bpanel.me.setPreferredSize(new Dimension(1016, panel3.me.getY() + (120+5)*5 + 30));
	}

	public static void main(String[] args) {
		//테스트
		if(Main.mf == null) 
			Main.mf = new MyFrame();
		else {
			Main.mf.panels.clear();
		}
		
		new _3_CategoryPage();
	}
}
