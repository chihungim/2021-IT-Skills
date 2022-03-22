package View;

import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Waypoint extends JDialog {

	String wayPoints[];
	JScrollPane pane;
	JPanel c;

	public Waypoint(Reserve r) {
		super(r, true);
		add(pane = new JScrollPane(c = new JPanel(new GridLayout(0, 1))));
		setVisible(true);
	}

}
