package com.argus.quartz;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzUtil {

	private static Scheduler scheduler = null;

	private static synchronized void stopScheduler () {
		try {
			scheduler.shutdown();
			scheduler = null;
		} catch (SchedulerException e) {
			System.err.println("Unable to stop scheduler");
			e.printStackTrace();
		}
	}

	public static void scheduleJobEverySecond (String jobName, Class jobClass, int interval) {
		Trigger trigger = TriggerUtils.makeSecondlyTrigger(interval);
		trigger.setStartTime(new Date());
		trigger.setName(jobName + "Trigger");
		scheduleJob(jobName, jobClass, trigger);
	}

	public static void scheduleJobEveryMin (String jobName, Class jobClass, int interval) {
		Trigger trigger = TriggerUtils.makeMinutelyTrigger(interval);
		trigger.setStartTime(new Date());
		trigger.setName(jobName + "Trigger");
		scheduleJob(jobName, jobClass, trigger);
	}

	public static synchronized void scheduleJob (String jobName, Class jobClass, Trigger trigger) {
		JobDetail jobDetail = new JobDetail(jobName, "JobGroup", jobClass);

		try {
			getScheduler().scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			System.err.print("Unable to schedule job " + jobName  + " for jobClass " + jobClass.getName());
			e.printStackTrace();
		}
	}

	private static synchronized Scheduler getScheduler () {
		if (scheduler == null) {
			try {
				scheduler = StdSchedulerFactory.getDefaultScheduler();
				scheduler.start();
			} catch (SchedulerException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return scheduler;
	}

}
