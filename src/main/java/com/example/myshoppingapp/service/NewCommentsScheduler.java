package com.example.myshoppingapp.service;

import com.example.myshoppingapp.domain.comments.OutputCommentDTO;
import com.example.myshoppingapp.domain.users.UserOutputDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class NewCommentsScheduler {
    private final CommentService commentService;
    private final UserService userService;

    private final EmailService emailService;

    private final Logger LOGGER = LoggerFactory.getLogger(NewCommentsScheduler.class);

    public NewCommentsScheduler(CommentService commentService, UserService userService, EmailService emailService) {
        this.commentService = commentService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 10/20/30 * * * *")
    public void checkForNewComments() {
        List<String> adminEmails = this.userService.findAllAdminEmails();

        List<OutputCommentDTO> newComments = this.commentService.getAllUnapprovedComments();
        if (newComments != null) {
            emailService.sendAlertForNewComments(adminEmails, newComments.size());
            LOGGER.info("New comments added. Sent email to ADMIN.");
        }

    }

}
