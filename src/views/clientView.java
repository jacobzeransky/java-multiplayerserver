package views;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.mpsClient;

public class clientView extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton cb, dcb, lib;
	private JTextField usern;
	private JPasswordField userp;
	private JLabel un, up;
	private mpsClient uclient;
	private static JFrame frame;

	public clientView() {
		uclient = new mpsClient("0.0.0.0", 10355);
		
		cb = new JButton("Connect");
		cb.setVerticalTextPosition(AbstractButton.CENTER);
		cb.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
		cb.setMnemonic(KeyEvent.VK_C);
		cb.setActionCommand("connect");
		
		
		dcb = new JButton("Disconnect");
		//Use the default text position of CENTER, TRAILING (RIGHT).
		dcb.setMnemonic(KeyEvent.VK_D);
		dcb.setActionCommand("disconnect");
		dcb.setEnabled(false);
		
		lib = new JButton("Login");
		lib.setVerticalTextPosition(AbstractButton.CENTER);
		lib.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
		lib.setMnemonic(KeyEvent.VK_L);
		lib.setActionCommand("login");
		lib.setVisible(false);
		
		un = new JLabel("Username: ");
		un.setVisible(false);
		usern = new JTextField(20);
		usern.setVisible(false);
		up = new JLabel("Password: ");
		un.setVisible(false);
		userp = new JPasswordField(20);
		userp.setActionCommand("login");
		userp.setVisible(false);
		
		//Listen for actions on buttons 1 and 3.
		cb.addActionListener(this);
		dcb.addActionListener(this);
		lib.addActionListener(this);
		
	//	b1.setToolTipText("Click this button to disable the middle button.");
		
		//Add Components to this container, using the default FlowLayout.
		add(cb);
		add(dcb);
		add(lib);
		add(un);
		add(usern);
		add(up);
		add(userp);
	}
	
	private void showLogin(){
		dcb.setEnabled(true);
		cb.setEnabled(false);
		
		cb.setVisible(false);
		lib.setVisible(true);
		un.setVisible(true);
		usern.setVisible(true);
		up.setVisible(true);
		userp.setVisible(true);
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		if ("connect".equals(e.getActionCommand())) {
			if (uclient.connect()){
				//JOptionPane.showMessageDialog(frame, "Connect successful.");
				showLogin();
			}
			else{
				JOptionPane.showMessageDialog(frame, "Connect not successful.");
			}
		} else if ("disconnect".equals(e.getActionCommand())) {
			if (uclient.disconnect()){
				JOptionPane.showMessageDialog(frame, "Disconnect successful.");
				dcb.setEnabled(false);
				cb.setEnabled(true);
			}
			else{
				JOptionPane.showMessageDialog(frame, "Disconnect not successful.");
			}
		}
		else if ("login".equals(e.getActionCommand())){
			if (usern.getText().length() < 4){
				JOptionPane.showMessageDialog(frame, "Username too short");
			}
			else if (userp.getPassword().length < 4){
				JOptionPane.showMessageDialog(frame, "Password too short.");
			}
			else{
				int resp = uclient.login(usern.getText(), userp.getPassword());
				if (resp == 0){
					JOptionPane.showMessageDialog(frame, "Login successful");
				}
				else if (resp == 1){
					JOptionPane.showMessageDialog(frame, "invalid password");
					
				}
				else{
					JOptionPane.showMessageDialog(frame, "unkown user");
				}
			}
		}
		else{
			
		}
	}
	
	/**
	* Create the GUI and show it.  For thread safety, 
	* this method should be invoked from the 
	* event-dispatching thread.
	*/
	private static void createAndShowGUI() {
		
		//Create and set up the window.
		frame = new JFrame("Client-side");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Create and set up the content pane.
		clientView newContentPane = new clientView();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);
		
		//Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setSize(new Dimension(500,500));
	}
	
	public static void main(String[] args) {
	//Schedule a job for the event-dispatching thread:
	//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(); 
			}
		});
	}

}