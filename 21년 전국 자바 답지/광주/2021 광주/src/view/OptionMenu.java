package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class OptionMenu extends BaseDialog {

	JLabel img;
	JPanel c, c_c;
	JPanel s;
	JLabel count;
	JButton add;
	JScrollPane pane;
	int mno, totalHeight = 0, price = 0;
	boolean isHasOption = false;

	ArrayList<ArrayList<JRadioButton>> groupList = new ArrayList<>();

	public OptionMenu(Restaurant r, Restaurant.Item item) {
		super("주문표에 추가", 300, 700);
		this.price = item.price;
		this.mno = item.no;
		add(size(img = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/메뉴/" + item.no + ".png")
				.getScaledInstance(300, 350, Image.SCALE_SMOOTH))), 300, 350), "North");
		add(s = new JPanel(new FlowLayout()), "South");

		add(c = new JPanel(new BorderLayout()));
		c.add(lbl("<html><font face = \"맑은 고딕\"; size = \"5\";><b>" + item.name
				+ "</b><br></font><font face = \"맑은 고딕\"; size = \"3\";>" + item.description, JLabel.LEFT), "North");
		c.add(pane = new JScrollPane(c_c = new JPanel()));
		c_c.setLayout(new BoxLayout(c_c, BoxLayout.Y_AXIS));
		try {
			if (getOne("SELECT distinct title FROM eats.options where menu = " + item.no).equals("")) {
				JLabel lbl;
				c_c.add(lbl = lbl("옵션이 없는 상품입니다.", JLabel.CENTER, 15));

				lbl.setAlignmentX(CENTER_ALIGNMENT);
				lbl.setForeground(Color.red);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery("SELECT distinct title FROM eats.options where menu = " + item.no);

			ArrayList<Option> list = new ArrayList<>();

			while (rs.next()) {
				isHasOption = true;
				Option o = new Option(rs.getString(1));
				list.add(o);
				c_c.add(o);
			}

			list.forEach(a -> {
				a.setUp();
				groupList.add(a.buttonList);
			});

		} catch (SQLException e) {
			e.printStackTrace();
		}

		s.add(btn("-", a -> {
			if (toInt(count.getText().replaceAll(".*[^0-9^].*", "")) == 1)
				return;
			count.setText((toInt(count.getText()) - 1) + "");
			add.setText(count.getText() + "개를 주문표에 추가하기");
			revalidate();
		}));

		s.add(count = lbl("1", JLabel.CENTER));

		s.add(btn("+", a -> {
			if (toInt(count.getText().replaceAll(".*[^0-9^].*", "")) == 9)
				return;

			count.setText((toInt(count.getText()) + 1) + "");
			add.setText(count.getText() + "개를 주문표에 추가하기");
			revalidate();
		}));

		s.add(add = btn(count.getText().replaceAll(".*[^0-9^].*", "") + "개를 주문표에 추가하기", a -> {

			ArrayList<Object> list = new ArrayList<>();
			list.add(item.name);

			int optionalPrice = 0;
			int selectCount = 0;

			if (isHasOption) {
				for (int i = 0; i < groupList.size(); i++) {
					selectCount = 0;
					for (int j = 0; j < groupList.get(i).size(); j++) {
						if (groupList.get(i).get(j).isSelected()) {
							list.add(groupList.get(i).get(j).getText());
							list.add(groupList.get(i).get(j).getName());
							optionalPrice += toInt(groupList.get(i).get(j).getName());
							selectCount++;
						}
					}
					if (selectCount == 0) {
						eMsg("옵션을 모두 선택해주세요.");
						return;
					}
				}
			}

			list.add(toInt(count.getText()));
			int totalPrice = (price + optionalPrice) * toInt(count.getText());
			list.add(totalPrice);

			orderList.add(list);
			r.orderCnt.setText("주문표(" + orderList.size() + "개)");
			dispose();
		}));
		
		pane.setOpaque(false);
		img.setBorder(new LineBorder(Color.BLACK));
	}

	class Option extends JPanel {

		ArrayList<JRadioButton> buttonList = new ArrayList<JRadioButton>();
		ButtonGroup bg = new ButtonGroup();
		String title;

		JPanel c;

		public Option(String title) {
			setLayout(new BorderLayout());
			this.title = title;
			add(lbl(title, JLabel.LEFT, 15), "North");

			add(c = new JPanel(new GridLayout(0, 1)));
		}

		void setUp() {
			try {
				var rs = stmt.executeQuery(
						"select * from options where title = '" + title.replace("'", "\\'") + "' and menu =" + mno);

				while (rs.next()) {
					var tmp = new JPanel(new BorderLayout());
					JRadioButton btn = new JRadioButton(rs.getString(3));
					buttonList.add(btn);
					btn.setName(rs.getString(4));
					tmp.add(btn, "West");
					tmp.add(lbl((rs.getString(4).equals("0") ? "추가 비용 없음" : "+" + rs.getString(4)), JLabel.LEFT),
							"East");
					bg.add(btn);
					c.add(tmp);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
