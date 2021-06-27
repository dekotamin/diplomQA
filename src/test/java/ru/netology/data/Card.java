package ru.netology.data;


import lombok.Data;

@Data
public class Card {
    private String number;
    private String month;
    private String year;
    private String cvc;
    private String holder;
}