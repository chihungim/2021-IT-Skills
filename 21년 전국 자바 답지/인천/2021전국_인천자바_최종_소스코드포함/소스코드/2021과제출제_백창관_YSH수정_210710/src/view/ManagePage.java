package view;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import javax.swing.JPanel;

import additional.Util;
import db.DBManager;

public class ManagePage extends BasePage {
	
	JPanel page;
	String bcap[] = "배송관리,배송현황,통계".split(",");
	
	public ManagePage() {
		super();
		this.add(page = new JPanel(new GridBagLayout()));
		page.setBackground(Color.white);
		
		var grid = new JPanel(new GridLayout(1, 0, 10, 5));
		page.add(grid);
		
		for (int i = 0; i < bcap.length; i++) {
			int idx=i;
			grid.add(Util.sz(Util.btn(bcap[i] , a->{
				System.out.println(a.getActionCommand());
				if (a.getActionCommand().equals(bcap[0])) {
					mf.addPage(new DeliveryPage());
				} else if (a.getActionCommand().equals(bcap[1])) {
//					new MapFrame("seller", LocalDateTime.now()).setVisible(true);
					new MapFrame("seller").setVisible(true);
				} else if (a.getActionCommand().equals(bcap[2])) {
					mf.addPage(new ChartPage());
				}
			}), 150, 150));
		}
	}
	
	public static void main(String[] args) {
		sno = 1;
		sname = "이름1";
		s_addr = 59;
		
		ManagePage mp = new ManagePage();
		mf.setVisible(true);
		mf.addPage(mp);
	}
}
