package org.example;

import java.util.Scanner;

public class Lab0 {
    static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("\n===== Создание персонажа =====");
        System.out.print("Введите имя: ");
        String name = in.nextLine();

        System.out.print("Введите уровень: ");
        int level = in.nextInt();
        in.nextLine();

        System.out.print("Введите гендер: ");
        String gender = in.nextLine();

        System.out.print("Введите Возраст: ");
        int age = in.nextInt();
        in.nextLine();

        System.out.print("Введите раса: ");
        String race = in.nextLine();

        System.out.print("Введите класс: ");
        String Class = in.nextLine();

        Person person = new Person(name, level, gender, age, race,Class);
        person.printPersonProfile();
    }
}
class Person{

    String name;
    int level;
    String gender;
    int age;
    String race;
    String Class;

    public Person(String name, int level, String gender, int age, String race,
                  String Class){

        this.name = name;
        this.level = level;
        this.gender = gender;
        this.age = age;
        this.race = race;
        this.Class = Class;

    }

    public void printPersonProfile() {
        System.out.println("\n===== ПРОФИЛЬ ПЕРСОНАЖА =====");
        System.out.println("Имя: " + name);
        System.out.println("Уровень: " + level);
        System.out.println("гендер: " + gender);
        System.out.println("Возраст: " + age);
        System.out.println("раса: " + race);
        System.out.println("Класс: " + Class);
    }
}