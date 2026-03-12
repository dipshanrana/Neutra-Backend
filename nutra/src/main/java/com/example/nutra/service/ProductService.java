package com.example.nutra.service;

import com.example.nutra.model.Category;
import com.example.nutra.model.Product;
import com.example.nutra.model.Review;

import com.example.nutra.repository.ProductRepository;
import com.example.nutra.repository.CategoryRepository;
import com.example.nutra.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    public Product addProduct(Product product, List<MultipartFile> featuredImages, MultipartFile singleProductImage,
            MultipartFile twoProductImage, MultipartFile threeProductImage) throws IOException {
        if (product.getCategory() != null) {
            Category cat = categoryRepository.findByNameIgnoreCase(product.getCategory().getName());
            if (cat == null) {
                cat = categoryRepository.save(product.getCategory());
            }
            product.setCategory(cat);
        }
        if (product.getReviews() != null) {
            for (Review r : product.getReviews()) {
                r.setProduct(product);
            }
        }

        if (featuredImages != null && !featuredImages.isEmpty()) {
            List<String> imagePaths = new ArrayList<>();
            for (MultipartFile file : featuredImages) {
                imagePaths.add(fileStorageService.storeFile(file));
            }
            product.setFeaturedImages(imagePaths);
        }

        if (singleProductImage != null && !singleProductImage.isEmpty()) {
            product.setSingleProductImage(fileStorageService.storeFile(singleProductImage));
        }
        if (twoProductImage != null && !twoProductImage.isEmpty()) {
            product.setTwoProductImage(fileStorageService.storeFile(twoProductImage));
        }
        if (threeProductImage != null && !threeProductImage.isEmpty()) {
            product.setThreeProductImage(fileStorageService.storeFile(threeProductImage));
        }

        if (product.getFeaturedImages() != null && product.getFeaturedImages().size() != 2) {
            throw new IllegalArgumentException("There must be exactly 2 featured images.");
        }
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    public List<Product> getProductsFromThreeCategories() {
        List<String> categories = productRepository.findAllCategories();
        Collections.shuffle(categories);
        return categories.stream()
                .limit(3)
                .map(category -> productRepository.findFirstByCategoryNameIgnoreCase(category))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Product> getAllProductsRandomly() {
        return productRepository.findAllRandomly();
    }

    public List<Product> searchByCategory(String category) {
        return productRepository.findByCategoryNameIgnoreCase(category);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> searchByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findBySingleProductSpBetween(minPrice, maxPrice);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product updateProduct(Long id, Product productDetails, List<MultipartFile> featuredImages,
            MultipartFile singleProductImage, MultipartFile twoProductImage, MultipartFile threeProductImage)
            throws IOException {
        Product existingProduct = getProductById(id);
        existingProduct.setName(productDetails.getName());
        existingProduct.setLink(productDetails.getLink());

        if (productDetails.getCategory() != null) {
            Category cat = categoryRepository.findByNameIgnoreCase(productDetails.getCategory().getName());
            if (cat == null) {
                cat = categoryRepository.save(productDetails.getCategory());
            }
            existingProduct.setCategory(cat);
        }

        existingProduct.setDescription(productDetails.getDescription());

        existingProduct.setSingleProductMp(productDetails.getSingleProductMp());
        existingProduct.setSingleProductSp(productDetails.getSingleProductSp());

        existingProduct.setTwoProductMp(productDetails.getTwoProductMp());
        existingProduct.setTwoProductSp(productDetails.getTwoProductSp());

        existingProduct.setThreeProductMp(productDetails.getThreeProductMp());
        existingProduct.setThreeProductSp(productDetails.getThreeProductSp());

        existingProduct.setDiscount(productDetails.getDiscount());
        existingProduct.setBadge(productDetails.getBadge());
        existingProduct.setCategoryBadge(productDetails.getCategoryBadge());
        existingProduct.setBenefitsParagraph(productDetails.getBenefitsParagraph());
        existingProduct.setBenefits(productDetails.getBenefits());

        if (featuredImages != null && !featuredImages.isEmpty()) {
            // Delete old files
            if (existingProduct.getFeaturedImages() != null) {
                existingProduct.getFeaturedImages().forEach(fileStorageService::deleteFile);
            }
            List<String> imagePaths = new ArrayList<>();
            for (MultipartFile file : featuredImages) {
                imagePaths.add(fileStorageService.storeFile(file));
            }
            existingProduct.setFeaturedImages(imagePaths);
        }

        if (singleProductImage != null && !singleProductImage.isEmpty()) {
            fileStorageService.deleteFile(existingProduct.getSingleProductImage());
            existingProduct.setSingleProductImage(fileStorageService.storeFile(singleProductImage));
        }
        if (twoProductImage != null && !twoProductImage.isEmpty()) {
            fileStorageService.deleteFile(existingProduct.getTwoProductImage());
            existingProduct.setTwoProductImage(fileStorageService.storeFile(twoProductImage));
        }
        if (threeProductImage != null && !threeProductImage.isEmpty()) {
            fileStorageService.deleteFile(existingProduct.getThreeProductImage());
            existingProduct.setThreeProductImage(fileStorageService.storeFile(threeProductImage));
        }

        if (existingProduct.getFeaturedImages() != null && existingProduct.getFeaturedImages().size() != 2) {
            throw new IllegalArgumentException("There must be exactly 2 featured images.");
        }

        existingProduct.setServingSize(productDetails.getServingSize());
        existingProduct.setCapsulesPerContainer(productDetails.getCapsulesPerContainer());
        existingProduct.setSupplementFacts(productDetails.getSupplementFacts());
        existingProduct.setFreebies(productDetails.getFreebies());
        existingProduct.setHowToUse(productDetails.getHowToUse());
        existingProduct.setFaqs(productDetails.getFaqs());

        if (productDetails.getReviews() != null) {
            if (existingProduct.getReviews() == null) {
                existingProduct.setReviews(new java.util.ArrayList<>());
            } else {
                existingProduct.getReviews().clear();
            }
            for (Review r : productDetails.getReviews()) {
                r.setProduct(existingProduct);
                existingProduct.getReviews().add(r);
            }
        } else {
            if (existingProduct.getReviews() != null) {
                existingProduct.getReviews().clear();
            }
        }
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product p = getProductById(id);
        if (p.getFeaturedImages() != null) p.getFeaturedImages().forEach(fileStorageService::deleteFile);
        fileStorageService.deleteFile(p.getSingleProductImage());
        fileStorageService.deleteFile(p.getTwoProductImage());
        fileStorageService.deleteFile(p.getThreeProductImage());
        productRepository.deleteById(id);
    }

    public List<Product> searchByBadge(String badge) {
        return productRepository.findByBadgeIgnoreCase(badge);
    }

    public List<Product> searchByCategoryBadge(String categoryBadge, String categoryName) {
        return productRepository.findByCategoryBadgeIgnoreCaseAndCategoryNameIgnoreCase(categoryBadge, categoryName);
    }
}
