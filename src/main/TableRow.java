package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.Arrays;

public class TableRow {
    private SimpleStringProperty number = new SimpleStringProperty();
    private SimpleStringProperty title = new SimpleStringProperty();
    private SimpleStringProperty code = new SimpleStringProperty();
    private SimpleStringProperty price = new SimpleStringProperty();
    private SimpleStringProperty amount = new SimpleStringProperty();
    private SimpleStringProperty totalPrice = new SimpleStringProperty();
    private SimpleStringProperty income = new SimpleStringProperty();
    private SimpleStringProperty sentToStorageOrCompensatedByRecipient = new SimpleStringProperty();
    private SimpleStringProperty brokenAmount = new SimpleStringProperty();
    private SimpleStringProperty brokenTotalPrice = new SimpleStringProperty();
    private SimpleStringProperty lostAmount = new SimpleStringProperty();
    private SimpleStringProperty lostTotalPrice = new SimpleStringProperty();
    private SimpleStringProperty leftInTheEndAmount = new SimpleStringProperty();
    private SimpleStringProperty leftInTheEndTotalPrice = new SimpleStringProperty();
    private SimpleStringProperty comment = new SimpleStringProperty();
    private boolean empty = true;

    public TableRow(String number, String title, String code, String price) {
        this.number = new SimpleStringProperty(number);
        this.title = new SimpleStringProperty(title);
        this.code = new SimpleStringProperty(code);
        this.price = new SimpleStringProperty(price);
        empty = false;
    }

    public boolean isEmpty(){
        return empty;
    }

    public TableRow() {
    }

    public static String[] getAverageValues(ObservableList<TableRow> rows){
        String[] result = new String[10];
        Arrays.fill(result, "0");
        for (TableRow row : rows){
            if (row.getAmount() != null)
                result[0] = String.valueOf(Integer.parseInt(result[0]) + Integer.parseInt(row.getAmount()));
            if (row.getPrice() != null)
                result[1] = (new BigDecimal(result[1])).add(new BigDecimal(row.getPrice())).toString();
            if (row.getIncome() != null)
                result[2] = String.valueOf(Integer.parseInt(result[2]) + Integer.parseInt(row.getIncome()));
            if (row.getSentToStorageOrCompensatedByRecipient() != null)
                result[3] = String.valueOf(Integer.parseInt(result[3]) + Integer.parseInt(row.getSentToStorageOrCompensatedByRecipient()));
            if (row.getBrokenAmount() != null)
                result[4] = String.valueOf(Integer.parseInt(result[4]) + Integer.parseInt(row.getBrokenAmount()));
            if (row.getBrokenTotalPrice() != null)
                result[5] = (new BigDecimal(result[5])).add(new BigDecimal(row.getBrokenTotalPrice())).toString();
            if (row.getLostAmount() != null)
                result[6] = String.valueOf(Integer.parseInt(result[6]) + Integer.parseInt(row.getLostAmount()));
            if (row.getLostTotalPrice() != null)
                result[7] = (new BigDecimal(result[5])).add(new BigDecimal(row.getLostTotalPrice())).toString();
            if (row.getLeftInTheEndAmount() != null)
                result[8] = String.valueOf(Integer.parseInt(result[8]) + Integer.parseInt(row.getLeftInTheEndAmount()));
            if (row.getLeftInTheEndTotalPrice() != null)
                result[9] = (new BigDecimal(result[9])).add(new BigDecimal(row.getLeftInTheEndTotalPrice())).toString();
        }
        return result;
    }

    public String getNumber() {
        return number != null ? number.get() : null;
    }

    public SimpleStringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getTitle() {
        return title != null ? title.get() : null;
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getCode() {
        return code != null ? code.get() : null;
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getPrice() {
        return price != null ? price.get() : null;
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getAmount() {
        return amount != null ? amount.get() : null;
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getTotalPrice() {
        return totalPrice != null ? totalPrice.get() : null;
    }

    public SimpleStringProperty totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public String getIncome() {
        return income != null ? income.get() : null;
    }

    public SimpleStringProperty incomeProperty() {
        return income;
    }

    public void setIncome(String income) {
        this.income.set(income);
    }

    public String getSentToStorageOrCompensatedByRecipient() {
        return sentToStorageOrCompensatedByRecipient != null ? sentToStorageOrCompensatedByRecipient.get() : null;
    }

    public SimpleStringProperty sentToStorageOrCompensatedByRecipientProperty() {
        return sentToStorageOrCompensatedByRecipient;
    }

    public void setSentToStorageOrCompensatedByRecipient(String sentToStorageOrCompensatedByRecipient) {
        this.sentToStorageOrCompensatedByRecipient.set(sentToStorageOrCompensatedByRecipient);
    }

    public String getBrokenAmount() {
        return brokenAmount != null ? brokenAmount.get() : null;
    }

    public SimpleStringProperty brokenAmountProperty() {
        return brokenAmount;
    }

    public void setBrokenAmount(String brokenAmount) {
        this.brokenAmount.set(brokenAmount);
    }

    public String getBrokenTotalPrice() {
        return brokenTotalPrice != null ? brokenTotalPrice.get() : null;
    }

    public SimpleStringProperty brokenTotalPriceProperty() {
        return brokenTotalPrice;
    }

    public void setBrokenTotalPrice(String brokenTotalPrice) {
        this.brokenTotalPrice.set(brokenTotalPrice);
    }

    public String getLostAmount() {
        return lostAmount != null ? lostAmount.get() : null;
    }

    public SimpleStringProperty lostAmountProperty() {
        return lostAmount;
    }

    public void setLostAmount(String lostAmount) {
        this.lostAmount.set(lostAmount);
    }

    public String getLostTotalPrice() {
        return lostTotalPrice != null ? lostTotalPrice.get() : null;
    }

    public SimpleStringProperty lostTotalPriceProperty() {
        return lostTotalPrice;
    }

    public void setLostTotalPrice(String lostTotalPrice) {
        this.lostTotalPrice.set(lostTotalPrice);
    }

    public String getLeftInTheEndAmount() {
        return leftInTheEndAmount != null ? leftInTheEndAmount.get() : null;
    }

    public SimpleStringProperty leftInTheEndAmountProperty() {
        return leftInTheEndAmount;
    }

    public void setLeftInTheEndAmount(String leftInTheEndAmount) {
        this.leftInTheEndAmount.set(leftInTheEndAmount);
    }

    public String getLeftInTheEndTotalPrice() {
        return leftInTheEndTotalPrice != null ? leftInTheEndTotalPrice.get() : null;
    }

    public SimpleStringProperty leftInTheEndTotalPriceProperty() {
        return leftInTheEndTotalPrice;
    }

    public void setLeftInTheEndTotalPrice(String leftInTheEndTotalPrice) {
        this.leftInTheEndTotalPrice.set(leftInTheEndTotalPrice);
    }

    public String getComment() {
        return comment != null ? comment.get() : null;
    }

    public SimpleStringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }
}
