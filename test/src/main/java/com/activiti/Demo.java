package com.activiti;

import org.activiti.engine.*;
import org.activiti.engine.task.Task;

public class Demo {
    public static void main(String[] args) {
        //创建流程引擎
        ProcessEngine  engine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault()
                .buildProcessEngine();
        // 获取流程储存服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        //获取流程运行服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        //获取流程任务组件
        TaskService taskService = engine.getTaskService();
        //部署流程文件
        repositoryService.createDeployment()
                .addClasspathResource("bpmn/demo.bpmn20.xml")
                .enableDuplicateFiltering()
                .deploy();
        //启动流程
        runtimeService.startProcessInstanceByKey("demo");
        //查询第一个任务
        Task task = taskService.createTaskQuery().singleResult();
        System.out.println("第一个任务名称："+task.getName());
        taskService.complete(task.getId());
        System.out.println("第一个任务完成");
        task = taskService.createTaskQuery().singleResult();
        System.out.println("第二个任务名称："+task.getName());
        taskService.complete(task.getId());
        System.out.println("第二个任务完成");
        System.exit(0);
    }
}
