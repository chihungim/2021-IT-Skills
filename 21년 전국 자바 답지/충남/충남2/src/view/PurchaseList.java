package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.xml.parsers.DocumentBuilder;

public class PurchaseList extends BaseFrame {

	LocalDate today = LocalDate.now();
	JLabel date;
	JButton prev, next;
	JPanel c;

	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 MM월");

	DayBox box[] = new DayBox[42];

	public PurchaseList() {
		super("구매내역", 1000, 800);
		try {
			datainit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		add(n = new JPanel(new FlowLayout(FlowLayout.CENTER)), "North");
		add(c = new JPanel(new GridLayout(0, 7)));
		n.add(prev = btn("◀", a -> {
			today = today.plusMonths(-1);
			setDay();
		}));
		n.add(date = lbl(today.format(format), JLabel.CENTER));

		n.add(next = btn("▶", a -> {
			today = today.plusMonths(1);
			setDay();
		}));

		for (var cap : "일,월,화,수,목,금,토".split(",")) {
			var lbl = lbl(cap, JLabel.CENTER);
			if (cap.equals("일"))
				lbl.setForeground(Color.RED);
			if (cap.equals("토"))
				lbl.setForeground(Color.BLUE);
			c.add(lbl);
		}

		for (int i = 0; i < box.length; i++) {
			c.add(box[i] = new DayBox(i, ""));
			box[i].setBorder(new LineBorder(Color.BLACK));
		}
		setDay();
		setVisible(true);
	}

	public static void main(String[] args) {
		no = 1;
		new PurchaseList();
	}

	void setDay() {
		date.setText(today.format(format));
		int month = today.getMonthValue();
		int year = today.getYear();
		var sdate = LocalDate.of(year, month, 1);
		var sday = sdate.getDayOfWeek().getValue() % 7;

		for (int i = 0; i < 42; i++) {
			var tmp = sdate.plusDays(i - sday);
			try {
				var rs = stmt.executeQuery("select * from purchase where user = " + no + " and date = '" + tmp + "'");
				String str = "<html>";
				while (rs.next()) {
					str += stNames.get(rs.getInt(3)) + "→" + stNames.get(rs.getInt(4)) + "<br>";
				}
				box[i].setDayContent(tmp.getDayOfMonth(), str);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (tmp.getMonthValue() != today.getMonthValue()) {
				var me = box[i];
				var r = me.daylbl.getForeground().getRed();
				var g = me.daylbl.getForeground().getGreen();
				var b = me.daylbl.getForeground().getBlue();
				me.daylbl.setForeground(new Color(r, g, b, 120));
			} else {
				var me = box[i];
				var r = me.daylbl.getForeground().getRed();
				var g = me.daylbl.getForeground().getGreen();
				var b = me.daylbl.getForeground().getBlue();
				me.daylbl.setForeground(new Color(r, g, b, 255));
			}
		}
	}

	class DayBox extends JPanel {

		JLabel daylbl;
		JEditorPane area;

		public DayBox(int day, String content) {
			setLayout(new BorderLayout());
			add(daylbl = lbl(day + "", JLabel.RIGHT, 12), "North");
			if (day % 7 == 0)
				daylbl.setForeground(Color.RED);
			if (day % 7 == 6)
				daylbl.setForeground(Color.blue);

			add(new JScrollPane(area = new JEditorPane()));
			area.setText(content);
			area.setContentType("text/html");
		}

		void setDayContent(int day, String content) {
			daylbl.setText(day + "");
			area.setText(content);
		}
	}
}
