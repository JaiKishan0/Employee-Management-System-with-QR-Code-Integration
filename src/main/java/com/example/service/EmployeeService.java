package com.example.service;

import java.util.List;

import com.example.entity.Employee;

public interface EmployeeService {
	public Employee save(Employee emp);
	public Employee getEmplById(int id);
	public List<Employee> getEmployees();
	public Employee updateEmployee(Employee emp);
	public void deleteEmplById(int id);
     public String generateQRCodeForEmployee(int id, String filePath);


}
