package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class OrderLogPage extends BasePage {

	JScrollPane pane;
	JPanel scr_p;
	int cnt;
	ArrayList<ProductItem> items = new ArrayList<>();

	public OrderLogPage() {
		data();
		ui();
		events();
	}

	void events() {
		var scroll = pane.getVerticalScrollBar();
		scroll.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (scroll.getValue() != 0 && (scroll.getValue() + scroll.getHeight() == scroll.getMaximum())) {
					cnt += 9;
					setLogs();
				}
			}
		});
	}

	void data() {
		String sql = "SELECT pu.pu_No, s.s_No, s.s_Addr, s.s_Name, u.u_No, u.u_Addr, p.p_No, c.c_Name, p.p_Name, pu.pu_Count, pu.pu_Price, pu_Date, ifnull(r.r_No, 0), ifnull(r.r_Time, '1111-11-11 00:00:00') FROM purchase pu left join receive r on r.pu_No=pu.pu_No inner join seller s on s.s_No=pu.s_No inner join user u on u.u_No=pu.u_No inner join product p on p.p_No=pu.p_No inner join category c on c.c_No=p.c_No where u.u_No=1 order by pu_Date asc";
		try {
			var rs = rs(sql);
			while (rs.next()) {
				items.add(new ProductItem(rs.getString(9), rs.getString(8), rs.getString(4), rs.getInt(10),
						rs.getInt(11), rs.getInt(13), rs.getString(12), rs.getInt(6), rs.getInt(3),
						LocalDateTime.parse(rs.getString(14), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
						rs.getInt(3), rs.getInt(1)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void ui() {
		add(lbl(uname + "님의 구매목록", JLabel.CENTER, 25), "North");
		add(pane = new JScrollPane(scr_p = new JPanel(new GridLayout(0, 3, 5, 5))));
		pane.getViewport().setBackground(Color.WHITE);
		pane.setOpaque(false);
		setLogs();
		setBorder(this, new EmptyBorder(50, 50, 50, 50));

	};

	void setLogs() {
		DecimalFormat format = new DecimalFormat("#,##0");
		for (var cnt = this.cnt; cnt < this.cnt + 9; cnt++) {
			if (cnt >= items.size()) {
				return;
			}
			var item = items.get(cnt);
			var itemP = setBorder(sz(new JPanel(new BorderLayout()), 300, 200),
					new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(5, 5, 5, 5)));

			var w = new JPanel();
			var c = setBorder(new JPanel(new FlowLayout(FlowLayout.LEFT)), new EmptyBorder(0, 10, 0, 0));
			var s = new JPanel(new BorderLayout());

			itemP.add(w, "West");
			itemP.add(c);
			itemP.add(s, "South");

			w.add(setBorder(sz(new JLabel(getImage("./datafile/image/" + item.name + ".jpg", 130, 130)), 130, 130),
					new LineBorder(Color.BLACK)), "West");
			c.add(new JLabel("<html>" + item.name + "<br><br>판매자 : " + item.seller + "<br><br>개수 :" + item.stock
					+ "개<br><br>가격 : " + format.format(item.price) + "원<br><br>", JLabel.LEFT));
			String state = "배달 " + (item.receive == 0 ? "준비" : "현황");

			JButton delivery;
			s.add(delivery = btn(state, a -> {
				sname = item.name;
				startTime = item.a;
				s_addr = item.saddr;
				new Map("user").setVisible(true);
			}), "East");

			delivery.setEnabled(item.receive == 0 ? false : true);
			scr_p.add(itemP);
		}
	}

	class ProductItem {
		String name, category, seller, date;
		int stock, price, receive, sNode, eNode, saddr, puno;
		LocalDateTime a;

		public ProductItem(String name, String category, String seller, int stock, int price, int receive, String date,
				int sNode, int eNode, LocalDateTime a, int saddr, int puno) {

			this.name = name;
			this.category = category;
			this.seller = seller;
			this.stock = stock;
			this.price = price;
			this.receive = receive;
			this.date = date;
			this.sNode = sNode;
			this.eNode = eNode;
			this.a = a;
			this.saddr = saddr;
			this.puno = puno;
		}
	}

}
