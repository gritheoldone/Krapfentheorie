package model;
import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Grid extends JFrame
{

	public Grid(Menu menu)
	{

		setTitle("Adjazenzmatrix");
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();

		int groesse = menu.getAdjMatrix().getKnotenanzahl();
		
		panel.setLayout(new GridLayout(groesse+1, groesse+1));
		JButton[][] buttonArray = new JButton[groesse][groesse];
		for (int i = -1; i < groesse; i++)
			{
				for (int j = -1; j < groesse; j++)
					{
						if (i == -1 && j == -1)
							panel.add(new JLabel(""));
						else if (i == -1)
							panel.add(new JLabel((j + 1) + "", JLabel.CENTER));
						else if (j == -1)
							panel.add(new JLabel((i + 1) + "", JLabel.CENTER));
						else
							{
								ChangingButton button = new ChangingButton(i, j, menu, buttonArray);
								button.setMargin(new Insets(4, 9, 4, 9));
								button.setBackground(Color.WHITE);
								if (i == j)
									{
										button.setEnabled(false);
										button.setBackground(Color.ORANGE);
									}
								panel.add(button);
								buttonArray[i][j] = button;
							}
					}
			}
		add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setVisible(true);
	}

}
