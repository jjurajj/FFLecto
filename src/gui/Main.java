package gui;

import ffjava.FFLeCTo;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JLabel;
import javax.swing.Timer;

import javax.swing.ButtonGroup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import jdk.nashorn.internal.codegen.CompilerConstants;

public class Main {

	private JFrame frame;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JTextField domainField;
	private JTextField roomField;
	private JTextField configField;
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	private Thread urlThread;
        private JLabel statusLabel;
	private int broj = 10;
	private Timer timer;
	private Timer timer1;
	private JButton btnStop;
        public final String lecto_dir_win = "C:\\Lecto";
        public String ffmpeg_path;
        public final String ffmpeg_version = "ffmpeg -version";
        public String ffmpeg_to_path = "setx path \"%PATH%;###\" ";
        public final String ffmpeg_list = "ffmpeg -list_devices true -f dshow -i dummy";
    
    private ArrayList<String> parseDevices(String text, String type) {
        
        ArrayList<String> devices = new ArrayList<>();
        
        // po defaultu uzmi video, a ak ne onda audio deviceove
        if (type=="video")
            text = text.substring(text.indexOf("DirectShow video devices"),text.indexOf("DirectShow audio devices"));
        else if (type=="audio")
            text = text.substring(text.indexOf("DirectShow audio devices")); 
        else return devices;
        
        while (text.contains("\"")) {
            int start = text.indexOf("\"");
            text = text.substring(start+1);
            int end = text.indexOf("\"");
            devices.add(text.substring(0,end));
            text = text.substring(end+1);
        }
        return devices;
    }
        
    public String writeRunBatGetText (String command, String txt_file) throws IOException {
    
        String bat_file = "\\temp_skripta.bat";
        File file = new File(lecto_dir_win + bat_file);
        file.createNewFile();
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.println(command);
        writer.close();
        Process p = Runtime.getRuntime().exec(lecto_dir_win + bat_file); //"cmd /c start a.bat"
            try {
                p.waitFor();
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        file.delete();
        
        file = new File(lecto_dir_win + txt_file);
        if (file.exists()) {
            List<String> lines = Files.readAllLines(Paths.get(lecto_dir_win + txt_file),StandardCharsets.UTF_8);
            String text = lines.toString();
            return text;
        } else {
            // Nema autputa ili je nes neuspjelo
            return "";
            //String custom_path = (String)JOptionPane.showInputDialog(frame, "Neuspjesno odredivanje ffmpeg verzije.", "Fatal error.", JOptionPane.INFORMATION_MESSAGE);
            //System.exit(0);
        }
        
    }
        
    public void initializeOnWindoes(JFrame frame) throws IOException {

        // Napravi lecto win folder
        boolean success=true;
        File file = new File(lecto_dir_win);
	if (!file.exists()) {success = file.mkdir();}
        if (!success) {
            String custom_path = (String)JOptionPane.showInputDialog(frame, "Neuspjesna inicijalizacija FFLecto alata.", "Fatal error.", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        // procitaj je li zapisana lokacija ffmpega u config.txtu ili pitaj za nju
        file = new File(lecto_dir_win+"\\config.txt");
	try {
            BufferedReader in = new BufferedReader(new FileReader(lecto_dir_win+"\\config.txt"));
            String line = in.readLine();
            this.ffmpeg_path = line;
            in.close();
        } catch (FileNotFoundException ex) {
            this.ffmpeg_path = getFFMPEGPath(frame);
            // tu sad jos napisi config.txt fajl s lokaicjom
        }
        if (this.ffmpeg_path.isEmpty()) {
            String custom_path = (String)JOptionPane.showInputDialog(frame, "Neuspjesno odredivanje ffmpeg putanje.", "Fatal error.", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        // napisi i izvrti version skriptu
        String version_cmd = ffmpeg_version + ">" + lecto_dir_win + "\\version.txt";
        String version_text = writeRunBatGetText (version_cmd, "\\version.txt");
        
        // ako izlaz version skripte nije ocekivan onda dodaj ffmpeg u path i probaj opet
        if (version_text.indexOf("ffmpeg version") > 4) {
            String add_ffmpeg_to_path_cmd = this.ffmpeg_path.replace(("###"), this.ffmpeg_path);
            version_text = writeRunBatGetText (add_ffmpeg_to_path_cmd, "\\version.txt");
            version_cmd = ffmpeg_version + ">" + lecto_dir_win + "\\version.txt";
            version_text = writeRunBatGetText (version_cmd, "\\version.txt");
            if (version_text.substring(0, 14) != "ffmpeg version") {
                String error_message = (String)JOptionPane.showInputDialog(frame, "Neuspjesno odredivanje ffmpeg verzije.", "Fatal error.", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
        
    }
        
    // Za dohvacanje putanje do ffmpega. Ali bolje da ga se doda u path.
    public String getFFMPEGPath(JFrame frame) {
        
        String path = "C:\\Program\\ Files\\ffmpeg\\bin\\ffmpeg.exe";
        String path_x86 = "C:\\Program Files(x86)\\ffmpeg\\bin\\ffmpeg.exe";
        String custom_path = "C:\\Program Files\\ffmpeg\\bin\\ffmpeg.exe";
        File f = new File(path);
        File f_custom;
        File f_x86 = new File(path_x86);
        
        if (f.exists()) {
            return path; //.replace("Program Files", "PROGRA~1");
        } else if (f_x86.exists()) {
            return path_x86.replace("Program Files(x86)", "PROGRA~2");
        } else {
            
            do {
                Object[] possibilities = {null};
                custom_path = (String)JOptionPane.showInputDialog(frame, "FFMPEG alat je neophodan za izvršavanje Lecta i nije pronaden na loaciji \"" + custom_path + "\".\n Molim unesite putanju do ffmpeg.exe datoteke ili preuzmite FFMPEG alat s www.ffmpeg.org i pokušajte ponovo.", "ffmpeg.exe lokacija", JOptionPane.INFORMATION_MESSAGE);
                if (custom_path != null){
                    f_custom = new File(custom_path + "\\ffmpeg.exe");
                } else {
                    return null;            //Za cancel opciju 
                }
            } while (!f_custom.exists());
            
            if (custom_path.contains("\\Program Files(x86)\\")) custom_path = custom_path.replace("Program Files(x86)", "progra~2");
            else if (custom_path.contains("\\Program Files\\")) custom_path = custom_path.replace("Program Files", "progra~1");
            
            return custom_path;
        }
            
        //frame,"Putanja za ffmpeg:", "Customized Dialog", JOptionPane.QUESTION_MESSAGE, possibilities, "C:\\");
                
    }
        
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

        private enum Actions {
            START,
            STOP,
            RESEND
        }
        
	private void initialize() {
                
            frame = new JFrame();
            frame.setTitle(Messages.getString("Main.mainTitle.title")); //$NON-NLS-1$
            frame.setBounds(200, 100, 500, 470);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            final JPanel panel = new JPanel();
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            panel.setLayout(null);

            // Ovo  postavlja ffmpeg path, po potrebi ga dodaje u path ili locira
            try {
                initializeOnWindoes(frame);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Sad trebamo izlistat i proparsat deviceove
            String devices_cmd = this.ffmpeg_list + " 2>" + lecto_dir_win + "\\devices.txt";
            ArrayList<String> video_dev, audio_dev = new ArrayList<>();
            try {
                String devices_text = writeRunBatGetText (devices_cmd, "\\devices.txt");
                video_dev = parseDevices(devices_text, "video");
                audio_dev = parseDevices(devices_text, "audio");
                // Sad parsamo taj tekst
            } catch (IOException ex) {
                String error_message = (String)JOptionPane.showInputDialog(frame, "Neuspjesno listanje medijskih inputa.", "Fatal error.", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            
                
                int y_offset = 20;
                
                /*String ffmpeg_path = getFFMPEGPath(frame);                               // Provjeri lokaciju ffmpeg.exe
                if (ffmpeg_path.equals(null)) {
                    System.out.print("Nedefinirana putanja do FFMPEGa");
                }*/

                
                FFLeCTo ff = new FFLeCTo();
                
                //ArrayList<String> devices = ff.listDevices(ffmpeg_path, lecto_dir_win);
                
                JLabel lblNewLabel_domain = new LocLabel("Main.lblNewLabel.text");
		lblNewLabel_domain.setText(Messages.getString("Main.customFFMPEGCommand.text")); //$NON-NLS-1$
		lblNewLabel_domain.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_domain.setBounds(30, y_offset, 350, 30);
		panel.add(lblNewLabel_domain);

                domainField = new JTextField();
		domainField.setBounds(200, y_offset + 4, 240, 25);
		domainField.setText(Messages.getString("Main.commandDummy.text"));
                domainField.setColumns(8);
		panel.add(domainField);
		
                Scanner s;
		try {
			s = new Scanner(Paths.get("settings.txt"));
                        String domena = "";
                        
                        // Ucitaj custom domenu
                        if (s.hasNextLine()) {
                            domena = s.nextLine();
                        }
                        
                        domainField.setText(domena);
                        
			s.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JSeparator separator = new JSeparator();
		separator.setBounds(10, y_offset + 75, 260, 2);
		panel.add(separator);

                // Na vrhu start i stop botun
                //4 checkbotuna za 4 av kombinacije
                // za svaku zadajem ekstenziju (samo mp4, dimenzije, kvalitetu ili bitrate)
                // Video: source, ekstenzija, dimenzije, bitrate/kvaliteta
                // Audio source, ekstenzija, bitrate
                // sve se pamti i ucitava
                // Na dnu custom ffmpeg command
                
                
                JLabel label = new LocLabel("Main.label.text"); //$NON-NLS-1$
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(30, 110, 118, 20);
		panel.add(label);
                
                final JComboBox<String> comboOutput = new JComboBox<String>();
                comboOutput.addItem(Messages.getString("Main.btnPlainText.text"));
                comboOutput.addItem(Messages.getString("Main.btnFindConfigFile.text"));
                comboOutput.addItem(Messages.getString("Main.btnNewButton.text"));
                comboOutput.setBounds(30, 150, 118, 20);
                panel.add(comboOutput);
                
                comboOutput.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        JComboBox<String> combo = (JComboBox<String>) event.getSource();
                        String selectedOutput = (String) combo.getSelectedItem();
                        
                        //ItemCount provjeravam jer je on jednak nuli u trenutku kad resetiram Iteme i pozove se action listener
                        if ((comboOutput.getItemCount()>0) && (selectedOutput.equals(Messages.getString("Main.btnFindConfigFile.text")))) {
                              final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						Scanner sc = new Scanner(file);
						PrintWriter pw = new PrintWriter("tempConfig.txt");
						while (sc.hasNextLine()) {
							pw.write(sc.nextLine() + System.getProperty("line.separator"));
						}
						sc.close();
						pw.close();
						String[] arr = file.toString().split("\\\\");
						configField.setText(arr[arr.length - 1]);
						//configFile.doClick();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
                                        ;
                                } else if (returnVal == JFileChooser.CANCEL_OPTION) { //Ako je kliknuo cancel onda vrati na plain text
                                    comboOutput.setSelectedItem(Messages.getString("Main.btnPlainText.text"));                                    
                                }
                        } else if ((comboOutput.getItemCount()>0) && (selectedOutput.equals(Messages.getString("Main.btnNewButton.text")))) {
                                comboOutput.setSelectedItem(Messages.getString("Main.btnPlainText.text"));                                    
                                new NewConfig();                                
                        } else if ((comboOutput.getItemCount()>0) && (selectedOutput.equals("Plain text"))) {
                        	configField.setText(Messages.getString("Main.textField.text"));
                        }
                        
                    }
                });

                configField = new JTextField();
                configField.setEditable(false);
		configField.setText(Messages.getString("Main.textField.text"));
		configField.setBounds(30, 170, 119, 20);
                configField.setColumns(10);
		panel.add(configField);
                
		final JCheckBox userId = new LocCheckBox("Main.chckbxSenderId.text"); //$NON-NLS-1$
		userId.setSelected(true);
		userId.setBounds(180, 148, 97, 23);
		panel.add(userId);

		final JCheckBox message = new LocCheckBox("Main.chckbxMessage.text"); //$NON-NLS-1$
		message.setSelected(true);
		message.setBounds(180, 168, 97, 23);
		panel.add(message);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, y_offset + 200 , 260, 2);
		panel.add(separator_1);

		JLabel lblInputControl = new LocLabel("Main.lblInputControl.text"); //$NON-NLS-1$
		lblInputControl.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblInputControl.setBounds(30, y_offset + 210, 154, 30);
		//panel.add(lblInputControl);

                y_offset = y_offset - 30;
                


                ActionListener al_big = new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    if (arg0.getActionCommand().equals("TimerCommand")) {
                        
                        statusLabel.setText(Messages.getString("Main.statusLabel1")+" "+broj+" "+ Messages.getString("Main.statusLabel2"));
                        broj--;
                        if (broj == -1) {
                            timer.stop();
                            broj = 10;
                            statusLabel.setText(Messages.getString("Main.statusLabel3"));
                            urlThread.start();
                        }
                        return;
                    } else if (arg0.getActionCommand().equals("ResendTimerCommand")) {
                        
                        broj--;
                        statusLabel.setText(Messages.getString("Main.statusLabel1")+" "+broj+" "+ Messages.getString("Main.statusLabel2"));
                        if (broj == 0) {
                            
                            timer.stop();
                            broj = 10;
                            statusLabel.setText(Messages.getString("Main.currentlyResending"));
                            //resender.run();
                            statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
                        }
                        return;
                    }
                    
                    if (arg0.getActionCommand() == Actions.RESEND.name()) {
                        try {
                            PrintWriter pw = new PrintWriter("newMessage.txt");
                            pw.close();
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            PrintWriter pw = new PrintWriter("oldMessage.txt");
                            pw.close();
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    }
                    
                    String domena = proofDomain(domainField.getText());
                    boolean pisi = false;
                    try {
                        URL proba = new URL(domena);
                        pisi = true;
                    } catch (MalformedURLException e) {
                        JOptionPane.showMessageDialog(null,Messages.getString("Main.urlError"));
                        return;
                    }
                    
                    PrintWriter pw = null;
                    try {
                        pw = new PrintWriter("settings.txt");
                    } catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(null,Messages.getString("Main.settingsError"));
                        return;
                    }
                    
                    if (pisi) {pw.write(domena);}
                    String nl = System.getProperty("line.separator");
                    
                    if (comboOutput.getSelectedItem().equals("Plain text")) {pw.write(nl + "Plain");}
                    else {pw.write(nl + "Config");}
                    
                    if (userId.isSelected()) {pw.write(nl + "UserId");}
                    else {pw.write(nl + "NoUserId");}
                    
                    if (message.isSelected()) {pw.write(nl + "Message");}
                    else {pw.write(nl + "NoMessage");}
                    pw.close();
                    
                    String soba = roomField.getText();
                    if (checkRoom(soba)) {
                        
                        String connect_to = "";
                        if (arg0.getActionCommand() == Actions.RESEND.name()) {
                            // dodatno, na resend all treba resendat i stat
                            // tu su sve pohranjene poruke
                            connect_to = "/studentMessages.txt";
                        } else if (arg0.getActionCommand() == Actions.START.name()) {
                            // tu je samo jedna, zadnja  poslana poruka
                            // tu prvu ne bi trebalo ispisati, to treba podesit u URL handleru
                            connect_to = "/zadnjiOdgovor.txt";
                        }
                        
                        try {
                            URL myUrl = new URL(domena + soba + connect_to);
                            try {
                                InputStream input = myUrl.openStream();
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null,Messages.getString("Main.urlError"));
                            }
                            if (arg0.getActionCommand() == Actions.RESEND.name()) {
                                //this.Url = myUrl;
                                timer.start();
                                timer.setActionCommand("ResendTimerCommand");
                                //resender = new UrlResendHandler(myUrl);
                                // Ovo treba pricekat da se izvrsi do kraja pa 
                                //urlThread = new Thread(new UrlResendHandler(myUrl));
                            } else if (arg0.getActionCommand() == Actions.START.name()) {
                                //urlThread = new Thread(new UrlHandler(myUrl));
                                timer.start();
                                timer.setActionCommand("TimerCommand");                                
                            }
                        } catch (MalformedURLException e2) {
                            JOptionPane.showMessageDialog(null,Messages.getString("Main.domainError"));
                        }
                        
                    } else {
                        JOptionPane.showMessageDialog(null,Messages.getString("Main.roomError"));
                    }
                    
                }
            };
		
		//timer = new Timer(1000, al);
                timer = new Timer(1000, al_big);

                //http://stackoverflow.com/questions/5936261/how-to-add-action-listener-that-listens-to-multiple-buttons
                
		JButton btnStart = new LocButton("Main.btnStart.text"); //$NON-NLS-1$
		btnStart.setActionCommand(Actions.START.name());
                btnStart.addActionListener(al_big);
		btnStart.setBounds(35+5, y_offset + 250, 89, 23);
		panel.add(btnStart);

		btnStop = new LocButton("Main.btnStop.text"); //$NON-NLS-1$
                btnStop.setActionCommand(Actions.STOP.name());
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//UrlHandler.setGotovo();
				if (timer.isRunning()) {
                                    timer.stop();
                                    broj = 10;
                                }
                                statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
				
			}
		});
		btnStop.setBounds(150+5, y_offset + 250, 89, 23);
		panel.add(btnStop);

		JButton btnResend = new LocButton("Main.btnResend.text"); //$NON-NLS-1$
		btnResend.setActionCommand(Actions.RESEND.name());
                btnResend.setBounds(85+5, y_offset + 290, 100, 25);
		btnResend.addActionListener(al_big);
		panel.add(btnResend);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, y_offset + 340, 260, 2);
		panel.add(separator_2);

		//JLabel statusLabel = new LocLabel(Messages.getString("Main.lblToInitiate.text")); //$NON-NLS-1$
                statusLabel = new JLabel(Messages.getString("Main.lblToInitiate.text")); //$NON-NLS-1$
                //statusLabel = new JLabel(Messages.getString("Main.lblToInitiate.text")); //$NON-NLS-1$
                statusLabel.setFont(new Font("Tahoma", 2, 12));
                statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
                //statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
		statusLabel.setBounds(30+5, y_offset + 360, 260, 30);
		panel.add(statusLabel);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(Box.createHorizontalGlue());
		frame.setJMenuBar(menuBar);

		JMenu mnLanguage = new LocMenu("Main.mnLanguage.text");
		menuBar.add(mnLanguage);

		JRadioButtonMenuItem rdbtnmntmCroatian = new JRadioButtonMenuItem(
				Messages.getString("Main.rdbtnmntmCroatian.text"));
		rdbtnmntmCroatian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("hr", "HR"));
				Messages.changeBundle();
				Messages.fire();
                                // Ovo ne bi trebalo ic tu ali iz nekog razloga mora;
                                configField.setText(Messages.getString("Main.textField.text"));
                                comboOutput.removeAllItems();
                                comboOutput.addItem(Messages.getString("Main.btnPlainText.text"));
                                comboOutput.addItem(Messages.getString("Main.btnFindConfigFile.text"));
                                comboOutput.addItem(Messages.getString("Main.btnNewButton.text"));
                                statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
                                
			}
		});
		buttonGroup.add(rdbtnmntmCroatian);
		mnLanguage.add(rdbtnmntmCroatian);

		JRadioButtonMenuItem rdbtnmntmEnglish = new JRadioButtonMenuItem(
				Messages.getString("Main.rdbtnmntmEnglish.text")); //$NON-NLS-1$
		rdbtnmntmEnglish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("en", "US"));
				Messages.changeBundle();
				Messages.fire();
                                // Ovo ne bi trebalo ic tu ali iz nekog razloga mora;
                                configField.setText(Messages.getString("Main.textField.text"));
                                statusLabel.setText(Messages.getString("Main.lblToInitiate.text"));
                                comboOutput.removeAllItems();
                                comboOutput.addItem(Messages.getString("Main.btnPlainText.text"));
                                comboOutput.addItem(Messages.getString("Main.btnFindConfigFile.text"));
                                comboOutput.addItem(Messages.getString("Main.btnNewButton.text"));
                
			}
		});
		rdbtnmntmEnglish.setSelected(true);
		buttonGroup.add(rdbtnmntmEnglish);
		mnLanguage.add(rdbtnmntmEnglish);

		JMenu mnAbout = new LocMenu("Main.mnAbout.text"); //$NON-NLS-1$
		menuBar.add(mnAbout);

		JMenuItem mntmNewMenuItem = new LocMenuItem(Messages.getString("Main.mntmNewMenuItem.text")); //$NON-NLS-1$
                mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//new About();
                            
                                JPanel p = new JPanel(new java.awt.GridLayout(0, 1));
                                JLabel about_content = new JLabel();
                                p.setSize(470, 430);
                                about_content.setBounds(10, 10, 450, 410);
                                about_content.setText(Messages.getString("Main.mnAbout.content"));
                                p.add(about_content);
                                
                                JFrame about_window = new About();
                                about_window.setSize(470,480);
                                //about_window.getContentPane().setBackground(Color.DARK_GRAY);
                                about_window.setTitle(Messages.getString("Main.mntmNewMenuItem.text"));
                                about_window.setContentPane(p);
                        }
		});
		mnAbout.add(mntmNewMenuItem);
	}

	public void initFiles() {
		File f = new File(Paths.get("settings.txt").toString());
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("settings.txt");
				pw.write("http://www.auress.org/");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H settings.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		f = new File(Paths.get("tempConfig.txt").toString());
		f.deleteOnExit();
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("tempConfig.txt");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H tempConfig.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		f = new File(Paths.get("oldMessage.txt").toString());
		f.deleteOnExit();
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("oldMessage.txt");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H oldMessage.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		f = new File(Paths.get("newMessage.txt").toString());
		f.deleteOnExit();
		try {
			if (!f.isFile()) {
				PrintWriter pw = new PrintWriter("newMessage.txt");
				pw.close();
			}
			//Runtime.getRuntime().exec("attrib +H newMessage.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        
        public boolean checkRoom(String soba) {
        
            if ((soba.length()==4) && (soba.substring(0, 1).matches("[0-9]")) && (soba.substring(1, 2).matches("[0-9]")) && (soba.substring(2, 3).matches("[0-9]")) && (soba.substring(3, 4).matches("[0-9]"))) {
                return true;
            } else {
                return false;
            }
            
        }
        
        public String proofDomain(String domena) {
        
            if (!domena.startsWith("http://"))
                domena = "http://" + domena;
            if (!domena.endsWith("/"))
                domena = domena + "/";
            return domena;
        }

}
