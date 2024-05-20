package com.example.roulet;

public class Wheel extends Thread {

    interface WheelListener {
        void newImage(int img);
    }

    static int[] drawables = {R.drawable.slot1,R.drawable.slot1,R.drawable.slot1,R.drawable.slot1,R.drawable.slot1,
            R.drawable.slot2,R.drawable.slot2,R.drawable.slot2,
            R.drawable.slot3, R.drawable.slot3,R.drawable.slot3,R.drawable.slot3,
            R.drawable.slot4,R.drawable.slot4,
                                R.drawable.slot6};
    public int currentIndex;
    private WheelListener wheelListener;
    private long frameDuration;
    private long startIn;
    private boolean isStarted;

    public Wheel(WheelListener wheelListener, long frameDuration, long startIn) {
        this.wheelListener = wheelListener;
        this.frameDuration = frameDuration;
        this.startIn = startIn;
        currentIndex = 0;
        isStarted = true;
    }

    public void nextImg() {
        currentIndex++;

        if (currentIndex == drawables.length) {
            currentIndex = 0;
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(startIn);
        } catch (InterruptedException e) {
        }

        while (isStarted) {
            try {
                Thread.sleep(frameDuration);
            } catch (InterruptedException e) {
            }

            if (isStarted) nextImg();

            if (wheelListener != null) {
                wheelListener.newImage(drawables[currentIndex]);
            }
        }
    }


    public void stopWheel() {
        isStarted = false;
    }
}