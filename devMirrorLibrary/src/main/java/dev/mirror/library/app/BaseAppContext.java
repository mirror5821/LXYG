package dev.mirror.library.app;

import net.tsz.afinal.FinalBitmap;
import android.app.Application;
import android.text.TextUtils;
import android.view.View;

public class BaseAppContext extends Application{
	protected static FinalBitmap sFinalBitmap;
	private static String IMG_TOP = "";

	@Override
	public void onCreate() {
		sFinalBitmap = FinalBitmap.create(this);
	};

	public static void displayImage(View v,String url){
		if(!TextUtils.isEmpty(url)){
			if(!url.startsWith("http://"))
				sFinalBitmap.display(v, IMG_TOP+url);
			else
				sFinalBitmap.display(v, url);
		}
	}

	public static void deleteBitmap(){
		sFinalBitmap.clearCache();
		sFinalBitmap.clearDiskCache();
		sFinalBitmap.clearMemoryCache();
	}
}
