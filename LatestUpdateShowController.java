package com.mom_management.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mom_management.model.LatestUpdateShow;
import com.mom_management.model.Task;
import com.mom_management.service.LatestUpdateShowService;
import com.mom_management.service.TaskService;

@RestController
@RequestMapping("/api/latest-updates")
public class LatestUpdateShowController {

	@Autowired
	private LatestUpdateShowService latestUpdateShowService;
	
	@Autowired
	private TaskService taskService;

//	 @GetMapping("/tasks/{taskId}")
//	    public ResponseEntity<List<LatestUpdateShow>> getAllByTaskOrderByUpdatedDateAsc(@PathVariable Long taskId) {
//	        List<LatestUpdateShow> latestUpdates = latestUpdateShowService.getAllByTaskOrderByUpdatedDateAsc(taskId);
//	        return ResponseEntity.ok(latestUpdates);
//	    }
	@GetMapping("/tasks/{taskId}")
    public ResponseEntity<?> getAllLatestUpdatesFromTaskId(@PathVariable("taskId") Long taskId) {
        List<LatestUpdateShow> latestUpdates = latestUpdateShowService.getAllLatestUpdatesFromTaskId(taskId);

        if (latestUpdates != null && !latestUpdates.isEmpty()) {
            return ResponseEntity.ok(latestUpdates);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
}