package com.mgb.mybatis.service;



import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Component
public class SFTPClientFileClass {
	
	public void uploadFile(String sftpHost,String SftpUser,String sftpPwd,int sftpPort) throws SftpException {
		try {
 			JSch jsch = new JSch();
			Session sftpSession = jsch.getSession("hb8v59v8nogx0twjul89","dac-ftp.mygenomebox.com",22);
			sftpSession.setConfig("StrictHostKeyChecking", "no");
			sftpSession.setPassword("c1Gcn!wH[31D9n1!U7jD");
			sftpSession.connect();
			System.out.println("Connecting...");
			
			String sftpPath = "/hb8v59v8nogx0twjul89/REPORT_FILES";
			String localPath = "D:\\Nilophar\\PdfTemplates\\TemplateBen.pdf";
			
			Channel channel = sftpSession.openChannel("sftp");
			ChannelSftp sftpChannel = (ChannelSftp)channel;
			sftpChannel.connect();
			System.out.println("opened sftp channel");
			
			sftpChannel.put(localPath,sftpPath);
			
			sftpChannel.disconnect();
			sftpSession.disconnect();
			System.out.println("disconnected");
			
		} catch (JSchException ex) {
			ex.printStackTrace();
		}
	}

}
