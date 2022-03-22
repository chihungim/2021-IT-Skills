package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;

public class _2_HomePage extends MyCommonPage {
	MyPanel panel1, panel2, panel3, panel4;
	MyPanel subpanel;

	String kind[], info1[][], info2[];

	public _2_HomePage() {
		// B구역 배치
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 0, 2, 1015, 57 });
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 0, 61, 1015, 173 });
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 0, 235, 1015, 39 });
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 1, 276, 1016, 400 });

		data();
		ui();
	}

	private void data() {
		// panel2용 데이터
		kind = "이번달 히트 곡, 최근 발매된 앨범, 서울특별시의 인기 음악".split(",");
		info1 = new String[][] { { "1.jpg", "Answer: Love Myself", "Love Yourself: Answer" },
				{ "2.jpg", "Intro: Persona", "MAP OF THE SOUL: 7" }, { "3.jpg", "Seattle Alone", "사춘기집! 꽃기운 - EP" } };

		// panel4용 데이터
		info2 = "팝,펑크,록,댄스,하우스,컨트리,가요,R&B,힙합,클래식,발라드,어쿠스틱,K-POP".split(",");
	}

	private void ui() {
		// panel1 생성
		panel1 = new MyPanel(bpanel.panels.get(0), new FlowLayout(FlowLayout.RIGHT));
		panel1.addButton("로그아웃", Color.WHITE, new int[] { 0, 0, 100, 40 });

		// panel2 생성
		panel2 = new MyPanel(bpanel.panels.get(1), new FlowLayout(FlowLayout.LEFT));
		for (int i = 0; i < 3; i++) {
			panel2.addPanel(null, Color.BLUE, new int[] { 0, 0, 995 / 3, 160 });

			subpanel = new MyPanel(panel2.panels.get(i), new FlowLayout(FlowLayout.LEFT, 5, 10));
			subpanel.addLabel(kind[i], Color.WHITE, "", JLabel.LEFT, Font.BOLD, 15, new int[] { 0, 0, 200, 20 });
			subpanel.addInfoBoxLabel(Color.WHITE, "./지급자료/images/album/" + info1[i][0],
					"<html>" + info1[i][1] + "<br>" + info1[i][2], Font.PLAIN, 12, new int[] { 100, 100 });
			subpanel.alignLabelimgtxt(subpanel.lbls.get(1), JLabel.RIGHT, JLabel.CENTER);
		}

		// panel3 생성
		panel3 = new MyPanel(bpanel.panels.get(2), new FlowLayout(FlowLayout.LEFT));
		panel3.addLabel("카테고리 둘러보기", Color.WHITE, "", JLabel.LEFT, Font.BOLD, 15, new int[] { 0, 0, 200, 20 });

		// panel4 생성
		panel4 = new MyPanel(bpanel.panels.get(3), new FlowLayout(FlowLayout.LEFT));
		for (int i = 0; i < 13; i++) {
			panel4.addLabel(info2[i], Color.WHITE, "./지급자료/images/category/" + info2[i] + ".jpg", JLabel.LEFT,
					Font.BOLD, 15, new int[] { 0, 0, 995 / 3, 120 });
			panel4.alignLabelimgtxt(panel4.lbls.get(i), JLabel.CENTER, JLabel.CENTER);
		}

		// 스크롤할 수 있도록 B구역 높이를 조정한다.
		panel4.me.setBounds(1, 276, 1016, (120 + 5) * 5 + 30);
		bpanel.me.setPreferredSize(new Dimension(1016, panel4.me.getY() + (120 + 5) * 5 + 30));

		// 특수 처리: 점선
		panel4.me.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(1, -1, -1, -1),
				BorderFactory.createDashedBorder(Color.WHITE)));

		Main.mf.repaint();
		Main.mf.revalidate();
	}

	public static void main(String[] args) {
		// 테스트
		if (Main.mf == null)
			Main.mf = new MyFrame();
		else {
			Main.mf.panels.clear();
		}

		new _2_HomePage();
	}
}
