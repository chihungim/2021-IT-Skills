package 광광주;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import 광광주.BaseFrame.Before;

public class OrderTable extends BaseFrame {

	JScrollPane pane;
	JPanel c_c, c;
	JPanel s, s_n;
	JLabel totaPriceLabel;
	int totalPrice = 0;

	Restaurant r;

	public OrderTable(Restaurant r) {
		super("주문표", 300, 400);

		this.r = r;

		add(lbl("주문표", JLabel.LEFT, 20), "North");
		add(c = new JPanel(new BorderLayout()));
		c.add(pane = new JScrollPane(c_c = new JPanel(new FlowLayout())));
		add(s = new JPanel(new BorderLayout()), "South");
		s.add(s_n = new JPanel(new BorderLayout()), "North");
		s.add(btn("주문하기", a -> {
			if (orderList.size() == 0) {
				eMsg("주문표에 담긴 메뉴가 없습니다.");
				return;
			}

			new Purchase(totalPrice, r).addWindowListener(new Before(this));
		}), "South");

		s_n.add(totaPriceLabel = lbl(df.format(totalPrice), JLabel.RIGHT), "East");

		setLists();

		s_n.add(lbl("최종금액", JLabel.LEFT, 10), "West");

		pane.setBorder(BorderFactory.createEmptyBorder());
		c_c.setBorder(new EmptyBorder(20, 5, 20, 5));
		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));
		setVisible(true);
	}

	class Order extends JPanel {

		JPanel c, s;
		JButton delete;

		public Order(int key, ArrayList<Object> obj) {
			System.out.println(key);
			setLayout(new BorderLayout());
			add(BaseFrame.lbl(obj.get(0).toString(), JLabel.LEFT, 10), "North");
			add(c = new JPanel());
			add(s = new JPanel(new BorderLayout()), "South");

			for (int i = 1; i < obj.size() - 2; i++) {
				if (i % 2 == 0)
					continue;

				c.add(lbl(obj.get(i).toString(), JLabel.LEFT));
			}

			s.add(lbl(obj.get(obj.size() - 2).toString() + "개, " + df.format(toInt(obj.get(obj.size() - 1).toString()))
					+ "원", JLabel.LEFT), "West");
			s.add(delete = btn("삭제하기", a -> {
				r.orderCount
						.setText("주문표(" + (BaseFrame.toInt(r.orderCount.getText().replaceAll("[^0-9]", "")) - 1) + ")");
				removeList(toInt(((JButton) a.getSource()).getName()));
			}), "East");

			delete.setOpaque(false);
			delete.setBorderPainted(false);
			delete.setFocusPainted(false);
			delete.setForeground(Color.BLACK);

			delete.setName(key + "");

			c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

			setPreferredSize(new Dimension(260, 80));

			setBorder(new LineBorder(Color.LIGHT_GRAY));
		}
	}

	void setLists() {
		c_c.removeAll();
		totalPrice = 0;
		int height = 0;

		for (int i = 0; i < orderList.size(); i++) {
			c_c.add(new Order(i, orderList.get(i)));
			totalPrice += (int) orderList.get(i).get(orderList.get(i).size() - 1);
			height += 80;
		}

		totaPriceLabel.setText(df.format(totalPrice));
		c_c.setPreferredSize(new Dimension(270, height + 60));
		revalidate();
		repaint();
	}

	public void removeList(int no) {
		orderList.remove(no);
		setLists();
	}
}
