package me.escoffier.quarkus.redis.fight;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.list.KeyValue;
import io.quarkus.redis.datasource.list.ListCommands;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.time.Duration;
import java.util.Random;

import static java.lang.Thread.sleep;

@ApplicationScoped
public class FightSimulator implements Runnable {


    private final PubSubCommands<FightResult> publisher;
    private final String name;
    private final Logger logger;
    private final ListCommands<String, FightRequest> queue;

    private volatile boolean stopped = false;

    public FightSimulator(@ConfigProperty(name = "simulator-name") String name, Logger logger, RedisDataSource ds) {
        this.name = name;
        this.logger = logger;
        this.publisher = ds.pubsub(FightResult.class);
        this.queue = ds.list(FightRequest.class);
    }

    public void start(@Observes StartupEvent ev) {
        new Thread(this).start();
    }

    public void stop(@Observes ShutdownEvent ev) {
        stopped = true;
    }

    @Override
    public void run() {
        logger.infof("Simulator %s starting", name);
        while ((!stopped)) {
            KeyValue<String, FightRequest> item = queue.brpop(Duration.ofSeconds(1), "fight-requests");
            if (item != null) {
                var request = item.value();
                logger.infof("Simulator %s is going to simulate a fight between %s and %s", name, request.hero().name, request.villain().name);
                var result = simulate(request);
                publisher.publish("fight-results", result);

            }
        }
    }

    public FightResult simulate(FightRequest request) {
        Random random = new Random();
        var newHeroLevel = request.hero().level + random.nextInt(100);
        var newVillainLevel = request.villain().level + random.nextInt(100);

        var durationOfTheFight = random.nextInt(5000) + 1;

        try {
            sleep(durationOfTheFight);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (newHeroLevel >= newVillainLevel) {
            return new FightResult(request.id(), request.hero(), request.villain(), request.hero().name);
        } else {
            return new FightResult(request.id(), request.hero(), request.villain(), request.villain().name);
        }
    }


}
