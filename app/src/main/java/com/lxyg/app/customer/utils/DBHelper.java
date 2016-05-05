package com.lxyg.app.customer.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 将asset里的数据库文件拷贝到系统数据库文件夹下，这样我们就可以用FinalDB来读取了
 * 
 * @author 李洋
 * 
 */
public class DBHelper {

	// 用户数据库文件的版本
	// 数据库文件目标存放路径为系统默认位置，cn.arthur.examples 是你的包名 这样可以被afinal db使用
	private static String DB_PATH = "/data/data/com.kkwy.app.business/databases/";
	/*
	 * //如果你想把数据库文件存放在SD卡的话 private static String DB_PATH =
	 * android.os.Environment.getExternalStorageDirectory().getAbsolutePath() +
	 * "/arthurcn/drivertest/packfiles/";
	 */
	private static final String DB_NAME_T = "citycode.db";

	private final Context myContext;

	/**
	 * 如果数据库文件较大，使用FileSplit分割为小于1M的小文件 此例中分割为 hello.db.101 hello.db.102
	 * hello.db.103
	 */

	public DBHelper(Context context) throws IOException {
		// this(context, DB_PATH + DB_NAME);
		myContext = context;
		// DB_PATH =myContext.getd
		// DB_PATH = context.get
		createDataBase(DB_NAME_T);
	}

	public void createDataBase(String dbName) throws IOException {
		// if (dbExist) {
		// // 数据库已存在，do nothing.
		// } else {
		// 创建数据库
		try {
			File dir = new File(DB_PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File dbf = new File(DB_PATH + dbName);
			if (dbf.exists()) {
				dbf.delete();
			}
			SQLiteDatabase.openOrCreateDatabase(dbf, null);
			// 复制asseets中的db文件到DB_PATH下
			copyDataBase(dbName, dbName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// }
	}


	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase(String dbName, String assetsName)
			throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(assetsName);
		// Path to the just created empty db
		String outFileName = DB_PATH + dbName;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}


}
