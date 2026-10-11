package org.example.Lab04;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry{
    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public LocalDateTime Timestamp;
    public String Level;
    public String Category;
    public String Message;
    public LogEntry(String Timestamp, String Level, String Category, String Message) {
        this.Timestamp = LocalDateTime.parse(Timestamp, formatter);
        this.Level = Level;
        this.Category = Category;
        this.Message = Message;
    }
}
