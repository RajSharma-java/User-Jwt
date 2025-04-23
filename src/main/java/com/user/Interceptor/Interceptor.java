package com.user.Interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.Dto.BaseDto;
import com.user.Dto.UserCredentialDto;
import com.user.Utils.JwtUtils;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@Component
public class Interceptor implements HandlerInterceptor{
    /*
use an Interceptor in Spring Boot :-
    1. Cross-Cutting Concerns:
                    An interceptor is like a gatekeeper — it lets you run some code before or after a controller method is executed.
                    Instead of repeating logic inside every controller, you use an interceptor for tasks that apply across many endpoints, like:

                    Concern	Example Use Case:
                            🔐 Authentication / Authorization ->	Check JWT token or user role before accessing protected routes
                            📜 Logging ->	Log requests/responses globally
                            ⏱️ Performance Timing ->	Measure execution time of endpoints
                            📦 Data Formatting	-> Modify request/response data uniformly
                            🔁 API Rate Limiting -> Limit how many times a user can call an API

    2. How Interceptors Work (Spring Boot Style)
        Client → Interceptor → Controller → Interceptor (post-process) → Response
            Spring Boot interceptors usually implement:  HandlerInterceptor


    And override:
        preHandle(...) – runs before controller method
        postHandle(...) – runs after controller method, before view rendering
        afterCompletion(...) – runs after request is fully complete

        Real Example – JWT Interceptor
      1. Reads JWT from the request header
      2. Validates it
      3. Blocks the request if the token is invalid

 */

    /*
    Below method ->>
    Runs before the controller method.
    Returns true → continue the request.
    Returns false → block the request and respond immediately
     */
//    @Override
//    public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        PublicEndpoint publicEndpoint;
//        try {
//            publicEndpoint=((HandlerMethod) handler).getMethod().getAnnotation((PublicEndpoint.class));
//        }
//        catch (Exception e)
//        {
//            return true;
//        }
//        /*
//        Converts the handler object to a HandlerMethod (Spring uses this for controllers).
//        Tries to read the @PublicEndpoint annotation from the controller method.
//        If the handler is not a controller (e.g., a static resource), the cast fails — so we just allow it by returning true.
//
//         */
//
//        String authHeader = request.getHeader("Authorization"); //Reads the Authorization header from the HTTP request.
//                                                                    // Example header: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
//
//
//        // when the request is for a public endpoint, we don't need to check for the Authorization header
//        if (publicEndpoint != null) {
//            return true;
//        }
//        /*
//        If the annotation was found, it's a public API.
//        We don’t validate JWT, just allow the request.
//        */
//
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) { //Checks that the Authorization header is not null and starts with "Bearer ".
//            // validate the JWT token
//            String token = authHeader.substring(7); //Extracts the actual JWT token by removing the "Bearer " prefix (first 7 characters).
//
//            if (JwtUtils.validateToken(token))//Validates the JWT using a utility method.
//            {
//                UserCredentialDto userCredential = JwtUtils.parseToken(token); //If valid, parses the token to get user info.
//                if(userCredential != null){
//                    request.setAttribute("credential", userCredential);
//                    return true;
//                }
//                /*
//                If a valid user object is found → set it in the request (can be accessed in controllers).
//                If parsing fails, continue to block.
//                 */
//
//                System.out.println("Failed to parse ");
//            }
//
//        }
//
//
//        BaseDto responseDto = new BaseDto();
//        //Creates a basic DTO to send error details to the client.
//        responseDto.setSuccess(false);
//        responseDto.setErrorCode("401");
//        responseDto.setMessage("Unauthorized access, please send a valid Authorization header.");
//
//        // Initialize ObjectMapper
//        ObjectMapper mapper = new ObjectMapper(); //Converts the BaseDto to a JSON string using Jackson.
//        String jsonString = mapper.writeValueAsString(responseDto);
//
//        response.setStatus(401);
//        response.setContentType("application/json");
//        response.getWriter().write(jsonString);
//        return false;
//        /*
//          Sends HTTP 401 Unauthorized response with a JSON error body.
//          return false means: don't call the controller method, stop here.
//         */
//    }
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        PublicEndpoint publicEndpoint;

        try{
            publicEndpoint = ((HandlerMethod) handler).getMethod().getAnnotation((PublicEndpoint.class));
        }catch (Exception e){

            // only if no route exist the codebase
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        // when the request is for a public endpoint, we don't need to check for the Authorization header
        if (publicEndpoint != null) {
            return true;
        }


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // validate the JWT token
            String token = authHeader.substring(7);

            if (JwtUtils.validateToken(token)){

                UserCredentialDto userCredential = JwtUtils.parseToken(token);
                if(userCredential != null){
                    request.setAttribute("credential", userCredential);
                    return true;
                }

                System.out.println("Failed to parse ");
            }

        }


        BaseDto responseDto = new BaseDto();
        responseDto.setSuccess(false);
        responseDto.setErrorCode("401");
        responseDto.setMessage("Unauthorized access, please send a valid Authorization header.");

        // Initialize ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(responseDto);

        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write(jsonString);
        return false;
    }



}


