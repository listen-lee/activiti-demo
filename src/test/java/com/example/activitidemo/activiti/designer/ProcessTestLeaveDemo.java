package com.example.activitidemo.activiti.designer;


import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.ActivitiTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class ProcessTestLeaveDemo {

    private String filename = "C:\\eclipse-workspace\\activiti-demo\\src\\main\\resources\\diagrams\\leaveDemo.bpmn";

    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void startProcess() throws Exception {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        repositoryService.createDeployment().addInputStream("leaveDemo.bpmn20.xml",
                new FileInputStream(filename)).deploy();
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("name", "Activiti");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leaveDemo", variableMap);
        Assertions.assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " "
                + processInstance.getProcessDefinitionId());
    }

    @Test
    public void startProcess1() {
    }
}