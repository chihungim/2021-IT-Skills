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
		setTitle("경유지");
		setSize(600, 400);
		setLocationRelativeTo(null);
		this.r = r;

		add(scr = new JScrollPane(tt = new JTextPane()));
		tt.setOpaque(false);
		tt.setContentType("text/html");
		tt.setEditable(false);

		// 요렇게 하면 더 간단하지 않을까
		for (int i = 0, num = 1; i < r.path.size(); i++, num++) {
			String sname = BaseFrame.stNames.get(r.path.get(i));
			if (i == 0)
				str = "<b>" + (num) + ". " + sname + " - 출발</b><br>";
			else if (r.timeTable[i][2] == 1) {
				str += "<b>" + (num) + ". " + sname + " - 하차</b><br>";
				str += "<b>" + (++num) + ". " + sname + " - 환승 "
						+ BaseFrame.mNames.get(BaseFrame.lineDim[r.path.get(i)][r.path.get(i + 1)]) + "(으)로 환승</b><br>";
			} else if (i == r.path.size() - 1)
				str += "<b>" + (num) + ". " + sname + " - 도착</b>";
			else
				str += (num) + ". " + sname + "<br>";
		}

		tt.setText("<font face = \"맑은 고딕\">" + str);

		setVisible(true);
	}

}
