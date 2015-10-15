/**
 * Copyright Trimble Inc., 2013 - 2014 All rights reserved.
 * 
 * Licensed Software Confidential and Proprietary Information of Trimble Inc.,
 * made available under Non-Disclosure Agreement OR License as applicable.
 * 
 * Product Name: ${Supervisor}
 * 
 * Module Name: com.trimble.supervisor.common
 * 
 * File name: ${LogReaderTask.java}
 * 
 * Author: ${Deepika}
 * 
 * Created On: ${25/04/14}
 * 
 * Abstract:
 * Reads the logcat messages and prints them
 * to a file.
 * 
 * Environment: Mobile Profile :Android
 * 
 * 
 * Notes:
 * 
 * Revision History:
 * 
 * 
 */
package com.trimble.fsm.fieldmaster.service.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import android.content.Context;
import android.os.Environment;

import com.trimble.fsm.fieldmaster.service.R;

public class LogReaderTask {

	private static final String TAG = "LogReaderTask";
	public static String SUPERVISOR_FOLDERNAME = null;

	private final String[] LOGCAT_CMD = new String[] { "logcat", "-v", "time" };

	private final int BUFFER_SIZE = 1024;

	private final int FILE_SIZE = 3 * 1024 * 1024; // 3 mb

	private Process mLogprocess = null;

	private boolean mIsRunning = true;

	private BufferedReader mReader = null;

	private String[] mLine = null;

	public static String ST_LOG_PATH = null;

	public final static String ST_LOG_FILE = "log.txt";

	public final static String SEND_LOG = "agLog.txt";

	private BufferedWriter mBufferedWriter = null;

	private FileWriter mFileWriter = null;

	private PrintWriter mWriter = null;

	private static LogReaderTask logReaderTask = null;

	private Thread mThread = null;

	public static LogReaderTask getInstance(final Context context) {
		if (logReaderTask == null) {
			logReaderTask = new LogReaderTask(context);
		}
		return logReaderTask;
	}

	private LogReaderTask(final Context context) {
		SUPERVISOR_FOLDERNAME = context.getString(R.string.app_name);
		ST_LOG_PATH = getStoreRoot() + File.separator + "log" + File.separator;
		makeFileOut();

		/*
		 * try { logprocess = Runtime.getRuntime().exec(LOGCAT_CMD_CLEAR);
		 * logprocess.destroy();
		 * 
		 * } catch (FileNotFoundException e) {
		 * 
		 * android.util.Log.e(TAG,e.getMessage(),e); } catch (IOException e) {
		 * 
		 * android.util.Log.e(TAG,e.getMessage(),e); }
		 */

	}

	/**
	 * creates a log file and writes messages into it.
	 */

	private void makeFileOut() {
		if (mWriter != null) {
			return;
		}
		if (!ServiceUtils.isSDCardMount()) {
			return;
		}

		// File logFile = new
		// File(context.getFileStreamPath(ST_LOG_FILE).getAbsolutePath());
		File logDir = new File(ST_LOG_PATH);
		File logFile = new File(ST_LOG_PATH + ST_LOG_FILE);
		if (!logDir.exists()) {
			logDir.mkdirs();
			try {
				logFile.createNewFile();
			} catch (IOException e) {

				android.util.Log.e(TAG, e.getMessage(), e);
			}
		}
		checkFileSize();
		try {

			mFileWriter = new FileWriter(logFile, true);
			mBufferedWriter = new BufferedWriter(mFileWriter);
			mWriter = new PrintWriter(mBufferedWriter, true);
		} catch (IOException e) {
			android.util.Log.e(TAG, e.getMessage(), e);
		}

	}

	/**
	 * if log file size reaches the limit then the old log file will be deleted
	 * and new file is created.
	 */
	private boolean checkFileSize() {
		boolean isExist = false;
		File logFile = new File(ST_LOG_PATH + ST_LOG_FILE);
		// new File(context.getFileStreamPath(ST_LOG_FILE).getAbsolutePath());
		if (logFile.length() > FILE_SIZE) {
			boolean isMaxSizeReached = logFile.delete();

			android.util.Log.d(TAG, " file deleted :" + isMaxSizeReached);

			try {
				boolean isNewFile = logFile.createNewFile();
				android.util.Log.d(TAG, " after file delete new file create :"
						+ isNewFile);
			} catch (IOException e) {
				android.util.Log.e(TAG, e.getMessage(), e);
			}
		}
		isExist = logFile.exists();
		return isExist;
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			writeToFile();

		}
	};

	public void execute() {
		mThread = new Thread(runnable, "YellowSub logger thread");
		mThread.start();
	}

	private void writeToFile() {
		InputStreamReader readerStreamReader = null;

		try {
			mLogprocess = Runtime.getRuntime().exec(LOGCAT_CMD);
			readerStreamReader = new InputStreamReader(
					mLogprocess.getInputStream());
			mReader = new BufferedReader(readerStreamReader, BUFFER_SIZE);
		} catch (IllegalArgumentException e) {
			android.util.Log.e(TAG, e.getMessage(), e);

			mIsRunning = false;
		} catch (IOException e) {
			android.util.Log.e(TAG, e.getMessage(), e);

			mIsRunning = false;
		}

		mLine = new String[1];

		try {
			while (mIsRunning) {
				if (Thread.interrupted()) {
					break;
				}
				mLine[0] = mReader.readLine();
				try {
					if (ServiceUtils.isSDCardMount() && mLine[0] != null) {
						makeFileOut();
						checkFileSize();
						if (mWriter != null) {
							mWriter.println(mLine[0]);
						}
					} else {
						// android.util.Log.e(TAG,"log file can't access error");
					}
				} catch (Throwable e) {
					android.util.Log.e(TAG, e.getMessage(), e);
					mIsRunning = false;
				}
			}
		} catch (IOException e) {
			android.util.Log.e(TAG, e.getMessage(), e);

			mIsRunning = false;
		} finally {
			if (mFileWriter != null) {
				try {
					mFileWriter.close();
				} catch (IOException e) {

					android.util.Log.e(TAG, e.getMessage(), e);
				}
			}

			if (mBufferedWriter != null) {
				try {
					mBufferedWriter.close();
				} catch (IOException e) {

					android.util.Log.e(TAG, e.getMessage(), e);
				}
			}

			if (mWriter != null) {
				try {
					mWriter.close();
				} catch (Exception e) {

					android.util.Log.e(TAG, e.getMessage(), e);
				}
			}
			if (readerStreamReader != null) {
				try {
					readerStreamReader.close();
				} catch (IOException e) {

					android.util.Log.e(TAG, e.getMessage(), e);
				}
			}
			if (mReader != null) {
				try {
					mReader.close();
				} catch (IOException e) {

					android.util.Log.e(TAG, e.getMessage(), e);
				}
			}

		}
	}

	/**
	 * Releases the instances.
	 */
	public void clear() {
		stopTask();

		mLogprocess = null;
		// logprocess.destroy();
		logReaderTask = null;

	}

	public void stopTask() {
		mIsRunning = false;
		if (mLogprocess != null)
			mLogprocess.destroy();
		if (mThread != null) {
			mThread.interrupt();
		}
		if (mFileWriter != null) {
			try {
				mFileWriter.close();
			} catch (IOException e) {

				android.util.Log.e(TAG, e.getMessage(), e);
			}
		}

		if (mBufferedWriter != null) {
			try {
				mBufferedWriter.close();
			} catch (IOException e) {

				android.util.Log.e(TAG, e.getMessage(), e);
			}
		}

		if (mWriter != null) {
			try {
				mWriter.close();
			} catch (Exception e) {

				android.util.Log.e(TAG, e.getMessage(), e);
			}
		}
	}

	public boolean copyFile(String stDesFOlderPath, String logFileName,
			String stSource) {

		boolean isSucess = false;
		if (!ServiceUtils.isSDCardMount()) {
			return isSucess;
		}

		File logDir = new File(stDesFOlderPath);
		File sendLogFile = new File(stDesFOlderPath + logFileName);
		if (!logDir.exists()) {
			boolean isDirCreated = logDir.mkdirs();
			if (!isDirCreated) {
				android.util.Log.e(TAG, " log directory not created");
				return isSucess;
			}

		}
		if (!sendLogFile.exists()) {
			boolean isLogFileCreated = false;
			try {
				isLogFileCreated = sendLogFile.createNewFile();
			} catch (IOException e) {
				android.util.Log.e(TAG, e.getMessage(), e);
			}
			if (!isLogFileCreated) {
				android.util.Log.e(TAG, " log file not created");
				return isSucess;
			}
		}

		// final File clogFile = new
		// File(context.getFileStreamPath(ST_LOG_FILE).getAbsolutePath());
		final File currentlogFile = new File(stSource);
		long clogFileLength = currentlogFile.length();
		FileOutputStream fileos = null;
		FileInputStream fileInputStream = null;
		try {
			fileos = new FileOutputStream(sendLogFile);
			fileInputStream = new FileInputStream(currentlogFile);
			// fileInputStream =context.openFileInput(ST_LOG_FILE);
			byte bData[] = new byte[4 * 1024];
			int iReadData = 0;

			while (clogFileLength > 0) {
				iReadData = fileInputStream.read(bData);
				fileos.write(bData);
				clogFileLength = clogFileLength - iReadData;
			}
			isSucess = true;
		} catch (IOException e) {

			android.util.Log.e(TAG, e.getMessage(), e);
		} finally {

			if (fileos != null) {
				try {
					fileos.close();
				} catch (IOException e) {
					android.util.Log.e(TAG, e.getMessage(), e);
				}
			}

			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					android.util.Log.e(TAG, e.getMessage(), e);
				}
			}
		}
		return isSucess;

	}

	/**
	 * Returns path of log file
	 */
	public File getLogFilePath() {

		File logFile = new File(ST_LOG_PATH + SEND_LOG);
		return logFile;
	}

	public static String getStoreRoot() {

		File SDCARD_PATH = Environment.getExternalStorageDirectory();

		String UF_FLAG_STORAGE = SDCARD_PATH.getAbsolutePath() + File.separator
				+ SUPERVISOR_FOLDERNAME;

		return UF_FLAG_STORAGE;
	}
}
