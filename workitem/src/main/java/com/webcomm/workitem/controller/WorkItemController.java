package com.webcomm.workitem.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import com.webcomm.workitem.model.Category;
import com.webcomm.workitem.model.Item;
import com.webcomm.workitem.model.PccDeveloper;
import com.webcomm.workitem.service.CategoryService;
import com.webcomm.workitem.service.ItemService;
import com.webcomm.workitem.service.PccDeveloperService;
import com.webcomm.workitem.service.ScheduleService;
import com.webcomm.workitem.util.ExcelExpoter;

@Controller
public class WorkItemController {

	List<String> testlist = new ArrayList<String>();

	@Autowired
	PccDeveloperService pccDeveloperService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	ItemService itemService;

	@Autowired
	ScheduleService scheduleService;

	@Autowired
	ExcelExpoter excelExpoter;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;

	/* 首頁 - WorkItems清單 */
	@GetMapping("/")
	public String goIndex(@ModelAttribute Item item, Model model) {
		System.out.println("*****into homepage*****");
		List<PccDeveloper> pccDeveloperList = pccDeveloperService.findAll();
		List<Category> categoryList = categoryService.findAllWithoutDayOff();

		// 取得cookie中所存的userId(為預設顯示的user)
		Cookie cookie = WebUtils.getCookie(request, "userId");
		
		if (null == cookie) {
			if(pccDeveloperList.size()>0) {
				cookie = new Cookie("userId", Long.toString(pccDeveloperList.get(0).getPkPccDeveloper())); // 以pccDeveloperList中第一個的pk做為預設值
			}else {
				cookie = new Cookie("userId", "1"); // 若list無值
			}
			response.addCookie(cookie);
		}

		Long userId = Long.parseLong(cookie.getValue());
		PccDeveloper selectedPccDeveloper = pccDeveloperService.getOne(userId);

		// 取出該user的work item
		List<Item> itemList = itemService.findAllByPccDeveloperWithOutDayOff(selectedPccDeveloper);
		List<Item> itemListWithDayOff = itemService.findAllByPccDeveloper(selectedPccDeveloper);

		StringBuilder sb = new StringBuilder();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		Date currentDate = calendar.getTime();

		for (Item i : itemList) {
			if (i.getCreateDate().after(currentDate) || i.getCreateDate().equals(currentDate)) {
				sb.append(i.getPccDeveloper().getName()).append("@").append(i.getCategory().getDescription()).append(" - ").append(i.getContent()).append("@").append(i.getWorkTime()).append("\n");
			}
		}

		Date startDate = itemService.getStartDateByPccDeveloper(selectedPccDeveloper);
		Date lastDate = itemService.getLastDateByPccDeveloper(selectedPccDeveloper);
		Integer workHours = scheduleService.getWorkDayCount(startDate, new Date()) * 8; // from first workitem date till now
		Integer hoursFinish = itemService.getPccDeveloperWorkHours(selectedPccDeveloper);

		Integer hourLeft = workHours - hoursFinish; // 所有工作天時數 - WI完成時數

		model.addAttribute("selectedPccDeveloper", selectedPccDeveloper);
		model.addAttribute("pccDeveloperList", pccDeveloperList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("itemList", itemListWithDayOff);
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
	@GetMapping("/pccDeveloperList")
	public String goPccDeveloperList(@ModelAttribute PccDeveloper pccDeveloper, Model model) {
		System.out.println("*****into pccDeveloperList*****");
		List<PccDeveloper> pccDeveloperList = pccDeveloperService.findAll();
		model.addAttribute("pccDeveloperList", pccDeveloperList);
		return "pccDeveloper_list";
	}

	/* 新增使用者 */
	@GetMapping("/addPccDeveloper")
	public String addPccDeveloper(@Valid @ModelAttribute PccDeveloper pccDeveloper, BindingResult bindingResult, Model model) {
		System.out.println("*****into addPccDeveloper*****");
		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			for (FieldError f : bindingResult.getFieldErrors()) {
				errorMsg.add(f.getDefaultMessage());
			}
			model.addAttribute("errorMsg", errorMsg);
			return goPccDeveloperList(pccDeveloper, model);
		}
		pccDeveloperService.addOne(pccDeveloper);
		return "redirect:/pccDeveloperList";
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
	@PostMapping("/updatePccDeveloper")
	public String updatePccDeveloper(@Valid PccDeveloper pccDeveloper, BindingResult bindingResult, @RequestParam(value = "pkPccDeveloper") Long pkPccDeveloper, Model model) {
		System.out.println("*****into updateCategory*****");
		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			errorMsg.add("更新時出現錯誤：");
			for (FieldError f : bindingResult.getFieldErrors()) {
				errorMsg.add(f.getDefaultMessage());
			}
			model.addAttribute("errorMsg", errorMsg);
			return goPccDeveloperList(new PccDeveloper(), model);
		}
		PccDeveloper updatePccDeveloper = pccDeveloperService.getOne(pkPccDeveloper);
		BeanUtils.copyProperties(pccDeveloper, updatePccDeveloper);
		pccDeveloperService.update(updatePccDeveloper);
		return "redirect:/pccDeveloperList";
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
	@PostMapping("/deletePccDeveloper")
	public String deletePccDeveloper(@ModelAttribute PccDeveloper pccDeveloper, Model model) {
		pccDeveloperService.delete(pccDeveloper);
		return "redirect:/pccDeveloperList";
	}

	/* 下載檔案 */
	@RequestMapping("/downLoadFile")
	public void downloadFile(HttpServletResponse response) {
		String filename = "workitem.xls";
		try {
			HSSFWorkbook wb = excelExpoter.exportXLS();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
			wb.write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
