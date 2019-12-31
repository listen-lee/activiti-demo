package com.example.activitidemo.activiti;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class ProcessEngineTest {
    @Test
    public void processEngine() {
        ProcessEngineConfiguration processEngineConfiguration = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        // 部署
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService
                .createDeployment()
                .addClasspathResource("processes/holiday-request.bpmn20.xml")
                .deploy();
        // 查询
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        log.info("Found process definition: {}", processDefinition.getName());
        // 启动实例

        Scanner scanner = new Scanner(System.in);
        log.info("Who are you?");
        String employee = scanner.nextLine();

        log.info("How many holidays do you want to request?");
        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

        log.info("Why do you need them?");
        String description = scanner.nextLine();

        // 运行实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = Map.of("employee", employee, "nrOfHolidays", nrOfHolidays, "description", description);
        // 运行实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", variables);

        // 任务服务
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        log.info("You have {} tasks: ", tasks.size());
        for (int i = 0; i < tasks.size(); i++) {
            log.info("[{}] [{}]", (i + 1), tasks.get(i).getName());
        }

        log.info("Which task would you like to complete?");
        int taskIndex = Integer.parseInt(scanner.nextLine());
        // 获取任务
        Task task = tasks.get(taskIndex - 1);
        Map<String, Object> processVariables = taskService.getVariables(task.getId());

        log.info("[{}] wants [{}] of holidays. Do you approve this?",
                processVariables.get("employee"),
                processVariables.get("nrOfHolidays"));

        // 完成任务
        boolean approved = scanner.nextLine().toLowerCase().equals("y");
        Map<String, Object> taskVariables = Map.of("approved", approved);
        taskService.complete(task.getId(), taskVariables);
        // 历史实例

        HistoryService historyService = processEngine.getHistoryService();

        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .finished()
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();
        for (HistoricActivityInstance activity : activities) {
            log.info("[{}] took [{}] milliseconds", activity.getActivityId(),
                    activity.getDurationInMillis());
        }
    }
}
