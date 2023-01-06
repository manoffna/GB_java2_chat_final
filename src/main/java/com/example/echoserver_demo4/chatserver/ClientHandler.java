package com.example.echoserver_demo4.chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {        //экземпляр класса создаётся для каждого клиента
    private static final String COMMAND_PREFIX = "/";
    private static final String SEND_MESSAGE_TO_CLIENT_COMMAND = COMMAND_PREFIX + "w";
    private static final String END_COMMAND = COMMAND_PREFIX + "end";
    private final Socket socket;
    private final ChatServer server;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String nick;

    public ClientHandler(Socket socket, ChatServer server) {
        try {
            this.nick = "";
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    authenticate();
                    readMessage();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void closeConnection() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
        if (out != null) {
             out.close();}
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (socket != null) {
                server.unsubscribe(this);
                socket.close();}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessage() {
        try {
            while (true) {
                final String msg = in.readUTF();
                System.out.println("Receive message: " + msg);
                if (msg.startsWith(COMMAND_PREFIX)) {
                    if (END_COMMAND.equals(msg)) {
                        break;
                    }
                    if (msg.startsWith(SEND_MESSAGE_TO_CLIENT_COMMAND)) { // /w nick Message
                        final String[] token = msg.split(" ");
                        final String nick = token[1];   // ПОПРОБОВАТЬ ЗДЕСЬ ВЫТАШИТЬ nick ЧЕРЕЗ hashmap!!!
                        server.sendMessageToClient(this, nick, msg.substring(SEND_MESSAGE_TO_CLIENT_COMMAND.length() + 2 + nick.length()));
                    }
                    continue;
                }
                server.broadcast (nick + ": " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void authenticate() {
        while(true) {
            try {
                final String str = in.readUTF(); // "/auth login pass"
                if (str.startsWith("/auth")) {
                    final String[] split = str.split(" ");
                    final String login = split[1];
                    final String password = split[2];
                    final String nick = server.getAuthservice().getNickByLoginAndPassword(login, password);
                    System.out.println(login + " " + password);
                    if (nick != null) {
                        if (server.isNickBusy(nick)) {
                            sendMessage("Пользователь " + nick + " уже подключен");
                            continue;
                        }
                        sendMessage("/authok " + nick);
                        this.nick = nick;
                        server.subscribe(this);
                        server.broadcast("Пользователь " + nick + " зашёл в чат");
                        break;
                    }
                    else {
                        sendMessage("Неверные логин и пароль");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            System.out.println("SERVER: send message to " + nick);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }
}
