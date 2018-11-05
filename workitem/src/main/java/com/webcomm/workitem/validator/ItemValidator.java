package com.webcomm.workitem.validator;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ItemValidator {

	@NotNull(message = "reason信息不可以为空")
	@Pattern(regexp = "[1-7]{1}", message = "reason的类型值为1-7中的一个类型")
	private String reason;// 订单取消原因

	// get、set方法、有参构造方法、无参构造方法、toString方法省略

	/**
	 * 验证参数：就是验证上述注解的完整方法
	 * 
	 * @return
	 */
	public void validateParams() {
		// 调用JSR303验证工具，校验参数
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<ItemValidator>> violations = validator.validate(this);
		Iterator<ConstraintViolation<ItemValidator>> iter = violations.iterator();
		if (iter.hasNext()) {
			String errMessage = iter.next().getMessage();
			throw new ValidationException(errMessage);
		}
	}
}
