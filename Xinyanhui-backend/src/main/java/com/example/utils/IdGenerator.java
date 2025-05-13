package com.example.utils;

import java.time.Instant;
import java.util.Random;

public class IdGenerator {
    private static final Random random = new Random();
    private static Instant now;
    public static Integer generateId() {
        now = Instant.now();
        int part1= (int)now.getEpochSecond();
        int part2= random.nextInt()%1000;
        return part1+part2;
    }

}
