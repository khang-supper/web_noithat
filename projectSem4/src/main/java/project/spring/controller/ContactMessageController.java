package project.spring.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import project.spring.model.ContactMessage;
import project.spring.repositories.ContactMessageRepository;

@Controller
//@RequestMapping("/admin/contact")
public class ContactMessageController {
	@GetMapping("/admin/contact")
    public String getAllContacts(Model model) {
	    List<ContactMessage> contacts = ContactMessageRepository.Instance().findAll();
	    model.addAttribute("contacts", contacts);
	    return "forderAdmin/contact-message";
    }
	@GetMapping("/lien-he")
    public String showAddForm(Model model) {
        model.addAttribute("contact", new ContactMessage());
        return "forderClient/contact";
    }

    @PostMapping("/lien-he")
    public String addCategory(@ModelAttribute("contact") ContactMessage contact) {
    	ContactMessageRepository.Instance().insert(contact);
        return "redirect:/lien-he";
    }
}
