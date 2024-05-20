package com.example.roulet;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_coin;
    private Button btn_flip;
    private TextView tv_currency;
    private TextView wins;
    private double navsebabki = 0;
    private double winss = 0;
    private Button button2;
    private Button button3;
    private TextView babki;
    private Button blackbatton;
    private Button whitebatton;
    private ImageView audio;
    private MediaPlayer mediaPlayer;
    private ImageView restart;

    private int[] coinImages = {R.drawable.blackcoin2, R.drawable.whitecoin2};
    private double currency;
    int counter = 0;

    private ImageView history1, history2, history3, history4, history5, history6;
    private int historyIndex = 0;
    private int[] coinResults = new int[6];


    private void hideAllHistoryImages() {
        history1.setImageResource(android.R.color.transparent);
        history2.setImageResource(android.R.color.transparent);
        history3.setImageResource(android.R.color.transparent);
        history4.setImageResource(android.R.color.transparent);
        history5.setImageResource(android.R.color.transparent);
        history6.setImageResource(android.R.color.transparent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBase.LoadData();
        Menu.MenuInit(findViewById(R.id.navigation_coin), this);
        currency = Fantiki.currentFantiki;


        iv_coin = findViewById(R.id.iv_coin);
        btn_flip = findViewById(R.id.btn_flip);
        tv_currency = findViewById(R.id.tv_currency);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        babki = findViewById(R.id.babki);
        blackbatton = findViewById(R.id.blackbutton);
        whitebatton = findViewById(R.id.whitebutton);
        audio = findViewById(R.id.audio);
        restart = findViewById(R.id.rastart);
        wins = findViewById(R.id.wins);
        Music.musicInit(this, R.raw.real_gone);
        history1 = findViewById(R.id.history1);
        history2 = findViewById(R.id.history2);
        history3 = findViewById(R.id.history3);
        history4 = findViewById(R.id.history4);
        history5 = findViewById(R.id.history5);
        history6 = findViewById(R.id.history6);


        hideAllHistoryImages();

        printnavsebabki();
        printcurrensy();


        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navsebabki = 0;
                printnavsebabki();
                blackbatton.setEnabled(true);
                whitebatton.setEnabled(true);
                btn_flip.setEnabled(false);
            }
        });
//        audio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mediaPlayer.isPlaying()) {
//                    mediaPlayer.pause();
//                }
//                else {
//                    mediaPlayer.start();
//                }
//            }
//        });

        btn_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currency >=navsebabki) {
                    flipTheCoin();
                } else {
                    Toast.makeText(MainActivity.this, "Пожалуйста,пополните", Toast.LENGTH_SHORT).show();
                }
            }
        });

       button2.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               navsebabki = 5;
               printnavsebabki();
               return true;
           }
       });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navsebabki >= 5 && (blackbatton.isEnabled() || whitebatton.isEnabled())) {
                    navsebabki -= 5;
                    printnavsebabki();
                }
            }
        });

        button3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                navsebabki = currency;
                printnavsebabki();
                return true;
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blackbatton.isEnabled() || whitebatton.isEnabled()) {
                    navsebabki += 5;
                    printnavsebabki();
                }
            }
        });


        blackbatton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navsebabki >=5 && currency >= 0) {
                    printcurrensy();
                    whitebatton.setEnabled(false);
                    btn_flip.setEnabled(true);
                } else {
                    Toast.makeText(MainActivity.this, "Пожалуйста, сделайте ставку перед тем, как крутить монету или проверьте баланс", Toast.LENGTH_SHORT).show();
                }

            }
        });

        whitebatton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navsebabki >=5 && currency >0) {
                    printcurrensy();
                    blackbatton.setEnabled(false);
                    btn_flip.setEnabled(true);
                } else {
                    Toast.makeText(MainActivity.this, "Пожалуйста, сделайте ставку перед тем, как крутить монету или проверьте баланс", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Music.musicOFF();
    }


    private void addToHistory(int coinImage) {
        if (historyIndex < coinResults.length) {

            coinResults[historyIndex] = coinImage;

            switch (historyIndex) {
                case 0:
                    history1.setImageResource(coinImage);
                    break;
                case 1:
                    history2.setImageResource(coinImage);
                    break;
                case 2:
                    history3.setImageResource(coinImage);
                    break;
                case 3:
                    history4.setImageResource(coinImage);
                    break;
                case 4:
                    history5.setImageResource(coinImage);
                    break;
                case 5:
                    history6.setImageResource(coinImage);
                    break;
            }
            historyIndex++;
    }
    else {
            history6.setImageResource(coinResults[4]);
            history5.setImageResource(coinResults[3]);
            history4.setImageResource(coinResults[2]);
            history3.setImageResource(coinResults[1]);
            history2.setImageResource(coinResults[0]);
            history1.setImageResource(coinImage);
            for (int i = 5; i >= 1; i--) {
                coinResults[i] = coinResults[i - 1];
            }
            coinResults[0] = coinImage;
        }
    }

    private void printnavsebabki(){
        babki.setText("" + navsebabki);
    }
    private void printcurrensy(){
        tv_currency.setText("Баланс: " + currency);

    }

    private void flipTheCoin() {
        if (AchievementActivity.achievementFirstBet()) AchievementActivity.showMessage(this);
        if (navsebabki == 0) {
            Toast.makeText(MainActivity.this, "Пожалуйста, сделайте ставку перед тем, как крутить монету", Toast.LENGTH_SHORT).show();
            return;
        }
        Random random = new Random(System.currentTimeMillis());
        int randomNumber = random.nextInt(2);
        String coinSide = (randomNumber == 0) ? "black" : "white";
        boolean isWin = (coinSide.equals("black") && blackbatton.isEnabled()) || (coinSide.equals("white") && whitebatton.isEnabled());

        final ValueAnimator rotateAnimator = ValueAnimator.ofFloat(0f, 360f);
        rotateAnimator.setDuration(1000);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rotation = (float) animation.getAnimatedValue();
                iv_coin.setRotationY(rotation);
            }
        });
        rotateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                btn_flip.setEnabled(false);
                blackbatton.setEnabled(false);
                whitebatton.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iv_coin.setRotationY(0f);
                blackbatton.setEnabled(true);
                whitebatton.setEnabled(true);

                if (coinSide.equals("black")) {
                    iv_coin.setImageResource(R.drawable.blackcoin2);
                } else {
                    iv_coin.setImageResource(R.drawable.whitecoin2);
                }
                addToHistory(coinImages[randomNumber]);
                if (isWin) {
                    if (AchievementActivity.allinnaBeloe(navsebabki,currency)&& coinSide.equals("white")) AchievementActivity.showMessage(MainActivity.this);
                    if (AchievementActivity.achivmentcoins(++counter)) AchievementActivity.showMessage(MainActivity.this);
                    currency += navsebabki ;
                    Fantiki.currentFantiki = currency;
                    DataBase.updateData(currency, MainActivity.this, navsebabki, navsebabki);
                    DataBase.addToHistory(navsebabki, winss, "Подбрось монетку");
                    winss = navsebabki;
                    updatewins();
                } else {
                    counter = 0;
                    currency -= navsebabki;
                    Fantiki.currentFantiki = currency;
                    DataBase.updateData(currency, MainActivity.this, 0, navsebabki);
                    DataBase.addToHistory(navsebabki, 0, "Подбрось монетку");
                    wins.setText("");
                }

                printcurrensy();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        rotateAnimator.start();

    }
    public void navigationPanelCoin(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout_coin);
        drawerLayout.openDrawer(GravityCompat.START);
    }


private void updatewins(){
        wins.setText("" + winss*2);
}
    private void updateCurrency(int amount) {
        currency += amount;
    }
    public void switchSound(View v) {
        Music.musicSwitch(v);
    }


}
