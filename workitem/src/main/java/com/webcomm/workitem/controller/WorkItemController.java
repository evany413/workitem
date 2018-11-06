package com.webcomm.workitem.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webcomm.workitem.model.Category;
import com.webcomm.workitem.model.Emp;
import com.webcomm.workitem.model.Item;
import com.webcomm.workitem.service.CategoryService;
import com.webcomm.workitem.service.EmpService;
import com.webcomm.workitem.service.ItemService;
import com.webcomm.workitem.service.ScheduleService;

@Controller
public class WorkItemController {

	List<String> testlist = new ArrayList<String>();

	@Autowired
	EmpService empService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	ItemService itemService;

	@Autowired
	ScheduleService scheduleService;

	/* 首頁 - WorkItems清單 */
	@GetMapping("/")
	public String goIndex(@ModelAttribute Item item, Model model) {
		System.out.println("*****into homepage*****");
		List<Emp> empList = empService.findAll();
		List<Category> categoryList = categoryService.findAllWithoutDayOff();
		List<Item> itemList = itemService.findAll();
		StringBuilder sb = new StringBuilder();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		Date currentDate = calendar.getTime();

		for (Item i : itemList) {
			if (i.getCreateDate().after(currentDate) || i.getCreateDate().equals(currentDate)) {
				sb.append(i.getEmp().getName()).append("@").append(i.getCategory().getDescription()).append(" - ").append(i.getContent()).append("@").append(i.getWorkTime()).append("\n");
			}
		}

		Date lastDate = itemService.getLastDate();
		Date startDate = itemService.getStartDate();
		Integer workHours = scheduleService.getWorkDayCount(startDate, new Date()) * 8; // from first workitem date till now
		Integer hoursFinish = itemService.getWorkHours();

		Integer hourLeft = workHours - hoursFinish; // 所有工作天時數 - WI完成時數

		model.addAttribute("empList", empList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("itemList", itemList);
		model.addAttribute("lastDate", lastDate);
		model.addAttribute("hourLeft", hourLeft);
		model.addAttribute("copyString", sb.toString());
		return "item_list";
	}

	/* 新增WorkItems */
	@PostMapping("/addWorkItems")
	public String addWorkItems(@Valid @ModelAttribute Item item, BindingResult bindingResult, Model model) {
		System.out.println("*****into addWorkItems*****");
		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			for (FieldError f : bindingResult.getFieldErrors()) {
				errorMsg.add(f.getDefaultMessage());
			}
			model.addAttribute("errorMsg", errorMsg);
			return goIndex(item, model);
		}
		itemService.addOne(item);
		return "redirect:/";
	}

	@PostMapping("/insertDayOff")
	public String InsertDayOff(@Valid @ModelAttribute Item item, BindingResult bindingResult, Model model) {
		System.out.println("*****into insertDayOff*****");
		List<String> errorMsg = new ArrayList<String>();
		Category dayOffCategory = categoryService.findDayOff();

		Item insertItem = new Item();
		BeanUtils.copyProperties(item, insertItem);// 建立一個插入的Item，泛回Item時仍然返回舊的。
		insertItem.setCategory(dayOffCategory); // 分類 設為"休假事項"
		insertItem.setContent("休假"); // 工作內容 設為"休假"

		if (bindingResult.hasErrors()) {
			for (FieldError f : bindingResult.getFieldErrors()) {
				if ("workTime".equals(f.getField())) {// 只顯示時間的錯誤訊息
					errorMsg.add(f.getDefaultMessage());
					model.addAttribute("errorMsg", errorMsg);
					return goIndex(item, model);
				}
			}
		}
		itemService.addOne(insertItem);
		return "redirect:/";
	}

	/* 使用者清單 */
	@GetMapping("/empList")
	public String goEmpList(@ModelAttribute Emp emp, Model model) {
		System.out.println("*****into empList*****");
		List<Emp> empList = empService.findAll();
//		model.addAttribute("emp", new Emp());
		model.addAttribute("empList", empList);
		return "emp_list";
	}

	/* 新增使用者 */
	@GetMapping("/addEmp")
	public String addEmp(@Valid @ModelAttribute Emp emp, BindingResult bindingResult, Model model) {
		System.out.println("*****into addEmp*****");
		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			for (FieldError f : bindingResult.getFieldErrors()) {
				errorMsg.add(f.getDefaultMessage());
			}
			model.addAttribute("errorMsg", errorMsg);
			return goEmpList(emp, model);
		}
		empService.addOne(emp);
		return "redirect:/empList";
	}

	/* 工作類別清單 */
	@GetMapping("/categoryList")
	public String goCategoryList(@ModelAttribute Category category, Model model) {
		System.out.println("*****into categoryList*****");
		List<Category> categoryList = categoryService.findAllWithoutDayOff();
		model.addAttribute("categoryList", categoryList);
		return "category_list";
	}

	/* 新增工作類別 */
	@GetMapping("/addCategory")
	public String addCategory(@Valid @ModelAttribute Category category, BindingResult bindingResult, Model model) {
		System.out.println("*****into addCategory*****");
		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			for (FieldError f : bindingResult.getFieldErrors()) {
				errorMsg.add(f.getDefaultMessage());
			}
			model.addAttribute("errorMsg", errorMsg);
			return goCategoryList(category, model);
		}
		categoryService.addOne(category);
		return "redirect:/categoryList";
	}

	/* 更新工作項目 */
	@PostMapping("/updateItem")
	public String updateItem(@Valid Item item, BindingResult bindingResult, @RequestParam(value = "pkItem") Long pkItem, Model model) {
		System.out.println("*****into updateItem*****");
		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			errorMsg.add("更新時出現錯誤：");
			for (FieldError f : bindingResult.getFieldErrors()) {
				errorMsg.add(f.getDefaultMessage());
			}
			model.addAttribute("errorMsg", errorMsg);
			return goIndex(new Item(), model);
		}
		Item updateItem = itemService.getOne(pkItem);
		BeanUtils.copyProperties(item, updateItem);
		itemService.update(updateItem);
		return "redirect:/";
	}

	/* 更新工作類別 */
	@PostMapping("/updateCategory")
	public String updateCategory(@Valid Category category, BindingResult bindingResult, @RequestParam(value = "pkCategory") Long pkCategory, Model model) {
		System.out.println("*****into updateCategory*****");
		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			errorMsg.add("更新時出現錯誤：");
			for (FieldError f : bindingResult.getFieldErrors()) {
				errorMsg.add(f.getDefaultMessage());
			}
			model.addAttribute("errorMsg", errorMsg);
			return goCategoryList(new Category(), model);
		}
		Category updateCategory = categoryService.getOne(pkCategory);
		BeanUtils.copyProperties(category, updateCategory);
		categoryService.update(updateCategory);
		return "redirect:/categoryList";
	}

	/* 更新使用者 */
	@PostMapping("/updateEmp")
	public String updateEmp(@Valid Emp emp, BindingResult bindingResult, @RequestParam(value = "pkEmp") Long pkEmp, Model model) {
		System.out.println("*****into updateCategory*****");
		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			errorMsg.add("更新時出現錯誤：");
			for (FieldError f : bindingResult.getFieldErrors()) {
				errorMsg.add(f.getDefaultMessage());
			}
			model.addAttribute("errorMsg", errorMsg);
			return goEmpList(new Emp(), model);
		}
		Emp updateEmp = empService.getOne(pkEmp);
		BeanUtils.copyProperties(emp, updateEmp);
		empService.update(updateEmp);
		return "redirect:/empList";
	}

	/* 刪除工作項目 */
	@PostMapping("/deleteItem")
	public String deleteItem(@ModelAttribute Item item, Model model) {
		itemService.delete(item);
		return "redirect:/";
	}

	/* 刪除工作類別 */
	@PostMapping("/deleteCategory")
	public String deleteCategory(@ModelAttribute Category category, Model model) {
		categoryService.delete(category);
		return "redirect:/categoryList";
	}

	/* 刪除使用者 */
	@PostMapping("/deleteEmp")
	public String deleteEmp(@ModelAttribute Emp emp, Model model) {
		empService.delete(emp);
		return "redirect:/empList";
	}

//	@GetMapping("/test")
//	public String testEmp(@ModelAttribute Emp emp) {
//		System.out.println("*****into test*****");
//		System.out.println(emp.getPkEmp());
//		return "test";
//	}
//
//	@RequestMapping("/addTest")
//	public String addTestEmp(@ModelAttribute Emp emp) {
//		System.out.println("*****into addTest*****");
//		System.out.println(emp.getPkEmp());
//		return "test";
//	}
//
//	@GetMapping("/test2")
//	public String testEmp2(@ModelAttribute Emp2 emp2) {
//		System.out.println("*****into test2*****");
//		System.out.println(emp2.getPkEmp2());
//		return "test2";
//	}
//
//	@RequestMapping("/addTest2")
//	public String addTestEmp2(@ModelAttribute Emp2 emp2) {
//		System.out.println("*****into addTest2*****");
//		System.out.println(emp2.getPkEmp2());
//		return "test2";
//	}

}
