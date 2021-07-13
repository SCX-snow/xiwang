package dream;

import java.util.Random;

import static java.lang.Thread.sleep;

public class lasttime {
    public static void last()
    {
        Random rand = new Random();
        try {
            sleep(rand.nextInt(500)+1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
