package com.example.roulet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifImageView;


import pl.droidsonroids.gif.GifDrawable;

public class FlyPlane extends AppCompatActivity {
    private AlertDialog alertDialog;
    private ImageView historyPrice;
    private float[] myVector = {};
    private TableLayout tableLayout;
    private float generatedValue;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");







    private TextView betView;
    private TextView balanceView;
    private TextView winView;
    private GifImageView gif;
    private pl.droidsonroids.gif.GifDrawable gifDrawable;



    private int currentBetIndex = 0;
    private List<Integer> betValues = Arrays.asList(1, 2, 5, 10);
    private Button startButton;
    boolean buttonClicked = false; // Переменная для отслеживания нажатия кнопки
    boolean isWinHandled  = false; // Переменная для отслеживания только одного вывода при победе
    boolean achivment  = false;
    boolean firstWin  = false;
    boolean valuesAdded = false;

    float bet;
    float winValue;
    private TextView prov;
    private TextView fon_View;














    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly_plane);


        Menu.MenuInit(findViewById(R.id.navigation_flyplane), this);

        Fantiki.ViewFantiki(findViewById(R.id.balanceView));
        Fantiki.ViewBet(findViewById(R.id.betView));


        startButton = findViewById(R.id.startButton);
        Button btn_1 = findViewById(R.id.btn_1);
        Button btn_2 = findViewById(R.id.btn_2);
        Button btn_5 = findViewById(R.id.btn_5);
        Button btn_10 = findViewById(R.id.btn_10);
        Button plussBtn = findViewById(R.id.plussBtn);
        Button minusBtn = findViewById(R.id.minusBtn);


        fon_View = findViewById(R.id.fon_View);
        betView = findViewById(R.id.betView);
        balanceView = findViewById(R.id.balanceView);
        winView = findViewById(R.id.winView);
        gif = findViewById(R.id.gif);
        gifDrawable = (pl.droidsonroids.gif.GifDrawable) gif.getDrawable();
        gifDrawable.stop();
        historyPrice = findViewById(R.id.historyPrice);
        MusicPlayer.playMusic(FlyPlane.this,R.raw.lovemusic,true);



        // Изменение ставки

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Fantiki.bet=1;
               Fantiki.ViewBet(findViewById(R.id.betView));
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fantiki.bet=2;
                Fantiki.ViewBet(findViewById(R.id.betView));
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fantiki.bet=5;
                Fantiki.ViewBet(findViewById(R.id.betView));
            }
        });

        btn_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fantiki.bet=10;
                Fantiki.ViewBet(findViewById(R.id.betView));
            }
        });
        findViewById(R.id.minusBtn).setOnLongClickListener(view -> {
            Fantiki.bet = 1;
            Fantiki.ViewBet(findViewById(R.id.betView));
            return true;
        });

        findViewById(R.id.plussBtn).setOnLongClickListener(view -> {
            Fantiki.bet = Fantiki.currentFantiki;
            Fantiki.ViewBet(findViewById(R.id.betView));
            return true;
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!buttonClicked) {
                    if (Fantiki.currentFantiki>=Fantiki.bet){
                        handleFirstButtonClick();

                    }else {
                        showToast("Недостаточно средств");
                    }
                } else if (!isWinHandled) {
                    if (Double.parseDouble(fon_View.getText().toString().replace(",","."))!=1){
                        handleSecondButtonClick();
                    }
                }
            }
        });
        startButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (startButton.getText().toString().equals("Зажмите для отмены")){
                    showToast("Отмена");
                    Fantiki.currentFantiki = Fantiki.currentFantiki+Fantiki.bet;
                    Fantiki.ViewFantiki(findViewById(R.id.balanceView));
                   // balanceView.setText(String.valueOf(Float.parseFloat(balanceView.getText().toString()) + bet));
                    startButton.setEnabled(false);


                }
                return false;
            }
        });


        historyPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistoryDialog();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayer.stopMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicPlayer.stopMusic();

    }

    private Timer timer;
    private TimerTask timerTask;

    private void updateValuePeriodically(final TextView fon_View, final float generatedValue) {
        // Останавливаем предыдущий таймер, если он был запущен
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        // Создаем новый таймер
        timer = new Timer();
        timerTask = new TimerTask() {
            float currentValue = 1;//Float.parseFloat(fon_View.getText().toString());

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Если текущее значение еще не достигло сгенерированного значения
                        if (currentValue < generatedValue) {
                            currentValue += 0.01f;
                            fon_View.setText(String.format("%.2f", currentValue));
                        } else {
                            isWinHandled = false;
                            loseAnim();
                            fon_View.setText("1");
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                        }
                    }
                });
            }
        };

        // Запускаем новый таймер
        timer.schedule(timerTask, 0, 50); // 0 миллисекунд - задержка перед первым запуском, 10 миллисекунд - интервал между запусками
    }



    private void loseAnim() {
        if (!valuesAdded) {
            updateVector(generatedValue);
            valuesAdded = true;
        }




        MusicPlayer.stopMusic();
        MusicPlayer.playMusic(FlyPlane.this,R.raw.fail,false);


        // Останавливаем текущую анимацию и освобождаем ресурсы
        gifDrawable.stop();
        gifDrawable.recycle();

        // Загружаем и устанавливаем новую анимацию
        try {
            gifDrawable = new pl.droidsonroids.gif.GifDrawable(getResources(), R.drawable.fly2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gif.setImageDrawable(gifDrawable);


        // Устанавливаем слушатель событий анимации
        gifDrawable.addAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationCompleted(int loopNumber) {
                // Вызываем метод для запуска следующей анимации (fly1)
                startNextAnimation();
            }
        });

        // Запускаем новую анимацию
        gifDrawable.start();

    }

    // Метод для запуска следующей анимации (fly1)
    private void startNextAnimation() {
        // Останавливаем текущую анимацию и освобождаем ресурсы
        gifDrawable.stop();
        gifDrawable.recycle();

        // Загружаем и устанавливаем анимацию fly1
        try {
            gifDrawable = new GifDrawable(getResources(), R.drawable.fly1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gif.setImageDrawable(gifDrawable);

        // Запускаем анимацию fly1
        gifDrawable.stop();
        startButton.setText("Старт");
        startButton.setEnabled(true);


        buttonClicked = false;

        valuesAdded = false; // Сбрасываем флаг при старте новой анимации
        Fantiki.win = new BigDecimal(Fantiki.win).setScale(2, RoundingMode.HALF_UP).doubleValue();
        DataBase.addToHistory(Fantiki.bet,Fantiki.win,"Самолет");
        DataBase.updateData(Fantiki.currentFantiki,FlyPlane.this,Fantiki.win,Fantiki.bet);


        MusicPlayer.stopMusic();
        MusicPlayer.playMusic(FlyPlane.this,R.raw.lovemusic,true);

    }








    // Метод для отображения Toast сообщения
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    //Обновление bet_View
    private void updateBetView() {
        int currentBet = betValues.get(currentBetIndex);
        betView.setText(String.valueOf(currentBet));
    }


    private float generateWeightedRandomValue() {
        double random = Math.random();

        if (random < 0.8) { // 70% вероятность
            return (float) (Math.random() * 2) + 1; // от 1 до 3
        } else if (random < 0.9) { // 20% вероятность (после исключения 70%)
            return (float) (Math.random() * 4) + 3; // от 3 до 7
        } else if (random < 0.98) { // 8% вероятность (после исключения 90%)
            return (float) (Math.random() * 3) + 7; // от 7 до 10
        } else { // 2% вероятность (после исключения 98%)
            return (float) (Math.random() * 5) + 10; // свыше 10
        }
    }

    public void showHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.history_dialog_layout, null);
        builder.setView(dialogView);

        // Добавьте вашу логику для обработки элементов в диалоге (например, истории)

        alertDialog = builder.create();
        alertDialog.show();
        setButtonValues(myVector);



    }
    private void updateVector(float newValue) {
        // Максимальное количество элементов в векторе
        int maxVectorSize = 9;
        newValue = Float.parseFloat(String.format("%.2f", newValue).replace(',', '.'));

        // Если вектор еще не полон, просто добавляем новый элемент
        if (myVector.length < maxVectorSize) {
            float[] newVector = new float[myVector.length + 1];
            System.arraycopy(myVector, 0, newVector, 0, myVector.length);
            newVector[myVector.length] = newValue;
            myVector = newVector;
        } else {
            // Если вектор полон, удаляем самый старый элемент и сдвигаем остальные
            for (int i = 0; i < maxVectorSize - 1; i++) {
                myVector[i] = myVector[i + 1];
            }
            myVector[maxVectorSize - 1] = newValue;
        }
    }
    private void setButtonValues(float[] values) {
        tableLayout = alertDialog.findViewById(R.id.historyTable);

        // Перебираем строки
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            View rowView = tableLayout.getChildAt(i);

            if (rowView instanceof TableRow) {
                TableRow row = (TableRow) rowView;

                // Перебираем кнопки в строке
                for (int j = 0; j < row.getChildCount(); j++) {
                    View buttonView = row.getChildAt(j);

                    if (buttonView instanceof Button) {
                        Button button = (Button) buttonView;

                        // Устанавливаем видимость только для кнопок, у которых есть соответствующее значение в векторе
                        if (i * row.getChildCount() + j < values.length) {
                            float buttonValue = values[i * row.getChildCount() + j];
                            button.setText(String.valueOf(buttonValue)+"x");
                            setButtonColor(button, buttonValue);
                            button.setVisibility(View.VISIBLE);
                        } else {
                            // Если значения в векторе закончились, скрываем оставшиеся кнопки
                            button.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        }
    }
    private void setButtonColor(Button button, float value) {
        if (value >= 1 && value < 5) {
            // Синий цвет
            button.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        } else if (value >= 5 && value < 10) {
            // Красный цвет
            button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        } else {
            // Зеленый цвет
            button.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
    }


    //startbtn
    private boolean isCountdownRunning = false;


    private void handleFirstButtonClick() {
        if (!achivment ){

            if (AchievementActivity.achievementFirstPlane()) AchievementActivity.showMessage(this);
            if (AchievementActivity.achievementFirstBet()) AchievementActivity.showMessage(this);

            achivment =true;
        }
        // Проверяем, что обратный отсчет не запущен
        if (!isCountdownRunning) {
            MusicPlayer.stopMusic();
            MusicPlayer.playMusic(FlyPlane.this,R.raw.fonmusic,true);
            // Устанавливаем, что обратный отсчет запущен
            isCountdownRunning = true;
           // bet = Float.parseFloat(betView.getText().toString());
            Fantiki.currentFantiki = Fantiki.currentFantiki - Fantiki.bet;
            Fantiki.ViewFantiki(findViewById(R.id.balanceView));


            // Запускаем обратный отсчет
            startCountdown(new Runnable() {


                @Override
                public void run() {


                    startButton.setText("Вывод");
                    startButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));



                    // Обработка завершения обратного отсчета
                    isCountdownRunning = false; // Сбрасываем флаг после завершения
                    // Ваш код
                    generatedValue = generateWeightedRandomValue();
                    gifDrawable.start();
                    buttonClicked = true;



                    updateValuePeriodically(fon_View, generatedValue);
                }
            });
        }
    }


    private void handleSecondButtonClick() {
        if (!firstWin){
            if (AchievementActivity.achievementFirstWinPlane()) AchievementActivity.showMessage(this);
            firstWin= true;
        }
        try {
            Fantiki.win = Fantiki.bet * Double.parseDouble(fon_View.getText().toString().replace(",", "."));
           winView.setText(String.valueOf(decimalFormat.format(Fantiki.win)));

            if (Double.parseDouble(fon_View.getText().toString().replace(",", ".")) > 10) {
                if (AchievementActivity.achievementCrash()) AchievementActivity.showMessage(this);
            }


            Fantiki.currentFantiki= Fantiki.currentFantiki+Fantiki.win;
            Fantiki.ViewFantiki(findViewById(R.id.balanceView));
            DataBase.updateData(Fantiki.currentFantiki,FlyPlane.this,Double.parseDouble(String.valueOf(decimalFormat.format(Fantiki.win)).toString().replace(",",".")),Fantiki.bet);

            //showToast("вывод:" + Fantiki.win);
            isWinHandled = true;

        } catch (NumberFormatException e) {
            showToast("Ошибка при вычислении выигрыша: " + fon_View.getText().toString());
            e.printStackTrace();
            isWinHandled = true;


        }
    }
    private void startCountdown(final Runnable callback) {
        startButton.setText("Зажмите для отмены");
        startButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F38500")));



        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = (millisUntilFinished / 1000) + 1; // Добавляем 1, чтобы начать с 3
                fon_View.setText(String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                fon_View.setText("GO!");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fon_View.setText(""); // Очистим текстовое поле после паузы
                        if (callback != null) {
                            callback.run();
                        }
                    }
                }, 2000); // 2 секунды паузы после "GO!"
            }
        }.start();
    }
    public void navigationPanel(View v) {
        //restartBet(v);
        DrawerLayout drawerLayout = findViewById(R.id.flyplane);
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void changeBet(View v) {
        if (v.getId() == R.id.minusBtn) {
            if (Fantiki.bet > 1) Fantiki.bet -= Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
        }
        else {
            double buff = Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
            if (buff + Fantiki.bet <= Fantiki.currentFantiki) Fantiki.bet += buff;
        }

        Fantiki.ViewBet(findViewById(R.id.betView));
    }



}





