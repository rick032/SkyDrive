package skydrive;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
<<<<<<< HEAD
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
=======
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
>>>>>>> e1638db39fed3a7d1bc647971d443ef98c9f5d9f
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.wb.swt.layout.grouplayout.LayoutStyle;

/**
 * 
 * @author rick032
 * 
 */
public class Window {

	protected Shell shell;
	protected SkyDrive skyDrive;
	private Text URLText;
	private Table table;
	private Text text;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Window window = new Window();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(637, 511);
<<<<<<< HEAD
		shell.setText("Get Sky Drive URL By rick032 v1.31 x86");
		final MessageBox messageBox = new MessageBox(shell,
				SWT.ICON_INFORMATION);
=======
		shell.setText("Get Sky Drive URL By rick032 v1.3");

>>>>>>> e1638db39fed3a7d1bc647971d443ef98c9f5d9f
		URLText = new Text(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL);

		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setResizable(false);
		column1.setWidth(40);
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setWidth(566);
		text = new Text(shell, SWT.BORDER);
		Button btnAddUrl = new Button(shell, SWT.NONE);
		btnAddUrl.setText("Add URL");
		btnAddUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String url = text.getText();
<<<<<<< HEAD
				if (!"".equals(url.trim())) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] {
							String.valueOf(table.getItemCount()), url });
				} else {
					// MessageBox
					messageBox.setMessage("Please input URL first!!");
					messageBox.open();
=======
				if (url != null) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] {
							String.valueOf(table.getItemCount()), url });

>>>>>>> e1638db39fed3a7d1bc647971d443ef98c9f5d9f
				}
			}
		});
		Button btnGenrate = new Button(shell, SWT.CENTER);
		btnGenrate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				StringBuffer sb = new StringBuffer();
				skyDrive = new SkyDrive();
<<<<<<< HEAD
				if (table.getItemCount() > 0) {
					URLText.setText("");
					String url = null;
					int i = 1;
					for (TableItem tableItem : table.getItems()) {
						sb.append(i++)
								.append("  -------------------------------")
								.append("\r\n");
						url = tableItem.getText(1);
						String list = null;
						list = skyDrive.getURLList(url);
=======
				if (table.getItemCount() > 1) {
					URLText.setText("");
					String url = null;
					int i=1;
					for (TableItem tableItem : table.getItems()) {
						sb.append(i++).append("  -------------------------------").append("\r\n");
						url = tableItem.getText(1);
						String list = skyDrive.getURLList(url);
>>>>>>> e1638db39fed3a7d1bc647971d443ef98c9f5d9f
						if (list == null) {
							list = "Please Check Your URL!! :" + url;
						}
						sb.append(list).append("\r\n");
					}

					URLText.setText(sb.toString());
					// URLText.setSelection(0, list.length());
<<<<<<< HEAD
				} else {
					// MessageBox
					messageBox.setMessage("Please add URL first!!");
					messageBox.open();
				}
			}
		});

		btnGenrate.setText("Generate");

		Button delbtn = new Button(shell, SWT.NONE);
		delbtn.setText("Del URL");
		delbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (table.getSelectionCount() > 0) {
					table.remove(table.getSelectionIndices());
					// reset 序號
					int i = 1;
					for (TableItem tableItem : table.getItems()) {
						tableItem.setText(0, String.valueOf(i++));
					}
				} else {
					// MessageBox
					messageBox.setMessage("Please selected URL first!!");
					messageBox.open();

				}
			}
		});
		GroupLayout gl_shell = new GroupLayout(shell);
		gl_shell.setHorizontalGroup(gl_shell
				.createParallelGroup(GroupLayout.LEADING)
				.add(gl_shell
						.createSequentialGroup()
						.add(5)
						.add(gl_shell
								.createParallelGroup(GroupLayout.TRAILING)
								.add(GroupLayout.LEADING, URLText,
										GroupLayout.DEFAULT_SIZE, 611,
										Short.MAX_VALUE)
								.add(GroupLayout.LEADING, table,
										GroupLayout.DEFAULT_SIZE, 611,
										Short.MAX_VALUE)
								.add(gl_shell
										.createSequentialGroup()
										.add(btnAddUrl)
										.addPreferredGap(LayoutStyle.RELATED)
										.add(text, GroupLayout.DEFAULT_SIZE,
												525, Short.MAX_VALUE))).add(5))
				.add(gl_shell.createSequentialGroup().add(6).add(delbtn)
						.addPreferredGap(LayoutStyle.RELATED).add(btnGenrate)
						.addContainerGap(460, Short.MAX_VALUE)));
		gl_shell.setVerticalGroup(gl_shell.createParallelGroup(
				GroupLayout.LEADING).add(
				gl_shell.createSequentialGroup()
						.add(5)
						.add(gl_shell
								.createParallelGroup(GroupLayout.BASELINE)
								.add(btnAddUrl)
								.add(text, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.RELATED)
						.add(table, GroupLayout.PREFERRED_SIZE, 116,
								GroupLayout.PREFERRED_SIZE)
						.add(18)
						.add(gl_shell.createParallelGroup(GroupLayout.BASELINE)
								.add(btnGenrate).add(delbtn))
						.add(15)
						.add(URLText, GroupLayout.DEFAULT_SIZE, 256,
								Short.MAX_VALUE)));
=======
				}
			}
		});
		btnGenrate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnGenrate.setText("Generate");
		GroupLayout gl_shell = new GroupLayout(shell);
		gl_shell.setHorizontalGroup(
			gl_shell.createParallelGroup(GroupLayout.LEADING)
				.add(gl_shell.createSequentialGroup()
					.add(5)
					.add(gl_shell.createParallelGroup(GroupLayout.LEADING)
						.add(gl_shell.createSequentialGroup()
							.add(btnGenrate)
							.addContainerGap())
						.add(gl_shell.createSequentialGroup()
							.add(gl_shell.createParallelGroup(GroupLayout.TRAILING)
								.add(GroupLayout.LEADING, URLText, GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
								.add(GroupLayout.LEADING, table, GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
								.add(gl_shell.createSequentialGroup()
									.add(btnAddUrl)
									.addPreferredGap(LayoutStyle.RELATED)
									.add(text, GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)))
							.add(5))))
		);
		gl_shell.setVerticalGroup(
			gl_shell.createParallelGroup(GroupLayout.LEADING)
				.add(gl_shell.createSequentialGroup()
					.add(5)
					.add(gl_shell.createParallelGroup(GroupLayout.BASELINE)
						.add(btnAddUrl)
						.add(text, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(LayoutStyle.RELATED)
					.add(table, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
					.add(18)
					.add(btnGenrate)
					.addPreferredGap(LayoutStyle.UNRELATED)
					.add(URLText, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
		);
>>>>>>> e1638db39fed3a7d1bc647971d443ef98c9f5d9f
		shell.setLayout(gl_shell);

	}
}
