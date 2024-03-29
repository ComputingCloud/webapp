package com.example.assignment1.service;


import com.example.assignment1.exeception.DataNotFoundExeception;
import com.example.assignment1.exeception.InvalidInputException;
import com.example.assignment1.exeception.UserAuthrizationExeception;
import com.example.assignment1.model.Product;
import com.example.assignment1.model.User;
import com.example.assignment1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserService userService;
    @Autowired
    ImageService imageService;


    public Product productCreate(Product product, String userName) throws UserAuthrizationExeception, InvalidInputException {
        User userobj = userService.loadUserByUsername(userName);
        if (userobj != null) {
            skuCheck(1, userobj.getId(), product.getSku(), "PostCheck");
            product.setOwnerUserId(userobj.getId());
            productRepository.saveAndFlush(product);
            return product;
        }
        // throw auth error
        throw new UserAuthrizationExeception("UnAuthrized username dose not exists");

    }

    public Product skuCheck(Integer id,Integer ownerId,String sku,String check) throws InvalidInputException {
        Product p;
        if(check.equals("PostCheck"))
            p=productRepository.findProductByownerUserIdAndSku(ownerId,sku);
        else
            p=productRepository.checkProductSku( id,ownerId, sku);
        if(p==null)
            return p;
        throw new InvalidInputException("SKU Value Exists already");
    }


    public Product getProduct(Integer productID) throws DataNotFoundExeception{
        Optional<Product> product = productRepository.findById(productID);

        if(product.isPresent()){
            return product.get();
        }
        throw new DataNotFoundExeception("Product Associated with given id: "+ productID + " Not present");
    }


    public String updateProduct(Integer productId, Product product) throws DataNotFoundExeception, InvalidInputException {

        Product p = getProduct(productId);
        skuCheck(p.getId(), p.getOwnerUserId(), product.getSku(), "PutCheck");
        p.setId(productId);
        p.setPName(product.getPName());
        p.setPDescription(product.getPDescription());
        p.setSku(product.getSku());
        p.setPManufacturer(product.getPManufacturer());
        p.setPQuantity(product.getPQuantity());
        productRepository.saveAndFlush(p);
        return "Updated Product Details";
    }

    public String patchProducts(Integer productId, Map<String,Object> updates) throws DataNotFoundExeception, InvalidInputException {
        Product prod = getProduct(productId);

        if(updates.size()==0){
            throw new InvalidInputException("Request Should not be empty");
        }
        for(Map.Entry<String, Object> map : updates.entrySet()){
            switch(map.getKey()){
                case "pName":
                    String name= (String) map.getValue();
                    if(name.isBlank() || name.isEmpty() || name == null)
                        throw new InvalidInputException("Prduct name cannot be empty");
                    else
                        prod.setPName(name);
                    break;
                case "pDescription" :
                    String description = (String) map.getValue();
                    if(description.isBlank() || description.isEmpty())
                        throw new InvalidInputException("Description cannot be empty");
                    prod.setPDescription(description);
                    break;
                case "sku":
                    String sku = (String) map.getValue();
                    if (sku.isBlank() || sku.isEmpty()||sku==null)
                        throw new InvalidInputException("Product SKU can't be null/empty");
                    skuCheck(prod.getId(), prod.getOwnerUserId(), sku, "PostCheck");
                    prod.setSku(sku);
                    break;
                case "pManufacture":
                    String manufacture = (String) map.getValue();
                    if (manufacture.isBlank() || manufacture.isEmpty()||manufacture==null)
                        throw new InvalidInputException("Product manufacture can't be null/empty");
                    prod.setPManufacturer(manufacture);
                    break;
                case "pQuantity":
                    Integer quantity = Integer.parseInt((String)(map.getValue()));
                    if (quantity < 0 || quantity > 100)
                        throw new InvalidInputException("Product quantity should be btw 1 and 100");
                    prod.setPQuantity(quantity);
                    break;
            }
        }
        productRepository.saveAndFlush(prod);

        return "Product deleted!!";

    }


    public String deleteProductDetails(Integer productId) throws DataNotFoundExeception {
        Product p = getProduct(productId);
        productRepository.deleteById(p.getId());
        imageService.deleteImageByProductId(productId, p.getOwnerUserId());

        return "Deleted Product";
    }

}
