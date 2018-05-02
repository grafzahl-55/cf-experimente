package de.escriba.experimental.cf.cdrmongo.model;

import de.escriba.experimental.cf.beans.cdr.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


public class ConvertersTest {


    private Converters converters=new Converters();


    @Test
    public void shouldNotOverwriteOrganizationName(){
        Organization org=new Organization("mu.org","Old Descr").tag("old1","old2","old3");
        OrganizationInfo organizationInfo =new OrganizationInfo("mu.org.new","New descr").tag("new1");
        converters.patch(organizationInfo,org);
        assertThat(org.getName()).isEqualTo("mu.org");
        assertThat(org.getDescription()).isEqualTo("New descr");
        assertThat(org.getTags()).containsExactly("new1");
    }

    @Test
    public void shouldNotOverwriteSpaceName(){
        Space space=new Space("mu.org","Old Descr").tag("old1","old2","old3");
        SpaceInfo spaceInfo =new SpaceInfo("mu.org.new","New descr").tag("new1");
        converters.patch(spaceInfo,space);
        assertThat(space.getName()).isEqualTo("mu.org");
        assertThat(space.getDescription()).isEqualTo("New descr");
        assertThat(space.getTags()).containsExactly("new1");
    }

    @Test
    public void shouldNotOverwriteContainerName(){
        ServiceContainer sc=new ServiceContainer("c1","ruin-time", "C1").tag("a");
        ServiceContainerInfo sci = new ServiceContainerInfo("c23","ruin-time-4");
        sci.setDescription("C1C1");// No tags
        converters.patch(sci,sc);

        assertThat(sc.getName()).isEqualTo("c1");
        assertThat(sc.getServiceType()).isEqualTo("ruin-time-4");
        assertThat(sc.getDescription()).isEqualTo("C1C1");
        assertThat(sc.getTags()).isEmpty();

    }

    @Test
    public void shouldCopyNameDescriptionAndTagsToInfoObject(){
        Organization org=new Organization("o","O").tag("1","2");
        Space spc=new Space("s","S").tag("3","2");

        OrganizationInfo oi=converters.toInfo(org);
        assertThat(oi.getName()).isEqualTo(org.getName());
        assertThat(oi.getDescription()).isEqualTo(org.getDescription());
        assertThat(oi.getTags()).isEqualTo(org.getTags());


    }

    @Test
    public void shouldPutSpaceInfosInExtendedOrgInfo(){
        Organization org=new Organization("o","O").tag("o1","22")
                .addSpace(
                        new Space("s","S").tag("s4")
                                .addContainer(
                                        new ServiceContainer("c1","type1").tag("U","V")
                                )
                );

        ExtendedOrganizationInfo xOrg=converters.toExtendedInfo(org);
        assertThat(xOrg.getName()).isEqualTo(org.getName());
        assertThat(xOrg.getDescription()).isEqualTo(org.getDescription());
        assertThat(xOrg.getTags()).isEqualTo(org.getTags());

        assertThat(xOrg.getSpaces()).isNotNull();
        ExtendedSpaceInfo xSpc=xOrg.getSpaces().get("s");
        assertThat(xSpc).isNotNull();
        assertThat(xSpc.getName()).isEqualTo("s");
        assertThat(xSpc.getDescription()).isEqualTo("S");
        assertThat(xSpc.getTags()).containsExactly("s4");

        assertThat(xSpc.getServiceContainers()).isNotNull();
        ServiceContainerInfo xC=xSpc.getServiceContainers().get("c1");
        assertThat(xC).isNotNull();
        assertThat(xC.getName()).isEqualTo("c1");
        assertThat(xC.getServiceType()).isEqualTo("type1");
        assertThat(xC.getTags()).containsExactlyInAnyOrder("U","V");

    }


}
