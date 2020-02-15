package io.zhranklin.gift.controller;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by 张武(zhangwu@corp.netease.com) at 2020/2/14
 */
@Component
public class UserService {
	private Map<String, Map<String, String>> db = new HashMap<>();
	private Map<String, String> mapping = new HashMap<>();
	private Random rand = new Random();

	private List<Integer> ints = new ArrayList<Integer>();

	@PostConstruct
	public void init() {
//		mapping = shuffle(total);
//		ints = shuffleNewInts();
	}

	public void shuffle(int total) {
		while (true) {
			List<Integer> numbers = IntStream.range(1, total + 1)
				.boxed()
				.collect(Collectors.toList());
			HashMap<String, String> result = new HashMap<>();
			IntStream.range(1, total)
				.forEach(i -> {
					while(true) {
						int idx = rand.nextInt(numbers.size());
						if (numbers.get(idx) != i) {
							result.put(""+i, ""+numbers.get(idx));
							numbers.remove(idx);
							break;
						}
					}
				});
			if (numbers.get(0) != total) {
				result.put("" + total, "" + numbers.get(0));
				mapping = result;
				return;
			}
		}
	}

	void deleteUser(String phone) {
		db.remove(phone);
		
	}

	public void updateUser(String phone, @RequestParam Map<String, String> user) {
		user.put("md5", md5(user.get("password")));
		db.put(phone, user);
	}

	public String md5(String string) {
		return DigestUtils.md5DigestAsHex(string.getBytes());
	}

	String getTargetNumberByPhone(String phone) {
		Map<String, String> user = getUserByPhone(phone);
		return mapping.get(user.get("number"));
	}

	Map<String, String> getUserByNumber(String number) {
		Optional<Map<String, String>> opt = db.values().stream().filter(d -> Objects.equals(d.get("number"), number)).findAny();
		return opt.orElse(null);
	}

	public Map<String, String> getUserByPhone(String phone) {
		return db.get(phone);
	}

	public List<String> getMappingStr()  {
		return mapping.entrySet().stream().map(k -> ""+k.getKey()+"→"+k.getValue()).collect(Collectors.toList());
	}

	public List<Integer> shuffleNewInts(int total)  {
		List<Integer> numbers = IntStream.range(1, total + 1)
			.boxed()
			.collect(Collectors.toList());
		return IntStream.range(1, total + 1)
			.mapToObj(i -> numbers.remove(rand.nextInt(numbers.size())))
			.collect(Collectors.toList());
	}

	public List<Map<String, String>> getAll() {
		return new ArrayList<>(db.values());
	}

	public void importData(List<Map<String, String>> data) {
		db.clear();
		mapping.clear();
		data.forEach(d -> {
			String phone = d.get("phone");
			if (!d.containsKey("md5")) {
				d.putIfAbsent("password", phone.substring(phone.length()-4));
				d.put("md5", md5(d.get("password")));
			}
			db.put(phone, d);
		});
	}
}
