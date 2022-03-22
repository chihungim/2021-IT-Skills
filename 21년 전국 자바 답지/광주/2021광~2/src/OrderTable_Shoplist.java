

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class OrderTable_Shoplist extends Basedialog {
	public OrderTable_Shoplist() {
		super("�ֹ�ǥ", 400, 650);
		
		//���� �ʱ�ȭ
		jlDim = new JLabel[10];
		
		//UI��ġ: �ֹ� ����
		np.add(jl1=labelB("�ֹ�ǥ", JLabel.LEFT, 40));
		jl1.setPreferredSize(new Dimension(350,  80));
		
		add(jsp=new JScrollPane(cp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		jsp.setPreferredSize(new Dimension(400,  450));
		cp.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//Shop�� orderList�� ������ ���� ǥ���Ѵ�.
		int _sum=uiUpdate();
	
		//UI��ġ
		sp.setLayout(new FlowLayout(FlowLayout.LEFT));
		sp.setPreferredSize(new Dimension(400, 100));
		
		sp.add(jl3=new JLabel(""));
		jl3.setLayout(new GridLayout(1, 2));
		jl3.add(labelB("���� �ݾ�", JLabel.LEFT, 15));
		jl3.add(jl4=label(_sum +"��", JLabel.RIGHT));
		jl3.setPreferredSize(new Dimension(350,  30));
		
		sp.add(jb1=button("�ֹ��ϱ�", 400, 30, Color.GREEN));
		
		setVisible(true);
	}
	
	int uiUpdate() {
		int _sum=0;
		for(int i=0; i<shop.orderList.size(); i++) {
			String _str="";
			ArrayList _subList = (ArrayList) shop.orderList.get(i);
			_str="<html><font size=4><b>" + menuInfoDim[(int)_subList.get(0)][1] + "<br>";   //_subList.get(0)�� �޴�ID
			
			int _optCnt=0, _size=_subList.size();
			//[_size-2]�� ���ż���, [_size-1]�� ���űݾ�
			for(int j=1; j<_size-2; j++) {
				if(++_optCnt>1) _str+=", ";
				_str+= optionDim[(int)_subList.get(j)][2];
			}
			_str+="<br>";
			_str+=_subList.get(_size-2) + "��, " + _subList.get(_size-1) + "��";

			//�Ѱ�
			_sum += (int)_subList.get(_size-1);
			
			cp.add(jlDim[i]=new JLabel(""));
			jlDim[i].setPreferredSize(new Dimension(400,  80));
			jlDim[i].setBorder(new MatteBorder(2, 2, 2, 2, Color.GRAY));
			
			jlDim[i].add(jl5=new JLabel(_str, JLabel.LEFT));
			jl5.setBounds(0, 0, 300, 80);

			jlDim[i].add(jl6=labelP("����", JLabel.RIGHT, 13));
			jl6.setName(i+"");								//orderList[���� idx�� ���
			jl6.setBounds(300, 50, 50, 25);
			
			jl6.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int _idx=toint(((JLabel)e.getSource()).getName());
					shop.orderList.remove(_idx);
					
					cp.removeAll();
					int _sum=uiUpdate();
					
					//�ֹ� ����, �ֹ� �ݾ� ����
					shop.jl3.setText("�ֹ�ǥ (" + shop.orderList.size() + ")");
					jl4.setText(_sum + "��");
				}
			});
			
			setVisible(true);
			cp.setPreferredSize(new Dimension(400, jlDim[i].getLocation().y + jlDim[i].getHeight() + 30));  //��ũ���� ����
			
			System.out.println(_str + "," + jlDim[i].getWidth() + "," + jlDim[i].getHeight());
		}
		
		repaint();
		revalidate();
		
		return _sum;
	}
}
