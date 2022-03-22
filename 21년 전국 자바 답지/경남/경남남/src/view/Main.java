package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import view.BaseFrame.Before;

public class Main extends BaseFrame {
	JPanel n_c, n_e, c_c, c_e, s_n, s_n_w, s_c;
	JTextField txt = new JTextField(15);
	String[] locate = "서울,경기,인천,부산,대구,대전,경남,전남,충남,광주,울산,경북,전북,충북,강원,제주,세종,전국".split(",");
	JLabel lblLocate[] = new JLabel[locate.length];
	static JButton btn[] = new JButton[4];
	static JButton btnLogin, btnSign;
	JLabel bd;

	public Main() {
		super("메인", 700, 700);
		getContentPane().setBackground(Color.white);
		add(n = new JPanel(new BorderLayout()), "North");
		add(c = new JPanel(new BorderLayout()), "Center");
		add(s = new JPanel(new BorderLayout()), "South");

		n.add(n_c = new JPanel(new GridLayout(0, 1)));
		n.add(n_e = new JPanel(new GridLayout(0, 1)), "East");

		c.add(c_c = new JPanel(new BorderLayout()));
		c.add(c_e = new JPanel(new BorderLayout()), "East");

		s.add(s_n = new JPanel(new BorderLayout()), "North");
		s_n.add(s_n_w = new JPanel(new FlowLayout(0)), "West");
		s.add(s_c = new JPanel(new GridLayout(0, 5)));

		n.setOpaque(false);
		c.setOpaque(false);
		s.setOpaque(false);
		n_c.setOpaque(false);
		n_e.setOpaque(false);
		c_c.setOpaque(false);
		c_e.setOpaque(false);
		s_n.setOpaque(false);
		s_n_w.setOpaque(false);
		s_c.setOpaque(false);

		northUI();
		centerUI();
		southUI();
		((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
		setVisible(true);
	}

	private void southUI() {
		sz(s, 1, 380);

		var s_n_w = new JPanel(new FlowLayout(0));
		s_n.add(s_n_w, "West");

		s_n_w.add(lbl("브랜드알바", 2, 20));

		s_n_w.add(btn("더보기+", a -> {
			new BrandAlba();
		}));
		s_n_w.setOpaque(false);

		s_n.add(bd = new JLabel(dateTimeFormat(LocalDateTime.now(), "yyyy.MM.dd hh:mm:ss") + " 기준"), "East");

		brandAlba();
	}

	void brandAlba() {
		ArrayList<String> cno = new ArrayList<String>();
		String path = "./지급자료/브랜드/", mname;
		try {
			for (var file : Files.list(Paths.get(path)).toArray(Path[]::new)) {
				mname = file.getName(file.getNameCount() - 1).toString();
				int pos = mname.lastIndexOf(".");
				mname = mname.substring(0, pos);
				cno.add(getOne("select c_no from company where name ='" + mname + "'"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery(
					"SELECT c.c_no, c.name, sum(hits) FROM recruitment r, company c where r.c_no=c.c_no and c.c_no in ("
							+ String.join(",", cno) + ")" + " group by c.c_no order by hits desc limit 15");
			while (rs.next()) {
				String no = rs.getString(1);
				JPanel jp = new JPanel(new BorderLayout());
				setLine(jp);
				jp.add(new JLabel(img("./지급자료/브랜드/" + rs.getString(2) + ".jpg", 130, 120)));
				s_c.add(jp);
				jp.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						new Inform(no);
					}
				});
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void northUI() {
		var n_c_t = new JPanel(new FlowLayout(0, 10, 10));
		var n_c_d = new JPanel(new FlowLayout(0, 0, 0));

		n_c.add(n_c_t);
		n_c.add(n_c_d);

		JLabel lbl;
		n_c_t.add(lbl = new JLabel("알바자바", 0));
		lbl.setFont(new Font("휴먼모음T", Font.BOLD, 30));

		n_c_t.add(txt);

		n_c_t.add(btn("검색", a -> {
			if (txt.getText().isEmpty()) {
				eMsg("빈칸입니다.");
				return;
			}

			if (!checkIsExists("select * from recruitment where title like '%" + txt.getText() + "%'")) {
				eMsg("입력한 내용과 일치하는 정보가 없습니다");
				return;
			}
			execute("insert into search values(0, '" + txt.getText() + "',now())");
		}));

		String bcap[] = "채용정보,하루알바,인재정보,기업등록".split(",");
		for (int i = 0; i < bcap.length; i++) {
			n_c_d.add(btn[i] = sz(btn(bcap[i], a -> {
				if (a.getActionCommand().contentEquals(bcap[0])) {
					new Recruitment();
				} else if (a.getActionCommand().contentEquals(bcap[1])) {
					new HaruAlba();
				} else if (a.getActionCommand().contentEquals(bcap[2])) {
					new Resume();
				} else if (a.getActionCommand().contentEquals("기업등록")) {
					new CompanyRegisteration().addWindowListener(new Before(this));
				} else if (a.getActionCommand().contentEquals("공고등록")) {
					new Announcement();
				} else if (a.getActionCommand().contentEquals("이력서")) {
					new WriteResume();
				}
			}), 100, 30));
		}

		var n_e_t = new JPanel(new FlowLayout(2, 10, 10));
		var n_e_d = new JPanel(new FlowLayout(2, 0, 0));

		n_e.add(n_e_t);
		n_e.add(n_e_d);

		n_e_t.add(btnLogin = btn("로그인", a -> {
			if (a.getActionCommand().contentEquals("로그인")) {
				new Login();
			} else {
				type = "";
				isLogin = false;
				btn[3].setText("기업등록");
				btnLogin.setText("로그인");
				btnSign.setText("회원가입");
			}
		}));

		n_e_t.add(new MailPanel());

		n_e_d.add(btnSign = sz(btn("회원가입", a -> {
			if (a.getActionCommand().contentEquals("회원가입")) {
				new Sign();
			} else if (a.getActionCommand().contentEquals("회원수정")) {
//				new EditProfile();
			} else if (a.getActionCommand().contentEquals("기업수정")) {
//				기업 수정
			}
		}), 130, 30));

		n_e_t.setOpaque(false);
		n_e_d.setOpaque(false);

		n_c_t.setOpaque(false);
		n_c_d.setOpaque(false);

	}

	void centerUI() {
		sz(c_e, 300, 1);
		c_c.add(lbl("지역별알바", 0, 20), "North");

		var c_c_c = new JPanel(new GridLayout(0, 6));
		c_c.add(c_c_c);
		c_c_c.setOpaque(false);

		for (int i = 0; i < locate.length; i++) {
			lblLocate[i] = new JLabel(locate[i], 0);
			c_c_c.add(lblLocate[i]);
			lblLocate[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					for (int j = 0; j < lblLocate.length; j++) {
						lblLocate[j].setBorder(BorderFactory.createEmptyBorder());
					}
					((JLabel) e.getSource()).setBorder(new LineBorder(Color.red));
				}
			});
		}

		c_e.add(lbl("인기검색어", 0, 20), "North");

		Popular p = new Popular();
		p.setBorder(new LineBorder(Color.BLACK));
		c_e.add(p);
	}

	class Popular extends JPanel {
		Item items[] = new Item[5];
		ArrayList<String> beforeWord = new ArrayList<>(); // 원래 순위
		ArrayList<String> afterWord = new ArrayList<>(); // 후 순위

		public Popular() {
			super(new GridLayout(0, 1));

			try {
				var rs = stmt.executeQuery(
						"SELECT word, count(*) cnt FROM albajava.search where time < now() group by word order by cnt desc, word asc limit 0 ,5");
				while (rs.next()) {
					beforeWord.add(rs.getString(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			beforeWord.forEach(a -> {
				add(items[beforeWord.indexOf(a)] = new Item(beforeWord.indexOf(a) + 1, a));
			});

			new Thread(() -> {
				while (true) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					bd.setText(dateTimeFormat(LocalDateTime.now(), "yyyy.MM.dd hh:mm:ss") + " 기준");

					try {
						var rs = stmt.executeQuery(
								"SELECT word, count(*) cnt FROM albajava.search where time < now() group by word order by cnt desc, word asc limit 0 ,5");
						afterWord.clear();

						while (rs.next()) {
							afterWord.add(rs.getString(1));
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					for (String str : afterWord) {
						int aidx = afterWord.indexOf(str);
						if (beforeWord.contains(str)) { // 순위에 이미 존재할 경우 ㅇㅇ
							int bidx = beforeWord.indexOf(str);
							if (bidx - aidx != 0 || aidx - bidx != 0) {
								if (aidx > bidx) { // 순위가 낮은경우 ()
									items[aidx].setTweak(aidx - bidx, false);
								} else { // 순위가 높은 경우 ?
									items[aidx].setTweak(bidx - aidx, true);
								}
							}
						} else { // new
							items[aidx].newTweak();
						}
					}

					afterWord.forEach(a -> {
						items[afterWord.indexOf(a)].set(afterWord.indexOf(a) + 1, a);
					});

					Collections.copy(beforeWord, afterWord);

					repaint();
					revalidate();
				}

			}).start();

		}

		class Item extends JPanel {

			String text;
			JLabel rankLabel, textLabel, tweakLabel;

			public Item(int rank, String text) {
				super(new BorderLayout(5, 5));
				this.text = text;
				add(rankLabel = new JLabel(rank + "", JLabel.CENTER), "West");
				add(textLabel = new JLabel(text + "", JLabel.LEFT));
				add(tweakLabel = new JLabel(), "East");

				rankLabel.setOpaque(true);
				rankLabel.setForeground(Color.WHITE);
				rankLabel.setBackground(Color.RED);
				tweakLabel.setOpaque(true);

				addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						new Recruitment();
						super.mousePressed(e);
					}
				});
			}

			void set(int rank, String text) {
				this.text = text;
				rankLabel.setText(rank + "");
				textLabel.setText(text);
			}

			void setTweak(int tweak, boolean isIncrease) {
				if (isIncrease) {
					tweakLabel.setForeground(Color.RED);
					tweakLabel.setText("↑ " + tweak);
				} else {
					tweakLabel.setForeground(Color.BLUE);
					tweakLabel.setText("↓ " + tweak);
				}
			}

			void newTweak() {
				tweakLabel.setForeground(Color.GREEN);
				tweakLabel.setText("NEW");
			}
		}
	}

	class MailPanel extends JPanel {

		JLabel img;
		JLabel l;

		public MailPanel() {
			super(new BorderLayout());
			this.setOpaque(false);

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (!isLogin) {
						eMsg("로그인해주세요.");
						new Login();
					} else {
						new Mail();
					}
				}
			});

			this.add(img = new JLabel(img("./지급자료/메일.png", 50, 50)));
			l = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					if (isLogin) {

						int cnt = 0;
						try {
							System.out.println(mailSql("send"));
							var rs = stmt.executeQuery(mailSql("send"));
							while (rs.next()) {
								cnt++;
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (cnt > 0) {
							g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
							g2.setColor(Color.yellow);
							g2.fillOval(24, 0, 24, 25);
							g2.setColor(Color.black);
							g2.drawOval(24, 0, 24, 25);
							g2.drawString(cnt + "", 34, 18);
						}
					}

				}
			};
			l.setBounds(0, 0, 50, 25);
			img.add(l);

		}
	}

	public static void main(String[] args) {
		new Main();

	}
}
