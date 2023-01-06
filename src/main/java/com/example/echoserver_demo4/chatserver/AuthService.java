package com.example.echoserver_demo4.chatserver;

public interface AuthService {
    String getNickByLoginAndPassword(String login, String password);
}
