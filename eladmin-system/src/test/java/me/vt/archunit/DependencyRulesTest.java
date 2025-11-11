package me.vt.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DependencyRulesTest {

    private final JavaClasses javaClasses = new ClassFileImporter().importPackages("me.vt");

    @Test
    public void no_accesses_to_upper_package() {
        NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES.check(javaClasses);
    }


    /**
     * 2. Controller 只能依赖 service 层 或 common 层
     * - 不允许依赖 repository / dao
     * - 不允许依赖其他模块的 controller
     */
    @Test
    void controllers_should_only_depend_on_service_or_common() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..rest..")
                .and().areAnnotatedWith(Path.class)
                .should().dependOnClassesThat()
                .resideInAnyPackage("..repository..", "..dao..", "..rest.."); // 禁止依赖这些包
        rule.check(javaClasses);
    }

    @Test
    void services_should_only_be_accessed_by_service_or_controller() {
        //        ArchRule rule1 = classes()
        //            .that().resideInAPackage("..service..")
        //            .and().haveSimpleNameNotEndingWith("Test")
        //            .and().haveSimpleNameNotEndingWith("Tests")
        //            .should()
        //            .onlyBeAccessed().byAnyPackage("..rest..", "..service..");

        ArchRule rule = classes()
                .that().resideInAPackage("..service..")
                .should(
                        BaseRules.onlyBeAccessed(clazz -> BaseRules.excludeTests().test(clazz)));
        rule.check(javaClasses);
    }
}
