package com.spring.boot.smartcontact.controller.user;

import com.spring.boot.smartcontact.model.AdminProduct;
import com.spring.boot.smartcontact.model.Product;
import com.spring.boot.smartcontact.model.User;
import com.spring.boot.smartcontact.service.AdminProductService;
import com.spring.boot.smartcontact.service.ProductService;
import com.spring.boot.smartcontact.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class ProductAddController {
    private final UserService userService;
    private final AdminProductService adminProductService;
    private final ProductService productService;

    public ProductAddController(UserService userService, AdminProductService adminProductService,
                                ProductService productService) {
        this.userService = userService;
        this.adminProductService = adminProductService;
        this.productService = productService;
    }

    @ModelAttribute
    public User addCommonAttribute(Principal principal) {
        return userService.getUserByUserName(principal.getName());
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestParam("dropDownList") int productId, Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("showSidebar", true);

        Product product = new Product();
        AdminProduct adminProduct = adminProductService.findById(productId);

        product.setProductName(adminProduct.getAdminProductName());
        product.setProductId(productId);
        product.setUser(user);

        productService.save(product);

        adminProduct.setAdminProductStatus(1);
        adminProductService.save(adminProduct);

        return "redirect:/user/index";
    }

    @GetMapping("/select-products")
    public String selectProducts(Model model) {
        List<AdminProduct> adminProductList = adminProductService.findAllByAdminStatusProductSetToZero();
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("adminProductList", adminProductList);
        model.addAttribute("showSidebar", true);
        return "user/select_products";
    }
    @PostMapping("/add-multiple-products")
    public String addMultipleProduct(@RequestParam("dropDownList") List<Integer> itemsIds, Model model) {
        User user = (User) model.getAttribute("user");
        if(!user.isEnabled())
            return "redirect:/logout";

        model.addAttribute("showSidebar", true);
        userService.saveUserProduct(user, itemsIds);
        return "redirect:/user/index";
    }
}
