package de.escriba.experimental.cf.cdr.repository;

import de.escriba.experimental.cf.cdr.controller.SourcesController;
import de.escriba.experimental.cf.cdr.model.ContextDefinitionEntity;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@Component
public class ContextDefinitionResourceProcessor implements ResourceProcessor<Resource<ContextDefinitionEntity>> {


    private final Method downloadMethod;

    public ContextDefinitionResourceProcessor() {
        try{
            downloadMethod =SourcesController.class.getMethod("downloadSource",Long.class, String.class);
        } catch(NoSuchMethodException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource<ContextDefinitionEntity> process(Resource<ContextDefinitionEntity> resource) {

        resource.getContent().getSources().forEach(src-> resource.add(linkTo(downloadMethod,resource.getContent().getId(),src.getRole())
                .withRel("role-"+src.getRole())));
        return resource;
    }
}
