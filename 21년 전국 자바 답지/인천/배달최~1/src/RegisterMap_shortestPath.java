import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class RegisterMap_shortestPath extends JFrame {
	HashMap<String, Point> locMap = new HashMap<String, Point>();
	JRadioButton jrb[] = new JRadioButton[70];
	ArrayList<Integer> al;
	int adjDim[][], pathDim[][];
	int roadCnt;
	int sNode=2, eNode=25;
	int bwSize = 50, bhSize = 50, hOff=-15, vOff=-8;
	boolean hasMinPath = false;
	int INF = 999;
	
	
	BufferedImage img;
	JLabel flag;
	JLabel map;
	JPanel jp;
	boolean move_flag;
	JLabel points;
	ArrayList<Point> pList = new ArrayList<Point>();

	String node1 = null, node2 = null;
	BufferedWriter writer1, writer2;
	BufferedReader reader;
	
	public RegisterMap_shortestPath() {
		//UI �ʱ�ȭ
		super("�����");
		this.setSize(1200, 900);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);
		this.getContentPane().setBackground(Color.YELLOW);

		jp = new JPanel();
		this.add(jp);

		jp.setLayout(null);
		jp.setBackground(Color.WHITE);
		jp.setSize(1200, 900);

		//�ؽ�Ʈ ���� �б�: ���� ��ġ ����
		readLoadLocation(); 

		//�ؽ�Ʈ ���� �б�: ���� ���� ����
		readLoadConnect();
		
		//���� �����ϱ�
		MyLabel ml = new MyLabel();
		
		//UI ������
		ml.setBounds(0, 0, 1000, 700);
		jp.add(ml);
		map = new JLabel(
				new ImageIcon(new ImageIcon("./map.png").getImage().getScaledInstance(1000, 700, Image.SCALE_DEFAULT)));
		map.setBounds(0, 0, 1000, 700);
		jp.add(map);

		this.setVisible(true);
		
		//���ͽ�Ʈ�� �˰��� ����: ����������� ������������ �ִܰ�� ������ 2���� �迭�� �����ϱ�
		hasMinPath = false;
		
//		sNode = Integer.parseInt("005");
//		eNode = Integer.parseInt("025");
		dijkstra(sNode);

		//����������� ������������ �ִܰ�� ������ ����Ѵ�.
		shortestPath();
		
		//�ִ� ��θ� ml�� �׸���.
		hasMinPath = true;
		ml.repaint();
		ml.revalidate();
	}

	//���ͽ�Ʈ�� �˰���
	private void dijkstra(int s) {
		pathDim = new int[4][roadCnt+1];	//��: [1][]�� �Ÿ�,[2][]�� �湮üũ, [3][]�� ���� ������� 
		
		//�ʱ�ȭ: �������(s)���� ��� ���� ����(i)���� �Ÿ�, �湮üũ, ���� ��������� ����Ѵ�.
		for(int i=1; i<=roadCnt; i++) {
			pathDim[1][i] = adjDim[s][i];
			pathDim[2][i] = 0;
			if(adjDim[i][s] < INF) pathDim[3][i] = s; 
		}
		
		//���� ������� ���: �湮üũ�� 1��, ���� ��������� ����(-1)�� ����Ѵ�.
		pathDim[2][s] = 1;
		pathDim[3][s] = -1;
		
		for(int i=1; i<=roadCnt-1; i++) {
			//minSpot����: ��Ȯ��(�湮üũ:false)�� �� �� �Ÿ��� ���� ����� ������ ã��, ã�� ���� Ȯ��(�湮üũ:true)�Ѵ�.
			int minDist = 2147483647, idx = 0;
			for(int j=1; j<=roadCnt; j++) {
				if(pathDim[2][j]==0 && minDist > pathDim[1][j]) {
					minDist = pathDim[1][j];
					idx = j;
				}
			}
			pathDim[2][idx] = 1;
			
			//��Ȯ���� �� �� ã�� ��(idx:from)�� ����� ����(to)���� ��� ã��, pathDim[0][to]���� �Ÿ�, [2][to]���� ��������� ����Ѵ�.
			int from=idx, to;
			for(to=1; to<=roadCnt; to++) {
				//��: (���� to���� �ִ� �Ÿ� > ���� from���� �ִ� �Ÿ� + from���� to���� �Ÿ�)�̸� ���� to���� �ִ� �Ÿ��� �� �F�� �Ÿ��� �����Ѵ�. 
				if(pathDim[2][to]==0 && pathDim[1][to] > pathDim[1][from]+adjDim[from][to]) {
					pathDim[1][to] = pathDim[1][from]+adjDim[from][to];
					pathDim[3][to] = from;
				}
			}
		}
	}

	private void shortestPath() {
//		System.out.println();
//		for(int i=1; i<=3; i++) {
//			for(int j=1; j<=roadCnt; j++) {
//				System.out.print(String.format("%4d", pathDim[i][j]));
//			}
//			System.out.println();
//		}
		
//		System.out.println();
//		for(int i=1; i<=roadCnt; i++) {
//			for(int j=1; j<=roadCnt; j++) {
//				System.out.print(String.format("%4d", adjDim[i][j]));
//			}
//			System.out.println();
//		}
		
		int idx = eNode;
		al = new ArrayList<Integer>();
		al.add(idx);
		while(pathDim[3][idx]!=-1) {
			al.add(pathDim[3][idx]);
			idx = pathDim[3][idx];
		}
		
		//�ùٸ� ������ ����Ѵ�.
		Collections.reverse(al);
		for(int i=0; i<al.size(); i++) {
			System.out.print(al.get(i) + " ");
		}
	}
	
	//���� ���� ���� �б�
	private void readLoadConnect() {
		adjDim = new int[roadCnt+1][roadCnt+1];
		
		//��� ������ �� ���� �Ÿ��� �ǰ� �Ѵ�.
		for(int i=1; i<=roadCnt; i++) {
			for(int j=i+1; j<=roadCnt; j++)
				adjDim[i][j] = adjDim[j][i] = INF;
		}
		
		//�� ������ �Ÿ��� �迭�� ����Ѵ�.
		try {
			for(var line: Files.readAllLines(Paths.get("./LoadConnect.txt"))) {
				var info = line.split("\t");
				int r = Integer.parseInt(info[0]);
				int c = Integer.parseInt(info[1]);
				
				//�� ������ �Ÿ� ���
				int x1 = locMap.get(String.format("%03d", r).intern()).x;
				int y1 = locMap.get(String.format("%03d", r).intern()).y;
				int x2 = locMap.get(String.format("%03d", c).intern()).x;
				int y2 = locMap.get(String.format("%03d", c).intern()).y;
				adjDim[r][c] = adjDim[c][r] = (int) Math.sqrt(0.0+(x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
			}
		} catch (IOException e) {
		}
	}

	//����(�ּ�) ��ġ �б�
	private void readLoadLocation() {
		try {
			for(var line: Files.readAllLines(Paths.get("./LoadLocation.txt"))) {
				var info = line.split("\t");
				jrb[roadCnt] = new JRadioButton(info[0]);
				jp.add(jrb[roadCnt]);

				locMap.put(info[0], new Point(Integer.parseInt(info[1]), Integer.parseInt(info[2])));
				
				jrb[roadCnt].setName(info[0]);
				jrb[roadCnt].setBounds(Integer.parseInt(info[1])+hOff, Integer.parseInt(info[2])+vOff, bwSize, bhSize);
				jrb[roadCnt].setVerticalAlignment(JRadioButton.TOP);
				jrb[roadCnt].setHorizontalTextPosition(JRadioButton.CENTER);
				jrb[roadCnt].setVerticalTextPosition(JRadioButton.BOTTOM);
				jrb[roadCnt].setOpaque(false);		//��
			}
			roadCnt = locMap.size();
		} catch (IOException e) {
		}	
	}

	void clearRadioButton() {
		for(int i=0; i<jrb.length; i++) {
			if(jrb[i] == null) break;
			jrb[i].setSelected(false);
		}
	}

	class MyLabel extends JLabel {
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.BLACK);
			
			//���� ������ �ִ� ��쿡�� ���߱�
			Set set = locMap.keySet();
			for(var pos: set) {
				int r = Integer.parseInt(pos.toString());
				for(int c=1; c<=roadCnt; c++) {
					if(adjDim[r][c]>0 && adjDim[r][c]<INF) {
						int x1 = locMap.get(pos).x;
						int y1 = locMap.get(pos).y;
						int x2 = locMap.get(String.format("%03d", c).intern()).x;
						int y2 = locMap.get(String.format("%03d", c).intern()).y;
						g2.drawLine(x1, y1, x2, y2);
					}
				}
			}
			
			//�ִ� ��θ� �������� ���߱�
			if(hasMinPath) {
				for(int i=0; i<al.size()-1; i++) {
					int x1 = locMap.get(String.format("%03d", al.get(i)).intern()).x;
					int y1 = locMap.get(String.format("%03d", al.get(i)).intern()).y;
					int x2 = locMap.get(String.format("%03d", al.get(i+1)).intern()).x;
					int y2 = locMap.get(String.format("%03d", al.get(i+1)).intern()).y;
					g2.setColor(Color.RED);
					g2.drawLine(x1, y1, x2, y2);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new RegisterMap_shortestPath();
	}
}
