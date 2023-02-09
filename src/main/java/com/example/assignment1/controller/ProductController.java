package com.example.assignment1.controller;


import com.example.assignment1.constants.UserConstants;
import com.example.assignment1.exeception.DataNotFoundExeception;
import com.example.assignment1.exeception.InvalidInputException;
import com.example.assignment1.exeception.UserAuthrizationExeception;
import com.example.assignment1.model.Product;
import com.example.assignment1.service.AuthService;
import com.example.assignment1.service.ProductService;
import com.example.assignment1.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("v1/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @RestControllerAdvice
    public class MyExceptionHandler {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
            List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
            String errorMessage = fieldErrors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    public ResponseEntity<?> productCreate(@Valid @RequestBody Product product, Errors error, HttpServletRequest request) {
        try {
            if (error.hasErrors()) {
                String response = error.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(","));
                throw new InvalidInputException(response);
            }

            return new ResponseEntity<Product>(
                    productService.productCreate(product,
                            authService.getUserNameFrmToken(request.getHeader("Authorization").split(" ")[1])),
                    HttpStatus.CREATED);
        } catch (InvalidInputException e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserAuthrizationExeception e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable("productId") Integer productId) {
        try {
            if (productId.toString().isBlank() || productId.toString().isEmpty()) {
                throw new InvalidInputException("Enter Valid Product Id");
            }
            return new ResponseEntity<Product>(productService.getProduct(productId), HttpStatus.OK);
        } catch (InvalidInputException e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DataNotFoundExeception e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{productId}")
    public ResponseEntity<?> updateUserDetails(@PathVariable("productId") Integer productId,
                                               @Valid @RequestBody Product product, HttpServletRequest request, Errors error) {
        try {
            if (productId.toString().isBlank() || productId.toString().isEmpty()) {
                throw new InvalidInputException("Enter Valid Product Id");
            }
           authService.isAuthorised(productService.getProduct(productId).getOwnerUserId(),request.getHeader("Authorization").split(" ")[1]); //isAuthorised(productService.getProduct(productId).getOwnerUserId(),

            if (error.hasErrors()) {
                String response = error.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(","));
                throw new InvalidInputException(response);
            }
            return new ResponseEntity<String>(productService.updateProduct(productId, product),
                    HttpStatus.NO_CONTENT);
        } catch (InvalidInputException e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserAuthrizationExeception e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (DataNotFoundExeception e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PatchMapping(value = "/{productId}")
    public ResponseEntity<?> patchUser(@PathVariable("product") Integer productId, @Valid @RequestBody Product product, HttpServletRequest request, Errors error) {

        try{
            if(productId.toString().isBlank() || productId.toString().isEmpty()){
                throw new InvalidInputException("Enter Valid Product Id");

            }
            authService.isAuthorised(productService.getProduct(productId).getOwnerUserId(),request.getHeader("Authorization").split(" ")[1]);
            return new ResponseEntity<String>(productService.updateProduct(productId, product),
                    HttpStatus.NO_CONTENT);
        }
        catch (InvalidInputException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (UserAuthrizationExeception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (DataNotFoundExeception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping(value = "/{productId}")
    public ResponseEntity<?> deleteUser(@PathVariable("productId") Integer productId, HttpServletRequest request){
        try{
            if(productId.toString().isBlank() || productId.toString().isEmpty()){
                throw new InvalidInputException("Enter Valid ProductID");

            }
            authService.isAuthorised(productService.getProduct(productId).getOwnerUserId(), request.getHeader("Authorization").split(" ")[1]);
            return new ResponseEntity<String>(productService.deleteProduct(productId), HttpStatus.NO_CONTENT);

        }catch (InvalidInputException e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserAuthrizationExeception e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (DataNotFoundExeception e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>(UserConstants.InternalErr, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    }
