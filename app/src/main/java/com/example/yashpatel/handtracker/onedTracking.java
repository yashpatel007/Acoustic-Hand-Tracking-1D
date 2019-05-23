package com.example.yashpatel.handtracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hauoli.trackhand.HauoliConst;
import com.hauoli.trackhand.HauoliTracker;

import java.util.Random;

import static java.lang.Math.abs;

public class onedTracking extends AppCompatActivity {

    TextView tv;


ImageView ball;

    AsteroidView asteroidView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asteroidView = new AsteroidView(this);
        setContentView(asteroidView);

       // track();

    }



    public void track() {

     try {

         int nDim = 1;
         int nSpk = 1;
         int nMic = 1;
         // set the initial distance from hand to mic = 100mm
         double[] initPos = {0, -100};
         // the position of the speaker on phone: [0, 0]
         double[] spkPos  = {0, 0};
         // the position of the microhpone on phone: [0, 0]
         double[] micPos  = {0, 0};

         HauoliTracker.initTracker(nSpk, nMic, initPos, spkPos, micPos);// returns the value 1
         HauoliTracker.setDistEstMethod(HauoliConst.METHOD_TAP);
         HauoliTracker.setAudioSource(HauoliConst.AUDIO_SOURCE_JAVA);
         HauoliTracker.setSeq(30, 240, 6000);
         HauoliTracker.setMaxTap(120);
         HauoliTracker.setTapStep(10);
         HauoliTracker.setAllowReset(false);
         HauoliTracker.setAutoThrd(true);
         HauoliTracker.setRecordAudio(false);
         HauoliTracker.setUseFile(false);
         HauoliTracker.enableGesture(false);
         double[] dists = new double[nMic];
         HauoliTracker.start();
         while (true) {
            HauoliTracker.getDists(dists);
               int status = HauoliTracker.getState();
               double posx= HauoliTracker.getPosX();
               double posy = HauoliTracker.getPosY();
               //x_cor=posy;
               double posz = HauoliTracker.getPosZ();
               int gesture = HauoliTracker.getGesture();
               // status =6 means it is tracking
               Log.d("Hauoli", "est dist = " + dists[0] +" Y: "+posy+ " gesture : "+ gesture +" status:"+ status);
               //tv.append("dists:"+String.valueOf(dists[0])+"\n" );
               try {
                   Thread.sleep(5);
               } catch (Exception e) {
                   Log.e("Error", "run: " + e.toString());
               }


         }
          //stop after 100 iterations
         // HauoliTracker.stop();


     }catch(Exception e){
         Toast.makeText(onedTracking.this,"Error",Toast.LENGTH_SHORT).show();
     }
 }// track() ends

//==================================== class begains ==================================//
    class AsteroidView extends SurfaceView implements Runnable {
        Thread gameThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playing;
        boolean paused = true;
        Canvas canvas;
        Paint paint;
        int y;
        int posx,posy;
        int dx, dy;
        int height, width;
        boulder b;

    private long thisTimeFrame;
        public AsteroidView(Context context) {
            super(context);

            ourHolder = getHolder();
            paint = new Paint();
        }

        @Override
        public void run() {
            Random r = new Random();
            b = new boulder();
            posx = 50;
            dx = 0;
            b.x = 100;
            b.y = 300;
            b.dx= dx;

            b.diameter = 95;
            //================================== here comes the SKK use ==============================//
            int nDim = 1;
            int nSpk = 1;
            int nMic = 1;
            // set the initial distance from hand to mic = 100mm
            double[] initPos = {0, -100};
            // the position of the speaker on phone: [0, 0]
            double[] spkPos  = {0, 0};
            // the position of the microhpone on phone: [0, 0]
            double[] micPos  = {0, 0};
            HauoliTracker.initTracker(nSpk, nMic, initPos, spkPos, micPos);// returns the value 1
            HauoliTracker.setDistEstMethod(HauoliConst.METHOD_TAP);
            HauoliTracker.setAudioSource(HauoliConst.AUDIO_SOURCE_JAVA);
            HauoliTracker.setSeq(30, 240, 6000);
            HauoliTracker.setMaxTap(120);
            HauoliTracker.setTapStep(10);
            HauoliTracker.setAllowReset(false);
            HauoliTracker.setAutoThrd(true);
            HauoliTracker.setRecordAudio(false);
            HauoliTracker.setUseFile(false);
            HauoliTracker.enableGesture(false);
            double[] dists = new double[nMic];
            HauoliTracker.start();
            HauoliTracker.getDists(dists);// gets the initial value of sensor  to improve accuracy
            int init_val= (int) dists[0];// store the initial sensor data to init_val
            //============================ end : so sad(haha) ======================================//
            while (playing)
            {
                if (!paused) {
                    //-------------------------------------------
                    HauoliTracker.getDists(dists);
                    int status = HauoliTracker.getState();
                    // double posx= HauoliTracker.getPosX();
                    double posy = HauoliTracker.getPosY();
                    // double posz = HauoliTracker.getPosZ();
                    int gesture = HauoliTracker.getGesture();
                    // status =6 means it is tracking// posy is same as dist[0] just negetive
                    b.dx = 2*(int) (abs(dists[0])-init_val);// we can change the speed of movement here 
                    Log.d("Hauoli", "est dist = " + dists[0] +" Y: "+posy+ " gesture : "+ gesture +" status:"+ status+" dx : "+b.dx);

                    try {
                        Thread.sleep(5);
                    } catch (Exception e) {
                        Log.e("Error", "run: " + e.toString());
                    }
                    //---------------------------------------------
                    update();
                }
                draw();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {

                }

            }
        }// run ends=============//
        public void update() {

           // posx += dx;

            if (posx > width)
                posx = width;
            if(posx<0)
                posx=0;

            b.update();

        }
        public void draw() {
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();
                // get width and height of canvas
                width = canvas.getWidth();
                height = canvas.getHeight();

                // Draw the background color
                canvas.drawColor(Color.WHITE);

                // Choose the brush color for drawing
                paint.setColor(Color.argb(255, 0, 0, 0));
                // canvas.drawCircle(posx, posy, 30l, paint);
                    b.width = width;
                    b.height = height;
                    b.draw(canvas, paint);

                // now unloak the canvas
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }

        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            // start or stop the run method when we tap the screen
            if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN)
                paused = !paused;
            return true;
        }
    }



    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        asteroidView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        asteroidView.pause();
    }



}//class ends
