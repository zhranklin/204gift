package io.zhranklin.gift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 张武(zhangwu@corp.netease.com) at 2020/2/14
 */
@Controller
@RequestMapping("/gift")
public class GiftController {

	@Autowired
	UserService userService;

	@RequestMapping("")
	public String home() {
		return "home";
	}

	@RequestMapping("/new")
	public String New() {
		return "address";
	}

	@RequestMapping("/view")
	public String view(String phone, String password, ModelMap mm) {
		Map<String, String> user = userService.getUserByPhone(phone);
		if (user == null || !validate(password, user)) {
			mm.put("message", "密码错误");
			return "error";
		}
		user.put("password", password);
		processMM(user, mm);
		return "address";
	}

	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(@RequestParam Map<String, String> info, ModelMap mm, String phone, String oldPass) {
		Map<String, String> oldUser = userService.getUserByPhone(phone);
		if (oldUser != null && !validate(oldPass, oldUser)) {
			mm.put("message", "???你踏马在干啥???");
			return "error";
		}
		if (oldUser != null && oldUser.containsKey("number")) {
			info.put("number", oldUser.get("number"));
		}
		userService.updateUser(phone, info);
		processMM(info, mm);
		return "address";
	}

	@RequestMapping(value = "/shuffle", method = RequestMethod.GET)
	public String shufflePage(ModelMap mm) {
		mm.put("users", userService.getAll());
		return "shuffle";
	}
	@RequestMapping(value = "/shuffle", method = RequestMethod.POST)
	public String shuffle(@RequestParam Map<String, String> params) {
		List<String> phones = params.entrySet().stream()
			.filter(e -> e.getValue().equals("$$phone"))
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
		userService.shuffle(phones.size());
		List<Integer> ints = new ArrayList<>(userService.shuffleNewInts(phones.size()));
		phones.forEach(phone -> {
			Map<String, String> user = userService.getUserByPhone(phone);
			user.put("number", ""+ints.remove(0));
		});
		return "home";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(ModelMap mm, String phone, String oldPass) {
		Map<String, String> oldUser = userService.getUserByPhone(phone);
		if (oldUser == null || !validate(oldPass, oldUser)) {
			mm.put("message", "???你踏马在干啥???");
			return "error";
		}
		userService.deleteUser(phone);
		mm.put("message", "删除成功");
		return "error";
	}

	@RequestMapping(value = "/export", method = RequestMethod.GET)
	@ResponseBody
	public Object export() {
		ArrayList<Object> result = new ArrayList<>();
		userService.getAll().forEach(u -> {
			HashMap<String, String> copied = new HashMap<>(u);
			copied.remove("password");
			copied.remove("oldPass");
			copied.remove("number");
			result.add(copied);
		});
		return result;
	}

	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public Object Import(@RequestBody List<Map<String, String>> data) {
		userService.importData(data);
		return "home";
	}

	private boolean validate(String oldPass, Map<String, String> user) {
		return user.get("md5").equals(userService.md5(oldPass));
	}

	private void processMM(@RequestParam Map<String, String> info, ModelMap mm) {
		String targetNumber = userService.getTargetNumberByPhone(info.get("phone"));
		mm.put("tn", targetNumber);
		Map<String, String> target = userService.getUserByNumber(targetNumber);
		if (target != null) {
			mm.put("t", target);
		}
		mm.put("mappings", userService.getMappingStr());
		mm.putAll(info);
	}

}
