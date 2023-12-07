package net.paiique.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;

import java.io.IOException;

import static net.paiique.util.Env.*;

public class SlackWebhook {
    public static void send(String message) {

        Slack slack = Slack.getInstance();
        String webhookUrl = DEBUG.getString().equals("true") ? SLACK_DEV_WEBHOOK.getString() : SLACK_PROD_WEBHOOK.getString();
        Payload payload = Payload.builder().text(message).build();

        try {
            slack.send(webhookUrl, payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
