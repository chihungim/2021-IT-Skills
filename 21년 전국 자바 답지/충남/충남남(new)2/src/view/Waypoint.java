package view;

import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Waypoint extends JDialog {

	JTextPane textPane;
	JScrollPane scrollPane;
	String way = "";
	ArrayList<String> ways = new ArrayList<>();
	ArrayList tranferpoint = new ArrayList<>();;
	Reserve r;
	int transIdx = 0, minus = 0;

	public Waypoint(Reserve r) {
		super(r, true);
		this.r = r;
		setSize(600, 400);
		setLocationRelativeTo(null);

		add(scrollPane = new JScrollPane(textPane = new JTextPane()));
		textPane.setOpaque(false);
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		for (int i = 0; i < r.cpath.size(); i++) {
			if (i == 0)
				way += "<b>" + BaseFrame.stations.get(r.cpath.get(i)) + " - ��� </b><br>";
			else if (i == r.cpath.size() - 1)
				way += "<b>" + BaseFrame.stations.get(r.cpath.get(i)) + "-���� <br>";
			else
				way += BaseFrame.stations.get(r.cpath.get(i)) + "<br>";
		}

		for (String w : way.split("<br>")) {
			ways.add(w);
		}

		int tmp = r.ho.get(0);
		transIdx = r.ho.get(0);
		System.out.println(ways);
		for (int i = 0; i < r.ho.size(); i++) {
			tmp = r.ho.get(i);
			if (transIdx != tmp) {
				if (ways.get(i - 1).matches(".*���.*"))
					minus = 10;
				ways.add(i, "<b>" + ways.get(i - 1).substring(0, ways.get(i - 1).length() - minus) + " - ����</b>");
				ways.add(i + 1, "<b>" + ways.get(i - 1).substring(0, ways.get(i - 1).length() - minus) + " - ȯ��"
						+ BaseFrame.lineName.get(r.cpath.get(i) + "," + r.cpath.get(i + 1)) + "(��)�� ȯ��</b>");
				transIdx = tmp;
			}
		}

		for (int i = 0; i < ways.size(); i++) {

			if (ways.get(i).contains("<b>"))
				ways.set(i, "<b>" + (i + 1) + ". " + ways.get(i).substring(3, ways.get(i).length()));
			else
				ways.set(i, (i + 1) + ". " + ways.get(i));
		}

		way = "<font face =\"���� ����\">" + String.join("<br>", ways);

		textPane.setText(way);

	}
}
