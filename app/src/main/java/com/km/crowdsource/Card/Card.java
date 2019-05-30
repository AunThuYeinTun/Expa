package com.km.crowdsource.Card;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Card {
    private String title;
    private int cardImage;

    public Card() {
    }

    public Card(String title, int cardImage) {
        this.title = title;
        this.cardImage = cardImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getCardImage() {
        return cardImage;
    }

    public void setCardImage(int cardImage) {
        this.cardImage = cardImage;
    }
}
