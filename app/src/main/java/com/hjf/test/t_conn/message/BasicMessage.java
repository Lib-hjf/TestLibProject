package com.hjf.test.t_conn.message;

/**
 * 通知消息，用于App组件之间事件通知和数据传递
 */
public class BasicMessage {

    @MessageType
    private String type;

    private String content = "{\"message\":\"empty message\"}";

    public BasicMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}
