package org.firefly.provider.swaggerui.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户创建请求", description = "用户创建请求参数")
public class PersonCreateRequest {
    @ApiModelProperty(
            name = "name", value = "姓名", notes = "用户姓名", required = true, example = "张山", dataType = "String")
    private String name;
    @ApiModelProperty(name = "age", value = "年龄", notes = "用户年龄", example = "25", dataType = "int")
    private int age;

    public PersonCreateRequest(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
