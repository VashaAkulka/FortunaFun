package com.example.roulet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.DecimalFormat;
import java.util.Random;

public class Racing extends AppCompatActivity {
    private ImageView car1, car2, car3;
    private Button raceButton;
    private Button decreaseButton;
    private Button increaseButton;
    private TextView Win2;
    private boolean raceInProgress = false;
    boolean red = false;
    boolean yellow = false;
    boolean green = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racing_game);
        Menu.MenuInit(findViewById(R.id.navigation_racing), this);
        if (AchievementActivity.achievementFirstRace()) AchievementActivity.showMessage(this);
        raceButton = findViewById(R.id.raceButton);
        decreaseButton = findViewById(R.id.decreaseButton);
        increaseButton = findViewById(R.id.increaseButton);
        car1 = findViewById(R.id.car1);
        car2 = findViewById(R.id.car2);
        car3 = findViewById(R.id.car3);
        Win2 = findViewById(R.id.win2);
        Fantiki.ViewFantiki(findViewById(R.id.balance4));
        Fantiki.ViewBet(findViewById(R.id.balance2));
        DataBase.LoadData();
        Win2.setText("0 FAN");
        raceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Fantiki.bet > Fantiki.currentFantiki) {
                    Toast.makeText(getApplicationContext(), "Недостаточно средств для ставки", Toast.LENGTH_SHORT).show();
                    return;
                }

                raceButton.setEnabled(true);
                startRace();
            }
        });
    }

    private void startRace() {
        raceInProgress = true;
        if (AchievementActivity.achievementFirstBet()) AchievementActivity.showMessage(this);
        final Random random = new Random();
        final int winnerCar = random.nextInt(3) + 1;

        Thread raceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100 && raceInProgress; i++) {
                    try {
                        Thread.sleep(100); // пауза перед перемещением
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            moveCars();
                        }
                    });

                    // Проверяем победителя
                    float car1X = car1.getX();
                    float car2X = car2.getX();
                    float car3X = car3.getX();
                    float winningPosition = convertDpToPixel(343); // Условно 250dp для победы
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.menu).setEnabled(false);
                            findViewById(R.id.buttonR).setEnabled(false);
                            findViewById(R.id.buttonY).setEnabled(false);
                            findViewById(R.id.buttonG).setEnabled(false);
                            findViewById(R.id.restartBeter).setEnabled(false);
                            findViewById(R.id.raceButton).setEnabled(false);
                        }
                    });
                    if (car1X >= winningPosition || car2X >= winningPosition || car3X >= winningPosition) {
                        raceInProgress = false; // Останавливаем гонку
                        break;
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        determineWinner();
                        raceInProgress = false;
                    }
                });


                clearBetView();
                Fantiki.ZeroBetOnColor();
            }
        });

        raceThread.start();
    }

    private void moveCars() {
        // Получаем текущие позиции машин
        float car1X = car1.getX();
        float car2X = car2.getX();
        float car3X = car3.getX();

        // Генерируем случайные значения скорости для каждой машины
        Random random = new Random();
        float car1Speed = random.nextInt(15) + 3; // Случайное число от 5 до 24
        float car2Speed = random.nextInt(15) + 3;
        float car3Speed = random.nextInt(15) + 3;

        // Увеличиваем позицию каждой машины по горизонтали с учетом случайной скорости
        car1X += car1Speed;
        car2X += car2Speed;
        car3X += car3Speed;

        // Устанавливаем новые позиции машин
        car1.setX(car1X);
        car2.setX(car2X);
        car3.setX(car3X);
    }

    private void determineWinner() {
        float car1X = car1.getX();
        float car2X = car2.getX();
        float car3X = car3.getX();
        float winningPosition = convertDpToPixel(343); // Условно 250dp для победы
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.menu).setEnabled(true);
                findViewById(R.id.buttonR).setEnabled(true);
                findViewById(R.id.buttonY).setEnabled(true);
                findViewById(R.id.buttonG).setEnabled(true);
                findViewById(R.id.restartBeter).setEnabled(true);
                findViewById(R.id.raceButton).setEnabled(true);
            }
        });
        if (car1X >= winningPosition) {
            if (red){
                Fantiki.currentFantiki += Fantiki.bet*2;
                Fantiki.win= Fantiki.bet*2;
                printwin();
                if (AchievementActivity.achievementFirstRaceWin()) AchievementActivity.showMessage(this);
            }
        } else if (car2X >= winningPosition) {
            if (yellow){
                Fantiki.currentFantiki += Fantiki.bet*2;
                Fantiki.win= Fantiki.bet*2;
                printwin();
                if (AchievementActivity.achievementFirstRaceWin()) AchievementActivity.showMessage(this);
            }
        } else if (car3X >= winningPosition) {
            if (green) {
                Fantiki.currentFantiki += Fantiki.bet*2;
                Fantiki.win= Fantiki.bet*2;
                printwin();
                if (AchievementActivity.achievementFirstRaceWin()) AchievementActivity.showMessage(this);
            }
        }
        Fantiki.ViewFantiki(findViewById(R.id.balance4));
        DataBase.updateData(Fantiki.currentFantiki, this, Fantiki.win, Fantiki.bet);
        DataBase.addToHistory(Fantiki.bet, Fantiki.win, "Гонки");
        clearBetView();
        Fantiki.ZeroBetOnColor();
        car1.setX(27);
        car2.setX(27);
        car3.setX(27);
        // Обновление интерфейса
        car1.invalidate();
        car2.invalidate();
        car3.invalidate();
        red = false;
        yellow = false;
        green = false;
    }

    private float convertDpToPixel(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
    public void changeBet(View v) {
        if (v.getId() == R.id.decreaseButton) {
            if (Fantiki.bet > 1) Fantiki.bet -= Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
        }
        else {
            double buff = Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
            if (buff + Fantiki.bet <= Fantiki.currentFantiki) Fantiki.bet += buff;
        }

        Fantiki.ViewBet(findViewById(R.id.balance2));
    }
    public void betOnColor(View v) {
        TextView betOnColor;
        int betOnColorId;
        double betOnColorValue;
        Button race = findViewById(R.id.raceButton);
        if (Fantiki.currentFantiki >= Fantiki.bet) {
            if (v.getId() == R.id.buttonR) {
                betOnColorId = R.id.textViewR;
                Fantiki.betOnRed += Fantiki.bet;
                betOnColorValue = Fantiki.betOnRed;
                red = true;
            } else if (v.getId() == R.id.buttonG) {
                betOnColorId = R.id.textViewG;
                Fantiki.betOnBlack += Fantiki.bet;
                betOnColorValue = Fantiki.betOnBlack;
                green = true;
            } else {
                betOnColorId = R.id.textViewY;
                Fantiki.betOnGreen += Fantiki.bet;
                betOnColorValue = Fantiki.betOnGreen;
                yellow = true;
            }

            betOnColor = findViewById(betOnColorId);
            betOnColor.setText("" + betOnColorValue + " FAN");

            Fantiki.currentFantiki -= Fantiki.bet;
            Fantiki.ViewFantiki(findViewById(R.id.balance4));
            race.setEnabled(true);
            race.setText("Поехали!");
        }
    }

    public void restartBete(View v) {
        Button roulette = findViewById(R.id.raceButton);
        Fantiki.currentFantiki += Fantiki.betOnBlack + Fantiki.betOnRed + Fantiki.betOnGreen;
        clearBetView();
        Fantiki.ZeroBetOnColor();
        Fantiki.ViewFantiki(findViewById(R.id.balance4));
        roulette.setEnabled(false);
        red = false;
        yellow = false;
        green = false;
    }


    public void clearBetView() {
        int[] textViewIds = {R.id.textViewR, R.id.textViewY, R.id.textViewG};
        for (int textViewId : textViewIds) {
            TextView textView = findViewById(textViewId);
            textView.setText("");
        }
    }
    public void navigationPanelRacing(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout_racing);
        drawerLayout.openDrawer(GravityCompat.START);
    }
    private void printwin() {
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedWin = df.format(Fantiki.win);
        Win2.setText(formattedWin + " FAN");
    }
}
