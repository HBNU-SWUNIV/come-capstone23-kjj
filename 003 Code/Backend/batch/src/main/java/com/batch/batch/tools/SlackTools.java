package com.batch.batch.tools;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Slf4j
@Component
public class SlackTools {

    @Value("${slack.webhook.url}") private String slackUrl;
    private final Slack slack = Slack.getInstance();

    public void sendSlackMessage(Exception e, String method) {
        try {
            slack.send(slackUrl, payload(p -> p.text(method)
                    .attachments(List.of(generateSlackAttachment(e)))));
        } catch (IOException exception) {
            log.warn("Slack 전송 에러");
        }
    }

    private Attachment generateSlackAttachment(Exception e) {
        String time = DateTools.getDateAndTime();
        return Attachment.builder()
                .color("ff0000")
                .title("[Batch] " + time + " 발생 에러 로그")
                .fields(List.of(
                                generateSlackField("Class", e.getClass().getSimpleName()),
                                generateSlackField("Error Message", e.getMessage())
                        )
                )
                .build();
    }

    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
