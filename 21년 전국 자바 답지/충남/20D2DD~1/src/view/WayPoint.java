package view;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class WayPoint extends JDialog {
	JTextPane txt;
	JScrollPane scr;
	String way = "";
	ArrayList<String> ways = new ArrayList<String>();
	ArrayList transPoint = new ArrayList<>();
	Reserve r;
	int transidx = 0, min = 0;

	public WayPoint(Reserve r) {
		super(r, true);
		setTitle("°æÀ¯Áö");
		this.r = r;
		setSize(600, 400);
		setLocationRelativeTo(null);

		add(scr = new JScrollPane(txt = new JTextPane()));
		txt.setOpaque(false);
		txt.setContentType("text/html");
		txt.setEditable(false);
		for (int i = 0; i < r.path.size(); i++) {
			if (i == 0)
				way += BaseFrame.stations.get(r.path.get(i)) + " - Ãâ¹ß</b><br>";
			else if (i == r.path.size() - 1)
				way += "<b>" + BaseFrame.stations.get(r.path.get(i)) + " - µµÂø <br>";
			else
				way += BaseFrame.stations.get(r.path.get(i)) + "<br>";
		}

		for (var w : way.split("<br>")) {
			ways.add(w);
		}

		for (int i = 0; i < r.ho.size(); i++) {
		}

		int temp = r.ho.get(0);
		transidx = r.ho.get(0);

		for (int i = 0; i < r.ho.size(); i++) {
			temp = r.ho.get(i);
			if (transidx != temp) {
				if (ways.get(i - 1).matches(".*Ãâ¹ß.*"))
					min = 10;
				ways.add(i, "<b>" + ways.get(i - 1).substring(0, ways.get(i - 1).length() - min + 1) + " - ÇÏÂ÷</b>");
				ways.add(i + 1, "<b>" + ways.get(i - 1).substring(0, ways.get(i - 1).length() - min + 1) + " - È¯½Â"
						+ BaseFrame.lineN.get(r.path.get(i) + "," + r.path.get(i + 1)) + "(À¸)·Î È¯½Â</b>");
				transidx = temp;
			}
		}

		for (int i = 0; i < ways.size(); i++) {
			if (ways.get(i).contains("<b>"))
				ways.set(i, "<b>" + (i + 1) + ". " + ways.get(i).substring(3, ways.get(i).length()));
			else
				ways.set(i, (i + 1) + ". " + ways.get(i));
		}

		way = "<font face = \"¸¼Àº °íµñ\">" + String.join("<br>", ways);
		txt.setText(way);
	}

	public static void main(String[] args) {
		new Map();
	}

}
