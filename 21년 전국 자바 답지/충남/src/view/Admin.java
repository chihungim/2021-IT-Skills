package view;

import java.awt.GridLayout;
import java.util.Arrays;

public class Admin extends BaseFrame {
	
	String cap[] = "ȸ�� ����,����ö �̿� ���,�α׾ƿ�".split(",");
	
	public Admin() {
		super("������", 300, 300);
		
		this.setLayout(new GridLayout(0, 1));
		
		Arrays.stream(cap).forEach(i->{
			this.add(btn(i, a->{
				if (i.equals(cap[0])) {
					new UserManage().addWindowListener(new Before(this));
				} else if (i.equals(cap[1])) {
					new Chart().addWindowListener(new Before(this));
				} else {
					uno="";
					uname="";
					dispose();
				}
			}));
		});
		
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Admin();
	}
	
}
