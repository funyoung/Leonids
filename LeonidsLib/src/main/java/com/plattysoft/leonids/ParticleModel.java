package com.plattysoft.leonids;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class ParticleModel {
    private List<Particle> mParticles;

    void setParticles(ArrayList<Particle> particles) {
        mParticles = particles;
    }

    void draw(Canvas canvas) {
        // Draw all the particles
        synchronized (mParticles) {
            for (int i = 0; i < mParticles.size(); i++) {
                mParticles.get(i).draw(canvas);
            }
        }
    }
}
