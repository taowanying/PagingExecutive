package pagingExecutive;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class IniFrame extends JFrame {
	JTable table = null; // 随机序列table
	JScrollPane jspSeq;
	public IniFrame() {
		// 实例化一个窗体;
		JFrame frame = new JFrame();
		frame.setTitle("页面置换算法初始化界面");
		frame.setBounds(10, 200, 522, 658);
		frame.setDefaultCloseOperation(3);
		// 实例化初始化数据面板
		JPanel iniPanel = new JPanel();
		iniPanel.setToolTipText("");
		iniPanel.setLayout(null);
		frame.getContentPane().add(iniPanel);

		JTextArea mannual = new JTextArea();
		mannual.setBounds(15, 74, 470, 505);
		mannual.setText("\n\n初始化操作说明：\n\n" + "第一步：输入物理块数（4——10块）\n\n" + "第二步：点击随机生成按钮\n\n产生随机的指令序列和页地址流；\n\n进入算法运行界面");
		iniPanel.add(mannual);

		JLabel LSequence = new JLabel("产生随机序列：");
		LSequence.setBounds(230, 30, 145, 29);
		iniPanel.add(LSequence);

		JButton suijishengcheng = new JButton("随机生成");
		suijishengcheng.setBounds(353, 30, 132, 29);
		iniPanel.add(suijishengcheng);

		JLabel LWLKS = new JLabel("物理块数：");
		LWLKS.setBounds(15, 30, 90, 29);
		iniPanel.add(LWLKS);

		JTextField JWLKS = new JTextField();
		JWLKS.setBounds(106, 30, 83, 29);
		iniPanel.add(JWLKS);

		int[] randomSeq = new int[128]; // 随机序列
		int[] pageSeq = new int[128];// 页地址流
		String[] bcolumnNames = { "编号", "指令序列", "页地址流" }; // 列名
		Object[][] btableVales = new Object[128][3]; // 数据

		// 实例化随机生成按钮响应事件
		suijishengcheng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				int block = 0; // 物理块数
				//iniPanel.remove(jspSeq);
				if (!JWLKS.getText().equals("")) {// 输入物理块数不为空
					block = Integer.parseInt(JWLKS.getText());
					if (block > 3 && block < 11) {
						int count = 0;
						// 随机生成1到128区间中的整数
						Random r = new Random();
						for (count = 0; count < 128; count++) {
							for (int i = 0; i < 128; i++) {// 给tab中的行列赋值
								btableVales[i][0] = i;
								randomSeq[i] = r.nextInt(128) + 1;
								btableVales[i][1] = randomSeq[i];
								btableVales[i][2] = randomSeq[i] / 10;
								pageSeq[i] = randomSeq[i] / 10;// 求余，计算所在页数
							}
						}
						//DefaultTableModel tableModel = new DefaultTableModel(btableVales, bcolumnNames); // 表格模型对象
						table = new JTable(btableVales, bcolumnNames);
						jspSeq = new JScrollPane(table);
						jspSeq.setBounds(35, 84, 450, 505);
						jspSeq.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
						jspSeq.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
						iniPanel.remove(mannual);
						
						iniPanel.add(jspSeq);
						
						
						Paging PagingFrame = new Paging(pageSeq, block); // 将所在页数和物理块数传值

					} else { 
						JTextField text1 = new JTextField("        物理块数只能为4——10之间的整数！");
						JFrame ini_fail = new JFrame();
						ini_fail.setTitle("提示");
						ini_fail.setLocation(750, 500);
						ini_fail.setSize(420, 80);
						ini_fail.setDefaultCloseOperation(2);
						ini_fail.getContentPane().add(text1);
						ini_fail.setVisible(true);
					}
				} else {
					JTextField text1 = new JTextField("          请输入物理块数！");
					JFrame ini_fail = new JFrame();
					ini_fail.setTitle("提示");
					ini_fail.setLocation(750, 500);
					ini_fail.setSize(300, 80);
					ini_fail.setDefaultCloseOperation(2);
					ini_fail.getContentPane().add(text1);
					ini_fail.setVisible(true);
				}
				
			}
		});
		frame.setVisible(true);

	}
}
