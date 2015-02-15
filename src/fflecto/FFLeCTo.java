/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fflecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static java.lang.Compiler.command;
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
    
    /**
     * @param args the command line arguments
     */
    //JFrame frame = new JFrame("InputDialog Example #1");
    
    public String getFFMPEGPath(JFrame frame) {
        
        String path = "C:\\Program Files\\ffmpeg\\ffmpeg.exe";
        String path_x86 = "C:\\Program Files(x86)\\ffmpeg\\ffmpeg.exe";
        String custom_path = "";
        File f = new File(path);
        File f_custom;
        File f_x86 = new File(path_x86);
        
        if (f.exists()) {
            return path;
        } else if (f_x86.exists()) {
            return path_x86;
        } else {
            
            do {
                Object[] possibilities = {null};
                custom_path = (String)JOptionPane.showInputDialog(frame, "FFMPEG nije pronaden na uobicajenoj loaciji: " + path + ". Molim unesite lokaciju ffmpeg.exe datoteke", possibilities);
                f_custom = new File(custom_path + "\\ffmpeg.exe");
            } while (!f_custom.exists());
            
            return custom_path;
        }
            
        //frame,"Putanja za ffmpeg:", "Customized Dialog", JOptionPane.QUESTION_MESSAGE, possibilities, "C:\\");
                
    }
    
    public void createAppDir() {
    }
    
    public ArrayList<String> listDevices(String ffexe_path, String app_dir) {
    
        ArrayList<String> devices = new ArrayList<>();
        try {
            String command = ffexe_path + " " + ffmpeg_list_devices  + " " + win_redirecting + app_dir + " " + devices_file;
            Process p = Runtime.getRuntime().exec(command);
        } catch (IOException ex) {
            Logger.getLogger(FFLeCTo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Otvori i parsaj fajl i vrati listu divajsova
        
        return devices;
    }
    
    public void main(String[] args) {
        
        JFrame frame = new JFrame("InputDialog Example #1");
        this.ffmpeg_path = getFFMPEGPath(frame);
        
        
        
        
        //try {
            //provjeri da li postoji fajl ffmpeg.exe na lokacijama
            // 
            // 
            
            /* do something */
            //C:\Program Files(x86)\ffmpeg\ffmpeg.exe
            
        //} catch {
        //}
        
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
            //BufferedReader is = new BufferedReader(new InputStreamReader(recordProcess.getErrorStream()));
            //String line;
            //while((line = is.readLine()) != null){
            
            //} 
            
            //Runtime.getRuntime().exec("taskkill /F /IM ffmpeg.exe");
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
