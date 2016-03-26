package common.image.load;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import common.library.utils.CheckUtils;

public class ImageUtils {
	public Bitmap m_bm; 
	
	public static CGSize getImageSize(String path)
	{
		CGSize size = new CGSize(0, 0);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		ExifInterface exif;
		try 
		{
			exif = new ExifInterface(path);
			BitmapFactory.decodeFile(path, options);
		} catch (OutOfMemoryError e) {
			return size;
		} catch (IOException e) {
			return size;
		}
		
	    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_90);
	    if( exifOrientation == ExifInterface.ORIENTATION_ROTATE_90 ||
	    		exifOrientation == ExifInterface.ORIENTATION_ROTATE_270 )
	    {
			size.height = options.outWidth;
			size.width = options.outHeight;	    	
	    }
	    else
	    {
			size.width = options.outWidth;
			size.height = options.outHeight;
	    }
	    
		
		return size;
	}
	
	public static Bitmap getValidateBitmap(String path, CGSize outSize )
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		try 
		{
			BitmapFactory.decodeFile(path, options);
		} catch (OutOfMemoryError e) {
			return null;
		} 
		
		int size = options.outWidth * options.outHeight * 4;
		
		long	availablesize = android.os.Debug.getNativeHeapAllocatedSize();
		if( availablesize == 0 )
			availablesize = size;

		float	scale = (float)size / availablesize;
		
		if( outSize != null )
		{
			float scale1 = (float)options.outWidth * options.outHeight / ( outSize.width * outSize.height );
			
			if( scale1 > scale )
				scale = scale1;
		}
		
		int s = 1;
		while(true)
		{	
			if( s * s > scale )
				break;
			s++;
		}
				
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inSampleSize = s;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		
		Bitmap bm = null;
		ExifInterface exif;
		try {
			exif = new ExifInterface(path);
		    int exifOrientation = exif.getAttributeInt(
		    	    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_90);
		    int exifDegree = exifOrientationToDegrees(exifOrientation);
		    try 
			{
		    	bm = BitmapFactory.decodeFile(path, opt);
			} catch (OutOfMemoryError e) {
				return null;
			}
			
			bm = rotate(bm, exifDegree);		    
		} catch (IOException e) {
			
			e.printStackTrace();
			return null;
		}	
		
		if( bm == null )
			bm = null;
		return bm;
	}
	
	
	public static int exifOrientationToDegrees(int exifOrientation)
	{
	  if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
	  {
	    return 90;
	  }
	  else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
	  {
	    return 180;
	  }
	  else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
	  {
	    return 270;
	  }
	  return 0;
	}
	

	public static Bitmap rotate(Bitmap bitmap, int degrees)
	{
	  if(degrees != 0 && bitmap != null)
	  {
	    Matrix m = new Matrix();
	    m.setRotate(degrees, (float) bitmap.getWidth() / 2, 
	    (float) bitmap.getHeight() / 2);
	    
	    try
	    {
	      Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
	      bitmap.getWidth(), bitmap.getHeight(), m, true);
	      if(bitmap != converted)
	      {
	        bitmap.recycle();
	        bitmap = converted;
	      }
	    }
	    catch(OutOfMemoryError ex)
	    {
	      
	    }
	  }
	  return bitmap;
	}
	
	public static byte[] getbyteArrayFromBitmap(Bitmap bm)
	{
		 // ï¿½ëŒ€ï¿½ï§žï¿½ï¿½ ï¿½ê³¹ì†´ï¿½ï¿½ï§�ìšŠì¾¶ ï¿½ëš¯ìŸ¾ï¿½ì’—ê¶“ï¿½ï¿½
		
		if( bm == null )
			return null;
		
		int cx = bm.getWidth();
		int cy = bm.getHeight();
		int nBufferSize = cx * cy * 4;
		try
		{
			long availablesize = android.os.Debug.getNativeHeapAllocatedSize();
			
			if( availablesize < nBufferSize )
			{
				return null;
			}
			
			ByteBuffer byBuff = null;
			try {
				byBuff = ByteBuffer.allocate(nBufferSize);
			} catch (OutOfMemoryError e) {		
				byBuff = null;
				System.gc();
				return null;
			}
				
			if( byBuff == null )
				return null;
			
		    bm.copyPixelsToBuffer(byBuff);
		
			return byBuff.array();
			
		}
		catch(IllegalArgumentException e )
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static DisplayImageOptions.Builder buildUILOption(int loading, int empty, int fail)
	{
		return new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.showImageOnLoading(loading)
		.showImageForEmptyUri(empty)
		.showImageOnFail(fail);
	}
	
	public static DisplayImageOptions.Builder buildUILOption(int loading, int fail)
	{
		return buildUILOption(loading, loading, fail);		
	}
	
	public static DisplayImageOptions.Builder buildUILOption(int icon)
	{
		return buildUILOption(icon, icon, icon);		
	}	
	
	public static void createThumbnail(String videoPath, String thumbPath)
	{
		Bitmap thumbBitmap = ImageLoader.getInstance().loadImageSync("file://" + videoPath);
		saveBitmap(thumbBitmap, thumbPath);
	}
	
	public static void saveBitmap(Bitmap bitmap, String path)
	{
		if( bitmap == null || CheckUtils.isEmpty(path) )
			return;
		
		FileOutputStream out = null;
		try {
		    out = new FileOutputStream(path);
		    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
		    // PNG is a lossless format, the compression factor (100) is ignored
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (out != null) {
		            out.close();
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}

}
