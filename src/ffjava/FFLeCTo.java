/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffjava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static java.lang.Compiler.command;
import static java.lang.System.in;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author juraj
 */
public class FFLeCTo {

    public String ffmpeg_path;
    public static String ffmpeg_list_devices = "-list_devices true -f dshow -i dummy";
    public static String devices_file = "devices.txt";
    public static String win_redirecting = "2>";
    public static String kill_win = "taskkill /F /IM ffmpeg.exe";
    /**
     * @param args the command line arguments
     */
    //JFrame frame = new JFrame("InputDialog Example #1");
    
    
    public Process runFFCommand(String ff_command) {
    
        try {

            // Pokreni naredbu
            Process p = Runtime.getRuntime().exec(ff_command);
            
            // Provjera jel se ffmpeg zavrtio kao proces
            BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line, pidInfo = "";
            while ((line = input.readLine()) != null) {
                pidInfo+=line; 
            }
            input.close();

            if(pidInfo.contains("ffmpeg")) {
                return p;
            } else {
                // Dialog o neuspjesnom pokretanju ffmpega
            } return null;
        } catch (IOException ex) {
            Logger.getLogger(FFLeCTo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
    
    public void writeToProcess (Process p, String command) {
    
        OutputStream ostream = p.getOutputStream(); //Get the output stream of the process, which translates to what would be user input for the commandline
        try {
            ostream.write(command.getBytes());       //write out the character Q, followed by a newline or carriage return so it registers that Q has been 'typed' and 'entered'.
            ostream.flush();
        } catch (IOException ex) {
            Logger.getLogger(FFLeCTo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String readFromProcess (Process p) throws IOException {
        
        InputStream istream = p.getInputStream();
        String input = "";
            for (int i = 0; i < istream.available(); i++) {
                //input = input.concat(istream.read());
                System.out.println("" + istream.read());
            }
        return input;
    }
            
    public ArrayList<String> listDevices(String ffexe_path, String app_dir) throws FileNotFoundException {
    
        try {
            
            ArrayList<String> devices = new ArrayList<>();
            try {
                String command = ffexe_path + " " + ffmpeg_list_devices  + " " + win_redirecting + app_dir + "\\" + devices_file;
                
                Process p = Runtime.getRuntime().exec(command);
            } catch (IOException ex) {
                Logger.getLogger(FFLeCTo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            BufferedReader in = new BufferedReader(new FileReader(app_dir + "\\" + devices_file));
            String line;
            while((line = in.readLine()) != null)
            {
                if (line.contains("DirectShow video devices")) {
                    while (((line = in.readLine()) != null) && (line.contains("\""))) {
                        int begin_index = line.indexOf("\"");
                        int end_index = line.lastIndexOf("\"");
                        devices.add(line.substring(begin_index, end_index));
                    }
                }
                if (line.contains("DirectShow audio devices")) {
                    while (((line = in.readLine()) != null) && (line.contains("\""))) {
                        int begin_index = line.indexOf("\"");
                        int end_index = line.lastIndexOf("\"");
                        devices.add(line.substring(begin_index, end_index));
                    }
                }
            }
            in.close();
            return devices;
            
        } catch (IOException ex) {
            Logger.getLogger(FFLeCTo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void main(String[] args) {
        
        JFrame frame = new JFrame("InputDialog Example #1");
        //this.ffmpeg_path = getFFMPEGPath(frame);
        
        
        
        try {
            // TODO code application logic here
            String run = "C:\\Users\\juraj\\Desktop\\ffmpeg-20141022-git-6dc99fd-win32-static\\ff-prompt.bat";
            //String command = "ffmpeg -f dshow -i video=\"Integrated Camera\" C:\\Users\\juraj\\Desktop\\ffmpeg-20141022-git-6dc99fd-win32-static\\bin\\drugi.mp4";
            //String command = "ffmpeg -i webcam_capture.avi -t 00:00:05 -c:v libx264 -crf 28 bla.mp4\n";
            //String command = "C:\\Users\\juraj\\Desktop\\ffmpeg-20141022-git-6dc99fd-win32-static\\bin\\ffmpeg.exe -i C:\\Users\\juraj\\Desktop\\ffmpeg-20141022-git-6dc99fd-win32-static\\bin\\webcam_capture.avi -t 00:00:05 -c:v libx264 -crf 28 C:\\Users\\juraj\\Desktop\\ffmpeg-20141022-git-6dc99fd-win32-static\\bin\\bla.mp4";
            //String command = "C:\\Users\\juraj\\Desktop\\ffmpeg-20141022-git-6dc99fd-win32-static\\bin\\ffmpeg.exe -f dshow -i video=\"UScreenCapture\" C:\\Users\\juraj\\Desktop\\ffmpeg-20141022-git-6dc99fd-win32-static\\bin\\webcam_capture.mp4";
            //String command = "C:\\Users\\juraj\\Desktop\\ffmpeg-20141022-git-6dc99fd-win32-static\\bin\\ffmpeg.exe -f dshow -i video=\"Webcam 1200\" -c:v libx264 -crf 22 C:\\Users\\juraj\\Desktop\\ffmpeg-20141022-git-6dc99fd-win32-static\\bin\\webcam_capture.mp4";
            String command = "C:\\Users\\juraj\\Desktop\\ffmpeg-20141022-git-6dc99fd-win32-static\\bin\\ffmpeg.exe -list_devices true -f dshow -i dummy > C:\\Users\\juraj\\Desktop\\devices.txt";
            Process p = Runtime.getRuntime().exec(command);
            OutputStream ostream = p.getOutputStream(); //Get the output stream of the process, which translates to what would be user input for the commandline
            InputStream istream = p.getInputStream();
            String input = "";
            for (int i = 0; i < istream.available(); i++) {
                //input = input.concat(istream.read());
                System.out.println("" + istream.read());
            }

            //ostream.write(command.getBytes());
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FFLeCTo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            ostream.write("q\n".getBytes());       //write out the character Q, followed by a newline or carriage return so it registers that Q has been 'typed' and 'entered'.
            ostream.flush(); 
            //Runtime.getRuntime().exec("kill -SIGINT 3904"); /F /IM
        } catch (IOException ex) {
            Logger.getLogger(FFLeCTo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public String getFfmpeg_path() {
        return ffmpeg_path;
    }

    public void setFfmpeg_path(String ffmpeg_path) {
        this.ffmpeg_path = ffmpeg_path;
    }
    
    
    
}
