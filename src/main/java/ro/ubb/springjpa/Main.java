package ro.ubb.springjpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.springjpa.service.ClientServiceImpl;
import ro.ubb.springjpa.ui.Console;

/**
 * author: radu
 */
public class Main {
    public static void main(String[] args)
    {
        Logger log = LoggerFactory.getLogger(Main.class);
        log.trace("Start...");

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        "ro.ubb.springjpa"
                );

        context.getBean(Console.class).run();
        log.trace("End...");
    }
}
