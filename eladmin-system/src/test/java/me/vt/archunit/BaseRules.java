package me.vt.archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.Arrays;
import java.util.function.Predicate;

public class BaseRules {
    public static ArchCondition<JavaClass> haveSimpleNameEndingWithAny(String... suffixes) {
        return new ArchCondition<JavaClass>("have simple name ending with any of " + Arrays.toString(suffixes)) {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                boolean matches = Arrays.stream(suffixes)
                        .anyMatch(suffix -> item.getSimpleName().endsWith(suffix));

                if (!matches) {
                    String message = String.format("Class %s does not end with any of %s",
                            item.getSimpleName(), Arrays.toString(suffixes));
                    events.add(SimpleConditionEvent.violated(item, message));
                }
            }
        };
    }

    /**
     * 限制指定类型只能出现在符合条件的类中
     *
     * @param targetClass  被限制的目标类，比如 MultipartFormDataInput
     * @param allowed      谓词，定义允许出现的类（如包规则、类名规则）
     */
    public static ArchCondition<JavaClass> onlyBeUsedIn(
            Class<?> targetClass,
            Predicate<JavaClass> allowed) {
        return onlyBeUsedIn(targetClass, allowed, javaClass -> false);
    }

    /**
     * 限制指定类型只能出现在符合条件的类中
     *
     * @param targetClass  被限制的目标类，比如 MultipartFormDataInput
     * @param allowed      谓词，定义允许出现的类（如包规则、类名规则）
     */
    public static ArchCondition<JavaClass> onlyBeUsedIn(
            Class<?> targetClass,
            Predicate<JavaClass> allowed,
            Predicate<JavaClass> excludes) {
        return new ArchCondition<>("only use " + targetClass.getSimpleName() + " in allowed classes") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                if (excludes.test(item)) {
                    return; // 跳过
                }
                boolean dependsOn = item.getDirectDependenciesFromSelf()
                        .stream()
                        .anyMatch(dep -> dep.getTargetClass().isEquivalentTo(targetClass));

                if (dependsOn && !allowed.test(item)) {
                    String message = String.format(
                            "Class %s uses %s but is not in an allowed location",
                            item.getName(), targetClass.getSimpleName());
                    events.add(SimpleConditionEvent.violated(item, message));
                }
            }
        };
    }

    /**
     * 自定义 onlyBeAccessed 条件
     * @param allowed 允许访问的类（包或其他规则）
     * @return ArchCondition
     */
    public static ArchCondition<JavaClass> onlyBeAccessed(Predicate<JavaClass> allowed) {
        return new ArchCondition<>("only be accessed by allowed classes") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                item.getDirectDependenciesFromSelf().forEach(access -> {
                    JavaClass origin = access.getTargetClass();
                    if (!allowed.test(origin)) {
                        String message = String.format(
                                "Class %s is accessed by %s, which is not allowed",
                                item.getName(), origin.getName());
                        events.add(SimpleConditionEvent.violated(item, message));
                    }
                });
            }
        };
    }


    /** ------------------ 通用排除测试类 ------------------ */
    public static Predicate<JavaClass> excludeTests() {
        return clazz -> {
            String name = clazz.getSimpleName();
            String pkg = clazz.getPackageName();
            if (name.endsWith("Test") || name.endsWith("Tests"))
                return false;
            if (pkg.contains(".test.") || pkg.contains(".tests."))
                return false;
            return true;
        };
    }

    public static ArchCondition<JavaClass> exceptTests(ArchCondition<JavaClass> original) {
        return new ArchCondition<JavaClass>(original.getDescription() + " (except test classes)") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                if (excludeTests().test(item)) {
                    original.check(item, events);
                }
            }
        };
    }

}
