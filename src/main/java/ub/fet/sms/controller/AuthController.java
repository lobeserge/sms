package ub.fet.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ub.fet.sms.model.ERole;
import ub.fet.sms.model.Role;
import ub.fet.sms.model.Student;
import ub.fet.sms.payload.request.LoginRequest;
import ub.fet.sms.payload.request.SignupRequest;
import ub.fet.sms.payload.response.JwtResponse;
import ub.fet.sms.payload.response.MessageResponse;
import ub.fet.sms.repository.RoleRepository;
import ub.fet.sms.repository.StudentRepository;
import ub.fet.sms.security.jwt.JwtUtils;
import ub.fet.sms.security.services.UserDetailsImpl;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	StudentRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPin()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getPhone(),
												 roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByName(signUpRequest.getName())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}


		Student student = new Student();
		student.setRollno(signUpRequest.getRollno());
		student.setRedno(signUpRequest.getRedno());
		student.setSclass(signUpRequest.getSclass());
		student.setName(signUpRequest.getName());
		student.getFname(signUpRequest.getFname());
		student.setMname(signUpRequest.getMname());
		student.setDob(signUpRequest.getDob());
		student.setDor(signUpRequest.getDor());
		student.setAddress(signUpRequest.getAddress());
		student.setCity(signUpRequest.getCity());
		student.setState(signUpRequest.getState());
		student.setPin(encoder.encode(signUpRequest.getPin()));



		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);

						break;
					case "staff":
						Role modRole = roleRepository.findByName(ERole.ROLE_STAFF)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(modRole);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
				}
			});
		}

		student.setRoles(roles);
		userRepository.save(student);

		return ResponseEntity.ok(student);
	}
}
