open module io.micronaut.inject {
	requires transitive io.micronaut.core;
	requires transitive org.slf4j;
	requires transitive jakarta.annotation;
	requires transitive jakarta.inject;

	exports io.micronaut.context;
	exports io.micronaut.context.annotation;
	exports io.micronaut.context.banner;
	exports io.micronaut.context.condition;
	exports io.micronaut.context.converters;
	exports io.micronaut.context.env;
	exports io.micronaut.context.env.yaml;
	exports io.micronaut.context.event;
	exports io.micronaut.context.exceptions;
	exports io.micronaut.context.i18n;
	exports io.micronaut.context.processor;
	exports io.micronaut.context.scope;
	exports io.micronaut.context.visitor;
	exports io.micronaut.inject;
	exports io.micronaut.inject.annotation;
	exports io.micronaut.inject.annotation.internal;
	exports io.micronaut.inject.ast;
	exports io.micronaut.inject.ast.beans;
	exports io.micronaut.inject.beans;
	exports io.micronaut.inject.beans.visitor;
	exports io.micronaut.inject.configuration;
	exports io.micronaut.inject.processing;
	exports io.micronaut.inject.provider;
	exports io.micronaut.inject.qualifiers;
	exports io.micronaut.inject.util;
	exports io.micronaut.inject.validation;
	exports io.micronaut.inject.visitor;
	exports io.micronaut.inject.writer;

	uses io.micronaut.context.env.PropertySourceLoader;
	provides io.micronaut.context.env.PropertySourceLoader with
			io.micronaut.context.env.yaml.YamlPropertySourceLoader,
			io.micronaut.context.env.PropertiesPropertySourceLoader;

	uses io.micronaut.core.type.TypeInformationProvider;
	provides io.micronaut.core.type.TypeInformationProvider with
			io.micronaut.inject.provider.ProviderTypeInformationProvider;

	uses io.micronaut.inject.annotation.AnnotationMapper;
	provides io.micronaut.inject.annotation.AnnotationMapper with
			io.micronaut.inject.annotation.internal.PersistenceContextAnnotationMapper,
			io.micronaut.inject.annotation.internal.TimedAnnotationMapper,
			io.micronaut.inject.beans.visitor.EntityIntrospectedAnnotationMapper,
			io.micronaut.inject.beans.visitor.EntityReflectiveAccessAnnotationMapper,
			io.micronaut.inject.beans.visitor.MappedSuperClassIntrospectionMapper,
			io.micronaut.inject.beans.visitor.JsonCreatorAnnotationMapper;

	uses io.micronaut.inject.annotation.AnnotationRemapper;
	provides io.micronaut.inject.annotation.AnnotationRemapper with
			io.micronaut.inject.annotation.internal.FindBugsRemapper,
			io.micronaut.inject.annotation.internal.JakartaRemapper;

	uses io.micronaut.inject.annotation.AnnotationTransformer;
	provides io.micronaut.inject.annotation.AnnotationTransformer with
			io.micronaut.inject.annotation.internal.CoreNullableTransformer,
			io.micronaut.inject.annotation.internal.CoreNonNullTransformer,
			io.micronaut.inject.annotation.internal.KotlinNullableMapper,
			io.micronaut.inject.annotation.internal.KotlinNotNullMapper,
			io.micronaut.inject.annotation.internal.JakartaPostConstructTransformer,
			io.micronaut.inject.annotation.internal.JakartaPreDestroyTransformer;

	uses io.micronaut.inject.BeanDefinitionReference;
	provides io.micronaut.inject.BeanDefinitionReference with
			io.micronaut.inject.provider.JavaxProviderBeanDefinition,
			io.micronaut.inject.provider.BeanProviderDefinition,
			io.micronaut.inject.provider.JakartaProviderBeanDefinition,
			io.micronaut.context.event.ApplicationEventPublisherFactory;

	uses io.micronaut.inject.configuration.ConfigurationMetadataWriter;
	provides io.micronaut.inject.configuration.ConfigurationMetadataWriter with
			io.micronaut.inject.configuration.JsonConfigurationMetadataWriter;

	uses io.micronaut.inject.visitor.TypeElementVisitor;
	provides io.micronaut.inject.visitor.TypeElementVisitor with
			io.micronaut.inject.beans.visitor.IntrospectedTypeElementVisitor,
			io.micronaut.context.visitor.BeanImportVisitor,
			io.micronaut.context.visitor.ContextConfigurerVisitor;

	uses io.micronaut.context.ApplicationContextConfigurer;
}