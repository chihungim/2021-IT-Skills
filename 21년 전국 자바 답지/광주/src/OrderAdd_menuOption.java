

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

public class OrderAdd_menuOption extends Basedialog {
	ButtonGroup bg0, bg1 = new ButtonGroup(), bg2 = new ButtonGroup();
	ArrayList<JRadioButton> jrbList;
	JButton jbDim[]=new JButton[6];
	int menuno;
	
	public OrderAdd_menuOption(int _menuno)  throws SQLException {
		super("�ֹ�ǥ�� �߰�", 400, 650);
		setModal(true);
		
		//�ʱ�ȭ
		menuno = _menuno;
		dataInit();
		
		//��� �̹���: JLabel�� �����̳ʷ� �����
		np.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		np.add(jl1 = new JLabel(""));
		
		jl1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		jl1.setPreferredSize(new Dimension(400, 400));
		jl1.add(jl2 = new JLabel(img("�޴�/"+_menuno+".png", 400, 350)));
		jl1.add(jl3 = new JLabel("<html><font size=5>" + menuInfoDim[_menuno][1] + "</font><br><font size=3>" + menuInfoDim[_menuno][6] + "</font>"));
		
		//UI��ġ: �ɼ�, ����
		add(jsp = new JScrollPane(cp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

		cp.setLayout(new GridLayout(0, 1));
		cp.removeAll();
		
		int _idx=0;
		String _str1="";
		jrbList = new ArrayList<JRadioButton>();
		
		rs = stmt.executeQuery("SELECT * FROM options o, menu m where o.menu=m.no and m.no=" + _menuno);
		while(rs.next()) {
			_idx++;
			if(_str1.equals("") || !_str1.equals(rs.getString("o.title"))) {
				cp.add(labelB(rs.getString("o.title"), JLabel.LEFT, 13));
				_str1 = rs.getString("o.title");
			}
			
			if(rs.getString("o.title").equals("�ɼ�")) bg0 = bg1;
			else bg0 = bg2;
			
			cp.add(jl1=new JLabel(""));
			jl1.setLayout(new GridLayout(1, 2));
			jl1.add(jrb1 = new JRadioButton(rs.getString("o.name")));
			jl1.setPreferredSize(new Dimension(350, 30));
			
			bg0.add(jrb1);
			
			jrbList.add(jrb1);
			jrb1.setName(rs.getString("o.no"));		//�ɼ� ID�� ����Ѵ�.
			jrb1.setBackground(Color.WHITE);
			jl1.add(label(rs.getInt("o.price")==0?"�߰���� ����":"+" + rs.getString("o.price"),JLabel.RIGHT));
			
			emp(cp, 0, 10, 0, 10);
		}
		
		if(_idx==0) {
			cp.add(size(jl1 = labelB("�ɼ��� ���� ��ǰ�Դϴ�.", 0, 15), 400, 200));
			jl1.setForeground(Color.RED);
			jl1.setVerticalAlignment(JLabel.TOP);
		}
		
		//UI��ġ: ��� ����
		sp.setLayout(new FlowLayout());
		sp.add(jbDim[0]=button("-", 50, 30, Color.GREEN));
		sp.add(jl4=label("1", JLabel.CENTER, 13, 60, 30));
		sp.add(jbDim[1]=button("+", 50, 30, Color.GREEN));
		sp.add(jbDim[2]=button("1���� �ֹ�ǥ�� �߰��ϱ�", 180, 30, Color.GREEN));
		
		for(int i=0; i<3; i++) {
			jbDim[i].addActionListener(this);
		}

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(jbDim[0])) {
			int _cnt = toint(jl4.getText())-1;
			jl4.setText(_cnt + "");
			jbDim[2].setText(_cnt + "���� �ֹ�ǥ�� �߰��ϱ�");
		}
		if(e.getSource().equals(jbDim[1])) {
			int _cnt = toint(jl4.getText())+1;
			jl4.setText(_cnt + "");
			jbDim[2].setText(_cnt + "���� �ֹ�ǥ�� �߰��ϱ�");
		}
		if(e.getSource().equals(jbDim[2])) {

			//������ �ɼ� ���
			int _sum1=(int)menuInfoDim[menuno][5], _sum2=0;
			
			ArrayList<Object> subList = new ArrayList<Object>();
			shop.orderList.add(subList);
			subList.add(menuno);
			for(int i=0; i<jrbList.size(); i++) {
				if(jrbList.get(i).isSelected()) {
					int _optID = toint(jrbList.get(i).getName());
					System.out.println(i + "��ư, _optID:" + _optID);
					subList.add(_optID);
					_sum2 += (int)optionDim[_optID][3];
				}
			}
			
			//���ż����� ���űݾ� ���: (�޴� ���� + �ɼ� ����)*����
			subList.add(toint(jl4.getText()));
			subList.add((_sum1+_sum2)*toint(jl4.getText()));
			
			//�ֹ��� �޴� ����
			shop.jl3.setText("�ֹ�ǥ (" + shop.orderList.size() + ")");

			//�� �ݱ�
			dispose();
			
			//�׽�Ʈ ���
			String _str4="";
			int _size = subList.size();
			//[_size-2]�� ���ż���, [_size-1]�� ���űݾ�
			for(int i=2; i<_size-2; i++) {			
				System.out.println((int)subList.get(i));
				_str4 += optionDim[(int)subList.get(i)][2] + ",";
			}
			System.out.println(_size + "," + menuno + "," + _str4 + "," + (int)subList.get(_size-1) + "," + (_sum1+_sum2)*toint(jl4.getText()));
		}
	}
	
	public static void main(String[] args) {
		try {
			new OrderAdd_menuOption(208);
		} catch (SQLException e) {
		}
	}
}
