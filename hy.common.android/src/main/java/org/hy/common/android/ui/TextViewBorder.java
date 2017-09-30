package org.hy.common.android.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hy.common.android.R;

/**
 * 实现方法是两个TextView叠加,只有描边的TextView为底,实体TextView叠加在上面
 * 看上去文字就有个不同颜色的边框了
 *
 * Created by ZhengWei(HY) on 2017/9/8.
 */
public class TextViewBorder extends android.support.v7.widget.AppCompatTextView
{
    /** 描边的TextView */
    private TextView borderTextView;

    /** 是否启用描边。默认为：false，为启用描边 */
    private boolean  isBorder    = false;

    /** 描边的颜色 */
    private int      borderColor = Color.BLACK;

    /** 描边的大小 */
    private int      borderSize  = 2;



    public TextViewBorder(Context context)
    {
        super(context);
        init(context ,null ,null);
    }



    public TextViewBorder(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init(context ,attrs ,null);
    }



    public TextViewBorder(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context ,attrs ,defStyle);
    }



    public void init(Context context, AttributeSet attrs ,Integer defStyle)
    {
        TypedArray v_TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewBorder);

        this.isBorder    = v_TypedArray.getBoolean(R.styleable.TextViewBorder_isBorder    ,false);
        this.borderSize  = v_TypedArray.getInt(    R.styleable.TextViewBorder_borderSize  ,2);
        this.borderColor = v_TypedArray.getColor(  R.styleable.TextViewBorder_borderColor ,Color.BLACK);

        v_TypedArray.recycle();

        if ( isBorder )
        {
            if ( attrs == null )
            {
                borderTextView = new TextView(context);
            }
            else
            {
                if ( defStyle ==null )
                {
                    borderTextView = new TextView(context, attrs);
                }
                else
                {
                    borderTextView = new TextView(context, attrs ,defStyle);
                }
            }

            TextPaint v_TextPaint = borderTextView.getPaint();
            v_TextPaint.setStrokeWidth(this.borderSize);            // 设置描边宽度
            v_TextPaint.setStyle(Paint.Style.STROKE);               // 对文字只描边
            borderTextView.setTextColor(this.borderColor);  // 设置描边颜色
            borderTextView.setGravity(getGravity());
        }
    }



    @Override
    public void setLayoutParams (ViewGroup.LayoutParams params)
    {
        super.setLayoutParams(params);

        if ( isBorder )
        {
            borderTextView.setLayoutParams(params);
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if ( isBorder )
        {
            CharSequence v_Text = borderTextView.getText();

            // 两个TextView上的文字必须一致
            if ( v_Text == null || !v_Text.equals(this.getText()) )
            {
                borderTextView.setText(getText());
                borderTextView.postInvalidate();
            }

            borderTextView.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }



    protected void onLayout (boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        if ( isBorder )
        {
            borderTextView.layout(left, top, right, bottom);
        }
    }



    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if ( isBorder )
        {
            borderTextView.draw(canvas);
        }
    }

}
