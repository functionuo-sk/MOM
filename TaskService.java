package com.mom_management.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.core.io.FileSystemResource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mom_management.dto.TaskDTO;
import com.mom_management.ennum.TaskStatus;
import com.mom_management.model.Department;
import com.mom_management.model.LatestUpdateShow;
import com.mom_management.model.Task;
import com.mom_management.model.UserDao;
import com.mom_management.repository.DepartmentRepository;
import com.mom_management.repository.LatestUpdateShowRepository;
import com.mom_management.repository.MomRepository;
import com.mom_management.repository.TaskRepository;
import com.mom_management.repository.UserRepository;

@Service
public class TaskService {

	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private MomRepository momRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private DepartmentRepository depRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private LatestUpdateShowRepository latestUpdateShowRepository;

	private UserDetails user;

	public UserDetails getDetails(UserDetails task) {

		this.user = task;
		System.out.println(user);
		return task;

	}
	
	private String getLoggedInUserFirstName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		UserDao user = userRepository.findByEmail(username);
		return user != null ? user.getFirstName() : null;
	}

	public static Date getStartOfTomorrow() {
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		LocalDateTime startOfDay = tomorrow.atStartOfDay();
		return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date getEndOfTomorrow() {
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		LocalDateTime endOfDay = tomorrow.atTime(23, 59, 59);
		return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
	}

//	@Scheduled(cron = "0 17 16 * * ?") // Runs every day at 2:25 PM
//	public void sendOverdueTaskReminders() {
//		System.out.println("Running scheduled task to send reminders for overdue tasks");
//
//		Date now = new Date();
//		List<Task> overdueTasks = taskRepository.findOverdueTasks(now);
//
//		System.out.println(overdueTasks);
//
//		if (overdueTasks.isEmpty()) {
//			System.out.println("No overdue tasks found.");
//		} else {
//			System.out.println("Overdue tasks found: " + overdueTasks.size());
//		}
//
//		for (Task task : overdueTasks) {
//			sendReminderEmail1(task);
//		}
//	}

	private void sendReminderEmail1(Task task) {
		logger.info("Running scheduled task to send reminders");
		System.out.println("hhhhhhhhhhhhhhhhhhhhhh");
		UserDao taskOwner = task.getTaskOwner();
		String email = taskOwner.getEmail();
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(email);
			helper.setSubject("Overdue Task Reminder: " + task.getTaskId());
			helper.setText("Dear " + taskOwner.getFirstName() + ",\n\n" + "This is a reminder that your task with ID "
					+ task.getTaskId() + " was due on (" + task.getDateofTarget() + ") and is now overdue.\n\n"
					+ "Please complete the task as soon as possible.\n\n" + "Regards,\nYour Task Management System");

			javaMailSender.send(mimeMessage);
			System.out.println("Reminder email sent for overdue task ID: " + task.getTaskId());
		} catch (MessagingException e) {
			e.printStackTrace();
			System.err.println("Failed to send reminder email for overdue task ID: " + task.getTaskId());
		}
	}

	private void sendReminderEmail(Task task) {
		logger.info("Running scheduled task to send reminders");
		System.out.println("hhhhhhhhhhhhhhhhhhhhhh");
		UserDao taskOwner = task.getTaskOwner();
		String email = taskOwner.getEmail();
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(email);
			helper.setSubject("Task Reminder: " + task.getTaskId());
			helper.setText("Dear " + taskOwner.getFirstName() + ",\n\n" + "This is a reminder that your task with ID "
					+ task.getTaskId() + " is due tomorrow (" + task.getDateofTarget() + ").\n\n"
					+ "Please ensure that you complete the task by the due date.\n\n"
					+ "Regards,\nYour Task Management System");

			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	// @Scheduled(cron = "0/10 * * * * ?")
//	@Scheduled(cron = "0 15 16 * * ?") // Runs every day at 1.25pm AM
//	public void sendTaskReminders() {
//		logger.info("Running scheduled task to send reminders");
//
//		Date startOfDay = getStartOfTomorrow();
//		Date endOfDay = getEndOfTomorrow();
//		System.out.println(endOfDay);
//		List<Task> tasksDueTomorrow = taskRepository.findTasks(startOfDay, endOfDay);
//		System.out.println(tasksDueTomorrow.toString());
//		if (tasksDueTomorrow.isEmpty()) {
//			logger.info("No tasks due tomorrow.");
//		} else {
//			logger.info("Tasks due tomorrow: " + tasksDueTomorrow.size());
//		}
//
//		for (Task task : tasksDueTomorrow) {
//			sendReminderEmail(task);
//		}
//	}

	public Page<Task> getAllTaskDTOs(int page, int size) {
		UserDetails details = this.user;
		System.out.println(details.getUsername());
		UserDao dao = userRepository.findByEmail(details.getUsername());
		dao.getRole();
		System.out.println("dao.getRole() " + dao.getRole());

		if ((dao.getRole().getRoleName()).equals("ADMIN")) {
			// Create a PageRequest for the requested page and size
			PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
			// Fetch tasks with pagination and sorting by the createdAt field in descending
			// order
			return taskRepository.findAll(pageRequest);
		} else {
			// Return an empty page or throw an exception if the user is not authorized
			return Page.empty(); // You can customize this based on your requirement
		}
	}

//	public Page<Task> getAllTaskDTOs1(int page, int size) {
//	    UserDetails details = this.user;
//	    System.out.println(details.getUsername());
//	    UserDao dao = userRepository.findByEmail(details.getUsername());
//	    dao.getRole();
//	    System.out.println("dao.getRole() " + dao.getRole());
//
//	    if ((dao.getRole().getRoleName()).equals("Admin")) {
//	        // Create a PageRequest for the requested page and size
//	        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
//	        // Fetch tasks with pagination and sorting by the createdAt field in descending order
//	        return taskRepository.findAll(pageRequest);
//	    } else {
//	        // Return an empty page or throw an exception if the user is not authorized
//	        return Page.empty(); // You can customize this based on your requirement
//	    }
//	}

	public List<TaskDTO> sendEmail() {

		UserDetails details = this.user;
		System.out.println(details.getUsername());

		UserDao dao = userRepository.findByUsername(details.getUsername());

		System.out.println("dao.getRole() " + dao.getEmail());
		if ((dao.getRole().getRoleName()).equals("ADMIN")) {
			return taskRepository.findAll().stream().map(this::convertTaskToTaskDTO).collect(Collectors.toList());

		} else {
			// Return an empty list or throw an exception if the user is not authorized
			return Collections.emptyList(); // You can customize this based on your requirement
		}
	}

	public void saveAll(MultipartFile file,List<TaskDTO> tasks) {
		for (TaskDTO taskDTO : tasks) {
			Task task = new Task();
			// task.setTaskId(taskDTO.getTaskId());
			task.setDateofInitiation(taskDTO.getDateofInitiation());
			task.setDateofTarget(taskDTO.getDateofTarget());

			String owner = taskDTO.getTaskOwner();
			System.out.println(owner);
			UserDao user = new UserDao();
			user.setFirstName(owner);
			task.setTaskOwner(user);
			if ((userRepository.findByFirstName(taskDTO.getTaskOwner())) != null) {
				UserDao user1 = userRepository.findByFirstName(taskDTO.getTaskOwner());
				task.setTaskOwner(user1);
			}

			task.setStatus(taskDTO.getStatus());
			task.setRemarks(taskDTO.getRemarks());
			task.setClosingDate(taskDTO.getClosingDate());
			task.setClosingRemarks(taskDTO.getClosingRemarks());
			task.setChangesInActivity(taskDTO.getChangesInActivity());
			task.setExtraTimeSpend(taskDTO.getExtraTimeSpend());

			task.setMom(momRepository.findById((long) (taskDTO.getMomId())).orElse(null));
//             task.setDepartment(depRepository.findById((long) (taskDTO.getDepartmentId())).orElse(null));
			task.setDepartment(departmentRepository.findById((long) taskDTO.getDepartmentId()).orElse(null));
			// for email

			UserDao dao = userRepository.findByFirstName(taskDTO.getTaskOwner());

			// System.out.println("Email "+dao.getEmail());

			String email = dao.getEmail();


	        try {
	            if (file != null && !file.isEmpty()) {
	                String directory = "D://newfile/";
	                File dir = new File(directory);
	                if (!dir.exists()) {
	                    dir.mkdirs(); // Create directory if it doesn't exist
	                }

	                String filePath = directory + file.getOriginalFilename();
	                System.out.println("File path: " + filePath);
	                Path path = Paths.get(filePath);
	                System.out.println("Path object: " + path);

	                // Save the file to the specified directory
	                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

	                // Send email with the file attached
	                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	                helper.setTo(email);
	                String subject = "MOM";
	                String resetLink = "http://localhost:8080/api/tasks" + "/" + task.getTaskId();

	                String content = "Dear Sir/Madam,\n\n" + "You have assigned a new task:\n\n"
	                        + "Please click on the following link to view the new task:\n\n" + resetLink + "\n\n"
	                        + "This is your last date - " + task.getDateofTarget() + "\n\n"
	                        + "If you didn't request this task, please ignore this email.\n\n"
	                        + "Regards,\nYour Application";

	                helper.setSubject(subject);
	                helper.setText(content);

	                FileSystemResource fileResource = new FileSystemResource(new File(filePath));
	                System.out.println("fileResource: " + fileResource);
	                helper.addAttachment(file.getOriginalFilename(), fileResource);

	                javaMailSender.send(mimeMessage);
	            } else {
	                // If file is null or empty, send the email without the attachment
	                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	                helper.setTo(email);
	                String subject = "MOM";
	                String resetLink = "http://localhost:8080/api/tasks" + "/" + task.getTaskId();

	                String content = "Dear Sir/Madam,\n\n" + "You have assigned a new task:\n\n"
	                        + "Please click on the following link to view the new task:\n\n" + resetLink + "\n\n"
	                        + "This is your last date - " + task.getDateofTarget() + "\n\n"
	                        + "If you didn't request this task, please ignore this email.\n\n"
	                        + "Regards,\nYour Application";

	                helper.setSubject(subject);
	                helper.setText(content);

	                javaMailSender.send(mimeMessage);
	            }
	        } catch (IOException | MessagingException e) {
	            e.printStackTrace();
	            logger.error("Error while processing file upload", e);
	        }


			taskRepository.save(task);
			LatestUpdateShow latestUpdateShow = new LatestUpdateShow();
            latestUpdateShow.setId(task.getTaskId());
            latestUpdateShow.setStatus(taskDTO.getStatus());
            latestUpdateShow.setRemarks(taskDTO.getRemarks());
            latestUpdateShow.setTask(task);
            latestUpdateShow.setUpdatedBy(getLoggedInUserFirstName());
            latestUpdateShow.setUpdatedDate(LocalDateTime.now());
            latestUpdateShowRepository.save(latestUpdateShow);
		}

	}

	public Optional<Task> getTaskDTOById(long id) {
		return taskRepository.findById(id);
	}

//	public Optional<TaskDTO> getTaskDTOById(long id) {
//	    return taskRepository.findById(id).map(task -> {
//	        TaskDTO taskDTO = convertTaskToTaskDTO(task);
//	        taskDTO.setSystemFilterTranxMaster(task.getSystemFilterTranxMaster());
//	        return taskDTO;
//	    });
//	}

	public TaskDTO saveTaskDTO1(TaskDTO taskDTO) {
		Task task = convertTaskDTOToTask(taskDTO);

		// Set the createdBy field with the logged-in user's first name
				String createdBy = getLoggedInUserFirstName();
				if (createdBy != null) {
					task.setCreatedBy(createdBy);
				}
				
		System.out.println("taskDTO.getTaskOwner() " + taskDTO.getTaskOwner());
		UserDao dao = userRepository.findByFirstName(taskDTO.getTaskOwner());
		// System.out.println("Email "+dao.getEmail());
		String email = dao.getEmail();

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		try {
			helper.setTo(email);

			String subject = "MOM";

			String resetLink = "http://localhost:8080/api/tasks" + "/" + task.getTaskId();

			String content = "Dear Sir/Madam,\n\n" + "You have assigned a new task:\n\n"
					+ "Please click on the following link to view the new task:\n\n" + resetLink + "\n\n"
					+ "This is your last date - " + task.getDateofTarget() + "\n\n"
					+ "If you didn't request this task, please ignore this email.\n\n" + "Regards,\nYour Application";

			helper.setSubject(subject);
			helper.setText(content);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		Task savedTask = taskRepository.save(task);
		return convertTaskToTaskDTO(savedTask);
	}

	public TaskDTO saveTaskDTO(TaskDTO taskDTO) {
		Task task = convertTaskDTOToTask(taskDTO);
		// Set the createdBy field with the logged-in user's first name
				String createdBy = getLoggedInUserFirstName();
				if (createdBy != null) {
					task.setCreatedBy(createdBy);
				}
//		UserDao dao = userRepository.findByFirstName(taskDTO.getTaskOwner());
//		task.setTaskOwner(dao);
		Task savedTask = taskRepository.save(task);
		return convertTaskToTaskDTO(savedTask);
	}

	public List<TaskDTO> getTasksByStatus(String status) {
		TaskStatus taskStatus;
		try {
			taskStatus = TaskStatus.valueOf(status.toUpperCase().replace(" ", "_"));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid status: " + status);
		}

		List<Task> tasks = taskRepository.findByStatus(taskStatus);
		System.out.println(tasks);
		return tasks.stream().map(this::convertTaskToTaskDTO).collect(Collectors.toList());

	}

	public List<Task> getTasksByStatus2(String status) {
		TaskStatus taskStatus;
		try {
			taskStatus = TaskStatus.valueOf(status.toUpperCase().replace(" ", "_"));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid status: " + status);
		}

		List<Task> tasks = taskRepository.findByStatus(taskStatus);
		System.out.println(tasks);
//        return tasks.stream()
//                .map(this::convertTaskToTaskDTO)
//                .collect(Collectors.toList());
		return tasks;
	}

	public TaskDTO updateTaskDTO(long id, TaskDTO taskDTO) {
		// Check if the task with the given ID exists
		Optional<Task> optionalTask = taskRepository.findById(id);
		if (optionalTask.isPresent()) {
			Task existingTask = optionalTask.get();

			// Update the fields of the existing task with the data from the DTO
			existingTask.setDateofInitiation(taskDTO.getDateofInitiation());
			existingTask.setDateofTarget(taskDTO.getDateofTarget());
			existingTask.setTaskOwner(userRepository.findByFirstName(taskDTO.getTaskOwner()));
			existingTask.setStatus(taskDTO.getStatus());
			existingTask.setRemarks(taskDTO.getRemarks());
			existingTask.setClosingDate(taskDTO.getClosingDate());
			existingTask.setClosingRemarks(taskDTO.getClosingRemarks());
			existingTask.setChangesInActivity(taskDTO.getChangesInActivity());
			existingTask.setExtraTimeSpend(taskDTO.getExtraTimeSpend());
			existingTask.setUpdatedBy(taskDTO.getUpdatedBy());
			existingTask.setUpdatedDate(taskDTO.getUpdatedDate());

			// Assuming there is a MomRepository to fetch Mom entities by id
			// For simplicity, I'm not implementing it here
			// You may want to implement this logic based on your project structure
			existingTask.setMom(momRepository.findById(taskDTO.getMomId()).orElse(null));

			// Save the updated task
			Task updatedTask = taskRepository.save(existingTask);

			// Convert the updated task back to DTO and return it
			return convertTaskToTaskDTO(updatedTask);
		} else {
			throw new IllegalArgumentException("Task not found with ID: " + id);
		}
	}

//	public void deleteTask(long id) {
//
//		UserDetails details = this.user;
//		System.out.println(details.getUsername());
//
//		UserDao dao = userRepository.findByUsername(details.getUsername());
//		dao.getRole();
//		System.out.println("dao.getRole() " + dao.getRole().getRoleName());
//		if ((dao.getRole().getRoleName()).equals("ADMIN")) {
//			taskRepository.deleteById(id);
//
//		} else {
//			// Return an empty list or throw an exception if the user is not authorized
//			System.out.println("id is not present"); // You can customize this based on your requirement
//		}
//
//	}
//	public void deleteTask(long id) {
//	    UserDetails details = this.user;
//	    System.out.println(details.getUsername());
//
//	    UserDao dao = userRepository.findByUsername(details.getUsername());
//	    dao.getRole();
//	    System.out.println("dao.getRole() " + dao.getRole().getRoleName());
//	    if ((dao.getRole().getRoleName()).equals("admin")) {
//	        // Check if the task with the given ID exists
//	        Optional<Task> optionalTask = taskRepository.findById(id);
//	        if (optionalTask.isPresent()) {
//	            // If the task exists, delete it from the database
//	            taskRepository.deleteById(id);
//	        } else {
//	            // If the task does not exist, throw an exception or handle the error accordingly
//	            throw new IllegalArgumentException("Task not found with ID: " + id);
//	        }
//	    } else {
//	        // Return an empty list or throw an exception if the user is not authorized
//	        System.out.println("User is not authorized to delete tasks."); // You can customize this based on your requirement
//	    }
//	}

//	public void deleteTask(long id) {
//		 taskRepository.deleteById(id);				
//	}

	public void deleteTask(long id) {
		UserDetails details = this.user;
		System.out.println(details.getUsername());

		UserDao dao = userRepository.findByEmail(details.getUsername());
		if (dao != null) {
			dao.getRole();
			System.out.println("dao.getRole() " + dao.getRole().getRoleName());
			if ("ADMIN".equals(dao.getRole().getRoleName())) {
				// Check if the task with the given ID exists
				Optional<Task> optionalTask = taskRepository.findById(id);
				if (optionalTask.isPresent()) {
					// If the task exists, delete it from the database
					taskRepository.deleteById(id);
				} else {
					// If the task does not exist, throw an exception or handle the error
					// accordingly
					throw new IllegalArgumentException("Task not found with ID: " + id);
				}
			} else {
				// Return an empty list or throw an exception if the user is not authorized
				System.out.println("User is not authorized to delete tasks."); // You can customize this based on your
																				// requirement
			}
		} else {
			// Handle the case where the UserDao object is null
			throw new IllegalArgumentException("User not found with username: " + details.getUsername());
		}
	}

	private TaskDTO convertTaskToTaskDTO(Task task) {
		TaskDTO taskdto = new TaskDTO();

		taskdto.setTaskId(task.getTaskId());
		taskdto.setDateofInitiation(task.getDateofInitiation());
		taskdto.setDateofTarget(task.getDateofTarget());

		System.out.println(task);

		// Check if taskOwner is not null before accessing its properties
		if (task.getTaskOwner() != null) {
			UserDao taskOwner = task.getTaskOwner();

			System.out.println(taskOwner);

			// Check if the user with the given ID exists in the repository
			Optional<UserDao> userOptional = userRepository.findById(taskOwner.getUserId());

			if (userOptional.isPresent()) {
				taskdto.setTaskOwner(userOptional.get().getFirstName());
			}
		}

		taskdto.setStatus(task.getStatus());
		taskdto.setRemarks(task.getRemarks());
		taskdto.setClosingDate(task.getClosingDate());
		taskdto.setClosingRemarks(task.getClosingRemarks());
		taskdto.setChangesInActivity(task.getChangesInActivity());
		taskdto.setExtraTimeSpend(task.getExtraTimeSpend());
		taskdto.setCreatedBy(task.getCreatedBy());
		taskdto.setCreatedDate(task.getCreatedDate());
		taskdto.setUpdatedBy(task.getUpdatedBy());
		taskdto.setUpdatedDate(task.getUpdatedDate());

		if (task.getMom() != null) {
			taskdto.setMomId(task.getMom().getId());
			taskdto.setDescription(task.getMom().getDescription());
		}

		taskdto.setDepartmentId(task.getDepartment().getDepartmentId());

		return taskdto;
	}

//    private TaskDTO convertTaskToTaskDTO(Task task) {
//        // Implement logic to convert a Task entity to a TaskDTO
//        // You can use libraries like ModelMapper or manually map the fields
//        // For simplicity, I'm manually mapping the fields here
//    	
//        TaskDTO taskdto = new TaskDTO();
//        //task.setTaskId(taskDTO.getTaskId());
//        taskdto.setDateofInitiation(task.getDateofInitiation());
//        taskdto.setDateofTarget(task.getDateofTarget());
//        taskdto.setDepartment(task.getDepartment());
//        
//        if((userRepository.findById(task.getTaskOwner().getUserId()))!=null) {
//        	taskdto.setTaskOwner(task.getTaskOwner().getFirstName());
//        }
//        
//        
//        taskdto.setStatus(task.getStatus());
//        taskdto.setRemark1(task.getRemark1());
//        taskdto.setRemarks2(task.getRemarks2());
//        taskdto.setClosingDate(task.getClosingDate());
//        taskdto.setClosingRemarks(task.getClosingRemarks());
//        taskdto.setChangesInActivity(task.getChangesInActivity());
//        taskdto.setExtraTimeSpend(task.getExtraTimeSpend());
//        if (task.getMom() != null) {
//            taskdto.setMomId(task.getMom().getId());
//        }
//		return taskdto;
//        
//    }

	private Task convertTaskDTOToTask(TaskDTO taskDTO) {

		Task task = new Task();
		// task.setTaskId(taskDTO.getTaskId());
		task.setDateofInitiation(taskDTO.getDateofInitiation());
		task.setDateofTarget(taskDTO.getDateofTarget());

		String owner = taskDTO.getTaskOwner();
		System.out.println(owner);
		UserDao user = new UserDao();
		user.setFirstName(owner);
		task.setTaskOwner(user);
		if ((userRepository.findByFirstName(taskDTO.getTaskOwner())) != null) {
			UserDao user1 = userRepository.findByFirstName(taskDTO.getTaskOwner());
			task.setTaskOwner(user1);
		}

		task.setStatus(taskDTO.getStatus());
		task.setRemarks(taskDTO.getRemarks());
		task.setClosingDate(taskDTO.getClosingDate());
		task.setClosingRemarks(taskDTO.getClosingRemarks());
		task.setChangesInActivity(taskDTO.getChangesInActivity());
		task.setExtraTimeSpend(taskDTO.getExtraTimeSpend());

		task.setCreatedBy(taskDTO.getCreatedBy());
		task.setCreatedDate(taskDTO.getCreatedDate());
		task.setUpdatedBy(taskDTO.getUpdatedBy());
		task.setUpdatedDate(taskDTO.getUpdatedDate());

		task.setMom(momRepository.findById((long) (taskDTO.getMomId())).orElse(null));
		task.setDepartment(depRepository.findById((long) (taskDTO.getDepartmentId())).orElse(null));
		// Assuming there is a MomRepository to fetch Mom entities by id
		// For simplicity, I'm not implementing it here
		// You may want to implement this logic based on your project structure
		// and relationships between Task and Mom entities
		return task;
	}
	
	public Page<TaskDTO> getAllDataByUserId(Pageable pageable) {
	    // Retrieve the username of the logged-in user
	    String username = user.getUsername();

	    // Find the UserDao based on the username
	    UserDao userDao = userRepository.findByEmail(username);

	    // If UserDao is found and not null
	    if (userDao != null) {
	        // Retrieve paginated tasks associated with the user
	        Page<Task> userTasks = taskRepository.findAllByTaskOwner(userDao, pageable);

	        // Convert the page of tasks to a page of TaskDTOs
	        return userTasks.map(this::convertTaskToTaskDTO);
	    } else {
	        // If UserDao is null, return an empty page
	        return Page.empty(pageable);
	    }
	}

//	public List<TaskDTO> getAllDataByUserId() {
//		// Retrieve the username of the logged-in user
//		String username = user.getUsername();
//
//		// Find the UserDao based on the username
//		UserDao userDao = userRepository.findByEmail(username);
//
//		// If UserDao is found and not null
//		if (userDao != null) {
//			// Retrieve all tasks associated with the user
//			List<Task> userTasks = taskRepository.findAllByTaskOwner(userDao);
//
//			// Convert the list of tasks to a list of TaskDTOs
//			List<String> taskInfoList = userTasks.stream()
//					.map(task -> "Task ID: " + task.getTaskId() + " + Task Owner: " + task.getTaskOwner())
//					.collect(Collectors.toList());
//
//			taskInfoList.forEach(System.out::println);
//			return userTasks.stream().map(this::convertTaskToTaskDTO).collect(Collectors.toList());
//		} else {
//			// If UserDao is null, return an empty list
//			return Collections.emptyList();
//		}
//	}
	// API to fetch all tasks related to the currently logged-in user deepak
//    public List<TaskDTO> getAllDataByUserId() {
//        // Retrieve the username of the logged-in user
//        String username = user.getUsername();
//        
//        // Find the UserDao based on the username
//        UserDao userDao = userRepository.findByEmail(username);
//        
//        // If UserDao is found and not null
//        if ((userDao.getRole().getRoleName()).equals("user")) {
//            // Retrieve all tasks associated with the user
//            List<Task> userTasks = taskRepository.findAllByTaskOwner(userDao);
//            
//            // Convert the list of tasks to a list of TaskDTOs
//            return userTasks.stream()
//                    .map(this::convertTaskToTaskDTO)
//                    .collect(Collectors.toList());
//        } else {
//            // If UserDao is null, return an empty list
//            return Collections.emptyList();
//        }
//    }

	public List<TaskDTO> getTasksByStatusAndUserId(String status) {
		// Retrieve the username of the logged-in user
		String username = user.getUsername();

		// Find the UserDao based on the username
		UserDao userDao = userRepository.findByEmail(username);

		TaskStatus taskStatus;
		try {
			taskStatus = TaskStatus.valueOf(status.toUpperCase().replace(" ", "_"));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid status: " + status);
		}

		// List<Task> tasks = taskRepository.findByStatus(taskStatus);

		// If UserDao is found and not null
		if (userDao != null) {
			// Retrieve all tasks associated with the user
			System.out.println(taskStatus);
			List<Task> userTasks = taskRepository.findByStatusAndTaskOwner(taskStatus, userDao);

			// Convert the list of tasks to a list of TaskDTOs
			return userTasks.stream().map(this::convertTaskToTaskDTO).collect(Collectors.toList());
		} else {
			// If UserDao is null, return an empty list
			return Collections.emptyList();
		}
	}

	public List<TaskDTO> getAllTasksByMomId(long momId) {
		return taskRepository.findAllByMomId(momId).stream().map(this::convertTaskToTaskDTO)
				.collect(Collectors.toList());
	}

//	public Task updateTaskDTO1(long id, TaskDTO taskDTO) {
//
//		String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
//		UserDao dao = userRepository.findByEmail(loggedInUsername);
//		String username = dao.getFirstName();
//		Task updatedTask = null;
//		Optional<Task> optionalTask = taskRepository.findById(id);
//		List<LatestUpdateShow> latestUpdateShows = new ArrayList<>();
//		LatestUpdateShow latestUpdateShowDTO = new LatestUpdateShow();
//
//		latestUpdateShowDTO.setId(latestUpdateShowDTO.getId());
//		latestUpdateShowDTO.setStatus(taskDTO.getStatus());
//		latestUpdateShowDTO.setRemarks(taskDTO.getRemarks());
//		latestUpdateShowDTO.setUpdatedBy(username);
//
//		if (optionalTask.isPresent()) {
//			Task existingTask = optionalTask.get();
//
//			existingTask.setDateofInitiation(taskDTO.getDateofInitiation());
//			existingTask.setDateofTarget(taskDTO.getDateofTarget());
//			existingTask.setTaskOwner(userRepository.findByFirstName(taskDTO.getTaskOwner()));
//			System.out.println(taskDTO.getStatus());
//			existingTask.setStatus(taskDTO.getStatus());
//			existingTask.setRemarks(taskDTO.getRemarks());
//			existingTask.setClosingDate(taskDTO.getClosingDate());
//			existingTask.setClosingRemarks(taskDTO.getClosingRemarks());
//			existingTask.setChangesInActivity(taskDTO.getChangesInActivity());
//			existingTask.setExtraTimeSpend(taskDTO.getExtraTimeSpend());
//			existingTask.setUpdatedBy(username);
//			existingTask.setDepartment(departmentRepository.findById((long) taskDTO.getDepartmentId()).orElse(null));
//			existingTask.setUpdatedDate(LocalDateTime.now());
//
//			existingTask.setMom(momRepository.findById(taskDTO.getMomId()).orElse(null));
//
////            List<LatestUpdateShowDTO> latestUpdateShowDTO1 = taskDTO.getLatestUpdateShows();
////            List<LatestUpdateShow> list = new ArrayList<>();
////            for(LatestUpdateShowDTO latestUpdateShowDTO:latestUpdateShowDTO1) {
////                LatestUpdateShow latestUpdateShow = new LatestUpdateShow();
////                latestUpdateShow.setId(latestUpdateShowDTO.getId());
////                latestUpdateShow.setStatus(latestUpdateShowDTO.getStatus());
////                latestUpdateShow.setRemarks(latestUpdateShowDTO.getRemarks());
////                latestUpdateShow.setUpdatedBy(latestUpdateShowDTO.getUpdatedBy());
////                latestUpdateShow.setUpdatedDate(LocalDateTime.now());
////                
////                latestUpdateShow.setTask(existingTask);
////                list.add(latestUpdateShow);
////            }
//
//			latestUpdateShowDTO.setUpdatedDate(LocalDateTime.now());
//			latestUpdateShows.add(latestUpdateShowDTO);
//			latestUpdateShowDTO.setTask(existingTask);
//			existingTask.setLatestUpdateShows(latestUpdateShows);
//			updatedTask = taskRepository.save(existingTask);
//		}
//		return updatedTask;
//	}
	
	
	public Task updateTaskDTO1(long id, TaskDTO taskDTO,MultipartFile file) {

		String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		UserDao dao = userRepository.findByEmail(loggedInUsername);
		String username = dao.getUsername();
		Task updatedTask = null;
		Optional<Task> optionalTask = taskRepository.findById(id);
		List<LatestUpdateShow> latestUpdateShows = new ArrayList<>();
		LatestUpdateShow latestUpdateShowDTO = new LatestUpdateShow();

		latestUpdateShowDTO.setId(latestUpdateShowDTO.getId());
		latestUpdateShowDTO.setStatus(taskDTO.getStatus());
		latestUpdateShowDTO.setRemarks(taskDTO.getRemarks());
		latestUpdateShowDTO.setUpdatedBy(getLoggedInUserFirstName());

		if (optionalTask.isPresent()) {
			Task existingTask = optionalTask.get();

			existingTask.setDateofInitiation(taskDTO.getDateofInitiation());
			existingTask.setDateofTarget(taskDTO.getDateofTarget());
			existingTask.setTaskOwner(userRepository.findByFirstName(taskDTO.getTaskOwner()));
			System.out.println(taskDTO.getStatus());
			existingTask.setStatus(taskDTO.getStatus());
			existingTask.setRemarks(taskDTO.getRemarks());
			existingTask.setClosingDate(taskDTO.getClosingDate());
			existingTask.setClosingRemarks(taskDTO.getClosingRemarks());
			existingTask.setChangesInActivity(taskDTO.getChangesInActivity());
			existingTask.setExtraTimeSpend(taskDTO.getExtraTimeSpend());
			existingTask.setUpdatedBy(username);
			existingTask.setDepartment(departmentRepository.findById((long) taskDTO.getDepartmentId()).orElse(null));
			existingTask.setUpdatedDate(LocalDateTime.now());

			existingTask.setMom(momRepository.findById(taskDTO.getMomId()).orElse(null));

//            List<LatestUpdateShowDTO> latestUpdateShowDTO1 = taskDTO.getLatestUpdateShows();
//            List<LatestUpdateShow> list = new ArrayList<>();
//            for(LatestUpdateShowDTO latestUpdateShowDTO:latestUpdateShowDTO1) {
//                LatestUpdateShow latestUpdateShow = new LatestUpdateShow();
//                latestUpdateShow.setId(latestUpdateShowDTO.getId());
//                latestUpdateShow.setStatus(latestUpdateShowDTO.getStatus());
//                latestUpdateShow.setRemarks(latestUpdateShowDTO.getRemarks());
//                latestUpdateShow.setUpdatedBy(latestUpdateShowDTO.getUpdatedBy());
//                latestUpdateShow.setUpdatedDate(LocalDateTime.now());
//                
//                latestUpdateShow.setTask(existingTask);
//                list.add(latestUpdateShow);
//            }

			latestUpdateShowDTO.setUpdatedDate(LocalDateTime.now());
			latestUpdateShows.add(latestUpdateShowDTO);
			latestUpdateShowDTO.setTask(existingTask);
			existingTask.setLatestUpdateShows(latestUpdateShows);
			
			if (file != null && !file.isEmpty()) {
	            try {
	                String directory = "D://newfile/";
	                File dir = new File(directory);
	                if (!dir.exists()) {
	                    dir.mkdirs(); // Create directory if it doesn't exist
	                }

	                String filePath = directory + file.getOriginalFilename();
	                Path path = Paths.get(filePath);
	                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

	                // Send email with the file attached
	                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	                helper.setTo(existingTask.getTaskOwner().getEmail());
	                String subject = "MOM";
	                String resetLink = "http://localhost:8080/api/tasks" + "/" + existingTask.getTaskId();

	                String content = "Dear Sir/Madam,\n\n" + "You have updated a task:\n\n"
	                        + "Please click on the following link to view the updated task:\n\n" + resetLink + "\n\n"
	                        + "This is your last date - " + existingTask.getDateofTarget() + "\n\n"
	                        + "If you didn't request this task, please ignore this email.\n\n"
	                        + "Regards,\nYour Application";

	                helper.setSubject(subject);
	                helper.setText(content);

	                FileSystemResource fileResource = new FileSystemResource(new File(filePath));
	                helper.addAttachment(file.getOriginalFilename(), fileResource);

	                javaMailSender.send(mimeMessage);
	            } catch (IOException | MessagingException e) {
	                e.printStackTrace();
	                logger.error("Error while processing file upload", e);
	            }
	        }
			
			updatedTask = taskRepository.save(existingTask);
		}
		return updatedTask;
	}


	public List<Task> getAllTask() {

		List<Task> savedTask = taskRepository.findAll();
		return savedTask;
	}

	public static boolean onlyDigits(String input) {

		for (int i = 0; i < input.length(); i++) {
			char currentChar = input.charAt(i);

			// Check the character
			if (!Character.isDigit(currentChar)) {
				System.out.println(currentChar + " is not a digit.");
				return false;
			}
		}
		System.out.println("aa" + input);
		return true;
	}

	public List<Task> searchTasks(String searchKey) {

		boolean value = onlyDigits(searchKey);
		Long number = null;
		Task task = new Task();

		List<Task> tasks = new ArrayList<>();
		if (value) {
			try {
				number = Long.parseLong(searchKey);
				System.out.println("Converted number: " + number);
				task = taskRepository.findByTaskId(number);
				tasks.add(task);
				return tasks;
			} catch (NumberFormatException e) {
				System.out.println("Invalid numeric string: " + searchKey);
			}
		} else {

			try {
				TaskStatus status = TaskStatus.valueOf(searchKey.toUpperCase());
				return taskRepository.findByStatus(status);
			} catch (IllegalArgumentException e) {
				System.out.println("not valid");
			}

			UserDao user = userRepository.findByFirstName(searchKey);
			Department dep = departmentRepository.findByDepartmentName(searchKey);
			// Department dep1=departmentRepository.findById((long)
			// dep.getDepartmentId()).orElse(null);
			// System.out.println(dep.getDepartmentName());
			if (user != null || dep != null) {
				tasks = taskRepository.findAllByDepartment(dep);
				// System.out.println(tasks+"jjjjjjjjjjj");
				tasks = taskRepository.findAllByTaskOwner(user);

				for (Task task1 : tasks) {
					System.out.println("Task ID: " + task1.getTaskId());
					System.out.println("Task Owner: " + task1.getTaskOwner());
					System.out.println("Status: " + task1.getDepartment().getDepartmentName());
					// Print other task details if needed
					System.out.println("----------------------------------");
				}
				return tasks;
			} else {

				return List.of();
			}

		}
		return tasks;
	}

//    public List<Task> searchAnyTasks(String searchKey) {
//    	System.out.println("searchKey.length()");
//    	System.out.println(searchKey);
//    	
//    	boolean value=onlyDigits(searchKey);
//    	Long number =null;
//    	Task task=new Task();
//    	UserDao user=new UserDao();
//    	Department dep=new Department();
//    	List<Task> tasks=new ArrayList<>();
//    	if(value) {
//    		   try {
//    	            number = Long.parseLong(searchKey);
//    	            System.out.println("Converted number: " + number);
//    	            task=taskRepository.findByTaskId(number);
//    	            tasks.add(task);
//    	        } catch (NumberFormatException e) {
//    	            System.out.println("Invalid numeric string: " + searchKey);
//    	        }
//    	}
//    	else {
//    		    		
////    		user.setFirstName(searchKey);
////    		    		
////    		System.out.println("hhhhhhhhhnn");
////    		dep.setDepartmentName(searchKey);
////    		System.out.println(dep +" "+ user);
////    		if(searchKey!=null) {
////    			user=userRepository.findByFirstName(searchKey);
////    			dep=departmentRepository.findByDepartmentName(searchKey);
////    		} 
////    		
//    		//status
//    		TaskStatus taskStatus;
//    		try {
//    			taskStatus = TaskStatus.valueOf(searchKey.toUpperCase());
//    		} catch (IllegalArgumentException e) {
//    			throw new IllegalArgumentException("Invalid status: " + searchKey);
//    		}
//
//    		tasks = taskRepository.findByStatus(taskStatus);
//    		
//    		//findAllByTaskOwner
//    		tasks=taskRepository.findAllByTaskOwner(user);
//    		System.out.println(tasks);
//    	}
//    	System.out.println(user.getFirstName());
//        return tasks;
//    }

}
