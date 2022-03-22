package view;

import java.awt.GridLayout;

import view.BaseFrame.Before;

public class Admin extends BaseFrame {

	String bcap[] = "회원 관리,지하철 이용 통계,로그아웃".split(",");

	public Admin() {
		super("관리자", 300, 300);

		this.setLayout(new GridLayout(0, 1));

		for (int i = 0; i < bcap.length; i++) {
			this.add(btn(bcap[i], a -> {
				if (a.getActionCommand().contentEquals(bcap[0])) {
					new UserManage().addWindowListener(new Before(this));
				} else if (a.getActionCommand().contentEquals(bcap[1])) {
					new Statistics().addWindowListener(new Before(this));
				} else {
					dispose();
					new Main();
				}
			}));
		}

		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Admin();
	}

}
