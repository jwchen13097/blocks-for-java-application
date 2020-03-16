package org.firefly.provider.swaggerui.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户响应", description = "用户响应")
public class PersonDTO {
    @ApiModelProperty(name = "name", value = "姓名", example = "张山", dataType = "String")
    private String name;
    @ApiModelProperty(name = "age", value = "年龄", example = "25", dataType = "int")
    private int age;

    public PersonDTO(String name, int age) {
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
