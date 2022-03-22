package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
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

	public MyPanel(JPanel _mp, LayoutManager _lo) {
		me = _mp;
		me.setLayout(_lo);
	}

	// 패널 생성
	void addPanel(Border bd, Color bg, int... loc) {
		JPanel _obj = new JPanel();
		_obj.setBackground(bg);
		_obj.setBorder(bd);

		if (me.getLayout() == null)
			_obj.setBounds(loc[0], loc[1], loc[2], loc[3]);
		else
			_obj.setPreferredSize(new Dimension(loc[2], loc[3]));

		panels.add(_obj);
		me.add(panels.get(panels.size() - 1));
	}

	// 버튼 생성
	void addButton(String title, Color bg, int... loc) {
		JButton _obj = new JButton(title);
		_obj.setBackground(bg);

		if (me.getLayout() == null)
			_obj.setBounds(loc[0], loc[1], loc[2], loc[3]);
		else
			_obj.setPreferredSize(new Dimension(loc[2], loc[3]));

		btns.add(_obj);
		me.add(btns.get(btns.size() - 1));
	}

	// 레이블 생성
	void addLabel(String title, Color fg, String imgpath, int textalign, int style, int size, int... loc) {
		JLabel _obj = new JLabel(title, textalign);
		_obj.setFont(new Font("맑은 고딕", style, size));
		_obj.setForeground(fg);

		if (imgpath != "")
			_obj.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(imgpath).getScaledInstance(loc[2], loc[3],
					Image.SCALE_SMOOTH)));

		if (me.getLayout() == null)
			_obj.setBounds(loc[0], loc[1], loc[2], loc[3]);
		else
			_obj.setPreferredSize(new Dimension(loc[2], loc[3]));

		lbls.add(_obj);
		me.add(lbls.get(lbls.size() - 1));
	}

	void alignLabelimgtxt(JLabel _obj, int halign, int valign) {
		_obj.setHorizontalTextPosition(halign);
		_obj.setVerticalTextPosition(valign);
	}

	void addInfoBoxLabel(Color fg, String imgpath, String txt, int fontstyle, int fontsize, int... imgsize) {
		JLabel _obj = new JLabel(txt);
		_obj.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(imgpath).getScaledInstance(imgsize[0],
				imgsize[1], Image.SCALE_SMOOTH)));
		_obj.setFont(new Font("맑은 고딕", fontstyle, fontsize));
		_obj.setForeground(fg);

		lbls.add(_obj);
		me.add(lbls.get(lbls.size() - 1));
	}

	// 텍스트박스 생성
	void addTextField(String title, Color bg, int... loc) {
		JTextField _obj = new JTextField(title);
		_obj.setBackground(bg);

		if (me.getLayout() == null)
			_obj.setBounds(loc[0], loc[1], loc[2], loc[3]);
		else
			_obj.setPreferredSize(new Dimension(loc[2], loc[3]));

		tfs.add(_obj);
		me.add(tfs.get(tfs.size() - 1));
	}

	// 비밀번호필드 생성
	void addPasswordField(String title, Color bg, int... loc) {
		JPasswordField _obj = new JPasswordField(title);
		_obj.setBackground(bg);

		if (me.getLayout() == null)
			_obj.setBounds(loc[0], loc[1], loc[2], loc[3]);
		else
			_obj.setPreferredSize(new Dimension(loc[2], loc[3]));

		tfs.add(_obj);
		me.add(tfs.get(tfs.size() - 1));
	}
}
