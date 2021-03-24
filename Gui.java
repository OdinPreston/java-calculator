import javax.swing.*;
import java.awt.*;

class Gui {
    final static int MAX_COLUMNS = 5;

    String operandButtonLabels[] = { "*", "/", "1", "2", "3", "+", "-", "4", "5", "6", "(", ")", "7", "8", "9", "^", "_", "C", "0", "=" };
    String operatorButtonLabels[] = { "^", "*", "/", "+", "-", "_", "(", ")", };
    private JButton operandButtons[] = new JButton[operandButtonLabels.length];
    private JButton operatorButtons[] = new JButton[operatorButtonLabels.length];
    private JPanel displayPanel;
    private JPanel buttonPanel;

    private JDialog mainFrame;
    private JTextField textfield;
    private JScrollBar scrollBar;
    private JButton add;
    private JButton subtract;
    private JButton multiply;
    private JButton divide;
    private JButton power;
    private JLabel label;

    Gui() {
	//	buttonPanel = new JPanel(new GridLayout(0,3));
	buttonPanel = new JPanel(new GridBagLayout());
	for(int i = 0; i < operandButtonLabels.length; ++i) {
	    operandButtons[i] = new JButton(operandButtonLabels[i]);
	    operandButtons[i].setFont(new Font("Default", Font.PLAIN, 40));
	    operandButtons[i].setBackground(Color.decode("#fdf6e3"));
	    operandButtons[i].setForeground(Color.decode("#657b83"));
	}

	GridBagConstraints c = new GridBagConstraints();
	displayPanel = new JPanel();
	displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));

	textfield = new JTextField("0.00000000000000000000000000000000000000");
	scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
	textfield.setFont(new Font("Default", Font.PLAIN, 40));
	textfield.setEditable(false);
	mainFrame = new JDialog();
	mainFrame.getContentPane().setBackground(Color.decode("#fdf6e3"));
	mainFrame.setTitle("Java calculator");
	mainFrame.setSize(350, 350);
	mainFrame.setLayout(new GridBagLayout());

	BoundedRangeModel brm = textfield.getHorizontalVisibility();

	scrollBar.setModel(brm);


	displayPanel.add(textfield);
	displayPanel.add(scrollBar);
	//	mainFrame.add(displayPanel);
	int i, j, k;
	c.gridx=0;
	c.gridy=0;
	c.fill = GridBagConstraints.BOTH;
	for(k = 0; k < operandButtons.length; ++k ) {
	    if(c.gridx >= MAX_COLUMNS) {
		c.gridx = 0;
		c.gridy += 1;
	    }
	    buttonPanel.add(operandButtons[k], c);
	    c.gridx+=1;
	}
	c.gridx=0;
	c.gridy=0;
	//	c.gridwidth = MAX_COLUMNS;
	c.fill = GridBagConstraints.HORIZONTAL;
	mainFrame.add(displayPanel, c);
	//	c.fill = GridBagConstraints.NONE;
	c.gridy+=1;
	c.fill = GridBagConstraints.NONE;
	//	c.gridwidth
	mainFrame.add(buttonPanel, c);
	mainFrame.setVisible(true);
	mainFrame.setLocationRelativeTo(null);
	mainFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
