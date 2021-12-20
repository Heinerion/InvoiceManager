package de.heinerion;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.invoice.storage.loading.IO;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {
  public static void main(String[] args) {
    new SpringApplicationBuilder(App.class)
        .headless(false)
        .web(WebApplicationType.NONE)
        .run(args);
  }

  @Bean
  public DataBase dataBase(IO io) {
    DataBase dataBase = DataBase.getInstance();
    dataBase.setIo(io);
    dataBase.load();
    return dataBase;
  }
}
