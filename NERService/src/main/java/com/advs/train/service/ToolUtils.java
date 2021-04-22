package com.advs.train.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class ToolUtils {
	
	private static final Logger mLog = Logger.getLogger(ToolUtils.class);
	private static final long TIMEOUT = 60 *60*60000;
	// 得到8位的UUID-(码)
		public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
				"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
				"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
				"U", "V", "W", "X", "Y", "Z" };

		public static String getUUID_8() {
			StringBuffer shortBuffer = new StringBuffer();
			String uuid = UUID.randomUUID().toString().replace("-", "");
			for (int i = 0; i < 8; i++) {
				String str = uuid.substring(i * 4, i * 4 + 4);
				int x = Integer.parseInt(str, 16);
				shortBuffer.append(chars[x % 0x3E]);
			}
			return shortBuffer.toString().toLowerCase();

		}
		
		
		public static String genNewModel(String newModelName) {
			List<String> cmdList = new ArrayList<String>();
			cmdList.add("/home/tsdata/bin/myTrain.sh");
			cmdList.add(newModelName);
			mLog.info("run command: " + cmdList.toString());
			ProcessBuilder pb = new ProcessBuilder(cmdList);
			pb.directory(new File("/usr/local/tsjboss/standalone/WEB-INF"));
			pb.redirectErrorStream(true);
			Process p = null;
			try {
				p = pb.start();
				long startTime = System.currentTimeMillis();
				WatchLogThread watchThread = new WatchLogThread(p, mLog);
				watchThread.start();
				boolean terminate = false;
				try {
					terminate = p.waitFor(TIMEOUT, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					mLog.error("wait for nodejs ", e);
				}
				if (mLog.isDebugEnabled()) {
					mLog.debug(
							"Node script run " + (System.currentTimeMillis() - startTime) + " ms. result is " + terminate);
				}

				if (!terminate) {
					p.destroy();
					mLog.warn("Java Programe wait " + TIMEOUT / 60000 + " min time out, so end process");
				}

			} catch (IOException e) {
				mLog.error(e);
			} finally {
				if (p != null && p.isAlive()) {
					p.destroy();
					mLog.warn("Node proess is still there, so end it");
				}
			}
			return newModelName;
		}

}
