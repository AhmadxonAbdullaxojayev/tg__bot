package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import java.io.File;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class MyBot extends TelegramLongPollingBot {
    List<TelegramUser> users = new ArrayList<>();


    public MyBot() {
        super("6494265943:AAHiPDr_vuIAGh6ZNyD0zrFU_4_cizYAsdY");
    }

    TelegramUser user = new TelegramUser();

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage()) {
            String chatId = update.getMessage().getChatId().toString();
            TelegramUser user = saveUser(chatId);
            Message message = update.getMessage();
            System.out.println("user step => " + user.getStep());
            if (message.hasText()) {
                String text = message.getText();
                if (text.equals("/list")) {
                    System.out.println(users);
                } else if (text.equals("/start")) {
                    if (user.getFullName() != null) {
                        try {
                            setLang(chatId, user);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Assalomu Alekum botka hush kelibsiz\n" +
                                "ILTIMOS ISIM FAMILIYANGIZNI KIRITING\n\n" +
                                "Привет, Алекум Ботка, добро пожаловать\n" +
                                "ПОЖАЛУЙСТА, ВВЕДИТЕ СВОЕ ИМЯ И ФАМИЛИЮ");


                        sendMessage.setChatId(chatId);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        user.setStep(BotConstant.ENTER_NAME);
                    }

                } else if (user.getStep().equals(BotConstant.ENTER_NAME)) {

                    try {
                        user.setFullName(text);
                        setLang(chatId, user);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
            }


//            }  else if (update.hasCallbackQuery()){
//            String chatId = String.valueOf(Long.valueOf(update.getCallbackQuery().getFrom().getId().toString()));
//            String data = update.getCallbackQuery().getData();
//            TelegramUser user = saveUser(chatId);
//            if (user.getStep().equals(BotConstant.SELECT_LANG)){
//                user.setSelectLang(BotQuery.UZ_SELECT);
//                if (data.equals(BotQuery.UZ_SELECT)){
//                    setText(chatId,"Habaringizni qoldiring ");
//                } else if (data.equals(BotQuery.RU_SELECT)) {
//                    user.setSelectLang(BotQuery.RU_SELECT);
//                    setText(chatId,"Оставьте свое сообщение ");
//                }
//                user.setStep(BotConstant.WRITE_MSG);
//            }


        } else if (update.hasCallbackQuery()) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            TelegramUser user = getMe(chatId);

            System.out.println(user.getStep());
            if (update.getCallbackQuery().getData().contains("LANG")) {

                try {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("Iltimos mashina turini tanlang.\n\n" +
                            "Пожалуйста, выберите тип машины.");
                    sendMessage.setChatId(chatId);

                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

                    List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButtonUZ = new InlineKeyboardButton();

                    inlineKeyboardButtonUZ.setText("BMW \uD83D\uDCB8");
                    inlineKeyboardButtonUZ.setCallbackData(BotQuery.BMW_SELECT);


                    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();

                    inlineKeyboardButton.setText("MERSADES \uD83D\uDCB8");
                    inlineKeyboardButton.setCallbackData(BotQuery.MERCEDES_SELECT);

                    inlineKeyboardButtons.add(inlineKeyboardButtonUZ);
                    inlineKeyboardButtons.add(inlineKeyboardButton);

                    List<List<InlineKeyboardButton>> tr = new ArrayList<>();
                    tr.add(inlineKeyboardButtons);
                    inlineKeyboardMarkup.setKeyboard(tr);

                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);

                    execute(sendMessage);


                    user.setStep(BotConstant.SELECT_CAR_TYPE);

                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            if (update.getCallbackQuery().getData().equals(BotQuery.BMW_SELECT)) {
                user = saveUser(Objects.requireNonNull(chatId));
                user.setCarType(BotConstant.BMW);
                user.setStep(BotConstant.BMW);

                try {
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId);
                    sendPhoto.setPhoto(new InputFile(new File("src/main/java/images/img.png")));
                    sendPhoto.setCaption("BMW I8\n" +
                            "Mator: Gibrit\n" +
                            "0-100 4.4 sekund\n" +
                            "BezinBak: 70\n" +
                            "OtKuchi: 500 ta\n" +
                            "Narxi: 250 000$");

                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<InlineKeyboardButton> rd = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                    inlineKeyboardButton.setText("BUY");
                    inlineKeyboardButton.setCallbackData(BotQuery.BMW_SELECT2);
                    rd.add(inlineKeyboardButton);
                    List<List<InlineKeyboardButton>> te = new ArrayList<>();
                    te.add(rd);
                    inlineKeyboardMarkup.setKeyboard(te);
                    sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
                    execute(sendPhoto);
                } catch (TelegramApiException e) {
                    System.out.println("Error sending photo: " + e.getMessage());
                    e.printStackTrace();
                }

            } else if (update.getCallbackQuery().getData().equals(BotQuery.MERCEDES_SELECT)) {
                user = saveUser(chatId);
                user.setCarType(BotConstant.MERCEDES);
                user.setStep(BotConstant.MERCEDES);

                try {
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId);
                    sendPhoto.setPhoto(new InputFile(new File("src/main/java/images/7K1A5572-scaled.jpg")));

                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText("MERCEDES BENZ G63.\n" +
                            "Yili:2023\n" +
                            "Mator:5.5 turbo\n" +
                            "0-100,3 sekud\n" +
                            "BezinBak: 80l\n" +
                            "OtKuchi: 321\n" +
                            "Narxi:350 000$");

                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<InlineKeyboardButton> bt = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                    inlineKeyboardButton.setText("BUY");
                    inlineKeyboardButton.setCallbackData(BotQuery.MERCEDES_SELECT1);
                    bt.add(inlineKeyboardButton);
                    List<List<InlineKeyboardButton>> in = new ArrayList<>();

                    in.add(bt);
                    inlineKeyboardMarkup.setKeyboard(in);
                    message.setReplyMarkup(inlineKeyboardMarkup);

                    execute(sendPhoto);
                    execute(message);


                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            }
            if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(BotQuery.BMW_SELECT2)) {
                try {
                    String chatId2 = update.getCallbackQuery().getMessage().getChatId().toString();
                    String fullName = user.getFullName();

                    SendMessage adminNotification = new SendMessage(chatId2,
                            fullName + " BMW sotib oldingiz.");

                    execute(adminNotification);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(BotQuery.MERCEDES_SELECT1)) {
                try {
                    String chatId3 = update.getCallbackQuery().getMessage().getChatId().toString();
                    String fullName = user.getFullName();

                    SendMessage adminNotificationn = new SendMessage(chatId3,
                            fullName + " MERCEDES sotib oldingiz.");


                    execute(adminNotificationn);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(BotQuery.BMW_SELECT2)) {
            try {
                String chatId3 = update.getCallbackQuery().getMessage().getChatId().toString();

                SendMessage paymentMethodMessage = new SendMessage(chatId3, "To'lov usulini tanlang:");

                ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
                replyMarkup.setResizeKeyboard(true);
                replyMarkup.setOneTimeKeyboard(true);

                KeyboardRow row1 = new KeyboardRow();
                row1.add("Karta bilan to'lash");

                KeyboardRow row2 = new KeyboardRow();
                row2.add("Bitkoin bilan to'lash");

                replyMarkup.setKeyboard(Arrays.asList(row1, row2));

                paymentMethodMessage.setReplyMarkup(replyMarkup);

                execute(paymentMethodMessage);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            if (messageText.equals("Karta bilan to'lash")) {
                SendMessage cardNumberMessage = new SendMessage(chatId, "Iltimos, 1212 2562 8936 2585 ga tolovni o'tqazing:");
                execute(cardNumberMessage);
            } else {
                String cardNumber = messageText.replaceAll("\\s+", "");
                if (cardNumber.equals("1212256289362585")) {
                    SendMessage paymentAmountMessage = new SendMessage(chatId, "Iltimos, to'lov summasini kiriting:");
                    execute(paymentAmountMessage);
                } else {
                    try {
                        int paymentAmount = Integer.parseInt(messageText);
                        if (paymentAmount >= 250000) {
                            SendMessage confirmationMessage = new SendMessage(chatId, "To'lov muvaffaqiyatli amalga oshirildi!");
                            execute(confirmationMessage);

                            SendVideo videoMessage = new SendVideo();
                            videoMessage.setChatId(update.getMessage().getChatId());


                            String videoFilePath = "C:\\Users\\user\\IdeaProjects\\Tg_bot\\src\\main\\java\\Video";
                            File videoFile = new File(videoFilePath);
                            InputFile inputFile = new InputFile(videoFile);
                            videoMessage.setVideo(inputFile);
                            videoMessage.setCaption("Siz pulizga uxladiz");
                            execute(videoMessage);

                        } else {
                            SendMessage insufficientAmountMessage = new SendMessage(chatId, "To'lov summasi yetarli emas. Iltimos, to'liq summani kiriting:");
                            execute(insufficientAmountMessage);

                            String adminChatId = "1068147717";
                            SendMessage adminErrorMessage = new SendMessage(adminChatId, "To'lov amalga oshmadi! Summa yetarli emas.");
                            execute(adminErrorMessage);

                        }
                    } catch (NumberFormatException e) {
                        //SendMessage invalidAmountMessage = new SendMessage(chatId, "Noto'g'ri formatda summa kiritildi. Iltimos, to'liq summani kiriting:");
                        //execute(invalidAmountMessage);
                    }
                }
            }
        }
    }


        








    private TelegramUser getMe(String chatId) {
        for (TelegramUser user : users) {
            if (user.getChatId().equals(chatId)) {
                return user;
            }
        }
        TelegramUser newUser = new TelegramUser();
        newUser.setChatId(chatId);
        users.add(newUser);
        return newUser;

    }


    private void setLang(String chatId, TelegramUser user) throws TelegramApiException {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(user.getFullName() + " ILTIMOS TINI TANLANG\n" +
                "ПОЖАЛУЙСТА, ВЫБЕРИТЕ TI");
        sendMessage.setChatId(chatId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> td = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButtonUZ = new InlineKeyboardButton();
        inlineKeyboardButtonUZ.setText("UZ");
        inlineKeyboardButtonUZ.setCallbackData(BotQuery.UZ_SELECT);

        InlineKeyboardButton inlineKeyboardButtonRU = new InlineKeyboardButton();

        inlineKeyboardButtonRU.setText("RU");
        inlineKeyboardButtonRU.setCallbackData(BotQuery.RU_SELECT);

        td.add(inlineKeyboardButtonUZ);
        td.add(inlineKeyboardButtonRU);

        List<List<InlineKeyboardButton>> tr = new ArrayList<>();
        tr.add(td);

        inlineKeyboardMarkup.setKeyboard(tr);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        execute(sendMessage);
        user.setStep(BotConstant.SELECT_LANG);
    }

    private TelegramUser saveUser(String chatId) {
        for (TelegramUser user : users) {
            if (user.getChatId().equals(chatId)) {
                return user;
            }
        }

        TelegramUser newUser = new TelegramUser();
        newUser.setChatId(chatId);
        users.add(newUser);
        return newUser;
    }

    @Override
    public String getBotUsername() {
        return "https://t.me/Cars_buy_bot";
    }

    private void setText(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }


}