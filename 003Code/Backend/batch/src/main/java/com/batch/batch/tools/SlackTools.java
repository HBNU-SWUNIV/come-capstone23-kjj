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
    private static final String SUCCESS_COLOR = "2C953C";
    private static final String FAIL_COLOR = "ff0000";

    public void sendSlackMessage(String jobName) {
        try {
            slack.send(slackUrl, payload(p -> p.text(null)
                    .attachments(List.of(generateSlackAttachment(jobName)))));
        } catch (IOException exception) {
            log.warn("Slack Message 전송 에러");
        }
    }

    public void sendSlackFailMessage(String jobName, String jobState) {
        try {
            slack.send(slackUrl, payload(p -> p.text(null)
                    .attachments(List.of(generateSlackAttachment(jobName, jobState)))));
        } catch (IOException exception) {
            log.warn("Slack Message 전송 에러");
        }
    }

    public void sendSlackErrorMessage(Exception e, String method) {
        try {
            slack.send(slackUrl, payload(p -> p.text(method)
                    .attachments(List.of(generateSlackAttachment(e)))));
        } catch (IOException exception) {
            log.warn("Slack Error Message 전송 에러");
        }
    }

    private Attachment generateSlackAttachment(String jobName) {
        String time = DateTools.getDateAndTime();
        return Attachment.builder()
                .color(SUCCESS_COLOR)
                .title("[Batch] " + time + " 성공 로그")
                .fields(List.of(
                                generateSlackField("Job", jobName)
                        )
                )
                .build();
    }

    private Attachment generateSlackAttachment(String jobName, String jobState) {
        String time = DateTools.getDateAndTime();
        return Attachment.builder()
                .color(FAIL_COLOR)
                .title("[Batch] " + time + " 실패 로그")
                .fields(List.of(
                        generateSlackField("Job", jobName),
                        generateSlackField("jobState", jobState)
                        )
                )
                .build();
    }

    private Attachment generateSlackAttachment(Exception e) {
        String time = DateTools.getDateAndTime();
        return Attachment.builder()
                .color(FAIL_COLOR)
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
