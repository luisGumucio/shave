package com.manaco.org.entries;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class sample {

    public static void main( String[] args )
    {
        URL url;

        try {
            url = new URL("https://www.bcb.gob.bo/librerias/indicadores/ufv/ultimo.php");
            URLConnection conn = url.openConnection();
            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.length() == 129) {
                    System.out.println(line.substring(70, 77));
                }
                System.out.println(line.length());

            }
            br.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
