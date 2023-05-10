package io.playdata.login.controller;

import io.playdata.login.AccountService;
import io.playdata.login.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class AccountController {
    // 기본 -> 로그인 폼
    // 버튼 누르면 -> 회원가입 페이지

//    @GetMapping
//    public String home() {
//        return "index";
//    }

    @GetMapping
    public String home(
            HttpSession session
    ) {
        if (session != null && session.getAttribute("login") != null) {
            boolean login = (boolean) session.getAttribute("login");
            if (login) {
                return "redirect:/main";
            }
        }
        return "index"; // 로그인할 수 있는 페이지로 연결
    }

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("account", new Account());
        return "join";
    }

    @Autowired
    private AccountService accountService;

    @PostMapping("/join")
    public String join(Model model,
                       @ModelAttribute("account") Account account,
                       RedirectAttributes redirectAttributes) {
        accountService.join(account);
        redirectAttributes.addFlashAttribute("msg", "정상 가입 완료");
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(HttpSession session,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes,
                        @RequestParam("loginID") String loginID, // name="loginID"
                        @RequestParam("password") String password // name="password"
    ) {
        Account account = accountService.login(loginID, password);
        if (account == null) {
            redirectAttributes.addFlashAttribute("msg", "존재하지 않는 회원입니다.");
            return "redirect:/";
        }
        // 로그인 성공 시
        session.setAttribute("login", true);
        response.addCookie(new Cookie(
                "name",account.getName()
        ));
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String main(HttpSession session,
                       @CookieValue("name") String name,
                       Model model) {
        boolean login = (boolean) session.getAttribute("login");
        if(!login){
            return "redirect:/";
        }
        model.addAttribute("msg", name + "님이 로그인 하셨습니다.");
        return "main";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,
                         RedirectAttributes redirectAttributes) {
        if(session!=null){
            session.invalidate();
            redirectAttributes.addFlashAttribute(
                    "msg","정상적으로 로그아웃되었습니다.");
        }
        return "redirect:/";
    }
}
