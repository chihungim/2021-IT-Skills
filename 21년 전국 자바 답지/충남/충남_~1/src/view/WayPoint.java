package view;

import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class WayPoint extends JDialog {
	
	JTextPane txt;
	JScrollPane scr;
	String str = "";
	
	
	public WayPoint(Reserve r) {
		this.setTitle("������");
		this.setModal(true);
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		
		this.add(scr = new JScrollPane(txt = new JTextPane()));
		txt.setOpaque(false);
		txt.setContentType("text/html");
		txt.setEditable(false);
		
		for (int i = 0, n = 1; i < r.path.size(); i++, n++) {
			String st = BaseFrame.stNames.get(r.path.get(i));
			if (i==0) str = "<b>" + n +". " + st + " - ���</b><br>";
			else if (r.timetable[i][2] == 1) {
				str += "<b>"+n+". "+st+" - ����</b><br>";
				str += "<b>"+(++n)+". "+st+" - ȯ�� "+BaseFrame.metroNames.get(BaseFrame.lineDim[r.path.get(i)][r.path.get(i+1)])+"(��)�� ȯ��</b><br>";
			}
			else if (i == r.path.size()-1) str +="<b>"+n+". "+st+" - ����</b>";
			else str += n+". "+st+"<br>";
		}
		
		txt.setText("<font face = '���� ���'>"+str);
		
		this.setVisible(true);
	}
	
}
