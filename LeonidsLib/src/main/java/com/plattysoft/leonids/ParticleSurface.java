package com.plattysoft.leonids;
/**
 * Most important class of the game.
 * Hold the game loop which checks all objects and draws them.
 * Gets the tiltevents.
 *
 * @author lars
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

class ParticleSurface extends SurfaceView implements Runnable {
    static long TIMER_TASK_INTERVAL = 33; // Default 30fps
    //private final ParticleTimerTask mTimerTask = new ParticleTimerTask(this);
    long mCurrentTime = 0;
    private long mEmittingTime;

    private WeakReference<ParticleSystem> mPs;


    //    private static final long UPDATE_INTERVAL = 10;
//    private final List<Particle> particles = new ArrayList<>();

    private List<Particle> mParticles = new ArrayList<>();
    private final List<Particle> mActiveParticles = new ArrayList<>();


    private Thread updateThread;
    private SurfaceHolder surfaceHolder;
    volatile private boolean shouldRun = true;
    volatile private boolean isTouched = false;


    public ParticleSurface(Context context) {
        this(context, null);
    }

    public ParticleSurface(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ParticleSurface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        shouldRun = true;
        surfaceHolder = getHolder();
    }

    @SuppressLint("WrongCall")
    public void run() {
        while (shouldRun) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }

            if (isTouched) {
                isTouched = false;
            }

            // draw
            Canvas c = surfaceHolder.lockCanvas();
            drawParticles(c);

            surfaceHolder.unlockCanvasAndPost(c);

            // sleep
            try {
                Thread.sleep(TIMER_TASK_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onUpdate(mCurrentTime);
            mCurrentTime += TIMER_TASK_INTERVAL;
        }
    }

    private void drawParticles(Canvas canvas) {
        if ( null == canvas) {
            return;
        }

        synchronized (mParticles) {
            for (Particle p : mParticles) {
                p.draw(canvas);
            }
        }
    }

    public boolean isRunning() {
        return shouldRun;
    }

    public void pause() {
        //TimerExec.onPause();

        shouldRun = false;
        while (updateThread != null) {
            try {
                updateThread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updateThread = null;
    }

    public void resume() {
        shouldRun = false;
        while (updateThread != null) {
            try {
                updateThread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        shouldRun = true;
        updateThread = new Thread(this);
        updateThread.start();

        //TimerExec.onResume();
    }

    void setStartTime(long time) {
        mCurrentTime = time;
    }

    void stopEmitting() {
        setEmittingTime(mCurrentTime);
    }

    void setEmittingTime(long time) {
        mEmittingTime = time;
    }

    void initParticles(int mMaxParticles, Drawable drawable) {
        if (drawable instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) drawable;
            for (int i = 0; i < mMaxParticles; i++) {
                mParticles.add(new AnimatedParticle(animation));
            }
        } else {
            Bitmap bitmap = null;
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            }
            initParticles(mMaxParticles, bitmap);
//            for (int i = 0; i < mMaxParticles; i++) {
//                mParticles.add(new Particle(bitmap));
//            }
        }
    }

    void initParticles(int mMaxParticles, Bitmap bitmap) {
        for (int i = 0; i < mMaxParticles; i++) {
            mParticles.add(new Particle(bitmap));
        }
    }

//    public void setParticles(List<Particle> particles) {
//        synchronized (this.particles) {
//            this.particles.clear();
//            this.particles.addAll(particles);
//            pendingUpdate = true;
//        }
//
//    }

    private void updateParticleField() {
//		mDrawingView.postInvalidate();
        //mDrawingView.setParticles(mActiveParticles);
    }

    void updateParticlesBeforeStartTime(int particlesPerSecond) {
        if (particlesPerSecond == 0) {
            return;
        }
        long currentTimeInMs = mCurrentTime / 1000;
        long framesCount = currentTimeInMs / particlesPerSecond;
        if (framesCount == 0) {
            return;
        }
        long frameTimeInMs = mCurrentTime / framesCount;
        for (int i = 1; i <= framesCount; i++) {
            onUpdate(frameTimeInMs * i + 1);
        }
    }

    void onUpdate(long miliseconds) {
        while (((mEmittingTime > 0 && miliseconds < mEmittingTime) || mEmittingTime == -1) && // This point should emit
                !mParticles.isEmpty() && // We have particles in the pool
                mActivatedParticles < mParticlesPerMillisecond * miliseconds) { // and we are under the number of particles that should be launched
            // Activate a new particle
            activateParticle(miliseconds);
        }
        synchronized (mActiveParticles) {
            for (int i = 0; i < mActiveParticles.size(); i++) {
                boolean active = mActiveParticles.get(i).update(miliseconds);
                if (!active) {
                    Particle p = mActiveParticles.remove(i);
                    i--; // Needed to keep the index at the right position
                    mParticles.add(p);
                }
            }
        }

        updateParticleField();
    }

    void activateParticle(long delay) {
        Particle p = mParticles.remove(0);
        p.init();

        // Initialization goes before configuration, scale is required before can be configured properly
        if (mPs.get() != null) {
            ParticleSystem ps = mPs.get();
            ps.activate(p, delay);
        }

        mActivatedParticles++;
        mActiveParticles.add(p);
    }

    void cleanupAnimation() {
        mParticles.addAll(mActiveParticles);
    }


    private float mParticlesPerMillisecond;
    private int mActivatedParticles;

    void resetEmitting() {
        mActivatedParticles = 0;
    }
    void resetEmitting(int particlesPerSecond) {
        resetEmitting();
        mParticlesPerMillisecond = particlesPerSecond / 1000f;
    }

    void setParticleSystem(ParticleSystem ps) {
        mPs = new WeakReference<>(ps);
    }
}
