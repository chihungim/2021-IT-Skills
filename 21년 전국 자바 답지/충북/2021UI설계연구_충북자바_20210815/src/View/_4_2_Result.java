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

public class _4_2_Result extends MyCommonPage{
	MyPanel panel1, panel2, panel3;
	MyPanel sub1, sub2;
	
	String kind[], info1[][][];
	
	public _4_2_Result() {
		//B���� ��ġ
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {0,2,1015,57});
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {0,58,1015,30});
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] {1,89,1016,143+39+400});
		
		data();
		ui();
	}
	
	private void data() {
		kind = "����,�ٹ�,��Ƽ��Ʈ,�÷��̸���Ʈ".split(",");
		info1 = new String[][][] {
			{{"./�����ڷ�/images/album/1.jpg","Breathing"},{"./�����ڷ�/images/album/2.jpg","Twenty Three"},{"./�����ڷ�/images/album/3.jpg","One Year, Six Monthes"},{"./�����ڷ�/images/album/4.jpg","Three Fights Up"}},
			{{"./�����ڷ�/images/album/1.jpg","When your thinking say yes"},{"./�����ڷ�/images/album/2.jpg","Southern Air"},{"./�����ڷ�/images/album/3.jpg","The Script"},{"./�����ڷ�/images/album/4.jpg","Science & Faith"}},
			{{"./�����ڷ�/images/artist/1.jpg","The Script"},{"./�����ڷ�/images/artist/2.jpg","Pain At The Disco"},{"./�����ڷ�/images/artist/3.jpg","James Arthor"},{"./�����ڷ�/images/artist/4.jpg","Charlie Puth"}},
			{}
		};
	}

	private void ui() {
		//panel1 ����
		panel1=new MyPanel(bpanel.panels.get(0), new FlowLayout(FlowLayout.RIGHT));
		panel1.addTextField("", Color.LIGHT_GRAY, new int[] {1,5,911,36});
		panel1.addButton("�˻�", Color.WHITE, new int[] {915,5,76,36});

		//panel2 ����
		panel2=new MyPanel(bpanel.panels.get(1), new FlowLayout(FlowLayout.LEFT));
		panel2.addLabel("Th�� ���� �˻���� �� 404��", Color.WHITE, "", JLabel.LEFT, Font.BOLD, 15, new int[] {0,0,1000,30});
		
		//panel3 ����
		panel3=new MyPanel(bpanel.panels.get(2), new FlowLayout(FlowLayout.LEFT, 5, 5));
		for(int i=0; i<4; i++) {
			panel3.addPanel(null, Color.DARK_GRAY, new int[] {0,0,480,270});
			
			sub1 = new MyPanel(panel3.panels.get(i), null);
			sub1.addLabel(kind[i] + " 50��", Color.WHITE, "", JLabel.LEFT, Font.PLAIN, 12, new int[] {2,10,161,30});
			sub1.addLabel("��� ����", Color.WHITE, "", JLabel.RIGHT, Font.PLAIN, 12, new int[] {344,10,92,31});
			sub1.addPanel(null, Color.DARK_GRAY, new int[] {2,37,434,235});
			
			sub2 = new MyPanel(sub1.panels.get(0), new GridLayout(2, 2, 5, 5));
			for(int j=0; j<info1[i].length; j++) {
				sub2.addInfoBoxLabel(Color.WHITE, info1[i][j][0], info1[i][j][1], Font.PLAIN, 12, new int[] {100, 100});
				sub2.alignLabelimgtxt(sub2.lbls.get(0), JLabel.RIGHT, JLabel.CENTER);
			}
			
			sub1.panels.get(0).setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(1, -1, -1, -1), BorderFactory.createDashedBorder(Color.WHITE)));
		}
		
		//���� ����
		panel3.me.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		Main.mf.repaint();
		Main.mf.revalidate();
	}

	public static void main(String[] args) {
		//�׽�Ʈ
		if(Main.mf == null) 
			Main.mf = new MyFrame();
		else {
			Main.mf.panels.clear();
		}
		
		new _4_2_Result();
	}
}
