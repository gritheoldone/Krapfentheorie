package model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ChangingButton extends JButton
{
	private Menu	menu;
	private int		x;
	private int		y;
	private JButton[][] butt;

	public ChangingButton(int x, int y, Menu menu, JButton[][] butt)
	{
		this.x = x;
		this.y = y;
		this.menu = menu;
		this.butt = butt;
		addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				buttonPressed();
			}
		});
		updateNameFromModel();
	}

	private void updateNameFromModel()
	{
		boolean wert = menu.getAdjMatrix().getWert(x+1, y+1);
		if (wert)
			{
				setText("1");
				setBackground(Color.GRAY);
			}
		else
			{
				setText("0");
				setBackground(Color.WHITE);
			}
	}
	
	private void buttonPressed()
	{
		menu.getAdjMatrix().changeValue(x+1, y+1);
		updateNameFromModel();
		JButton button = butt[y][x];
		boolean wert = menu.getAdjMatrix().getWert(x+1, y+1);
		if (wert)
			{
				button.setText("1");
				button.setBackground(Color.GRAY);
			}
		else
			{
				button.setText("0");
				button.setBackground(Color.WHITE);
			}
		menu.getOutput().setText(menu.getAdjMatrix().aktualisiereBerechnungen());
	}
	
	
}
