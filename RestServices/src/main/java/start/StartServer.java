package start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import repository.JdbcUtils;


@ComponentScan("games")
@ComponentScan("repository")
@SpringBootApplication
public class StartServer {
    public static void main(String[] args){
        JdbcUtils.initialize();
        SpringApplication.run(StartServer.class,args);
    }
}
