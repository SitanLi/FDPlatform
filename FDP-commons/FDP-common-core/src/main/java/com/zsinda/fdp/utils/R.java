
package com.zsinda.fdp.utils;

import com.zsinda.fdp.constant.CommonConstants;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Optional;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 200成功
	 */
	@Getter
	@Setter
	private int code;

	@Getter
	@Setter
	private String msg;


	@Getter
	@Setter
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, CommonConstants.REQUEST_SUCCESS, null);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, CommonConstants.REQUEST_SUCCESS, null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, CommonConstants.REQUEST_SUCCESS, msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, CommonConstants.REQUEST_FAIL, null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, CommonConstants.REQUEST_FAIL, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, CommonConstants.REQUEST_FAIL, null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, CommonConstants.REQUEST_FAIL, msg);
	}

	public static boolean isSuccess(@Nullable R r) {
		return Optional.ofNullable(r)
				.map(x -> ObjectUtils.nullSafeEquals(CommonConstants.REQUEST_SUCCESS, x.code))
				.orElse(Boolean.FALSE);
	}

	public static boolean isNotSuccess(@Nullable R r) {
		return !isSuccess(r);
	}

	private static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}
}
