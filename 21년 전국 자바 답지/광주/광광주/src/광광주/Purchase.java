package 광광주;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Purchase extends BaseFrame {

	JPanel n, n_c, c;
	JScrollPane pane;
	int totalWidth = 0, cardPw = 0, cardidx = -1, cardNo = 0;
	ArrayList<Card> cards;

	public Purchase(int price, Restaurant r) {
		super("결제", 310, 400);
		cards = new ArrayList<>();
		add(n = new JPanel(new BorderLayout()), "North");
		n.add(n_c = new JPanel(new BorderLayout()));

		n.add(lbl(orderList.size() != 1 ? orderList.get(0).get(0).toString() + "의 결제외 " + (orderList.size() - 1) + "건"
				: orderList.get(0).get(0).toString(), JLabel.LEFT, 15), "North");

		n_c.add(lbl("총 금액", JLabel.LEFT, 15), "West");
		n_c.add(lbl(df.format(price), JLabel.RIGHT), "East");

		add(pane = new JScrollPane(c = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5))));

		try {
			var rs = stmt.executeQuery("SELECT * FROM payment where user = " + uno);

			if (!rs.next()) {
				c.add(lbl("<html><font size = \"5\"; color = \"red\"; face = \"맑은 고딕\"><b>결제 수단이 없습니다.",
						JLabel.CENTER));
			}

			rs.previous();

			while (rs.next()) {
				Card c = new Card(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(5));
				cards.add(c);
				this.c.add(size(c, 250, 120));
				totalWidth += 120;

				c.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						cards.forEach(a -> a.col = Color.blue);
						cardidx = cards.indexOf(((Card) e.getSource()));
						((Card) e.getSource()).col = Color.RED;
						cardPw = ((Card) e.getSource()).pw;
						cardNo = ((Card) e.getSource()).no;
						repaint();
						revalidate();
					}

				});
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		add(btn("선택한 결제 수단으로 결제하기", a -> {
			if (cardidx == -1) {
				eMsg("결제할 카드를 선택해야 합니다.");
				return;
			}

			int yn = JOptionPane.showConfirmDialog(null, "정말로 " + df.format(price) + "원을 결제하시겠습니까?", "메시지",
					JOptionPane.YES_NO_OPTION);
			if (yn == JOptionPane.OK_OPTION) {
				var c = new JPanel(new GridLayout(0, 1));
				JTextField txt = new JTextField(10);
				c.add(lbl("결제 비밀번호를 입력해주세요.", JLabel.LEFT));
				c.add(txt);
				int oc = JOptionPane.showConfirmDialog(null, c, "메시지", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE);

				if (oc == JOptionPane.OK_OPTION) {
					if (!txt.getText().equals(cardPw + "")) {
						eMsg("경고 비밀번호가 일치하지 않습니다.");
						return;
					}
					iMsg("주문이 완료되었습니다.");
					isPurchaseOver = true;
					// 대충 인서트 구문
					
					execute("insert receipt values(0," + price + ","
							+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) + ","
							+ r.sno + "," + uno + "," + cardNo + ",0)");
					int no = getLastRef("SELECT * FROM eats.receipt");
					
					
					
				} else {
					iMsg("사용자가 결제를 취소했습니다.");
					return;
				}
			}
		}), "South");
		pane.setBorder(BorderFactory.createEmptyBorder());
		c.setPreferredSize(new Dimension(260, totalWidth));

		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

		setVisible(true);
	}

	class Card extends JLabel {

		Color col;
		StringBuffer num;
		int pw;
		int no;

		public Card(int no, String name, String num, int pw) {
//			setOpaque(true);
			this.pw = pw;
			this.no = no;
			col = Color.BLUE;
			this.num = new StringBuffer(num);
			this.num.insert(4, "-");
			this.num.insert(9, "-");
			this.num.insert(14, "-");

			setText("<html><font face = \"맑은 고딕\"; size = \"5\"; color = \"white\"><b>" + name + "</b><br>"
					+ "</font><font face = \"맑은고딕\"; size = \"4\"; color = \"white\";>" + this.num + "<br>" + uname);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			RoundRectangle2D rectangle2d = new RoundRectangle2D.Float(0, 0, getWidth(), 120, 20f, 20f);
			g2.setColor(col);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.fill(rectangle2d);
			super.paintComponent(g2);
		}

	}

}
