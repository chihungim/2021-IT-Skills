package view;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import db.DBManager;

public class Test extends BaseFrame {
		
	public Test() {
		super("테스트", 500, 500);
		this.setLayout(new FlowLayout());
		
		for (int i = 0; i < 10; i++) {
			JLabel lbl = new JLabel("머킹의 회전목마", img("./datafiles/아이콘.png", 25, 30), 0);
			JButton btn = new JButton("머킹의 회전목마", img("./datafiles/아이콘.png", 25, 30));
			lbl.setHorizontalTextPosition(JButton.RIGHT);
			lbl.setVerticalTextPosition(JButton.CENTER);
			this.add(lbl);
		}
		
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		String str = "01-abc123-한글1234"; String restr = str.replaceAll("[^0-9]",""); System.out.println(str + " ==> " + restr);
		String i = "3";
		System.out.println(DBManager.getOne("select r_name from ride where r_no="+i));
	}
	
}
