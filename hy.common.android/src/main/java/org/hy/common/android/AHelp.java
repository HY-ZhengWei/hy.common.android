package org.hy.common.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;

import java.io.File;


/**
 * Android 的辅助类
 *
 * @author  ZhengWei(HY)
 * @version 2013-11-15
 *          2016-01-15  建立独立项目，通用化此类
 */
public final class AHelp
{

    private AHelp()
    {
        // Nothing.
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
     * @param i_ImageNamePrefix  图片名称的前缀
     * @return  返回拍照保存的文件，但要等拍照完成时读取才是有效的
     */
    public static File photograph(Activity i_Activity ,int i_ResultCode ,String i_ImageNamePrefix)
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

                    Uri v_PhotoURI = FileProvider.getUriForFile(i_Activity ,"tempfiles.fileprovider" ,v_PhotoFile);
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
