package com.example.crud;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class DrinkDAO {
    public List<Drink> loadFromRAR(InputStream is) throws Exception {
        List<Drink> result = new ArrayList<>();
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            String s;
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            Drink dto;
            while ((s = br.readLine()) != null) {
                String[] tmp = s.split("-");
                dto = new Drink(Integer.parseInt(tmp[0]), tmp[1], Float.parseFloat(tmp[2]),Boolean.parseBoolean(tmp[3]),tmp[4]);
                result.add(dto);
            }
        }
        finally {
            try {
                if(br != null) br.close();
                if(isr != null) isr.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public List<Drink> loadFromInternal(FileInputStream fis) throws Exception {
        List<Drink> result = new ArrayList<>();
        String s = "";
        Drink drink = null;

        try(InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr)) {
            while((s = br.readLine()) != null) {
                String[] tmp = s.split("-");
                drink = new Drink(
                        Integer.parseInt(tmp[0]),
                        tmp[1],
                        Float.parseFloat(tmp[2]),
                        Boolean.parseBoolean(tmp[3]),
                        tmp[4]
                );
                result.add(drink);
            }
        }
        catch (Exception e) {
            Log.i("Error loading", e.getMessage());
        }
        return result;
    }

    public void saveToInternal(FileOutputStream fos, List<Drink> lst) {
        try(OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            String result = "";
            for(Drink d : lst) {
                result += d.toString() + "\n";
            }
            osw.write(result);
            osw.flush();
        }
        catch (Exception e) {
            Log.i("Error saving file", e.getMessage());
        }
    }
}
