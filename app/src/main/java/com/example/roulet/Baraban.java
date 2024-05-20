package com.example.roulet;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Baraban extends AppCompatActivity {

    private TextView baabki;
    private TextView wins;

    private ImageView restart;

    private TextView currency2;
    private ImageView image1, image2, image3;
    private Wheel wheel1, wheel2, wheel3;
    private Button button, plus, minus;
    public static final Random RANDOM = new Random();
    private Handler handler;

    private double stavka = 0;
    private double summa = 0;
    private double winss = 0;
    int counter = 0;
    private int scrollCount = 0;

    boolean firstPress = false;
    boolean secondPress = false;

    public static long randomLong(long lower, long upper) {
        return lower + (long) (RANDOM.nextDouble() * (upper - lower));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baraban);
        DataBase.LoadData();
        Menu.MenuInit(findViewById(R.id.navigation_baraban), this);
        summa = Fantiki.currentFantiki;
        restart = findViewById(R.id.rastart);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        button = findViewById(R.id.button);
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        baabki = findViewById(R.id.baabki);
        currency2 = findViewById(R.id.currency2);
        wins = findViewById(R.id.wins);
        Music.musicInit(this, R.raw.for_me);
        printcurrency2();

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stavka = 0;
                printBalanse();
                button.setEnabled(true);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stavka >= 5) {
                    stavka -= 5;
                    printBalanse();
                }
            }
        });
        minus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                stavka = 5;
                printBalanse();
                return true;
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stavka += 5;
                printBalanse();
            }
        });
        plus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                stavka = summa;
                printBalanse();
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (summa >= stavka && stavka>=5 && !firstPress) {
                    startWheels();
                } else if (stavka<5 ){
                    Toast.makeText(Baraban.this, "Минимальная ставка - 5", Toast.LENGTH_SHORT).show();
                }
                else if(stavka>summa){
                    Toast.makeText(Baraban.this, "Ставка не может превышать баланс", Toast.LENGTH_SHORT).show();
                }
                else {
                    button.setEnabled(true);
                    stopsWheels();
                }
            }
        });
        handler = new Handler();
    }
        private void startWheels() {
        firstPress = true;
        if (AchievementActivity.achievementFirstBet()) AchievementActivity.showMessage(this);

        button.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(true);
            }
        }, 250);


        wheel1 = new Wheel(new Wheel.WheelListener() {
            @Override
            public void newImage(final int img) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image1.setImageResource(img);
                    }
                });
            }
        }, 15, randomLong(0, 200));
        wheel1.start();

        wheel2 = new Wheel(new Wheel.WheelListener() {
            @Override
            public void newImage(final int img) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image2.setImageResource(img);
                    }
                });
            }
        }, 15, randomLong(150, 400));
        wheel2.start();


        wheel3 = new Wheel(new Wheel.WheelListener() {
            @Override
            public void newImage(final int img) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image3.setImageResource(img);
                    }
                });
            }
        }, 15, randomLong(150, 400));
        wheel3.start();

        // Обновление интерфейса
        button.setText("Stop");

        // Запуск таймера для остановки колес
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopNextWheel();
            }
        }, 1500);}

    private void stopNextWheel() {
        if (wheel1 != null) {
            wheel1.stopWheel();
            wheel1 = null;
        } else if (wheel2 != null) {
            wheel2.stopWheel();
            wheel2 = null;
        } else if (wheel3 != null) {
            wheel3.stopWheel();
            wheel3 = null;
        }
        // Если все колеса остановлены, проверяем результаты
        if (wheel1 == null && wheel2 == null && wheel3 == null) {
            checkResults(); // Вызываем проверку результатов только когда все колеса остановлены
        } else {
            // Запускаем таймер для остановки следующего колеса
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopNextWheel();
                }
            }, 500); // Задержка между остановкой колес
        }

    }

    private void stopsWheels() {
        button.setEnabled(false);
        // Останавливаем все колеса
        if (wheel1 != null) {
            wheel1.stopWheel();
            wheel1 = null;
        }
        if (wheel2 != null) {
            wheel2.stopWheel();
            wheel2 = null;
        }
        if (wheel3 != null) {
            wheel3.stopWheel();
            wheel3 = null;
        }
    }

    private void checkResults() {
        int[] images = new int[5];
        @SuppressLint("UseCompatLoadingForDrawables") Drawable[] drawables = new Drawable[]{getResources().getDrawable(R.drawable.slot1), getResources().getDrawable(R.drawable.slot2),
                getResources().getDrawable(R.drawable.slot3), getResources().getDrawable(R.drawable.slot4), getResources().getDrawable(R.drawable.slot6)};
        Drawable[] imageViews = new Drawable[]{image1.getDrawable(), image2.getDrawable(), image3.getDrawable()};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                if (Objects.requireNonNull(imageViews[i].getConstantState()).equals(drawables[j].getConstantState())) {
                    images[j]++;
                    break;
                }
            }
        }

        int index = -1;
        for (int i = 0; i < 5; i++) {
            if (images[i] == 3){
                index = i;
                break;
            }
        }

        boolean payout = index >=0;

        if (payout) {
            // Выплачиваем ставку, если выпали три одинаковые картинки
            double winAmount = 0;


            switch (index) {
                case 0: winAmount = stavka * 20; break;
                case 1: winAmount = stavka * 40; break;
                case 2: winAmount = stavka * 30; break;
                case 3: winAmount = stavka * 50; break;
                case 4: winAmount = stavka * 60; break;
            }
            if (AchievementActivity.achievementSeven(index)) AchievementActivity.showMessage(this);
            if (AchievementActivity.achivmentThreevriad(++counter)) AchievementActivity.showMessage(this);
            // Увеличиваем баланс игрока на выигрыш
            summa += winAmount - stavka; // Вычитаем ставку, так как она уже включена в выигрыш
            Fantiki.currentFantiki = summa; // Обновляем текущий баланс игрока в базе данных
            DataBase.updateData(summa, Baraban.this, winAmount, stavka); // Обновляем данные в базе данных
            DataBase.addToHistory(stavka, winAmount, "Три в ряд");

            // Отображаем выигрыш на экране
            printcurrency2();
            printWin(winAmount);

        } else {
            counter=0;
            // Если тройка картинок не выпала, отнимаем ставку от баланса
            summa -= stavka;
            Fantiki.currentFantiki = summa; // Обновляем текущий баланс игрока в базе данных
            DataBase.updateData(summa, Baraban.this, 0, stavka); // Обновляем данные в базе данных (выигрыш = 0)
            DataBase.addToHistory(stavka, 0, "Три в ряд");


            // Отображаем проигрыш на экране
            printcurrency2();
            printWin(0);
        }


        // Возвращаем кнопке исходный текст и разрешаем ее нажатие
        firstPress = false;
        button.setEnabled(true);
        button.setText("Start");
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        Music.musicOFF();
    }
    private void printBalanse() {
        baabki.setText("" + stavka);
    }

    private void printcurrency2() {
        currency2.setText("Баланс: " + summa);
    }

    private void printWin(double wiiiin) {
        wins.setText(wiiiin + "");
    }
    public void switchSound(View v) {
        Music.musicSwitch(v);
    }
    public void navigationPanelBaraban(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout_baraban);
        drawerLayout.openDrawer(GravityCompat.START);
    }
}