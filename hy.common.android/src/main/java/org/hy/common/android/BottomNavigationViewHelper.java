package org.hy.common.android;

import android.annotation.SuppressLint;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by ZhengWei(HY) on 2017/9/19.
 */
public class BottomNavigationViewHelper
{

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view)
    {
        //获取子View BottomNavigationMenuView的对象
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try
        {
            //设置私有成员变量mShiftingMode可以修改
            // 2019-02-02  老版本的是：menuView.getClass().getDeclaredField("mShiftingMode");
            Field shiftingMode = menuView.getClass().getDeclaredField("labelVisibilityMode");
            shiftingMode.setAccessible(true);
            // 2019-02-02  老版本的是：shiftingMode.setBoolean(menuView, false);
            shiftingMode.setInt(menuView, 1);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++)
            {
                BottomNavigationItemView v_Item = (BottomNavigationItemView) menuView.getChildAt(i);

                // 去除shift效果
                // 2019-02-02  老版本是：v_Item.setShiftingMode(false);
                v_Item.setShifting(false);

                v_Item.setChecked(v_Item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e)
        {
            Log.e("BNavigationViewHelper", "没有mShiftingMode这个成员变量", e);
        }
        catch (IllegalAccessException e)
        {
            Log.e("BNavigationViewHelper", "无法修改mShiftingMode的值", e);
        }
    }

}