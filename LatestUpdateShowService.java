package com.mom_management.service;


import com.mom_management.model.LatestUpdateShow;
import com.mom_management.model.Task;
import com.mom_management.repository.LatestUpdateShowRepository;
import com.mom_management.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LatestUpdateShowService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	 private final LatestUpdateShowRepository latestUpdateShowRepository;

	    @Autowired
	    public LatestUpdateShowService(LatestUpdateShowRepository latestUpdateShowRepository) {
	        this.latestUpdateShowRepository = latestUpdateShowRepository;
	    }

//	    public List<LatestUpdateShow> getAllByTaskOrderByUpdatedDateAsc(Long taskId) {
//	        Task task = new Task();
//	        task.setTaskId(taskId); // Create a new Task object with the provided taskId
//	        return latestUpdateShowRepository.findAllByTaskOrderByUpdatedDateAsc(task);
//	    }
	    
	    public List<LatestUpdateShow> getAllLatestUpdatesFromTaskId(Long taskId) {
	        // Retrieve the Task by taskId
	        Task task = getTaskById(taskId); // Assuming you have a method to retrieve a Task by its ID

	        if (task != null) {
	            // Find all LatestUpdateShow entities associated with the given Task and order by updatedDate asc
	            return latestUpdateShowRepository.findAllByTaskOrderByUpdatedDateAsc(task);
	        } else {
	            // Handle case where Task with given taskId is not found
	            return null;
	        }
	    }

	    
	    private Task getTaskById(Long taskId) {
	        // Implement logic to retrieve Task by its ID using TaskRepository
	        return taskRepository.findByTaskId(taskId);
	    }
	    
	    
}