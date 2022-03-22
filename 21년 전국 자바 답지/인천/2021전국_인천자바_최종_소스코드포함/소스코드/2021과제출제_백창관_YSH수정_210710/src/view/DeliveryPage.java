package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import additional.Util;
import db.DBManager;
import view.OrderLogPage.ProductItem;

public class DeliveryPage extends BasePage {
	
	JPanel scr_p;
	JScrollPane scr = new JScrollPane();
	int count = 0;
	
	public DeliveryPage() {
		super();
		
		this.add(scr_p = new JPanel(new BorderLayout()));

		scr_p.add(Util.lbl(sname+"의 물품배송관리", 0, 25), "North");
		scr_p.add(scr = new JScrollPane(c = new JPanel(new GridLayout(0, 3, 10, 10))));

		scr_p.setOpaque(false);
		c.setBackground(Color.white);

		getNextPage();

		var scroll = scr.getVerticalScrollBar();
		scroll.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (scroll.getValue() != 0 && (scroll.getValue() + scroll.getHeight() == scroll.getMaximum())) {
					count += 9;
					getNextPage();
				}
			}
		});

		Util.setEmpty(this, 50, 50, 50, 50);
		
	}
	
	void getNextPage() {
		String sql = "select pu.pu_No, p.p_Name, u.u_Name, pu.pu_Count, pu.pu_Price, pu.pu_Date, ifnull(r.r_No, 0) from purchase pu inner join product p on p.p_No=pu.p_No inner join user u on u.u_No=pu.u_No left join receive r on r.pu_No=pu.pu_No where s_No=1 and r_No is null order by pu.pu_No";
		sql += " limit " + count + ", 9";
		try {
			var rs = DBManager.rs(sql);
			while (rs.next()) {
//				9 8 4 10 11 13 12 6 3
				c.add(new ProductItem(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(7)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.repaint();
		c.revalidate();
	}
	
	class ProductItem extends JPanel {

		String name;
		int puno;
		JPanel ce, w, s;
		DecimalFormat format = new DecimalFormat("#,##0");
		JButton delivery;

		public ProductItem(int puno, String name, String user, int stock, int price, int receive) {
			this.name = name;
			this.puno = puno;
			
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(300, 200));
			this.setBackground(Color.white);
			this.setBorder(new LineBorder(Color.black));

			this.add(w = new JPanel(), "West");
			this.add(ce = new JPanel(new FlowLayout(0)), "Center");
			this.add(s = new JPanel(new BorderLayout()), "South");
			w.setOpaque(false);
			ce.setOpaque(false);
			s.setOpaque(false);

			JLabel lbl;
			w.add(Util.sz(lbl = new JLabel(Util.img("./datafile/image/" + name + ".jpg", 130, 130)), 130, 130), "West");
			Util.setLine(lbl);

			ce.add(new JLabel("<html>" + name + "<br><br>구매자 : " + user + "<br><br>개수 : " + stock + "개<br><br>가격 : "
					+ format.format(price) + "원<br><br>", JLabel.LEFT));
			
		
			
			
			s.add(delivery=Util.btn("배달하기", e->{
				System.out.println(puno);
				DBManager.execute("insert into receive values(0, '"+puno+"', now())");
				Util.iMsg("배송을 시작합니다.");
				
				// 원래 repaint되서 없어져야됨
				c.removeAll();
				getNextPage();
			}), "East");
			
			
			Util.setEmpty(c, 0, 10, 0, 0);
			this.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(5, 5, 5, 5)));
			
		}
	}
	
}
