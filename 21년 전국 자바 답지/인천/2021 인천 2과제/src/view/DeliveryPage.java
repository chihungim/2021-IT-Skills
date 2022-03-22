package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import view.DeliveryPage.ProductItem;

public class DeliveryPage extends BasePage {
	JPanel scr_p;
	JScrollPane pane;
	int cnt = 0;

	ArrayList<ProductItem> items = new ArrayList<>();

	public DeliveryPage() {
		data();
		ui();
		events();
	}

	void data() {
		try {
			System.out.println(
					"select pu.pu_No, p.p_Name, u.u_Name, pu.pu_Count, pu.pu_Price, pu.pu_Date, ifnull(r.r_No, 0) from purchase pu inner join product p on p.p_No=pu.p_No inner join user u on u.u_No=pu.u_No left join receive r on r.pu_No=pu.pu_No where s_No="
							+ sno + " and r_No is null order by pu.pu_No");
			var rs = rs(
					"select pu.pu_No, p.p_Name, u.u_Name, pu.pu_Count, pu.pu_Price, pu.pu_Date, ifnull(r.r_No, 0) from purchase pu inner join product p on p.p_No=pu.p_No inner join user u on u.u_No=pu.u_No left join receive r on r.pu_No=pu.pu_No where s_No="
							+ sno + " and r_No is null order by pu.pu_No");
			while (rs.next()) {
				items.add(new ProductItem(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
						rs.getInt(7)));
				System.out.println("ㅇㅇ");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void events() {
		var scroll = pane.getVerticalScrollBar();
		scroll.addAdjustmentListener(a -> {
			if (scroll.getValue() != 0 && (scroll.getValue() + scroll.getHeight() == scroll.getMaximum())) {
				cnt += 6;
				setRequests();
			}
		});
	}

	void setRequests() {
		DecimalFormat format = new DecimalFormat("#,##0");
		for (int cnt = this.cnt; cnt < this.cnt + 6; cnt++) {
			if (cnt > items.size() - 1) {
				return;
			}
			var item = items.get(cnt);
			var itemP = setBorder(sz(new JPanel(new BorderLayout()), 300, 200), new LineBorder(Color.BLACK));
			var w = new JPanel();
			var c_e = new JPanel(new FlowLayout(0));
			var s = new JPanel(new BorderLayout());
			itemP.add(w, "West");
			itemP.add(c_e, "Center");
			itemP.add(s, "South");

			w.add(setBorder(sz(new JLabel(getImage("./datafile/image/" + item.name + ".jpg", 130, 130)), 130, 130),
					new LineBorder(Color.BLACK)), "West");

			c_e.add(new JLabel("<html>" + item.name + "<br><br>구매자 : " + item.user + "<br><br>개수 : " + item.stock
					+ "개<br><br>가격 : " + format.format(item.price) + "원<br><br>", JLabel.LEFT));
			s.add(btn("배달하기", a -> {
				execute("insert into receive values(0, '" + item.puno + "', now())");
				iMsg("배송을 시작합니다.");
				scr_p.removeAll();
				this.cnt = 0;
				setRequests();
			}), "East");
			itemP.setBorder(new LineBorder(Color.BLACK));
			scr_p.add(itemP);
			itemP.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(5, 5, 5, 5)));
		}
	}

	void ui() {
		add(lbl(sname + "의 물품배송관리", 0, 25), "North");

		add(pane = new JScrollPane(scr_p = new JPanel(new GridLayout(0, 3, 10, 10))));
		pane.getViewport().setOpaque(false);
		setRequests();
		setBorder(this, new EmptyBorder(50, 50, 50, 50));
	}

	class ProductItem {
		String name, user;
		int puno, stock, price, receive;

		public ProductItem(int puno, String name, String user, int stock, int price, int receive) {
			this.name = name;
			this.user = user;
			this.puno = puno;
			this.stock = stock;
			this.price = price;
			this.receive = receive;
		}
	}
}
