package com.example.eagleweb.combobutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @创建者 帅子
 * @创建时间 17/10/25.
 * @描述
 */

public class ComboTextView extends RelativeLayout {

    private Handler     mHandler;
    private ProgressBar mCountdown;
    private TextView    mTvTime;
    private int         ms;
    private Timer       mTimer;
    private int progress = 60;   // 进度
    private ComboListener mComboListener;
    private boolean canCombo = true;
    private boolean canClick = true;   // 是否可点击
    private long    downMil;   // 按下的时间
    private long    upMil; // 抬起的时间
    private boolean isCombo;    // 是否是连击的
    private int comboCount = 0; // 连击次数

    public ComboTextView(Context context) {
        this(context, null);
    }

    public ComboTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ComboTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHandler = new Handler();
        ms = 3000 / 60;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_combo_layout, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    float scaleValue = 0.9f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canClick) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downMil = System.currentTimeMillis();
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, scaleValue, 1, scaleValue, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(100);
                scaleAnimation.setFillAfter(true);
                startAnimation(scaleAnimation);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                upMil = System.currentTimeMillis();
                isCombo = false;    // 初始化是否连击
                if (upMil - downMil < 400) {
                    // 算是点击事件
                    if (canCombo) {
                        // 整个View变小
                        if (mTimer == null) {
                            mTimer = new Timer();
                        } else {
                            try {
                                mTimer.cancel();
                                mTimer = null;
                                mTimer = new Timer();
                            } catch (Exception e) {
                                e.printStackTrace();
                                mTimer = null;
                                mTimer = new Timer();
                            }
                        }
                        if (progress > 0 && progress < 60) {
                            // 是连击
                            isCombo = true;
                            comboCount++;
                        } else {
                            comboCount = 0;
                            isCombo = false;
                        }

                        progress = 60;
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                SystemClock.sleep(ms);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (progress <= 0) {
                                            if (mComboListener != null) {
                                                mComboListener.comboOver(comboCount);
                                            }
                                            if (mTimer != null) {
                                                mTimer.cancel();
                                                mTimer = null;
                                            }
                                            mTvTime.setText("发送");
                                            return;
                                        }
                                        progress--;
                                        mCountdown.setProgress(progress);
                                        mTvTime.setText("连击 " + progress);

                                    }
                                });
                            }
                        }, 0, 3000 / 60);
                    }

                    if (mComboListener != null) {
                        mComboListener.click(isCombo, comboCount);
                    }

                }
                // 整个View恢复正常
                ScaleAnimation scale2 = new ScaleAnimation(scaleValue, 1, scaleValue, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scale2.setDuration(100);
                scale2.setFillAfter(true);
                startAnimation(scale2);
                break;
            default:
                break;
        }


        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 加载完成，设置开始倒计时
        mCountdown = (ProgressBar) findViewById(R.id.pb_countdown);
        mCountdown.setMax(60);
        mCountdown.setProgress(0);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvTime.setText("发送");
    }

    public interface ComboListener {
        void click(boolean isCombo, int comboCount);

        void comboOver(int comboCount);
    }

    public void setComboListener(ComboListener comboListener) {
        mComboListener = comboListener;
    }

    /**
     * 设置按钮类型
     *
     * @param type 1：可连击  2：不可连击 (默认可连击)
     */
    public void setType(int type) {
        switch (type) {
            case 0:
                break;
            case 1:
                // 可连击
                canCombo = true;
                break;
            case 2:
                // 不可连击
                canCombo = false;
                break;
            default:
                break;
        }
    }

    /**
     * 设置按钮状态
     *
     * @param state 1：不可点击状态  2：可点击状态  (默认可点击)
     */
    public void setState(int state) {
        switch (state) {
            case 1:
                canClick = true;
                // 不可点击状态
                if (mTimer != null) {
                    mTimer.cancel();
                }
                mTvTime.setText("发送");
                mTvTime.setTextColor(Color.parseColor("#a7acb2"));
                mCountdown.setBackgroundColor(Color.parseColor("#ee000000"));
                break;
            case 2:
                // 可点击状态
                canClick = false;
                if (mTimer != null) {
                    mTimer.cancel();
                }
                mTvTime.setText("发送");
                mTvTime.setTextColor(Color.parseColor("#ffffff"));
                mCountdown.setBackgroundColor(Color.parseColor("#12b06b"));
                break;
            default:
                break;
        }
    }

    public void destory() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

}
