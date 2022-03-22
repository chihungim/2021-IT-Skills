import java.awt.BorderLayout;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class Stage extends BaseFrame {

	Pew pew;
	PewBasket basket;
	int mP, cP, sum;
	String t_no;

	public Stage() {
		super("좌석", 1000, 800);
		BaseFrame.sumPrice = 0;
		var c = new JPanel(new BorderLayout());
		add(c);
		mP = 1;
		var title = size(lbl("Stage", 0, 30), 2, 80);
		c.add(title, "North");

		c.add(pew = new Pew());
		add(size(basket = new PewBasket(), 250, 1), "East");

		title.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
		setVisible(true);
	}

	public static void main(String[] args) {
		new Stage();
	}

	public Stage(String t_no) {
		this();
		this.t_no = t_no;
		try {
			var rs = stmt.executeQuery("select * from ticket t, perform p where t.p_no = p.p_no and t_no = " + t_no);
			if (rs.next()) {
				mP = rs.getString(4).split(",").length;
				for (int i = 0; i < basket.peopleCnt.length; i++) {
					basket.peopleCnt[i].setEnabled(false);
				}

				for (int i = 0; i < mP; i++) {
					basket.peopleCnt[i].setText("●");
				}

				String seats[] = rs.getString(4).split(",");
				String discounts[] = rs.getString(5).split(",");
				for (int i = 0; i < discounts.length; i++) {
					pew.pews.get(seats[i].substring(0, 1)).get(toInt(seats[i].substring(1)) - 1)
							.setBackground(Color.orange);
					var item = basket.addItem(seats[i]);
					if (toInt(discounts[i]) != 0)
						item.box[toInt(discounts[i]) - 1].setSelected(true);
					item.boxEvent();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	class Pew extends JPanel {

		String ABCDEF[] = "A,B,C,D,E,F".split(",");
		JLabel lbl[][] = new JLabel[6][10];
		HashMap<String, ArrayList<JLabel>> pews;

		public Pew() {
			setLayout(new BorderLayout());

			pews = new HashMap<>();
			var c = new JPanel(new GridLayout(0, 11, 5, 5));
			add(lbl("날짜:" + p_date, JLabel.RIGHT, 20), "North");
			add(c);

			for (int i = 0; i < lbl.length; i++) {
				c.add(lbl(ABCDEF[i], 0, 20));
				ArrayList<JLabel> line = new ArrayList<>();
				for (int j = 0; j < lbl[0].length; j++) {
					lbl[i][j] = lbl(j + 1 + "", JLabel.CENTER, 15);
					lbl[i][j].setOpaque(true);
					lbl[i][j].setName(ABCDEF[i] + String.format("%02d", j + 1));
					lbl[i][j].setBorder(new LineBorder(Color.BLACK));
					line.add(lbl[i][j]);
					lbl[i][j].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							var lbl = ((JLabel) e.getSource());

							if (lbl.getBackground() == Color.LIGHT_GRAY)
								return;

							if (lbl.getBackground() != Color.ORANGE) {
								cP++;
								if (cP > 5) {
									eMsg("더 이상 선택이 불가합니다.");
									lbl.setBackground(Stage.this.getBackground());
									cP--;
									return;
								}
								basket.addItem(lbl.getName());
								lbl.setBackground(Color.ORANGE);
								return;
							} else {
								cP--;
								lbl.setBackground(Stage.this.getBackground());
								basket.removeItem(lbl.getName());
							}
						}
					});
					c.add(lbl[i][j]);
				}
				pews.put(ABCDEF[i], line);
			}

			try {
				var rs = stmt.executeQuery("SELECT * FROM 2021전국.ticket where p_no = " + p_no);
				while (rs.next()) {
					for (String seat : rs.getString(4).split(",")) {
						System.out.println(seat);
						var l = pews.get(seat.substring(0, 1)).get(toInt(seat.substring(1)) - 1);
						l.setBackground(Color.LIGHT_GRAY);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	class PewBasket extends JPanel {

		JPanel m;
		JLabel peopleCnt[] = new JLabel[5];
		JPanel selectedPews;
		JLabel sumPrice;
		String bcap[] = "이전으로,다음으로".split(",");
		ArrayList<BasketItem> selList = new ArrayList<>();

		public PewBasket() {
			super(new BorderLayout(5, 5));

			var n = new JPanel(new BorderLayout());
			var c = new JPanel(new BorderLayout());
			var c_n = new JPanel(new FlowLayout(FlowLayout.LEFT));
			var c_s = new JPanel(new FlowLayout(FlowLayout.LEFT));
			var s = new JPanel(new GridLayout(1, 0, 5, 5));
			add(m = new JPanel(new BorderLayout()));

			m.add(n, "North");
			m.add(c);
			m.add(s, "South");

			n.add(lbl(p_name, JLabel.LEFT, 20), "North");
			n.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));

			c.add(c_n, "North");
			c.add(selectedPews = new JPanel(new FlowLayout(FlowLayout.CENTER)));
			c.add(BaseFrame.size(c_s, 1, 50), "South");

			c_n.add(lbl("인원수:", JLabel.LEFT));

			for (int i = 0; i < peopleCnt.length; i++) {
				peopleCnt[i] = lbl("○", JLabel.CENTER);
				peopleCnt[i].setName((i + 1) + "");
				peopleCnt[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (!((JLabel) (e.getSource())).isEnabled())
							return;

						for (int i = 0; i < peopleCnt.length; i++) {
							peopleCnt[i].setText("○");
						}

						mP = toInt(((JLabel) (e.getSource())).getName());

						for (int i = 0; i < toInt(((JLabel) (e.getSource())).getName()); i++) {
							peopleCnt[i].setText("●");
						}
						super.mousePressed(e);
					}
				});
				c_n.add(peopleCnt[i]);
			}

			for (int i = 0; i < mP; i++) {
				peopleCnt[i].setText("●");
			}

			c_s.add(sumPrice = new JLabel("총금액:" + sum, JLabel.LEFT));

			bcap[0] = isEdit ? "취소하기" : bcap[0];
			bcap[1] = isEdit ? "수정하기" : bcap[1];

			for (String bc : bcap) {
				s.add(btn(bc, a -> {
					if (a.getActionCommand().equals("이전으로") || a.getActionCommand().equals("취소하기")) {
						dispose();
					} else {
						if (selList.size() == 0) {
							eMsg("좌석을 선택해주세요.");
							return;
						}

						if (selList.size() != mP) {
							eMsg("인원수에 맞게 좌석을 선택해 주세요.");
							return;
						}

						String discounts = "";
						String seats = "";

						for (BasketItem bi : selList) {
							String discount = "";

							for (int i = 0; i < bi.box.length; i++) {
								if (bi.box[i].isSelected()) {
									discount = (i + 1) + "";
								}
							}

							if (discount.equals(""))
								discount = "0";

							if (discounts.equals("")) {
								discounts = discount;
							} else {
								discounts += "," + discount;
							}

							if (seats.equals("")) {
								seats = bi.snn;
							} else {
								seats += "," + bi.snn;
							}
						}
						if (a.getActionCommand().equals("수정하기")) {

							execute("update ticket set t_seat = '" + seats + "',t_discount = '" + discounts
									+ "' where t_no=" + t_no);

							dispose();
						} else {

							String seatsAndprice = "";

							for (BasketItem bi : selList) {
								if (seatsAndprice.equals("")) {
									seatsAndprice = bi.title.getText();
								} else {
									seatsAndprice += "'" + bi.title.getText();
								}
							}
							new Purchase(seatsAndprice, seats, discounts)
									.addWindowListener(bf = new before(Stage.this));
						}
					}
				}));
			}

			selectedPews.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLACK), "선택한 좌석"));
			m.setBorder(new EmptyBorder(5, 5, 5, 5));
			setBorder(new LineBorder(Color.BLACK));
		}

		BasketItem addItem(String selSeat) {
			sum = 0;
			var item = new BasketItem(selSeat);
			selectedPews.add(item);
			selList.add(item);

			for (BasketItem bk : selList) {
				sum += bk.price;
			}

			BaseFrame.sumPrice = sum;

			basket.sumPrice.setText("총금액 : " + new DecimalFormat("#,##0").format(sum));

			revalidate();
			repaint();
			return item;
		}

		void removeItem(String selSeat) {

			sum = 0;

			Iterator<BasketItem> itr = selList.iterator();

			while (itr.hasNext()) {
				String s = itr.next().getName();
				if (s.equals(selSeat)) {
					itr.remove();
				}
			}

			selectedPews.removeAll();
			selList.forEach(selectedPews::add);
			for (BasketItem bk : selList) {
				sum += bk.price;
			}

			BaseFrame.sumPrice = sum;

			basket.sumPrice.setText("총금액 : " + new DecimalFormat("#,##0").format(sum));

			revalidate();
			repaint();
		}

		class BasketItem extends JPanel {

			String seatName;
			int seatNo;
			int price;
			JCheckBox box[] = { new JCheckBox("청소년 할인 20%"), new JCheckBox("어린이 할인 40%"), new JCheckBox("장애인 할인 50%") };
			ButtonGroup bg = new ButtonGroup();
			JLabel title;
			JLabel exp = lbl("▼", 0, 15);
			String snn;

			public BasketItem(String snn) {
				setLayout(null);
				this.snn = snn;
				setName(snn);
				seatName = snn.substring(0, 1);
				seatNo = toInt(snn.substring(1));
				price = BaseFrame.price;
				exp.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						if (exp.getText().equals("▼")) {
							exp.setText("▲");
							BaseFrame.size(BasketItem.this, 220, 90);
							return;
						} else {
							exp.setText("▼");
							BaseFrame.size(BasketItem.this, 220, 20);
							return;
						}
					}
				});
				add(title = lbl(snn + " : " + new DecimalFormat("#,##0").format(BaseFrame.price), JLabel.CENTER));

				add(exp);

				for (int i = 0; i < box.length; i++) {
					add(box[i]);
					box[i].setBounds(10, (20 * (i + 1)), 120, 20);
					bg.add(box[i]);
					box[i].addActionListener(a -> {
						boxEvent();
					});
				}
				title.setBounds(60, 1, 100, 20);
				exp.setBounds(190, 0, 20, 20);

				BaseFrame.size(this, 220, 20);
				setBorder(new LineBorder(Color.BLACK));
			}

			void boxEvent() {
				if (box[0].isSelected()) {
					price = (int) (BaseFrame.price * 0.8);
				} else if (box[1].isSelected()) {
					price = (int) (BaseFrame.price * 0.6);
				} else {
					price = (int) (BaseFrame.price * 0.5);
				}
				title.setText(snn + " : " + new DecimalFormat("#,##0").format(price));

				sum = 0;

				for (BasketItem bk : selList) {
					sum += bk.price;
				}

				basket.sumPrice.setText("총금액 : " + new DecimalFormat("#,##0").format(sum));
			}
		}
	}

}
