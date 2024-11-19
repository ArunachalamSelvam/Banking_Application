package com.banking.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/uploadImageServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
                 maxFileSize = 1024 * 1024 * 10,  // 10 MB
                 maxRequestSize = 1024 * 1024 * 15) // 15 MB
public class SaveCustomerImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String UPLOAD_DIR = "customerImages";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	System.out.println("Inside Image servlet");

        // Get the upload directory path
        String applicationPath = getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

        // Debugging
        System.out.println("Application Path: " + applicationPath);
        System.out.println("Upload File Path: " + uploadFilePath);
        
        // Create the directory if it doesn't exist
        File uploadDir = new File(uploadFilePath);
        if (!uploadDir.exists()) {
            boolean dirCreated = uploadDir.mkdirs();
            System.out.println("Directory created: " + dirCreated);
        }

        // Process the uploaded image
        Part filePart = request.getPart("image"); // The "image" is the name of the form field
        if (filePart == null) {
            System.out.println("File part is null!");
            response.getWriter().println("Error: No image file received.");
            return;
        }

        String fileName = filePart.getSubmittedFileName();
        System.out.println("File Name: " + fileName);

        // Create the path to save the file
        String filePath = uploadFilePath + File.separator + fileName;
        System.out.println("File Path: " + filePath);

        // Save the file on the server
        try (FileOutputStream outputStream = new FileOutputStream(filePath);
             InputStream fileContent = filePart.getInputStream()) {
            
            int read;
            final byte[] bytes = new byte[1024];
            while ((read = fileContent.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            System.out.println("Image saved successfully.");
            response.getWriter().println("Image uploaded successfully as " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            response.getWriter().println("Error occurred while saving the image: " + e.getMessage());
        }
    }
}
