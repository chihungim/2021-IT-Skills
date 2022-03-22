package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Pay extends BaseFrame {

	String cap[] = "날짜,지점,테마,시간,가격,인원수,총금액".split(",");

	JTextField p;

	String rdate;
	String rTime;
	JComponent args[];

	JLabel sumlbl;

	public Pay(String rdate, String rTime) {
		super("결제", 350, 400);

		this.rdate = rdate;
		this.rTime = rTime;

		args = new JComponent[] { lbl(rdate, JLabel.CENTER), lbl(cname, JLabel.CENTER), lbl(tname, JLabel.CENTER),
				lbl(rTime, JLabel.CENTER), lbl(new DecimalFormat("#,##0").format(toInt(cprice)), JLabel.CENTER),
				p = new JTextField(20), sumlbl = lbl("0", JLabel.LEFT) };

		setUI();
		setVisible(true);
	}

	void setUI() {
		var n = new JPanel(new BorderLayout());
		var c = new JPanel(new GridLayout(0, 1));
		var s = new JPanel(new FlowLayout());

		add(n, "North");
		add(c);
		add(s, "South");
		n.add(lbl("결제", 0, 30));
		n.add(lbl("P A Y M E N T", 0, 20), "South");

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			tmp.add(lbl(cap[i], JLabel.LEFT));
			tmp.add(args[i]);
			tmp.setOpaque(false);
			c.add(tmp);
		}

		p.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (p.getText().matches(".*[^0-9].*")) {
					eMsg("문자는 입력이 불가합니다.");
					p.setText("");
					sumlbl.setText("0");
					return;
				}

				if (p.getText().equals("")) {
					sumlbl.setText("0");
					return;
				}

				if (toInt(p.getText()) <= 0) {
					eMsg("인원수는 1명부터 가능합니다.");
					p.setText("");
					sumlbl.setText("0");
					return;
				}

				if (toInt(tpersonnel) < toInt(p.getText())) {
					eMsg("해당 인원수를 초과했습니다.");
					p.setText("");
					sumlbl.setText("0");
					return;
				}

				sumlbl.setText(new DecimalFormat("#,##0").format(toInt(cprice) * toInt(tpersonnel)));
			}

		});

		for (var str : "결제,이전".split(",")) {
			s.add(btn(str, a -> {
				if (a.getActionCommand().equals("결제")) {
					if (sumlbl.getText().equals("0")) {
						eMsg("인원수를 입력해주세요.");
						return;
					}

					execute("insert reservation values(0," + uno + ",'" + cno + "','" + tno + "','" + rdate + "','"
							+ rTime + "'," + p.getText() + "," + 0 + ")");
					isPayed = true;
					iMsg("에이스 강남점의 감옥탈출이 " + rTime + "에 예약되었습니다\n총금액:+" + sumlbl.getText());
					dispose();					
				} else {
					dispose();
				}
			}));
		}

		c.setOpaque(true);
		c.setBackground(Color.BLACK);
		s.setOpaque(true);
		s.setBackground(Color.BLACK);
	}

	@Override
	public JLabel lbl(String cap, int alig) {
		var lbl = super.lbl(cap, alig);
		lbl.setForeground(Color.WHITE);
		return lbl;
	}

}
