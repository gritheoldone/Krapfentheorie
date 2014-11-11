package model;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Menu extends JFrame
{
	private AdjazenzMatrix	matrix;
	private JTextArea		output;
	private JScrollPane		scrollPane;
	private Grid			grid;
	private JPanel			panel;
	private JMenuBar		menuBar;
	private JMenuItem		menuItem;
	private KnotenEingabe	knotenEingabe;

	public Menu()
	{
		init();
	}

	public JTextArea getOutput()
	{
		return output;
	}

	public void setOutput(JTextArea output)
	{
		this.output = output;
	}

	public void initMenuBar()
	{
		menuBar = new JMenuBar();
		menuItem = new JMenuItem("Neu");
		menuBar.add(menuItem);
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				knotenEingabe.setVisible(true);
			}
		});

		setJMenuBar(menuBar);
	}

	
	public void initPanel()
	{
		panel = new JPanel(new BorderLayout());
		output = new JTextArea();
		output.setEditable(false);
		scrollPane = new JScrollPane(output);
		panel.add(scrollPane, BorderLayout.CENTER);
		add(panel);
	}

	public void init()
	{
		setTitle("Graphentheorie");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initMenuBar();
		initPanel();
		setSize(700, 750);
		setLocationRelativeTo(null);
		setVisible(true);
		knotenEingabe = new KnotenEingabe(this);
	}


	public static void main(String[] args)
	{
		new Menu();
	}

	public void MatrixErzeugen(int knoten)
	{
		matrix = new AdjazenzMatrix(knoten);
	}

	public AdjazenzMatrix getAdjMatrix()
	{
		return matrix;
	}

	public Grid getGrid()
	{
		return grid;
	}

	public void setGrid(Grid grid)
	{
		this.grid = grid;
	}

}
