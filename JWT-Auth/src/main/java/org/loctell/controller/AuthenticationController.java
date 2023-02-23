package org.loctell.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.loctell.RepositoryLayer.UserRepository;
import org.loctell.Security.AuthenticationResponse;
import org.loctell.Security.RegisterRequest;
import org.loctell.ServiceLayer.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @ResponseBody
    @RequestMapping(value = "/signup-page")
    public ModelAndView ShowSignUpPage(Model m) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("signup.html");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView RegisterAPI(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();

        RegisterRequest rr = new RegisterRequest();
        rr.setFirstname(request.getParameter("firstname"));
        rr.setLastname(request.getParameter("lastname"));
        rr.setEmail(request.getParameter("email"));
        rr.setPassword(passwordEncoder.encode(request.getParameter("password")));

        AuthenticationResponse uu = service.register(rr);

        if (uu != null) {
            mav.setViewName("SuccessInsertuser.html");
        } else {
            mav.setViewName("ErrorInsertingUser.html");
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/log-in", method = RequestMethod.POST)
    public ModelAndView LoginAPI(HttpServletRequest req, @RequestParam("email") String email, @RequestParam("password") String password) {
        ModelAndView mv = new ModelAndView();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception exception) {
            mv.setViewName("failed.html");
        }
        var userDetails = userRepository.findByEmail(email);
        if (userDetails != null) {
            mv.setViewName("userdetails.html");
        } else {
            mv.setViewName("failed.html");
        }
        return mv;
    }

    @ResponseBody
    @RequestMapping("/login-page")
    public ModelAndView ShowLoginPage(Model m) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login.html");
        return mv;
    }

    @GetMapping("/logout")
    public ModelAndView LogOutAPI(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        ModelAndView modelAndView = new ModelAndView("signup.html");
        modelAndView.addObject("message", "You have been logged out successfully.");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/home-page")
    public ModelAndView ShowHomePage(Model m) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("home.html");
        return mv;
    }
}