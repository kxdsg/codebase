package com.argus.file;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * nginx.conf文件操作工具类
 */
public class NginxConfUtil {

	private static Logger logger = LoggerFactory.getLogger(NginxConfUtil.class);
	/*
	nginx服务器配置
	 */
	private static String nginx_ip = "127.0.0.1";
	private static String nginx_user = "admin";
	private static String nginx_pass = "123456";
	private static String nginx_dir = "/usr/local/openresty/nginx/conf/service_conf";
	private static String nginx_sbin = "/usr/local/openresty/nginx/sbin/nginx";


	/**
	 * 生成配置文件
	 * @param name
	 * @param nginx_conf
	 * @param nginx_template_conf
	 */
	public static void generateConf( String name, String nginx_conf ,String nginx_template_conf) {
		File file = new File(nginx_template_conf);
		File outFile = new File(nginx_conf);

		BufferedInputStream bin = null;
		try {
			bin = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		byte[] buff = new byte[(int) file.length()];
		try {
			bin.read(buff);
			bin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String str = new String(buff);
		str = str.replace("@[name]@", name);

		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(outFile));
			bw.write(str);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			logger.error("生成临时配置文件失败！ ");
		}
	}


	/**
	 * 上传配置文件
	 * @param session
	 * @param srcFile
	 * @param toFileDir
	 * @throws Exception
	 */
	public static void uploadConf(Session session, String srcFile,
                                   String toFileDir) throws Exception{
		ChannelSftp channelSftp = null;
			// 打开SFTP通道
		channelSftp = (ChannelSftp) session.openChannel("sftp");
		channelSftp.connect();
		channelSftp.put(srcFile, toFileDir, ChannelSftp.OVERWRITE);
		channelSftp.disconnect();
	}


	/**
	 * 建立连接
	 * @return
	 * @throws Exception
	 */
	public static Session initSession() throws Exception{
		Session session = null;
		session = JSCHUtil.getInstance().connect(nginx_ip, 22, nginx_user,
				nginx_pass);
		return session;
	}


	/**
	 * 配置路由
	 * @throws Exception
	 */
	public static void configure() throws Exception{
		String nginx_conf_path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		Session session = initSession();
		try {
			String name = "";//业务逻辑获取
			String nginx_conf = nginx_conf_path + "_" + name + ".conf";
			String nginx_template_conf = nginx_conf_path + "nginx_template.conf";
			NginxConfUtil.generateConf(name,nginx_conf,nginx_template_conf);
			NginxConfUtil.uploadConf(session, nginx_conf, nginx_dir);
			FileUtil.delete(nginx_conf);
			String cmd = nginx_sbin + " -s reload";
			JSCHUtil.getInstance().execCmd(session, cmd);
		} catch (Exception e) {
			logger.error("服务路由配置失败！");
		} finally {
			JSCHUtil.getInstance().close(session);
		}
	}

}
