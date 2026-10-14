package org.example.Lab05;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class SaveEntry {

    public String name;
    public String file;
    public String updatedAt;

    public SaveEntry() {
    }

    public SaveEntry(String name, String file, String updatedAt) {
        this.name = name;
        this.file = file;
        this.updatedAt = updatedAt;
    }

    public void saves_name(String name) {
        this.name = name;
    }
    public void saves_file(String file) {
        this.file = file;
    }
    public void saves_updatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonIgnore
    public String get_name() {
        return name;
    }
    @JsonIgnore
    public String get_file() {
        return file;
    }
    @JsonIgnore
    public String get_updatedAt() {
        return updatedAt;
    }
}
