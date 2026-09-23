package org.example;

import java.util.Scanner;

public class Lab2 {;

    static void main() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите количество игроков:");
        int players = in.nextInt();
        System.out.println("Введите количество оперативной памяти:");
        int RAM = in.nextInt();
        System.out.println("У вас есть пароль? Введите 1 если есть");
        in.nextLine();
        String p_e = in.nextLine();
        boolean password_exists = false;
        if(p_e.contains("1")) {
            password_exists = true;
        }
        System.out.println(checkConfiguration(players, RAM, password_exists));
    }
    public static String checkConfiguration(int players, int RAM, boolean password_exists) {
        String res = "";
        boolean error = false;
        if(players==0){
            error = true;
            res+="Запуск невозможен: количество игроков должно быть больше нуля.";
        }
        if(RAM<8) {
            error = true;
            res+="Запуск невозможен: серверу недостаточно оперативной памяти.";
        }
        if(error==false) {
            if (players / RAM > 6) {
                res+="Запуск возможен с предупреждением: для такого количества игроков рекомендуется больше оперативной памяти.";
            }
            if (password_exists) {
                res+="Запуск возможен с предупреждением: публичный сервер защищён паролем.";
            }
            res+="Запуск возможен";
        }
        return res;
    }
}
