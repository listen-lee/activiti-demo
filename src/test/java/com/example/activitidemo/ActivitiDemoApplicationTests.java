package com.example.activitidemo;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiDemoApplicationTests {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;

    @Test
    public void contextLoads() {
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("kermit").list();
        if (taskList.size() > 0) {
            taskService.complete(taskList.get(0).getId());
        }
        log.error("taskList:{}", taskList);
    }

    @Test
    public void historyQuery() {
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().finished().list();
        log.error("historicTaskInstanceList:{}", historicTaskInstanceList);

    }

    @Test
    public void taskQuery() {
        List<Task> taskList = taskService.createTaskQuery().list();
        log.error("taskList:{}", taskList);
    }

}
