package com.example.roulet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.DecimalFormat;
import java.util.Random;

public class Percent extends AppCompatActivity {

    private SeekBar seekBar;
    private Button playButton;
    private Button decreaseButton;
    private Button increaseButton;
    private TextView percentageTextView;


    private double winningPercentage;
    private double winningPercentage2;
    private Handler handler = new Handler();



    private TextView balanceT;
    private TextView Win1;
    private TextView balance1;
    private Random random;
    private TextView Resultpercent;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percent);
        DataBase.LoadData();
        if (AchievementActivity.achievementNewBee()) AchievementActivity.showMessage(this);
        Menu.MenuInit(findViewById(R.id.navigation_percent), this);
        seekBar = findViewById(R.id.seekBar);
        playButton = findViewById(R.id.playButton);
        percentageTextView = findViewById(R.id.percentageTextView);
//        balanceT = findViewById(R.id.balance1);
        Win1 = findViewById(R.id.win1);
        balance1 = findViewById(R.id.balancem);
        decreaseButton = findViewById(R.id.decreaseButton);
        increaseButton = findViewById(R.id.increaseButton);
        Resultpercent = findViewById(R.id.resultTextView);


        updateSeekBarColor();
        random = new Random();
        Fantiki.ViewFantiki(findViewById(R.id.balancem));
        Fantiki.ViewBet(findViewById(R.id.balance1));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                winningPercentage = progress / 100.;
                int color1;
                color1 = Color.WHITE;
                percentageTextView.setTextColor(color1);
                DecimalFormat df = new DecimalFormat("##0.00%");
                String formattedPercentage = df.format(winningPercentage / 100.0);

                percentageTextView.setText(formattedPercentage);
                updateSeekBarColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        findViewById(R.id.decreaseButton).setOnLongClickListener(view -> {
            Fantiki.bet = 1;
            Fantiki.ViewBet(findViewById(R.id.balance1));
            return true;
        });

        findViewById(R.id.increaseButton).setOnLongClickListener(view -> {
            Fantiki.bet = Fantiki.currentFantiki;
            Fantiki.ViewBet(findViewById(R.id.balance1));
            return true;
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Fantiki.currentFantiki >= Fantiki.bet) {
                    playGame();
                } else {
                    Toast.makeText(Percent.this, "Нет денег", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    double randomNumber;
    String formattedRandomNumber;
    public void changeBete(View v) {
        if (v.getId() == R.id.decreaseButton) {
            if (Fantiki.bet > 1) Fantiki.bet -= Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
        }
        else {
            double buff = Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
            if (buff + Fantiki.bet <= Fantiki.currentFantiki) Fantiki.bet += buff;
        }

        Fantiki.ViewBet(findViewById(R.id.balance1));
    }


    private void playGame() {
        if (AchievementActivity.achievementFirstBet()) AchievementActivity.showMessage(this);
        Resultpercent.setText("Ваш процент: ");
        winningPercentage2 = winningPercentage;
        randomNumber = random.nextDouble() * 100;
        DecimalFormat df = new DecimalFormat("##0.00");
        formattedRandomNumber = df.format(randomNumber);
        formattedRandomNumber = formattedRandomNumber.replace(",", ".");
        randomNumber = Double.parseDouble(formattedRandomNumber);
        double targetPercentage = Double.parseDouble(formattedRandomNumber);
        animateSeekBarThumb(Double.parseDouble(formattedRandomNumber));
    }

    private static final int ANIMATION_DURATION = 10000; // Длительность анимации в миллисекундах
    private void animateSeekBarThumb(final double targetPercentage) {
        playButton.setEnabled(false);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator startAnimation = ObjectAnimator.ofInt(seekBar, "progress", 0);
        startAnimation.setDuration(ANIMATION_DURATION / 3);
        startAnimation.setInterpolator(new LinearInterpolator());
        startAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                int progress = (int) ((ObjectAnimator) animation).getAnimatedValue();
                double currentPercentage = progress / 100.0;
                DecimalFormat df = new DecimalFormat("##0.00%");
                String formattedPercentage = df.format(currentPercentage / 100.0);
                percentageTextView.setText(formattedPercentage);
                ObjectAnimator targetAnimation = ObjectAnimator.ofInt(seekBar, "progress", seekBar.getProgress(), (int) (targetPercentage * 100));
                targetAnimation.setDuration(ANIMATION_DURATION / 3);
                targetAnimation.setInterpolator(new LinearInterpolator());
                targetAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int progress = (int) animation.getAnimatedValue();
                        double currentPercentage = progress / 100.0;
                        DecimalFormat df = new DecimalFormat("##0.00%");
                        String formattedPercentage = df.format(currentPercentage / 100.0);
                        percentageTextView.setText(formattedPercentage);
                    }
                });

                // После завершения второй анимации, вызываем метод check()
                targetAnimation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        playButton.setEnabled(true); // Включаем кнопку "Играть" после окончания анимации
                        check(); // Проверяем результат
                    }
                });

                targetAnimation.start(); // Запускаем вторую анимацию
            }
        });

        animatorSet.play(startAnimation);
        animatorSet.start(); // Запускаем первую анимацию
    }
    private boolean isSeekBarResetting = false;
    private void check() {
        Resultpercent.setText("Ваш процент: " + winningPercentage2 );
        Fantiki.win = 0;
        printwin();
        int color;
        double check;
        check = Double.parseDouble(formattedRandomNumber);
        if (check >= winningPercentage2) {
            if (AchievementActivity.achievementFirstPercent()) AchievementActivity.showMessage(this);
            if (winningPercentage2 >= 2 && winningPercentage2 < 10) {
                Fantiki.currentFantiki += Fantiki.bet * 0.4;
                Fantiki.win += Fantiki.bet * 0.4;
                DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
                DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Процент");
                printwin();
            } else if (winningPercentage2 >= 10 && winningPercentage2 < 20) {
                Fantiki.currentFantiki += Fantiki.bet * 0.9;
                Fantiki.win += Fantiki.bet * 0.9;
                DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
                DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Процент");
                printwin();
            } else if (winningPercentage2 >= 20 && winningPercentage2 < 30) {
                Fantiki.currentFantiki += Fantiki.bet * 1.6;
                Fantiki.win += Fantiki.bet * 1.6;
                DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
                DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Процент");
                printwin();
            } else if (winningPercentage2 >= 30 && winningPercentage2 < 40) {
                Fantiki.currentFantiki += Fantiki.bet * 2.5;
                Fantiki.win += Fantiki.bet * 2.5;
                DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
                DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Процент");
                printwin();
            } else if (winningPercentage2 >= 40 && winningPercentage2 < 50) {
                Fantiki.currentFantiki += Fantiki.bet * 5.25;
                Fantiki.win += Fantiki.bet * 5.25;
                DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
                DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Процент");
                printwin();
            } else if (winningPercentage2 >= 50 && winningPercentage2 < 60) {
                if (AchievementActivity.achievementPercentMiddle()) AchievementActivity.showMessage(this);
                Fantiki.currentFantiki += Fantiki.bet * 7.36;
                Fantiki.win += Fantiki.bet * 7.36;
                DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
                DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Процент");
                printwin();
            } else if (winningPercentage2 >= 60 && winningPercentage2 < 70) {
                if (AchievementActivity.achievementPercentMiddle()) AchievementActivity.showMessage(this);
                Fantiki.currentFantiki += Fantiki.bet * 10.75;
                Fantiki.win += Fantiki.bet * 10.75;
                DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
                DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Процент");
                printwin();
            } else if (winningPercentage2 >= 70 && winningPercentage2 < 80) {
                if (AchievementActivity.achievementPercentMiddle()) AchievementActivity.showMessage(this);
                Fantiki.currentFantiki += Fantiki.bet * 15.79;
                Fantiki.win += Fantiki.bet * 15.79;
                DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
                DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Процент");
                printwin();
            } else if (winningPercentage2 >= 80 && winningPercentage2 < 90) {
                if (AchievementActivity.achievementPercentMiddle()) AchievementActivity.showMessage(this);
                Fantiki.currentFantiki += Fantiki.bet * 18.89;
                Fantiki.win += Fantiki.bet * 18.89;
                DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
                DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Процент");
                printwin();
            } else if (winningPercentage2 >= 90 && winningPercentage2 < 100) {
                if (AchievementActivity.achievementPercentMiddle()) AchievementActivity.showMessage(this);
                if (AchievementActivity.achievementTopPercent()) AchievementActivity.showMessage(this);
                Fantiki.currentFantiki += Fantiki.bet * 25.6;
                Fantiki.win += Fantiki.bet * 25.6;
                DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
                DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Процент");
                printwin();
            }
            playButton.setEnabled(true);
            color = Color.GREEN;
            printbalance();
        } else {
            playButton.setEnabled(true);
            Fantiki.currentFantiki -= Fantiki.bet;
            color = Color.RED;
            DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
            DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Процент");
            printbalance();
        }
        percentageTextView.setText(formattedRandomNumber + "%");
        percentageTextView.setTextColor(color);
        percentageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                winningPercentage = 0.00;
                percentageTextView.setText("0.00%");
                percentageTextView.setTextColor(Color.WHITE);
                seekBar.setProgress(0);
                seekBar.setThumbOffset(0);
                updateSeekBarColor();
                playButton.setEnabled(true);
                isSeekBarResetting = false;
            }
        }, 5000);

        playButton.setEnabled(false);
        isSeekBarResetting = true;
    }

    private void printwin() {
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedWin = df.format(Fantiki.win);
        Win1.setText(formattedWin);
    }

    private void printbalance() {
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedBalance = df.format(Fantiki.currentFantiki);
        balance1.setText(formattedBalance + " FAN");
    }
    public void navigationPanelPercent(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout_percent);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void updateSeekBarColor() {
        if (seekBar.getProgress() < winningPercentage) {
            seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progress_losing));
        } else {
            seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progress_winning));
        }
    }
}

