package com.example.administrator.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class WuziqiPanel extends View {

    private int mPaintWidth;
    private float mLineHeigt;
    // 棋盘最大行数
    private int MAX_LINE = 10;
    // 是否五子连珠的计算范围
    private int MAX_COUNT_IN_LINE = 5;

    // 绘制棋盘里面的画笔
    private Paint mPaint = new Paint();

    // 棋子
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;

    // 点击时候获取白子跟黑子的集合
    private List<Point> mWhiteArray = new ArrayList<>();
    private List<Point> mBlickArray = new ArrayList<>();

    // 声明一个表示当前棋子的变量
    private boolean mIsWhite = true;

    // 游戏结束的全局变量
    private boolean mIsGameOver;

    // 判断白子是否为赢家（否则黑子赢家）
    private boolean mWhiteWinner;


    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44ff0000);
        init();
    }

    // 测量棋盘大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heigtSize = MeasureSpec.getSize(heightMeasureSpec);
        int heigtMode = MeasureSpec.getMode(heightMeasureSpec);

        //  选出着宽跟高里面最小的一个
        int width = Math.min(widthSize, heigtSize);

        if (widthMode == MeasureSpec.UNSPECIFIED) {

            width = heigtSize;

        } else if (heigtMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);

    }

    // 尺寸相关变量赋值
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        /**
         *
         * mPaintWidth == 总宽
         * mPaintWidth * 1.0f  将其转换成浮点类型数据
         * 转换之后除最大行数计算出没一行的高度
         *
         * */
        mPaintWidth = w;
        mLineHeigt = mPaintWidth * 1.0f / MAX_LINE;

        // 棋子的宽高
        int pieceWidth = (int) (mLineHeigt * ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
    }


    // 画笔属性，棋子属性
    private void init() {
        mPaint.setColor(0x88000000);  // 颜色
        mPaint.setAntiAlias(true);  // 抗锯齿
        mPaint.setDither(true);     // 防抖动（画笔比较柔和）
        mPaint.setStyle(Paint.Style.STROKE);  // 画笔属性

        // 棋子图片绑定
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);


    }

    // 上面得到宽高设置好画笔之后开始绘制棋盘操作
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制棋盘
        drawBoard(canvas);

        // 点击绘制棋子
        drawPieces(canvas);
        //  绘制完成判断是否结束
        checkGameOver();

    }


    // 绘制棋子的方法
    private void drawPieces(Canvas canvas) {

        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {

            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeigt,
                    (whitePoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeigt, null);

        }

        for (int i = 0, n = mBlickArray.size(); i < n; i++) {

            Point blickPoint = mBlickArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blickPoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeigt,
                    (blickPoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeigt, null);

        }
    }

    // 判断游戏赢家
    private void checkGameOver() {

        boolean whitewin = checkFiveInLine(mWhiteArray);
        boolean blickwin = checkFiveInLine(mBlickArray);

        if (whitewin || blickwin) {
            mIsGameOver = true;
            mWhiteWinner = whitewin;

            String text = mWhiteWinner ? "白子赢了" : "黑子赢了";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    // 是否五子连珠
    private boolean checkFiveInLine(List<Point> Points) {

        for (Point p : Points) {
            int x = p.x;
            int y = p.y;

            // 横向胜利
            boolean win = checkHorizontal(x, y, Points);


        }

        return false;
    }

    // 横向胜利逻辑
    private boolean checkHorizontal(int x, int y, List<Point> points) {

        int count = 1;

        // 左边
        for (int i = 0; i < MAX_COUNT_IN_LINE; i++) {
            // x 轴减1 之后是否还有同类棋子没有就break
            if (points.contains(new Point(x - 1, y))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) return true;

        // 右边
        for (int i = 0; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + 1, y))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) return true;

        return false;
    }

    // 纵向胜利逻辑
    private boolean checkVetical(int x, int y, List<Point> points) {

        int count = 1;

        // 上边
        for (int i = 0; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) return true;

        // 下边
        for (int i = 0; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) return true;

        return false;
    }

    // 左斜胜利逻辑
    private boolean checkVetical(int x, int y, List<Point> points) {

        int count = 1;

        // 上边
        for (int i = 0; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) return true;

        // 下边
        for (int i = 0; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) return true;

        return false;
    }


    // 右斜胜利逻辑
    private boolean checkVetical(int x, int y, List<Point> points) {

        int count = 1;

        // 上边
        for (int i = 0; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) return true;

        // 下边
        for (int i = 0; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_COUNT_IN_LINE) return true;

        return false;
    }


    // 绘制棋盘的方法,从每一行每一列中心画直线
    private void drawBoard(Canvas canvas) {

        int w = mPaintWidth;
        float lineHeight = mLineHeigt;

        for (int i = 0; i < MAX_LINE; i++) {

            /**
             * 绘制棋盘主要是确定X轴的中心 与Y轴的中心来画直线
             *
             * X轴的开始的位置为行高的二分之一 即每一行的中心
             * X轴结束的位置为总宽减去行高的二分之一即最后一列的中心（减去的为最右边的半格）
             *
             * */
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }


    //  捕获用户手势
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 如果游戏结束停止捕获
        if (mIsGameOver) {
            return false;
        }


        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x, y);


            // 判断点是否有棋子 （其中是否有值与集合中的值相等）
            if (mWhiteArray.contains(p) || mBlickArray.contains(p)) {
                return false;
            }


            if (mIsWhite) {
                mWhiteArray.add(p);
            } else {
                mBlickArray.add(p);
            }
            invalidate();
            mIsWhite = !mIsWhite;
            return true;
        }
        return true;
    }

    // 合法的点（Point）
    private Point getValidPoint(int x, int y) {


        return new Point((int) (x / mLineHeigt), (int) (y / mLineHeigt));
    }
}










