package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class MyCommonPage {
	MyFrame mf;
	MyPanel apanel, bpanel, cpanel;
	
	public MyCommonPage() {
		//MFrame ����
		mf = Main.mf;

		//MFrame�� �г� ��ġ
		mf.setLayout(null);
		mf.addPanel(new EmptyBorder(10, 20, 10, 20), Color.DARK_GRAY, "./�����ڷ�/images/side.png", new int[] {1,1,250,659});
		mf.addPanel(new EmptyBorder(0, 0, 0, 0), Color.DARK_GRAY, "", new int[] {0,0,0,0});
		mf.addScrollPane(mf.panels.get(1), new int[] {250,1,1033,660});
		mf.addPanel(new EmptyBorder(10, 20, 10, 20), Color.DARK_GRAY, "", new int[] {1,660,1282,98});
		
		//A������ ������Ʈ ��ġ
		apanel = new MyPanel(mf.panels.get(0), new GridLayout(20,1,0,5));
		
		String titles[] = "MENU,Ȩ,�˻��ϱ�,,LIBRARY,���ƿ�,������,,PLAYLIST,������ �߰�,��ź�ҳ�� ����,K��,���� ��� ������,�÷��̸���Ʈ 1".split(",");
		int styles[] = {Font.BOLD, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.BOLD, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.BOLD, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.PLAIN};
		int sizes[] = {15,12,12,12,15,12,12,12,15,12,12,12,12,12};
		for(int i=0; i<titles.length; i++) {
			apanel.addLabel(titles[i], Color.WHITE, "", JLabel.LEFT, styles[i], sizes[i], new int[] {0,0,0,0});
		}
		
		//B������ ������Ʈ ��ġ
		bpanel = new MyPanel(mf.panels.get(1), null);
		
		//C������ ������Ʈ ��ġ
		cpanel = new MyPanel(mf.panels.get(2), null);
		cpanel.addLabel("�̹���", Color.WHITE, "", 0, 0, 12, new int[] {5,14,55,52});
		cpanel.addLabel("", Color.RED, "./�����ڷ�/images/prev.png", 0, 0, 12, new int[] {247,39,30,27});
		cpanel.addLabel("", null, "./�����ڷ�/images/play.png", 0, 0, 0, new int[] {569,40,32,24});
		cpanel.addLabel("", null, "./�����ڷ�/images/next.png", 0, 0, 0, new int[] {866,40,31,26});
		cpanel.addLabel("", null, "./�����ڷ�/images/queue.png", 0, 0, 0, new int[] {1228,27,34,38});
		cpanel.addLabel("������� �ƴ�", Color.WHITE, "", JLabel.CENTER, 0, 12, new int[] {390,12,399,25});
		
		//Ư�� ó��: ����
		cpanel.me.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(1, -1, -1, -1), BorderFactory.createDashedBorder(Color.WHITE)));
		cpanel.lbls.get(0).setBorder(BorderFactory.createDashedBorder(Color.WHITE));

		mf.setVisible(true);
	}

	public static void main(String[] args) {
		//�׽�Ʈ
		if(Main.mf == null) 
			Main.mf = new MyFrame();
		else {
			Main.mf.panels.clear();
		}
			
		new MyCommonPage();
	}
}
