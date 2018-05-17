package pagingExecutive;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;

public class Paging extends JFrame {
	int FIFOhit = 0, LRUhit = 0, OPThit = 0;

	public Paging(final int[] seq, final int block) {
		// 实例化一个窗体;
		JFrame pageframe = new JFrame();
		pageframe.setTitle("页面置换功能界面");
		pageframe.setBounds(550, 260, 436, 589);
		pageframe.setDefaultCloseOperation(2);
		pageframe.getContentPane().setLayout(null);
		// 实例化运行结果面板
		JPanel resultPanel = new JPanel();
		resultPanel.setBackground(Color.WHITE);
		resultPanel.setToolTipText("");
		resultPanel.setLayout(null);
		resultPanel.setBounds(15, 0, 380, 533);
		pageframe.getContentPane().add(resultPanel);
		String labels[] = { "FIFO", "LRU", "OPT" };
		JComboBox comboBox = new JComboBox(labels);
		comboBox.setBounds(42, 82, 107, 37);
		resultPanel.add(comboBox);

		JLabel lblFifo = new JLabel();
		lblFifo.setFont(UIManager.getFont("Label.font"));
		lblFifo.setText("FIFO命中率：");
		lblFifo.setBounds(42, 199, 121, 37);
		resultPanel.add(lblFifo);

		JTextArea FIFOrate = new JTextArea();
		FIFOrate.setText("");
		FIFOrate.setBounds(157, 199, 175, 37);
		resultPanel.add(FIFOrate);

		JButton yunxing = new JButton("运行");
		yunxing.setBounds(198, 82, 134, 37);
		resultPanel.add(yunxing);

		JLabel lblLru = new JLabel();
		lblLru.setText("LRU命中率：");
		lblLru.setFont(UIManager.getFont("Label.font"));
		lblLru.setBounds(42, 251, 121, 37);
		resultPanel.add(lblLru);

		JLabel lblOpt = new JLabel();
		lblOpt.setText("OPT命中率：");
		lblOpt.setFont(UIManager.getFont("Label.font"));
		lblOpt.setBounds(42, 303, 121, 37);
		resultPanel.add(lblOpt);

		JTextArea LRUrate = new JTextArea();
		LRUrate.setText("");
		LRUrate.setBounds(157, 251, 175, 37);
		resultPanel.add(LRUrate);

		JTextArea OPTrate = new JTextArea();
		OPTrate.setText("");
		OPTrate.setBounds(157, 303, 175, 37);
		resultPanel.add(OPTrate);

		JLabel label = new JLabel();
		label.setText("——————结果分析——————");
		label.setBounds(42, 134, 312, 44);
		resultPanel.add(label);

		JLabel label_1 = new JLabel();
		label_1.setText("——————算法运行——————");
		label_1.setBounds(42, 23, 323, 44);
		resultPanel.add(label_1);

		JLabel label_2 = new JLabel();
		label_2.setText("——————演示效果——————");
		label_2.setBounds(42, 418, 312, 44);
		resultPanel.add(label_2);

		JButton dtys = new JButton("动态演示");
		dtys.setBounds(198, 477, 134, 37);
		resultPanel.add(dtys);

		JButton best = new JButton("最优算法");
		best.setBounds(42, 366, 134, 37);
		resultPanel.add(best);

		JTextArea textbest = new JTextArea();
		textbest.setText("");
		textbest.setBounds(191, 366, 141, 37);
		resultPanel.add(textbest);

		JComboBox comboBox_1 = new JComboBox(labels);
		comboBox_1.setBounds(52, 477, 107, 37);
		resultPanel.add(comboBox_1);

		pageframe.setVisible(true);
		Object[] tableTitle = new Object[129];// 列名
		tableTitle[0] = "物理块数";
		for (int i = 1; i < 129; i++)
			tableTitle[i] = "第" + i + "步";

		// 自定义表格绘制器
		class ColorTableCellRenderer extends DefaultTableCellRenderer {
			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			int[][] flag;
			int block;

			ColorTableCellRenderer(int b, int[][] f) {
				block = b;
				flag = new int[1000][1000];
				for (int i = 0; i < block; i++)
					for (int j = 0; j < 129; j++)
						flag[i][j] = f[i][j];
			}

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (flag[row][column] == 1) {
					// 调用基类方法
					setBackground(Color.cyan); // 设置颜色
					return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				} else {
					return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				}
			}
		}
		Object[][] data1 = new Object[block][129];// 存放FIFO页面置换结果
		Object[][] data2 = new Object[block][129];// 存放LRU页面置换结果
		Object[][] data3 = new Object[block][129];// 存放OPT页面置换结果
		yunxing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean flag;
				int pos = -1;// 标记指令序列数组
				int memorynum = 0;
				int[] Memory = new int[block];// 内存物理块所含页面数组
				int[][] changecolor = new int[block][129];
				int replacepos = 0;// 被替换页面所在的物理块

				// FIFO算法
				if (comboBox.getSelectedItem().equals("FIFO")) {
					for (int a = 0; a < block; a++)
						Memory[a] = -1; // 初始化为-1
					for (int q = 0; q < 128; q++) {
						flag = false;
						pos++;
						int temp = seq[pos];// 中间变量暂时存放当前指令所在页地址
						// 查看是否已经存在内存中
						for (int i = 0; i < block; i++) {
							if (Memory[i] == temp) {// 如果命中，命中数+1，跳出循环
								FIFOhit++;
								flag = true;
								break;
							}
						}
						// 如果未命中，考虑是否要替换
						if (!flag) {
							if (memorynum < block) {// 如果内存没满，则不用替换
								Memory[memorynum] = temp;
								memorynum++;
							} else {// 如果内存已满，选择最早调入的页面进行替换
								Memory[replacepos] = temp;// Memory[replacepos]为被替换的元素，第一次满即替换memory[0]
								changecolor[replacepos][pos + 1] = 1;// 标记待变颜色的表格框
								replacepos = (replacepos + 1) % block;// 按照block大小来循环替换
							}
						}
						for (int i = 0; i < block; i++)
							data1[i][pos + 1] = Memory[i];
					}
					// 显示命中率
					FIFOrate.setText(String.valueOf((double) FIFOhit / 128));

					int a = 0;
					for (int i = 0; i < block; i++) {
						a = i + 1;
						data1[i][0] = "第" + a + "块";
					}
					DefaultTableModel tableModel1 = new DefaultTableModel(data1, tableTitle); // 表格模型对象
					JTable Result1 = new JTable(tableModel1);
					int columnCount1 = Result1.getColumnCount();
					Result1.getColumnModel().getColumn(0).setPreferredWidth(80);
					for (int i = 1; i < columnCount1; i++) {
						TableColumn tableColumn1 = Result1.getColumnModel().getColumn(i);
						tableColumn1.setPreferredWidth(80);
					}
					Result1.setRowHeight(40);// 指定每一行的行高40
					Result1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					JScrollPane jspResult1 = new JScrollPane(Result1);
					jspResult1.setBounds(10, 10, 1018, 320);
					jspResult1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					jspResult1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					TableCellRenderer tcr1 = new ColorTableCellRenderer(block, changecolor);
					Result1.setDefaultRenderer(Object.class, tcr1);

					JFrame FIFO = new JFrame("FIFO算法结果        命中率：" + (double) FIFOhit / 128);
					FIFO.getContentPane().setLayout(null);
					FIFO.setBounds(920, 10, 1048, 380);
					FIFO.setDefaultCloseOperation(2);
					FIFO.getContentPane().add(jspResult1);
					FIFO.setVisible(true);
				}
				// LRU算法
				else if (comboBox.getSelectedItem().equals("LRU")) {
					int[] use = new int[block];
					int count = 0;
					for (int a = 0; a < block; a++)
						Memory[a] = -1; // 初始化为-1
					for (int q = 0; q < 128; q++) {
						count = 0;
						flag = false;
						pos++;
						int temp = seq[pos];// 中间变量暂时存放当前指令所在页地址
						// 查看是否已经存在内存中
						for (int i = 0; i < block; i++) {
							if (Memory[i] == temp) {// 如果命中，命中数+1，跳出循环
								LRUhit++;
								flag = true;
								break;
							}
						}
						// 如果未命中，考虑是否要替换
						if (!flag) {
							if (memorynum < block) {// 如果内存没满，则不用替换
								Memory[memorynum] = temp;
								memorynum++;
							} else {// 如果内存已满，选择最近最久未使用的页面进行替换
								for (int i = 1; count < block; i++) {
									for (int j = 0; j < block; j++) {
										if (Memory[j] == seq[pos - i]) {// 在块中找到了指令序列所在页面
											if (use[j] == 0) {
												// System.out.println("第"+q+"次use["+j+"]为0，将变为"+i);
												count++;
												use[j] = i;
											}
											break;
										}
									}
								}

								int max = 0;
								// use最大的即为待替换的页面
								for (int i = 0; i < block; i++) {
									if (max < use[i]) {
										max = use[i];
										replacepos = i;
									}
								}
								Memory[replacepos] = temp;// Memory[replacepos]为被替换的元素
								changecolor[replacepos][pos + 1] = 1;// 标记待变颜色的表格框
							}
						}
						for (int i = 0; i < block; i++) {
							data2[i][pos + 1] = Memory[i];
							use[i] = 0;
						}
					}
					// 显示命中率
					LRUrate.setText(String.valueOf((double) LRUhit / 128));

					int a = 0;
					for (int i = 0; i < block; i++) {
						a = i + 1;
						data2[i][0] = "第" + a + "块";
					}
					DefaultTableModel tableModel2 = new DefaultTableModel(data2, tableTitle); // 表格模型对象
					JTable Result2 = new JTable(tableModel2);
					int columnCount2 = Result2.getColumnCount();
					Result2.getColumnModel().getColumn(0).setPreferredWidth(80);
					for (int i = 1; i < columnCount2; i++) {
						TableColumn tableColumn2 = Result2.getColumnModel().getColumn(i);
						tableColumn2.setPreferredWidth(80);
					}
					Result2.setRowHeight(40);// 指定每一行的行高40
					Result2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					JScrollPane jspResult2 = new JScrollPane(Result2);
					jspResult2.setBounds(10, 10, 1018, 320);
					jspResult2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					jspResult2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					TableCellRenderer tcr2 = new ColorTableCellRenderer(block, changecolor);
					Result2.setDefaultRenderer(Object.class, tcr2);

					JFrame LRU = new JFrame("LRU算法结果        命中率：" + (double) LRUhit / 128);
					LRU.getContentPane().setLayout(null);
					LRU.setBounds(920, 390, 1048, 380);
					LRU.setDefaultCloseOperation(2);
					LRU.getContentPane().add(jspResult2);
					LRU.setVisible(true);
				}

				// OPT算法
				else if (comboBox.getSelectedItem().equals("OPT")) {
					int[] use = new int[block];
					int count = 0;
					for (int a = 0; a < block; a++)
						Memory[a] = -1; // 初始化为-1
					for (int q = 0; q < 128; q++) {
						count = 0;
						flag = false;
						pos++;
						int temp = seq[pos];// 中间变量暂时存放当前指令所在页地址
						// 查看是否已经存在内存中
						for (int i = 0; i < block; i++) {
							if (Memory[i] == temp) {// 如果命中，命中数+1，跳出循环
								OPThit++;
								flag = true;
								break;
							}
						}
						// 如果未命中，考虑是否要替换
						if (!flag) {
							if (memorynum < block) {// 如果内存没满，则不用替换
								Memory[memorynum] = temp;
								memorynum++;
							} else {// 如果内存已满，选择最近最久未使用的页面进行替换
								for (int x = pos; x < 128; x++) {
									for (int j = 0; j < block; j++) {
										if (Memory[j] == seq[x]) {// 在块中找到了指令序列所在页面
											if (use[j] == 0) {
												// System.out.println("第"+q+"次use["+j+"]为0，将变为"+i);
												count++;
												use[j] = x;
											}
											break;
										}
									}
									if (count == block)// 若块中页面在后续执行指令都调用了
										break;
								}
								boolean flag1 = false;

								for (int i = 0; i < block; i++) {
									if (use[i] == 0) {
										replacepos = i;
										flag1 = true;
										break;
									}
								}
								if (!flag1) {// 如果每个块中页面在后续指令中都调用了
									int max = 0;
									// use最大的即为待替换的页面
									for (int i = 0; i < block; i++) {
										if (max < use[i]) {
											max = use[i];
											replacepos = i;
										}
									}
								}
								Memory[replacepos] = temp;// Memory[replacepos]为被替换的元素
								changecolor[replacepos][pos + 1] = 1;// 标记待变颜色的表格框
							}
						}
						for (int i = 0; i < block; i++) {
							data3[i][pos + 1] = Memory[i];
							use[i] = 0;
						}
					}
					// 显示命中率
					OPTrate.setText(String.valueOf((double) OPThit / 128));

					int a = 0;
					for (int i = 0; i < block; i++) {
						a = i + 1;
						data3[i][0] = "第" + a + "块";
					}
					DefaultTableModel tableModel3 = new DefaultTableModel(data3, tableTitle); // 表格模型对象
					JTable Result3 = new JTable(tableModel3);
					int columnCount3 = Result3.getColumnCount();
					Result3.getColumnModel().getColumn(0).setPreferredWidth(80);
					for (int i = 1; i < columnCount3; i++) {
						TableColumn tableColumn3 = Result3.getColumnModel().getColumn(i);
						tableColumn3.setPreferredWidth(80);
					}
					Result3.setRowHeight(40);// 指定每一行的行高40
					Result3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					JScrollPane jspResult3 = new JScrollPane(Result3);
					jspResult3.setBounds(10, 10, 1018, 320);
					jspResult3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					jspResult3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					TableCellRenderer tcr3 = new ColorTableCellRenderer(block, changecolor);
					Result3.setDefaultRenderer(Object.class, tcr3);

					JFrame OPT = new JFrame("OPT算法结果        命中率：" + (double) OPThit / 128);
					OPT.getContentPane().setLayout(null);
					OPT.setBounds(920, 760, 1048, 380);
					OPT.setDefaultCloseOperation(2);
					OPT.getContentPane().add(jspResult3);
					OPT.setVisible(true);
				}
			}
		});

		best.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int max = 0;
				String str = null;
				if (FIFOhit > max) {
					max = FIFOhit;
					str = "FIFO";
				}
				if (LRUhit > max) {
					max = LRUhit;
					str = "LRU";
				}
				if (OPThit > max) {
					max = OPThit;
					str = "OPT";
				}
				textbest.setText(str);
			}
		});

		dtys.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox_1.getSelectedItem().equals("FIFO")) {
					Dynamic dtys_frame = new Dynamic(seq, block, data1);
				} else if (comboBox_1.getSelectedItem().equals("LRU")) {
					Dynamic dtys_frame = new Dynamic(seq, block, data2);
				} else if (comboBox_1.getSelectedItem().equals("OPT")) {
					Dynamic dtys_frame = new Dynamic(seq, block, data3);
				}
			}
		});
	}
}
