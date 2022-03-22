package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Search extends BaseDialog {

	PlaceHolderField sfield = new PlaceHolderField("검색할 음식점 이름을 입력하세요.");
	PlaceHolderField pfields[] = { new PlaceHolderField("최소가격"), new PlaceHolderField("최대가격") };
	JComboBox<String> box = new JComboBox<>();
	JRadioButton aligs[] = { new JRadioButton("기본 정렬"), new JRadioButton("평점 정렬"), new JRadioButton("배달비용 정렬") };
	ButtonGroup bg = new ButtonGroup();
	String alig = "";
	JPanel c_c;
	JScrollPane pane;

	public Search() {
		super("검색", 1500, 700);
		ui();
		search();
		setVisible(true);
	}

	void ui() {
		setLayout(new BorderLayout(5, 5));
		var c = new JPanel(new BorderLayout(5, 5));
		var c_w = new JPanel(new GridLayout(0, 1, 5, 5));
		c_c = new JPanel();

		add(size(sfield, 1, 30), "North");

		add(c);
		c.add(size(c_w, 250, 1), "West");
		c_c.setLayout(new BoxLayout(c_c, BoxLayout.Y_AXIS));
		c.add(pane = new JScrollPane(c_c));

		c_w.add(box);
		c_w.add(lbl("평점", JLabel.LEFT));

		Arrays.stream(aligs).forEach(a -> {
			a.addItemListener(i -> {
				if (((JRadioButton) i.getSource()).isSelected() == true) {
					if (i.getSource().equals(aligs[0])) {
						alig = "";
					}
					if (i.getSource().equals(aligs[1])) {
						alig = " order by ROUND(AVG(r.RATE), 1) desc";
					}
					if (i.getSource().equals(aligs[2])) {
						alig = " order by s.DELIVERYFEE asc";
					}
				}
			});
			bg.add(a);
			a.setOpaque(false);
			c_w.add(a);
		});

		c_w.setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(5, 5, 250, 5)));
		c_w.add(lbl("가격 필터", JLabel.LEFT));

		Arrays.stream(pfields).forEach(a -> {
			a.setOpaque(false);
			c_w.add(a);
		});

		c_w.add(btn("검색", a -> search()));

		box.addItem("모두");

		try {
			var rs = stmt.executeQuery("select * from category");
			while (rs.next()) {
				box.addItem(rs.getString("name"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		pane.setBorder(BorderFactory.createEmptyBorder());
		pane.getViewport().setOpaque(false);
		((javax.swing.JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	void search() {
		for (int i = 0; i < pfields.length; i++) {
			if (pfields[i].getText().matches(".*[^0-9].*") && !pfields[i].getText().equals("")) {
				eMsg("가격필터에는 숫자만 입력가능합니다.");
				return;
			}
		}

		if (toInt(pfields[0].getText()) > toInt(pfields[1].getText()) && !pfields[0].getText().isEmpty()
				&& !pfields[1].getText().isEmpty()) {
			eMsg("최저가격이 최대 가격보다 클 수 없습니다.");
			return;
		}

		c_c.removeAll();

		String sql = "SELECT \r\n" + "    s.NO,\r\n" + "    s.name,\r\n" + "    c.name,\r\n"
				+ "    format(s.DELIVERYFEE,'#,##0'),\r\n" + "    MIN(MINUTE(m.cooktime)),\r\n"
				+ "    MAX(MINUTE(m.cooktime)),\r\n" + "    ROUND(AVG(r.RATE), 1)\r\n" + "FROM\r\n" + "    seller s\r\n"
				+ "        INNER JOIN\r\n" + "    category c ON s.CATEGORY = c.NO\r\n" + "        LEFT OUTER JOIN\r\n"
				+ "    review r ON s.NO = r.SELLER\r\n" + "        INNER JOIN\r\n" + "    menu m ON s.NO = m.SELLER\r\n"
				+ "where\r\n" + "	s.name like '%" + sfield.getText() + "%' \r\n" + "    and ("
				+ (box.getSelectedIndex() == 0 ? "true" : "c.No = " + box.getSelectedIndex()) + ") \r\n"
				+ "    and  m.price >= '" + (pfields[0].getText().isEmpty() ? "0" : pfields[0].getText())
				+ "' and m.price <= '" + (pfields[1].getText().isEmpty() ? "100000000" : pfields[1].getText()) + "'\r\n"
				+ "GROUP BY s.no " + alig;

		try {
			var rs = stmt.executeQuery(sql);

			var b = Box.createHorizontalBox();

			while (rs.next()) {

				var p = new JPanel(new BorderLayout());
				var img = new JLabel(getIcon("./지급자료/배경/" + rs.getInt(1) + ".png", 400, 180));
				if (!new File("./지급자료/배경/" + rs.getInt(1) + ".png").exists()) {
					img.setOpaque(true);
					img.setBackground(Color.LIGHT_GRAY);
				}
				p.add(size(img, 350, 180));
				p.setMaximumSize(new Dimension(400, 200));
				p.add(new JLabel("<html><strong>" + rs.getString(2) + "</strong>" + "<br>" + rs.getString(4)
						+ "원 배달 수수료 / " + rs.getInt(5) + "분 ~ " + rs.getInt(6) + "분 / 평점 " + rs.getDouble(7)), "South");
				b.add(Box.createHorizontalStrut(5));
				b.add(p);
				b.setAlignmentX(LEFT_ALIGNMENT);
				b.setAlignmentY(TOP_ALIGNMENT);
				if (b.getComponents().length == 6) {
					c_c.add(b);
					b = Box.createHorizontalBox();
				}
				final String sname = rs.getString(2), rate = "평점 " + rs.getString(6),
						fee = rs.getString(4) + "원 배달 수수료", time = rs.getInt(5) + " ~ " + rs.getInt(6) + "분";

				final int sno = rs.getInt(1);
				p.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (e.getClickCount() == 2) {
							new Restaurant(sname, sno, rate, fee, time).addWindowListener(new Before(Search.this));
						}
					}
				});
			}

			if (b.getComponents().length > 0) {
				c_c.add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		repaint();
		revalidate();

	}

	public static void main(String[] args) {
		new Search();
	}
}
