package 광광주;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Search extends BaseFrame {

	String order = "";
	JComboBox<String> category = new JComboBox<>();
	PlaceHolderTextField searchField = new PlaceHolderTextField(20);
	PlaceHolderTextField txt[] = { new PlaceHolderTextField(15), new PlaceHolderTextField(15) };
	JRadioButton[] rbtn = { new JRadioButton("기본 정렬"), new JRadioButton("평점 정렬"), new JRadioButton("배달비용 정렬") };
	WestPanel c_w;
	ButtonGroup bg = new ButtonGroup();
	JScrollPane pane;
	int comboIdx = 0, totalHeight = 0;
	JPanel c_c;

	public Search(int idx) {
		super("검색", 1900, 700);
		comboIdx = idx;
		setLayout(new BorderLayout(5, 5));

		var c = new JPanel(new BorderLayout(5, 5));
		c_c = new JPanel();
		c_c.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		add(size(searchField, 0, 30), "North");
		searchField.setPlaceHolder("검색할 음식점 이름을 입력하세요.");

		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					search();
				}
				super.keyTyped(e);
			}
		});

		add(c);
		c.add(c_w = new WestPanel(), "West");
		c.add(pane = new JScrollPane(c_c));

		pane.setOpaque(false);
		c_c.setOpaque(false);
		c_c.setBorder(new EmptyBorder(5, 5, 5, 5));

		category.setSelectedIndex(comboIdx);

		search();

		c_w.setOpaque(false);
		c.setOpaque(false);
		c_c.setOpaque(false);

		pane.setBorder(new LineBorder(Color.lightGray));
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
		setVisible(true);
	}

	void search() {

		int price[] = new int[2];

		for (int i = 0; i < 2; i++) {
			if (txt[i].getText().matches(".*[0-9].*")) {
				price[i] = toInt(txt[i].getText());
			} else {
				if (txt[i].getText().equals("")) {
					if (i == 0)
						price[i] = 0;
					else
						price[i] = 2147483647;
				} else {
					eMsg("가격필터에는 숫자만 입력가능합니다.");
					return;
				}

			}
		}

		if (price[1] < price[0]) {
			eMsg("최저 가격이 최대 가격보다 클 수 없습니다.");
			return;
		}

		try {
			var rs = stmt.executeQuery(
					"SELECT sub2.sno, sub2.sname, minute(sub2.mtime), minute(sub2.mmtime), sub2.dfp, round(avg(r.rate),1) as `avg`  "
							+ "FROM "
							+ "(SELECT sub1.sno AS sno, sub1.sname AS sname, sub1.df as dfp, sub1.cate as cg,  "
							+ "	MIN(m.cooktime) AS mtime, MAX(m.cooktime) AS mmtime FROM "
							+ " menu m, (SELECT s.no AS sno, s.name AS sname,s.DELIVERYFEE as df, s.category as cate FROM seller s INNER JOIN category c where (true) "
							+ (searchField.getText().length() == 0 ? " "
									: " and s.name like '%" + searchField.getText() + "%' ")
							+ "	GROUP BY s.no) sub1  WHERE  sub1.sno = m.seller and m.price >= '" + price[0]
							+ "' and m.price <= '" + price[1]
							+ "' GROUP BY `sno`) AS sub2 LEFT OUTER JOIN review r ON sub2.sno = r.seller "
							+ "where (true) "
							+ (category.getSelectedIndex() != 0
									? " and sub2.cg = '" + category.getSelectedIndex() + "' "
									: "")
							+ " GROUP BY sub2.sno " + order);

			// select s2.no, s2.name, sub1.a from seller s2 left join (SELECT s.no, s.name,
			// avg(r.rate) a FROM seller s, menu m, review r where s.no = m.seller and
			// r.seller = s.no group by s.no) as sub1 on s2.no = sub1.no;

			if (!rs.next()) {
				return;
			}
			rs.previous();

			c_c.removeAll();
			while (rs.next()) {
				totalHeight += 220;
				Item i = new Item(rs.getInt(1), rs.getString(2),
						new DecimalFormat("#,##0").format(rs.getInt(5)) + "원 배달 수수료",
						rs.getString(3) + "~" + rs.getString(4) + "분", "평점" + rs.getDouble(6));
				c_c.add(i);
			}
			revalidate();
			repaint();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c_c.setPreferredSize(new Dimension(1500, totalHeight));
	}

	class Item extends JPanel {

		JLabel img;
		JPanel s;
		int no;
		String name;
		String rate;
		String time;
		String fee;

		public Item(int no, String name, String fee, String time, String rate) {
			this.no = no;
			this.name = name;
			this.rate = rate;
			this.time = time;
			this.fee = fee;
			setLayout(new BorderLayout());
			img = new JLabel(new ImageIcon(getImage("./지급자료/배경/" + no + ".png", 500, 200)));
			add(img);
			img.setPreferredSize(new Dimension(500, 200));
			add(s = new JPanel(new GridLayout(0, 1)), "South");
			s.add(lbl(name, JLabel.LEFT, 15));
			s.add(lbl(fee + "/" + time + "/" + rate, JLabel.LEFT));
			setOpaque(false);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new Restaurant(name, no, rate, fee, time).addWindowListener(new Before(Search.this));
				}
			});
		}
	}

	class WestPanel extends JPanel {

		String placeHolder[] = "최소 가격,최대 가격".split(",");

		public WestPanel() {
			setLayout(new FlowLayout());
			var c = Search.this.size(new JPanel(new GridLayout(0, 1, 5, 5)), (int) (1600 * 0.15), (int) (700 * 0.6));
			c.setOpaque(false);
			add(c);
			c.add(category);
			c.add(lbl("평점", JLabel.LEFT));
			for (int i = 0; i < 3; i++) {
				c.add(rbtn[i]);
				rbtn[i].addActionListener(a -> {
					if (((JRadioButton) a.getSource()).isSelected()) {
						if (a.getActionCommand().equals("기본 정렬")) {
							order = "";
						} else if (a.getActionCommand().equals("평점 정렬")) {
							order = "order by `avg` desc";
						} else {
							order = "order by sub2.dfp asc";
						}
					}
				});
				bg.add(rbtn[i]);

			}

			rbtn[0].setSelected(true);
			c.add(lbl("가격 필터", JLabel.LEFT));

			for (int i = 0; i < txt.length; i++) {
				c.add(txt[i]);
				txt[i].setPlaceHolder(placeHolder[i]);
			}

			c.add(btn("적용", a -> {
				search();
			}));

			setPreferredSize(new Dimension((int) (1600 * 0.16), (int) (700 * 0.7)));
			category.addItem("모두");
			try {
				var rs = stmt.executeQuery("Select * from category");
				while (rs.next()) {
					category.addItem(rs.getString(2));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			c.setBorder(new EmptyBorder(5, 5, 30, 5));

			setBorder(new LineBorder(Color.LIGHT_GRAY));
		}

	}

	public static void main(String[] args) {
		BaseFrame.uno = 3;
		new Search(1);
	}
}
