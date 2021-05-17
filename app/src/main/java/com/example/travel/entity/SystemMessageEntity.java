package com.example.travel.entity;

/**
 * @@author:ljz
 * @@date:2021/5/14,17:08
 * @@version:1.0
 * @@annotation:
 **/
public class SystemMessageEntity {

    private String systemMessageId;
    private String messagerPortrait;
    private String messagerName;
    private int messageState; //0 未读 1已读
    private String messageMain;
    private String messageTime;


    public String getSystemMessageId() {
        return systemMessageId;
    }

    public void setSystemMessageId(String systemMessageId) {
        this.systemMessageId = systemMessageId;
    }

    public String getMessagerPortrait() {
        return messagerPortrait;
    }

    public void setMessagerPortrait(String messagerPortrait) {
        this.messagerPortrait = messagerPortrait;
    }

    public String getMessagerName() {
        return messagerName;
    }

    public void setMessagerName(String messagerName) {
        this.messagerName = messagerName;
    }

    public int getMessageState() {
        return messageState;
    }

    public void setMessageState(int messageState) {
        this.messageState = messageState;
    }

    public String getMessageMain() {
        return messageMain;
    }

    public void setMessageMain(String messageMain) {
        this.messageMain = messageMain;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
