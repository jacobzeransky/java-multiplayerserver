package views;
import java.awt.Dimension;
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
	private JButton cb, dcb, lib, cub;
	private JTextField usern;
	private JPasswordField userp, ruserp;
	private JLabel un, up, rup;
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
		
		lib = new JButton("Login");
		lib.setVerticalTextPosition(AbstractButton.CENTER);
		lib.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
		lib.setMnemonic(KeyEvent.VK_L);
		lib.setActionCommand("login");
		
		cub = new JButton("Creat User");
		cub.setVerticalTextPosition(AbstractButton.CENTER);
		cub.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
		cub.setMnemonic(KeyEvent.VK_L);
		
		un = new JLabel("Username: ");
		usern = new JTextField(20);
		up = new JLabel("Password: ");
		userp = new JPasswordField(20);
		rup =new JLabel("Repeat password: ");
		ruserp = new JPasswordField(20);
		
		//Listen for actions on buttons 1 and 3.
		cb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if (uclient.connect()){
    				//JOptionPane.showMessageDialog(frame, "Connect successful.");
    				//showLogin();
    			}
    			else{
    				JOptionPane.showMessageDialog(frame, "Connect not successful.");
    			}
            }
        });
		dcb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if (uclient.disconnect()){
    				JOptionPane.showMessageDialog(frame, "Disconnect successful.");
    				dcb.setEnabled(false);
    				cb.setEnabled(true);
    			}
    			else{
    				JOptionPane.showMessageDialog(frame, "Disconnect not successful.");
    			}
            }
        });
		lib.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
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
        });
		cub.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if (usern.getText().length() < 4){
    				JOptionPane.showMessageDialog(frame, "Username too short");
    			}
    			else if (userp.getPassword().length < 4){
    				JOptionPane.showMessageDialog(frame, "Password too short.");
    			}
    			else if (differentPasswords(userp.getPassword(), ruserp.getPassword())){
    				JOptionPane.showMessageDialog(frame, "Passwords dont match.");
    			}
    			else{
    				int resp = uclient.createUser(usern.getText(), userp.getPassword());
    				if (resp == 0){
    					JOptionPane.showMessageDialog(frame, "Creation successful");
    				}
    				else if (resp == 1){
    					JOptionPane.showMessageDialog(frame, "user already exists");
    					
    				}
    				else{
    					JOptionPane.showMessageDialog(frame, "database error");
    				}
    			}
            }
            
            private boolean differentPasswords(char[] p1, char[] p2){       	
            	for (int i=0;i<p1.length;i++){
        			if (p1[i] != p2[i]){
        				return true;
        			}
        		}
            	return false;
            }
        });
	//	b1.setToolTipText("Click this button to disable the middle button.");
		
		//Add Components to this container, using the default FlowLayout.
		add(cb);
		add(dcb);
		add(lib);
		add(cub);
		add(un);
		add(usern);
		add(up);
		add(userp);
		add(rup);
		add(ruserp);
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
			
		} else if ("disconnect".equals(e.getActionCommand())) {
			
		}
		else if ("login".equals(e.getActionCommand())){
			
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
