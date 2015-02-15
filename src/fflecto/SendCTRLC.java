package fflecto;

/* 
 * Sends CTRL-C to running processes from Java (in Windows)
 * and ca get ProcessID(s) for a given process name.
 * IMPORTANT!
 * This function NEEDS SendSignalC.exe in the ext\ subdirectory.
 * @author Kai Goergen
 */

import java.io.*;
import java.util.*;

public class SendCTRLC {


    /**
     * Get all PIDs for a given name and send CTRL-C to all
     * @param processName
     * @return
     */
    public static List<String> sendCTRLC(String processName) {
        // get all ProcessIDs for the processName
        List<String> processIDs = getProcessIDs(processName);
        System.out.println("" + processIDs.size() + " PIDs found for " + processName + ": " + processIDs.toString());
        for (String pid : processIDs) {
            // close it
            sendCTRLC(Integer.parseInt(pid));
        }
        return processIDs;
    }

    /**
     * Send CTRL-C to the process using a given PID
     * @param processID
     */
    public static void sendCTRLC(int processID) {
        System.out.println(" Sending CTRL+C to PID " + processID);
        try {
            Process p = Runtime.getRuntime().exec("cmd /c ext\\SendSignalC.exe " + processID);
            //StreamGobbler.StreamGobblerLOGProcess(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get List of PIDs for a given process name
     * @param processName
     * @return
     */
    public static List<String> getProcessIDs(String processName) {
        List<String> processIDs = new ArrayList<String>();
        try {
            String line;
            Process p = Runtime.getRuntime().exec("tasklist /v /fo csv");
            BufferedReader input = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (!line.trim().equals("")) {
                    // Pid is after the 1st ", thus it's argument 3 after splitting
                    String currentProcessName = line.split("\"")[1];
                    // Pid is after the 3rd ", thus it's argument 3 after splitting
                    String currentPID = line.split("\"")[3];
                    if (currentProcessName.equalsIgnoreCase(processName)) {
                        processIDs.add(currentPID);
                    }
                }
            }
            input.close();
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        return processIDs;

    }
}