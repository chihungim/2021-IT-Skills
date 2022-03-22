package view;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.sql.Timestamp;

import javax.swing.JPanel;

import additional.Util;
import db.DBManager;

public class MyPage extends BasePage {
	
	JPanel page;
	String bcap[] = "구매내역,정보수정".split(",");
	
	public MyPage() {
		super();
		this.add(page = new JPanel(new GridBagLayout()));
		page.setBackground(Color.white);
		
		var grid = new JPanel(new GridLayout(1, 0, 10, 5));
		page.add(grid);
		
		for (int i = 0; i < bcap.length; i++) {
			int idx=i;
			grid.add(Util.sz(Util.btn(bcap[i] , a->{
				if (a.getActionCommand().contentEquals(bcap[0])) {
					mf.addPage(new OrderLogPage());
				} else  {
					mf.addPage(new EditProfilePage());
				}
			}), 150, 150));
		}
		
		
	}
	
}
