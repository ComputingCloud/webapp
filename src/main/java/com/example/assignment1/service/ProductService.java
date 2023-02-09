package com.example.assignment1.service;


import com.example.assignment1.exeception.DataNotFoundExeception;
import com.example.assignment1.exeception.InvalidInputException;
import com.example.assignment1.exeception.UserAuthrizationExeception;
import com.example.assignment1.model.Product;
import com.example.assignment1.model.User;
import com.example.assignment1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserService userService;


    public Product productCreate(Product product, String userName) throws UserAuthrizationExeception, InvalidInputException {
        User userObj = userService.loadUserByUsername(userName);
        if(userObj != null){
            skuCheck(1, userObj.getId(), product.getSku(), "PostCheck");
            product.setOwnerUserId(userObj.getId());
            productRepository.saveAndFlush(product);
            return product;
        }
        throw new UserAuthrizationExeception("Unauthorized Username, does not Exists");
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


    public String updateProduct(Integer productId, Product product) throws DataNotFoundExeception{

        Product prod = getProduct(productId);

        prod.setId(productId);
        prod.setPName(product.getPName());
        prod.setPDescription(product.getPDescription());
        prod.setPQuantity(product.getPQuantity());

        prod.setSku(product.getSku());
        prod.setPManufacturer(product.getPManufacturer());

        productRepository.saveAndFlush(prod);      // flushes the data immediately during the execution

        return "Details of the product updated";
    }

    public String deleteProduct(Integer productId) throws DataNotFoundExeception {
        Product prod = getProduct(productId);
        productRepository.deleteById(prod.getId());

        return "Product deleted!!";

    }

}
