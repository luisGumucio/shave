package com.manaco.org.entries.schedule;

import com.manaco.org.model.Ufv;
import com.manaco.org.repositories.UfvRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;

@Component
public class UfvScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UfvScheduler.class);

    @Autowired
    private UfvRepository ufvRepository;

//    @Scheduled(cron = "*/50 * * * * *")
    @Scheduled(cron = "0 0 7 * * *")
    public void reportCurrentTime() {
        URL url;
        try {
            url = new URL("https://www.bcb.gob.bo/librerias/indicadores/ufv/ultimo.php");
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.length() == 129) {
                    Ufv ufv = ufvRepository.findByCreationDate(LocalDate.now());
                    if(ufv == null) {
                        ufv = new Ufv();
                        ufv.setCreationDate(LocalDate.now());
                        BigDecimal ufvValue = new BigDecimal(line.substring(70, 77).replace(",", "."));
                        ufv.setValue(ufvValue);
                        ufvRepository.save(ufv);
                        LOGGER.info(ufvValue.toString());
                    }

                }
            }
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
