import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;  

//import java.lang.management.OperatingSystemMXBean;
import com.sun.management.OperatingSystemMXBean;

import javax.management.MBeanServerConnection;

import java.io.File;
/*w  w w .  j  a  v  a2s.c o m*/
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;

public class OffLoadingSystemConfig {
    
	
    public static void main(String[] args) throws Exception{
//    	 Process proc = Runtime.getRuntime().exec("cmd.exe /c start /wait windows.bat");
    	ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    	ses.scheduleAtFixedRate(new Runnable() {
    	    @Override
    	    public void run() {
    	        try {
					sendOffLineReport();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
    	}, 0, 1, TimeUnit.HOURS);
    }
    
	public static void sendOffLineReport() throws IOException {
		 String OS = System.getProperty("os.name").toLowerCase();
		 System.out.println(OS);
		 ProcessBuilder pb = null;
		 String[] files = null;
		 File folder;
		 File[] listOfFiles;
		 if (OS.indexOf("win") >= 0) {
			 pb = new ProcessBuilder("cmd", "/c", "windows-short.lnk");
			 folder = new File("C:/data");
	    	 files = new String[]{"c:/data/process.html", "c:/data/nic.html", "c:/data/cpu.html", "c:/data/battstat.csv", "c:/data/battery.csv", "c:/data/memory.html"};
		 } else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 ) {
			 pb = new ProcessBuilder("cmd", "/c", "linux-shortcut.lnk");
			 folder = new File("data");
			 files = new String[]{"~/data/process.txt", "~/data/network.txt", "~/data/memory.htable", "c:/data/battstat.txt"};
		 }
		 
	   	 Process p = pb.start();    	 
	   	 p.destroy();    	 
	    	 String to="balajiit@gmail.com";//change accordingly  
	    	  final String user="kowthalganesh.kowthalraj@gmail.com";//change accordingly  
	    	  final String password="ganeshganesh";//change accordingly  
	    	   
	    	  //1) get the session object     
	    	  Properties props = System.getProperties();  
	    	  props.put("mail.smtp.host", "smtp.gmail.com");    
	          props.put("mail.smtp.socketFactory.port", "465");    
	          props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	          props.put("mail.smtp.auth", "true");    
	          props.put("mail.smtp.port", "465");
	          props.put("mail.smtp.starttls.enable","true");
	          props.put("mail.smtp.debug", "true");
	          props.put("mail.smtp.socketFactory.port", "465");
	          props.put("mail.smtp.socketFactory.fallback", "false");
	    	  Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {  
	    	   protected PasswordAuthentication getPasswordAuthentication() {  
	    		   return new PasswordAuthentication(user,password);  
	    	   }  
	    	  });  
	    	     
	    	  //2) compose message     
	    	  try{  
	    	    MimeMessage message = new MimeMessage(session);  
	    	    message.setFrom(new InternetAddress(user));  
	    	    message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
	    	    message.setSubject("Message Alert");
	    	      
	    	    //3) create MimeBodyPart object and set your message text     
	    	    BodyPart messageBodyPart1 = new MimeBodyPart();  
	    	    messageBodyPart1.setText("This is Summary Report from monitoring tool");  
	    	      
	    	    //4) create new MimeBodyPart object and set DataHandler object to this object      
	    	    //5) create Multipart object and add MimeBodyPart objects to this object      
	    	    Multipart multipart = new MimeMultipart(); 
	    	    multipart.addBodyPart(messageBodyPart1);  
	    	    
	    	    for(int i=0; i<files.length; i++) {
		    	    DataSource source = new FileDataSource(files[i]);  
		    	    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		    	    messageBodyPart2.setDataHandler(new DataHandler(source));  
		    	    messageBodyPart2.setFileName(files[i]);
		    	    multipart.addBodyPart(messageBodyPart2);  
		    	    //6) set the multiplart object to the message object  
		    	    message.setContent(multipart ); 
	    	    }
		     
	    	    //7) send message  
	    	    Transport.send(message);  
	    	   
	    	   System.out.println("message sent....");  
	    	   }catch (Exception ex) {ex.printStackTrace();}  
	    }
}
