package org.firefly.provider.swaggerui.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.firefly.provider.swaggerui.domain.dto.PersonCreateRequest;
import org.firefly.provider.swaggerui.domain.dto.PersonDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/person")
// @Api注解的类是Swagger资源。
@Api(
        value = "全局用户管理",
        tags = {"public", "person"},
        produces = "xxx_produces",
        consumes = "xxx_consumes",
        protocols = "xxx_protocols",
        authorizations = {@Authorization(value = "")}
)
@ApiResponses({
        @ApiResponse(code = 200, message = "成功"),
        @ApiResponse(code = 401, message = "未授权"),
        @ApiResponse(code = 404, message = "未发现"),
        @ApiResponse(code = 500, message = "内部错误")
})
public class PersonController {
    @PostMapping("/create")
    // @ApiOperation注解的方法是一个HTTP请求的操作。
    @ApiOperation(
            // 方法的描述
            value = "创建用户",
            // 提示内容
            notes = "根据请求创建用户",
            // 重新分组
            tags = {"public", "person"},
            response = PersonDTO.class,
            responseContainer = "List",
            responseReference = "responseReference",
            httpMethod = "POST",
            nickname = "nickname",
            produces = "xxx_produces",
            consumes = "xxx_consumes",
            protocols = "xxx_protocols",
            authorizations = {@Authorization(value = "")})
    public PersonDTO create(@RequestBody
                            // @ApiParam注解的参数，可以指定约束，如是否必填等。
                            // 如果要能接受null，可以使用@ApiImplicitParams({@ApiImplicitParam...
                            @ApiParam(
                                    // 参数名
                                    name = "personCreateRequest",
                                    // 参数的描述
                                    value = "创建人员参数",
                                    defaultValue = "xxx_defaultValue",
                                    required = true,
                                    example = "{}")
                                    PersonCreateRequest personCreateRequest) {
        return new PersonDTO(personCreateRequest.getName(), personCreateRequest.getAge());
    }
}
