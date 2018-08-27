package com.zhpan.custom_pie_chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zhpan.library.DensityUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhpan on 2017/3/16.
 */

public class PieChartView extends View {
    private Paint mPaintText;   //  文字画笔
    private Paint mPaintPie;   //  画圆画笔
    private Paint mPaintArc;    //  圆角矩形画笔
    private Paint mPaintRing;    //  扇形圆环的画笔
    private Paint mPaintLine;   //  扇形透明圆环画笔

    private float mTextSize;    //  字体大小
    private int mTextColor;   //  字体颜色
    private float mStrokeWidth; //  扇形圆环宽度

    //  控件宽高
    private int mWidth;
    private int mHeight;

    //  圆心坐标
    private float mPieCenterX;
    private float mPieCenterY;
    //  三个圆的半径
    private float mPieRadius1;
    private float mPieRadius2;
    private float mPieRadius3;
    //  10dp的间距
    private int mMartin10;

    private RectF pieOval;

    private String text="资产分配";
    private float mTextLength;

    private List<PieItemBean> items;
    private float totalValue=1;
    private Context mContext;

    public float getTextSize() {
        return mTextSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setTextSize(float textSize) {
        mTextSize = DensityUtils.dp2px(mContext,textSize);
        //mTextSize = textSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<PieItemBean> getItems() {
        return items;
    }

    public void setItems(List<PieItemBean> items) {
        this.items = items;
        totalValue = 0;
        for (PieItemBean item : items) {
            totalValue += item.getValue();
        }
        //  重绘View
        invalidate();
    }

    public PieChartView(Context context) {
        super(context);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }




    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChartView);
        mTextSize = typedArray.getDimension(R.styleable.PieChartView_textSize, 32);
        mTextColor=typedArray.getColor(R.styleable.PieChartView_textColor, Color.parseColor("#333333"));
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        mContext=context;
        mPaintText = new Paint();
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setAntiAlias(true);

        mPaintPie = new Paint();
        mPaintPie.setStyle(Paint.Style.FILL);
        mPaintPie.setAntiAlias(true);

        mPaintArc = new Paint();
        mPaintArc.setStyle(Paint.Style.FILL);
        mPaintArc.setAntiAlias(true);
        mPaintArc.setStrokeWidth(mStrokeWidth);

        mPaintRing = new Paint();
        mPaintRing.setAntiAlias(true);
        mPaintRing.setStyle(Paint.Style.STROKE);

        mPaintLine=new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.STROKE);

        mMartin10 = DensityUtils.dp2px(context, 10);
        pieOval = new RectF();
        items = new ArrayList<>();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getWidth();
            mHeight = getHeight();

            //  计算圆心和半径
            mPieCenterX = mWidth / 3;
            mPieCenterY = mHeight / 2;
            mPieRadius1 = mWidth / 5 + 15;
            mPieRadius2 = mPieRadius1 / 3 * 2;
            mPieRadius3 = mPieRadius1 / 3 * 2 - mPieRadius2 / 10;
            mStrokeWidth=mPieRadius1-mPieRadius3;

            pieOval.left=mPieCenterX - mPieRadius1+2*mMartin10;
            pieOval.top=mPieCenterY - mPieRadius1+2*mMartin10;
            pieOval.right= mPieCenterX + mPieRadius1-2*mMartin10;
            pieOval.bottom=mPieCenterY + mPieRadius1-2*mMartin10;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setTextStyle();
        drawCircle(canvas);
        drawArc(canvas);
        drawText(canvas);
    }

    private void setTextStyle() {
        //  控制TextSize在32到50之间
        mTextSize=mTextSize>50? 50:mTextSize;
        mTextSize=mTextSize<32?32:mTextSize;
        mPaintText.setColor(mTextColor);
        mPaintText.setTextSize(mTextSize);
    }

    //  扇形图中间的文字
    private void drawText(Canvas canvas) {
        if(items.size()<=0||items==null){
            text="暂无数据";
        }
        mTextLength = mPaintText.measureText(text);
        //  画扇形图中间文字的文字
        float margin = (2 * mPieRadius3 - mTextLength) / 2;//   计算文字左右间距
        canvas.drawText(text, mPieCenterX - mPieRadius3 + margin, mPieCenterY + 10, mPaintText);
    }

    //  画扇形圆环和百分比
    private void drawArc(Canvas canvas) {

        if(items!=null&&items.size()>0){
            //  计算类型名称所占高度
            Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
            double typeHeight = Math.ceil(fontMetrics.descent - fontMetrics.ascent);

            //  计算类型百分比的Y轴初始坐标
            double textY= mPieCenterY-(items.size()*typeHeight+(items.size()-mMartin10)+typeHeight)/2;

            float startAngle=0; //  扇环起始角度
            mPaintRing.setStrokeWidth(mStrokeWidth);    //  设置扇环宽度

            for(int i=0;i<items.size();i++){
                mPaintArc.setColor(items.get(i).getColor());
                mPaintRing.setColor(items.get(i).getColor());
                float sweep= (float) (items.get(i).getValue()/totalValue*360);
                //  画扇形圆环
                canvas.drawArc(pieOval, startAngle, sweep+1, false, mPaintRing);
                startAngle+=(float) (items.get(i).getValue()/totalValue*360);

                //  画文字
                String itemTypeText = items.get(i).getType()+":";
                //  计算百分比并保留两位小数
                double percent=(items.get(i).getValue()/totalValue)*100;
                DecimalFormat df = new DecimalFormat("#.00");
                String percentStr = df.format(percent)+"%";

                float left=mPieCenterX+mPieRadius1+3*mMartin10;
                float top= (float)(textY+i*(typeHeight+mMartin10)-mMartin10);
                float right=(float)(mPieCenterX+mPieRadius1+3*mMartin10+1.2*mMartin10);
                float bottom=(float)(textY+i*(typeHeight+mMartin10)+0.2*mMartin10);
                RectF rectF=new RectF(left,top,right,bottom);
                canvas.drawRoundRect(rectF,5,5, mPaintArc);
                canvas.drawText(itemTypeText+"   "+percentStr,left+2*mMartin10,(float)(textY+i*(typeHeight+mMartin10)),mPaintText);
            }
        }
    }

    private void drawCircle(Canvas canvas) {
        // 画最外层圆
        mPaintPie.setColor(Color.parseColor("#f1f1f1"));
        canvas.drawCircle(mPieCenterX, mPieCenterY, mPieRadius1, mPaintPie);
        // 画中间圆
        mPaintPie.setColor(Color.parseColor("#4C7F7F7F"));
        canvas.drawCircle(mPieCenterX, mPieCenterY, mPieRadius2, mPaintPie);
        // 画最内层圆
        mPaintPie.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawCircle(mPieCenterX, mPieCenterY, mPieRadius3, mPaintPie);
    }

    //  数据实体类
    public static class PieItemBean {

        private String type;
        private double value;
        private int color;

        public PieItemBean(String type, int color, double value) {
            this.type = type;
            this.color = color;
            this.value=value;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }


        public double getValue() {
            return value;
        }

        public void setValue(double degreeValue) {
            this.value = degreeValue;
        }

        public String getType() {
            return type;
        }

        public void setType(String itemType) {
            this.type = itemType;
        }

    }
}
