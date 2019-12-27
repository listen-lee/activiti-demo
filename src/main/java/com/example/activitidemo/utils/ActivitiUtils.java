package com.example.activitidemo.utils;/*
 * @program: activiti-demo
 *
 * @description:
 *
 * @author: guangpeng.li
 *
 * @create: 2019-12-27 14:25
 */

import lombok.extern.slf4j.Slf4j;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetailQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ActivitiUtils {
    public static void deployProcess(RepositoryService repositoryService, String definition) {
        // 部署流程
        repositoryService.createDeployment()
                .addClasspathResource(definition)
                .deploy();

    }

    public static ProcessInstance startProcess(RuntimeService runtimeService, String definitionName, TaskService taskService) {
        // 开启流程
        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("name", "Activiti");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(definitionName, variableMap);
        log.error("processId: {}, processDefinitionId: {}", processInstance.getId(), processInstance.getProcessDefinitionId());
        // 获取"申请" 任务并提交表单
        startApplyTask(taskService, processInstance.getId());
        return processInstance;
    }

    private static void startApplyTask(TaskService taskService, String processId) {
        // 获取task
        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();

        log.error("taskId: {}, taskDefinitionKey: {}", task.getId(), task.getTaskDefinitionKey());
        // 封装表单
//        Map<String, String> taskFormData = new HashMap<>();
//        taskFormData.put("applyId", "1");
//        taskFormData.put("applyName", "张三");
        Map<String, Object> taskFormData = Map.of("applyId", "1", "applyName", "张三", "applyHours", 16, "applyReason", "结婚");
//        taskService.setVariables(task.getId(), taskFormData);
        // 执行人id
        task.setAssignee("1");
        taskService.complete(task.getId(), taskFormData);

    }

    public static void historyTask(HistoryService historyService, String processId) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processId).list();
        log.info("HistoricTaskInstance:{}", list);
    }
}
