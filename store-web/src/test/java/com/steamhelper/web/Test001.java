package com.steamhelper.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test001 {
	
	private static final String ITEM_CATEGORY_PATH = "./store-web/src/test/resources/商品分类/";
	private static final String ITEM_CATEGORY_JSON = "商品分类json.txt";
	private static final String ITEM_CATEGORY_SQL = "商品分类sql.txt";
	
	
	private static void parseJson(String str, FileWriter writer) throws IOException {
		JSONObject obj = JSONObject.parseObject(str);
		writer.write("INSERT INTO `store`.`sys_item_category` (`id`, `pid`, `category_name`) VALUES ("
				+ obj.getIntValue("value") + ", 0, '"
				+ obj.getString("label") + "');\n");
		JSONArray lv2 = obj.getJSONArray("children");
		for (int i = 0; i < lv2.size(); i++) {
			JSONObject lv2Obj = lv2.getJSONObject(i);
			writer.write("INSERT INTO `store`.`sys_item_category` (`id`, `pid`, `category_name`) VALUES ("
					+ lv2Obj.getIntValue("value") + ", "+
					+lv2Obj.getInteger("pid") + ", '"
					+ lv2Obj.getString("label") + "');\n");
			JSONArray lv3 = lv2Obj.getJSONArray("children");
			for (int j = 0; j < lv3.size(); j++) {
				JSONObject lv3Obj = lv3.getJSONObject(j);
				writer.write("INSERT INTO `store`.`sys_item_category` (`id`, `pid`, `category_name`) VALUES ("
						+ lv3Obj.getIntValue("value") + ", "+
						+lv3Obj.getInteger("pid") + ", '"
						+ lv3Obj.getString("label") + "');\n");
			}
		}
		writer.write("\n");
	}
	
	private static void parseJsonFile() {
		try (FileWriter writer = new FileWriter(ITEM_CATEGORY_PATH + ITEM_CATEGORY_SQL)) {
			try (BufferedReader br = new BufferedReader(new FileReader(ITEM_CATEGORY_PATH + ITEM_CATEGORY_JSON))) {
				String line;
				while ((line = br.readLine()) != null) {
					parseJson(line, writer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	public static void main(String[] args) {
		// 商品分类
		parseJsonFile();
		//readFile();
	}
	
	

	private static void readFile() {
		try (BufferedReader br = new BufferedReader(new FileReader(ITEM_CATEGORY_PATH + "商品分类json.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
