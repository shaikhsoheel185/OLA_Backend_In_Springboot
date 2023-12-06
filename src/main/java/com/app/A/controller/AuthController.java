package com.app.A.controller;

import com.app.config.JwtUtil;
import com.app.domain.UserRole;
import com.app.exception.UserException;
import com.app.modal.Driver;
import com.app.modal.User;
import com.app.repository.DriverRepository;
import com.app.repository.UserRepository;
import com.app.request.DriverSignupRequest;
import com.app.request.LogInRequest;
import com.app.request.SignupRequest;
import com.app.response.JwtResponse;
import com.app.service.CustomUserDetailsService;
import com.app.service.DriverService;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriverRepository driverRepository;


    @Autowired
    private DriverService driverService;



    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserService userService;


    @Autowired
    public AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user/signup")
    public ResponseEntity<JwtResponse> signupHandler(@RequestBody SignupRequest signupRequest ) throws UserException {

        String email = signupRequest.getEmail();
        String fullName = signupRequest.getFullName();
        String mobile = signupRequest.getMobile();
        String password = signupRequest.getPassword();
        userRepository.findByEmail(email);

        User user = userRepository.findByEmail(email);


        if (user != null) {
            throw new UserException(" User Already Exist with email " +  email );
        }

        String encodedPassword =passwordEncoder.encode(password);
        User createdUser = new User();
        createdUser.setEmail(signupRequest.getEmail());
        createdUser.setPassword(encodedPassword);
        createdUser.setFullName(signupRequest.getFullName());
        createdUser.setMobile(signupRequest.getMobile());
        createdUser.setRole(UserRole.USER);

        User savedUser=userRepository.save(createdUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken
                (savedUser.getEmail()
                ,savedUser.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.USER);
        jwtResponse.setMessage("Account Created Successfully: "+savedUser.getFullName());

        return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.OK);

    }


    @PostMapping("/driver/signup")
    public ResponseEntity<JwtResponse> driverSignupHandler(@RequestBody DriverSignupRequest driverSignupRequest){

        Driver driver = driverRepository.findByEmail(driverSignupRequest.getEmail());

        JwtResponse jwtResponse=new JwtResponse();

        if(driver!=null) {

            jwtResponse.setAuthenticated(false);
            jwtResponse.setErrorDetails("email already used with another account");
            jwtResponse.setError(true);

            return new ResponseEntity<JwtResponse>(jwtResponse,HttpStatus.BAD_REQUEST);
        }




        Driver createdDriver=driverService.registerDriver(driverSignupRequest);

        Authentication authentication = new UsernamePasswordAuthenticationToken(createdDriver.getEmail(), createdDriver.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateJwtToken(authentication);

        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.DRIVER);
        jwtResponse.setMessage("Account Created Successfully: "+createdDriver.getName());


        return new ResponseEntity<JwtResponse>(jwtResponse,HttpStatus.ACCEPTED);
    }
//
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signin(@RequestBody LogInRequest req){

        String username = req.getEmail();
        String password = req.getPassword();

        Authentication authentication = authenticate(password , username);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateJwtToken(authentication);

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.USER);
        jwtResponse.setMessage("Account Login Successfully: ");

        return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.ACCEPTED);
    }
//
//
//
    private Authentication authenticate(String password, String username) {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("invalid username ord password from authenticate method");
        }

        if ( !passwordEncoder.matches( password, userDetails.getPassword())) {
            throw new BadCredentialsException(" invalid username or password");
        }

//            return  new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }


}
