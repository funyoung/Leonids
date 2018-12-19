package com.plattysoft.leonids.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleModel;
import com.plattysoft.leonids.ParticleSurface;

import java.lang.ref.WeakReference;

/*
 * 这个类就是加工了SurfaceView之后的类，所有要运动的物件都最终放在这里进行绘制
 */
public class Playground extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Thread thread; // SurfaceView通常需要自己单独的线程来播放动画
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

//    private Sprite obj;

    public Playground(Context c) {
        this(c, null);
    }

    public Playground(Context c, AttributeSet attrs) {
        super(c, attrs);

        this.surfaceHolder = this.getHolder();
        this.surfaceHolder.addCallback(this);
//        Bitmap img = BitmapFactory.decodeResource(c.getResources(), R.drawable.app_icon);
//        this.obj = new Sprite(img);
    }

    @Override
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (changedView == this && visibility == GONE) {
            Toast.makeText(getContext(), "SurfaceView已经隐藏", Toast.LENGTH_LONG).show();
            thread.interrupt();
        }
    }

    @Override
    public void run() {
        while (true) {
            canvas = this.surfaceHolder.lockCanvas(); // 通过lockCanvas加锁并得到該SurfaceView的画布
            if (null == canvas) {
                return;
            }

            updateParticles(canvas);
//            obj.getNextPos();
//            obj.drawSelf(canvas); // 把SurfaceView的画布传给物件，物件会用这个画布将自己绘制到上面的某个位置
            this.surfaceHolder.unlockCanvasAndPost(canvas); // 释放锁并提交画布进行重绘
            try {
                Thread.sleep(TIMER_TASK_INTERVAL); // 这个就相当于帧频了，数值越小画面就越流畅
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateParticles(Canvas canvas) {
        if (null == canvas || null == model) return;
        synchronized (model) {
            if (mPs.get() != null) {
                ParticleSurface ps = mPs.get();
                ps.onUpdate(ps.mCurrentTime);
                ps.mCurrentTime += TIMER_TASK_INTERVAL;
            }

            canvas.drawColor(Color.BLACK);
            model.drawParticles(canvas);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Toast.makeText(getContext(), "SurfaceView已经销毁", Toast.LENGTH_LONG).show();
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Toast.makeText(getContext(), "SurfaceView已经创建", Toast.LENGTH_LONG).show();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // 这里是SurfaceView发生变化的时候触发的部分
        //obj.setBound(width, height);
    }

    private static long TIMER_TASK_INTERVAL = 1000;
    private ParticleModel model;
    private WeakReference<ParticleSurface> mPs;
    public void schedule(ParticleSurface ps, ParticleModel model, long timerTaskInterval) {
        mPs = new WeakReference<>(ps);
        this.model = model;
        TIMER_TASK_INTERVAL = timerTaskInterval;
        this.thread = new Thread(this);
        this.thread.start();
    }
}
