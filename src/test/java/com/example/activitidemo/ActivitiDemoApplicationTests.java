package com.example.activitidemo;

import com.example.activitidemo.utils.ActivitiUtils;
import com.example.activitidemo.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiDemoApplicationTests {
    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private SecurityUtil securityUtil;


    @Test
    public void contextLoads() {
        securityUtil.logInAs("system");
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        for (ProcessDefinition processDefinition : processDefinitionPage.getContent()) {
            log.debug("\t > Process definition: {}", processDefinition);
        }
    }

    @Test
    public void deploy() {
        securityUtil.logInAs("system");
        ActivitiUtils.deployProcess(repositoryService, "processes/leaveDemo.bpmn");
        Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        for (ProcessDefinition processDefinition : processDefinitionPage.getContent()) {
            log.error("\t > Process definition: {}", processDefinition);
        }

        ProcessInstance processInstance = ActivitiUtils.startProcess(runtimeService, "leaveDemo", taskService);
        ActivitiUtils.historyTask(historyService, processInstance.getId());
    }

}
