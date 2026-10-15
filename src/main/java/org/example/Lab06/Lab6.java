package org.example.Lab06;

import java.util.ArrayList;
import java.util.Scanner;

public class Lab6 {
    private static final String[] heights = {"▁", "▂", "▃", "▄", "▅", "▆", "▇", "█"};
    static ArrayList<String> chart_cpu = new ArrayList<>();
    static ArrayList<String> chart_ram = new ArrayList<>();
    static ArrayList<String> chart_temperature = new ArrayList<>();

    public static void main(String[] args) {
        ServerMonitor monitor = new ServerMonitor();

        monitor.addDataListener(Lab6::onDataReceived);
        monitor.start();

        System.out.println("Мониторинг запущен.");
        System.out.println("Для завершения программы нажмите Enter.");

        new Scanner(System.in).nextLine();
        monitor.stop();
    }

    private static void onDataReceived(
            double cpu, double ram, double temperature) {

        chart_cpu = array_add_element(chart_cpu, cpu);
        chart_ram = array_add_element(chart_ram, ram);
        chart_temperature = array_add_element(chart_temperature, temperature);

        for (int i = 0; i < 10; i++) {
            System.out.println();
        }

        System.out.println("CPU: " + cpu + "%  ");
        print_array(chart_cpu);
        System.out.println();

        System.out.println("RAM: " + ram + "%  ");
        print_array(chart_ram);
        System.out.println();

        System.out.println("Temperature: " + temperature + " °C  ");
        print_array(chart_temperature);
        System.out.println();
    }

    static void print_array(ArrayList<String> arr) {
        for (String i : arr) System.out.print(i);
    }

    static ArrayList<String> array_add_element(ArrayList<String> arr, double new_element) {
        if (arr.size() > 32) arr.removeFirst();
        arr.add(convert_to_element(new_element));
        return arr;
    }
    static String convert_to_element(double element) {
        if(element < 12.5) return "▁";
        if(element < 25) return "▂";
        if(element < 37.5) return "▃";
        if(element < 50) return "▄";
        if(element < 62.5) return "▅";
        if(element < 75) return "▆";
        if(element < 87.5) return "▇";
        return "█";
    }

}
