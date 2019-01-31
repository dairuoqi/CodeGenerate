package com.utils;

import com.constant.GenEnum;
import com.domain.Column;
import com.domain.Table;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.*;

/**
 * @author rock
 * @date 2019/01/31
 */
@Slf4j
public class GenUtils {


    private static List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add(GenEnum.DOMAIN.getFileName());
        templates.add(GenEnum.DAO.getFileName());
        templates.add(GenEnum.MAPPER.getFileName());
        templates.add(GenEnum.SERVICE.getFileName());
        templates.add(GenEnum.IMPL.getFileName());
        return templates;
    }


    /**
     * 在对应的模块生成代码
     *
     * @param tableName
     * @param columns
     */
    public static void generate(String tableName, List<Column> columns) {
        Configuration config = getConfig();
        disposeColumns(columns, config);
        String className = tableToJava(tableName);
        Table table = Table.builder()
                .tableName(tableName)
                .uppercaseClassName(className)
                .lowercaseClassName(StringUtils.uncapitalize(className))
                .columns(columns)
                .pk(columns.get(0))
                .build();

        VelocityContext context = getVelocityContext(table, config);
        List<String> templates = getTemplates();
        for (String template : templates) {
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            byte[] bytes = sw.toString().getBytes();
            String packageName = config.getString("package").substring(config.getString("package").lastIndexOf(".") + 1);
            createFolderAndFile(bytes, template, className, packageName);
        }
    }


    private static void disposeColumns(List<Column> columns, Configuration config) {
        for (Column column : columns) {
            String attrName = columnToJava(column.getColumnName());
            column.setUppercaseAttrName(attrName);
            column.setLowercaseAttrName(StringUtils.uncapitalize(attrName));
            String javaDataType = config.getString(column.getColumnDataType(), "unknownType");
            column.setJavaDataType(javaDataType);
        }
    }


    /**
     * 设置velocity资源加载器
     */
    private static void setProperties() {
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
    }

    /**
     * 设置velocity资源加载器 , 封装模板数据
     *
     * @param table
     * @param config
     * @return
     */
    private static VelocityContext getVelocityContext(Table table, Configuration config) {
        setProperties();
        Map<String, Object> velocityMap = new HashMap<>(16);
        velocityMap.put("tableName", table.getTableName());
        velocityMap.put("pk", table.getPk());
        velocityMap.put("className", table.getUppercaseClassName());
        velocityMap.put("classname", table.getLowercaseClassName());
        velocityMap.put("pathName", config.getString("package").substring(config.getString("package").lastIndexOf(".") + 1));
        velocityMap.put("package", config.getString("package"));
        velocityMap.put("columns", table.getColumns());
        velocityMap.put("datetime", DateUtils.formatDateTime(new Date()));
        return new VelocityContext(velocityMap);
    }


    /**
     * 列名转换成Java属性名
     */
    private static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    private static String tableToJava(String tableName) {
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     * TODO yml形式
     */
    private static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("获取配置文件失败，", e);
        }
    }


    /**
     * 创建文件夹和文件
     *
     * @param bytes
     * @param template
     * @param className
     * @param packageName
     */
    private static void createFolderAndFile(byte[] bytes, String template, String className, String packageName) {
        String basePath = "src" + File.separator + "main" + File.separator;
        String packagePath = basePath + "java" + File.separator;
        String folderName, fileName;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("Domain.java.vm")) {
            folderName = packagePath + "pojo" + File.separator;
            fileName = folderName + className + "BO.java";
        } else if (template.contains("Dao.java.vm")) {
            folderName = packagePath + "dao" + File.separator;
            fileName = folderName + className + "Mapper.java";
        } else if (template.contains("Service.java.vm")) {
            folderName = packagePath + "service" + File.separator;
            fileName = folderName + className + "Service.java";
        } else if (template.contains("ServiceImpl.java.vm")) {
            folderName = packagePath + "service" + File.separator;
            fileName = folderName + className + "ServiceImpl.java";
        } else if (template.contains("Mapper.xml.vm")) {
            folderName = basePath + "resources" + File.separator + "mapper" + File.separator;
            fileName = folderName + className + "Mapper.xml";
        } else {
            return;
        }
        createFolderIfNotExists(folderName);
        newFile(bytes, fileName);

    }


    /**
     * 创建文件夹
     *
     * @param folderName
     */
    private static void createFolderIfNotExists(String folderName) {
        File file = new File(folderName);
        if (!file.exists()) {
            boolean result = file.mkdirs();
            log.info("create folder name:{} is {}", folderName, result);
        }
    }

    /**
     * 创建文件
     *
     * @param bytes
     * @param fileName
     */
    private static void newFile(byte[] bytes, String fileName) {
        try {
            System.out.println(System.getProperty("java.class.path"));
            File newFile = new File(fileName);
            FileOutputStream fop = new FileOutputStream(newFile);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            fop.write(bytes);
            fop.flush();
            fop.close();
            log.info("fileName: {}, created success", fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
