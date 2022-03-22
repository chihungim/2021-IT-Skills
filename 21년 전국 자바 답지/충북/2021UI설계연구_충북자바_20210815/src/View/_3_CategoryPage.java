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
		//B���� ��ġ
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {0,2,1015,230});
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {0,235,1015,39});
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {1,276,1016,400});

		data();
		ui();
	}
	
	private void data() {
		//panel3�� ������
		info1 = new String[][] {{"1.jpg", "Answer: Love Myself", "Love Yourself: Answer"},
							    {"2.jpg", "Intro: Persona", "MAP OF THE SOUL: 7"},
							    {"3.jpg", "Seattle Alone", "�������! �ɱ�� - EP"},
							    {"4.jpg", "Seattle Alone", "�������! �ɱ�� - EP"},
							    {"5.jpg", "Seattle Alone", "�������! �ɱ�� - EP"},
							    {"6.jpg", "Seattle Alone", "�������! �ɱ�� - EP"},
							    {"7.jpg", "Seattle Alone", "�������! �ɱ�� - EP"},
							    {"8.jpg", "Seattle Alone", "�������! �ɱ�� - EP"},
							    {"9.jpg", "Seattle Alone", "�������! �ɱ�� - EP"},
							    {"10.jpg", "Seattle Alone", "�������! �ɱ�� - EP"},
							    {"11.jpg", "Seattle Alone", "�������! �ɱ�� - EP"},
							    {"12.jpg", "Seattle Alone", "�������! �ɱ�� - EP"},
							    {"13.jpg", "Seattle Alone", "�������! �ɱ�� - EP"}
						       };
	}

	private void ui() {
		//panel1 ����
		panel1=new MyPanel(bpanel.panels.get(0), new BorderLayout());
		panel1.addLabel("��", Color.white, "./�����ڷ�/images/category/��.jpg", 0, Font.BOLD, 20, new int[] {0,0,1015,230});
		panel1.alignLabelimgtxt(panel1.lbls.get(0), JLabel.CENTER, JLabel.CENTER);
		
		//panel2 ����
		panel2=new MyPanel(bpanel.panels.get(1), new FlowLayout(FlowLayout.LEFT));
		panel2.addLabel("�ֱ� �߸��� �ٹ�", Color.WHITE, "", JLabel.LEFT, Font.BOLD, 15, new int[] {0,0,200,20});

		//panel3 ����
		panel3=new MyPanel(bpanel.panels.get(2), new GridLayout(0, 3, 10, 10));
		for(int i=0; i<13; i++) {
			panel3.addInfoBoxLabel(Color.WHITE, "./�����ڷ�/images/album/" + info1[i][0], "<html>"+info1[i][1]+"<br>"+info1[i][2], Font.PLAIN, 12, new int[] {120,120});
			panel3.alignLabelimgtxt(panel3.lbls.get(i), JLabel.RIGHT, JLabel.CENTER);
		}
		
		//���� ����
		panel3.me.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		//��ũ���� �� �ֵ��� B���� ���̸� �����Ѵ�.
		panel3.me.setBounds(1, 276, 1016, (120+5)*5 + 30);
		bpanel.me.setPreferredSize(new Dimension(1016, panel3.me.getY() + (120+5)*5 + 30));
	}

	public static void main(String[] args) {
		//�׽�Ʈ
		if(Main.mf == null) 
			Main.mf = new MyFrame();
		else {
			Main.mf.panels.clear();
		}
		
		new _3_CategoryPage();
	}
}
