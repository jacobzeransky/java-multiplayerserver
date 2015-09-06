package views;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import main.mpsClient;

public class clientView extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton connectb, disconnectb, loginb, createuserb, joinlobbyb;
	private JTextField usernametf, chattf;
	private JPasswordField userpf, repeatuserpf;
	private JLabel usernamel, userpasswordl, repeatuserpasswordl;
	private JPanel topbuttonp, bottombuttonp, eventp, chatp, loginp;
	private JScrollPane eventsp, chatsp;
	private mpsClient uclient;
	private static JFrame frame;
	private JTextArea eventta, chatta;
	private Timer timer;
	private int state;

	public clientView() {
		super(new GridBagLayout());
		uclient = new mpsClient("0.0.0.0", 10355);
		
		connectb = new JButton("Connect");		
		disconnectb = new JButton("Disconnect");		
		loginb = new JButton("Login");		
		createuserb = new JButton("Create User");
		joinlobbyb = new JButton("Join Lobby");
		
		usernamel = new JLabel("Username: ");
		usernametf = new JTextField(10);
		userpasswordl = new JLabel("Password: ");
		userpf = new JPasswordField(10);
		repeatuserpasswordl =new JLabel("Repeat password: ");
		repeatuserpf = new JPasswordField(10);
		
		eventta = new JTextArea(10, 20);
		eventta.setEditable(false);
		eventta.setVisible(true);
		
		chatta = new JTextArea(15, 20);
		chatta.setEditable(false);
		chatta.setBackground(Color.LIGHT_GRAY);
		chattf = new JTextField(20);
		
		
		chattf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				uclient.chatMessage(chattf.getText());
				chattf.setText("");
			}
		});
		
		userpf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				loginb.doClick();
			}
		});
	//	event_area.setBackground(Color.gray);
		
		//Listen for actions on buttons 1 and 3.
		connectb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (uclient.connect()){
    				//JOptionPane.showMessageDialog(frame, "Connect successful.");
    				showLogin();
    			}
    			else{
    				JOptionPane.showMessageDialog(frame, "Connect not successful.");
    			}
            }
        });
		disconnectb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if (uclient.disconnect()){
    				//JOptionPane.showMessageDialog(frame, "Disconnect successful.");
    				showConnect();
    			}
    			else{
    				JOptionPane.showMessageDialog(frame, "Disconnect not successful.");
    			}
            }
        });
		loginb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if (usernametf.getText().length() < 4){
    				JOptionPane.showMessageDialog(frame, "Username too short");
    			}
    			else if (userpf.getPassword().length < 4){
    				JOptionPane.showMessageDialog(frame, "Password too short.");
    				
    			}
    			else{
    				int resp = uclient.login(usernametf.getText(), userpf.getPassword());
    				if (resp == 0){
    					JOptionPane.showMessageDialog(frame, "Login successful");
    					usernametf.setText("");
    					userpf.setText("");
    					
    					showMain();
    				}
    				else if (resp == 1){
    					JOptionPane.showMessageDialog(frame, "invalid password");
    					userpf.requestFocusInWindow();
    					
    				}
    				else if (resp == 2){
    					JOptionPane.showMessageDialog(frame, "unkown user");
    				}
    				else if (resp == 3){
    					JOptionPane.showMessageDialog(frame, "database error");
    				}
    				else if (resp == 4){
    					JOptionPane.showMessageDialog(frame, "user already logged in error");
    				}
    				else{
    					JOptionPane.showMessageDialog(frame, "ERROR ERROR");
    				}
    			}
            }
        });
		createuserb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if (usernametf.getText().length() < 4){
    				JOptionPane.showMessageDialog(frame, "Username too short");
    			}
    			else if (userpf.getPassword().length < 4){
    				JOptionPane.showMessageDialog(frame, "Password too short.");
    			}
    			else if (differentPasswords(userpf.getPassword(), repeatuserpf.getPassword())){
    				JOptionPane.showMessageDialog(frame, "Passwords dont match.");
    			}
    			else{
    				int resp = uclient.createUser(usernametf.getText(), userpf.getPassword());
    				if (resp == 0){
    					JOptionPane.showMessageDialog(frame, "Creation successful");
    				}
    				else if (resp == 1){
    					JOptionPane.showMessageDialog(frame, "user already exists");
    					
    				}
    				else if (resp == 2){
    					JOptionPane.showMessageDialog(frame, "database error");
    				}
    				else{
    					JOptionPane.showMessageDialog(frame, "ERROR ERROR");
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
		
		joinlobbyb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	System.out.println("lobby");
            }

        });
		
		GridBagConstraints c = new GridBagConstraints();
		
		// top button panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		
		topbuttonp = new JPanel();
		topbuttonp.add(connectb);
		topbuttonp.add(disconnectb);
		
		add(topbuttonp, c);
		
		// bottom button panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		
		bottombuttonp = new JPanel();
		bottombuttonp.add(loginb);
		bottombuttonp.add(createuserb);
		bottombuttonp.add(joinlobbyb);
		
		add(bottombuttonp, c);
		
		// login labels and text fields
		loginp = new JPanel(new GridBagLayout());
		
		c.gridx = 0;
		c.gridy = 0;
		loginp.add(usernamel, c);
		
		c.gridx = 1;
		c.gridy = 0;
		loginp.add(usernametf, c);
		
		c.gridx = 0;
		c.gridy = 1;
		loginp.add(userpasswordl, c);
		
		c.gridx = 1;
		c.gridy = 1;
		loginp.add(userpf, c);
		
		c.gridx = 0;
		c.gridy = 2;
		loginp.add(repeatuserpasswordl, c);
		
		c.gridx = 1;
		c.gridy = 2;
		loginp.add(repeatuserpf, c);
		
		c.gridx = 0;
		c.gridy = 1;
		add(loginp, c);
		
		// chat panel
		chatp = new JPanel(new GridBagLayout());
		chatsp = new JScrollPane(chatta);
		
		c.gridx = 0;
		c.gridy = 0;
		chatp.add(chatsp, c);
		
		c.gridx = 0;
		c.gridy = 1;
		chatp.add(chattf, c);
		
		c.gridx = 1;
		c.gridy = 1;
		add(chatp, c);
		
		// event text area
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 2;
		c.gridy = 1;
		eventsp = new JScrollPane(eventta);
		add(eventsp, c);
		
		showConnect();
		
		timer = new Timer(500, this);
        timer.setInitialDelay(0);
        timer.start();
               
	}
	
	private void showConnect(){
		connectb.setVisible(true);
		disconnectb.setVisible(false);
		bottombuttonp.setVisible(false);
		loginb.setVisible(true);
		createuserb.setVisible(true);
		joinlobbyb.setVisible(false);
		loginp.setVisible(false);
		chatp.setVisible(false);
		eventsp.setVisible(true);
		state = 0;
	}
	
	private void showLogin(){
		connectb.setVisible(false);
		disconnectb.setVisible(true);
		bottombuttonp.setVisible(true);		
		loginp.setVisible(true);
		state = 1;
	}
	
	private void showMain(){
		loginb.setVisible(false);
		createuserb.setVisible(false);
		loginp.setVisible(false);
		joinlobbyb.setVisible(true);
		chatp.setVisible(true);
		state = 2;
	}
	
	public void updateAreas() {
        String s;
     /*   while (true) {
        	if ((s = uclient.getEvent()) != null){
        		eventta.insert(s + "\n", eventta.getText().length());
        		eventta.setCaretPosition(eventta.getText().length());
        	}
        	else{
        		break;
        	}
        	
        	if ((s = uclient.getChat()) != null){
        		chatta.insert(s + "\n", chatta.getText().length());
        		chatta.setCaretPosition(chatta.getText().length());
        	}
        	else{
        		break;
        	}
        	//System.out.println("update");
        }*/
        while((s = uclient.getEvent()) != null){
    		eventta.insert(s + "\n", eventta.getText().length());
    		eventta.setCaretPosition(eventta.getText().length());
    	}
        if (state > 1){
	        while ((s = uclient.getChat()) != null){
	    		chatta.insert(s + "\n", chatta.getText().length());
	    		chatta.setCaretPosition(chatta.getText().length());
	    	}
        }
    }
	
	public void actionPerformed(ActionEvent e) {
		if (state > -1){
			updateAreas();
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		//Create and set up the content pane.
		clientView newContentPane = new clientView();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);
		
		//Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setSize(new Dimension(900,500));
		 
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
