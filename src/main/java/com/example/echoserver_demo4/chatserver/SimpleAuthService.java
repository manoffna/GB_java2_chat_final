package com.example.echoserver_demo4.chatserver;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {

    private final List<UserData> users;

    public SimpleAuthService () {
        users = new ArrayList<>();
        for (int i = 0; i<5;i++) {
            users.add(new UserData("login" + i, "pass" + i, "nick" + i));
        }
        System.out.println();
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if (user.login.equals(login) && user.password.equals(password)) {
                return user.nick;
            }
        }
        return null; // возвращать null крайне не рекомендуется. но пока здесь так оставляем
    }

    private static class UserData {
        private final String login;
        private final String password;
        private final String nick;

        public UserData(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }
}
