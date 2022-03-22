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
		//MFrame 지정
		mf = Main.mf;

		//MFrame에 패널 배치
		mf.setLayout(null);
		mf.addPanel(new EmptyBorder(10, 20, 10, 20), Color.DARK_GRAY, "./지급자료/images/side.png", new int[] {1,1,250,659});
		mf.addPanel(new EmptyBorder(0, 0, 0, 0), Color.DARK_GRAY, "", new int[] {0,0,0,0});
		mf.addScrollPane(mf.panels.get(1), new int[] {250,1,1033,660});
		mf.addPanel(new EmptyBorder(10, 20, 10, 20), Color.DARK_GRAY, "", new int[] {1,660,1282,98});
		
		//A구역에 컴포넌트 배치
		apanel = new MyPanel(mf.panels.get(0), new GridLayout(20,1,0,5));
		
		String titles[] = "MENU,홈,검색하기,,LIBRARY,좋아요,재생기록,,PLAYLIST,재생목록 추가,방탄소년단 모음,K팝,내가 듣고 싶은거,플레이리스트 1".split(",");
		int styles[] = {Font.BOLD, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.BOLD, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.BOLD, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.PLAIN};
		int sizes[] = {15,12,12,12,15,12,12,12,15,12,12,12,12,12};
		for(int i=0; i<titles.length; i++) {
			apanel.addLabel(titles[i], Color.WHITE, "", JLabel.LEFT, styles[i], sizes[i], new int[] {0,0,0,0});
		}
		
		//B구역에 컴포넌트 배치
		bpanel = new MyPanel(mf.panels.get(1), null);
		
		//C구역에 컴포넌트 배치
		cpanel = new MyPanel(mf.panels.get(2), null);
		cpanel.addLabel("이미지", Color.WHITE, "", 0, 0, 12, new int[] {5,14,55,52});
		cpanel.addLabel("", Color.RED, "./지급자료/images/prev.png", 0, 0, 12, new int[] {247,39,30,27});
		cpanel.addLabel("", null, "./지급자료/images/play.png", 0, 0, 0, new int[] {569,40,32,24});
		cpanel.addLabel("", null, "./지급자료/images/next.png", 0, 0, 0, new int[] {866,40,31,26});
		cpanel.addLabel("", null, "./지급자료/images/queue.png", 0, 0, 0, new int[] {1228,27,34,38});
		cpanel.addLabel("재생중이 아님", Color.WHITE, "", JLabel.CENTER, 0, 12, new int[] {390,12,399,25});
		
		//특수 처리: 점선
		cpanel.me.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(1, -1, -1, -1), BorderFactory.createDashedBorder(Color.WHITE)));
		cpanel.lbls.get(0).setBorder(BorderFactory.createDashedBorder(Color.WHITE));

		mf.setVisible(true);
	}

	public static void main(String[] args) {
		//테스트
		if(Main.mf == null) 
			Main.mf = new MyFrame();
		else {
			Main.mf.panels.clear();
		}
			
		new MyCommonPage();
	}
}
