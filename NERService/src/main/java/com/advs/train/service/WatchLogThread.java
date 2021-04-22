package com.advs.train.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class WatchLogThread extends Thread {
	Process p;
	Logger mLog;

	public WatchLogThread(Process p, Logger mLog) {
		super();
		this.p = p;
		this.mLog = mLog;
	}

	@Override
	public void run() {
		try (BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
			String line = null;
			while ((line = stdout.readLine()) != null) {
				if (StringUtils.contains(line, "Error")) {
					mLog.error(line);
				} else {
					mLog.info(line);
				}
			}
			mLog.debug("watch log end");
		} catch (Exception e) {
			mLog.error(e);
		}
	}
}
