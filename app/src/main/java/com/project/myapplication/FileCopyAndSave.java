package com.project.myapplication;

import android.content.Context;
import android.content.res.AssetManager;

import com.project.myapplication.dao.UndergroundHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 최형식 on 2015-03-09.
 */
public class FileCopyAndSave {
	public static void fileCopy(Context context, String folderPath) throws IOException{
		File folder = new File(folderPath);
		File file = context.getDatabasePath(UndergroundHelper.DB_UNDERGROUND);
		AssetManager am = context.getAssets();
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			InputStream is = am.open(UndergroundHelper.DB_UNDERGROUND);
			BufferedInputStream bis = new BufferedInputStream(is);

			if(folder.exists()){

			}else{
				folder.mkdirs();
			}
			if(file.exists()){
				file.delete();
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			int read = -1;
			byte[] buffer = new byte[1024];
			while((read = bis.read(buffer, 0, 1024)) != -1) {
				bos.write(buffer, 0, read);
			}
			bos.flush();

			bos.close();
			fos.close();
			bis.close();
			is.close();
		}catch(IOException e){
			e.printStackTrace();
			//Log.e("Error"," : "+e.getMessage());
		}
	}
}