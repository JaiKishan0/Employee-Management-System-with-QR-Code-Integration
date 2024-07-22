package com.example.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Employee;
import com.example.error.RecordNotFoundException;
import com.example.repository.EmployeeRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class EmployeeServiceImp implements EmployeeService {
    @Autowired
    EmployeeRepository repo;

    @Override
    public Employee save(Employee emp) {
        return repo.save(emp);
    }

    @Override
    public Employee getEmplById(int id) {
        Optional<Employee> empDb = repo.findById(id);
        if (empDb.isPresent()) {
            return empDb.get();
        } else {
            throw new RecordNotFoundException("Record not found");
        }
    }

    @Override
    public List<Employee> getEmployees() {
        return repo.findAll();
    }

    @Override
    public Employee updateEmployee(Employee emp) {
        Optional<Employee> empDb = repo.findById(emp.getId());
        if (empDb.isPresent()) {
            Employee employee = empDb.get();
            employee.setSalary(emp.getSalary());
            employee.setEmail(emp.getEmail());
            repo.save(employee);
            return employee;
        } else {
            throw new RecordNotFoundException("Record not found");
        }
    }

    @Override
    public void deleteEmplById(int id) {
        Optional<Employee> emp = repo.findById(id);
        if (emp.isPresent()) {
            repo.delete(emp.get());
        } else {
            throw new RecordNotFoundException("Record not found");
        }
    }

    @Override
    public String generateQRCodeForEmployee(int id, String filePath) {
        Optional<Employee> empDb = repo.findById(id);
        if (!empDb.isPresent()) {
            throw new RecordNotFoundException("Record not found");
        }

        Employee emp = empDb.get();
        String qrText = "Employee ID: " + emp.getId() + ", Name: " + emp.getName();
        
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            File qrFile = new File(filePath);
            ImageIO.write(qrImage, "PNG", qrFile);

            return "QR code generated and saved to " + filePath;
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return "Failed to generate QR code";
        }
    }
}
