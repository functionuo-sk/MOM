package com.mom_management.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mom_management.ennum.TaskStatus;
import com.mom_management.model.Department;
import com.mom_management.model.Task;
import com.mom_management.model.UserDao;

public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByStatus(TaskStatus status);

	Page<Task> findAllByTaskOwner(UserDao taskOwner, Pageable pageable);
	List<Task> findAllByMomId(long momId);

	List<Task> findAllByTaskOwner(UserDao userDao);

	List<Task> findByStatusAndTaskOwner(TaskStatus status, UserDao taskOwner);

	Task findByTaskId(Long taskId);

	List<Task> findByTaskIdOrTaskOwnerOrDepartment(Long taskId, UserDao taskOwner, Department department);

	List<Task> findAllByDepartment(Department department);

	@Query("SELECT t FROM Task t WHERE t.dateofTarget < :now")
	List<Task> findOverdueTasks(@Param("now") Date now);

	@Query("SELECT t FROM Task t WHERE t.dateofTarget BETWEEN :startOfDay AND :endOfDay")
	List<Task> findTasks(@Param("startOfDay") Date startOfDay, @Param("endOfDay") Date endOfDay);
}
