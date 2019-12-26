package com.example.activitidemo;

import com.example.activitidemo.utils.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiDemoApplicationTests {
    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Test
    public void contextLoads() {
        securityUtil.logInAs("system");
        ProcessDefinition processDefinition = processRuntime.processDefinition("categorizeProcess");

        assertThat(processDefinition).isNotNull();
    }

}
