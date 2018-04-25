package de.escriba.experimental.cf.sbs;

import de.escriba.experimental.cf.sbs.model.FileInfo;
import de.escriba.experimental.cf.sbs.model.FolderInfo;
import de.escriba.experimental.cf.sbs.model.TagValue;
import de.escriba.experimental.cf.sbs.repository.FolderInfoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import de.escriba.experimental.cf.sbs.repository.FileInfoRepository;
import de.escriba.experimental.cf.sbs.repository.TagRepository;
import de.escriba.experimental.cf.sbs.repository.TagService;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackageClasses = SbsApplication.class)
public class SbsApplication {

        @Autowired
        private FolderInfoRepository fldRepo;
        @Autowired
        private FileInfoRepository fileRepo;
        @Autowired
        private TagService tagService;
        
    
    
	public static void main(String[] args) {
		SpringApplication.run(SbsApplication.class, args);
	}
        
        
        @Bean
        public CommandLineRunner testDataCreator(){
            return arg->{
                FolderInfo folder1=new FolderInfo();
                folder1.setName("Folder1");
                fldRepo.save(folder1);
                
                for(int i=1; i<=5; i++){
                    FileInfo fi=new FileInfo();
                    fi.setName("File#"+i);
                    fi.setFolder(folder1);
                    fileRepo.save(fi);
                    
                    fi.tag("Serial", "No-"+i);
                    tagService.saveTags(fi);
                    
                }
                
                
            
            };
        }
        
}
