package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class _1_LoginPage {
	MyPanel mpanel1;
	MyFrame mf;
	
	public _1_LoginPage() {
		//MFrame 지정
		mf = Main.mf;

		ui();
		data();
		event();
	}

	private void data() {
	}
	
	private void ui() {
		//MFrame에 컴포넌트 배치
		mf.setLayout(new GridBagLayout());
		mf.addPanel(new EmptyBorder(10, 20, 10, 20), Color.LIGHT_GRAY, "", new int[] {0,0,310,295});
						
		mpanel1 = new MyPanel(mf.panels.get(0), new GridLayout(6, 1, 5, 5));
		mpanel1.addLabel("Music Player", Color.WHITE, "", JLabel.CENTER, Font.BOLD, 30, new int[] {0,0,0,0});
		mpanel1.addLabel("ID", Color.WHITE, "", JLabel.LEFT, Font.PLAIN, 10, new int[] {0,0,0,0});
		mpanel1.addTextField("", Color.GRAY, new int[] {0,0,0,0});
		mpanel1.addLabel("PW", Color.WHITE, "", JLabel.LEFT,  Font.PLAIN, 10, new int[] {0,0,0,0});
		mpanel1.addPasswordField("", Color.GRAY, new int[] {0,0,0,0});
		mpanel1.addButton("로그인", Color.WHITE, new int[] {0,0,0,0});
		
		mf.setVisible(true);
	}

	private void event() {
	}

	public static void main(String[] args) {
		//테스트
		if(Main.mf == null) 
			Main.mf = new MyFrame();
		else {
			Main.mf.panels.clear();
		}
		
		new _1_LoginPage();
	}
}
