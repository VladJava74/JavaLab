package org.example.Lab05;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class SaveData {
    public String Character;
    public String Map;
    public Position Position;
    public int Health;
    public List<String> Inventory;

    public void save_Character(String character) {
        this.Character = character;
    }

    public void save_Map(String map) {
        this.Map = map;
    }

    public void save_Position(Position position) {
        this.Position = position;
    }

    public void save_Health(int health) {
        this.Health = health;
    }
    public void save_Inventory(List<String> inventory) {
        this.Inventory = inventory;
    }

    @JsonIgnore
    public String get_Character() {
        return Character;
    }

    @JsonIgnore
    public String get_Map() {
        return Map;
    }

    @JsonIgnore
    public Position get_Position() {
        return Position;
    }

    @JsonIgnore
    public int get_Health() {
        return Health;
    }

    @JsonIgnore
    public List<String> get_Inventory() {
        return Inventory;
    }
}