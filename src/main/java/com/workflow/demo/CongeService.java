package com.workflow.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.workflow.dto.ProcessInstanceResponse;
import com.workflow.dto.TaskDetails;

import org.flowable.cmmn.engine.impl.cmd.GetIdentityLinksForCaseInstanceCmd;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CongeService {
	
	
    public static final String TASK_CANDIDATE_GROUP = "managers";
    public static final String PROCESS_DEFINITION_KEY = "holidayRequest";
   // public static final String EMP_NAME = variables.put("employee", TDemande.getEmpName()); ;
    RuntimeService runtimeService;
    TaskService taskService;
    ProcessEngine processEngine;
    RepositoryService repositoryService;
    
    
    @Autowired  
	congeRepository congeRepository ;

    //********************************************************** deployment service methods **********************************************************

    public void deployProcessDefinition() {

        Deployment deployment =
                repositoryService
                        .createDeployment()
                        .addClasspathResource("processus/Processflowconge.bpmn20.xml")
                        .deploy();


    }


    //********************************************************** process service methods **********************************************************

    public ProcessInstanceResponse applyHoliday(TDemande tDemande) {

    	
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("Typeconge", tDemande.getCongeType());
        variables.put("DateDebut", tDemande.getDateDebut());
        variables.put("DateFin", tDemande.getDateFin());
        variables.put("Commentaire", tDemande.getComment());
        variables.put("employee", tDemande.getEmpName());
                
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);

        return new ProcessInstanceResponse(processInstance.getId(), processInstance.isEnded());
       
    }
    //***************Table demande de conge *****************************
    public void saveDemande(TDemande tDemande) {
    	
    	congeRepository.save(tDemande);
    }


    public List<TaskDetails> getManagerTasks() {
        List<Task> tasks =
                taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP).list();
        List<TaskDetails> taskDetails = getTaskDetails(tasks);

        return taskDetails;
    }

    private List<TaskDetails> getTaskDetails(List<Task> tasks) {
        List<TaskDetails> taskDetails = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> processVariables = taskService.getVariables(task.getId());
            taskDetails.add(new TaskDetails(task.getId(), task.getName(), processVariables));
        }
        return taskDetails;
    }


    public void approveHoliday(String taskId,Boolean approved) {

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("approved", approved.booleanValue());
        taskService.complete(taskId, variables);
    }
    

    public void acceptHoliday(String taskId) {
        taskService.complete(taskId);
    }


    public List<TaskDetails> getUserTasks(TDemande td) {

        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(td.getEmpName()).list();
        List<TaskDetails> taskDetails = getTaskDetails(tasks);

        return taskDetails;
    }


    public String checkProcessHistory(String processId) {

        HistoryService historyService = processEngine.getHistoryService();

        List<HistoricActivityInstance> activities =
                historyService
                        .createHistoricActivityInstanceQuery()
                        .processInstanceId(processId)
                        .finished()
                        .orderByHistoricActivityInstanceEndTime()
                        .asc()
                        .list();

        for (HistoricActivityInstance activity : activities) {
            System.out.println(
                    activity.getActivityId() + " took " + activity.getDurationInMillis() + " milliseconds");
        }

       System.out.println("\n \n \n \n");
       return  "ProcessHistory";
    }
    
    public void approveHolidayRH(String taskId,Boolean approvedRH) {

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("approvedRH", approvedRH.booleanValue());
        taskService.complete(taskId, variables);
    }

    public List<TDemande> rechercheConge(String Commentaire,String Typeconge, String empName) {
    	
    return congeRepository.findByrechercheid(Commentaire,Typeconge,empName);
    
    }
    
    public List<TDemande> recherche() {
    	
        return congeRepository.findAll();
        }
    
    


}