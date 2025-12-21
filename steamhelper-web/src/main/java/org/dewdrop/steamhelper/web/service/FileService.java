package org.dewdrop.steamhelper.web.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.util.util.DateUtil;
import org.dewdrop.steamhelper.util.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
	
	@Value("${imagePath.local:}")
	private String localPath;
	@Value("${imagePath.server:}")
	private String serverPath;
	
	public static final long M_1 = 1 << 20;
	
	public IReturnBean<String> saveFile(String sub, MultipartFile file) throws IOException {
		if (file.getSize() > M_1) {
			return IReturnBean.fail("本地存储失败，图片不能超过1M");
		}
		String originalFilename = file.getOriginalFilename();
		if (StringUtil.isBlank(originalFilename)) {
			return IReturnBean.fail("本地存储失败，未获取图片名称");
		}
		String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
		String filename = StringUtil.randomString(6) + System.currentTimeMillis() + suffix;
		String local1 = sub + "/" + DateUtil.formatDate(new Date(), DateUtil.YYYY_MM) + "/" + DateUtil.formatDate(new Date(), DateUtil.DD);
		String dirPath = localPath + local1;
		File dir = new File(dirPath);
		if (!dir.exists() || !dir.isDirectory()) {
			boolean mkdir = dir.mkdirs();
			if (!mkdir) {
				return IReturnBean.fail("本地存储失败，未能创建目录");
			}
		}
		file.transferTo(Paths.get(dirPath + "/" + filename));
		return IReturnBean.success(local1 + "/" + filename);
	}
	
}
