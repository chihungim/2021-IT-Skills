package View;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MyPage extends BaseFrame {

	String cap[] = "���̵�,��ֹ�ȣ,�̸�,�������,�޴���ȭ".split(",");
	String bcap[] = "ȸ������ ����,���� ����,Ƽ�����".split(",");
	JTextField txt[] = { new JTextField(15), new JPasswordField(15), new JTextField(15), new JTextField(5),
			new JTextField(5), new JTextField(5), new JTextField(7), new JTextField(7), new JTextField(7) };

	public MyPage() {
		super("����������", 400, 300);

		var c = new JPanel(new GridLayout(0, 1));
		var w = new JPanel(new FlowLayout());

		add(c);
		add(size(w,(int)(getWidth()*0.2),getHeight()), "West");

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			c.add(tmp);
			tmp.add(size(lbl(cap[i], JLabel.LEFT), 50, 20));
			if (i > 4)
				continue;
			tmp.add(txt[i]);
		}

		for (String bc : bcap) {
			w.add(size(btn(bc, a -> {

			}), (int) (getWidth() * 0.3), (int) (getHeight() * 0.1)));
		}

		setVisible(true);
	}

	public static void main(String[] args) {
		new MyPage();
	}
}
