package com.mom_management.controller;


import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mom_management.dto.TaskDTO;
import com.mom_management.model.Task;
import com.mom_management.service.TaskService;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    
    @Autowired
	private ObjectMapper mapper;
    
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskDTO>> getTasksByStatusForIndex(@PathVariable String status) {
        //List<TaskDTO> tasks = taskService.getTasksByStatus(status);
        return new ResponseEntity<>(taskService.getTasksByStatus(status), HttpStatus.OK);
    }
    
    @GetMapping("/statuss/{status}")
    public ResponseEntity<List<Task>> getTasksByStatuss(@PathVariable String status) {
        //List<TaskDTO> tasks = taskService.getTasksByStatus(status);
        return new ResponseEntity<>(taskService.getTasksByStatus2(status), HttpStatus.OK);
    }
//    
//    @GetMapping("/status/{status}")
//    public ResponseEntity<Page<TaskDTO>> getAllTasks(@PathVariable String status,
//        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
//        @RequestParam(value = "size", defaultValue = "10") Integer size
//    ) {
//        // Adjust the page number to start from 0-based index
//        Page<TaskDTO> tasks = taskService.getAllTaskDTOs(status,page -1, size);
//        return new ResponseEntity<>(tasks, HttpStatus.OK);
//    }

    
    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        // Subtract 1 from page number to adjust it to start from 0-based index
        Page<Task> tasks = taskService.getAllTaskDTOs(page - 1, size);
        System.out.println("Tasks Page: " + tasks.toString());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Require USER or ADMIN role to access
    public ResponseEntity<Task> getTaskById(@PathVariable long id) {
        Optional<Task> task = taskService.getTaskDTOById(id);
        return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    //@PreAuthorize("hasRole('USER')") // Require ADMIN role to access
    public ResponseEntity<TaskDTO> saveTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO savedTaskDTO = taskService.saveTaskDTO1(taskDTO);
        return new ResponseEntity<>(savedTaskDTO, HttpStatus.CREATED);
    }
    
    
//    @PostMapping("/task")
//    public ResponseEntity<?> createTasks(@RequestBody List<TaskDTO> tasks) {
//        // Process the list of tasks
//        // Example logic:
//        taskService.saveAll(tasks);
//        return new ResponseEntity<>(tasks, HttpStatus.OK);
//    }
    
    @PostMapping("/task")
    public ResponseEntity<?> createTasks(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("tasks") String tasksJson) throws IOException, ParseException {

    	List<TaskDTO> tasks = new ArrayList<>();
        try {
            //ObjectMapper mapper = new ObjectMapper();
            tasks = mapper.readValue(tasksJson, new TypeReference<List<TaskDTO>>() {});
            //System.out.println("tasksJson: " + tasksJson);
            System.out.println("tasks: " + tasks);
        	 System.out.println("tasksJson"+tasksJson);
             //tasks = parseTasksJson(tasksJson);
             System.out.println(tasks);
             taskService.saveAll(file, tasks);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid tasks JSON", HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    
//old my code
//    @PutMapping("/{id}")
//    //@PreAuthorize("hasRole('USER')") // Require ADMIN role to access
//    public ResponseEntity<TaskDTO> updateTask(@PathVariable long id, @RequestBody TaskDTO taskDTO) {
//        // Call the service method to update the task
//        // You need to implement this method in your TaskService
//        TaskDTO updatedTaskDTO = taskService.updateTaskDTO(id, taskDTO);
//        if (updatedTaskDTO != null) {
//            return new ResponseEntity<>(updatedTaskDTO, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')") // Require ADMIN role to access
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("/mom/{momId}")
    public ResponseEntity<List<TaskDTO>> getAllTasksByMomId(@PathVariable long momId) {
        List<TaskDTO> tasks = taskService.getAllTasksByMomId(momId);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    
    @GetMapping("/user-tasks")
    public ResponseEntity<Page<TaskDTO>> getAllUserTasks(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page -1, size);
        Page<TaskDTO> taskDTOPage = taskService.getAllDataByUserId(pageable);
        return new ResponseEntity<>(taskDTOPage, HttpStatus.OK);
    }
//    @GetMapping("/user-tasks")
//    public List<TaskDTO> getAllUserTasks() {
//        return taskService.getAllDataByUserId();
//    }
    
//    @GetMapping("/user-tasks")
//    public ResponseEntity<Page<Task>> getAllUserTasks(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
//            @RequestParam(value = "size", defaultValue = "10") Integer size) {
//    	Page<Task> tasks = taskService.getAllDataByUserId(page - 1, size);
//        System.out.println("Tasks Page: " + tasks.toString());
//        return new ResponseEntity<>(tasks, HttpStatus.OK);
//    }
    
//    @GetMapping
//    public ResponseEntity<Page<Task>> getAllTasks(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
//                                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
//        // Subtract 1 from page number to adjust it to start from 0-based index
//        Page<Task> tasks = taskService.getAllTaskDTOs(page - 1, size);
//        System.out.println("Tasks Page: " + tasks.toString());
//        return new ResponseEntity<>(tasks, HttpStatus.OK);
    
    @GetMapping("/user-tasks/status/{status}")
    public List<TaskDTO> getAllUserTasksByStatus(@PathVariable String status) {
        return taskService.getTasksByStatusAndUserId(status);
    }
   
//    //new deepak code
//    @PutMapping("/{id}")
//    // @PreAuthorize("hasRole('USER')") // Require ADMIN role to access
//    public ResponseEntity<Task> updateTask(@PathVariable long id, @RequestBody TaskDTO taskDTO) {
//        // Call the service method to update the task
//        // You need to implement this method in your TaskService
//        Task updatedTaskDTO = taskService.updateTaskDTO1(id, taskDTO);
//        if (updatedTaskDTO != null) {
//            return new ResponseEntity<>(updatedTaskDTO, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable long id,
                                           @RequestParam(value = "file", required = false) MultipartFile file,
                                           @RequestParam("taskDTO") String taskDTOJson) {
        try {
            // Convert taskDTOJson to TaskDTO object
        	TaskDTO taskDTO = mapper.readValue(taskDTOJson,  TaskDTO.class);
            //TaskDTO taskDTO = new ObjectMapper().readValue(taskDTOJson, TaskDTO.class);

            // Call the service method to update the task
        	System.out.println("taskDTOJson"+taskDTOJson);
            Task updatedTask = taskService.updateTaskDTO1(id, taskDTO, file);
            if (updatedTask != null) {
                return new ResponseEntity<>(updatedTask, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
    	List<Task> tasks = taskService.getAllTask();
    	return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


    @GetMapping("/search")
    public List<Task> searchTasks(@RequestParam String searchKey) {

    	return taskService.searchTasks(searchKey);
    }

//    @GetMapping
//    public ResponseEntity<List<TaskDTO>> getAllTasks() {
//        List<TaskDTO> tasks = taskService.getAllTaskDTOs();
//        return new ResponseEntity<>(tasks, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<TaskDTO> getTaskById(@PathVariable long id) {
//        Optional<TaskDTO> task = taskService.getTaskDTOById(id);
//        return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @PostMapping
//    public ResponseEntity<TaskDTO> saveTask(@RequestBody TaskDTO taskDTO) {
//        TaskDTO savedTaskDTO = taskService.saveTaskDTO(taskDTO);
//        return new ResponseEntity<>(savedTaskDTO, HttpStatus.CREATED);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
//        taskService.deleteTask(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}

