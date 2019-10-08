package com.dms;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;

@Configuration
public class RespositoryConfiguration extends RepositoryRestMvcConfiguration {

	public RespositoryConfiguration(ApplicationContext context, ObjectFactory<ConversionService> conversionService) {
		super(context, conversionService);
	}

	@Override
	@Bean
	public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
		HateoasPageableHandlerMethodArgumentResolver resolver = super.pageableResolver();
		resolver.setOneIndexedParameters(true);
		return resolver;
	}

}