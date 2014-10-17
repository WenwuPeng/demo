package com.demo.chaos;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.reader.ClassReaders;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.config.FilterBuilder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import scala.Option;

public class ChaosService extends Service<ChaosConfiguration> {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0) {
            args = new String[2];
            args[1] = "server";
            args[2] = "demo.yml";
        }
        new ChaosService().run(args);
    }

    @Override
    public void initialize(Bootstrap<ChaosConfiguration> bootstrap) {
        bootstrap.setName("helloWorld");
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }
    @Override
    public void run(ChaosConfiguration helloWorldConfiguration, Environment environment) throws Exception {
        initSwaggerConfig(environment);
        final String template = helloWorldConfiguration.getTemplate();
        final String defaultName = helloWorldConfiguration.getDefaultName();
        environment.addResource(new ChaosResource(template, defaultName));
        environment.addHealthCheck(new TemplateHealthCheck(template));

    }

    private void initSwaggerConfig(Environment environment) {
        // Swagger Resource
        environment.addResource(new ApiListingResourceJSON());

        // Swagger providers
        environment.addProvider(new ApiDeclarationProvider());
        environment.addProvider(new ResourceListingProvider());
        FilterBuilder filterConfig = environment.addFilter(CrossOriginFilter.class, "/*");
        filterConfig.setInitParam(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");

        // Swagger Scanner, which finds all the resources for @Api Annotations
        ScannerFactory.setScanner(new DefaultJaxrsScanner());
        // Add the reader, which scans the resources and extracts the resource information
        ClassReaders.setReader(new DefaultJaxrsApiReader());
        SwaggerConfig config = ConfigFactory.config();
        config.setApiVersion("0.1");
        config.setApiPath("/api/api-docs");
        config.setBasePath("http://localhost:9090/api");
        config.setInfo(Option.apply(new ApiInfo("Chaos Swagger UI",
                "This is just a demo to show how to integrate Swagger UI with a dropwizard project.",
                null,
                "test",
                null,
                null)));
    }
}