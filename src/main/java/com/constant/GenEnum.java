package com.constant;


/**
 * GenEnum
 * 常量枚举类
 *
 * @author rock
 * @date 2019/01/30
 */
public enum GenEnum {

    /**
     * POJO
     */
    DOMAIN("template/Domain.java.vm"),

    /**
     * DAO
     */
    DAO("template/Dao.java.vm"),

    SERVICE("template/Service.java.vm"),


    IMPL("template/ServiceImpl.java.vm"),


    MAPPER("template/Mapper.xml.vm");

    private String fileName;

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    GenEnum(String fileName) {
        this.fileName = fileName;
    }

}
