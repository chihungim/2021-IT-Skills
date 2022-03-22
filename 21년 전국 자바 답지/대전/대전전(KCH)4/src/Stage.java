import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
	JPanel basket_c_c = new JPanel();

	public Stage() {
		super("좌석", 1200, 600);
		ui();
		setVisible(true);
	}

	public Stage(String t_no) {
		this();
		btn[0].setText("취소하기");
		btn[1].setText("수정하기");

		try {
			var rs = stmt.executeQuery("select * from ticket t, perform p where t.p_no = p.p_no and t_no = " + t_no);
			if (rs.next()) {
				mp = rs.getString(4).split(",").length;
				for (int i = 0; i < people.length; i++) {
					people[i].setEnabled(false);
				}

				for (int i = 0; i < mp; i++) {
					people[i].setText("●");
				}

				String seats[] = rs.getString(4).split(",");
				String discounts[] = rs.getString(5).split(",");

				for (int i = 0; i < discounts.length; i++) {

					var r = Arrays.binarySearch("A,B,C,D,E,F".split(","), seats[i].substring(0, 1));
					var c = toInt(seats[i].substring(1, 3)) - 1;
					if (!discounts[i].isEmpty())
						this.addItem(this.seats[r][c], toInt(discounts[i]));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void ui() {

		setLayout(new BorderLayout());

		var seatP = new JPanel(new BorderLayout());
		var seat_c = new JPanel(new BorderLayout());
		var seat_c_c = new JPanel(new GridLayout(0, 11, 5, 5));
		var basketP = new JPanel(new BorderLayout());
		var basket_c = new JPanel(new BorderLayout());
		var basket_c_n = new JPanel(new FlowLayout(FlowLayout.LEFT));
		var basket_s = new JPanel(new GridLayout(0, 2, 5, 5));

		add(seatP);
		add(size(basketP, 300, 1), "East");

		seatP.add(seat_c);
		seat_c.add(seat_c_c);
		basketP.add(basket_c);
		basketP.add(basket_s, "South");

		seatP.add(lbl("Stage", 0, 20), "North");
		seat_c.add(lbl(p_date, JLabel.RIGHT, 15), "North");

		for (int i = 0; i < seats.length; i++) {
			seat_c_c.add(lbl("A,B,C,D,E,F".split(",")[i], JLabel.CENTER, 15));
			for (int j = 0; j < seats[i].length; j++) {
				seat_c_c.add(seats[i][j] = new JLabel(j + 1 + "", JLabel.CENTER));
				seats[i][j].setBorder(new LineBorder(Color.BLACK));
				seats[i][j].setName("A,B,C,D,E,F".split(",")[i] + ":" + String.format("%02d", (j + 1)));
				System.out.println("A,B,C,D,E,F".split(",")[i] + ":" + String.format("%02d", (j + 1)));
				seats[i][j].setOpaque(true);
				seats[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						var me = (JLabel) e.getSource();
						if (me.getBackground().equals(Color.LIGHT_GRAY))
							return;

						if (!me.getBackground().equals(Color.ORANGE)) {
							addItem(me);
						} else if (me.getBackground().equals(Color.ORANGE)) {
							removeItem(me);
						}
					}
				});
			}
		}

		try {
			var rs = stmt.executeQuery("Select * from ticket where p_no=" + p_no);
			while (rs.next()) {
				for (var s : rs.getString(4).split(",")) {
					int r = Arrays.binarySearch("A,B,C,D,E,F".split(","), s.substring(0, 1));
					int c = toInt(s.substring(1)) - 1;
					seats[r][c].setBackground(Color.LIGHT_GRAY);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JLabel sTitle = null;

		basket_c.add(basket_c_n, "North");
		basket_c.add(basket_c_c);

		basketP.add(sTitle = lbl(p_name, JLabel.LEFT, 20), "North");
		sTitle.setBorder(new CompoundBorder(new MatteBorder(0, 0, 3, 0, Color.BLACK), new EmptyBorder(5, 5, 5, 5)));

		basket_c_n.add(lbl("인원수:", JLabel.LEFT));

		for (int i = 0; i < people.length; i++) {
			if (i == 0)
				people[i] = lbl("●", JLabel.CENTER);
			else
				people[i] = lbl("○", JLabel.CENTER);

			people[i].setName((i + 1) + "");
			people[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (!((JLabel) e.getSource()).isEnabled())
						return;

					for (var i = 0; i < people.length; i++) {
						people[i].setText("○");
					}

					mp = toInt(((JLabel) e.getSource()).getName());

					for (int i = 0; i < mp; i++) {
						people[i].setText("●");
					}

					super.mousePressed(e);
				}
			});

			basket_c_n.add(people[i]);
		}

		mp = 1;
		basket_c_c.setLayout(new BoxLayout(basket_c_c, BoxLayout.Y_AXIS));
		basket_c.add(sumlbl = lbl("총금액: " + sum, JLabel.LEFT), "South");
		basket_c_c.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "선택한 좌석"));
		int idx = 0;

		for (var bcap : "이전으로,다음으로".split(",")) {
			basket_s.add(btn[idx] = btn(bcap, a -> {
				switch (a.getActionCommand()) {
				case "이전으로":
				case "취소하기":
					break;

				default:
					break;
				}
			}));
			idx++;
		}

		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	protected void removeItem(JLabel me) {
		me.setBackground(this.getBackground());
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
		System.out.println(sum);
		sumlbl.setText("총금액:" + new DecimalFormat("#,##0").format(sum));
		me.setBackground(null);
		revalidate();
		repaint();

	}

	protected void addItem(JLabel me, int... discount) {
		if (basketList.size() == 5) {
			eMsg("더이상 선택이 불가능 합니다.");
			return;
		}

		me.setBackground(Color.ORANGE);

		var v = new BasketItem(me.getName());
		basketList.add(v);
		basket_c_c.add(v);
		if (discount.length > 0)
			v.box[discount[0]].setSelected(true);

		v.boxEvent();
		sum = 0;
		basketList.forEach(a -> sum += a.price);
		basketList.forEach(basket_c_c::add);
		sumlbl.setText("총금액:" + new DecimalFormat("#,##0").format(sum));

		revalidate();
		repaint();
	}

	class BasketItem extends JPanel {
		JCheckBox[] box = { new JCheckBox("청소년 할인 20%"), new JCheckBox("어린이 할인 40%"), new JCheckBox("장애인 할인 50%") };
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
						setMaximumSize(new Dimension(590, 90));
						return;
					} else {
						options.setText("▼");
						setMaximumSize(new Dimension(590, 20));
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

			setMaximumSize(new Dimension(590, 20));
			title.setBounds(80, 1, 100, 20);
			options.setBounds(200, 0, 20, 20);
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

	}

}
