package 전국대전자바;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.mysql.cj.x.protobuf.MysqlxConnection.Close;

public class Main extends BaseFrame {
	private JPanel mainp = new JPanel(new BorderLayout());
	private JPanel n = new JPanel(new BorderLayout()), n_w = new JPanel(new FlowLayout(0, 10, 0));
	private JPanel n_e = new JPanel(new FlowLayout(2, 10, 0));
	private JPanel s = new JPanel();
	private JLabel lbl[] = { new JLabel("TICKETING"), new JLabel("MONTH SCHEDULE"), new JLabel("CHART"), new JLabel(),
			new JLabel(""), new JLabel("MYPAGE") };
	JLabel cntlbl[] = { new JLabel("1"), new JLabel("2"), new JLabel("3") };
	String titles[] = { "인기공연(뮤지컬)", "인기공연(오페라)", "인기공연(콘서트)" };
	String codes[] = { "M", "O", "C" };
	int kindcnt = 0, num = 0;
	byte[] imagebytes;

	public Main() {
		super("메인", 620, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		ui();
		data();
		event();
		setVisible(true);
	}

	private void data() {
		if (isLogined == false) {
			lbl[3].setVisible(false);
			lbl[4].setText("LOGIN");
		} else {
			lbl[3].setVisible(true);
			lbl[4].setText("LOGOUT");
			lbl[3].setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./Datafiles/회원사진/" + u_no + ".jpg")
					.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
			lbl[3].setBorder(new LineBorder(Color.black));
		}
	}

	void event() {
		lbl[0].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				new Search().addWindowListener(new before(Main.this));
			}
		});

		lbl[1].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				new MonthSchedule().addWindowListener(new before(Main.this));
			}
		});

		lbl[2].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				new Chart().addWindowListener(new before(Main.this));
			}
		});

		lbl[4].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (isLogined == true && lbl[4].getText().equals("LOGIN")) {
					dispose();
				} else if (lbl[4].getText().equals("LOGIN")) {
					new Login().addWindowListener(new before(Main.this));
				} else {
					iMsg("로그아웃되었습니다.");
					lbl[3].setVisible(false);
					lbl[4].setText("LOGIN");
				}
			}
		});

		lbl[5].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (isLogined == false) {
					eMsg("로그인을 하세요");
					return;
				} else {
					new MyPage().addWindowListener(new before(Main.this));
				}
			}
		});
	}

	void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(s, "Center");
		n.add(n_w, "West");
		n.add(n_e, "East");
		n.setBorder(new EmptyBorder(0, 0, 15, 0));
		mainp.setBorder(new EmptyBorder(0, 5, 5, 5));

		for (int i = 0; i < 3; i++) {
			lbl[i].setFont(new Font("굴림", Font.BOLD, 15));
			n_w.add(lbl[i]);
		}
		n_e.add(lbl[3]);
		n_e.add(lbl[4]);
		n_e.add(lbl[5]);
		lbl[4].setFont(new Font("굴림", Font.BOLD, 15));
		lbl[5].setFont(new Font("굴림", Font.BOLD, 15));

		Popular pop = new Popular();
		s.add(pop);

		new Thread() {
			public void run() {
				while (true) {
					try {
						pop.data(codes[kindcnt % 3], titles[kindcnt % 3]);
						kindcnt++;
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			};
		}.start();
	}

	public static void main(String[] args) {
		new Main();
	}

	class Popular extends JPanel {
		private TitledBorder border = new TitledBorder(new LineBorder(Color.black), "");

		JPanel mainp = new JPanel(new BorderLayout()), mainp2 = new JPanel();
		JPanel n = new JPanel(new GridLayout(0, 5, 15, 5)), c = new JPanel(new GridLayout(0, 5, 15, 5)),
				s = new JPanel(new GridLayout(0, 5, 15, 5));
		JLabel ranklbl[] = new JLabel[5], imglbl[] = new JLabel[5], namelbl[] = new JLabel[5];

		public Popular() {
			border.setTitleColor(Color.black);
			border.setTitleFont(new Font("굴림", Font.BOLD, 15));
			this.setBorder(border);

			ui();
		}

		void ui() {
			JPanel in = new JPanel();
			this.add(in);

			in.setLayout(new BorderLayout());
			in.add(mainp, "North");
			in.add(mainp2, "South");
			mainp.add(n, "North");
			mainp.add(c, "Center");
			mainp.add(s, "South");

			for (int i = 0; i < 5; i++) {
				ranklbl[i] = new JLabel();
				imglbl[i] = new JLabel();
				imglbl[i].setPreferredSize(new Dimension(100, 100));
				namelbl[i] = new JLabel();
				namelbl[i].setHorizontalAlignment(SwingConstants.CENTER);

				n.add(new JLabel((i + 1) + "위"));
				c.add(imglbl[i]);
				s.add(namelbl[i]);
			}

			for (int i = 0; i < 3; i++) {
				cntlbl[i].setHorizontalAlignment(SwingConstants.CENTER);
				mainp2.add(cntlbl[i]);
			}
		}

		void data(String kind, String title) {
			// 데이터
			try {
				ResultSet rs = stmt.executeQuery(
						"select p.p_no, p.p_name, p.pf_no, count(*) c from perform p, ticket t where t.p_no = p.p_no and left(p.pf_no,1) = '"
								+ kind + "' group by p.p_name order by c desc limit 0, 5");
				int idx = 0;
				while (rs.next()) {
					imglbl[idx].setIcon(new ImageIcon(
							Toolkit.getDefaultToolkit().getImage("./Datafiles/공연사진/" + rs.getString(3) + ".jpg")
									.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
					imglbl[idx].setName(rs.getString(2));
					namelbl[idx].setText(imglbl[idx].getName());
					idx++;
				}

				for (int i = 0; i < 3; i++) {
					cntlbl[i].setForeground(Color.BLACK);
				}
				cntlbl[kindcnt % 3].setForeground(Color.RED);

				for (int i = 0; i < 3; i++) {
					border.setTitle(title);
					repaint();
					revalidate();
				}

				s.repaint();
				s.revalidate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
