package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class MyPanel {
	JPanel me;

	ArrayList<JPanel> panels = new ArrayList<JPanel>();
	ArrayList<JButton> btns = new ArrayList<JButton>();
	ArrayList<JLabel> lbls = new ArrayList<JLabel>();
	ArrayList<JTextField> tfs = new ArrayList<JTextField>();
	ArrayList<JPasswordField> pfs = new ArrayList<JPasswordField>();

	public MyPanel(JPanel me, LayoutManager layout) {
		this.me = me;
		me.setBackground(Color.BLACK);
		me.setLayout(layout);
	}

	JPanel addPanel(Border bd, Color bg, int... loc) {
		var _obj = new JPanel();
		_obj.setBackground(bg);
		_obj.setBorder(bd);

		if (me.getLayout() == null)
			_obj.setBounds(loc[0], loc[1], loc[2], loc[3]);
		else
			_obj.setPreferredSize(new Dimension(loc[2], loc[3]));

		panels.add(_obj);
		me.add(panels.get(panels.size() - 1));
		return _obj;
	}

	JButton addButton(String title, Color bg, int... loc) {
		var _obj = new JButton(title);
		_obj.setBackground(bg);

		if (me.getLayout() == null)
			_obj.setBounds(loc[0], loc[1], loc[2], loc[3]);
		else
			_obj.setPreferredSize(new Dimension(loc[2], loc[3]));

		btns.add(_obj);
		me.add(btns.get(btns.size() - 1));
		return _obj;
	}

	JLabel addLabel(String title, Color fg, String imgpath, int textalign, int style, int size, int... loc) {
		var _obj = new JLabel(title, textalign);
		_obj.setFont(new Font("¸¼Àº °íµñ", style, size));
		_obj.setForeground(fg);

		if (imgpath != "")
			_obj.setIcon(new ImageIcon(java.awt.Toolkit.getDefaultToolkit().getImage(imgpath).getScaledInstance(loc[2],
					loc[3], Image.SCALE_SMOOTH)));

		if (me.getLayout() == null)
			_obj.setBounds(loc[0], loc[1], loc[2], loc[3]);
		else
			_obj.setPreferredSize(new Dimension(loc[2], loc[3]));

		lbls.add(_obj);
		me.add(lbls.get(lbls.size() - 1));
		return _obj;
	}

	JPasswordField addPasswordField(String title, Color bg, int... loc) {
		var _obj = new JPasswordField(title);
		_obj.setBackground(bg);

		if (me.getLayout() == null)
			_obj.setBounds(loc[0], loc[1], loc[2], loc[3]);
		else
			_obj.setPreferredSize(new Dimension(loc[2], loc[3]));

		tfs.add(_obj);
		me.add(tfs.get(tfs.size() - 1));
		return _obj;
	}

}
