package pxf.extsweb.admin.framework.web.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pxf.extsweb.admin.framework.web.entity.MenuInformation;
import pxf.extsweb.admin.framework.web.service.MenuInformationService;

/**
 * @author potatoxf
 * @date 2021/4/8
 */
@RequestMapping("/admin")
@Controller
public class AdminController {

  private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
  private final MenuInformationService menuInformationService;

  public AdminController(MenuInformationService menuInformationService) {
    this.menuInformationService = menuInformationService;
  }

  @ResponseBody
  @RequestMapping("/login")
  public String login(Model model) {
    return "login";
  }

  @ResponseBody
  @RequestMapping("/index")
  public String index(Model model) {
    List<MenuInformation> menuInformations = menuInformationService.buildMenuTree();
    model.addAttribute("menus", menuInformations);
    return "index";
  }
}
