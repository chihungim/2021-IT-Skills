import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

public class Stage extends BaseFrame {

	JLabel seats[][] = new JLabel[6][10];
	JLabel people[] = new JLabel[5];
	JLabel sumlbl;
	ArrayList<BasketItem> basketList = new ArrayList<>();
	int sum = 0, mp;
	JButton btn[] = new JButton[2];

	public Stage() {
		super("좌석", 1000, 500);
		ui();
		setVisible(true);
	}

	public Stage(String string) {
		this();
		btn[0].setText("취소하기");
		btn[1].setText("이전으로");
		System.out.println("ㅇㅇ");
	}

	void ui() {
		setLayout(new BorderLayout(5, 5));
		// seat
		var seatP = new JPanel(new BorderLayout());
		var seat_c = new JPanel(new BorderLayout());
		var seat_c_c = new JPanel(new GridLayout(0, 11, 5, 5));

		var basketP = new JPanel(new BorderLayout());
		var basket_c = new JPanel(new BorderLayout());
		var basket_c_n = new JPanel(new FlowLayout(FlowLayout.LEFT));
		var basket_c_c = new JPanel();
		var basket_s = new JPanel(new GridLayout(0, 2, 5, 5));

		add(seatP);
		add(size(basketP, 200, 1), "East");

		seatP.add(seat_c);
		seat_c.add(seat_c_c);
		basketP.add(basket_c);
		basketP.add(basket_s, "South");

		JLabel title;

		// seat

		seatP.add(title = lbl("STAGE", 0, 20), "North");
		seat_c.add(lbl(p_date, JLabel.RIGHT, 15), "North");

		for (int i = 0; i < seats.length; i++) {
			seat_c_c.add(lbl("A,B,C,D,E,F".split(",")[i], JLabel.CENTER, 15));
			for (int j = 0; j < seats[i].length; j++) {
				seat_c_c.add(seats[i][j] = new JLabel(j + 1 + "", JLabel.CENTER));
				seats[i][j].setBorder(new LineBorder(Color.BLACK));
				seats[i][j].setName("A,B,C,D,E,F".split(",")[i] + ":" + String.format("%02d", (j + 1)));
				seats[i][j].setOpaque(true);
				seats[i][j].addMouseListener(new MouseAdapter() {
					boolean isClicked;

					@Override
					public void mousePressed(MouseEvent e) {

						var me = (JLabel) (e.getSource());
						if (!isClicked) {

							if (basketList.size() == 5) {
								eMsg("더이상 선택이 불가능 합니다.");
								return;
							}

							isClicked = true;
							me.setBackground(Color.ORANGE);
							basketList.add(new BasketItem(me.getName()));
							sum = 0;
							basketList.forEach(a -> sum += a.price);
							basketList.forEach(basket_c_c::add);
							sumlbl.setText("총금액:" + new DecimalFormat("#,##0").format(sum));
							revalidate();
							repaint();
							return;
						} else {
							isClicked = false;

							Iterator<BasketItem> itr = basketList.iterator();
							while (itr.hasNext()) {
								if (itr.next().seat.equals(me.getName())) {
									itr.remove();
								}
							}
							basket_c_c.removeAll();
							sum = 0;
							basketList.forEach(a -> sum += a.price);
							basketList.forEach(basket_c_c::add);
							sumlbl.setText("총금액:" + new DecimalFormat("#,##0").format(sum));
							me.setBackground(null);
							revalidate();
							repaint();
							return;
						}
					}
				});
			}
		}

		// 장바구니
		JLabel sTitle = null;

		basket_c.add(basket_c_n, "North");
		basket_c.add(basket_c_c);

		basketP.add(sTitle = lbl(p_name, JLabel.LEFT, 20), "North");
		sTitle.setBorder(new CompoundBorder(new MatteBorder(0, 0, 3, 0, Color.BLACK), new EmptyBorder(5, 5, 5, 5)));

		basket_c_n.add(lbl("인원수:", JLabel.LEFT));

		for (int i = 0; i < people.length; i++) {
			people[i] = lbl("○", JLabel.CENTER);
			people[i].setName((i + 1) + "");
			people[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (!((JLabel) (e.getSource())).isEnabled())
						return;

					for (int i = 0; i < people.length; i++) {
						people[i].setText("○");
					}

					mp = toInt(((JLabel) (e.getSource())).getName());

					for (int i = 0; i < toInt(((JLabel) (e.getSource())).getName()); i++) {
						people[i].setText("●");
					}
					super.mousePressed(e);
				}
			});

			basket_c_n.add(people[i]);
		}
		basket_c_c.setLayout(new BoxLayout(basket_c_c, BoxLayout.Y_AXIS));
		basket_c.add(sumlbl = lbl("총금액: " + sum, JLabel.LEFT), "South");
		basket_c_c.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "선택한 좌석"));
		title.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
		int idx = 0;
		for (var bcap : "이전으로,다음으로".split(",")) {
			basket_s.add(btn[idx] = btn(bcap, a -> {

			}));
			idx++;
		}

	}

	class BasketItem extends JPanel {

		JCheckBox box[] = { new JCheckBox("청소년 할인 20%"), new JCheckBox("어린이 할인 40%"), new JCheckBox("장애인 할인 50%") };
		ButtonGroup bg = new ButtonGroup();
		JLabel options = lbl("▼", 0, 15);
		JLabel title;
		String seat;
		int price = BaseFrame.price;

		public BasketItem(String seat) {
			super(null);
			this.seat = seat;
			add(title = lbl(seat + " : " + new DecimalFormat("#,##0").format(BaseFrame.price), JLabel.CENTER));
			add(options);
			options.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (options.getText().equals("▼")) {
						options.setText("▲");
						setMaximumSize(new Dimension(400, 90));
						return;
					} else {
						options.setText("▼");
						setMaximumSize(new Dimension(400, 20));
						return;
					}
				}
			});

			for (int i = 0; i < box.length; i++) {
				add(box[i]);
				box[i].setBounds(10, (20 * (i + 1)), 120, 20);
				bg.add(box[i]);
				box[i].addActionListener(a -> boxEvent());
			}

			setMaximumSize(new Dimension(400, 20));
			title.setBounds(25, 1, 100, 20);
			options.setBounds(160, 0, 20, 20);
			setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2), new LineBorder(Color.BLACK)));
		}

		void boxEvent() {
			if (box[0].isSelected()) {
				price = (int) (BaseFrame.price * 0.8);
			} else if (box[1].isSelected()) {
				price = (int) (BaseFrame.price * 0.6);
			} else {
				price = (int) (BaseFrame.price * 0.5);
			}
			title.setText(seat + " : " + new DecimalFormat("#,##0").format(price));

			sum = 0;

			basketList.forEach(a -> {
				sum += a.price;
			});

			sumlbl.setText("총금액:" + new DecimalFormat("#,##0").format(sum));
		}

		// 디버깅용
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return seat;
		}
	}

}
