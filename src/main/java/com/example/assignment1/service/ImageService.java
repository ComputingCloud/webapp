package com.example.assignment1.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.assignment1.exeception.BadInputException;
import com.example.assignment1.exeception.DataNotFoundExeception;
import com.example.assignment1.model.Image;
import com.example.assignment1.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;

@Service
public class ImageService {

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Autowired
    private FileStore fileStore;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AmazonS3 amazonS3;


    public Image saveImage(Integer productId, Integer userId, MultipartFile file) {
        // check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        System.out.println("10");

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setCacheControl("public, max-age=31536000");
        String path = String.format("%s/%s/%s", bucketName, userId, productId);
        String fileName = String.format("%s/%s",String.valueOf(UUID.randomUUID()), file.getOriginalFilename());
        System.out.println(path+" "+fileName);
        System.out.println(amazonS3);
        try {
            System.out.println("create Image "+59);
            amazonS3.putObject(path, fileName,  file.getInputStream(), objectMetadata);
            System.out.println("create Image "+60);
        } catch (IOException e) {
            System.out.println(e);
            throw new IllegalStateException("Failed to upload file", e);
        }
        Image image = Image.builder().productId(productId).fileName(fileName).s3BucketPath(String.valueOf(amazonS3.getUrl(path, fileName))).build();
        return imageRepository.saveAndFlush(image);
    }

    public List<Image> getAllImages(Integer productId, Integer userId) throws DataNotFoundExeception {
        // TODO Auto-generated method stub
        List<Image> images = imageRepository.findImageByProductId(productId);
        if(images==null)
            throw new DataNotFoundExeception("No Product with given Id");
        return images;
    }

    public Image getImage(Integer productId, Integer userId, Integer imageId) throws DataNotFoundExeception, BadInputException {
        // TODO Auto-generated method stub
        Optional<Image> imgObj=imageRepository.findById(imageId);
        if(imgObj.isEmpty())
            throw new DataNotFoundExeception("No Image with given Id");
        System.out.println(imgObj.get().getProductId()+" "+productId);
        if(imgObj.get().getProductId()!=productId) {
            throw new BadInputException("ProductId and imageId won't match");
        }
        return imgObj.get();
    }

	public Image deleteImage(Integer productId, Integer userId, Integer imageId)  throws DataNotFoundExeception, BadInputException {
		// TODO Auto-generated method stub
        Optional<Image> imgObj=imageRepository.findById(imageId);
        if(imgObj.isEmpty())
            throw new DataNotFoundExeception("No Image with given Id");
        System.out.println(imgObj.get().getProductId()+" "+productId);
        if(imgObj.get().getProductId()!=productId)
            throw new BadInputException("ProductId and imageId won't match");
        String path_name=String.format("%s/%s/%s", userId, productId,imgObj.get().getFileName());
        fileStore.deleteFile(bucketName,path_name);
        imageRepository.deleteById(imageId);
        return imgObj.get();
	}

    public void deleteImageByProductId(Integer productId, Integer userId)  throws DataNotFoundExeception {
        String path = String.format("%s/%s/", userId, productId);
        String folderName = path;
        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(folderName);
        ListObjectsV2Result objects = amazonS3.listObjectsV2(listObjectsRequest);
        List<S3ObjectSummary> summaries = objects.getObjectSummaries();
        System.out.println(objects+" "+summaries);
        for (S3ObjectSummary summary : summaries) {
            System.out.println(summary.getKey());
            amazonS3.deleteObject(bucketName, summary.getKey());
        }
        amazonS3.deleteObject(bucketName, folderName);
    }

}
