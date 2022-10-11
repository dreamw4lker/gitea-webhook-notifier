package org.dreamw4lker.gwn.utils;

import lombok.extern.slf4j.Slf4j;
import org.dreamw4lker.gwn.domain.CommitBean;
import org.dreamw4lker.gwn.domain.PersonBean;
import org.dreamw4lker.gwn.domain.WebhookBean;
import org.springframework.util.ObjectUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
public class HtmlUtils {
    public static String prepareHtmlSubject(WebhookBean webhookBean) {
        String result = "[Gitea]";
        result += " [" + webhookBean.getRepository().getName() + "] ";
        result += webhookBean.getCommits()
                .stream()
                .filter(c -> c.getId() != null)
                .map(c -> c.getId().substring(0, Math.min(10, c.getId().length())))
                .collect(Collectors.joining(","));
        return result;
    }

    public static String prepareHtmlMessage(WebhookBean webhookBean) {
        StringBuilder message = new StringBuilder("<html>");
        message.append("<body>");
        message.append("<h2 style=\"color:#d20005;font-size:14px;margin-bottom:0\">Branch: ").append(webhookBean.getRef()).append("</h2>");
        message.append("<h2 style=\"font-size:14px;margin-top:0\"><a href=\"").append(webhookBean.getCompareUrl())
                .append("\" target=\"_blank\">Diff</a></h2>");
        message.append("<table style=\"border:0;border-collapse:collapse;font-size:14px;width:100%\">");
        message.append("<tbody>");
        for (CommitBean commit : webhookBean.getCommits()) {
            message.append("<tr style=\"border-top:1px solid #ccc\"><td style=\"padding-top:12px\">")
                    .append(preparePerson(commit.getAuthor()))
                    .append("</td></tr>");
            message.append("<tr><td>").append(formatDateTime(commit.getTimestamp())).append("</td></tr>");
            message.append("<tr>");
            message.append("<td style=\"padding-top:14px;padding-bottom:14px\"><span style=\"font-family:monospace\">")
                    .append(commit.getMessage())
                    .append("</span></td>");
            message.append("<tr><td>Hash: <a href=\"")
                    .append(commit.getUrl()).append("\" target=\"_blank\">").append(commit.getId()).append("</a>")
                    .append("</td></tr>");
            message.append("</tr>");
            message.append("<tr>");
            message.append("<td style=\"padding-bottom:12px\">");
            for (String str : commit.getAdded()) {
                message.append("A ").append(str).append("<br>");
            }
            for (String str : commit.getModified()) {
                message.append("M ").append(str).append("<br>");
            }
            for (String str : commit.getRemoved()) {
                message.append("R ").append(str).append("<br>");
            }
            message.append("</td>");
            message.append("</tr>");
        }
        message.append("</tbody>");
        message.append("</body>");
        message.append("</html>");
        return message.toString();
    }

    private static String preparePerson(PersonBean personBean) {
        String result = "";
        if (!ObjectUtils.isEmpty(personBean.getName())) {
            result += personBean.getName();
        } else if (!ObjectUtils.isEmpty(personBean.getUsername())) {
            result += personBean.getUsername();
        }

        if (!ObjectUtils.isEmpty(personBean.getEmail())) {
            if (result.isEmpty()) {
                result += personBean.getEmail();
            } else {
                result += " <" + personBean.getEmail() + ">";
            }
        }
        return result;
    }

    private static String formatDateTime(String unformattedDateTime) {
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(unformattedDateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME);
            return zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss '('ZZZZ')'"));
        } catch (Exception ex) {
            log.warn("Can't parse datetime: " + unformattedDateTime);
            return unformattedDateTime;
        }
    }
}
