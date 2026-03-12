package com.example.nutra.config;

import com.example.nutra.model.*;
import com.example.nutra.model.type.RoleType;
import com.example.nutra.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BlogRepository blogRepository;
    private final InformationRepository informationRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private static final String MOCK_IMAGE_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==";
    private static final String PLACEHOLDER_URL = "/uploads/placeholder.png";

    @Override
    public void run(String... args) throws Exception {
        createPlaceholderFile();
        if (categoryRepository.count() > 0) {
            System.out.println("Database already seeded. Skipping...");
            return;
        }

        System.out.println("Seeding initial data...");

        // 1. Seed Admin
        String adminEmail = "admin@admin.com";
        if (userRepository.findByUsername(adminEmail) == null) {
            Admin admin = new Admin();
            admin.setUsername(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(RoleType.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
        }

        // 2. Seed 10 Customers
        for (int i = 1; i <= 10; i++) {
            String customerUser = "user" + i + "@example.com";
            if (userRepository.findByUsername(customerUser) == null) {
                Customer customer = new Customer();
                customer.setUsername(customerUser);
                customer.setPassword(passwordEncoder.encode("password123"));
                customer.setRole(RoleType.CUSTOMER);
                customer.setActive(true);
                userRepository.save(customer);
            }
        }

        // 3. Seed 5 Categories
        List<Category> categories = new ArrayList<>();
        String[] catNames = {"Supplements", "Vitamins", "Lifestyle", "Equipment", "Bundles"};
        for (String name : catNames) {
            Category cat = Category.builder()
                    .name(name)
                    .svg("<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'><path d='M12 2L2 7l10 5 10-5-10-5z'/></svg>")
                    .badge("NEW")
                    .shortDescription("High-quality " + name.toLowerCase() + " for your health.")
                    .image(PLACEHOLDER_URL)
                    .build();
            categories.add(categoryRepository.save(cat));
        }

        // 4. Seed 10 Products
        for (int i = 1; i <= 10; i++) {
            Category productCat = categories.get(i % categories.size());
            Product product = Product.builder()
                    .name("Premium Product " + i)
                    .link("https://nutra.com/product-" + i)
                    .category(productCat)
                    .description("This is a premium description for Product " + i + ". It contains all the necessary nutrients.")
                    .singleProductMp(50.0 + i)
                    .singleProductSp(40.0 + i)
                    .twoProductMp(100.0 + i)
                    .twoProductSp(80.0 + i)
                    .threeProductMp(150.0 + i)
                    .threeProductSp(110.0 + i)
                    .discount(10.0)
                    .badge(i % 2 == 0 ? "NEW" : "HOT")
                    .categoryBadge((i % 5 + 1) + " BEST SELLER")
                    .featuredImages(Arrays.asList(PLACEHOLDER_URL, PLACEHOLDER_URL))
                    .singleProductImage(PLACEHOLDER_URL)
                    .twoProductImage(PLACEHOLDER_URL)
                    .threeProductImage(PLACEHOLDER_URL)
                    .benefitsParagraph("These are the benefits of using Product " + i + ". It helps you stay healthy.")
                    .benefits(Arrays.asList(
                            new ProductBenefit("<svg>...</svg>", "Benefit 1 for Product " + i),
                            new ProductBenefit("<svg>...</svg>", "Benefit 2 for Product " + i)
                    ))
                    .servingSize("1 Scoop")
                    .capsulesPerContainer("30 Servings")
                    .supplementFacts(Arrays.asList(
                            new NutrientInfo("Protein", "25g", "50%"),
                            new NutrientInfo("Vitamin C", "500mg", "100%")
                    ))
                    .freebies(Arrays.asList("Free Shaker", "Free Shipping"))
                    .howToUse(Arrays.asList("Mix with water", "Shake well", "Drink immediately"))
                    .faqs(Arrays.asList(
                            new FAQ("Is it safe?", "Yes, it is 100% lab-tested."),
                            new FAQ("When to take?", "Take it after your workout.")
                    ))
                    .build();

            // Add dummy reviews
            for (int r = 1; r <= 2; r++) {
                Review review = Review.builder()
                        .username("reviewer" + r)
                        .stars(5)
                        .comment("Great product " + i + "! Highly recommended.")
                        .build();
                product.addReview(review);
            }

            productRepository.save(product);
        }

        // 5. Seed 3 Blogs
        for (int i = 1; i <= 3; i++) {
            Blog blog = Blog.builder()
                    .title("Healthy Living Tip #" + i)
                    .content("<p>This is a healthy living tip content for blog " + i + ". Always stay hydrated and eat clean.</p>")
                    .author("Nutra Expert")
                    .image(PLACEHOLDER_URL)
                    .category(categories.get(i % categories.size()))
                    .build();
            blogRepository.save(blog);
        }

        // 6. Seed 3 Information Pages
        for (int i = 1; i <= 3; i++) {
            Information info = Information.builder()
                    .title("Nutra Policy " + i)
                    .content("This is the detailed content for Policy " + i + ". We value our customers and their health.")
                    .image(PLACEHOLDER_URL)
                    .category(categories.get(0))
                    .build();
            informationRepository.save(info);
        }

        System.out.println("Data seeding completed successfully.");
    }

    private void createPlaceholderFile() throws IOException {
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path placeholder = path.resolve("placeholder.png");
        if (!Files.exists(placeholder)) {
            byte[] bytes = Base64.getDecoder().decode(MOCK_IMAGE_BASE64);
            Files.copy(new ByteArrayInputStream(bytes), placeholder);
            System.out.println("Placeholder image created at: " + placeholder);
        }
    }
}
