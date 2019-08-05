package com.cypoem.wheel_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.zhpan.library.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhpan on 2017/6/7.
 * Description:
 */

public class WheelView extends View {
    //  文字大小
    private float mTextSize;
    //  文字颜色
    private int mTextColor;
    //  文字行间距
    private int mLinePadding;
    //  文字最大放大比例，默认2.0
    private float mTextMaxScale;
    //  文字最小alpha值，默认0.4f
    private float mTextMinAlpha;
    //  是否循环
    private boolean isCircle;
    //  数据集合
    private List<String> dataList = new ArrayList<>();
    //  x坐标
    private int xOri;
    //  y坐标
    private int yOri;
    //  文字最大宽度
    private float maxTextWidth;
    //  文字高度
    private int textHeight;
    //  实际内容宽度
    private int contentWidth;
    //  实际内容高度
    private int contentHeight;
    //  按下时的y坐标
    private float downY;
    private float downX;
    //  本次滑动Y坐标的偏移量
    private float offsetY;
    //  fling之前的offsetY
    private float oldOffsetY;
    //  当前选中项
    private int curIndex;
    private int offsetIndex;
    //  回弹距离
    private float bounceDistance;
    //  是否处于滑动状态
    private boolean isSliding;
    // 正常状态下最多显示几个文字，默认3（偶数时，边缘的文字会截断）
    private int mMaxShowNum;
    private String label="个";

    private TextPaint mTextPaint;
    private Paint.FontMetrics mFontMetrics;
    private Scroller mScroller;
    private int mMinVelocity;
    private int mMaxVelocity;
    private int mScaledTouchSlop;
    private VelocityTracker mVelocityTracker;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
        mTextSize = typedArray.getDimension(R.styleable.WheelView_textSize, 32);
        mTextColor = typedArray.getColor(R.styleable.WheelView_textColor, Color.parseColor("#CCCCCC"));
        mLinePadding = (int) typedArray.getDimension(R.styleable.WheelView_linePadding, DensityUtils.dp2px(context, 20));
        mTextMaxScale = typedArray.getFloat(R.styleable.WheelView_textMaxScale, 2.0f);
        mTextMinAlpha = typedArray.getFloat(R.styleable.WheelView_textMinAlpha, 0.4f);
        isCircle = typedArray.getBoolean(R.styleable.WheelView_recycleMode, false);
        mMaxShowNum =typedArray.getInt(R.styleable.WheelView_maxShowNum,3);
        //label=typedArray.getString(R.styleable.WheelView_label);
        typedArray.recycle();

        init(context);

    }

    private void init(Context context) {
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mFontMetrics = mTextPaint.getFontMetrics();

        mScroller = new Scroller(context);
        //  得到滑动最小速度，以像素/每秒来进行计算
        mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        //  得到滑动的最大速度
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        //  滑动的最小距离，只有大于这个距离才被视为滑动
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        contentWidth = (int) (maxTextWidth + getPaddingLeft() + getPaddingRight());
        if (mode != MeasureSpec.EXACTLY) {  //    wrap_content
            width = contentWidth;
        }
        mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        textHeight = (int) (mFontMetrics.bottom - mFontMetrics.top);
        contentHeight=textHeight*mMaxShowNum+mLinePadding*mMaxShowNum;
        if (mode != MeasureSpec.EXACTLY) {  //  wrap_content
            height = contentHeight + getPaddingTop() + getPaddingBottom();
        }
        xOri = width / 2;
        yOri = height / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        addVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                    finishScroll();
                }
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetY = event.getY() - downY;
                if (isSliding || Math.abs(offsetY) > mScaledTouchSlop) {
                    isSliding = true;
                    reDraw();
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollYVelocity = 2 * getScrollYVelocity() / 3;
                if (Math.abs(scrollYVelocity) > mMinVelocity) {
                    oldOffsetY = offsetY;
                    mScroller.fling(0, 0, 0, scrollYVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                    invalidate();
                } else {
                    finishScroll();
                }
                //  没有滑动
                if(!isSliding){
                    if(downY<contentHeight/3){
                        moveBy(-1);
                    }else if(downY>2*contentHeight/3){
                        moveBy(1);
                    }
                }
                isSliding=false;
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    private void recycleVelocityTracker() {
        if(mVelocityTracker!=null){
            mVelocityTracker.recycle();
            mVelocityTracker=null;
        }
    }

    private void moveBy(int offsetIndex) {
        moveTo(getNowIndex(offsetIndex));
    }
    //  滑动到指定位置
    private void moveTo(int index) {
        if(index<0||index>dataList.size()||curIndex==index){
            return;
        }
        if(!mScroller.isFinished()){
            mScroller.forceFinished(true);
        }
        finishScroll();
        int dy=0;
        int centerPadding=textHeight+mLinePadding;
        if(!isCircle){
            dy=(curIndex-index)*centerPadding;
        }else {
            int offsetIndex=curIndex-index;
            int d1= Math.abs(offsetIndex)*centerPadding;
            int d2=(dataList.size()- Math.abs(offsetIndex)*centerPadding);

            if(offsetIndex>0){
                if(d1<d2){
                    dy=d1;
                }else {
                    dy=-d2;
                }
            }else {
                if(d1<d2){
                    dy=-d1;
                }else {
                    dy=d2;
                }
            }
        }
        mScroller.startScroll(0,0,0,dy,500);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != dataList && dataList.size() > 0) {
            canvas.clipRect(
                    xOri - contentWidth ,
                    yOri - contentHeight / 2,
                    xOri + contentWidth ,
                    yOri + contentHeight / 2
            );

            drawText(canvas);
            drawLabel(canvas);
        }
    }

    private void drawLabel(Canvas canvas) {

        canvas.drawText(label,xOri+40,yOri,mTextPaint);
    }

    private void drawText(Canvas canvas) {
        //  绘制文字
        int listSize=dataList.size();
        int centerPadding=textHeight+mLinePadding;
        int half=mMaxShowNum/2+1;
        for(int i=-half;i<half;i++){
            int index=curIndex-offsetIndex+i;
            if(isCircle){
                if(index<0){
                    index=(index+1)%dataList.size()+dataList.size()-1;
                }else if(index>dataList.size()-1){
                    index=index%dataList.size();
                }
            }
            if(index>=0&&index<listSize){
                //  计算每个字的中间y坐标
                int tempY=yOri+i*centerPadding;
                tempY+=offsetY%centerPadding;
                //  根据每个字中间y坐标到xOri的距离，计算scale值
                float scale=1.0f-(1.0f* Math.abs(tempY-yOri)/centerPadding);
                //  根据textMaxScale计算tempScale值，即实际text应该放大的倍数，范围1-textMaxScale
                float tempScale=scale*(mTextMaxScale-1.0f)+1.0f;
                tempScale=tempScale<1.0f?1.0f:tempScale;
                mTextPaint.setTextSize(mTextSize*tempScale);
                mTextPaint.setAlpha((int)(255*mTextMinAlpha*tempScale));

                //  绘制
                Paint.FontMetrics tempFm=mTextPaint.getFontMetrics();
                String text=dataList.get(index);
                float textWidth=mTextPaint.measureText(text);
                canvas.drawText(text,xOri-textWidth/2,tempY-(tempFm.ascent+tempFm.descent)/2,mTextPaint);

            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            offsetY=oldOffsetY+mScroller.getCurrY();
            if(!mScroller.isFinished()){
                reDraw();
            }else {
                finishScroll();
            }
        }
    }

    private int getScrollYVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return velocity;
    }

    private void reDraw() {
        //  curIndex需要偏移量
        int i = (int) (offsetY / (textHeight + mLinePadding));
        if (isCircle || (curIndex - i >= 0 && curIndex - i < dataList.size())) {
            offsetIndex = i;
            if (null != onScrollChangedListener) {
                onScrollChangedListener.onScrollChanged(getNowIndex(-offsetIndex));
            }
            postInvalidate();
        } else {
            finishScroll();
        }
    }

    private void finishScroll() {
        int centerPadding = textHeight + mLinePadding;
        float v = offsetY % centerPadding;
        if (v > 0.5f * centerPadding) {
            ++offsetIndex;
        } else if (v < -0.5f * centerPadding) {
            --offsetIndex;
        }

        //  重置curIndex
        curIndex = getNowIndex(-offsetIndex);
        //  计算回弹距离
        bounceDistance = offsetIndex * centerPadding - offsetY;
        offsetY += bounceDistance;

        //  更新
        if (null != onScrollChangedListener) {
            onScrollChangedListener.onScrollFinished(curIndex);
        }
        //  重绘
        reset();
        postInvalidate();

    }

    private void reset() {
        offsetY = 0;
        oldOffsetY = 0;
        offsetIndex = 0;
        bounceDistance = 0;
    }

    private int getNowIndex(int offsetIndex) {
        int index = curIndex + offsetIndex;
        if (isCircle) {
            if (index < 0) {
                index = (index + 1) % dataList.size() + dataList.size() - 1;
            } else if (index > dataList.size() - 1) {
                index = index % dataList.size();
            }
        } else {
            if (index < 0) {
                index = 0;
            } else if (index > dataList.size() - 1) {
                index = dataList.size() - 1;
            }
        }
        return index;
    }

    private void addVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 滚动发生变化时的回调接口
     */
    public interface OnScrollChangedListener {
        public void onScrollChanged(int curIndex);

        public void onScrollFinished(int curIndex);
    }

    private OnScrollChangedListener onScrollChangedListener;

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    public void setDataList(List<String> dataList){
        this.dataList.clear();
        this.dataList.addAll(dataList);

        //  更新maxTextWidth
        if(null!=dataList&&dataList.size()>0){
            int size=dataList.size();
            for(int i=0;i<size;i++){

                float tempWidth=mTextPaint.measureText(dataList.get(i));
                if(tempWidth>maxTextWidth){
                    maxTextWidth=tempWidth;
                }
                curIndex=0;
            }
        }
        requestLayout();
    }

    /**
     *  获取当前选中的位置
     */
    public int getCurIndex(){
        return curIndex-offsetIndex;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
    }
}
