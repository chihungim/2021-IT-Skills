package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Calc extends BaseFrame {

	String num[] = "0,일,이,삼,사,오,육,칠,팔,구".split(",");
	String unit[] = { "", "십", "백", "천", "만", "십만", "백만", "천만", "억", "십업", "백억", "천억" };
	JComboBox[] combo = {
		new JComboBox(),	
		new JComboBox(),	
		new JComboBox(),	
		new JComboBox(),	
		new JComboBox()	
	};
	JTextField txt;
	JPanel panels[] = new JPanel[9];
	
	
	public Calc() {
		super("급여계산기", 450, 600);

		this.add(n = new JPanel(), "North");
		this.add(c = new JPanel(new GridLayout(0, 1)));
		n.add(lbl("<html><center>2021년 최저시급은<br><font color=\"red\";>8,720<font color=\"black\">원 입니다.", 0, 20));
		n.setBackground(Color.yellow);
		
		for (int i = 0; i < panels.length; i++) {
			c.add(panels[i] = new JPanel(new BorderLayout()));
		}
		panels[0].setLayout(new FlowLayout());
		panels[7].setLayout(new GridLayout());
		
		panels[0].add(combo[0]);
		panels[0].add(new JLabel(">>"));
		panels[0].add(combo[1]);
		
		panels[1].add(new JLabel("급여"), "West");
		panels[1].add(txt = new JTextField(15));
		var lbl = new JLabel("원");
		panels[1].add(lbl, "South");
		lbl.setHorizontalAlignment(SwingConstants.RIGHT);
		txt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!isNumeric(e.getKeyChar())) {
					e.consume();
				} 
			}
			@Override
			public void keyReleased(KeyEvent e) {
				lbl.setText(price2Kor(txt.getText()));
			}
		});
		
		
		this.setVisible(true);
	}

	String price2Kor(String strNum) {
		int j = strNum.length() - 1;
		String str = "";
		
		// 문자열의 길이 만큼 반복문 실행
		for (int i = 0; i < strNum.length(); i++) {
			int n = strNum.charAt(i) - '0'; // 문자열에 있는 문자를 하나씩 가져와서 int형으로 변환
			if (!num[n].contentEquals("0")) { // 숫자가 0일 경우는 출력하지 않음
				str += num[n]; // 숫자를 한글로 읽어서 출력
				str += unit[j]; // 단위 출력
			}
			j--;
		}
		return str;
	}

	public static void main(String[] args) {
		new Calc();
	}

}
