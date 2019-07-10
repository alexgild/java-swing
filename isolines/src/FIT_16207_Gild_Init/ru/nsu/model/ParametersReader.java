package FIT_16207_Gild_Init.ru.nsu.model;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ParametersReader {
    private ArrayList<Color> colors;
    private Color isolinesColor;

    public ParametersReader(String fileName) {
        try(BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
            colors = new ArrayList<>();
            String line;
            line = bf.readLine();
            //k
            int xCountGrid = Integer.parseInt(line.split(" ")[0]);
            //m
            int yCountGrid = Integer.parseInt(line.split(" ")[1]);

            //n
            int levels = Integer.parseInt(bf.readLine());

            for(int i = 0; i <= levels; i++) {
                line = bf.readLine();
                int red = Integer.parseInt(line.split(" ")[0]);
                int green = Integer.parseInt(line.split(" ")[1]);
                int blue = Integer.parseInt(line.split(" ")[2]);
                colors.add(new Color(red, green, blue));
            }
            line = bf.readLine();
            int red = Integer.parseInt(line.split(" ")[0]);
            int green = Integer.parseInt(line.split(" ")[1]);
            int blue = Integer.parseInt(line.split(" ")[2]);
            isolinesColor = new Color(red, green, blue);

            if(!(bf.readLine().equals("EOF"))) {
                throw new IOException();
            }

        } catch (IOException e) {
            System.out.println("Error while parameters reading");
        }
    }

    ArrayList<Color> getColors() {
        return this.colors;
    }

    public Color getIsolinesColor() {
        return this.isolinesColor;
    }
}
