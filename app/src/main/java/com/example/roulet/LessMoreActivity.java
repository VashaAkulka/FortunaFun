package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;


public class LessMoreActivity extends AppCompatActivity {
    final String[] shapes = {"circle", "star", "moon"};
    int number;

    Drawable card;
    int countSubCards = 0;
    ImageView[] subCards;

    double up;
    double down;

    float pixels;
    boolean playNow = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_less_more);

        Menu.MenuInit(findViewById(R.id.navigation_more_less), this);
        Music.musicInit(this, R.raw.music);

        Fantiki.ViewFantiki(findViewById(R.id.balanceView_more_less));
        Fantiki.ViewBet(findViewById(R.id.betView_more_less));

        subCards = new ImageView[]{findViewById(R.id.card_1), findViewById(R.id.card_2), findViewById(R.id.card_3), findViewById(R.id.card_4),
                findViewById(R.id.card_5), findViewById(R.id.card_6), findViewById(R.id.card_7), findViewById(R.id.card_8), findViewById(R.id.card_9),
                findViewById(R.id.card_10), findViewById(R.id.card_11), findViewById(R.id.card_12)};

        randomImage();
        ((ImageView)findViewById(R.id.main_card)).setImageDrawable(card);
        percentCalculate();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidthPx = displayMetrics.widthPixels;
        float screenWidthDp = screenWidthPx / displayMetrics.density;
        pixels = (float) ((screenWidthDp - 80) / 4.6 * 1.5 + 10) * displayMetrics.density + 0.5f;

        findViewById(R.id.buttonMinus_more_less).setOnLongClickListener(view -> {
            Fantiki.bet = 0;
            Fantiki.ViewBet(findViewById(R.id.betView_more_less));
            return true;
        });

        findViewById(R.id.buttonPlus_more_less).setOnLongClickListener(view -> {
            Fantiki.bet = Fantiki.currentFantiki;
            Fantiki.ViewBet(findViewById(R.id.betView_more_less));
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Music.musicOFF();
    }

    public void GameMoreLess(View v) {
             if (Fantiki.currentFantiki < Fantiki.bet && !playNow) {
                  return;
             }

            if (countSubCards == 0) {
                SwitchButton(false);
                Fantiki.currentFantiki -= Fantiki.bet;
                DataBase.updateData(Fantiki.currentFantiki, this);

                Fantiki.win = Fantiki.bet;
                Fantiki.ViewFantiki(findViewById(R.id.balanceView_more_less));
                playNow = true;
                if (AchievementActivity.achievementFirstBet()) AchievementActivity.showMessage(this);
            }

            int old_number = number;
            Drawable old_card = card;

            ImageView cardSlide = findViewById(R.id.card_slide);
            cardSlide.animate().translationXBy(-pixels).setDuration(500).withStartAction(() -> {
                SwitchDownUp(false);
                SwitchTake(false);
            }).withEndAction(() -> {
                cardSlide.animate().rotationYBy(-90).setDuration(300).withEndAction(() -> {
                    randomImage();
                    cardSlide.setImageDrawable(card);
                    cardSlide.animate().rotationYBy(90).setDuration(200).withEndAction(() -> {
                        cardSlide.setImageDrawable(null);
                        ((ImageView)findViewById(R.id.main_card)).setImageDrawable(card);
                        cardSlide.animate().translationXBy(pixels).withEndAction(() -> {
                            cardSlide.setImageResource(R.drawable.back);

                            if (old_number <= number && v.getId() == R.id.button_up) {
                                Fantiki.win = Fantiki.win * up;
                                Fantiki.win = new BigDecimal(Fantiki.win).setScale(2, RoundingMode.HALF_UP).doubleValue();

                                subCards[countSubCards].setImageDrawable(old_card);
                                countSubCards = (countSubCards + 1) % 12;
                                if (AchievementActivity.achievementCount(countSubCards)) AchievementActivity.showMessage(this);
                                SwitchTake(true);
                            } else if (old_number >= number && v.getId() == R.id.button_down) {
                                Fantiki.win = Fantiki.win * down;
                                Fantiki.win = new BigDecimal(Fantiki.win).setScale(2, RoundingMode.HALF_UP).doubleValue();

                                subCards[countSubCards].setImageDrawable(old_card);
                                countSubCards = (countSubCards + 1) % 12;
                                if (AchievementActivity.achievementCount(countSubCards)) AchievementActivity.showMessage(this);
                                SwitchTake(true);
                            } else {
                                Fantiki.win = 0;
                                countSubCards = 0;
                                SwitchButton(true);
                                clearSubCards();
                                SwitchTake(false);
                                playNow = false;
                            }

                            percentCalculate();
                            ((TextView)findViewById(R.id.winningView_more_less)).setText("" + Fantiki.win + " FAN");
                            SwitchDownUp(true);
                        }).start();
                    }).start();
                }).start();
            }).start();
    }


    public void Take(View v) {
        Fantiki.currentFantiki += Fantiki.win;
        Fantiki.ViewFantiki(findViewById(R.id.balanceView_more_less));
        DataBase.updateData(Fantiki.currentFantiki, this);

        Fantiki.win = 0;
        countSubCards = 0;
        SwitchButton(true);
        SwitchTake(false);

        clearSubCards();
        playNow = false;
    }

    public void clearSubCards(){
        for (ImageView imageView : subCards) {
            imageView.setImageDrawable(null);
        }
    }

    void SwitchButton(boolean fl) {
        findViewById(R.id.buttonMinus_more_less).setEnabled(fl);
        findViewById(R.id.buttonPlus_more_less).setEnabled(fl);
        findViewById(R.id.change_card).setEnabled(fl);
    }

    void SwitchDownUp(boolean fl) {
        findViewById(R.id.button_down).setEnabled(fl);
        findViewById(R.id.button_up).setEnabled(fl);
    }

    void SwitchTake(boolean fl) {
        findViewById(R.id.take_more_less).setEnabled(fl);
    }


    public void changeCard(View v) {
        randomImage();
        ((ImageView)findViewById(R.id.main_card)).setImageDrawable(card);
        percentCalculate();
    }

    public void percentCalculate() {
        up = number / 20. + 1;
        down = (11 - number) / 20. + 1;

        ((TextView)findViewById(R.id.percent_up)).setText("x" + up);
        ((TextView)findViewById(R.id.percent_down)).setText("x" + down);
    }

    public void randomImage() {
        AssetManager assetManager = getAssets();
        Random ran = new Random();
        number = ran.nextInt(10) + 1;
        String fileName = "cards_hilo/" + shapes[ran.nextInt(3)] + number + ".png";

        try (InputStream inputStream = assetManager.open(fileName)) {
            card = Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeBetMoreLess(View v) {
        if (v.getId() == R.id.buttonMinus_more_less) {
            if (Fantiki.bet > 1) Fantiki.bet -= Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
        }
        else {
            double buff = Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
            if (buff + Fantiki.bet <= Fantiki.currentFantiki) Fantiki.bet += buff;
        }

        Fantiki.ViewBet(findViewById(R.id.betView_more_less));
    }

    public void navigationPanelMoreLess(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout_less_more);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void switchSoundMoreLess(View v) {
        Music.musicSwitch(v);
    }
}