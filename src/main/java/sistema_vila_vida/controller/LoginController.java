package sistema_vila_vida.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login.html")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/esqueci-senha")
    public String mostrarRecuperacaoSenha() {
        return "esqueci-senha";
    }
}