package com.example.tutorial3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.tutorial3.service.InMemoryStudentService;
import com.example.tutorial3.service.StudentService;
import com.example.tutorial3.model.StudentModel;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StudentController {
	private final StudentService studentService;
	
	public StudentController() {
		studentService = new InMemoryStudentService();
	}
	
	@RequestMapping("/student/add")
	public String add(@RequestParam(value = "npm", required = true) String npm, 
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "gpa", required = true) double gpa) {
		StudentModel student = new StudentModel(npm, name, gpa);
		studentService.addStudent(student);
		return "add";
	}
	
	/* @RequestMapping("/student/view")
	public String view(Model model, @RequestParam(value = "npm", required = true) String npm) {
		StudentModel student = studentService.selectStudent(npm);
		model.addAttribute("student", student);
		return "view";
	} */
	
	@RequestMapping("/student/viewall")
	public String viewAll(Model model) {
		List<StudentModel> students = studentService.selectAllStudents();
		model.addAttribute("students", students);
		return "viewall";
	}
	
	@RequestMapping(value = {"/student/view", "/student/view/{npm}"})
	public String npmPath (@PathVariable Optional<String> npm, Model model) {
		if (npm.isPresent()) {
			StudentModel student = studentService.selectStudent(npm.get());
			
			if (student == null) {
				return "notfound";
			} else {
				model.addAttribute("student", student);
				return "view";
			}
		} else {
			return "notfound";
		}
	}
	
	@RequestMapping(value = {"/student/delete", "student/delete/{npm}"})
	public String deleteNpmPath (@PathVariable Optional<String> npm, Model model) {
		if (npm.isPresent()) {
			StudentModel student = studentService.selectStudent(npm.get());
			
			if (student == null) {
				return "notfound";
			} else {
				StudentModel delete = studentService.delete(npm.get());
				model.addAttribute("studentName", delete.getName());
				model.addAttribute("studentNpm", delete.getNpm());
				model.addAttribute("studentGpa", delete.getGpa());
				return "deleteStudent";
			}
		} else {
			return "notfound";
		}
	}
}
