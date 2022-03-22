import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

public class Search extends Basedialog {
	JComboBox<String> combo = new JComboBox<String>();
	JRadioButton radio[] = {
			new JRadioButton("�⺻ ����"),
			new JRadioButton("���� ����"),
			new JRadioButton("��� ��� ����")
	};

	JPanel base = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new BorderLayout()), w = new JPanel(new GridLayout(0, 1, 0, 10)), c = new JPanel(new GridLayout(0, 3, 10, 10));
	ButtonGroup bg = new ButtonGroup();
	HashMap<Integer, JLabel> hash = new HashMap<Integer, JLabel>();

	//����
	String str[] = "no asc,a2 desc,deliveryfee asc".split(",");
	String opt = str[0];
	
	public Search(int cate) {
		super("�˻�", 1000, 560);

		phText = "�˻��� ������ �̸��� �Է��ϼ���.,�ּ� ����,�ִ� ����".split(",");
		
		//������ �ʱ�ȭ
		try {
			dataInit();
		} catch (SQLException e) {
		}
		
		//��ġ1
		size(np, 0, 50);
		np.add(jtf1 = textPH(0, 15, 0, 0));
		jtf1.addKeyListener(this);

		//��ġ2
		size(wp, 200, 0);
		wp.setLayout(new GridLayout(0,1));
		wp.add(combo);
		combo.removeAllItems();
		for(int i=0; i<catList.size(); i++)
			combo.addItem(catList.get(i)+"");
		
		wp.add(label("����", JLabel.LEFT, 13, 0, 0));
		for (int i = 0; i < radio.length; i++) {
			bg.add(radio[i]);
			wp.add(radio[i]);
			radio[i].setName(str[i]);
			radio[i].setOpaque(false);
			radio[i].addActionListener(a->{
				opt = ((JRadioButton)a.getSource()).getName();
			});
		}
		radio[0].setSelected(true);
		
		wp.add(label("���� ����", JLabel.LEFT, 13, 0, 0));
		wp.add(jtf2 = textPH(1, 15, 0, 0));
		wp.add(jtf3 = textPH(2, 15, 0, 0));
		wp.add(jb1 = button("����",0,0,Color.GREEN));
		
		emp(wp, 0, 0, 200, 0);
	
		cp.setLayout(new GridLayout(0, 3, 5, 5));
		jsp = new JScrollPane(cp, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.setAutoscrolls(true);
		add(jsp, "Center");
		
		setVisible(true);
		
		//�̺�Ʈ
		jb1.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(jb1)) search();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == e.VK_ENTER) search();
	}
	
	void search() {
		//�÷��̽� Ȧ�� ó��
		int _min=-1, _max=-1;
		if(jtf2.getText().equals(phText[toint(jtf2.getName())])) _min=0;
		if(jtf3.getText().equals(phText[toint(jtf3.getName())])) _max=1000000;

		//���� ó��
		if((_min!=0 && jtf2.getText().matches(".*\\D.*")) || (_max!=1000000 && jtf3.getText().matches(".*\\D.*"))) {
			errmsg("���� ���Ϳ��� ���ڸ� �Է� �����մϴ�.");
			return;
		}
		
		cp.removeAll();
		
		_min = _min!=0?toint(jtf2.getText()):_min;
		_max = _max!=1000000?toint(jtf3.getText()):_max;
		if(_min > _max) {
			errmsg("���� ������ �ִ� ���ݺ��� Ŭ �� �����ϴ�.");
			return;
		}
		
		//�˻�: Left Join
		String sub1 = "(Select seller s1, avg(price) a1, min(cooktime) mn, max(cooktime) mx from menu group by s1 having a1>=" + _min + " and a1<=" + _max + ") sub1";
		String sub2 = "(Select * from seller, " + sub1 + " where seller.no=sub1.s1) sub2";
		String sub3 = "(Select seller, avg(rate) a2 from review group by seller) sub3";
		String qry = "Select *, ifNull(a2,0) b1 from " + sub2 + " Left join " + sub3 + " on sub2.s1=sub3.seller order by " + opt;
		
		System.out.println(qry);
		
		try {
			ResultSet rs = stmt.executeQuery(qry);
			while(rs.next()) {
				final int _fid = rs.getInt(1);
				final String _fstr[] = {rs.getString("Name"), (new DecimalFormat("#,##0��").format(rs.getInt("DeliveryFee"))) + " ��� ������", 
								LocalTime.parse(rs.getString("mn")).getMinute() + "", LocalTime.parse(rs.getString("mx")).getMinute() + "��",
								"���� " + (new DecimalFormat("0.0").format(rs.getDouble("b1")))
						};

				cp.add(jl1 = new JLabel(img("./���/" + _fid + ".png", 240, 110)));
				jl1.setText("<html>" + _fstr[0] + "<br>" + _fstr[1] + " / " + _fstr[2] + " - " + _fstr[3] + " / " + _fstr[4]);
				jl1.setVerticalTextPosition(JLabel.BOTTOM);
				jl1.setHorizontalTextPosition(JLabel.CENTER);
				
				jl1.addMouseListener(new MouseAdapter() {
					String _str[] = _fstr;
					int _id = _fid;
					
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount()==2) {
//							new Shop(_id, _str);
						}
					}
				});
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		repaint();
		revalidate();
	}

	public static void main(String[] args) {
		new Search(0);
	}
}
