/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;

import java.io.*;

    

/**
 *
 * @author lawinslow
 */
public class JavaApplication3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("C:/Program Files (x86)/Campbellsci/LoggerNet/cora_cmd.exe");
        Process cora = pb.start();
        
        InputStream input   = cora.getInputStream();
        OutputStream output = cora.getOutputStream();
        
        BufferedReader buffin = new BufferedReader(new InputStreamReader(input));
        BufferedWriter buffout = new BufferedWriter(new OutputStreamWriter(output));
        
        buffout.write("connect localhost;");
        buffout.flush();
        Thread.sleep(2000);
        while(buffin.ready()){
            String tmp = buffin.readLine();
            System.out.println(tmp);
            
        }

        buffout.write("list-tables Mendota;");
        buffout.flush();
        Thread.sleep(2000);
        while(buffin.ready()){
            String tmp = buffin.readLine();
            System.out.println(tmp);
            
        }
        
        buffout.write("exit;");
        buffout.flush();
        Thread.sleep(2000);
        
        System.out.println(cora.exitValue());
        
    }
}
