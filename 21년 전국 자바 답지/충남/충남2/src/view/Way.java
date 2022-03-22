package view;

import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Way extends JDialog {
	JTextPane tt;
	JScrollPane scr;
	String str = "";
	ArrayList<String> way = new ArrayList<String>();
	ArrayList<String> transP = new ArrayList<String>();
	Reserve r;
	int min = 0;
	String tidx = "";

	public Way(Reserve r) {
		super(r, true);
		setTitle("������");
		setSize(600, 400);
		setLocationRelativeTo(null);
		this.r = r;

		add(scr = new JScrollPane(tt = new JTextPane()));
		tt.setOpaque(false);
		tt.setContentType("text/html");
		tt.setEditable(false);

		// �䷸�� �ϸ� �� �������� ������
		for (int i = 0, num = 1; i < r.path.size(); i++, num++) {
			String sname = BaseFrame.stNames.get(r.path.get(i));
			if (i == 0)
				str = "<b>" + (num) + ". " + sname + " - ���</b><br>";
			else if (r.timeTable[i][2] == 1) {
				str += "<b>" + (num) + ". " + sname + " - ����</b><br>";
				str += "<b>" + (++num) + ". " + sname + " - ȯ�� "
						+ BaseFrame.mNames.get(BaseFrame.lineDim[r.path.get(i)][r.path.get(i + 1)]) + "(��)�� ȯ��</b><br>";
			} else if (i == r.path.size() - 1)
				str += "<b>" + (num) + ". " + sname + " - ����</b>";
			else
				str += (num) + ". " + sname + "<br>";
		}

		tt.setText("<font face = \"���� ���\">" + str);

		setVisible(true);
	}

}
