package com.sibertech.lib.db.measure;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DipTimer {

    // System.currentTimeMillis() возвращает количество миллисекунд прошедших с полуночи 1 января 1970 года, это называется UNIX-время.
    // класс Instant обозначает момент времени с начала эпохи Unix (1970-01-01T00:00:00Z) и умеет конвертировать его в миллисекунды.
    // Вот так:
    //      Instant now = Instant.now();
    //      long countOfMillis = now.toEpochMilli();
    // то есть - System.currentTimeMillis() и класс Instant работают в одной системе координат, и время в миллисекундах взятое в один и
    // тот же момент классом Instant и System.currentTimeMillis(), должны дать один и тот же результат
    //
    // Аналогично классу Instant работает класс LocalDateTime - он также обозначает момент времени с начала эпохи Unix.
    // Но класс LocalDateTime умеет отдавать только секунды с начала эпохи Юникс, отдавать миллисекунды он не умеет.

    protected long startTime  = System.currentTimeMillis(); // начало замера времени на всю вставку
    protected long spent_time = 0L;

    public void start() {
        startTime  = System.currentTimeMillis();
    }

    public void startAt (LocalDateTime ldtStart) {
        ZoneOffset offset = OffsetDateTime.now().getOffset(); // получить ZoneOffset, установленный для операционной системы, на которой выполняется оператор
        Instant startInstant = ldtStart.toInstant(offset);

        startTime = startInstant.toEpochMilli();
    }

    public void finish() {
        spent_time  = System.currentTimeMillis() - startTime;
    }

    public int getSpentTimeMillisec() {
        return (int)spent_time;
    }


    public void test() {
        LocalDateTime ldtNow = LocalDateTime.now();
        startTime  = System.currentTimeMillis()/1000;
        Instant now = Instant.now();
        long countOfSecondsInstant = now.toEpochMilli()/1000;
        long countOfSecondsLocalDateTime = ldtNow.toEpochSecond(ZoneOffset.of("+03:00"));// установить смещение Москвы относительно UTC - "+03:00"
        ZoneOffset offset = OffsetDateTime.now().getOffset(); // получить ZoneOffset, установленный для операционной системы, на которой выполняется оператор
        long countOfSecondsLocalDateTime2 = ldtNow.toEpochSecond(offset);// смещение Москвы относительно UTC - "+03:00"

        int i = 0;
    }
}
