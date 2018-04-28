package de.escriba.experimental.cf.cdr.controller;

import de.escriba.experimental.cf.cdr.model.ContextDefinitionEntity;
import de.escriba.experimental.cf.cdr.model.ContextSourceEntity;
import de.escriba.experimental.cf.cdr.repository.ContextDefinitionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController()
public class SourcesController {

    private final ContextDefinitionRepository repository;

    public SourcesController(ContextDefinitionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/sources/{id}/{role}")
    @Transactional
    public ResponseEntity<byte[]> downloadSource(@PathVariable Long id, @PathVariable String role) {
        Optional<ContextDefinitionEntity> contextEntityOpt = repository.findById(id);
        if (contextEntityOpt.isPresent()) {
            ContextSourceEntity cse = contextEntityOpt.get().getSourceByRole(role);
            if (cse != null) {
                HttpHeaders hd = new HttpHeaders();
                hd.setContentType(MediaType.parseMediaType(cse.getContentType()));
                hd.setContentLength(cse.getSource().length);
                ResponseEntity<byte[]> resp = new ResponseEntity<>(cse.getSource(), hd, HttpStatus.OK);
                return resp;
            }
        }
        return new ResponseEntity<byte[]>(new byte[0], HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/sources/{id}/{role}",consumes = MediaType.ALL_VALUE)
    @Transactional
    public ResponseEntity<String> uploadSource(@PathVariable Long id,
                                               @PathVariable String role, @RequestBody byte[] data,
                                               @RequestHeader("Content-Type") String contentType) {
        Optional<ContextDefinitionEntity> contextEntityOpt = repository.findById(id);
        if (contextEntityOpt.isPresent()) {
            ContextDefinitionEntity cDef = contextEntityOpt.get();
            ContextSourceEntity cse = cDef.getSourceByRole(role);
            HttpStatus status;
            if (cse == null) {
                cse = new ContextSourceEntity().role(role);
                contextEntityOpt.get().addSources(cse);
                status = HttpStatus.CREATED;
            } else {
                status = HttpStatus.ACCEPTED;
            }
            cse.setContentType(contentType);
            cse.setSource(data);
            repository.save(cDef);
            return new ResponseEntity<>("", status);
        }
        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value="/sources/{id}/{role}")
    @Transactional
    public ResponseEntity<String> deleteSource(@PathVariable Long id,
                                               @PathVariable String role) {
        HttpStatus status;
        Optional<ContextDefinitionEntity> contextEntityOpt = repository.findById(id);
        if (contextEntityOpt.isPresent()) {
            ContextDefinitionEntity cDef = contextEntityOpt.get();
            ContextSourceEntity cse = cDef.getSourceByRole(role);
            if (cse == null) {
                status = HttpStatus.NOT_FOUND;
            } else {
                cDef.getSources().remove(cse);
                repository.save(cDef);
                status = HttpStatus.ACCEPTED;
            }

        } else {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>("", status);
    }
}
