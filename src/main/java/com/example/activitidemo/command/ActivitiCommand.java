package com.example.activitidemo.command;/*
 * @program: activiti-demo
 *
 * @description:
 *
 * @author: guangpeng.li
 *
 * @create: 2019-12-26 16:46
 */

import com.example.activitidemo.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@Component
public class ActivitiCommand implements CommandLineRunner {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public void run(String... args) throws Exception {
        securityUtil.logInAs("system");
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        log.info("> Available Process definitions: {}", processDefinitionPage.getTotalItems());

        processDefinitionPage.getContent().forEach(processDefinition -> log.info("\t > Process definition: {}", processDefinition));
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void processText() {
        securityUtil.logInAs("system");

        String content = pickRandomString();

        log.info("> Processing content: {} at: {}", content, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey("categorizeProcess")
                .withName("Processing Content: " + content)
                .withVariable("content", content)
                .build()
        );

        log.info(">>> Created Process Instance: {}", processInstance);

    }

    private String pickRandomString() {
        String[] texts = {"hello from london", "Hi there from activiti!", "all good news over here.", "I've tweeted about activiti today.",
                "other boring projects.", "activiti cloud - Cloud Native Java BPM"};
        return texts[new Random().nextInt(texts.length)];
    }
}

