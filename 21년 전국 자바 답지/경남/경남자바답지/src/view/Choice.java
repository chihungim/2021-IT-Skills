package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import db.DBManager;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class Choice extends BaseFrame implements ItemListener {

	JTextField search = new JTextField(18);
	JPanel kind, job;

	ArrayList<ArrayList<JCheckBox>> chk = new ArrayList<ArrayList<JCheckBox>>(); 
	ArrayList<String> cap = new ArrayList<String>();
	HashSet<String> hash = new HashSet<String>();
	int cnt;

	public Choice(String name, JTextField txt) {
		super("직종 선택", 700, 500);

		if (!name.contentEquals("채용정보")) {
			cnt= 1;
		} else cnt= 3;

		this.add(c = new JPanel(new BorderLayout()));
		this.add(e = new JPanel(new BorderLayout()), "East");

		var c_n = new JPanel(new FlowLayout(0));
		c.add(c_n, "North");
		c_n.add(search);
		c_n.add(sz(btn("검색", a -> {
			search();
		}), 80, 25));

		c.add(new JScrollPane(kind = new JPanel(new GridLayout(0, 1))));
		
		sz(e, 250, 1);

		e.add(new JLabel("선택한 직종", 0), "North");
		e.add(new JScrollPane(job = new JPanel(new FlowLayout(0, 0, 0))));

		var s_s = new JPanel(new FlowLayout(2));
		e.add(s_s, "South");
		s_s.add(sz(btn("선택 완료", a -> {
			
			if (name.contentEquals("채용정보")) {
				if (job.getComponentCount()>3) {
					eMsg("직종을 3개 이하로 선택해주세요.");
					return;
				}
			} else {
				if (job.getComponentCount()>=2) {
					eMsg("하나만 선택해주세요.");
					return;
				}
			}
			
			ArrayList<String> list = new ArrayList<String>();
			for (var p : job.getComponents()) {
				list.add(((ItemPanel)p).cate);
			}
			
			txt.setText(String.join(",", list));
			this.dispose();
			
		}), 120, 25));
		
		search();

		setEmpty(kind, 10, 10, 10, 10);
		setEmpty(c, 0, 0, 0, 10);
		setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);
		this.setVisible(true);
	}
	
	void search() {
		kind.removeAll();
		chk.clear();
		cap.clear();
		try {
			var rs = DBManager.rs("SELECT t.t_no, d_no, d.name FROM details d, type t where d.t_no=t.t_no and d.name like '%"+search.getText()+"%'");
			int idx=1, i;
			ArrayList<JCheckBox> list = new ArrayList<JCheckBox>();
			while (rs.next()) {
				if (idx!=rs.getInt(1)) {
					idx = rs.getInt(1);
					chk.add(list);
					list = new ArrayList<JCheckBox>();
				}
				list.add(new JCheckBox(rs.getString(3)));
			}
			chk.add(list);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			var rs = DBManager.rs("SELECT distinct t.name FROM details d, type t where d.t_no=t.t_no and d.name like '%"+search.getText()+"%'");
			while (rs.next()) {
				cap.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < cap.size(); i++) {
			var tmp = new JPanel(new BorderLayout());
			var grid = new JPanel(new GridLayout(0, 2, 0, 0));
			tmp.add(lbl(cap.get(i), 2, 20), "North");
			tmp.add(grid);
			for (int j = 0; j < chk.get(i).size(); j++) {
				grid.add(chk.get(i).get(j));
				chk.get(i).get(j).addItemListener(this);
			}
			sz(tmp, 1, 130);
			kind.add(tmp);
		}
		repaint();
		revalidate();
	}

	public static void main(String[] args) {
		new Register();
	}
	
	void updatePanel() {
		job.setPreferredSize(new Dimension(1, 100*job.getComponentCount()));
		job.repaint();
		job.revalidate();
	}
	
	void chkFalse(String cate) {
		for (int i = 0; i < chk.size(); i++) {
			for (int j = 0; j < chk.get(i).size(); j++) {
				if (chk.get(i).get(j).getText().contentEquals(cate)) {
					chk.get(i).get(j).setSelected(false);
				}
			}
		}
	}
	

	class ItemPanel extends JPanel {

		String cate;
		
		public ItemPanel(String cate) {
			super(new BorderLayout());
			this.cate=cate;
			this.setPreferredSize(new Dimension(job.getWidth(), 70));
			this.add(new JLabel(cate), "West");
			this.add(sz(btn("X", a -> {
				job.remove(ItemPanel.this);
				chkFalse(cate);
				updatePanel();
			}), 50, 80), "East");
			this.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(10, 10, 10, 10)));

		}

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		
		var box = ((JCheckBox)e.getSource());
		
		if (e.getStateChange() == ItemEvent.SELECTED) {
			job.add(new ItemPanel(box.getText()));
			updatePanel();
			
		} else {
			for (var p : job.getComponents()) {
				var item = (ItemPanel)p;
				if (box.getText().contentEquals(item.cate)) {
					job.remove(p);
					updatePanel();
				}
			}
		}
		
	}

}
