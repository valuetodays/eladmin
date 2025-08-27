package me.vt.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import lombok.extern.slf4j.Slf4j;
import me.vt.BaseController;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@Slf4j
public class MultipartUsageRulesTest {

    private final JavaClasses javaClasses = new ClassFileImporter().importPackages("me.vt");

    /**
     * MultipartFormDataInput 只能在 Controller 中使用
     */
    @Test
    void multipart_should_only_be_used_in_controllers() {
        ArchRule rule = classes()
            .should(BaseRules.onlyBeUsedIn(
                MultipartFormDataInput.class,
                clazz -> {
                    log.info("simpleName: {}", clazz.getSimpleName());
                    log.info("packageName: {}", clazz.getPackageName());
                    if (clazz.getName().equals(BaseController.class.getName())) {
                        return true;
                    }
                    return clazz.getSimpleName().endsWith("Controller") && clazz.getName().contains(".rest.");
                },
                excludes -> excludes.getName().endsWith("Test")
            ));
        rule.check(javaClasses);
    }
}
