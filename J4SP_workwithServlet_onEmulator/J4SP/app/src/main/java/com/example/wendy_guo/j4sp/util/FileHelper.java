package com.example.wendy_guo.j4sp.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.R;

public class FileHelper {
	
	public static final String TAG = FileHelper.class.getSimpleName();
    public static SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT, Locale.US);



    public static final int SHORT_SIDE_TARGET = 1280;



    public static byte[] getB(Context context, Uri uri){
        byte[] data = null;
        try {
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static byte[] getBytes(Context context, Uri uri){
        InputStream iStream = null;
        try {
            iStream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        try {
            while ((len = iStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteBuffer.toByteArray();

    }
	
	public static byte[] getByteArrayFromFile(Context context, Uri uri) {
		byte[] fileBytes = null;
        InputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        
        if (uri.getScheme().equals("content")) {
        	try {
        		inStream = context.getContentResolver().openInputStream(uri);
        		outStream = new ByteArrayOutputStream();
            
        		byte[] bytesFromFile = new byte[1024*1024]; // buffer size (1 MB)
        		int bytesRead = inStream.read(bytesFromFile);
        		while (bytesRead != -1) {
        			outStream.write(bytesFromFile, 0, bytesRead);
        			bytesRead = inStream.read(bytesFromFile);
        		}
            
        		fileBytes = outStream.toByteArray();
        	}
	        catch (IOException e) {
	        	Log.e(TAG, e.getMessage());
	        }
	        finally {
	        	try {
                    if(inStream != null)
	        		    inStream.close();
                    if(outStream != null)
	        		    outStream.close();
	        	}
	        	catch (IOException e) { /*( Intentionally blank */ }
	        }
        }
        else {
        	try {
	        	File file = new File(uri.getPath());
	        	FileInputStream fileInput = new FileInputStream(file);
	        	fileBytes = IOUtils.toByteArray(fileInput);
        	}
        	catch (IOException e) {
        		Log.e(TAG, e.getMessage());
        	}
       	}
        
        return fileBytes;
	}
	
	public static byte[] reduceImageForUpload(byte[] imageData) {
//        Log.i("reduceImageForUpload","reduceImageForUpload");
		Bitmap bitmap = ImageResizer.resizeImageMaintainAspectRatio(imageData, SHORT_SIDE_TARGET);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		byte[] reducedData = outputStream.toByteArray();
		try {
			outputStream.close();
		}
		catch (IOException e) {
			// Intentionally blank
		}
		
		return reducedData;
	}

	public static String getFileName(Context context, Uri uri, String fileType) {
		String fileName = "uploaded_file.";
		
		if (fileType.equals(Constants.IMAGE_TYPE)) {
			fileName += "png";
		}
		else {
			if (uri.getScheme().equals("content")) {
				String mimeType = context.getContentResolver().getType(uri);
				int slashIndex = mimeType.indexOf("/");
				String fileExtension = mimeType.substring(slashIndex + 1);
				fileName += fileExtension;
			}
			else {
				fileName = uri.getLastPathSegment();
			}
		}
		
		return fileName;
	}
    public static File [] listFiles(Context context) {
        File fileDirectory = context.getFilesDir();
        File [] filteredFiles = fileDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.getAbsolutePath().contains(".jpg")) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        return filteredFiles;
    }

//    public static void writeTextToInternal(String s){
//        try {
//            FileOutputStream fos = openFileOutput(Constants.OFFLINE_CACHE, Context.MODE_PRIVATE);
//            fos.write(someText.toString().getBytes());
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }





    public static File saveImageToInternalStorage(Bitmap bitmapImage,Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(Constants.CACHE_IMG_FOLDER, Context.MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();
        sb.append(sharedPref.getInt(Constants.USER_ID, -1));
        sb.append("_");
        sb.append(new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()));
        sb.append(".jpg");
        // Create imageDir

        File mypath=new File(directory, File.separator + sb.toString());

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mypath;
    }


    public static void writeByteArraytoFile(Context context, byte[] content) {

        try {

            String FILENAME = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date())+".jpg";
            FileOutputStream outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write(content, 0, content.length);

            outputStream.close();
        } catch (Exception ex) {
            Log.e("JAVA_DEBUGGING", "Exception while creating save file!");
            ex.printStackTrace();
        }

    }
}