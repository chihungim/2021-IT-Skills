

import java.awt.GridLayout;

import javax.swing.JButton;

public class Mypage extends Basedialog {
	String str[] ="�ֹ�����,�������,�������ܰ���,���� ������".split(",");
	JButton jb[] = new JButton[4];
	
	public Mypage() {
		super("����������", 1000, 1000);
		setLayout(new GridLayout(0, 1));
		
		for (int i = 0; i < jb.length; i++) {
			add(jb[i] = btn(str[i], a->{
				if(a.getSource().equals(jb[0])) {
					new Orderlist().addWindowListener(new be(this));
				}
				if(a.getSource().equals(jb[1])) {
					new Review().addWindowListener(new be(this));
				}
				if(a.getSource().equals(jb[2])) {
					new Manage().addWindowListener(new be(this));
				}
				if(a.getSource().equals(jb[3])) {
					new Steamed().addWindowListener(new be(this));
				}
			}));
		}
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Mypage();
	}

}
