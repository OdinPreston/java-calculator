import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;

import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

class Gui {
    final static int MAX_COLUMNS = 5;
    final static Color STANDARD_BACKGROUND = Color.decode("#fdf6e3");
    final static Color STANDARD_FOREGROUND = Color.decode("#657b83");
    final static Color ALTERNATIVE_BACKGROUND = Color.decode("#eee8d5");
    final static Color YELLOW = Color.decode("#b58900");
    final static Color ORANGE = Color.decode("#cb4b16");
    final static Color GREEN = Color.decode("#859900");
    final static Color BASE1 = Color.decode("#93a1a1");

    private String expression = new String("");
    private JDialog mainDialog;
    private JButton buttons[];
    private JPanel buttonPanel;
    private JPanel displayPanel;
    private JTextField textField;
    private JScrollBar scrollBar;
    private static GridBagConstraints c = new GridBagConstraints();

    private JButton[] createButtons() {
	String labels[] = { "D", "*", "/", "1", "2", "3", "C", "+", "-", "4", "5", "6", "Q", "(", ")", "7", "8", "9", "^", "_", ".", "0", "=" };
	final JButton buttons[] = new JButton[labels.length];
	int i;
	for(i = 0; i < labels.length; ++i) {
	    buttons[i] = new JButton(labels[i]);
	    buttons[i].setFont(new Font("Default", Font.PLAIN, 40));
	    buttons[i].setBackground(STANDARD_BACKGROUND);
	    buttons[i].setForeground(STANDARD_FOREGROUND);	    
	}
	return buttons;
    }

    private void bindButtons(JButton buttons[]) {
	int i;
	for(i = 0; i < buttons.length; ++i) {
	    final int j = i;
	    buttons[i].addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
			bindButton(buttons[j]);
			}
		});	    
	}
    }

    private void bindButton(JButton button) {
	switch(button.getText().toCharArray()[0]) {
	case 'D':
	    expression = (expression.length() > 0) ? expression.substring(0, expression.length()-1) : "";
	    textField.setText(expression);
	    break;
	case 'C':
	    expression = "";
	    textField.setText(expression);
	    break;
	case 'Q':
	    System.exit(0);
	    break;
	case '_':
	    expression += " ";
	    textField.setText(expression);
	    break;
	case '=':
	    Notation n;
	    try {
		n = Notation.getType(expression);
		expression = "" + n.evaluate(n.tokenize(expression));
		textField.setText(expression);
		expression = "";
	    } catch(WrongExpressionException e) {
		JOptionPane.showMessageDialog(null, e.message);
		textField.setText(expression);
	    }
	    break;
	default:
	    expression += button.getText();
	    textField.setText(expression);	    
	    break;
	}
    }

    private JDialog createMainDialog() {
	JDialog mainDialog = new JDialog();
	mainDialog.getContentPane().setBackground(STANDARD_BACKGROUND);
	mainDialog.setTitle("Java calculator");
	mainDialog.setSize(350, 320);
	mainDialog.setLayout(new GridBagLayout());
	mainDialog.setLocationRelativeTo(null);
	mainDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	return mainDialog;
    }

    private JPanel createDisplayPanel() {
	JPanel displayPanel = new JPanel();
	displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
	return displayPanel;
    }

    private JPanel createButtonPanel() {
	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new GridBagLayout());
	buttonPanel.setBackground(STANDARD_BACKGROUND);
	return buttonPanel;
    }

    private JTextField createDisplayTextField() {
	JTextField textField = new JTextField("0.0");
	textField.setFont(new Font("Default", Font.PLAIN, 40));
	textField.setEditable(false);
	textField.setBackground(ALTERNATIVE_BACKGROUND);
	textField.setForeground(STANDARD_FOREGROUND);
	textField.setBorder(BorderFactory.createEmptyBorder());
	textField.addMouseListener(new MouseListener() {
		// We need to implement all of these,
		// but only mousePressed is of interest
		// (mouseClicked is press and release)
		@Override
		public void mousePressed(MouseEvent e) {
		    if(e.getButton() == MouseEvent.BUTTON1) {
			Toolkit.getDefaultToolkit()
			    .getSystemClipboard()
			    .setContents(new StringSelection(textField.getText()), null);
		    }
		}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
	    });
	return textField;
    }

    private JScrollBar createScrollBar(JTextField tf) {
	JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
	BoundedRangeModel brm = textField.getHorizontalVisibility();
	scrollBar.setModel(brm);
	scrollBar.setBackground(STANDARD_BACKGROUND);
	scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
		@Override
		protected void configureScrollBarColors() {
		    this.thumbColor = BASE1;
		}
		@Override
		protected JButton createIncreaseButton(int orientation) {
		    JButton button = super.createIncreaseButton(orientation);
		    button.setBackground(ALTERNATIVE_BACKGROUND);
		    return button;
		}
		@Override
		protected JButton createDecreaseButton(int orientation) {
		    JButton button = super.createDecreaseButton(orientation);
		    button.setBackground(ALTERNATIVE_BACKGROUND);
		    return button;
		}
	    });	
	return scrollBar;
    }

    private void fillDisplayPanel(JPanel dp, JTextField tf, JScrollBar sb) {
	displayPanel.add(tf);
	displayPanel.add(sb);	
    }

    private void fillButtonPanel(JPanel bp, JButton buttons[]) {
	int i;
	c.gridx = c.gridy = 0;
	c.fill = GridBagConstraints.BOTH;
	for(i = 0; i < buttons.length; ++i) {
	    if(c.gridx == MAX_COLUMNS+1 && c.gridy >= 2) {
		c.gridx = 1;
		c.gridy += 1;
	    } else if(c.gridx == MAX_COLUMNS+1) {
		c.gridx = 0;
		c.gridy += 1;
	    }
	    bp.add(buttons[i], c);
	    c.gridx += 1;
	}
    }

    private void fillMainDialog(JDialog md, JPanel dp, JPanel bp) {
	c.gridx=0;
	c.gridy=0;
	c.fill = GridBagConstraints.HORIZONTAL;
	md.add(displayPanel, c);
	c.gridy+=1;
	md.add(buttonPanel, c);
    }

    Gui() {
	mainDialog = createMainDialog();
	textField = createDisplayTextField();
	buttons = createButtons();
	bindButtons(buttons);
	scrollBar = createScrollBar(textField);
	displayPanel = createDisplayPanel();
	buttonPanel = createButtonPanel();
	fillDisplayPanel(displayPanel, textField, scrollBar);
	fillButtonPanel(buttonPanel, buttons);
	fillMainDialog(mainDialog, displayPanel, buttonPanel);

	mainDialog.setVisible(true);
    }
}
