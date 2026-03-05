package E_commers.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import E_commers.model.Product;
import E_commers.model.ProductRequest;
import E_commers.repo.ProductRepository;
import E_commers.repo.ProductRequestRepository;
import E_commers.repo.UserRepository;
import E_commers.service.OrderService;
import E_commers.service.ProductService;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {


    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userrepository;
    @Autowired
    private ProductService productserivce;
    @Autowired
    private ProductRequestRepository productRequestRepository;
    
    @GetMapping("/product/all")
    public String prodcutpage(Model model, HttpSession session) {
        String sellerName = (String) session.getAttribute("username");
        if (sellerName != null) {
            model.addAttribute("products", productRequestRepository.findBySellername(sellerName));
        } else {
            model.addAttribute("products", productRequestRepository.findAll());
        }
        return "sellerdashboard";
    }
        
    @GetMapping("/sellerdashboard")
    public String viewAllProducts(Model model, HttpSession session) {
        String sellerName = (String) session.getAttribute("username");
        if (sellerName != null) {
            model.addAttribute("products", productRequestRepository.findBySellername(sellerName));
        } else {
            model.addAttribute("products", productRequestRepository.findAll());
        }
        return "sellerdashboard";
    }

    @GetMapping("/product/add")
    public String addProductPage(Model model) {
        model.addAttribute("p", new Product());
        return "add_product";
    }
  
    @PostMapping("/request")
    public String productRequest(
            @ModelAttribute ProductRequest request,
            HttpSession session) throws IOException {

        // Save the image bytes from the uploaded file
        if (request.getFile() != null && !request.getFile().isEmpty()) {
            request.setProductimage(request.getFile().getBytes());
        }

        // Optionally capture seller name from session
        String sellerName = (String) session.getAttribute("username");
        if (sellerName != null) {
            request.setSellername(sellerName);
        }

        request.setStatus("PENDING");
        productRequestRepository.save(request);

        return "redirect:/sellerdashboard";
    }

    @GetMapping("/product-approval")
    public String viewProductApproval(Model model) {
        model.addAttribute("requests", productRepository.findAll());
        return "product-approval";
    }

    @GetMapping("/request/image/{id}")
    public ResponseEntity<byte[]> getRequestImage(@PathVariable Long id) {
        ProductRequest req = productRequestRepository.findById(id).orElse(null);
        if (req == null || req.getProductimage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(req.getProductimage());
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/product/all";
    }
    @GetMapping("/product/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {

        Product product = productserivce.getProductById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(product.getProductimage());
    }
    @GetMapping("/product/view/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {

        Product product = productRepository.findById(id).orElse(null);

        model.addAttribute("p", product);

        return "see-product";
    }



    @PostMapping("/product/update")
    public String updateProduct(
            @RequestParam long id,
            @RequestParam String productName,
            @RequestParam String productdetails,
            @RequestParam double productprice,
            @RequestParam(required = false) MultipartFile productimage,
            HttpSession session
    ) throws IOException {

        Product p = productRepository.findById(id).orElse(null);

        if(p != null) {
            p.setProductName(productName);
            p.setProductdetails(productdetails);
            p.setProductprice(productprice);

            if(productimage != null && !productimage.isEmpty()) {
                p.setProductimage(productimage.getBytes());
            }

            productRepository.save(p);
        }

        return "redirect:/product/all";
    }
    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute Product product) throws IOException {

        if(product.getFile() != null && !product.getFile().isEmpty()) {
            product.setProductimage(product.getFile().getBytes());
        }

        product.setAddproductdate(LocalDate.now());
        product.setStatus("PENDING");

        productRepository.save(product);

        return "redirect:/product/all";
    }
    @PostMapping("/orders/assign/{id}")
    public String assignDelivery(@PathVariable Long id,
                                 @RequestParam Long deliveryId) {

        OrderService orderService = new OrderService();
		orderService.assignDelivery(id, deliveryId);

        return "redirect:/orders/seller";
    }
    

}
