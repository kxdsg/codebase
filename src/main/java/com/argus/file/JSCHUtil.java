package com.argus.file;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Java Secure Channel工具类
 */
public class JSCHUtil {

	private static final Logger logger = LoggerFactory.getLogger(JSCHUtil.class);

	private JSCHUtil() {}

	public static class JSCHUtilHolder{
		private static final JSCHUtil instance = new JSCHUtil();
	}

	public static JSCHUtil getInstance(){
		return JSCHUtilHolder.instance;
	}

	private Session getSession(String host, int port, String ueseName)
			throws Exception {
		JSch jsch = new JSch();
		Session session = jsch.getSession(ueseName, host, port);
		return session;
	}

	public Session connect(String host, int port, String ueseName,
			String password) throws Exception {
		Session session = getSession(host, port, ueseName);
		logger.debug("Session created.");
		session.setPassword(password);
		Properties config = new Properties();
		config.setProperty("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
        logger.debug("Session connected.");
		return session;
	}

	public String execCmd(Session session, String command) throws Exception {
		if (session == null) {
			throw new RuntimeException("Session is null!");
		}
		ChannelExec exec = (ChannelExec) session.openChannel("exec");
		logger.debug("Opening exec Channel.....");
		InputStream in = exec.getInputStream();
		byte[] b = new byte[1024];

		exec.setCommand(command);
		exec.connect();
		StringBuffer buffer = new StringBuffer();
		while (in.read(b) > 0) {
			buffer.append(new String(b));
		}
		exec.disconnect();

		return buffer.toString();
	}

	public void close(Session session) {
		if (session != null && session.isConnected()) {
			session.disconnect();
			session = null;
		}
	}


}
