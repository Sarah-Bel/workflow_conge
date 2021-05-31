package com.workflow.demo;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.dto.ProcessInstanceResponse;
import com.workflow.dto.TaskDetails;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CongeController {

	@Autowired
	CongeService congerService;
    
    

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    public void deployWorkflow() {
    	congerService.deployProcessDefinition();
    }

    //********************************************************** process endpoints **********************************************************
    @PostMapping("/conger/demande")
    public ProcessInstanceResponse applyHoliday(@RequestBody TDemande tDemande) {
    	congerService.saveDemande(tDemande);
        return congerService.applyHoliday(tDemande);
    }


    @GetMapping("/manager/tasks")
    public List<TaskDetails> getTasks() {
        return congerService.getManagerTasks();
    }


    @PostMapping("/manager/approve/tasks/{taskId}/{approved}")
    public void approveTask(@PathVariable("taskId") String taskId,@PathVariable("approved") Boolean approved){
    	
    	congerService.approveHoliday(taskId,approved);

    }

    @PostMapping("/user/accept/{taskId}")
    public void acceptHoliday(@PathVariable("taskId") String taskId){
    	congerService.acceptHoliday(taskId);
    }

    @GetMapping("/RH/tasks")
    public List<TaskDetails> getUserTasks() {
        return congerService.getUserTasks();
    }


  /*  @GetMapping("/process/{processId}")
    public void checkState(@PathVariable("processId") String processId){
        congeService.checkProcessHistory(processId);
    }*/

    @PostMapping("/RH/approve/tasks/{taskId}/{approvedRH}")
    public void approveTaskRH(@PathVariable("taskId") String taskId,@PathVariable("approvedRH") Boolean approvedRH){
    	congerService.approveHolidayRH(taskId,approvedRH);
    }
    
    @GetMapping("/Recherche/{valuers}")
    public  List<TDemande> recherche(
    		@PathVariable("valuers") String Commentaire,
    		@PathVariable("valuers") String Typeconge,
    		@PathVariable("valuers") String empName
    		)
    {
    	return congerService.rechercheConge(Commentaire,Typeconge,empName);
    }

    


}