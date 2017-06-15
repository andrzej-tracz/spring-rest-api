package agency.http.controllers;

import agency.exceptions.AccessDeniedException;
import agency.exceptions.BadCredentialsException;
import agency.security.AuthDTO;
import agency.security.model.JwtUser;
import agency.services.security.JWTService;
import agency.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static agency.services.AuthService.USER_REQUEST_KEY;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService userService;

    @Autowired
    private JWTService jwtService;

    @Value("${auth.jwt.header}")
    private String authHeader;

    @GetMapping(value = "/api/secure/hello/{name}")
    public Map helloPublic(@PathVariable String name, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        response.put("Hello", name);
        response.put("user", request.getAttribute(USER_REQUEST_KEY));

        return response;
    }

    @GetMapping(value = "/api/public/hello/{name}")
    public Map helloSecure(@PathVariable String name) {
        Map<String, String> response = new HashMap<>();

        response.put("Hello", name);

        return response;
    }

    @PostMapping(value = "/api/login")
    public Map auth(@RequestBody AuthDTO auth) throws BadCredentialsException {

        String userName = auth.getUsername();
        String passWord = auth.getPassword();

        Boolean hasValidCredentials = userService.authenticate(userName, passWord);

        if (hasValidCredentials) {
            Map<String, String> response = new HashMap<>();
            JwtUser user = new JwtUser(userName, passWord);
            response.put("token", jwtService.getToken(user));

            return response;
        }

        throw new BadCredentialsException();
    }

    @GetMapping(value = "/api/me")
    public Map me(HttpServletRequest request) throws AccessDeniedException {

        if (null == this.userService.user(request)) {
           throw new AccessDeniedException();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("user", this.userService.user(request));

        return response;
    }
}
