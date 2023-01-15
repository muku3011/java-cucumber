package com.bdd.feature.browser;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

    public static void sendEmail(String emailId, String emailPassword, String subject, String body) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailId, emailPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailId)
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent to: " + emailId);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static String readEmail(String emailId, String emailPassword) throws MessagingException {
        Store store = null;
        Folder inbox = null;

        try {
            String host = "imap.gmail.com";
            Properties properties = new Properties();
            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);
            Session emailSession = Session.getDefaultInstance(properties);

            // create the imap store object and connect to the imap server
            store = emailSession.getStore("imaps");

            store.connect(host, emailId, emailPassword);

            // create the inbox object and open it
            inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);

            return getLastUnreadEmailBody(inbox);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inbox != null) {
                inbox.close(false);
            }
            if (store != null) {
                store.close();
            }
        }
        return null;
    }

    private static String getLastUnreadEmailBody(Folder inbox) throws MessagingException, IOException, InterruptedException {
        Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

        if (messages.length == 0) {
            System.out.println("No unread email, going to wait 5 seconds");
            Thread.sleep(5000);
            String lastUnreadEmailBody = getLastUnreadEmailBody(inbox);
            if (lastUnreadEmailBody != null) {
                return lastUnreadEmailBody;
            }
        }

        for (Message message : messages) {
            if ("OTP Confirmation".equals(message.getSubject())) {
                message.setFlag(Flags.Flag.SEEN, true);
                System.out.println("---------------------------------");
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                return message.getContent().toString();
            } else {
                System.out.println("Not an OTP email, skipping it ...");
            }
        }
        System.out.println("No OTP email found");
        return null;
    }

    /**
     * Returns a list with all links contained in the input
     * Follow https://myaccount.google.com/security to create password for gmail access
     */
    public static String extractUrls(String emailId, String emailPassword) throws MessagingException {

        String messageBody = readEmail(emailId, emailPassword);

        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?+-=\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher;
        if (messageBody != null) {
            urlMatcher = pattern.matcher(messageBody);
            if (urlMatcher.find()) {
                return messageBody.substring(urlMatcher.start(0), urlMatcher.end(0));
            }
        }
        return null;
    }
}
