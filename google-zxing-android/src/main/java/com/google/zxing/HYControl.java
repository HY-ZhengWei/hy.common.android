package com.google.zxing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;





/**
 * Created by ZhengWei(HY) on 2017/8/28.
 */
public class HYControl
{

    /* 取景的方向。true为竖屏; false为ZXing默认的横屏 */
    public  static boolean $Direction        = true;

    /* 扫码类型。true为二维码; false为条形码 */
    public  static boolean $QRCodeType       = true;

    /* 遮罩层的颜色 */
    public  static int     $MaskColor        = Color.parseColor("#33FFFFFF");



    /* 边框线的颜色 */
    public  static int     $BorderColor      = Color.WHITE;

    /* 边框线的大小 */
    public  static int     $BorderSize       = 1;



    /* 边框直角线的颜色 */
    public  static int     $CornerColor      = Color.RED;

    /* 边框直角线的大小 */
    public  static int     $CornerSize       = 3;

    /* 边框直角线的长度 */
    public  static int     $CornerLen        = 40;



    /* 扫描线的方向。true为上下扫描; false为左右扫描 */
    public  static boolean $ScanDirection    = true;

    /* 扫描线的颜色 */
    public  static  int    $ScanLineColor    = Color.RED;

    /* 扫描线的大小 */
    public  static  int    $ScanLineSize     = 1;

    /* 扫描线距离上下或者左右边框的间距，默认值为0dp */
    public  static int     $ScanLineMargin   = 0;

    private static float   $ScanLineTop      = 0;
    private static float   $ScanLineBottom   = 0;
    private static float   $ScanLineLeft     = 0;
    private static float   $ScanLineRight    = 0;



    /**
     * 画遮罩层
     */
    public static void drawMask(Canvas i_Canvas ,Paint io_Paint ,Rect i_Frame)
    {
        int width  = i_Canvas.getWidth();
        int height = i_Canvas.getHeight();

        if ( $MaskColor != Color.TRANSPARENT )
        {
            io_Paint.setStyle(Paint.Style.FILL);
            io_Paint.setColor($MaskColor);
            i_Canvas.drawRect(0, 0, width, i_Frame.top, io_Paint);
            i_Canvas.drawRect(0, i_Frame.top, i_Frame.left, i_Frame.bottom + 1, io_Paint);
            i_Canvas.drawRect(i_Frame.right + 1, i_Frame.top, width, i_Frame.bottom + 1, io_Paint);
            i_Canvas.drawRect(0, i_Frame.bottom + 1, width, height, io_Paint);
        }
    }



    /**
     * 画边框线
     */
    public static void drawBorderLine(Canvas i_Canvas ,Paint io_Paint ,Rect i_Frame)
    {
        if ( $BorderSize > 0 )
        {
            io_Paint.setStyle(Paint.Style.STROKE);
            io_Paint.setColor($BorderColor);
            io_Paint.setStrokeWidth($BorderSize);
            i_Canvas.drawRect(i_Frame, io_Paint);
        }
    }



    /**
     * 画四个直角的线
     */
    public static void drawCornerLine(Canvas i_Canvas ,Paint io_Paint ,Rect i_FrameRect)
    {
        if ( $CornerSize > 0 )
        {
            io_Paint.setStyle(Paint.Style.STROKE);
            io_Paint.setColor($CornerColor);
            io_Paint.setStrokeWidth($CornerSize * 2);
            i_Canvas.drawLine(i_FrameRect.left - $CornerSize, i_FrameRect.top , i_FrameRect.left - $CornerSize + $CornerLen, i_FrameRect.top, io_Paint);
            i_Canvas.drawLine(i_FrameRect.left, i_FrameRect.top - $CornerSize, i_FrameRect.left, i_FrameRect.top - $CornerSize + $CornerLen, io_Paint);
            i_Canvas.drawLine(i_FrameRect.right + $CornerSize, i_FrameRect.top, i_FrameRect.right + $CornerSize - $CornerLen, i_FrameRect.top, io_Paint);
            i_Canvas.drawLine(i_FrameRect.right, i_FrameRect.top - $CornerSize, i_FrameRect.right, i_FrameRect.top - $CornerSize + $CornerLen, io_Paint);

            i_Canvas.drawLine(i_FrameRect.left - $CornerSize, i_FrameRect.bottom, i_FrameRect.left - $CornerSize + $CornerLen, i_FrameRect.bottom, io_Paint);
            i_Canvas.drawLine(i_FrameRect.left, i_FrameRect.bottom + $CornerSize, i_FrameRect.left, i_FrameRect.bottom + $CornerSize - $CornerLen, io_Paint);
            i_Canvas.drawLine(i_FrameRect.right + $CornerSize, i_FrameRect.bottom, i_FrameRect.right + $CornerSize - $CornerLen, i_FrameRect.bottom, io_Paint);
            i_Canvas.drawLine(i_FrameRect.right, i_FrameRect.bottom + $CornerSize, i_FrameRect.right, i_FrameRect.bottom + $CornerSize - $CornerLen, io_Paint);
        }
    }



    /**
     * 画扫描线
     */
//    private void drawScanLine(Canvas i_Canvas ,Paint io_Paint ,Rect i_FrameRect)
//    {
//        float v_HalfCornerSize = $CornerSize / 2;
//
//        if ( $ScanDirection )
//        {
//            if (mGridScanLineBitmap != null)
//            {
//                RectF dstGridRectF = new RectF(i_FrameRect.left + v_HalfCornerSize + 0.5f
//                                              ,i_FrameRect.top + v_HalfCornerSize + $ScanLineMargin
//                                              ,$ScanLineRight
//                                              ,i_FrameRect.bottom - v_HalfCornerSize - $ScanLineMargin);
//
//                Rect srcGridRect = new Rect((int) (mGridScanLineBitmap.getWidth() - dstGridRectF.width())
//                                                  ,0
//                                                  ,mGridScanLineBitmap.getWidth()
//                                                  ,mGridScanLineBitmap.getHeight());
//
//                if (srcGridRect.left < 0)
//                {
//                    srcGridRect.left = 0;
//                    dstGridRectF.left = dstGridRectF.right - srcGridRect.width();
//                }
//
//                i_Canvas.drawBitmap(mGridScanLineBitmap, srcGridRect, dstGridRectF, io_Paint);
//            }
//            else if (mScanLineBitmap != null)
//            {
//                RectF lineRect = new RectF($ScanLineLeft, i_FrameRect.top + v_HalfCornerSize + $ScanLineMargin
//                                          ,$ScanLineLeft + mScanLineBitmap.getWidth()
//                                          ,i_FrameRect.bottom - v_HalfCornerSize - $ScanLineMargin);
//                i_Canvas.drawBitmap(mScanLineBitmap, null, lineRect, io_Paint);
//            }
//            else
//            {
//                io_Paint.setStyle(Paint.Style.FILL);
//                io_Paint.setColor($ScanLineColor);
//                i_Canvas.drawRect($ScanLineLeft
//                                 ,i_FrameRect.top + v_HalfCornerSize + $ScanLineMargin
//                                 ,$ScanLineLeft + $ScanLineSize
//                                 ,i_FrameRect.bottom - v_HalfCornerSize - $ScanLineMargin, io_Paint);
//            }
//        }
//        else
//        {
//            if (mGridScanLineBitmap != null)
//            {
//                RectF dstGridRectF = new RectF(i_FrameRect.left + v_HalfCornerSize + $ScanLineMargin
//                                              ,i_FrameRect.top + v_HalfCornerSize + 0.5f
//                                              ,i_FrameRect.right - v_HalfCornerSize - $ScanLineMargin, $ScanLineBottom);
//
//                Rect srcRect = new Rect(0, (int) (mGridScanLineBitmap.getHeight() - dstGridRectF.height())
//                                                 ,mGridScanLineBitmap.getWidth()
//                                                 ,mGridScanLineBitmap.getHeight());
//
//                if (srcRect.top < 0) {
//                    srcRect.top = 0;
//                    dstGridRectF.top = dstGridRectF.bottom - srcRect.height();
//                }
//
//                i_Canvas.drawBitmap(mGridScanLineBitmap, srcRect, dstGridRectF, io_Paint);
//            }
//            else if (mScanLineBitmap != null)
//            {
//                RectF lineRect = new RectF(i_FrameRect.left + v_HalfCornerSize + $ScanLineMargin, $ScanLineTop
//                                          ,i_FrameRect.right - v_HalfCornerSize - $ScanLineMargin
//                                          ,$ScanLineTop + mScanLineBitmap.getHeight());
//                i_Canvas.drawBitmap(mScanLineBitmap, null, lineRect, io_Paint);
//            }
//            else
//            {
//                io_Paint.setStyle(Paint.Style.FILL);
//                io_Paint.setColor($ScanLineColor);
//                i_Canvas.drawRect(i_FrameRect.left + v_HalfCornerSize + $ScanLineMargin
//                                 ,$ScanLineTop, i_FrameRect.right - v_HalfCornerSize - $ScanLineMargin
//                                 ,$ScanLineTop + $ScanLineSize, io_Paint);
//            }
//        }
//    }

}
