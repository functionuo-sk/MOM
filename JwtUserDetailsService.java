package com.mom_management.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mom_management.error.ErrorResponse;
import com.mom_management.exception.ResourceNotFoundException;
import com.mom_management.message.ApiResponseFormat;
import com.mom_management.model.Role;
import com.mom_management.model.UserDao;
import com.mom_management.model.UserDto;
import com.mom_management.repository.DepartmentRepository;
import com.mom_management.repository.RoleRepository;
import com.mom_management.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userDao;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	@Autowired
	private JavaMailSender javaMailSender;

	private UserDetails user;

	public UserDetails getDetails(UserDetails task) {

		this.user = task;
		System.out.println(user);
		return task;

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDao user = userDao.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of());
	}

	public UserDao getUserByUsername(String username) throws UsernameNotFoundException {
		UserDao user = userDao.findByUsername(username);
		return user;
	}

//	public UserDao save1(UserDto user) throws DuplicateUserException {
//		UserDao userDup = userDao.findByUsername(user.getUsername());
//		if (userDup != null) {
//			throw new DuplicateUserException("Username already exists");
//		}
//
//		UserDao newUser = new UserDao();
//		newUser.setUsername(user.getUsername());
//		newUser.setEmail(user.getEmail());
////   	newUser.setToken(user.getUsername());
//	
//		newUser.setRole(roleRepository.findById((int) user.getRoleId()).orElse(null));
//
//		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
//		return userDao.save(newUser);
//	}

	public ResponseEntity<?> save(UserDto userDto) {
		Role role = null;

		// Check if the provided role is "User" or "Admin"
		if ("USER".equalsIgnoreCase(userDto.getRoleName())) {
			role = roleRepository.findByRoleName("USER");
		} else if ("ADMIN".equalsIgnoreCase(userDto.getRoleName())) {
			role = roleRepository.findByRoleName("ADMIN");
		} else {
			// Invalid role provided
			ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
					"Invalid role or role not found.", "Failed");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		// Check if the role is null (i.e., not found)
		if (role == null) {
			ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
					"Invalid role or role not found.", "Failed");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		// Check if any users exist in the database
		long userCount = userDao.count();
		boolean isFirstUser = userCount == 0;

		// If it's the first user and they are an admin, allow registration without
		// createdBy
		if (isFirstUser && "ADMIN".equalsIgnoreCase(userDto.getRoleName())) {
			UserDao user = new UserDao();
			user.setUserId(userDto.getUserId());
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setUsername(userDto.getUsername());
			user.setEmail(userDto.getEmail());
			user.setCreated_By(null); // Allow null for the first admin
			user.setCreatedDate(userDto.getCreateDate());
			user.setUpdated_By(userDto.getUpdatedBy());
			user.setUpdated_Date(userDto.getUpdatedDate());
			user.setActive(userDto.isStatus());
			user.setPassword(bcryptEncoder.encode(userDto.getPassword()));
			user.setDepartment(departmentRepository.findById((long) userDto.getDepartmentId()).orElse(null));
			user.setRole(role);

			userDao.save(user);

			// Success response
			ErrorResponse response = new ErrorResponse(HttpStatus.OK.value(), "First admin registered successfully",
					"Success");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		// For subsequent users including additional admins, require createdBy field
		if (!isFirstUser) {
			// Assuming `this.user` holds the details of the currently logged-in user
			UserDetails details = this.user;
			UserDao dao = userDao.findByEmail(details.getUsername());
			String createdBy = dao.getFirstName();

			// Proceed with user registration
			UserDao user = new UserDao();
			user.setUserId(userDto.getUserId());
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setUsername(userDto.getUsername());
			user.setEmail(userDto.getEmail());
			user.setCreated_By(createdBy); // Set createdBy from the currently logged-in admin
			user.setCreatedDate(userDto.getCreateDate());
			user.setUpdated_By(userDto.getUpdatedBy());
			user.setUpdated_Date(userDto.getUpdatedDate());
			user.setActive(userDto.isStatus());
			user.setPassword(bcryptEncoder.encode(userDto.getPassword()));
			user.setDepartment(departmentRepository.findById((long) userDto.getDepartmentId()).orElse(null));
			user.setRole(role);

			userDao.save(user);

			// Success response
			ErrorResponse response = new ErrorResponse(HttpStatus.OK.value(), "Data inserted successfully", "Success");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		// If createdBy is not set and it's not the first user, return error
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "createdBy is a required field.",
				"Failed");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

//	public ResponseEntity<?> save(UserDto userDto) {
//		
//		
//        
//		Role role = null;
//		// Check if the provided role is "User" or "Admin"
//		if ("USER".equalsIgnoreCase(userDto.getRoleName())) {
//			role = roleRepository.findByRoleName("USER");
//		} else if ("ADMIN".equalsIgnoreCase(userDto.getRoleName())) {
//			role = roleRepository.findByRoleName("ADMIN");
//		} else {
//			// Invalid role provided
//			ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
//					"Invalid role or role not found.", "Failed");
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//		}
//
//		// Check if the role is null (i.e., not found)
//		if (role == null) {
//			ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
//					"Invalid role or role not found.", "Failed");
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//		}
//		UserDetails details = this.user;
//		System.out.println(details.getUsername());
//		UserDao dao = userDao.findByEmail(details.getUsername());
//		 String createdBy = dao.getFirstName();
//		System.out.println("dao.getFirstName() " + dao.getFirstName());
//		
//		// Proceed with user registration
//		UserDao user = new UserDao();
//		user.setUserId(userDto.getUserId());
//		user.setFirstName(userDto.getFirstName());
//		user.setLastName(userDto.getLastName());
//		user.setUsername(userDto.getUsername());
//		user.setEmail(userDto.getEmail());
//		user.setCreated_By(createdBy);
//		user.setCreatedDate(userDto.getCreateDate());
//		user.setUpdated_By(userDto.getUpdatedBy());
//		user.setUpdated_Date(userDto.getUpdatedDate());
//		user.setActive(userDto.isStatus());
//		user.setPassword(bcryptEncoder.encode(userDto.getPassword()));
////	    subCategory.setCategory(categoryRepository.findById((long) subCategoryDto.getCategoryId()).orElse(null));
//		user.setDepartment(departmentRepository.findById((long) userDto.getDepartmentId()).orElse(null));
//		user.setRole(role);
//
//		userDao.save(user);
//
//		// Success response
//		ErrorResponse response = new ErrorResponse(HttpStatus.OK.value(), "Data inserted successfully", "Success");
//		return ResponseEntity.status(HttpStatus.OK).body(response);
//	}

	public ResponseEntity<?> getAllUsers() {
		List<UserDao> user = userDao.findAll();
		ApiResponseFormat response = new ApiResponseFormat();
		response.setStatus("success");
		response.setCode(HttpStatus.OK.value());
		response.setMessage("Request successful");
		response.setData(user);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
//	public List<UserDao> getAllUser() {
//		return userDao.findAll();
//	}

	public Optional<UserDao> getById(long userId) {
		return userDao.findById(userId);
	}

	public ResponseEntity<?> getUserById(long userId) {

		UserDao user = userDao.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Id : " + userId + " Unavailable"));
		ApiResponseFormat response = new ApiResponseFormat();
		response.setStatus("success");
		response.setCode(HttpStatus.OK.value());
		response.setMessage("Request successful");
		response.setData(user);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
// not work
	public String updateUser(long userId, UserDao users) {
		if (userDao.existsById(userId)) {
			users.setUserId(userId);
			userDao.save(users);
			return "user updated";
		}
		return "User not found";
	}

	public ResponseEntity<?> resetPassword(String token, String password) {
		UserDao userOptional = userDao.findByToken(token);

		if (userOptional == null) {
			throw new UsernameNotFoundException("Invalid Token!!!");
		}

		userOptional.setPassword(passwordEncoder.encode(password));
		userDao.save(userOptional);

		return ResponseEntity.status(HttpStatus.OK).body("Password reset successfully");
	}

	public String forgotPassword(String email) {
		UserDao userOptional = userDao.findByEmail(email);
		if (userOptional == null) {
			return "Invalid email id.";
		}

		UserDao user = userDao.findByEmail(userOptional.getEmail());
		user.setToken(generateToken());
		user = userDao.save(userOptional);

		// Generate the full reset link
		String resetLink = "http://localhost:3000/reset-password?token=" + user.getToken() + "&email="
				+ user.getEmail();
		sendForgotPasswordEmail1(user, resetLink);

		return "Forgot password email sent.";
	}

	public void sendForgotPasswordEmail1(UserDao user, String resetLink) {
		String recipientEmail = user.getEmail();
		String subject = "Forgot Password";
		String content = "Dear Sir/Madam,\n\n" + "Please click on the following link to reset your password:\n"
				+ resetLink + "\n\n" + "If you didn't request a password reset, please ignore this email.\n\n"
				+ "Regards,\n" + "Your Application ";

		MimeMessage message = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(recipientEmail);
			helper.setSubject(subject);
			helper.setText(content, false);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			// Handle the exception (e.g., log it or throw a custom exception)
		}
	}

	private String generateToken() {

		StringBuilder token = new StringBuilder();

		return token.append(UUID.randomUUID().toString()).append(UUID.randomUUID().toString()).toString();

	}

	public void sendPasswordChangedConfirmationEmail(String email) {
		String recipientEmail = email;
		String subject = "Password Changed Confirmation";
		String content = "Dear Sir/Madam,\n\n" + "Your password has been changed successfully.\n\n"
				+ "If you didn't perform this action, please contact us.\n\n" + "Regards,\n" + "Your Application";

		sendEmail(recipientEmail, subject, content);
	}

	private void sendEmail(String recipientEmail, String subject, String content) {
		MimeMessage message = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(recipientEmail);
			helper.setSubject(subject);
			helper.setText(content, false);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			// Handle the exception (e.g., log it or throw a custom exception)
		}
	}

	public boolean checkOldPassword(UserDao user, String oldPassword) {
		return passwordEncoder.matches(oldPassword, user.getPassword());
	}

	public void changePassword(UserDao user, String newPassword) {
		user.setPassword(newPassword);
		userDao.save(user);
	}

	public UserDao findByEmail(String email) {
		// Assuming UserDao has a constructor that takes relevant parameters.
		UserDao user = userDao.findByEmail(email);/* provide necessary parameters */
		// Set any necessary fields in the UserDao object.
		user.setEmail(email);

		return user;
	}

	public UserDao getUserByEmail(String email) {
		return userDao.findByEmail(email);
	}

	public UserDao getByUsername(String username) {
		return userDao.findByUsername(username);
	}

	public UserDao getByToken(String userToken) {
		// TODO Auto-generated method stub
		return null;
	}
//its working
	public ResponseEntity<?> updateUser(UserDto userDto, String adminEmail) {
		UserDao admin = userDao.findByEmail(adminEmail);
		if (admin == null || !admin.getRole().getRoleName().equalsIgnoreCase("ADMIN")) {
			return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),
					"Access denied. Only ADMIN can update user.", "Failed"), HttpStatus.FORBIDDEN);
		}

		Optional<UserDao> userOptional = userDao.findById(userDto.getUserId());
		if (!userOptional.isPresent()) {
			throw new ResourceNotFoundException("User id does not exist");
		}

		UserDao user = userOptional.get();
		user.setUsername(userDto.getUsername());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setActive(userDto.isStatus());
		user.setUpdated_By(userDto.getUpdatedBy());
		user.setUpdated_Date(userDto.getUpdatedDate());
		user.setPassword(bcryptEncoder.encode(userDto.getPassword()));
		user.setDepartment(departmentRepository.findById((long) userDto.getDepartmentId()).orElse(null));
		Role role = roleRepository.findByRoleName(userDto.getRoleName().toUpperCase());
		if (role == null) {
			return new ResponseEntity<>(
					new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid role or role not found.", "Failed"),
					HttpStatus.BAD_REQUEST);
		}
		user.setRole(role);

		userDao.save(user);

		return ResponseEntity.ok(new ErrorResponse(HttpStatus.OK.value(), "User successfully updated", "Success"));
	}

	public ResponseEntity<?> deleteUser(long userId, String adminEmail) {
	    UserDao admin = userDao.findByEmail(adminEmail);
	    if (admin == null || !admin.getRole().getRoleName().equalsIgnoreCase("ADMIN")) {
	        return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),
	                "Access denied. Only ADMIN can delete user.", "Failed"), HttpStatus.FORBIDDEN);
	    }

	    Optional<UserDao> userOptional = userDao.findById(userId);
	    if (!userOptional.isPresent()) {
	        throw new ResourceNotFoundException("User id does not exist");
	    }

	    // Change the status of the user to inactive instead of deleting
	    UserDao user = userOptional.get();
	    user.setActive(false);
	    userDao.save(user);

	    return ResponseEntity.ok(new ErrorResponse(HttpStatus.OK.value(), "User successfully deactivated", "Success"));
	}

//  public UserDao save(UserDto user) {
//  UserDao newUser = new UserDao();
//  newUser.setUsername(user.getUsername());
//  newUser.setEmail(user.getEmail());
// // newUser.setToken(user.get);
//  newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
//  return userDao.save(newUser);
//}

//	public void changePassword(UserDao user, String newPassword) {
////		String encodedPassword = passwordEncoder.encode(newPassword);
//		user.setPassword(newPassword);
//		userDao.save(user);
//	}
	// JwtUserDetailsService

//	public UserDao updateUser(UserDto user) throws ResourceNotFoundException {
//		UserDao newUser = userDao.findById(user.getUserId())
//				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
//		
//		newUser.setUsername(user.getUsername());
//		newUser.setEmail(user.getEmail());
//		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));	
//		newUser.setRole(roleRepository.findById((int) user.getRoleId()).orElse(null));
//
//		return userDao.save(newUser);
//	}
//	public ResponseEntity<ErrorResponse> updateUser(UserDto userDto) {
//		
//				Optional<UserDao> user1 = userDao.findById(userDto.getUserId());
//				if (!user1.isPresent()) {
//					throw new ResourceNotFoundException(" User id does not exist");
//				}
//		
//				UserDao user = user1.get();
//				user.setFirstName(userDto.getFirstName());
//				user.setLastName(userDto.getLastName());
//				user.setEmail(userDto.getEmail());
//				//user.setUsername(userDto.getUsername());
//				user.setPassword(userDto.getPassword());
////				user.setCreated_By(userDto.getCreated_By());
////				user.setCreatedDate(userDto.getCreatedDate());
//				//user.setUpdated_By(userDto.getUpdatedBy());
//				//user.setUpdated_Date(userDto.getUpdatedDate());
//				//user.setActive(userDto.isStatus());
//				//user.setDepartment(departmentRepository.findById(userDto.getDepartmentId()).orElse(null));		
//		
//				userDao.save(user);
//		
//				ErrorResponse response = new ErrorResponse(Integer.parseInt(ErrorCode.Success.getCode()),
//						" User successfully updated", ErrorCode.Success.getMessage());
//				return ResponseEntity.status(HttpStatus.OK).body(response);
//			}

}