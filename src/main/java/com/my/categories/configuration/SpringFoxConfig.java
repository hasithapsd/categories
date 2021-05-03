package com.my.categories.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static com.my.categories.filter.CorrelationFilter.REQUEST_CORRELATION_ID;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Items by categories")
                .description("REST servce for create categories and items")
                .license("")
                .licenseUrl("http://unlicense.org")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .contact(new Contact("","", ""))
                .build();
    }

    @Bean
    public Docket api() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder
                .name(REQUEST_CORRELATION_ID)
                .modelRef(
                        new ModelRef("string")
                )
                .parameterType("header")
                .required(false)
                .description("Coffee shop Correlation ID")
                .build();

        List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(aParameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.my.categories"))
                .build()
                .apiInfo(apiInfo())
                .globalOperationParameters(aParameters);
    }
}