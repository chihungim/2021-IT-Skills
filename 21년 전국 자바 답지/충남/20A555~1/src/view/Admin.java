package view;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Admin extends BaseFrame {
	
	String bcap[] = "회원 관리,지하철 이용 통계,로그아웃".split(",");
	
	public Admin() {
		super("관리자", 300, 300);
		this.setLayout(new GridLayout(0, 1));
		
		for (int i = 0; i < bcap.length; i++) {
			this.add(btn(bcap[i], a->{
				if (a.getActionCommand().equals(bcap[0])) {
					new UserManage().addWindowListener(new Before(this));
				} else if (a.getActionCommand().equals(bcap[1])) {
					new Chart().addWindowListener(new Before(this));
				} else {
					h.setLog(false);
					h.setVisible(true);
				}
			}));
		}
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				h.setVisible(true);
			}
		});
		
		this.setVisible(true);
	}
	
}
