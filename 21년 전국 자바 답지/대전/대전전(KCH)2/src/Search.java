import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class Search extends BaseFrame {

	JMenuBar bar;
	JMenu menu;

	String type = "전체";
	String items[] = "전체,뮤지컬,오페라,콘서트".split(",");
	JTextField searchField;

	DefaultTableModel m = model("p_no,공연날짜,공연명,공연가격".split(","));
	JTable t = table(m);
	Imgs images;
	JPanel c_c;

	public Search() {
		super("검색", 600, 500);
		setJMenuBar(bar = new JMenuBar());
		bar.add(menu = new JMenu("분류"));
		for (String it : items) {
			JMenuItem item = new JMenuItem(it);
			item.addActionListener(a -> {
				if (a.getActionCommand().equals("전체")) {
					type = "전체";
					search();
				} else if (a.getActionCommand().equals("뮤지컬")) {
					type = "M";
					search();
				} else if (a.getActionCommand().equals("오페라")) {
					type = "O";
					search();
				} else {
					type = "C";
					search();
				}
			});
			menu.add(item);
		}

		var n = new JPanel(new BorderLayout());
		var n_c = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		var n_s = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		var c = new JPanel(new GridLayout(1, 0));

		add(n, "North");
		n.add(n_c);
		n.add(n_s, "South");
		add(c);

		n_c.add(lbl("공연명:", 0));
		n_c.add(searchField = new JTextField(15));
		n_c.add(btn("검색", a -> {
			c_c.removeAll();
			c_c.add(new Imgs());
			repaint();
			revalidate();
		}));

		n_s.add(lbl("현재 예매 가능 공연", JLabel.RIGHT, 15));

		c.add(c_c = new JPanel());
		c_c.add(new Imgs());
		c.add(new JScrollPane(t));

		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (t.getSelectedRow() == -1)
					return;

				var no = t.getValueAt(t.getSelectedRow(), 0).toString();
				new Reserve(no).addWindowListener(new before(Search.this));
				super.mousePressed(e);
			}
		});

		t.getColumnModel().getColumn(0).setMinWidth(0);
		t.getColumnModel().getColumn(0).setMaxWidth(0);

		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
		setVisible(true);
	}

	void search() {
		c_c.removeAll();
		c_c.add(new Imgs());
		repaint();
		revalidate();
	}

	class Imgs extends JPanel {

		JLabel type;
		CardLayout page;
		int pageCount = 1, pageIdx = 1;

		JLabel next, prev, first, last;

		public Imgs() {

			super(new BorderLayout());

			var n = new JPanel(new BorderLayout());
			var c = new JPanel(new BorderLayout());
			var c_c = new JPanel(page = new CardLayout());
			var c_s = new JPanel(new FlowLayout());
			var s = new JPanel(new FlowLayout());

			switch (Search.this.type) {
			case "M":
				n.add(type = lbl("분류: 뮤지컬", JLabel.RIGHT, 15));
				break;
			case "C":
				n.add(type = lbl("분류: 콘서트", JLabel.RIGHT, 15));
				break;
			case "O":
				n.add(type = lbl("분류: 오페라", JLabel.RIGHT, 15));
				break;
			default:
				n.add(type = lbl("분류: 전체", JLabel.RIGHT, 15));
				break;

			}

			add(n, "North");
			add(c);
			c.add(c_c);
			c.add(prev = lbl("◀", JLabel.LEFT, 30), "West");
			c.add(next = lbl("▶", JLabel.RIGHT, 30), "East");
			c.add(c_s, "South");
			add(s, "South");

			dtcr.setHorizontalAlignment(SwingConstants.CENTER);

			addrow(m, "select p_no,p_date ,p_name,format(p_price, '#,##0')  from perform where p_date >= curdate() "
					+ (Search.this.type.equals("전체") ? "" : "and left(pf_no, 1) = '" + Search.this.type + "' ")
					+ (!searchField.getText().equals("") ? "and p_name like '%" + searchField.getText() + "%'" : "")
					+ "group by p_name order by p_date asc");

			try {
				var rs = stmt
						.executeQuery("select pf_no ,p_name,p_date,p_actor,p_no from perform where p_date >= curdate() "
								+ (Search.this.type.equals("전체") ? ""
										: "and left(pf_no, 1) = '" + Search.this.type + "' ")
								+ (!searchField.getText().equals("")
										? "and p_name like '%" + searchField.getText() + "%'"
										: "")
								+ "group by p_name order by p_date asc");
				var tmp = new JPanel(new GridLayout(0, 2, 2, 2));
				int idx = 0;
				if (!rs.next()) {
					iMsg("검색 결과가 없습니다.");
					return;
				}

				while (rs.next()) {

					if (idx != 0 && idx % 4 == 0) {
						c_c.add(tmp, pageCount + "");

						tmp = new JPanel(new GridLayout(0, 2, 2, 2));
						pageCount++;
					}

					JLabel img = new JLabel(new ImageIcon(
							Toolkit.getDefaultToolkit().getImage("./Datafiles/공연사진/" + rs.getString(1) + ".jpg")
									.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
					img.setName(rs.getString(5));

					img.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							new Reserve(((JLabel) e.getSource()).getName()).addWindowListener(new before(Search.this));
							super.mouseClicked(e);
						}
					});

					img.setBorder(new LineBorder(Color.BLACK));
					img.setToolTipText(rs.getString(4) + "/" + rs.getString(3));
					tmp.add(img);
					idx++;
				}

				if (idx % 4 > 0) {
					for (int i = 0; i < 4 - idx % 4; i++) {
						JLabel l = new JLabel();
						l.setBorder(new LineBorder(Color.BLACK));
						l.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								eMsg("공연 정보가 없습니다.");
							}
						});
						tmp.add(l);
					}
					c_c.add(tmp, pageCount + "");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			JLabel lbl[] = new JLabel[pageCount];

			for (int i = 0; i < lbl.length; i++) {
				lbl[i] = lbl("●", JLabel.CENTER, 10);
				c_s.add(lbl[i]);
				lbl[i].setName((i + 1) + "");

				lbl[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						for (int i = 0; i < lbl.length; i++) {
							lbl[i].setForeground(Color.BLACK);
						}

						var 뷁 = ((JLabel) (e.getSource()));
						뷁.setForeground(Color.RED);
						page.show(c_c, 뷁.getName());
					}
				});
			}

			lbl[0].setForeground(Color.RED);

			s.add(first = lbl("≪처음으로", JLabel.CENTER, 15));
			s.add(last = lbl("마지막으로≫", JLabel.CENTER, 15));

			prev.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (pageIdx - 1 == 0)
						return;
					else
						pageIdx--;
					for (int i = 0; i < lbl.length; i++) {
						lbl[i].setForeground(Color.BLACK);
					}

					lbl[pageIdx - 1].setForeground(Color.red);
					page.show(c_c, pageIdx + "");
					super.mousePressed(e);
				}
			});

			next.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (pageIdx + 1 == pageCount + 1)
						return;
					else
						pageIdx++;

					for (int i = 0; i < lbl.length; i++) {
						lbl[i].setForeground(Color.BLACK);
					}

					lbl[pageIdx - 1].setForeground(Color.red);
					page.show(c_c, pageIdx + "");
					super.mousePressed(e);
				}
			});

			first.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {

					for (int i = 0; i < lbl.length; i++) {
						lbl[i].setForeground(Color.BLACK);
					}

					lbl[0].setForeground(Color.RED);
					page.first(c_c);
					pageIdx = 1;
				}
			});

			last.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {

					for (int i = 0; i < lbl.length; i++) {
						lbl[i].setForeground(Color.BLACK);
					}

					lbl[pageCount - 1].setForeground(Color.RED);
					page.last(c_c);
					pageIdx = pageCount;
				}
			});

			setBorder(new EmptyBorder(20, 20, 20, 20));
		}
	}

	public static void main(String[] args) {
		new Search();
	}
}
