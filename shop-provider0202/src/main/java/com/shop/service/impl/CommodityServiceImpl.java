package com.shop.service.impl;

import com.shop.bean.Commodity;
import com.shop.bean.CommodityExample;
import com.shop.bean.CommodityImage;
import com.shop.bean.ResponseBody;
import com.shop.dao.CommodityDAO;
import com.shop.dao.CommodityImageDAO;
import com.shop.enums.CommodityOrder;
import com.shop.exception.BasicException;
import com.shop.service.CommodityService;
import com.shop.util.CommonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommodityServiceImpl implements CommodityService {
    private final CommodityDAO dao;
    private final CommodityImageDAO imageDAO;
    @Value("${custom.upload-folder}")
    private String FILE_FOLDER;

    public CommodityServiceImpl(CommodityDAO dao, CommodityImageDAO imageDAO) {
        this.dao = dao;
        this.imageDAO = imageDAO;
    }

    private String getOrderString(Integer order) {
        String orderBy = null;
        if (CommodityOrder.SALES.isEquals(order))
            orderBy = "sales desc";
        else if (CommodityOrder.NEWER.isEquals(order))
            orderBy = "create_time desc";
        else if (CommodityOrder.PRICEASC.isEquals(order))
            orderBy = "price asc";
        else if (CommodityOrder.PRICEDESC.isEquals(order))
            orderBy = "price desc";
        return orderBy;
    }

    @Override
//    @Cacheable(value="commodity", key = "#root.methodName+#root.args")
    public ResponseBody<List<Commodity>> getCommodityList(Integer pageIndex, Integer pageSize, Integer order) {
        CommodityExample commodityExample = new CommodityExample();
        commodityExample.setOffset((long)(pageIndex - 1) * pageSize);
        commodityExample.setLimit(pageSize);
        commodityExample.setOrderByClause(getOrderString(order));
        long total = dao.countByExample(commodityExample);
        List<Commodity> commodities = dao.selectByExample(commodityExample);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", commodities);
        return ResponseBody.success(map);
    }

    @Override
//    @Cacheable(value="commodity", key = "#root.methodName+#root.args")
    public ResponseBody<List<Commodity>> getCommodityListByTypeId(Integer typeId, Integer pageIndex, Integer pageSize, Integer order) {
        CommodityExample commodityExample = new CommodityExample();
        commodityExample.setOffset((long)(pageIndex - 1) * pageSize);
        commodityExample.setLimit(pageSize);
        CommodityExample.Criteria criteria = commodityExample.createCriteria();
        criteria.andTypeIdEqualTo(typeId);
        commodityExample.setOrderByClause(getOrderString(order));
        long total = dao.countByExample(commodityExample);
        List<Commodity> commodities = dao.selectByExample(commodityExample);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", commodities);
        return ResponseBody.success(map);
    }

    @Override
//    @Cacheable(value="commodity", key = "#root.methodName+#root.args")
    public ResponseBody<List<Commodity>> getCommodityListByName(String name, Integer pageIndex, Integer pageSize, Integer order) {
        CommodityExample commodityExample = new CommodityExample();
        commodityExample.setOffset((long)(pageIndex - 1) * pageSize);
        commodityExample.setLimit(pageSize);
        CommodityExample.Criteria criteria = commodityExample.createCriteria();
        for (int index = 0; index < name.length(); index++)
            criteria.andNameLike("%" + name.substring(index, index + 1) + "%");
        commodityExample.setOrderByClause(getOrderString(order));
        long total = dao.countByExample(commodityExample);
        List<Commodity> commodities = dao.selectByExample(commodityExample);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", commodities);
        return ResponseBody.success(map);
    }

    @Override
//    @Cacheable(value="commodity", key = "#root.methodName+#root.args")
    public ResponseBody<Commodity> getCommodityById(Integer commodityId) {
        return ResponseBody.success(dao.selectByPrimaryKey(commodityId));
    }

    @Override
//    @Cacheable(value="commodity", key = "#root.methodName+#root.args")
    public ResponseBody<List<Commodity>> getCommoditiesByIdList(List<Integer> commodityIdList) {
        CommodityExample commodityExample = new CommodityExample();
        CommodityExample.Criteria criteria = commodityExample.createCriteria();
        criteria.andIdIn(commodityIdList);
        return ResponseBody.success(dao.selectByExample(commodityExample));
    }

    // 库存和销量
    @Override
    public ResponseBody<Integer> updateList(List<Commodity> commodityList) {
        return ResponseBody.success(dao.updateList(commodityList));
    }

    @Override
    public ResponseBody addCommodity(Commodity commodity) {
        return ResponseBody.success(dao.insert(commodity));
    }

    @Override
    public ResponseBody updateCommodity(Commodity commodity) {
        return ResponseBody.success(dao.updateByPrimaryKeySelective(commodity));
    }

    @Override
    public ResponseBody delCommodity(Integer id) {
        return ResponseBody.success(dao.deleteByPrimaryKey(id));
    }

    @Override
    public ResponseBody uploadImage(MultipartFile srcFile, Integer commodityId) {
        File path = new File(FILE_FOLDER);
        if (!path.exists())
            path.mkdirs();
        String originalFilename = srcFile.getOriginalFilename();
        String fileName = CommonUtil.generateFileName(originalFilename);
        String filePath = FILE_FOLDER + fileName;
        File img = new File(filePath);
        try {
            srcFile.transferTo(img);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BasicException("上传文件失败");
        }
        CommodityImage image = new CommodityImage();
        image.setImgName(fileName);
        image.setCommodityId(commodityId);
        return ResponseBody.success(imageDAO.insert(image));
    }

    @Override
    public ResponseBody addImage(CommodityImage image) {
        return ResponseBody.success(imageDAO.insert(image));
    }

    @Override
    public ResponseBody delImage(CommodityImage image) {
        int i = imageDAO.deleteByPrimaryKey(image.getImgId());
        String filePath = FILE_FOLDER + image.getImgName();
        File img = new File(filePath);
        if (img.isFile())
            img.delete();
        return ResponseBody.success(i);
    }
}
