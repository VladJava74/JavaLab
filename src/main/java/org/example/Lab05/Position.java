package org.example.Lab05;

public class Position {
    public int X;
    public int Y;

    public Position() {
        // Нужен Jackson для десериализации
    }

    public Position(int x, int y) {
        this.X = x;
        this.Y = y;
    }
}
