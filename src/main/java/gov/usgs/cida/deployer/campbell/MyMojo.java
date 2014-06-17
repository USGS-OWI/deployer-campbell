package gov.usgs.cida.deployer.campbell;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Goal which touches a timestamp file.
 *
 * @deprecated Don't use!
 */
@Mojo( name = "deploy", defaultPhase = LifecyclePhase.DEPLOY )
public class MyMojo
    extends AbstractMojo
{
    /**
     * Location of the file.
     */
    @Parameter( property="site", required=true )
    private String _site;
    
    @Parameter(property="program_file", required=true)
    private File _program_file;
    
    @Parameter( property="campbell.cora_path", defaultValue="C:/Program Files (x86)/Campbellsci/LoggerNet/cora_cmd.exe", required=true)
    private File _cora_path;
    
    @Parameter(property="campbell.loggernet_url", defaultValue="localhost", required=true)
    private String _loggernet_url;
    
    public void execute()
        throws MojoExecutionException
    {
        getLog().info("Starting deployment");
        
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(_cora_path.getAbsolutePath());
        
        try{
            Process cora = pb.start();

            InputStream input   = cora.getInputStream();
            OutputStream output = cora.getOutputStream();

            BufferedReader buffin = new BufferedReader(new InputStreamReader(input));
            BufferedWriter buffout = new BufferedWriter(new OutputStreamWriter(output));

            while(buffin.ready()){
                String tmp = buffin.readLine();
                getLog().debug(tmp);
            }
            
            getLog().info("Connecting to loggnet at:"+_loggernet_url);
            buffout.write("connect "+_loggernet_url+";");
            buffout.flush();
            Thread.sleep(2000);
            while(buffin.ready()){
                String tmp = buffin.readLine();
                getLog().info(tmp);
            }
            
            getLog().info("Sending program file...");
            getLog().info("Site:"+_site);
            getLog().info("File:"+_program_file.getAbsolutePath());
            buffout.write("send-program-file "+_site+" \""+_program_file.getAbsolutePath()+"\";");
            buffout.flush();
            Thread.sleep(10000);
            while(buffin.ready()){
                String tmp = buffin.readLine();
                getLog().info(tmp);
            }
            
            buffout.write("exit;");
            buffout.flush();
            Thread.sleep(2000);
            
        }catch(IOException ioe){
            getLog().error(ioe);
        }catch(InterruptedException ie){
            getLog().error(ie);
        }
        
    }
}
