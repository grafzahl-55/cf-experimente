package de.escriba.experimental.cf.sbs;

import de.escriba.experimental.cf.sbs.model.FileInfo;
import de.escriba.experimental.cf.sbs.model.FolderInfo;
import de.escriba.experimental.cf.sbs.repository.FileInfoRepository;
import de.escriba.experimental.cf.sbs.repository.FolderInfoRepository;
import de.escriba.experimental.cf.sbs.repository.TagRepository;
import de.escriba.experimental.cf.sbs.repository.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SbsApplicationTests {

    @Autowired
    private FolderInfoRepository fldRepo;
    @Autowired
    private FileInfoRepository fileRepo;
    @Autowired
    private TagService tagService;
    @Autowired
    private TagRepository tagRepo;
    

    @Test
    public void removeFoldeAndExpectThatFilesAnTagsAreDeleted(){
        fileRepo.deleteAll();
        tagRepo.deleteAll();
        fldRepo.deleteAll();
        
        FolderInfo folder=new FolderInfo();
        folder.setName("My Testfolder");
        fldRepo.save(folder);
        
        FileInfo file1=new FileInfo();
        file1.setName("File 1");
        file1.setFolder(folder);
        fileRepo.save(file1);
        file1.tag("serial", "1").tag("foo", "bar");
        tagService.saveTags(file1);
        
        FileInfo file2=new FileInfo();
        file2.setName("File 2");
        file2.setFolder(folder);
        fileRepo.save(file2);
        file2.tag("serial", "2");
        tagService.saveTags(file2);
        
        assertThat(tagRepo.count()).isEqualTo(3);
        assertThat(fileRepo.count()).isEqualTo(2);
        assertThat(fldRepo.count()).isEqualTo(1);
        
        FolderInfo f=fldRepo.findById(folder.getId()).get();
        assertThat(f.getFiles().size()).isEqualTo(2);
        
        f.getFiles().forEach(tagService::removeTags);
        fileRepo.deleteAll(f.getFiles());
        
        fldRepo.deleteById(f.getId());
        
        assertThat(tagRepo.count()).isEqualTo(0);
        assertThat(fileRepo.count()).isEqualTo(0);
        assertThat(fldRepo.count()).isEqualTo(0);
        
        
        
    }
    
    @Test
    @Transactional
    public void tegFilesAndExpectThemToBeFound(){
        FolderInfo folder=new FolderInfo();
        folder.setName("testfolder");
        fldRepo.save(folder);
        
        FileInfo f1=new FileInfo();
        f1.setName("file1");
        f1.setFolder(folder);
        fileRepo.save(f1);
        
        FileInfo f2=new FileInfo();
        f2.setName("file2");
        f2.setFolder(folder);
        fileRepo.save(f2);

        FileInfo f3=new FileInfo();
        f3.setName("file3");
        f3.setFolder(folder);
        fileRepo.save(f3);
        
        f1.tag("env", "E");
        f2.tag("env", "E");
        f3.tag("env", "Q").tag("mode", "debug");
        
        tagService.saveTags(f1);
        tagService.saveTags(f2);
        tagService.saveTags(f3);
        
        tagRepo.findAll().forEach(System.out::println);
               
        
        assertThat(tagService.findFilesByTagNameAndValue(Pageable.unpaged(),"env", "E").map(f->f.getName()).getContent())
                .containsExactlyInAnyOrder("file1","file2");
        
        assertThat(tagService.findFilesByTagNameAndValue(Pageable.unpaged(),"mode", "debug").map(f->f.getName()).getContent())
                .containsExactly("file3");
        
        assertThat(tagService.findFilesByTagNameAndValue(Pageable.unpaged(),"env", "X").map(f->f.getName()).getContent())
                .isEmpty();
        
        
               
        
    }
    
}
