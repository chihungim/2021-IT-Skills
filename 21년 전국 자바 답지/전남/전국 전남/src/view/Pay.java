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

	String cap[] = "��¥,����,�׸�,�ð�,����,�ο���,�ѱݾ�".split(",");

	JTextField p;

	String rdate;
	String rTime;
	JComponent args[];

	JLabel sumlbl;

	public Pay(String rdate, String rTime) {
		super("����", 350, 400);

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
		n.add(lbl("����", 0, 30));
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
					eMsg("���ڴ� �Է��� �Ұ��մϴ�.");
					p.setText("");
					sumlbl.setText("0");
					return;
				}

				if (p.getText().equals("")) {
					sumlbl.setText("0");
					return;
				}

				if (toInt(p.getText()) <= 0) {
					eMsg("�ο����� 1����� �����մϴ�.");
					p.setText("");
					sumlbl.setText("0");
					return;
				}

				if (toInt(tpersonnel) < toInt(p.getText())) {
					eMsg("�ش� �ο����� �ʰ��߽��ϴ�.");
					p.setText("");
					sumlbl.setText("0");
					return;
				}

				sumlbl.setText(new DecimalFormat("#,##0").format(toInt(cprice) * toInt(tpersonnel)));
			}

		});

		for (var str : "����,����".split(",")) {
			s.add(btn(str, a -> {
				if (a.getActionCommand().equals("����")) {
					if (sumlbl.getText().equals("0")) {
						eMsg("�ο����� �Է����ּ���.");
						return;
					}

					execute("insert reservation values(0," + uno + ",'" + cno + "','" + tno + "','" + rdate + "','"
							+ rTime + "'," + p.getText() + "," + 0 + ")");
					isPayed = true;
					iMsg("���̽� �������� ����Ż���� " + rTime + "�� ����Ǿ����ϴ�\n�ѱݾ�:+" + sumlbl.getText());
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
