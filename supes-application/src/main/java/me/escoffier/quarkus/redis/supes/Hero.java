package me.escoffier.quarkus.redis.supes;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Random;

@Entity
public class Hero extends PanacheEntity {

    public String name;
    public String otherName;
    public int level;
    public String picture;

    @Column(columnDefinition = "TEXT")
    public String powers;

    public static Hero getRandomHero() {
        var count = count();

        if (count > 0) {
            var randomSupe = new Random().nextInt((int) count);
            return findAll().page(randomSupe, 1).firstResult();
        }

        // Nothing in the database
        return null;
    }

}

