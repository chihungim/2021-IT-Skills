package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class AdminMain extends BaseFrame {
	JTabbedPane pane = new JTabbedPane();
	String cap[] = "사용자 관리,추천 여행지 관리,일정 관리,예매 관리".split(",");
	JPanel p[] = { new UserManage(), new Recommend(), new ScheduleManage(), new ReserveManage() };

	public AdminMain() {
		super("관리자", 1100, 700);
		setUI();
		setVisible(true);
	}

	void setUI() {
		add(pane);
		pane.setTabPlacement(2);
		for (int i = 0; i < cap.length; i++) {
			pane.add(p[i]);
			pane.setTitleAt(i, cap[i]);
		}

		pane.add("테마", null);
		pane.add("로그아웃", null);

		pane.addChangeListener(e -> {
			if (pane.getSelectedIndex() == 4) {
				whiteColor = (whiteColor.equals(Color.WHITE)) ? Color.DARK_GRAY : Color.WHITE;
				blackColor = (blackColor.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE;
				BaseDialog.whiteColor = (whiteColor.equals(Color.WHITE)) ? Color.WHITE : Color.DARK_GRAY;
				BaseDialog.blackColor = (blackColor.equals(Color.WHITE)) ? Color.WHITE : Color.BLACK;
				((JPanel) getContentPane()).setBackground(whiteColor);
				for (int i = 0; i < 4; i++) {
					p[i].setBackground(whiteColor);
				}
				repaint();
				revalidate();
				pane.setBackgroundAt(4, (whiteColor.equals(Color.WHITE)) ? Color.DARK_GRAY : Color.WHITE);
				pane.setBackgroundAt(4, (blackColor.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE);
			}
			if (pane.getSelectedIndex() == 5) {
				dispose();
			}
		});
		pane.setBackgroundAt(4, (whiteColor.equals(Color.WHITE)) ? Color.DARK_GRAY : Color.WHITE);
		pane.setBackgroundAt(4, (blackColor.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE);

	}

	public static void main(String[] args) {
		new AdminMain();
	}

	class UserManage extends JPanel {

		DefaultTableModel m = BaseFrame.model("순번,아이디,비밀번호,성명,이메일,포인트,예매수".split(","));
		JTable t = BaseFrame.table(m);
		JTextField searchField = HintText("성명", new JTextField(20));
		JPopupMenu menu = new JPopupMenu();
		JMenuItem item = new JMenuItem("예매 조회");

		boolean isChanged;

		JPanel n = new JPanel(new BorderLayout()), n_e = new JPanel(new FlowLayout());
		JPanel s = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		public UserManage() {
			super(new BorderLayout());
			setUI();
			search("");
		}

		void setUI() {
			n.add(lbl("사용자 관리", JLabel.LEFT, 20), "West");
			add(n, "North");
			n.add(n_e, "East");
			n_e.add(searchField);
			n_e.add(btn("사용자조회", a -> search(searchField.getText())));
			add(new JScrollPane(t));
			add(s, "South");

			for (var bcap : "저장,삭제".split(",")) {
				s.add(BaseFrame.btn(bcap, a -> {
					if (a.getActionCommand().equals("저장")) {
						for (int j = 0; j < t.getRowCount(); j++) {
							int idx = j;
							try {
								var rs = BaseFrame.stmt
										.executeQuery("select * from user where no = '" + t.getValueAt(idx, 0) + "'");
								if (rs.next()) {
									for (int k = 0; k < 6; k++) {
										if (t.getValueAt(idx, k) != rs.getString(k + 1)) {
											BaseFrame.imsg = new BaseFrame.iMsg("수정내용을 저장 완료하였습니다");
											BaseFrame.execute("update user set id = '" + t.getValueAt(idx, 1)
													+ "', pwd = '" + t.getValueAt(idx, 2) + "', name = '"
													+ t.getValueAt(idx, 3) + "', email = '" + t.getValueAt(idx, 4)
													+ "', point = '" + t.getValueAt(idx, 5) + "' where no = '"
													+ t.getValueAt(idx, 0) + "'");
											return;
										}
									}
								}
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} else {
						BaseFrame.emsg = new BaseFrame.eMsg("삭제를 완료하였습니다");
						BaseFrame.execute("delete from user where no = '" + t.getValueAt(t.getSelectedRow(), 0) + "'");
						search("");
					}
				}));
			}
		}

		void search(String name) {
			if (name.equals("성명")) {
				return;
			}

			m.setRowCount(0);

			BaseFrame.addRow(m,
					"select u.no, u.id, u.pwd, u.name, u.email, u.point, count(user_no) from user u, reservation r where r.user_no = u.no and name like '%"
							+ name + "%' group by user_no order by u.no");
		}
	}

	class Recommend extends JPanel {

		JPanel rPanel, iPanel;
		JPanel rc, ic;

		int srnum;

		public Recommend() {
			super(new GridLayout(0, 1));
			setUI();
		}

		void setUI() {
			add(rPanel = new JPanel(new BorderLayout(5, 5)));

			rPanel.add(BaseFrame.lbl("추천 여행지 관리", JLabel.LEFT, 15), "North");
			rc = new JPanel(new GridLayout(1, 0, 5, 5));
			rPanel.add(rc);

			try {
				var rs = stmt.executeQuery(
						"SELECT * FROM busticketbooking.recommend_info ri inner join recommend r on ri.recommend_no = r.no inner join location l on r.location_no = l.no group by name order by r.no");
				while (rs.next()) {
					JLabel img = BaseFrame.imageLabel("지급파일/images/recommend/"
							+ BaseFrame.hashMap.get(rs.getString("name")) + "/" + rs.getString(2) + ".jpg", 200, 200);
					final TitledBorder title = new TitledBorder(new LineBorder(Color.BLACK), rs.getString("name"));
					img.setName(rs.getString("name"));
					final int num = rs.getInt(1);
					img.setBorder(title);
					rc.add(img);
					final JPopupMenu menu = new JPopupMenu();
					JPopupMenu pop = new JPopupMenu();
					img.setComponentPopupMenu(pop);

					for (var bcap : "이미지 설정,설명설정".split(",")) {
						JMenuItem item = new JMenuItem(bcap);
						pop.add(item);
						item.addActionListener(a -> {
							if (a.getActionCommand().equals("설명설정")) {
								srnum = num;
								ic.removeAll();
								for (var str : new File("./지급파일/images/recommend/" + hashMap.get(img.getName()))
										.listFiles()) {
									JLabel subImg = imageLabel(str.toString(), 200, 200);
									JPopupMenu popop = new JPopupMenu();
									for (var bc : "삭제,설명 텍스트 입력".split(",")) {
										JMenuItem jItem = new JMenuItem(bc);
										jItem.addActionListener(b -> {
											JLabel tmp = subImg;
											String v = str.getName().replaceAll("[^0-9]", "");
											if (b.getActionCommand().equals("삭제")) {
												ic.remove(tmp);

												// 자료 없어짐

												revalidate();
												repaint();
											} else {
												String inp = JOptionPane.showInputDialog(null, "설명 텍스트를 입력해주세요.", "입력",
														JOptionPane.QUESTION_MESSAGE);
												if (inp != null) {
													BaseFrame.execute("update recommend_info set description = '" + inp
															+ "' where recommend_no = " + num + " and title = " + v);
												}
											}
										});
										popop.add(jItem);
									}
									subImg.setComponentPopupMenu(popop);
									ic.add(subImg);
								}
								revalidate();
							} else {
								JFileChooser file = new JFileChooser();
								FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & png Images",
										"jpg,png".split(","));
								file.setFileFilter(filter);
								file.setMultiSelectionEnabled(false);
								if (file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
									if (new File("").exists()) {

									} else {

									}

									img.setIcon(new ImageIcon(
											Toolkit.getDefaultToolkit().getImage(file.getSelectedFile().getPath())
													.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
								}

							}

						});
					}

					menu.add(new PosItem(img, title, menu));
					img.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount() == 2) {
								menu.show((JLabel) e.getSource(), e.getX(), e.getY());
							}
							super.mouseClicked(e);
						}
					});
				}
			} catch (

			SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			add(iPanel = new JPanel(new BorderLayout()));

			ic = new JPanel(new GridLayout(1, 0, 5, 5));

			var n = new JPanel(new FlowLayout(FlowLayout.LEFT));

			iPanel.add(n, "North");
			iPanel.add(ic);

			n.add(BaseFrame.lbl("추천 여행지 관리", JLabel.LEFT, 15));
			n.add(btn("추가", a -> {
				JFileChooser file = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & png Images", "jpg,png".split(","));
				file.setFileFilter(filter);
				file.setMultiSelectionEnabled(false);
				if (file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					JLabel img = imageLabel(file.getSelectedFile().toString(), 200, 200);
					ic.add(img);
					revalidate();
					repaint();
					var title = file.getSelectedFile().getName();
				}
			}));
		}

		class PosItem extends JPanel {

			JScrollPane pane;

			HashMap<String, String> map = new HashMap<>();

			public PosItem(JLabel img, TitledBorder t, JPopupMenu p) {
				super(new BorderLayout());

				var c = new JPanel(new GridLayout(0, 1));

				add(pane = new JScrollPane(c));

				try {
					var rs = BaseDialog.stmt.executeQuery("select * from location");
					while (rs.next()) {
						map.put(rs.getString(1), rs.getString(2));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (String v : map.keySet()) {
					JButton b = new JButton(map.get(v));
					b.setName(v);
					c.add(b);
					b.addActionListener(a -> {
						int rNum = 0;
						try {
							var rs = stmt.executeQuery(
									"select * from location l, recommend r where l.no = r.location_no and name like '%"
											+ t.getTitle() + "%'");
							if (rs.next()) {
								rNum = rs.getInt(3);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						execute("update recommend set location_no = " + ((JButton) (a.getSource())).getName()
								+ " where no = " + rNum);
						img.setIcon(new ImageIcon(Toolkit
								.getDefaultToolkit().getImage("지급파일/images/recommend/"
										+ BaseFrame.hashMap.get(a.getActionCommand()) + "/1.jpg")
								.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
						System.out.println("지급파일/images/recommend/" + BaseFrame.hashMap.get(a.getActionCommand() + "/"
								+ hashMap.get(((JButton) (a.getSource())).getName()) + ".jpg"));
						t.setTitle(a.getActionCommand());

						p.setVisible(false);
					});
				}

			}
		}
	}

	class ScheduleManage extends JPanel {
		DefaultTableModel m = BaseFrame.nmodel("순번,출발지,도착지,출발날짜,이동시간".split(","));
		JTable t = BaseFrame.table(m);
		JPopupMenu menu;

		public ScheduleManage() {
			super(new BorderLayout());
			setUI();
			setData();
			event();
		}

		void setUI() {
			add(lbl("일정 관리", JLabel.LEFT, 20), "North");
			add(new JScrollPane(t));
			var s = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			add(s, "South");
			menu = new JPopupMenu();
			for (var bcap : "저장,삭제".split(",")) {
				s.add(btn(bcap, a -> {
					if (a.getActionCommand().equals("저장")) {
						save();
					} else {
						delete();
					}
				}));
			}

		}

		void save() {
			BaseFrame.imsg = new BaseFrame.iMsg("수정내용을 저장 완료하였습니다.");
			for (int i = 0; i < t.getRowCount(); i++) {
				int depart = BaseFrame
						.toInt(BaseFrame.getOne("select no from location2 where name = '" + t.getValueAt(i, 1) + "'"));
				int arrive = BaseFrame
						.toInt(BaseFrame.getOne("select no from location2 where name = '" + t.getValueAt(i, 2) + "'"));
				BaseFrame.execute("update schedule set departure_location2_no = " + depart + ", arrival_location2_no = "
						+ arrive + " where no = " + BaseFrame.toInt(t.getValueAt(i, 0)));
			}
		}

		void event() {
			t.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 3) {
						if (t.getSelectedRow() != -1 && t.getSelectedColumn() == 1 || t.getSelectedColumn() == 2) {
							menu.removeAll();
							menu.add(new PosItem(t.getSelectedRow(), t.getSelectedColumn(), menu));
							menu.show(AdminMain.this, e.getX(), e.getY());
						}
					}
				}
			});
		}

		void delete() {
			BaseFrame.imsg = new BaseFrame.iMsg("삭제를 완료하였습니다.");
			BaseFrame.execute("delete from reservation where schedule_no = "
					+ BaseFrame.toInt(t.getValueAt(t.getSelectedRow(), 0)));
			BaseFrame
					.execute("delete from schedule where no = " + BaseFrame.toInt(t.getValueAt(t.getSelectedRow(), 0)));
			setData();
		}

		void setData() {
			m.setRowCount(0);

			try {
				var rs = BaseFrame.stmt.executeQuery(
						"SELECT s.no, a.name, b.name, date, elapsed_time FROM busticketbooking.schedule s inner join a on s.departure_location2_no = a.no inner join a b on s.arrival_location2_no = b.no");
				while (rs.next()) {
					Object row[] = new Object[m.getColumnCount()];
					for (int i = 0; i < row.length; i++) {
						row[i] = rs.getString(i + 1);
					}
					m.addRow(row);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		class PosItem extends JPanel {

			JScrollPane pane1;
			JScrollPane pane2;

			HashMap<String, String> map = new HashMap<>();

			public PosItem(int r, int c, JPopupMenu m) {
				super(new BorderLayout());

				var cc = new JPanel(new GridLayout(1, 0));
				var c1 = new JPanel(new GridLayout(0, 1));
				var c2 = new JPanel(new GridLayout(0, 1));

				add(cc);
				cc.add(pane1 = new JScrollPane(c1));
				pane2 = new JScrollPane(c2);

				try {
					var rs = stmt.executeQuery("select * from location");
					while (rs.next()) {
						map.put(rs.getString(1), rs.getString(2));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (String v : map.keySet()) {
					JButton b = new JButton(map.get(v));
					b.setName(v);
					c1.add(b);
					b.addActionListener(a -> {

						c2.removeAll();
						cc.add(pane2);
						try {
							var rs = stmt.executeQuery("select * from location2 where location_no = "
									+ ((JButton) (a.getSource())).getName());
							while (rs.next()) {
								JButton btn = new JButton(rs.getString(2));
								btn.addActionListener(d -> {
									remove(pane2);
									t.setValueAt(a.getActionCommand() + " " + d.getActionCommand(), r, c);
									m.setVisible(false);
								});

								c2.add(btn);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						revalidate();
						pane2.setVisible(true);
					});
				}

				pane1.setPreferredSize(new Dimension(150, 300));
				pane2.setVisible(false);
			}
		}
	}

	class ReserveManage extends JPanel {
		JPanel n, c, s;
		JPanel n_s;
		JPanel Chart;

		DefaultTableModel m = BaseFrame.model("순번,예매자,출발지,도착지,출발날짜,도착시간".split(","));
		JTable t = BaseFrame.table(m);
		JScrollPane jsc = new JScrollPane(t);

		JComboBox<String> com = new JComboBox<String>();

		JPanel p1 = new JPanel(new GridLayout(0, 1)), p2 = new JPanel(new GridLayout(0, 1)),
				menuP = new JPanel(new GridLayout(1, 0));
		JPopupMenu menu = new JPopupMenu();
		JScrollPane menuJsc = new JScrollPane(p1);
		JScrollPane menuJsc2 = new JScrollPane(p2);
		JButton btn1[] = new JButton[16], btn2[];

		boolean bool;

		int num[] = new int[6];
		int count = 0;

		public ReserveManage() {

			UI();
			data();
			event();
		}

		void data() {
			try {
				var rs = BaseFrame.stmt.executeQuery("select * from location");
				int i = 0;
				while (rs.next()) {
					p1.add(btn1[i] = new JButton(rs.getString("name")));
					i++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		void event() {
			t.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == 3 && (t.getSelectedColumn() == 1 || t.getSelectedColumn() == 2)) {
						count = t.getSelectedColumn();
						menu.show(t, e.getX(), e.getY());
					}
				}
			});

			for (int i = 0; i < btn1.length; i++) {
				int j = i;
				btn1[i].addActionListener(a -> {
					p2.removeAll();
					try {
						ResultSet rs1 = BaseFrame.stmt
								.executeQuery("select * from location2 where location_no = " + (j + 1));
						rs1.last();
						btn2 = new JButton[rs1.getRow()];
						rs1.beforeFirst();
						int k = 0;
						while (rs1.next()) {
							p2.add(btn2[k] = new JButton(rs1.getString(2)));
							btn2[k].addActionListener(a2 -> {
								JButton jj = (JButton) a2.getSource();
								String aa = btn1[j].getText() + " " + jj.getText();
								t.setValueAt(aa, t.getSelectedRow(), count);
								bool = true;
							});
							k++;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					repaint();
					revalidate();
				});

				for (Component c : getComponents()) {
					if (c instanceof JPanel)
						remove(c);
				}

				areaChart();

				com.addItemListener(e -> {
					if (com.getSelectedIndex() == 0) {
						areaChart();
					} else {
						radialChart();
					}
				});

				rowData();
			}
		}

		private void radialChart() {
			try {
				remove(Chart);
			} catch (NullPointerException e) {
				System.out.println("원래 이래요 ㅋㅋ");
			}

			Chart = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					g2.setColor(Color.BLACK);
					int po[][] = new int[6][2];

					Polygon p[] = { new Polygon(), new Polygon(), new Polygon(), new Polygon(), new Polygon(),
							new Polygon() };

					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 6; j++) {
							p[i].addPoint((int) (380 + (100 - i * 20) * Math.cos(j * Math.PI / 3 + (Math.PI / 6))),
									(int) (150 + (100 - i * 20) * Math.sin(j * Math.PI / 3 + (Math.PI / 6))));
						}

						g2.drawPolygon(p[i]);
					}

					for (int i = 0; i < 6; i++) {
						po[i][0] = (int) (375 + (110) * Math.cos(i * Math.PI / 3 - (Math.PI / 2)));
						po[i][1] = (int) (150 + (110) * Math.sin(i * Math.PI / 3 - (Math.PI / 2)));
					}

					int basex = 0, basey = 0;

					try {
						var rs = BaseFrame.stmt.executeQuery(
								"SELECT *, count(schedule_no) as cnt FROM busticketbooking.reservation group by schedule_no order by cnt desc, schedule_no asc limit 6");
						int i = 0, max = 0;
						while (rs.next()) {
							if (i == 0) {
								max = rs.getInt(4);
								basex = (int) (po[0][0] / max);
								basey = (int) (po[0][1] / max);
							}
							g2.setColor(Color.BLACK);
							if (i < 5)
								g2.drawString(max - 2 * i + "", 370, 55 + (i * 20));
							g2.drawString(rs.getString(3), po[i][0], po[i][1]);
							num[i] = rs.getInt(4);
							i++;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					g2.setColor(new Color(0, 123, 255));
					Polygon poly = new Polygon();
					for (int i = 0; i < num.length; i++) {
						poly.addPoint(
								(int) (380 + (100 - (num[0] - num[i]) / 2.0 * 20)
										* Math.cos(i * Math.PI / 3 - (Math.PI / 2))),
								(int) (150 + (100 - (num[0] - num[i]) / 2.0 * 20)
										* Math.sin(i * Math.PI / 3 - (Math.PI / 2))));
					}
					g2.drawPolygon(poly);

				}
			};

			Chart.setOpaque(true);
			add(Chart).setBounds(100, 100, 500, 300);
			revalidate();
			repaint();
		}

		void rowData() {
			m.setRowCount(0);

			try {
				ResultSet rs = BaseFrame.stmt.executeQuery(
						"SELECT u.name, a.name, b.name, date, date_add(date, interval elapsed_time HOUR_SECOND) FROM busticketbooking.reservation r inner join user u on r.user_no = u.no inner join schedule s on r.schedule_no = s.no inner join a on s.departure_location2_no = a.no inner join a b on s.arrival_location2_no = b.no order by r.no asc");
				int i = 1;
				while (rs.next()) {
					Object row[] = { i, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5).split(" ")[1] };
					m.addRow(row);
					i++;
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		void UI() {
			setLayout(null);

			menu.add(menuP);
			menuP.add(menuJsc);
			menuP.add(menuJsc2);

			BaseDialog.size(menu, 240, 280);

			add(BaseFrame.lbl("예매 관리", 2, 20)).setBounds(10, 20, 130, 30);
			add(com).setBounds(110, 25, 100, 25);
			add(BaseFrame.lbl("<가장 예매가 많은 일정 TOP 6>", 0, 13)).setBounds(370, 20, 200, 30);

			com.addItem("2차원 영역형");
			com.addItem("방사형");

			add(jsc).setBounds(10, 420, 950, 180);
			add(BaseFrame.btn("저장", e -> save())).setBounds(750, 600, 90, 30);
			add(BaseFrame.btn("삭제", e -> delete())).setBounds(850, 600, 90, 30);
		}

		void delete() {
			BaseFrame.imsg = new BaseFrame.iMsg("삭제를 완료하였습니다.");
			BaseFrame.execute("delete from reservation where  schedule_no = "
					+ BaseFrame.toInt(t.getValueAt(t.getSelectedRow(), 0)));
			BaseFrame
					.execute("delete from schedule where no = " + BaseFrame.toInt(t.getValueAt(t.getSelectedRow(), 0)));
			rowData();
		}

		void save() {
			if (bool) {
				BaseFrame.imsg = new BaseFrame.iMsg("수정내용을 저장 완료하였습니다.");
				for (int i = 0; i < t.getRowCount(); i++) {
					int depart = BaseFrame.toInt(
							BaseFrame.getOne("select no from location2 where name = '" + t.getValueAt(i, 1) + "'"));
					int arrive = BaseFrame.toInt(
							BaseFrame.getOne("select no from location2 where name = '" + t.getValueAt(i, 2) + "'"));
					BaseFrame.execute(
							"update schedule set departure_location2_no = " + depart + ", arrival_location2_no = "
									+ arrive + " where no = " + BaseFrame.toInt(t.getValueAt(i, 0)));
				}
			}
		}

		void areaChart() {
			try {
				remove(Chart);
			} catch (NullPointerException e) {
				System.out.println("원래 이래요 ㅋㅋ");
			}

			Chart = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.drawLine(50, 20, 50, 260);

					// 섡

					for (int i = 0; i < 5; i++) {
						g2.drawLine(50, 20 + (i * 60), 600, 20 + (i * 60));
					}

					Polygon p = new Polygon();

					try {
						var rs = stmt.executeQuery(
								"select *, count(schedule_no) as cnt from reservation group by schedule_no order by cnt desc, schedule_no asc limit 6");
						int i = 0, max = 0;
						while (rs.next()) {
							max = i == 0 ? rs.getInt(4) : max;
							if (i < 5)
								g2.drawString(max - (i * 2) + "", 30, 25 + (i * +60));

							g2.drawString(rs.getString(3), 50 + (i * 105), 290);
							p.addPoint(50 + (i * 110), ((max - rs.getInt(4)) * 40) + 20);
							i++;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

					p.addPoint(600, 260);
					p.addPoint(50, 260);

					g2.setColor(new Color(0, 123, 255));
					g2.fillPolygon(p);
				}
			};

			add(Chart).setBounds(15, 100, 900, 300);
			Chart.setOpaque(true);
			revalidate();
			repaint();
		}

	}
}
