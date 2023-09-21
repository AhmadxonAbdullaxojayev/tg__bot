package org.example;


import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Getter
@Setter
public class TelegramUser {

    private String chatId;
    private String user;
    private String step;
    private String mag;
    private String fullName;
    private String selectLang;

    private String Car;
    private String carType;



    @Override
    public String toString() {
        return "TelegramUser{" +
                "chatId='" + chatId + '\'' +
                ", step='" + step + '\'' +
                ", mag='" + mag + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }

    public void execute(SendMessage message) {
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarType() {
        return carType;
    }
}
