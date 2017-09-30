package org.hy.common.android;

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

    public static void disableShiftMode(BottomNavigationView view)
    {
        //获取子View BottomNavigationMenuView的对象
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try
        {
            //设置私有成员变量mShiftingMode可以修改
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++)
            {
                BottomNavigationItemView v_Item = (BottomNavigationItemView) menuView.getChildAt(i);
                // 去除shift效果
                v_Item.setShiftingMode(false);
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
