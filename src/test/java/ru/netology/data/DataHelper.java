package ru.netology.data;


import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class DataHelper {

    public static Faker faker = new Faker(new Locale("ru"));

    public static String approvedCardNumber() {
        return "4444444444444441";
    }

    public static String declinedCardNumber() {
        return "4444444444444442";
    }

    public static String name() {
        return faker.name().fullName();
    }

    public static String validMonth() {
        String[] mouthSet = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        int validMonth = (int) (Math.random() * mouthSet.length);
        String mouth = mouthSet[validMonth];
        return mouth;
    }

    public static String setFutureYear() {
        LocalDate date = LocalDate.now().plusYears(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy");
        String year = date.format(formatter);
        return year;
    }

    public static String setEarlyYear() {
        LocalDate date = LocalDate.now().minusYears(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy");
        String year = date.format(formatter);
        return year;
    }

    public static String validYear() {
        int year = faker.number().numberBetween(22, 27);
        return Integer.toString(year);
    }

    public static String validCvc() {
        int cvcCvv = faker.number().numberBetween(100, 999);
        return Integer.toString(cvcCvv);
    }
}

