package me.escoffier.quarkus.redis.supes;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.list.ListCommands;
import io.smallrye.mutiny.Multi;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class SupesService {

    private final ListCommands<String, FightRequest> commands;
    private final Multi<FightResult> stream;

    public SupesService(RedisDataSource dataSource, ReactiveRedisDataSource reactiveRedisDataSource) {
        commands = dataSource.list(FightRequest.class);
        stream = reactiveRedisDataSource.pubsub(FightResult.class).subscribe("fight-results")
                .broadcast().toAllSubscribers();
    }

    public FightRequest submitAFight() {
        var hero = Hero.getRandomHero();
        var villain = Villain.getRandomVillain();
        var id = UUID.randomUUID().toString();
        var request = new FightRequest(id, hero, villain);
        commands.lpush("fight-requests", request);
        return request;
    }

    public Multi<FightResult> getFightResults() {
        return stream;
    }

}
