package com.webcomm.workitem.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.webcomm.workitem.model.CategoryDetail;
import com.webcomm.workitem.model.Item;
import com.webcomm.workitem.model.PccDeveloper;
import com.webcomm.workitem.service.CategoryDetailService;
import com.webcomm.workitem.service.CategoryService;
import com.webcomm.workitem.service.ItemService;
import com.webcomm.workitem.service.PccDeveloperService;
import com.webcomm.workitem.service.ScheduleService;
import com.webcomm.workitem.util.DateUtil;
import com.webcomm.workitem.util.ExcelExpoter;

@Controller
public class WorkItemController {

	@Autowired
	CategoryService categoryService;
	@Autowired
	CategoryDetailService categoryDetailService;
	@Autowired
	PccDeveloperService pccDeveloperService;
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
		List<Category> categoryWODayOffList = categoryService.findAllWithoutDayOff();
		List<Category> categoryAllList = categoryService.findAll();
		List<CategoryDetail> categoryDetailWODayOffList = categoryDetailService.findAllWithoutDayOff();
		List<CategoryDetail> categoryDetailAllList = categoryDetailService.findAll();
		List<PccDeveloper> pccDeveloperList = pccDeveloperService.findAll();

		/* 取得cookie中所存的userId(為預設顯示的user) */
		Cookie cookie = WebUtils.getCookie(request, "userId");
		if (null == cookie || "null".equals(cookie.getValue())) { //前端Cookie若無值，會取得null字串
			if (pccDeveloperList.size() > 0) {
				cookie = new Cookie("userId", Long.toString(pccDeveloperList.get(0).getPkPccDeveloper())); // 以pccDeveloperList中第一個的key做為預設值
			} else {
				cookie = new Cookie("userId", "1"); // if pccDeveloperList is empty, set a default value
			}
			response.addCookie(cookie);
		}
		Long userId = Long.parseLong(cookie.getValue());
		PccDeveloper selectedPccDeveloper = pccDeveloperService.getOne(userId);

		/* 取出該user的work item */
		List<Item> itemList = itemService.findAllByPccDeveloperWithOutDayOff(selectedPccDeveloper);
		List<Item> itemListWithDayOff = itemService.findAllByPccDeveloper(selectedPccDeveloper);

		/* 回傳複製所需的字串，evan@待辦事項 - 三代採購網 - 研究與學習spring boot */
		StringBuilder sb = new StringBuilder();
		Date currentDate = DateUtil.getPureDate(new Date()); // 今天日期(不含時分秒)
		for (Item i : itemList) {
			if (i.getCreateDate().after(currentDate) || i.getCreateDate().equals(currentDate)) {
				sb.append(i.getPccDeveloper().getName()).append("@").append(i.getCategoryDetail().getCategory().getDescription()).append(" - ").append(i.getCategoryDetail().getDescription()).append(" - ").append(i.getContent()).append("@").append(i.getWorkTime()).append("\n");
			}
		}

		/* calculate WI time */
		Date startDate = itemService.getStartDateByPccDeveloper(selectedPccDeveloper);
		Date lastDate = itemService.getLastDateByPccDeveloper(selectedPccDeveloper);
		Integer workHours = scheduleService.getWorkDayCount(startDate, new Date()) * 8; // from first work item date till now
		Integer hoursFinish = itemService.getPccDeveloperWorkHoursAfter(selectedPccDeveloper, DateUtil.getNextDay(startDate)); // 第一次填的隔天起算，第一次填的時間可不計。
		Integer hourLeft = workHours - hoursFinish; // 所有工作天時數 - WI完成時數
		if (workHours == 0) {// 代表目前還沒填過WI
			hourLeft = 0;
		}

		model.addAttribute("selectedPccDeveloper", selectedPccDeveloper);
		model.addAttribute("pccDeveloperList", pccDeveloperList);
		model.addAttribute("categoryWODayOffList", categoryWODayOffList);
		model.addAttribute("categoryAllList", categoryAllList);
		model.addAttribute("categoryDetailWODayOffList", categoryDetailWODayOffList);
		model.addAttribute("categoryDetailAllList", categoryDetailAllList);
		model.addAttribute("itemList", itemListWithDayOff);
		model.addAttribute("lastDate", lastDate);
		model.addAttribute("hourLeft", hourLeft);
		model.addAttribute("copyString", sb.toString());
		return "item_list";
	}

	/* 工作類別清單 */
	@GetMapping("/categoryList")
	public String goCategoryList(@ModelAttribute Category category, @ModelAttribute CategoryDetail categoryDetail, Model model) {
		System.out.println("*****into categoryList*****");
		List<Category> categoryList = categoryService.findAllWithoutDayOff();
		List<CategoryDetail> categoryDetailList = categoryDetailService.findAllWithoutDayOff();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("categoryDetailList", categoryDetailList);
		return "category_list";
	}

	/* 使用者清單 */
	@GetMapping("/pccDeveloperList")
	public String goPccDeveloperList(@ModelAttribute PccDeveloper pccDeveloper, Model model) {
		System.out.println("*****into pccDeveloperList*****");
		List<PccDeveloper> pccDeveloperList = pccDeveloperService.findAll();
		model.addAttribute("pccDeveloperList", pccDeveloperList);
		return "pccDeveloper_list";
	}

	/* 新增work item */
	@PostMapping("/addWorkItems")
	public String addWorkItems(@Valid @ModelAttribute Item item, BindingResult bindingResult, Model model) {
		System.out.println("*****into addWorkItems*****");

		/* error message */
		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			for (FieldError f : bindingResult.getFieldErrors()) {
				errorMsg.add(f.getDefaultMessage());
			}
			model.addAttribute("errorMsg", errorMsg);
			return goIndex(item, model); // forward message to index
		}

		itemService.addOne(item);
		return "redirect:/";
	}

	/* 新增休假事項 */
	@PostMapping("/insertDayOff")
	public String InsertDayOff(@Valid @ModelAttribute Item item, BindingResult bindingResult, Model model) {
		System.out.println("*****into insertDayOff*****");
		/* evan@休假事項 - 休假事項 - 休假@8 */
		CategoryDetail dayOffCategoryDetail = categoryDetailService.findDayOff();
		Item insertItem = new Item();
		BeanUtils.copyProperties(item, insertItem);// insertItem for insert, item for return
		insertItem.setCategoryDetail(dayOffCategoryDetail); // 分類 設為"休假事項"
		insertItem.setContent("休假"); // 工作內容 設為"休假"

		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			for (FieldError f : bindingResult.getFieldErrors()) {
				if ("workTime".equals(f.getField())) { // show msg related to time
					errorMsg.add(f.getDefaultMessage());
					model.addAttribute("errorMsg", errorMsg);
					return goIndex(item, model);
				}
			}
		}

		itemService.addOne(insertItem);
		return "redirect:/";
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
			return goCategoryList(category, new CategoryDetail(), model);
		}
		categoryService.addOne(category);
		return "redirect:/categoryList";
	}

	/* 新增工作類別細項 */
	@GetMapping("/addCategoryDetail")
	public String addCategoryDetail(@Valid @ModelAttribute CategoryDetail categoryDetail, BindingResult bindingResult, Model model) {
		System.out.println("*****into addCategory*****");
		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			for (FieldError f : bindingResult.getFieldErrors()) {
				errorMsg.add(f.getDefaultMessage());
			}
			model.addAttribute("errorMsg", errorMsg);
			return goCategoryList(new Category(), categoryDetail, model);
		}
		categoryDetailService.addOne(categoryDetail);
		return "redirect:/categoryList";
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

	/* 更新工作項目 */
	@PostMapping("/updateItem")
	public String updateItem(@Valid Item item, BindingResult bindingResult, @RequestParam(value = "pkItem") Long pkItem, Model model) {
		System.out.println("*****into updateItem*****");
		System.out.println(pkItem);
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
			return goCategoryList(new Category(), new CategoryDetail(), model);
		}
		Category updateCategory = categoryService.getOne(pkCategory);
		BeanUtils.copyProperties(category, updateCategory);
		categoryService.update(updateCategory);
		return "redirect:/categoryList";
	}

	/* 更新工作類別細項 */
	@PostMapping("/updateCategoryDetail")
	public String updateCategoryDetail(@Valid CategoryDetail categoryDetail, BindingResult bindingResult, @RequestParam(value = "pkCategoryDetail") Long pkCategoryDetail, Model model) {
		System.out.println("*****into updateCategoryDetail*****");
		List<String> errorMsg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			errorMsg.add("更新時出現錯誤：");
			for (FieldError f : bindingResult.getFieldErrors()) {
				errorMsg.add(f.getDefaultMessage());
			}
			model.addAttribute("errorMsg", errorMsg);
			return goCategoryList(new Category(), new CategoryDetail(), model);
		}
		CategoryDetail updateCategoryDetail = categoryDetailService.getOne(pkCategoryDetail);
		BeanUtils.copyProperties(categoryDetail, updateCategoryDetail);
		categoryDetailService.update(updateCategoryDetail);
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
	public String deleteItem(@RequestParam(name = "pkItem") long pkItem, Model model) {
		itemService.deleteById(pkItem);
		return "redirect:/";
	}

	/* 刪除工作類別 */
	@PostMapping("/deleteCategory")
	public String deleteCategory(@ModelAttribute Category category, Model model) {
		categoryService.delete(category);
		return "redirect:/categoryList";
	}

	/* 刪除工作類別 */
	@PostMapping("/deleteCategoryDetail")
	public String deleteCategoryDetail(@RequestParam(name = "pkCategoryDetail") long pkCategoryDetail, Model model) {
		categoryDetailService.deleteById(pkCategoryDetail);
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
