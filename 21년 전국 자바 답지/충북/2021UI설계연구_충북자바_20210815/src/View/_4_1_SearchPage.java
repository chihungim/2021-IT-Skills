package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class _4_1_SearchPage extends MyCommonPage{
	MyPanel panel1, panel2;
	
	String info2[];
	
	public _4_1_SearchPage() {
		//B구역 배치
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {0,2,1015,57});
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {1,58,1016,173+39+400});
		
		data();
		ui();
	}
	
	private void data() {
		//panel2용 데이터
		info2 = "팝,펑크,록,댄스,하우스,컨트리,가요,R&B,힙합,클래식,발라드,어쿠스틱,K-POP".split(",");
	}

	private void ui() {
		//panel1 생성
		panel1=new MyPanel(bpanel.panels.get(0), new FlowLayout(FlowLayout.RIGHT));
		panel1.addTextField("", Color.LIGHT_GRAY, new int[] {1,5,911,36});
		panel1.addButton("검색", Color.WHITE, new int[] {915,5,76,36});

		//panel2 생성
		panel2=new MyPanel(bpanel.panels.get(1), new GridLayout(0, 3, 10, 10));
		for(int i=0; i<13; i++) {
			panel2.addLabel(info2[i], Color.WHITE, "./지급자료/images/category/" + info2[i] + ".jpg", JLabel.LEFT, Font.BOLD, 15, new int[] {0,0,1016/3,120});
			panel2.alignLabelimgtxt(panel2.lbls.get(i), JLabel.CENTER, JLabel.CENTER);
		}
		
		//여백 조정
		panel2.me.setBorder(new EmptyBorder(10, 10, 10, 10));
				
		//스크롤할 수 있도록 B구역 높이를 조정한다.
		panel2.me.setBounds(1, 58, 1016, (120+5)*5 + 30);
		bpanel.me.setPreferredSize(new Dimension(1016, panel2.me.getY() + (120+5)*5 + 30));
	}

	public static void main(String[] args) {
		//테스트
		if(Main.mf == null) 
			Main.mf = new MyFrame();
		else {
			Main.mf.panels.clear();
		}
		
		new _4_1_SearchPage();
	}
}
