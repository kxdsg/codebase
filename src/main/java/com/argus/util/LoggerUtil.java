package com.argus.util;

import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoggerUtil {

	private Logger logger;
	private String pkgName;

	public static LoggerUtil getLogger(String loggerName) {
		return new LoggerUtil(loggerName);
	}

	private LoggerUtil(String loggerName) {
		this.logger = Logger.getLogger(loggerName);
	}

	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public void warnSql(SQLException sqle, String sql) {
		logger.warn("SQL:Encountered [" + sqle.getMessage()
				+ "] while tring to execute statement [" + sql + "]");
	}

	public void errorSql(SQLException sqle, String sql) {
		logger.error("SQL:Encountered [" + sqle.getMessage()
				+ "] while tring to execute statement [" + sql + "]");
	}

	public static LoggerUtil getInstance(Class loggerPkgName) {
		return getInstance(loggerPkgName.getName());
	}

	public static LoggerUtil getInstance(String loggerPkgName) {
		Logger logger = Logger.getLogger(loggerPkgName);

		return new LoggerUtil(logger);
	}

	private LoggerUtil(Logger logger) {
		this.logger = logger;
	}

	public boolean isInfo() {
		return logger.isEnabledFor(Level.INFO);
	}

	public void logInfo(Object... objectList) {
		if (isInfo()) {
			logger.info(StringUtil.concat(objectList));
		}
	}

	public boolean isWarn() {
		return logger.isEnabledFor(Level.WARN);
	}

	public void logWarn(Object... objectList) {
		if (isWarn()) {
			logger.warn(StringUtil.concat(pkgName, "-", objectList));
		}
	}

	public boolean isDebug() {
		return logger.isEnabledFor(Level.DEBUG);
	}

	public void logDebug(Object... objectList) {
		if (isDebug()) {
			logger.debug(StringUtil.concat(objectList));
		}
	}

	public void logError(Object... objectList) {
		logger.error(StringUtil.concat(objectList));
	}

	public void logError(Exception e) {
		logger.error(e.getMessage(), e);
	}

	public void logError(Exception e, Object... objectList) {
		logger.error(e.getMessage(), e);
		logger.error(StringUtil.concat(objectList));
	}

	public void debug(Object... objs) {
		if (isDebug())
			this.logger.debug(StringUtil.concat(objs));
	}

	public void info(Object... objs) {
		if (isInfo())
			this.logger.info(StringUtil.concat(objs));
	}

	public void warn(Object... objs) {
		if (isWarn())
			this.logger.warn(StringUtil.concat(objs));
	}

	public void error(Exception e) {
		e.printStackTrace();
		this.logger.error(e.getMessage(), e);
	}

	public void error(Exception e, Object... objs) {
		e.printStackTrace();
		error(new Object[] { e.getMessage(), StringUtil.concat(objs) });
	}

	public void error(Object... objs) {
		logger.error(StringUtil.concat(objs));
	}

	public static void tryException(int instance) throws Exception {
		if (instance > 10) {
			throw new Exception("This is a test Exception");
		} else {
			instance++;
			try {
				tryException(instance);
			} catch (Exception e) {
				LoggerUtil logger = LoggerUtil
						.getInstance("com.kang.testClass.error");
				logger.logError(e);
				throw e;
			}
		}
	}

	public static String constructStackTrace(Exception e) {
		StackTraceElement[] elems = e.getStackTrace();
		boolean firstElem = true;
		StringBuffer buf = new StringBuffer();
		for (StackTraceElement elem : elems) {
			if (firstElem) {
				firstElem = false;
			} else {
				buf.append("\n    ");
			}
			buf.append(elem.getClassName() + "." + elem.getMethodName() + ":"
					+ elem.getLineNumber());
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		LoggerUtil logger = LoggerUtil.getInstance("com.kang.testClass");
		logger.logDebug("This is the sample debug info ", new Date());
		logger.logInfo("This is the sample debug info ", new Date());

		logger = LoggerUtil.getInstance("anotherpkg.testClass");
		logger.logDebug("This is the sample debug info ", new Date());
		logger.logInfo("This is the sample debug info ", new Date());
		logger.logWarn("This is the sample debug info ", new Date());

		try {
			tryException(0);
		} catch (Exception e) {
			logger.logError(e);
		}

		System.out.println("Done!");
	}

}
