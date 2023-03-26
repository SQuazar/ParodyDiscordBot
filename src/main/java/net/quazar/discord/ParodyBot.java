package net.quazar.discord;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.concurrent.CompletableFuture;

public class ParodyBot extends ListenerAdapter implements Runnable {

    private final String token;
    private final WebhookClient webhookClient;

    public ParodyBot(String token, String webhookUrl) {
        this.token = token;
        this.webhookClient = WebhookClient.withUrl(webhookUrl);
    }

    @Override
    public void run() {
        JDA jda = JDABuilder.createLight(token)
                .setActivity(Activity.watching("Server"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
        jda.upsertCommand(Commands.slash("parody", "Пародия пользователя")
                .addOption(OptionType.USER, "user", "Пользователь", true)
                .addOption(OptionType.STRING, "message", "Сообщение", true))
                .queue();
        jda.addEventListener(this);
    }

    @SubscribeEvent
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        e.deferReply(true).queue();
        User user = e.getOption("user").getAsUser();
        String username = user.getName();
        String avatar = user.getEffectiveAvatarUrl();
        Member member;
        if ((member = e.getOption("user").getAsMember()) != null)
            username = member.getEffectiveName();
        final String name = username;
        CompletableFuture.runAsync(() -> webhookClient.send(new WebhookMessageBuilder()
                .setUsername(name)
                .setAvatarUrl(avatar)
                .setContent(e.getOption("message").getAsString())
                .build())).whenComplete((unused, throwable) -> e.getHook().deleteOriginal().queue());
    }

}
