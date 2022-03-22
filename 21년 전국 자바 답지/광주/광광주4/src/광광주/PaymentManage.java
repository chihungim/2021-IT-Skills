package 광광주;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import 광광주.PaymentManage.CardPanel.Card;

public class PaymentManage extends BaseFrame {

	ArrayList<광광주.PaymentManage.CardPanel.Card> cards = new ArrayList<>();
	CardPanel cardPanel;
	CardInfo info;
	int totalHeight;
	public PaymentManage() {
		super("결제수단 관리", 265, 600);
		setLayout(null);

		add(cardPanel = new CardPanel());
		add(info = new CardInfo());

		cardPanel.setBounds(0, 0, 250, 0);
		info.setBounds(250, 0, 250, 0);
		setHeight();
		
		setVisible(true);
	}

	void setHeight() {
		this.setSize(265, totalHeight);
		cardPanel.setBounds(0, 0, 250, totalHeight);
		info.setBounds(250, 0, 250, totalHeight);
		revalidate();
		repaint();
	}

	class CardInfo extends JPanel {

		PlaceHolderTextField txt[] = new PlaceHolderTextField[7];
		String holders[] = "카드 발급자,0000,0000,0000,0000,CVC (000/0000),000000".split(",");

		JButton buttonForRegisterUserCard;

		int col[] = { 19, 4, 4, 4, 4, 19, 19 };
		{
			for (int i = 0; i < txt.length; i++) {
				txt[i] = new PlaceHolderTextField(col[i]);
				txt[i].setPlaceHolder(holders[i]);
			}
		}

		public CardInfo() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			for (int i = 0; i < txt.length; i++) {
				add(txt[i]);
			}
			add(buttonForRegisterUserCard = btn("간편결제 등록하기", a -> {
				for (int i = 0; i < txt.length; i++) {
					if (txt[i].getText().isEmpty()) {
						eMsg("빈 칸 없이 입력해야합니다.");
						return;
					}
				}

				for (int i = 1; i < 5; i++) {
					if (txt[i].getText().matches(".*[^0-9].*") || txt[i].getText().length() != 4) {
						eMsg("카드 번호의 각 항목은 숫자 4자리로 구성되어야 합니다.");
						return;
					}
				}

				if (txt[5].getText().matches(".*[^0-9].*")
						|| !(txt[5].getText().length() > 2 && txt[5].getText().length() < 5)) {
					eMsg("CVC번호는 3~4자리 숫자로 구성되어야 합니다.");
					return;
				}

				if (txt[6].getText().matches(".*[^0-9].*") || txt[6].getText().length() != 6) {
					eMsg("카드 비밀번호는 6자리 숫자로만 구성되어야 합니다.");
					return;
				}

				for (Card c : cards) {
					if (c.name.equals(txt[0].getText()) && c.originalNum
							.equals(txt[1].getText() + txt[2].getText() + txt[3].getText() + txt[4].getText()))
						eMsg("이미 등록되있는 카드입니다.");
				}

				iMsg("카드 등록을 완료했습니다.");

				execute("insert payment values(0,'" + txt[0].getText() + "','"
						+ (txt[1].getText() + txt[2].getText() + txt[3].getText() + txt[4].getText()) + "','"
						+ txt[5].getText() + "','" + txt[6].getText() + "'," + uno + ")");

				String num = txt[1].getText() + txt[2].getText() + txt[3].getText() + txt[4].getText();
				cardPanel.addCard(txt[0].getText(), num);
				PaymentManage.this.setSize(270, totalHeight);
			}));

			PaymentManage.size(buttonForRegisterUserCard, 220, 25);
		}
	}

	class CardPanel extends JPanel {

		JButton bigPlusButtonForRegisterCard;

		public CardPanel() {
			setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			try {
				var rs = stmt.executeQuery("select * from payment where user = " + uno);
				while (rs.next()) {
					Card card = new Card(rs.getString(2), rs.getString(3));
					cards.add(card);
					add(card);
					totalHeight += 200;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			add(bigPlusButtonForRegisterCard = btn("+", a -> {
				PaymentManage.this.setSize(500, totalHeight                                  );
			}));

			PaymentManage.size(bigPlusButtonForRegisterCard, 240, 120);
			bigPlusButtonForRegisterCard.setBackground(Color.BLACK);
			bigPlusButtonForRegisterCard.setFont(new Font("맑은 고딕", Font.PLAIN, 100));
			bigPlusButtonForRegisterCard.setVerticalAlignment(SwingConstants.CENTER);
			bigPlusButtonForRegisterCard.setRolloverEnabled(false);
			bigPlusButtonForRegisterCard.setFocusPainted(false);
		}

		void addCard(String name, String num) {
			this.removeAll();
			cards.add(new Card(name, num));
			for (Card c : cards) {
				add(c);
			}
			add(bigPlusButtonForRegisterCard = btn("+", a -> {
				PaymentManage.this.setSize(500, 600);
			}));
			bigPlusButtonForRegisterCard.setBackground(Color.BLACK);
			bigPlusButtonForRegisterCard.setFont(new Font("맑은 고딕", Font.PLAIN, 100));
			bigPlusButtonForRegisterCard.setVerticalAlignment(SwingConstants.CENTER);
			bigPlusButtonForRegisterCard.setRolloverEnabled(false);
			bigPlusButtonForRegisterCard.setFocusPainted(false);
		}

		class Card extends JLabel {

			Color col;
			StringBuffer num;
			String originalNum;;
			String name;

			public Card(String name, String num) {
				this.name = name;
				this.originalNum = num;
				col = Color.BLUE;
				this.num = new StringBuffer(num);
				this.num.insert(4, "-");
				this.num.insert(9, "-");
				this.num.insert(14, "-");
				setPreferredSize(new Dimension(240, 120));
				setText("<html><font face = \"맑은 고딕\"; size = \"5\"; color = \"white\"><b>" + name + "</b><br>"
						+ "</font><font face = \"맑은고딕\"; size = \"4\"; color = \"white\";>" + this.num + "<br>"
						+ uname);
			}

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				RoundRectangle2D rectangle2d = new RoundRectangle2D.Float(0, 0, 240, 120, 20f, 20f);
				g2.setColor(col);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.fill(rectangle2d);
				super.paintComponent(g2);
			}

		}
	}

	public static void main(String[] args) {
		uno = 1;
		new PaymentManage();
	}
}
