package com.manaco.org.entries.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileRead {


    public static void main(String args []) {
        List<String> move = new ArrayList<>();
        long start = new Date().getTime();

        String fileName = "/Users/gumu/Documents/documentManaco/move.txt"; //this path is on my local
        try (BufferedReader fileBufferReader = new BufferedReader(new FileReader(fileName))) {
            String fileLineContent;
            while ((fileLineContent = fileBufferReader.readLine()) != null) {
                System.out.println(fileLineContent);
                move.add(fileLineContent);
            }
            fileBufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = new Date().getTime();

        long time = (long) (end - start);
        System.out.println("BufferedReader Time Consumed => " + time);
        System.out.println("total Time Consumed => " + move.size());
    }
}


