package com.example.weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReader {

    public static FileReader getInstance() {
        return new FileReader();
    }

    public  StringBuilder readFile(String fileName) throws IOException {
        FileInputStream inputStream = null;
        StringBuilder out = new StringBuilder();
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            File configFile = new File(classLoader.getResource(fileName).getFile());
            inputStream = new FileInputStream(configFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
        return out;
    }

}
