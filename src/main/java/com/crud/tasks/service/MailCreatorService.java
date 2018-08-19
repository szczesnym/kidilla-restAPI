package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailCreatorService {

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    public String buildTrelloCardEmail(String message) {
        Context context = new Context();
        List<String> functionality = new ArrayList<>();
        functionality.add("You can manage your tasks");
        functionality.add("Provides connection with Trello Account");
        functionality.add("Application allows sending tasks to Trello");

        context.setVariable("message", message);
        context.setVariable("task_url", "http://localhost:8888/crud");
        context.setVariable("button", "Visit website");
        context.setVariable("admin_config", adminConfig);
        context.setVariable("application_functionality", functionality);

        context.setVariable("admin_name", adminConfig.getAdminName());
        context.setVariable("company_name", adminConfig.getCompanyName());
        context.setVariable("company_goal", adminConfig.getCompanyGoal());
        context.setVariable("company_email", adminConfig.getCompanyEmail());
        context.setVariable("company_phone", adminConfig.getCompanyPhone());
        context.setVariable("goodbye_message", adminConfig.getGoodbyeMessage());
        context.setVariable("show_button", false);
        context.setVariable("is_friend", true);
        //nie działa opcja ścieżki na //mail
        return templateEngine.process("created-trello-card-mail", context);
    }

    public String dailyTaskInfoMail(String message, List<TaskDto> tasksList) {
        Context context = new Context();
        List<String> functionality = new ArrayList<>();
        functionality.add("You can manage your tasks");

        context.setVariable("message", message);
        context.setVariable("task_url", "http://localhost:8080/v1/tasks/getTasks");
        context.setVariable("button", "Visit website");
        context.setVariable("admin_config", adminConfig);
        context.setVariable("application_functionality", functionality);
        context.setVariable("tasks_list", tasksList);

        context.setVariable("show_button", true);
        context.setVariable("is_friend", true);
        //nie działa opcja ścieżki na //mail
        return templateEngine.process("daily-task-info-mail", context);

    }
}
