package org.example.Lab07;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Lab7 {
    private static int _lineIndex;

    private static int _depth = -1;

    private static String[] _lines;

    static Scanner in = new Scanner(System.in);
    static void main() {
        try {
            Path path = Paths.get("src/main/resources/lab_7.txt");
            _lines = Files.readAllLines(path).toArray(new String[0]);
        }
        catch (Exception e) {
            System.out.println("ERROR " + e.getClass().getSimpleName());
            return;
        }

        System.out.println(_lines[0]);

        _lineIndex = 1;

        PrintTree();

        //in.nextLine();
    }

    private static void PrintTree(){
        while (_lineIndex < _lines.length) {
            var line = _lines[_lineIndex].trim();

            if(line.isEmpty()) {
                _lineIndex += 1;
                continue;
            }
            if(line.equals("END")) {
                _depth--;
                //_lineIndex++;
                return;
            }

            if(line.startsWith("BEGIN ")) {
                var name = line.substring(6);

                System.out.print(GetIndent());
                if(_lineIndex + 2 < _lines.length) {
                    if (_lines[_lineIndex + 1].trim().equals("END") &
                            _lines[_lineIndex + 2].trim().equals("END")) System.out.println("└─" + name);
                    else System.out.println("├─" + name);
                }
                else if(_lineIndex + 1 < _lines.length) {
                    System.out.println("└─" + name);
                }
                else System.out.println("├─" + name);

                _depth++;
                _lineIndex++;

                PrintTree();
            }
            _lineIndex++;
        }
    }
    private static String GetIndent() {
        var result = "";

        for (var i = 0; i <= _depth; i++) {
            result += "│ ";
        }
        return result;
    }
}
