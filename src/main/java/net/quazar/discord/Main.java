package net.quazar.discord;

public class Main {
    public static void main(String[] args) {
        String token = System.getenv("BOT_TOKEN");
        String webhookUrl = System.getenv("WEBHOOK_URL");
        new ParodyBot(token, webhookUrl).run();
    }
}
