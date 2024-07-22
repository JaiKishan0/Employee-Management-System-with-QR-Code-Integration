package com.example.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Employee;
import com.example.service.EmployeeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@RestController
@RequestMapping("/api")
public class CrudController {
    @Autowired
    EmployeeService service;

    @PostMapping("/employee")
    public ResponseEntity<Employee> save(@RequestBody Employee emp) {
        return ResponseEntity.ok().body(service.save(emp));
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmplById(@PathVariable int id) {
        return ResponseEntity.ok().body(service.getEmplById(id));
    }

    @GetMapping("/employee")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok().body(service.getEmployees());
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        employee.setId(id);
        return ResponseEntity.ok().body(service.updateEmployee(employee));
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
        service.deleteEmplById(id);
        return ResponseEntity.ok().body("Record has been deleted");
    }

    @GetMapping("/employee/{id}/qrcode")
    public ResponseEntity<byte[]> generateQRCode(@PathVariable int id) {
        Employee emp = service.getEmplById(id);
        if (emp == null) {
            return ResponseEntity.notFound().build();
        }

        String qrText = "Employee ID: " + emp.getId() + ", Name: " + emp.getName();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String filePath = "C:\\Users\\jaiki\\OneDrive\\Desktop\\Spring_Boot_Workspace\\QR code" + id + ".png";

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Save QR code to file
            File qrFile = new File(filePath);
            ImageIO.write(qrImage, "PNG", qrFile);

            // Write QR code to ByteArrayOutputStream
            ImageIO.write(qrImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            // Set response headers and return image
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "image/png");
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
}}
