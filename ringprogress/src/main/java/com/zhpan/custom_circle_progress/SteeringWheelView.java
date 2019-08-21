package com.zhpan.custom_circle_progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SteeringWheelView extends View {

    private int circleWidth = 100;
    private int circleColor = Color.argb(150, 255, 0, 0);
    private int innerCircleColor = Color.rgb(0, 150, 0);
    private int backgroundColor = Color.rgb(255, 255, 255);
    private Paint paint = new Paint();
    int center = 0;
    int innerRadius = 0;
    private float innerCircleRadius = 0;
    public Dir dir = Dir.UP;

    public SteeringWheelView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SteeringWheelView(Context context) {
        this(context,null);

        // paint = new Paint();
    }

    public SteeringWheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);

        center = getMeasuredWidth() / 2;
        innerRadius = (center - circleWidth / 2 - 10);// Բ��
        innerCircleRadius = center / 3;
        this.setOnTouchListener(onTouchListener);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 0;

        if (specMode == MeasureSpec.AT_MOST) {
            result = getWidth();
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 0;

        if (specMode == MeasureSpec.AT_MOST) {

            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initBackGround(canvas);
        drawDirTriangle(canvas, dir);

    }

    private void drawDirTriangle(Canvas canvas, Dir dir) {
        paint.setColor(innerCircleColor);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);

        switch (dir) {
        case UP:
            drawUpTriangle(canvas);
            break;
        case DOWN:
            drawDownTriangle(canvas);
            break;
        case LEFT:
            drawLeftTriangle(canvas);
            break;
        case RIGHT:
            drawRightTriangle(canvas);
            break;
        case CENTER:
            invalidate();
            break;
        default:
            break;
        }

        paint.setColor(backgroundColor);

        float smallCircle = 10;
        canvas.drawCircle(center, center, smallCircle, paint);
        // canvas.drawText(text, center, center+40, paint);

    }

    private void drawRightTriangle(Canvas canvas) {
        Path path = new Path();
        path.moveTo(center, center);
        double sqrt2 = innerCircleRadius / Math.sqrt(2);
        double pow05 = innerCircleRadius * Math.sqrt(2);
        path.lineTo((float) (center + sqrt2), (float) (center - sqrt2));
        path.lineTo((float) (center + pow05), center);
        path.lineTo((float) (center + sqrt2), (float) (center + sqrt2));
        canvas.drawPath(path, paint);
        paint.setColor(backgroundColor);
        canvas.drawLine(center, center, center + innerCircleRadius, center, paint);

        drawOnclikColor(canvas, Dir.RIGHT);
    }

    private void drawLeftTriangle(Canvas canvas) {
        Path path = new Path();
        path.moveTo(center, center);
        double sqrt2 = innerCircleRadius / Math.sqrt(2);
        double pow05 = innerCircleRadius * Math.sqrt(2);
        path.lineTo((float) (center - sqrt2), (float) (center - sqrt2));
        path.lineTo((float) (center - pow05), center);
        path.lineTo((float) (center - sqrt2), (float) (center + sqrt2));
        canvas.drawPath(path, paint);

        paint.setColor(backgroundColor);
        canvas.drawLine(center, center, center - innerCircleRadius, center, paint);

        drawOnclikColor(canvas, Dir.LEFT);

    }

    private void drawDownTriangle(Canvas canvas) {
        Path path = new Path();
        path.moveTo(center, center);
        double sqrt2 = innerCircleRadius / Math.sqrt(2);
        double pow05 = innerCircleRadius * Math.sqrt(2);
        path.lineTo((float) (center - sqrt2), (float) (center + sqrt2));
        path.lineTo(center, (float) (center + pow05));
        path.lineTo((float) (center + sqrt2), (float) (center + sqrt2));
        canvas.drawPath(path, paint);

        paint.setColor(backgroundColor);
        canvas.drawLine(center, center, center, center + innerCircleRadius, paint);

        drawOnclikColor(canvas, Dir.DOWN);
    }

    private void drawOnclikColor(Canvas canvas, Dir dir) {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(100);
        switch (dir) {
        case UP:
            canvas.drawArc(new RectF(center - innerRadius, center - innerRadius, center + innerRadius, center
                    + innerRadius), 225, 90, false, paint);
            break;
        case DOWN:
            canvas.drawArc(new RectF(center - innerRadius, center - innerRadius, center + innerRadius, center
                    + innerRadius), 45, 90, false, paint);
            break;
        case LEFT:
            canvas.drawArc(new RectF(center - innerRadius, center - innerRadius, center + innerRadius, center
                    + innerRadius), 135, 90, false, paint);
            break;
        case RIGHT:
            canvas.drawArc(new RectF(center - innerRadius, center - innerRadius, center + innerRadius, center
                    + innerRadius), -45, 90, false, paint);
            break;

        default:
            break;
        }

        paint.setStyle(Paint.Style.FILL);
    }

    private void drawUpTriangle(Canvas canvas) {
        Path path = new Path();
        path.moveTo(center, center);
        double sqrt2 = innerCircleRadius / Math.sqrt(2);
        double pow05 = innerCircleRadius * Math.sqrt(2);

        path.lineTo((float) (center - sqrt2), (float) (center - sqrt2));
        path.lineTo(center, (float) (center - pow05));
        path.lineTo((float) (center + sqrt2), (float) (center - sqrt2));
        canvas.drawPath(path, paint);

        paint.setColor(backgroundColor);
        canvas.drawLine(center, center, center, center - innerCircleRadius, paint);

        drawOnclikColor(canvas, Dir.UP);
    }

    private void initBackGround(Canvas canvas) {
        clearCanvas(canvas);
        drawBackCircle(canvas);
        drawInnerCircle(canvas);

    }
    private void drawInnerCircle(Canvas canvas) {
        paint.setColor(innerCircleColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        canvas.drawCircle(center, center, innerCircleRadius, paint);
    }

    private void drawBackCircle(Canvas canvas) {
        paint.setColor(circleColor);
        paint.setStrokeWidth(circleWidth);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(center, center, innerRadius, paint); // ����ԲȦ

        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4);
        canvas.drawLine(center, center, 0, 0, paint);
        canvas.drawLine(center, center, center * 2, 0, paint);
        canvas.drawLine(center, center, 0, center * 2, paint);
        canvas.drawLine(center, center, center * 2, center * 2, paint);

    }

    private void clearCanvas(Canvas canvas) {
        canvas.drawColor(backgroundColor);
    }

    OnTouchListener onTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            Dir tmp = Dir.UNDEFINE;
            if ((tmp = checkDir(event.getX(), event.getY())) != Dir.UNDEFINE) {
                dir = tmp;
                invalidate();
            }
            return true;
        }

        private Dir checkDir(float x, float y) {
            Dir dir = Dir.UNDEFINE;

            if (Math.sqrt(Math.pow(y - center, 2) + Math.pow(x - center, 2)) < innerCircleRadius) {
                dir = Dir.CENTER;
            } else if (y < x && y + x < 2 * center) {
                dir = Dir.UP;
            } else if (y < x && y + x > 2 * center) {
                dir = Dir.RIGHT;
            } else if (y > x && y + x < 2 * center) {
                dir = Dir.LEFT;
            } else if (y > x && y + x > 2 * center) {
                dir = Dir.DOWN;
            }

            return dir;
        }

    };

    public enum Dir {
        UP, DOWN, LEFT, RIGHT, CENTER, UNDEFINE
    }

}
