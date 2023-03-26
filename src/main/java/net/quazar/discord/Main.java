package net.quazar.discord;

import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        var env = Dotenv.load();
        String token = env.get("BOT_TOKEN");
        String webhookUrl = env.get("WEBHOOK_URL");
        new ParodyBot(token, webhookUrl).run();
    }
}
