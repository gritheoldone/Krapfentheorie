/**
 * 
 */
/**
 * @author Roman
 *
 */
package model;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class KnotenEingabe extends JDialog
{
	private JPanel			panel;
	private JButton			ok;
	private SpinnerModel	smodel;
	private JSpinner		spinner;
	private Menu			menu;
	private int				value	= AdjazenzMatrix.start;

	public KnotenEingabe(Menu menu)
	{
		this.menu = menu;
		init();
		initPanels();
		initElements(menu);
		addElements();
		addPanels();
		setModal(true);
		setVisible(true);
	}

	private void initElements(Menu menu)
	{
		smodel = new SpinnerNumberModel(AdjazenzMatrix.start, AdjazenzMatrix.min, AdjazenzMatrix.max, 1);
		spinner = new JSpinner(smodel);
		JFormattedTextField tf = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
		tf.setEditable(false);
		tf.setBackground(Color.white);
		tf.setHorizontalAlignment(JTextField.CENTER);
		tf.setFont(new Font(null, Font.BOLD, 20));

		Dimension d = spinner.getPreferredSize();
		d.height = 30;
		spinner.setPreferredSize(d);

		smodel.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				value = ((Number) spinner.getValue()).intValue();
			}
		});

		ok = new JButton("OK");
		ok.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				uebernehmen();
			}
		});
	}

	private void addPanels()
	{
		add(panel);
	}

	private void addElements()
	{
		panel.add(spinner);
		panel.add(ok);
	}

	private void initPanels()
	{
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
	}

	private void init()
	{
		setSize(250, 70);
		setTitle("Knoteneingabe");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
	}

	private void uebernehmen()
	{
		menu.MatrixErzeugen(value);
		if (menu.getGrid() != null)
			menu.getGrid().dispose();
		menu.setGrid(new Grid(menu));
		menu.getOutput().setText(menu.getAdjMatrix().aktualisiereBerechnungen());
		setVisible(false);
	}

}
