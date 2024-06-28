package com.example.sahtyapp1.Controller;

import com.example.sahtyapp1.Entity.ERole;
import com.example.sahtyapp1.Entity.Role;
import com.example.sahtyapp1.Entity.Utilisateur;
import com.example.sahtyapp1.Repository.RoleRepo;
import com.example.sahtyapp1.Repository.UtilisateurRepo;
import com.example.sahtyapp1.Request.LoginRequest;
import com.example.sahtyapp1.Request.SignUpRequest;
import com.example.sahtyapp1.Response.JwtResponse;
import com.example.sahtyapp1.Response.MessageResponse;
import com.example.sahtyapp1.Security.JwtUtils;
import com.example.sahtyapp1.ServiceImpl.UtilisateurDetailServiceImpl;
import com.example.sahtyapp1.ServiceImpl.UtilisateurDetailsImpl;
import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
@ResponseBody
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private final  UtilisateurRepo utilisateurRepo;

    @Autowired
    private final  RoleRepo roleRepo;

    @Autowired
    private final  PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private UtilisateurDetailServiceImpl utilisateurDetailService;



    public AuthController(UtilisateurRepo utilisateurRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.utilisateurRepo = utilisateurRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }
    private static final Logger logger = (Logger) LogManager.getLogger(AuthController.class);


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        utilisateurDetailService.generateResetToken(email);
        return ResponseEntity.ok("Reset token sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        if (utilisateurDetailService.resetPassword(token, newPassword)) {
            return ResponseEntity.ok("Password successfully reset.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
        }
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UtilisateurDetailsImpl userDetails = (UtilisateurDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jwtResponse);
    }



  /* @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UtilisateurDetailsImpl userDetails = (UtilisateurDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jwtResponse);
    }*/




   @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpRequest signUpRequest) {
        // Check if the role requested exists in the database
        Set<Role> roles = new HashSet<>();

        if (signUpRequest.getRole() != null && !signUpRequest.getRole().isEmpty()) {
            signUpRequest.getRole().forEach(role -> {
                switch (role) {
                    case "admin":
                        roleRepo.findByName(ERole.Admin).ifPresent(roles::add);
                        break;
                    case "patient":
                        roleRepo.findByName(ERole.Patient).ifPresent(roles::add);
                        break;
                    case "medecin":
                        roleRepo.findByName(ERole.Medecin).ifPresent(roles::add);
                        break;
                    case "pharmacien":
                        roleRepo.findByName(ERole.Pharmacien).ifPresent(roles::add);
                        break;
                    default:
                        // Handle unknown roles
                        break;
                }
            });
        } else {
            // If no role is specified, assign a default role (e.g., ROLE_USER)
            roleRepo.findByName(ERole.Role_User).ifPresent(roles::add);
        }

        // Check if username is already taken
        if (utilisateurRepo.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check if email is already in use
        if (utilisateurRepo.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Utilisateur utilisateur = new Utilisateur(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        utilisateur.setAdresse(signUpRequest.getAdresse());
        utilisateur.setNom(signUpRequest.getNom());
        utilisateur.setPrenom(signUpRequest.getPrenom());
        utilisateur.setDate_naissance(signUpRequest.getDate_naissance());
        utilisateur.setRolee(signUpRequest.getRolee());
        utilisateur.setRoles(roles);

        utilisateurRepo.save(utilisateur);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

/* @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        // Check if the role requested exists in the database
        Set<Role> roles = new HashSet<>();

        if (signUpRequest.getRole() != null && !signUpRequest.getRole().isEmpty()) {
            signUpRequest.getRole().forEach(role -> {
                switch (role) {
                    case "admin":
                        roleRepo.findByName(ERole.Role_Admin).ifPresent(roles::add);
                        break;
                    case "patient":
                        roleRepo.findByName(ERole.Role_Patient).ifPresent(roles::add);
                        break;
                    case "medecin":
                        roleRepo.findByName(ERole.Role_Medecin).ifPresent(roles::add);
                        break;
                    case "pharmacien":
                        roleRepo.findByName(ERole.Role_Pharmacien).ifPresent(roles::add);
                        break;
                    default:
                        // Handle unknown roles
                        break;
                }
            });
        } else {
            // If no role is specified, assign a default role (e.g., ROLE_USER)
            roleRepo.findByName(ERole.Role_User).ifPresent(roles::add);
        }

        // Check if username is already taken
        if (utilisateurRepo.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check if email is already in use
        if (utilisateurRepo.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Utilisateur utilisateur = new Utilisateur(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        utilisateur.setRoles(roles);
        utilisateurRepo.save(utilisateur);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


*/

}
