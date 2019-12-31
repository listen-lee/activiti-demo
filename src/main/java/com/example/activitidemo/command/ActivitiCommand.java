package com.example.activitidemo.command;/*
 * @program: activiti-demo
 *
 * @description:
 *
 * @author: guangpeng.li
 *
 * @create: 2019-12-26 16:46
 */

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ActivitiCommand implements CommandLineRunner {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public ActivitiCommand(RepositoryService repositoryService, RuntimeService runtimeService, TaskService taskService) {
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    @Override
    public void run(String... args) throws Exception {
//        log.info("Number of process definitions: [{}]", repositoryService.createProcessDefinitionQuery().count());
//        log.info("Number of Tasks: [{}]", taskService.createTaskQuery().count());
//        runtimeService.startProcessInstanceByKey("oneTaskProcess");
//        log.info("Number of tasks after process start: [{}]", taskService.createTaskQuery().count());
    }
}

