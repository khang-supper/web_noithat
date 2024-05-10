package project.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import project.spring.model.Account;
import project.spring.repositories.AccountRepository;

@Controller
@RequestMapping("/admin/account")
public class AccountController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AccountRepository accountRepository;
    
/////
	private String getRole() {
        return (String) request.getSession().getAttribute("role");
    }
    private boolean isAdmin() {
        String role = getRole();
        return role != null && role.equals("1");
    }
////////
@GetMapping("")
public String getAllAccounts(Model model) {
    List<Account> accounts = accountRepository.findAll();
    model.addAttribute("accounts", accounts);
return "forderAdmin/account/accounts";
}

@GetMapping("/admin")
public String getAllAccountsrole(Model model) {
    List<Account> accountss = accountRepository.findAllrole();
    model.addAttribute("accountss", accountss);
return "forderAdmin/account/accountAdmin";
}

@GetMapping("/add")
public String showAddForm(Model model) {
    model.addAttribute("account", new Account());
    return "forderAdmin/account/add-account";
}

@PostMapping("/add")
public String addAccount(@ModelAttribute("account") Account account) {
    accountRepository.insert(account);
    return "redirect:/admin/account";
}

@GetMapping("/edit")
public String showEditForm(@RequestParam("id") int id, Model model) {
    Account account = accountRepository.findById(id);
    model.addAttribute("account", account);
    return "forderAdmin/account/edit-account";
}

@PostMapping("/edit")
public String updateAccount(@RequestParam("id") int id,
        @ModelAttribute("account") Account account) {
    account.setId(id);
    accountRepository.update(account);
    return "redirect:/admin/account";
}

@GetMapping("/delete/{id}")
public String deleteAccount(@PathVariable("id") int id) {
    accountRepository.deleteById(id);
    return "redirect:/admin/account";
}
@GetMapping("/role/delete/{id}")
public String deleteAccounts(@PathVariable("id") int id) {
    accountRepository.deleteById(id);
    return "redirect:/admin/account/admin";
}
}
