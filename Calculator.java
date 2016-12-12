/*
*Name: Peter Valdez
*Date: 11-20-16
*Purpose: This is a calculator program, that allows the user to input numbers 
and operators such as add, subtract, multiply, and divide.  
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

public class Calculator extends Frame implements ActionListener
{
	private Button keys[];
	private Panel calcKeyPad;
	private TextField display;
	private double op1;
	private boolean first;
	private boolean foundKey;
	private boolean clearField;
	private int op2;
	private DecimalFormat patternCalculator;

	public Calculator()
	{
		//creates an instance of the menu.
		MenuBar mnuBar = new MenuBar();
		setMenuBar(mnuBar);

		//constructs and populates File menu.
		Menu mnuFile = new Menu("File", true);
		mnuBar.add(mnuFile);
			MenuItem mnuFileExit = new MenuItem("Exit");
			mnuFile.add(mnuFileExit);

		//constructs and populates the edit menu
		Menu mnuEdit = new Menu("Edit", true);
		mnuBar.add(mnuEdit);
			MenuItem mnuEditClear = new MenuItem("Clear");
			mnuEdit.add(mnuEditClear);
			mnuEdit.insertSeparator(1);
			MenuItem mnuEditCopy = new MenuItem("Copy");
			mnuEdit.add(mnuEditCopy);
			MenuItem mnuEditPaste = new MenuItem("Paste");
			mnuEdit.add(mnuEditPaste);

		//constructs and populates the About menu
		Menu mnuAbout = new Menu("About", true);
			mnuBar.add(mnuAbout);
			MenuItem mnuAboutCalculator = new MenuItem("About Calculator");
			mnuAbout.add(mnuAboutCalculator);

			//Add the Action Listener to each menu item.
			mnuFileExit.addActionListener(this);
			mnuEditClear.addActionListener(this);
			mnuEditCopy.addActionListener(this);
			mnuEditPaste.addActionListener(this);
			mnuAboutCalculator.addActionListener(this);

			//Assigns an ActionCommand to each menu item.
			mnuFileExit.setActionCommand("Exit");
			mnuEditClear.setActionCommand("Clear");
			mnuEditCopy.setActionCommand("Copy");
			mnuEditPaste.setActionCommand("Paste");
			mnuAboutCalculator.setActionCommand("About");

			//comstructs componetns and initialize beginning values
			display = new TextField(20);
				display.setEditable(false);
			calcKeyPad = new Panel();
			keys = new Button[16];
			first = true;
			op1 = 0.0;
			clearField = true;
			op2 = 0;
			patternCalculator = new DecimalFormat("########.########");

			//constructs and assigns captions to the Buttons.
			for (int i=0; i<=9; i++)
				keys[i] = new Button(String.valueOf(i));

			keys[10] = new Button("/");
			keys[11] = new Button("*");
			keys[12] = new Button("-");
			keys[13] = new Button("+");
			keys[14] = new Button("=");
			keys[15] = new Button(".");

			//set Frame and calcKeyPad layout to grid layout.
			setLayout(new BorderLayout());
			calcKeyPad.setLayout(new GridLayout(4,4,10,10));

			for (int i=7; i<=10; i++) //7, 8, 9, divide.
				calcKeyPad.add(keys[i]);

			for (int i=4; i<=6; i++) //4, 5, 6
				calcKeyPad.add(keys[i]);

			calcKeyPad.add(keys[11]); //multiply

			for (int i=1; i<=3; i++) // 1, 2, 3
				calcKeyPad.add(keys[i]);

			calcKeyPad.add(keys[12]); //subtract

			calcKeyPad.add(keys[0]); //0 key

			for (int i=15; i>=13; i--)
				calcKeyPad.add(keys[i]); //decimal points, =, add (+) keys

			for (int i = 0; i < keys.length; i++)
				keys[i].addActionListener(this);

			add(display, BorderLayout.NORTH);
			add(calcKeyPad, BorderLayout.CENTER);

			addWindowListener(
				new WindowAdapter()
					{
					public void windowClosing(WindowEvent e)
						{
						System.exit(0);
						}
					}
			);

	} //end of constructor method

	public void actionPerformed(ActionEvent e)
	{
		//test for menu item clucks.
		String arg = e.getActionCommand();
		if (arg == "Exit")
			System.exit(0);

		if (arg == "Clear")
		{
			clearField = true;
			first = true;
			op1 = 0.0;
			display.setText("");
			display.requestFocus();
		}

		if (arg == "Copy")
		{
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection contents = new StringSelection(display.getText());
			cb.setContents(contents, null);
		}

		if (arg == "Paste")
		{
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable content = cb.getContents(this);
			try
			{
				String s = (String)content.getTransferData(DataFlavor.stringFlavor);
				display.setText(patternCalculator.format(Double.parseDouble(s)));
			}
			catch (Throwable exc)
			{
				display.setText("");
			}
		}

		if (arg == "About")
		{
			String message = "Calculator ver. 1.0\nInGeeksWeTrust Software\nCopyright 2016\nAll rights reserved";
			JOptionPane.showMessageDialog(null, message, "About Calculator", JOptionPane.INFORMATION_MESSAGE);
		}

		//test for button clicks.
		foundKey = false;

		//Search for the clicked key.
		for (int i=0; i < keys.length && !foundKey; i++)
		{
			if(e.getSource() == keys[i])
			{
				foundKey = true;
				switch(i)
				{
					//number and decimal point buttons
					case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 15:
						if(clearField)
						{
							display.setText("");
							clearField = false;
						}
						display.setText(display.getText() + keys[i].getLabel());
						break;

					//operator buttons
					case 10: case 11: case 12: case 13: case 14:
						clearField = true;

						if (first) //first operand
						{
							if(display.getText().length()==0) op1 = 0.0;
							else op1 = Double.parseDouble(display.getText());

							first = false;
							clearField = true;
							op2 = i; //save the last operator.
						}
						else //second operand
						{
							switch(op2)
							{
								case 10: //divide button
									op1 /= Double.parseDouble(display.getText());
									break;
								case 11: //multiply button
									op1 *= Double.parseDouble(display.getText());
									break;
								case 12: //minus button
									op1 -= Double.parseDouble(display.getText());
									break;
								case 13: //plus button
									op1 += Double.parseDouble(display.getText());
									break;
							}	//end of swith(op2)
							display.setText(patternCalculator.format(op1));
							clearField = true;

							if(i==14) first = true; //equal button
							else op2 = i; //save the last operator
						} //end else
						break;
				} //end of switch(i)
			} //end of if
		} //end of for
	} // end of actionPerformed

	public static void main(String args[])
	{
		//set frame properties
		Calculator f = new Calculator();
		f.setTitle("Calculator Application");
		f.setBounds(200,200,300,300);
		f.setVisible(true);

		//set image properties and add to frame
		Image icon = Toolkit.getDefaultToolkit().getImage("calcImage.gif");
		f.setIconImage(icon);

	} //end of main
} // end of class