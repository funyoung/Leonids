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
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class ParticleSurface extends SurfaceView implements Runnable {
    private static final long UPDATE_INTERVAL = 10;
    private Thread updateThread;
    private SurfaceHolder surfaceHolder;

    volatile private boolean shouldRun = true;
    volatile private boolean isTouched = false;


    private final List<Particle> particles = new ArrayList<>();


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
                Thread.sleep(UPDATE_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean pendingUpdate = false;
    private void drawParticles(Canvas canvas) {
        if (!pendingUpdate || null == canvas) {
            return;
        }

        synchronized (particles) {
            for (Particle p : particles) {
                p.draw(canvas);
            }
        }
        pendingUpdate = false;
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

    public void setParticles(List<Particle> particles) {
        synchronized (this.particles) {
            this.particles.clear();
            this.particles.addAll(particles);
            pendingUpdate = true;
        }

    }

}
