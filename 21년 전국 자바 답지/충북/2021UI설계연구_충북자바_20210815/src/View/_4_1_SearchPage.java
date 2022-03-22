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
		//B���� ��ġ
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {0,2,1015,57});
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {1,58,1016,173+39+400});
		
		data();
		ui();
	}
	
	private void data() {
		//panel2�� ������
		info2 = "��,��ũ,��,��,�Ͽ콺,��Ʈ��,����,R&B,����,Ŭ����,�߶��,����ƽ,K-POP".split(",");
	}

	private void ui() {
		//panel1 ����
		panel1=new MyPanel(bpanel.panels.get(0), new FlowLayout(FlowLayout.RIGHT));
		panel1.addTextField("", Color.LIGHT_GRAY, new int[] {1,5,911,36});
		panel1.addButton("�˻�", Color.WHITE, new int[] {915,5,76,36});

		//panel2 ����
		panel2=new MyPanel(bpanel.panels.get(1), new GridLayout(0, 3, 10, 10));
		for(int i=0; i<13; i++) {
			panel2.addLabel(info2[i], Color.WHITE, "./�����ڷ�/images/category/" + info2[i] + ".jpg", JLabel.LEFT, Font.BOLD, 15, new int[] {0,0,1016/3,120});
			panel2.alignLabelimgtxt(panel2.lbls.get(i), JLabel.CENTER, JLabel.CENTER);
		}
		
		//���� ����
		panel2.me.setBorder(new EmptyBorder(10, 10, 10, 10));
				
		//��ũ���� �� �ֵ��� B���� ���̸� �����Ѵ�.
		panel2.me.setBounds(1, 58, 1016, (120+5)*5 + 30);
		bpanel.me.setPreferredSize(new Dimension(1016, panel2.me.getY() + (120+5)*5 + 30));
	}

	public static void main(String[] args) {
		//�׽�Ʈ
		if(Main.mf == null) 
			Main.mf = new MyFrame();
		else {
			Main.mf.panels.clear();
		}
		
		new _4_1_SearchPage();
	}
}
