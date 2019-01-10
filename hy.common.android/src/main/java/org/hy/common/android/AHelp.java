package org.hy.common.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.xml.XJSON;

import java.io.File;
import java.io.IOException;
import java.util.Set;


/**
 * Android 的辅助类
 *
 * @author  ZhengWei(HY)
 * @version 2013-11-15
 *          2016-01-15  建立独立项目，通用化此类
 *          2017-11-27  添加：通过SharedPreferences保存、读取对象的方法。
 *          2019-01-08  添加：android中调用系统拍照，返回图片是旋转90度的解决方法
 */
public final class AHelp
{

    private AHelp()
    {
        // Nothing.
    }



    /**
     * 获取应用ID。即build.gradle配置文件中applicationId值。
     *
     * @param i_Activity
     * @return
     */
    public static String getApplicationId(Activity i_Activity)
    {
        return i_Activity.getApplication().getPackageName();
    }



    /**
     * 获取本地私有数据保存对象
     *
     * @param i_Activity
     * @return
     */
    public static SharedPreferences getPreferences(Activity i_Activity)
    {
        return i_Activity.getSharedPreferences(getApplicationId(i_Activity) ,Context.MODE_PRIVATE);
    }



    /**
     * 在本地存储私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_Value     数据本身
     */
    public static void putPreferences(Activity i_Activity ,String i_Key ,@NonNull Object i_Value) throws Exception
    {
        XJSON v_XJson = new XJSON();

        v_XJson.setAccuracy(true);
        v_XJson.setReturnNVL(false);

        putPreferences(i_Activity ,i_Key ,v_XJson.parser(i_Value).toJSONString());
    }



    /**
     * 在本地存储私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_Value     数据本身
     */
    public static void putPreferences(Activity i_Activity ,String i_Key ,@NonNull Date i_Value)
    {
        putPreferences(i_Activity ,i_Key ,i_Value.getTime());
    }



    /**
     * 在本地存储私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_Value     数据本身
     */
    public static void putPreferences(Activity i_Activity ,String i_Key ,@NonNull java.util.Date i_Value)
    {
        putPreferences(i_Activity ,i_Key ,i_Value.getTime());
    }



    /**
     * 在本地存储私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_Value     数据本身
     */
    public static void putPreferences(Activity i_Activity ,String i_Key ,String i_Value)
    {
        SharedPreferences.Editor v_Editor = getPreferences(i_Activity).edit();
        v_Editor.putString(i_Key ,i_Value);
        v_Editor.commit();
    }



    /**
     * 在本地存储私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_Value     数据本身
     */
    public static void putPreferences(Activity i_Activity ,String i_Key ,int i_Value)
    {
        SharedPreferences.Editor v_Editor = getPreferences(i_Activity).edit();
        v_Editor.putInt(i_Key ,i_Value);
        v_Editor.commit();
    }



    /**
     * 在本地存储私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_Value     数据本身
     */
    public static void putPreferences(Activity i_Activity ,String i_Key ,float i_Value)
    {
        SharedPreferences.Editor v_Editor = getPreferences(i_Activity).edit();
        v_Editor.putFloat(i_Key ,i_Value);
        v_Editor.commit();
    }



    /**
     * 在本地存储私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_Value     数据本身
     */
    public static void putPreferences(Activity i_Activity ,String i_Key ,long i_Value)
    {
        SharedPreferences.Editor v_Editor = getPreferences(i_Activity).edit();
        v_Editor.putLong(i_Key ,i_Value);
        v_Editor.commit();
    }



    /**
     * 在本地存储私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_Value     数据本身
     */
    public static void putPreferences(Activity i_Activity ,String i_Key ,boolean i_Value)
    {
        SharedPreferences.Editor v_Editor = getPreferences(i_Activity).edit();
        v_Editor.putBoolean(i_Key ,i_Value);
        v_Editor.commit();
    }



    /**
     * 在本地存储私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_Value     数据本身
     */
    public static void putPreferences(Activity i_Activity ,String i_Key ,Set<String> i_Value)
    {
        SharedPreferences.Editor v_Editor = getPreferences(i_Activity).edit();
        v_Editor.putStringSet(i_Key ,i_Value);
        v_Editor.commit();
    }



    /**
     * 删除本地存储私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     */
    public static void removePreferences(Activity i_Activity ,String i_Key)
    {
        SharedPreferences.Editor v_Editor = getPreferences(i_Activity).edit();
        v_Editor.remove(i_Key);
        v_Editor.commit();
    }



    /**
     * 获取本地存储的私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @return
     */
    public static String getPreferences(Activity i_Activity ,String i_Key)
    {
        return getPreferences(i_Activity ,i_Key ,"");
    }



    /**
     * 获取本地存储的私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_DefValue  默认值
     * @return
     */
    public static <T> T getPreferences(Activity i_Activity ,String i_Key ,@NonNull T i_DefValue)
    {
        String v_Json = getPreferences(i_Activity ,i_Key);

        if ( Help.isNull(v_Json) )
        {
            return i_DefValue;
        }

        XJSON v_XJson = new XJSON();

        v_XJson.setAccuracy(true);
        v_XJson.setReturnNVL(false);

        try
        {
            return (T) v_XJson.parser(v_Json, i_DefValue.getClass());
        }
        catch (Exception exce)
        {
            return i_DefValue;
        }
    }



    /**
     * 获取本地存储的私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_DefValue  默认值
     * @return
     */
    public static Date getPreferences(Activity i_Activity ,String i_Key ,Date i_DefValue)
    {
        long v_Time = -1L;
        if ( i_DefValue == null )
        {
            v_Time = getPreferences(i_Activity, i_Key, v_Time);

            if ( v_Time == -1L )
            {
                return null;
            }
        }
        else
        {
            v_Time = getPreferences(i_Activity, i_Key, i_DefValue.getTime());
        }

        return new Date(v_Time);
    }



    /**
     * 获取本地存储的私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_DefValue  默认值
     * @return
     */
    public static java.util.Date getPreferences(Activity i_Activity ,String i_Key ,java.util.Date i_DefValue)
    {
        long v_Time = -1L;
        if ( i_DefValue == null )
        {
            v_Time = getPreferences(i_Activity, i_Key, v_Time);

            if ( v_Time == -1L )
            {
                return null;
            }
        }
        else
        {
            v_Time = getPreferences(i_Activity, i_Key, i_DefValue.getTime());
        }

        return new java.util.Date(v_Time);
    }



    /**
     * 获取本地存储的私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_DefValue  默认值
     * @return
     */
    public static String getPreferences(Activity i_Activity ,String i_Key ,String i_DefValue)
    {
        return getPreferences(i_Activity).getString(i_Key ,i_DefValue);
    }



    /**
     * 获取本地存储的私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_DefValue  默认值
     * @return
     */
    public static int getPreferences(Activity i_Activity ,String i_Key ,int i_DefValue)
    {
        return getPreferences(i_Activity).getInt(i_Key ,i_DefValue);
    }



    /**
     * 获取本地存储的私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_DefValue  默认值
     * @return
     */
    public static float getPreferences(Activity i_Activity ,String i_Key ,float i_DefValue)
    {
        return getPreferences(i_Activity).getFloat(i_Key ,i_DefValue);
    }



    /**
     * 获取本地存储的私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_DefValue  默认值
     * @return
     */
    public static long getPreferences(Activity i_Activity ,String i_Key ,long i_DefValue)
    {
        return getPreferences(i_Activity).getLong(i_Key ,i_DefValue);
    }



    /**
     * 获取本地存储的私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_DefValue  默认值
     * @return
     */
    public static boolean getPreferences(Activity i_Activity ,String i_Key ,boolean i_DefValue)
    {
        return getPreferences(i_Activity).getBoolean(i_Key ,i_DefValue);
    }



    /**
     * 获取本地存储的私有数据
     *
     * @param i_Activity
     * @param i_Key       数据标记
     * @param i_DefValue  默认值
     * @return
     */
    public static Set<String> getPreferences(Activity i_Activity ,String i_Key ,Set<String> i_DefValue)
    {
        return getPreferences(i_Activity).getStringSet(i_Key ,i_DefValue);
    }



    /**
     * 复制文本到粘贴板中
     *
     * @param i_Activity
     * @param i_Text
     */
    public static void copyText(Activity i_Activity ,String i_Text)
    {
        ClipboardManager cm = (ClipboardManager) i_Activity.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("HY" ,i_Text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }



    /**
     * 显示等待对话框
     *
     * 用 ProgressDialog.dismiss() 方法关闭
     *
     * @param i_Activity
     * @param i_MessageRID
     * @return
     */
    public static ProgressDialog showWaitingDialog(Activity i_Activity ,int i_MessageRID)
    {
        return showWaitingDialog(i_Activity ,i_Activity.getString(i_MessageRID));
    }



    /**
     * 显示等待对话框
     *
     * 用 ProgressDialog.dismiss() 方法关闭
     *
     * @param i_Activity
     * @param i_Message
     * @return
     */
    public static ProgressDialog showWaitingDialog(Activity i_Activity ,String i_Message)
    {
        ProgressDialog v_Waiting = new ProgressDialog(i_Activity);
        v_Waiting.setIndeterminate(false);  // 循环滚动
        v_Waiting.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        v_Waiting.setMessage(i_Message);
        v_Waiting.setCancelable(false);     // false不能取消显示，true可以取消显示
        v_Waiting.show();

        return v_Waiting;
    }



    /**
     * 打开本地相册
     *
     * @param i_Activity
     * @param i_ResultCode
     */
    public static void openPhotoAlbum(Activity i_Activity ,int i_ResultCode)
    {
        Intent v_Intent = null;

        if ( Build.VERSION.SDK_INT < 19 )
        {
            v_Intent = new Intent(Intent.ACTION_GET_CONTENT);
            v_Intent.setType("image/*");
        }
        else
        {
            v_Intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }

        i_Activity.startActivityForResult(v_Intent ,i_ResultCode);
    }



    /**
     * 拍照。打开系统拍照应用
     *
     * @param i_Activity
     * @param i_ResultCode
     * @param i_Fileprovider     应在AndroidManifest.xml中配置过的文件路径名称。如：tempfiles.fileprovider
     * @param i_ImageNamePrefix  图片名称的前缀
     * @return  返回拍照保存的文件，但要等拍照完成时读取才是有效的
     */
    public static File photograph(Activity i_Activity ,int i_ResultCode ,String i_Fileprovider ,String i_ImageNamePrefix)
    {
        Intent v_PhotoGraphIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if ( v_PhotoGraphIntent.resolveActivity(i_Activity.getPackageManager()) != null )
        {
            File v_PhotoFile = createImageTempFile(i_Activity ,i_ImageNamePrefix);

            if ( v_PhotoFile != null )
            {
                try
                {
                    // v_PhotoGraphIntent.putExtra(MediaStore.EXTRA_OUTPUT ,Uri.fromFile(v_PhotoFile));

                    Uri v_PhotoURI = FileProvider.getUriForFile(i_Activity ,i_Fileprovider ,v_PhotoFile);
                    v_PhotoGraphIntent.putExtra(MediaStore.EXTRA_OUTPUT ,v_PhotoURI);
                    v_PhotoGraphIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 这里加入flag

                    i_Activity.startActivityForResult(v_PhotoGraphIntent, i_ResultCode);
                    return v_PhotoFile;
                }
                catch (Exception exce)
                {
                    exce.printStackTrace();
                    v_PhotoFile.delete();
                }
            }
        }

        return null;
    }



    /**
     * 获取图片的旋转角度
     *
     * @param i_Image 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String i_Image)
    {
        int degree = 0;

        try
        {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(i_Image);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return degree;

    }



    /**
     * 将图片按照指定的角度进行旋转
     *
     * @param bitmap 需要旋转的图片
     * @param degree 指定的旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap i_Bitmap, int i_Degree)
    {
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(i_Degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(i_Bitmap, 0, 0, i_Bitmap.getWidth(), i_Bitmap.getHeight(), matrix, true);

        if (i_Bitmap != null && !i_Bitmap.isRecycled())
        {
            i_Bitmap.recycle();
        }
        return newBitmap;
    }



    /**
     * android中调用系统拍照，返回图片是旋转90度的解决方法
     *
     * @param i_Image 图片绝对路径
     * @return
     */
    public static Bitmap createBitmap(String i_Image)
    {
        Bitmap v_Bitmap = BitmapFactory.decodeFile(i_Image);
        int    v_Degree = getBitmapDegree(i_Image);

        if ( v_Degree == 0 )
        {
            return v_Bitmap;
        }
        else
        {
            return rotateBitmapByDegree(v_Bitmap, v_Degree);
        }
    }



    /**
     * 创建图片临时文件
     *
     * @param i_ImageNamePrefix  图片名称的前缀
     * @return
     */
    public static File createImageTempFile(Activity i_Activity ,String i_ImageNamePrefix)
    {
        return createTempFile(i_Activity ,i_ImageNamePrefix ,".jpg");
    }



    /**
     * 创建临时文件
     *
     * param  i_Prefix  名称的前缀
     * @param i_Suffix  文件类型。文件名后缀，带点，如: .jpg
     * @return
     */
    public static File createTempFile(Activity i_Activity ,String i_Prefix ,String i_Suffix)
    {
        // File v_StorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File v_StorageDir = Environment.getExternalStorageDirectory();
        File v_TempFiles  = new File(v_StorageDir.getAbsolutePath() + "/tempfiles");
        File v_ImageFile  = null;

        try
        {
            if ( !v_TempFiles.exists() )
            {
                v_TempFiles.mkdir();
            }

            v_ImageFile = File.createTempFile(i_Prefix ,i_Suffix ,v_TempFiles);
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }

        return v_ImageFile;
    }



    /**
     * 将Uri转成真实的文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByUri(Context context, Uri uri)
    {
        String path = null;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            }
        }
        return null;
    }
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs)
    {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    private static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    private static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    private static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    /**
     * 是否开启了闪光灯
     *
     * @param i_Camera
     * @return
     */
    public static boolean flashlightIsOpen(Camera i_Camera)
    {
        if ( i_Camera == null )
        {
            return false;
        }

        try
        {
            Camera.Parameters v_Parameters = i_Camera.getParameters();
            String v_FlashMode = v_Parameters.getFlashMode();

            return v_FlashMode.equals(Camera.Parameters.FLASH_MODE_TORCH);
        }
        catch (Exception e)
        {
            return false;
        }
    }



    /**
     * 打开闪光灯
     *
     * @param i_Camera
     * @return
     */
    public static Camera flashLightOpen(Camera i_Camera)
    {
        Camera v_Camera = i_Camera;

        try
        {
            if ( v_Camera == null )
            {
                v_Camera = Camera.open();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return v_Camera;
        }

        Camera.Parameters v_Parameter = v_Camera.getParameters();
        v_Parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        v_Camera.setParameters(v_Parameter);

        return v_Camera;
    }



    /**
     * 关闭闪光灯。
     *
     * 注：不释放设备，请自行释放。如下代码
     *   v_Camera.release();
     *   v_Camera = null;
     *
     * @param i_Camera
     * @return
     */
    public static Camera flashLightClose(Camera i_Camera)
    {
        Camera v_Camera = i_Camera;

        if ( v_Camera != null )
        {
            Camera.Parameters v_Parameter = v_Camera.getParameters();
            v_Parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            v_Camera.setParameters(v_Parameter);
        }

        return v_Camera;
    }



    /**
     * 分享图片到微信
     *
     * @param i_ImageRID  图片ID
     * @param i_TitleRID  标题ID
     */
    public static void shareImageToWeiXin(Activity i_Activity ,int i_ImageRID ,int i_TitleRID)
    {
        Bitmap v_Bitmap      = BitmapFactory.decodeResource(i_Activity.getResources() ,i_ImageRID);
        Uri v_URIByImage  = Uri.parse(MediaStore.Images.Media.insertImage(i_Activity.getContentResolver(), v_Bitmap, null, null));
        Intent v_ShareIntent = new Intent();

        v_ShareIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI")); // 发送图片给好友
        v_ShareIntent.setAction(Intent.ACTION_SEND);
        v_ShareIntent.putExtra(Intent.EXTRA_STREAM, v_URIByImage);
        v_ShareIntent.setType("image/*");
        i_Activity.startActivity(Intent.createChooser(v_ShareIntent, i_Activity.getResources().getString(i_TitleRID)));
    }



    /**
     * 分享图片到所有应用
     *
     * @param i_ImageRID  图片ID
     * @param i_TitleRID  标题ID
     */
    public static void shareImageToAll(Activity i_Activity ,int i_ImageRID ,int i_TitleRID)
    {
        Bitmap v_Bitmap      = BitmapFactory.decodeResource(i_Activity.getResources() ,i_ImageRID);
        Uri v_URIByImage  = Uri.parse(MediaStore.Images.Media.insertImage(i_Activity.getContentResolver(), v_Bitmap, null, null));
        Intent v_ShareIntent = new Intent();

        v_ShareIntent.setAction(Intent.ACTION_SEND);
        v_ShareIntent.putExtra(Intent.EXTRA_STREAM, v_URIByImage);
        v_ShareIntent.setType("image/*");
        v_ShareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i_Activity.startActivity(Intent.createChooser(v_ShareIntent, i_Activity.getResources().getString(i_TitleRID)));
    }



    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getBrand()
    {
        return Build.BRAND;
    }



    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel()
    {
        return Build.MODEL;
    }



    /**
     * 获取当前应用的版本号
     *
     * @return
     */
    public static String getVersion(Activity i_Activity)
    {
        try
        {
            PackageManager v_PackageManager = i_Activity.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo v_PackInfo       = v_PackageManager.getPackageInfo(i_Activity.getPackageName() ,0);

            return v_PackInfo.versionName;
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }

        return "";
    }



    /**
     * IMEI由15位数字组成，其组成为：
     * 1、前6位数（TAC，Type ApprovalCode)是"型号核准号码"，一般代表机型。
     * 2、接着的2位数（FAC，Final Assembly Code)是"最后装配号"，一般代表产地。
     * 3、之后的6位数（SNR)是"串号"，一般代表生产顺序号。
     * 4、最后1位数（SP)通常是"0"，为检验码，备用。
     * IMEI码具有唯一性，贴在手机背面的标志上，并且读写于手机内存中。它也是该手机在厂家的"档案"和"身份证号"。
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Activity i_Activity)
    {
        TelephonyManager v_TelephoneMgr = (TelephonyManager)i_Activity.getSystemService(Context.TELEPHONY_SERVICE);

        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                return v_TelephoneMgr.getImei();
            }
            else
            {
                return v_TelephoneMgr.getDeviceId();
            }
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }

        return "";
    }

}
