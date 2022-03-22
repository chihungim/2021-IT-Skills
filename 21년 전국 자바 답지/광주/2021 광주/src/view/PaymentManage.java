package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class PaymentManage extends BaseDialog {
	JPanel c = new JPanel(new FlowLayout(1)), ea = new JPanel(new GridLayout(0, 1, 0, 5));
	JLabel plus = lbl("+", 0, 60);
	JScrollPane scr;
	GradientPaint blue = new GradientPaint(0, 0, new Color(49, 132, 237), 290, 130, new Color(89, 197, 241));
	String str[] = "ī�� �߱���,0000,0000,0000,0000,CVV (000/0000),00000".split(",");
	PlaceHolderField tt[] = new PlaceHolderField[7];

	public PaymentManage() {
		super("�������ܰ���", 350, 610);

		add(scr = new JScrollPane(c));

		plus.setBackground(Color.BLACK);
		plus.setForeground(Color.WHITE);
		plus.setOpaque(true);
		size(plus, 320, 135);

		setCard();

		size(ea, 350, 610);

		ea.add(tt[0] = new PlaceHolderField(15));
		JPanel temp = new JPanel(new GridLayout(1, 0, 5, 0));
		for (int i = 0; i < 4; i++) {
			temp.add(tt[i + 1] = new PlaceHolderField(10));
		}
		ea.add(temp);
		temp.setOpaque(false);
		ea.add(tt[5] = new PlaceHolderField(15));
		ea.add(tt[6] = new PlaceHolderField(15));
		ea.add(btn("���� ���� ����ϱ�", a -> {
			for (int i = 0; i < tt.length; i++) {
				if (tt[i].getText().isEmpty()) {
					eMsg("�� ĭ ���� �Է��ؾ� �մϴ�.");
					return;
				}
				if (i > 0 && i < 5) {
					if (tt[i].getText().length() != 4) {
						eMsg("ī�� ��ȣ�� �� �׸��� ���� 4�ڸ��� �����Ǿ�� �մϴ�.");
						return;
					}
				}
			}
			if (!tt[5].getText().matches("\\d{3,4}")) {
				eMsg("CVV��ȣ�� 3~4�ڸ� ���ڷ� �����Ǿ�� �մϴ�.");
				return;
			}
			if (!tt[6].getText().matches("\\d{6}")) {
				eMsg("������� ��й�ȣ�� 6�ڸ� ���ڷθ� �����Ǵ��� �մϴ�.");
				return;
			}
			String ca = "";
			for (int i = 1; i <= 4; i++) {
				ca += tt[i].getText();
			}
			try {
				if (!getOne("select * from payment where issuer = '" + tt[0].getText() + "' and card = '" + ca + "'")
						.equals("")) {
					eMsg("�̹� ��ϵǾ� �ִ� ī���Դϴ�.");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			iMsg("ī�� ����� �Ϸ��߽��ϴ�.");
			execute("insert into payment values(0,'" + tt[0].getText() + "','" + ca + "','" + tt[5].getText() + "','"
					+ tt[6].getText() + "'," + uno + ")");
			setCard();
			for (int i = 0; i < tt.length; i++) {
				tt[i].setText("");
			}
			remove(ea);
			setSize(350, 610);
		}));

		setBorder(ea, new EmptyBorder(10, 10, 400, 10));

		for (int i = 0; i < tt.length; i++) {
			tt[i].placeHolder = str[i];
		}

		plus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				add(ea, "East");
				setSize(700, 610);
			}
		});

		scr.setBorder(null);
		scr.setOpaque(false);
		c.setBackground(Color.WHITE);
		ea.setOpaque(false);

		setVisible(true);
	}

	private void setCard() {
		c.removeAll();
		int hei = 0;

		try {
			ResultSet rs = stmt.executeQuery("select ISUSER, card from payment where user = " + uno);
			while (rs.next()) {
				String name = rs.getString(1);
				StringBuffer num = new StringBuffer(rs.getString(2));
				num.insert(4, "-");
				num.insert(9, "-");
				num.insert(14, "-");
				JPanel temp = new JPanel() {
					Font f = new Font("���� ���", Font.BOLD, 15);
					Font f1 = new Font("���� ���", Font.PLAIN, 13);

					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						Graphics2D g2 = (Graphics2D) g;
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						g2.setPaint(blue);

						RoundRectangle2D rec = new RoundRectangle2D.Float(0, 0, 310, 130, 20, 20);
						g2.fill(rec);

						g2.setColor(Color.WHITE);
						g2.setFont(f);
						g2.drawString(name, 20, 40);
						g2.setFont(f1);
						g2.drawString(num.toString(), 20, 60);
						g2.drawString(uname, 20, 80);

					}
				};
				size(temp, 310, 135);
				temp.setOpaque(false);
				c.add(temp);
			}
			rs.last();
			hei = rs.getRow() * 140 + 140;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		c.add(plus);

		size(c, 310, hei);

		repaint();
		revalidate();
	}

	public static void main(String[] args) {
		uno = 1;
		uname = "���Ϸ�";
		new PaymentManage();
	}

}
