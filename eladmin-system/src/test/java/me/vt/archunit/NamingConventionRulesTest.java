package me.vt.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import me.vt.MyPanacheRepository;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * .
 *
 * @author lei.liu
 * @since 2025-08-26
 */
public class NamingConventionRulesTest {

    private final JavaClasses javaClasses = new ClassFileImporter().importPackages("me.vt");

    @Test
    public void components_should_be_suffixed() {
        classes()
            .that().resideInAPackage("..component..")
            .and().areAnnotatedWith(ApplicationScoped.class)
            .should(BaseRules.haveSimpleNameEndingWithAny("Manager", "Component"))
            .check(javaClasses);
    }

    @Test
    public void services_should_be_suffixed() {
        classes()
            .that().resideInAPackage("..service..")
            .and().areAnnotatedWith(ApplicationScoped.class)
            .should(BaseRules.haveSimpleNameEndingWithAny("Service", "ServiceImpl"))
            .check(javaClasses);
    }

    @Test
    public void controllers_should_not_have_Gui_in_name() {
        classes()
            .that().resideInAPackage("..rest..")
            .should().haveSimpleNameNotContaining("Gui")
            .check(javaClasses);
    }

    @Test
    public void controllers_should_be_suffixed() {
        classes()
            .that().resideInAPackage("..rest..")
            .and().areAnnotatedWith(Path.class)
//            .and().areAssignableTo(AbstractController.class)
            .should().haveSimpleNameEndingWith("Controller")
            .check(javaClasses);
    }

    @Test
    public void dao_should_be_suffixed() {
        classes()
            .that().resideInAPackage("..repository..")
            .and().areAnnotatedWith(ApplicationScoped.class)
            .and().areAssignableTo(MyPanacheRepository.class)
            .should(BaseRules.haveSimpleNameEndingWithAny("Repository"))
            .check(javaClasses);
    }

}
