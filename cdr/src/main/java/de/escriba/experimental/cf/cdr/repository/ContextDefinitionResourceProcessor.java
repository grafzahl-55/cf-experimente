package de.escriba.experimental.cf.cdr.repository;

import de.escriba.experimental.cf.cdr.controller.SourcesController;
import de.escriba.experimental.cf.cdr.model.ContextDefinitionEntity;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@Component
public class ContextDefinitionResourceProcessor implements ResourceProcessor<Resource<ContextDefinitionEntity>> {
    @Override
    public Resource<ContextDefinitionEntity> process(Resource<ContextDefinitionEntity> resource) {

        resource.getContent().getSources().forEach(src->{
            resource.add(linkTo(SourcesController.class).slash("sources")
                    .slash(resource.getContent().getId())
                    .slash(src.getRole())
                    .withRel("role-"+src.getRole()));
        });
        return resource;
    }
}
