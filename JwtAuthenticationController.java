package com.mom_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mom_management.config.JwtTokenUtil;
import com.mom_management.dto.ChangePasswordDto;
import com.mom_management.model.JwtRequest;
import com.mom_management.model.JwtResponse;
import com.mom_management.model.NewPasswordDetails;
import com.mom_management.model.UserDao;
import com.mom_management.model.UserDto;
import com.mom_management.repository.UserRepository;
import com.mom_management.service.JwtUserDetailsService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		try {
			// Your authentication logic here
			authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
			System.out.println(userDetails);
			final String token = jwtTokenUtil.generateToken(userDetails);

			return ResponseEntity.ok(new JwtResponse(token));

		} catch (DisabledException e) {
			// Catch and rethrow to avoid logging as an error
			throw new Exception(e.getMessage());
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("INVALID_CREDENTIALS");
		}

	}

	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		return userDetailsService.getAllUsers();
	}

	@GetMapping("/users/email")
	public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
		try {
			UserDao user = userRepository.findByEmail(email);
			if (user != null) {
				return ResponseEntity.ok(user);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable long userId) {
		return userDetailsService.getUserById(userId);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestParam(required = false) String email) {
		if (email == null || email.trim().isEmpty()) {
//	            ErrorResponse response = new ErrorResponse(Integer.parseInt(ErrorCode.Bad_Request.getCode()),
//	                    "Email address is required", ErrorCode.Bad_Request.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong");
		}

		String response = userDetailsService.forgotPassword(email);
		if (!response.startsWith("Invalid")) {
			response = response;
//	            response = "http://localhost:8080/reset-password?token=" + response;
//	            ErrorResponse response1 = new ErrorResponse(Integer.parseInt(ErrorCode.Success.getCode()),
//	                    "A Link has been sent to your email ID to Reset the Password.", ErrorCode.Success.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body("ok");
		} else {
//	            ErrorResponse response1 = new ErrorResponse(Integer.parseInt(ErrorCode.NOT_FOUND.getCode()),
//	                    "Invalid email address", ErrorCode.NOT_FOUND.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
		}
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
		String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		userDto.setUserId(id);
		return userDetailsService.updateUser(userDto, adminEmail);
	}

	@DeleteMapping("/users/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable long userId) {
	    String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	    return userDetailsService.deleteUser(userId, adminEmail);
	}

	@PutMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam String token,
			@RequestBody NewPasswordDetails newPasswordDetails) {
		String password = newPasswordDetails.getNewPassword();
		String confirmPassword = newPasswordDetails.getConfirmNewPassword();

		if (password == null || password.isBlank() || !password.equals(confirmPassword)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password or passwords do not match");
		}

		return userDetailsService.resetPassword(token, password);
	}

	@PatchMapping("/change-password")
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
		UserDao user = userDetailsService.findByEmail(changePasswordDto.getEmail());

		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		// String password = bcryptEncoder.encode(changePasswordDto.getOldPassword());
		// Check if the old password matches the stored password

		if (!bcryptEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
			return ResponseEntity.badRequest().body("Old password is incorrect");
		}

		// Check if the new password and confirm password match
		if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
			return ResponseEntity.badRequest().body("New password and confirm password do not match");
		}

		// Encode the new password
		String encodedPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());

		userDetailsService.changePassword(user, encodedPassword);
		userDetailsService.sendPasswordChangedConfirmationEmail(user.getEmail());

		return ResponseEntity.ok("Password changed successfully");
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(user));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	@GetMapping("/api/currentUser")
    public ResponseEntity<UserDao> getCurrentUser(Authentication authentication) {
        UserDao currentUser = userRepository.findByUsername(authentication.getName());
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
	
//	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
//	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
//
//		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//
//		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//
//		final String token = jwtTokenUtil.generateToken(userDetails);
//
//		return ResponseEntity.ok(new JwtResponse(token));
//	}

//		@PutMapping("/reset-password")
//		public ResponseEntity<?> resetPassword(@RequestParam String token,
//				@RequestBody NewPasswordDetails newPasswordDetails) {
//
//			// String password=passwordMap.get("password");
//			String password = newPasswordDetails.getNewPassword();
//			if (password == null || password.isBlank()) {
////	            ErrorResponse response = new ErrorResponse(
////	                Integer.parseInt(ErrorCode.Bad_Request.getCode()),
////	                "Invalid password",
////	                "Password cannot be null or blank"
////	            );
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request");
//			}
//			UserDao userdetails = userRepository.findByUsername(newPasswordDetails.getConfirmNewPassword());
//			if (userdetails != null) {
//				if (userdetails.getToken().equals(token)) {
//					userdetails.setPassword(passwordEncoder.encode(password));
//					userRepository.save(userdetails);
//
//					userDetailsService.sendPasswordChangedConfirmationEmail(userdetails.getEmail());
//				} else {
//					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is not valid");
//				}
//			}
//
////	      sendPasswordChangedConfirmationEmail(user);
//			return userDetailsService.resetPassword(token, password);
////	         ErrorResponse response1 = new ErrorResponse(Integer.parseInt(ErrorCode.Success.getCode()),
////	                    "Password successfully changed", ErrorCode.Success.getMessage());
////	         return ResponseEntity.status(HttpStatus.OK).body(response1);
//		}

}
