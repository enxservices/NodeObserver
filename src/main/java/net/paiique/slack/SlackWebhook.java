package net.paiique.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

import java.io.IOException;

import static net.paiique.util.Env.*;

public class SlackWebhook {
    public static void send(String message) {


        Slack slack = Slack.getInstance();

        String webhookUrl = DEBUG.get().equals("true") ? SLACK_DEV_WEBHOOK.get() : SLACK_PROD_WEBHOOK.get();
        Payload payload = Payload.builder().text(message).build();

        WebhookResponse response = null;
        try {
            response = slack.send(webhookUrl, payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
      //  System.out.println(response); // WebhookResponse(code=200, message=OK, body=ok)

    }
}
