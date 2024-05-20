package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Miner extends AppCompatActivity {
    private TextView minCountTextView;
    private TextView winView;
    private TextView betView;
    private Button startBtn;

    private int currentMinCount;
    private GridLayout minesweeperGrid;
    private double winMultiplier = 1.0; // Начальное значение коэффициента выигрыша
    private int totalBombCount =0;
    private boolean firstClick = false;
    private boolean achivStart = false;
    private boolean updatedate = false;
    private int count =0;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private double lastX=0;
    private int countCard=0;


    @Override
    protected void onPause() {
        super.onPause();
        Music.musicOFF();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Music.musicOFF();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miner);


        Menu.MenuInit(findViewById(R.id.navigation_miner), this);
        Music.musicInit(this, R.raw.music);
        Fantiki.ViewFantiki(findViewById(R.id.balanceView));
        Fantiki.ViewBet(findViewById(R.id.betView));

        minCountTextView = findViewById(R.id.minCountTextView);
        currentMinCount = Integer.parseInt(minCountTextView.getText().toString());
        minesweeperGrid = findViewById(R.id.minesweeperGrid);
        winView = findViewById(R.id.winView);
        betView = findViewById(R.id.betView);
        startBtn = findViewById(R.id.startBtn);




        findViewById(R.id.minusMinerBtn).setOnLongClickListener(view -> {

            Fantiki.bet = 1;
            if (Fantiki.currentFantiki>=Fantiki.bet){
                startBtn.setEnabled(true);
            }
            Fantiki.ViewBet(findViewById(R.id.betView));
            return true;
        });

        findViewById(R.id.plussMinerBtn).setOnLongClickListener(view -> {
            Fantiki.bet = Fantiki.currentFantiki;
            if (Fantiki.currentFantiki>=Fantiki.bet){
                startBtn.setEnabled(true);
            }
            Fantiki.ViewBet(findViewById(R.id.betView));
            return true;
        });
        findViewById(R.id.plussMinBtn).setOnLongClickListener(view -> {
            currentMinCount = 24;

            updateMinCount(currentMinCount);
            return true;
        });
        findViewById(R.id.minusMinBtn).setOnLongClickListener(view -> {
            currentMinCount = 1;
            updateMinCount(currentMinCount);
            return true;
        });
        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (AchievementActivity.achievementFirstBet()) AchievementActivity.showMessage(Miner.this);

                    if (AchievementActivity.achievementFirstPMiner()) AchievementActivity.showMessage(Miner.this);


                    if (!firstClick){
                        if (Fantiki.currentFantiki>=Fantiki.bet){
                            countCard=0;

                            findViewById(R.id.minusMinerBtn).setEnabled(false);
                            findViewById(R.id.plussMinerBtn).setEnabled(false);
                            findViewById(R.id.minusMinBtn).setEnabled(false);
                            findViewById(R.id.plussMinBtn).setEnabled(false);
                            findViewById(R.id.startBtn).setBackgroundColor(getResources().getColor(R.color.orange));
                            startBtn.setText("Вывод"+winMultiplier*Fantiki.bet);


                            Fantiki.bet = Double.parseDouble(betView.getText().toString());
                            Fantiki.currentFantiki = Fantiki.currentFantiki - Fantiki.bet;
                            Fantiki.ViewFantiki(findViewById(R.id.balanceView));
                            firstClick= true;
                            totalBombCount = Integer.parseInt(minCountTextView.getText().toString());
                            initToStart();
                        }else {
                            showToast("Недостаточно средств");
                        }

                    } else if (countCard==0) {
                        showToast("Откройте хотя бы одну карточку! ");
                    }else {


                        firstClick = false;
                        updatedate = false;
                        startBtn.setEnabled(false);





                        openAllCardsForDuration(2000);
                        Fantiki.win = Fantiki.bet*winMultiplier;
                        Fantiki.currentFantiki = Fantiki.currentFantiki+Fantiki.win;
                        Fantiki.ViewFantiki(findViewById(R.id.balanceView));

                        winView.setText(String.valueOf(decimalFormat.format(Fantiki.win)));

                        findViewById(R.id.startBtn).setBackgroundColor(getResources().getColor(R.color.green));
                        winMultiplier=1;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // По истечении времени делаем кнопку активной
                                startBtn.setEnabled(true);
                            }
                        }, 2500);
                    }

                }




//








        });







    }
    private void updateWinMultiplier() {



        double increaseRate = 0.1; // Пример: коэффициент будет увеличиваться на 10% от числа мин на поле

        // Рассчитываем новый коэффициент
        winMultiplier = winMultiplier + (increaseRate * totalBombCount);
//        // Форматирование с двумя знаками после запятой
//        DecimalFormat df = new DecimalFormat("#.##");
//        String winMultiplierString = df.format(winMultiplier);
//        winView.setText(winMultiplierString);



    }

    private void initToStart(){


        for (int i = 0; i < minesweeperGrid.getChildCount(); i++) {
            View child = minesweeperGrid.getChildAt(i);
            if (child instanceof EasyFlipView) {
                EasyFlipView flipView = (EasyFlipView) child;
                flipView.setFlipOnTouch(true);
                flipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
                    @Override
                    public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide) {
                        if (newCurrentSide == EasyFlipView.FlipState.BACK_SIDE) {
                            flipView.setFlipOnTouch(false);
                            countCard++;


                            boolean isBomb = checkForBomb(flipView);


                            if (isBomb ) {
                                winMultiplier =1.0;
                                findViewById(R.id.startBtn).setEnabled(false);

                                handleLoss();
                            } else {
                                handleWin();
                                //startBtn.setEnabled(true);
                                if (firstClick) {
                                    startBtn.setText("Вывод:" + decimalFormat.format(winMultiplier * Fantiki.bet));
                                }
                            }

                        }
                    }
                });
            }
        }

        setBombs(Integer.parseInt(minCountTextView.getText().toString()));
    }


    private boolean checkForBomb(EasyFlipView flipView) {
        View backView = flipView.getChildAt(0);


        // Проверяем, является ли View LinearLayout (задней стороной EasyFlipView)
        if (backView instanceof LinearLayout) {
            LinearLayout backLayout = (LinearLayout) backView;

            // Получаем фон LinearLayout (задней стороны EasyFlipView)
            Drawable backgroundDrawable = backLayout.getBackground();

            // Проверяем, является ли фон бомбой (по ресурсу)
            boolean isBomb = (backgroundDrawable != null) && (backgroundDrawable.getConstantState() != null)
                    && (backgroundDrawable.getConstantState().equals(getResources().getDrawable(R.drawable.miner_bomb).getConstantState()));

           

            return isBomb;
        }

        // Если что-то пошло не так, например, на задней стороне находится ImageView
        // Можно добавить обработку этого случая или вернуть false, если это не ожидаемая структура
        return false;
    }



    // Методы для обработки победы и проигрыша
    private void handleWin() {
        // Здесь реализуйте логику для случая победы
        updateWinMultiplier();
    }
    private void handleLoss() {
        firstClick = false;
        findViewById(R.id.startBtn).setBackgroundColor(getResources().getColor(R.color.green));
        startBtn.setEnabled(false);




        // Открываем все карточки на несколько секунд
        openAllCardsForDuration(2000); // Пример: 3000 миллисекунд (3 секунды)



    }

    // Метод для открытия всех карточек на заданное время
    private void openAllCardsForDuration(long duration) {
       // findViewById(R.id.startBtn).setEnabled(false);
        for (int i = 0; i < minesweeperGrid.getChildCount(); i++) {
            View child = minesweeperGrid.getChildAt(i);
            if (child instanceof EasyFlipView) {
                EasyFlipView flipView = (EasyFlipView) child;
                if (!flipView.isBackSide()) { // Проверяем, является ли карточка закрытой
                    // Переворачиваем карточку на лицевую сторону с анимацией
                    flipView.flipTheView();
                }
            }
        }

        // Используем Handler для задержки закрытия карточек
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Закрываем все карточки
                //findViewById(R.id.startBtn).setEnabled(false);
                closeAllCards();
            }
        }, duration);



    }

    // Метод для закрытия всех карточек
    private void closeAllCards() {


        for (int i = 0; i < minesweeperGrid.getChildCount(); i++) {
            View child = minesweeperGrid.getChildAt(i);
            if (child instanceof EasyFlipView) {
                EasyFlipView flipView = (EasyFlipView) child;
                if (!flipView.isFrontSide()) { // Проверяем, является ли карточка открытой
                    // Переворачиваем карточку на обратную сторону с анимацией
                    flipView.flipTheView();

                    // Получаем LinearLayout (заднюю сторону карточки)
                    View backView = flipView.getChildAt(0); // Второй дочерний элемент - это задняя сторона
                    if (backView instanceof LinearLayout) {
                        LinearLayout backLayout = (LinearLayout) backView;

                        // Возвращаем исходный фон
                        backLayout.setBackgroundResource(R.drawable.miner_coin);
                    }
                }
            }
        }

        winMultiplier=1;
        startBtn.setText("Старт");
       // startBtn.setEnabled(false);
        findViewById(R.id.minusMinerBtn).setEnabled(true);
        findViewById(R.id.plussMinerBtn).setEnabled(true);
        findViewById(R.id.minusMinBtn).setEnabled(true);
        findViewById(R.id.plussMinBtn).setEnabled(true);

        if (!updatedate){
            Fantiki.win = new BigDecimal(Fantiki.win).setScale(2, RoundingMode.HALF_UP).doubleValue();
            DataBase.addToHistory(Fantiki.bet,Fantiki.win,"Сапер");
            DataBase.updateData(Fantiki.currentFantiki,Miner.this,Fantiki.win,Fantiki.bet);
            updatedate=true;
        }
        startBtn.setEnabled(true);





    }





    // Метод для отображения всплывающего сообщения
    private void showToast(String message) {
        Log.d("Miner", "showToast: " + message);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    public void changeBet(View v) {
        if (v.getId() == R.id.minusMinerBtn) {
            if (Fantiki.bet > 1) Fantiki.bet -= Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
            if (Fantiki.currentFantiki>=Fantiki.bet){
                startBtn.setEnabled(true);
            }
        }
        else {
            double buff = Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
            if (buff + Fantiki.bet <= Fantiki.currentFantiki) Fantiki.bet += buff;
            if (Fantiki.currentFantiki>=Fantiki.bet){
                startBtn.setEnabled(true);
            }
        }

        Fantiki.ViewBet(findViewById(R.id.betView));
    }

    public void changeMin(View v) {
        if (minCountTextView == null) {
            Log.e("Miner", "minCountTextView is null");
            return;
        }

        try {

            if (v.getId() == R.id.minusMinBtn) {
                if (currentMinCount > 1) {
                    currentMinCount--;
                    updateMinCount(currentMinCount);
                }
            } else if (currentMinCount < 24) {
                currentMinCount++;
                updateMinCount(currentMinCount);
            }
        } catch (NumberFormatException e) {
            // Handle the case where the text cannot be parsed to an integer
            Log.e("Miner", "Error parsing text to integer: " + e.getMessage());
        }
    }

    private void updateMinCount(int newMinCount) {
        if (minCountTextView != null) {
            minCountTextView.setText(String.valueOf(newMinCount));
            // Additional logic or actions based on the new count
        } else {
            Log.e("Miner", "minCountTextView is null in updateMinCount");
        }
    }

    public void navigationPanel(View v) {
        //restartBet(v);
        DrawerLayout drawerLayout = findViewById(R.id.miner);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    // Функция для установки бомбы для заданного количества мин
    private void setBombs(int bombCount) {
        List<View> linearLayouts = new ArrayList<>();

        // Находим все LinearLayout в GridLayout
        for (int i = 0; i < minesweeperGrid.getChildCount(); i++) {
            View child = minesweeperGrid.getChildAt(i);
            if (child instanceof EasyFlipView) {
                EasyFlipView flipView = (EasyFlipView) child;
                for (int j = 0; j < flipView.getChildCount(); j++) {
                    View flipViewChild = flipView.getChildAt(j);
                    if (flipViewChild instanceof LinearLayout) {
                        linearLayouts.add(flipViewChild);
                    }
                }
            }
        }

        // Проверяем, достаточно ли LinearLayout для установки бомб
        if (bombCount > linearLayouts.size()) {
            // Обработка ошибки: количество мин превышает количество LinearLayout
            return;
        }

        Collections.shuffle(linearLayouts);


        // Устанавливаем бомбы для первых bombCount LinearLayout
        for (int i = 0; i < bombCount; i++) {
            LinearLayout linearLayout = (LinearLayout) linearLayouts.get(i);
            linearLayout.setBackgroundResource(R.drawable.miner_bomb);


        }

    }


}
